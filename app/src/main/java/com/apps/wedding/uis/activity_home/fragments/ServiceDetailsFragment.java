package com.apps.wedding.uis.activity_home.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wedding.R;
import com.apps.wedding.databinding.FragmentServiceDetailsBinding;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.uis.activity_home.HomeActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
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

import java.net.CookiePolicy;

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
    private HttpDataSource httpDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private CompositeDisposable disposable = new CompositeDisposable();
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_details, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {

        setupPlayer();


    }

    private void setupPlayer() {
        Observable.empty()
                .flatMap(o -> {
                    Observable observable = Observable.empty();
                    observable.subscribeOn(Schedulers.io());
                    observable.observeOn(Schedulers.io());
                    observable.subscribe(new Observer() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(@NonNull Object o) {

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            Log.e("d","f");
                            trackSelector = new DefaultTrackSelector(activity);
                            dataSourceFactory = new DefaultDataSource.Factory(activity);
                            MediaItem mediaItem = MediaItem.fromUri(Uri.parse("https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"));
                            MediaSourceFactory mediaSourceFactory = new DefaultMediaSourceFactory(dataSourceFactory);

                            player = new ExoPlayer.Builder(activity)
                                    .setTrackSelector(trackSelector)
                                    .setMediaSourceFactory(mediaSourceFactory)
                                    .build();
                            player.setMediaItem(mediaItem);
                            player.setPlayWhenReady(true);
                            player.prepare();

                        }
                    });
                    return observable;

                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Object o) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        binding.exoPlayer.setPlayer(player);
                    }
                });






    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();

    }
}