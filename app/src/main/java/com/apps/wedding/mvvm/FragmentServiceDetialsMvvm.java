package com.apps.wedding.mvvm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.wedding.model.DepartmentDataModel;
import com.apps.wedding.model.DepartmentModel;
import com.apps.wedding.model.FilterModel;
import com.apps.wedding.model.FilterRangeModel;
import com.apps.wedding.model.FilterRateModel;
import com.apps.wedding.model.SingleWeddingHallDataModel;
import com.apps.wedding.model.WeddingHallDataModel;
import com.apps.wedding.model.WeddingHallModel;
import com.apps.wedding.remote.Api;
import com.apps.wedding.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentServiceDetialsMvvm extends AndroidViewModel {
    private String TAG = "FragmentServiceDetialsMvvm";
    private Context context;

    private Timer timer;
    private TimerTask timerTask;
    private MutableLiveData<SingleWeddingHallDataModel> singleWeddingHallDataModelMutableLiveData;
    private MutableLiveData<Boolean> isLoadingLivData;
    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    public MutableLiveData<SingleWeddingHallDataModel> getSingleWedding() {
        if (singleWeddingHallDataModelMutableLiveData == null) {
            singleWeddingHallDataModelMutableLiveData = new MutableLiveData<>();
        }
        return singleWeddingHallDataModelMutableLiveData;
    }

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentServiceDetialsMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }


    public void getSingleWeddingHallData() {
        isLoadingLivData.postValue(true);
        Api.getService(Tags.base_url)
                .getSingleWeddingHall(Tags.api_key, "11")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new SingleObserver<Response<SingleWeddingHallDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleWeddingHallDataModel> response) {
                        isLoadingLivData.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                singleWeddingHallDataModelMutableLiveData.postValue(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoadingLivData.postValue(false);
                        Log.e(TAG, "onError: ", e);
                    }
                });

    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
