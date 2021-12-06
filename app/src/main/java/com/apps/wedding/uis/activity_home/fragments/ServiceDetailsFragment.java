package com.apps.wedding.uis.activity_home.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import android.system.Os;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wedding.R;
import com.apps.wedding.databinding.FragmentServiceDetailsBinding;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.uis.activity_home.HomeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.installations.Utils;

import java.net.CookiePolicy;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ServiceDetailsFragment extends BaseFragment {
    private static final String TAG = ServiceDetailsFragment.class.getName();

    private HomeActivity activity;
    private FragmentServiceDetailsBinding binding;
    private ExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private CompositeDisposable disposable = new CompositeDisposable();
    private boolean isInFullScreen = false;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
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


        getVideoImage();
        binding.flVideo.setOnClickListener(v -> {
            isInFullScreen = true;
            binding.motionLayout.transitionToEnd();
            if (player != null) {
                player.setPlayWhenReady(true);
            }


        });

    }

    private void getVideoImage() {
        int microSecond = 6000000;// 6th second as an example
        Uri uri = Uri.parse("https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4");
        RequestOptions options = new RequestOptions().frame(microSecond);


        Glide.with(activity).asBitmap()
                .load(uri)
                .apply(options)
                .into(binding.imageVideo);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupPlayer() {


        trackSelector = new DefaultTrackSelector(activity);
        dataSourceFactory = new DefaultDataSource.Factory(activity);
        MediaSourceFactory mediaSourceFactory = new DefaultMediaSourceFactory(dataSourceFactory);
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse("https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"));

        player = new ExoPlayer.Builder(activity)
                .setTrackSelector(trackSelector)
                .setMediaSourceFactory(mediaSourceFactory)
                .build();

        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(false);
        binding.exoPlayer.setPlayer(player);
        player.prepare();

        binding.exoPlayer.setOnTouchListener((v, event) -> {
            if (player != null && player.isPlaying()) {
                player.setPlayWhenReady(false);
            } else if (player != null && !player.isPlaying()) {

                player.setPlayWhenReady(true);

            }
            return false;
        });

        binding.llMore.setOnClickListener(v -> {
            if (binding.expandedLayout.isExpanded()){
                binding.expandedLayout.collapse(true);
                binding.imageArrow.clearAnimation();
                binding.imageArrow.animate().setDuration(300).rotation(0).start();
            }else {
                binding.expandedLayout.expand(true);
                binding.imageArrow.animate().setDuration(300).rotation(180).start();

            }
        });
        binding.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmReservision();
            }
        });

    }

    public boolean isFullScreen(){
        return isInFullScreen;
    }

    public void setToNormalScreen() {

        isInFullScreen = false;
        binding.motionLayout.transitionToStart();
        if (player != null) {
            player.setPlayWhenReady(false);
        }

    }

    @Override
    public void onResume() {
        if (Util.SDK_INT <= 23 || player == null) {
            setupPlayer();
        }
        super.onResume();


    }

    @Override
    public void onStart() {
        if (Util.SDK_INT > 23) {
            if (player==null){
                setupPlayer();
                binding.exoPlayer.onResume();
            }



        }
        super.onStart();


    }

    @Override
    public void onPause() {
        if (Util.SDK_INT <= 23) {
            if (player != null) {
               player.setPlayWhenReady(false);
            }
        }
        super.onPause();


    }



    @Override
    public void onDestroyView() {
        if (Util.SDK_INT > 23) {
            if (player != null) {
                player.release();
                player = null;
            }

        }
        super.onDestroyView();


        disposable.clear();

    }
    public void confirmReservision() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.reservationConfirmFragment);
    }

}