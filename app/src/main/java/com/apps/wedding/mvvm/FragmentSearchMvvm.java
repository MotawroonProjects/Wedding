package com.apps.wedding.mvvm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.wedding.model.FilterModel;
import com.apps.wedding.model.FilterRangeModel;
import com.apps.wedding.model.FilterRateModel;
import com.apps.wedding.model.WeddingHallDataModel;
import com.apps.wedding.model.WeddingHallModel;
import com.apps.wedding.remote.Api;
import com.apps.wedding.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentSearchMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentSearchMvvm";
    private Context context;

    private MutableLiveData<List<WeddingHallModel>> weddingHallModelMutableLiveData;
    private MutableLiveData<Boolean> isLoadingLivData;

    private CompositeDisposable disposable = new CompositeDisposable();


    public FragmentSearchMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }


    public LiveData<List<WeddingHallModel>> getWeddingHall() {
        if (weddingHallModelMutableLiveData == null) {
            weddingHallModelMutableLiveData = new MutableLiveData<>();
        }
        return weddingHallModelMutableLiveData;
    }


    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    //_________________________hitting api_________________________________

    public void getWeddingHallData(String name) {
        isLoadingLivData.postValue(true);


        Api.getService(Tags.base_url)
                .getSearchWeddingHall(Tags.api_key, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new SingleObserver<Response<WeddingHallDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<WeddingHallDataModel> response) {
                        isLoadingLivData.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                List<WeddingHallModel> list = response.body().getData();
                                weddingHallModelMutableLiveData.setValue(list);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoadingLivData.setValue(false);
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
