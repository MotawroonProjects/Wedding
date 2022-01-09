package com.e_co.wedding.uis.activity_home.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.e_co.wedding.R;
import com.e_co.wedding.adapter.OfferAdapter;
import com.e_co.wedding.adapter.SliderAdapter;
import com.e_co.wedding.databinding.FragmentServiceDetailsBinding;
import com.e_co.wedding.model.SingleWeddingHallDataModel;
import com.e_co.wedding.model.WeddingHallModel;
import com.e_co.wedding.mvvm.FragmentServiceDetialsMvvm;
import com.e_co.wedding.uis.activity_base.BaseFragment;
import com.e_co.wedding.uis.activity_home.HomeActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ServiceDetailsFragment extends BaseFragment {
    private static final String TAG = ServiceDetailsFragment.class.getName();
    private FragmentServiceDetialsMvvm fragmentServiceDetialsMvvm;
    private HomeActivity activity;
    private FragmentServiceDetailsBinding binding;
    private ExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private CompositeDisposable disposable = new CompositeDisposable();
    private boolean isInFullScreen = false;
    private Timer timer;
    private TimerTask timerTask;
    private SliderAdapter sliderAdapter;
    private SingleWeddingHallDataModel singleWeddingHallDataModel;
    private String service_id = "";
    private OfferAdapter offerAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            service_id = bundle.getString("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Observable.timer(130, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        initView();

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void initView() {
        fragmentServiceDetialsMvvm = ViewModelProviders.of(this).get(FragmentServiceDetialsMvvm.class);
        fragmentServiceDetialsMvvm.getIsLoading().observe(activity, isLoading -> {
            if (isLoading) {
                binding.progBar.setVisibility(View.VISIBLE);
                binding.nested.setVisibility(View.GONE);

            } else {
                binding.progBar.setVisibility(View.GONE);
                binding.nested.setVisibility(View.VISIBLE);
            }

        });

        binding.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                binding.progBarVideo.setVisibility(View.GONE);

            }


        });


        offerAdapter = new OfferAdapter(activity, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.recView);
        binding.recView.setAdapter(offerAdapter);

        fragmentServiceDetialsMvvm.getSingleWedding().observe(activity, s -> {
            singleWeddingHallDataModel = s;
            binding.setModel(singleWeddingHallDataModel.getData());
            if (singleWeddingHallDataModel != null && singleWeddingHallDataModel.getData() != null) {
                if (singleWeddingHallDataModel.getData().getService_images() != null && singleWeddingHallDataModel.getData().getService_images().size() > 0) {
                    sliderAdapter = new SliderAdapter(singleWeddingHallDataModel.getData().getService_images(), activity);
                    binding.pager.setAdapter(sliderAdapter);
                    binding.tab.setupWithViewPager(binding.pager);
                    if (singleWeddingHallDataModel.getData().getService_images().size() > 1) {
                        timer = new Timer();
                        timerTask = new MyTask();
                        timer.scheduleAtFixedRate(timerTask, 6000, 6000);
                    }
                }
                if (singleWeddingHallDataModel.getData().getVideo_link() != null) {
                    binding.webView.loadUrl(singleWeddingHallDataModel.getData().getVideo_link());
                }

                if (singleWeddingHallDataModel.getData().getOffer() != null && singleWeddingHallDataModel.getData().getOffer().size() > 0) {

                    offerAdapter.updateList(singleWeddingHallDataModel.getData().getOffer());
                    Log.e("sdsa", "sdfs");
                } else {
                    Log.e("tt", "tt");

                }

            }
        });
        fragmentServiceDetialsMvvm.getSingleWeddingHallData(service_id);


        binding.btnBook.setOnClickListener(view -> confirmReservation());

    }


    @Override
    public void onPause() {
        super.onPause();
        binding.webView.onPause();


    }


    public void confirmReservation() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", singleWeddingHallDataModel);
        bundle.putSerializable("data2", null);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.reservationConfirmFragment, bundle);
    }

    public void book(WeddingHallModel.OfferModel offerModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", singleWeddingHallDataModel);
        bundle.putSerializable("data2", offerModel);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.reservationConfirmFragment, bundle);
    }

    public class MyTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                int current_page = binding.pager.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);

                }
            });

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }


        disposable.clear();


    }
}