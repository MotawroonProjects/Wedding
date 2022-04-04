package com.e_co.wedding.mvvm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.e_co.wedding.model.SingleWeddingHallDataModel;
import com.e_co.wedding.model.WeddingHallDataModel;
import com.e_co.wedding.model.WeddingHallModel;
import com.e_co.wedding.remote.Api;
import com.e_co.wedding.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentServiceDetialsMvvm extends AndroidViewModel {
    private String TAG = "FragmentServiceDetialsMvvm";
    private Context context;

    private Timer timer;
    private TimerTask timerTask;
    private MutableLiveData<SingleWeddingHallDataModel> singleWeddingHallDataModelMutableLiveData;
    private MutableLiveData<Boolean> isLoadingLivData;

    private MutableLiveData<Boolean> isLoadingAnotherServicesData;

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }


    public MutableLiveData<Boolean> getIsLoadingAnotherServicesData() {
        if (isLoadingAnotherServicesData == null) {
            isLoadingAnotherServicesData = new MutableLiveData<>();
        }
        return isLoadingAnotherServicesData;
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


    public void getSingleWeddingHallData(String id) {
        isLoadingLivData.postValue(true);
        Api.getService(Tags.base_url)
                .getSingleWeddingHall(Tags.api_key, id)
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
                                getAnotherService(response.body().getData().getUser_id(),id);
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

    private void getAnotherService(String user_id, String service_id) {

    }
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
