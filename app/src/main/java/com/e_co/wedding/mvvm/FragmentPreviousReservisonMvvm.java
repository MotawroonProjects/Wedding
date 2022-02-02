package com.e_co.wedding.mvvm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.e_co.wedding.R;
import com.e_co.wedding.model.ReservionDataModel;
import com.e_co.wedding.model.ResevisionModel;
import com.e_co.wedding.model.StatusResponse;
import com.e_co.wedding.model.UserModel;
import com.e_co.wedding.remote.Api;
import com.e_co.wedding.share.Common;
import com.e_co.wedding.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentPreviousReservisonMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentPreviousReservisionMvvm";

    private Context context;
    private MutableLiveData<List<ResevisionModel>> listMutableLiveData;


    private MutableLiveData<Boolean> isLoadingLivData;

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentPreviousReservisonMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<List<ResevisionModel>> getReservionList() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        return listMutableLiveData;
    }


    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }


    //_________________________hitting api_________________________________


    public void getReservionData(UserModel userModel) {
        if (userModel==null){
            isLoadingLivData.setValue(false);
            listMutableLiveData.setValue(new ArrayList<>());

            return;
        }
        isLoadingLivData.postValue(true);


        Api.getService(Tags.base_url)
                .getPreviousReservion("Bearer " + userModel.getData().getToken(), Tags.api_key, userModel.getData().getId() + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new SingleObserver<Response<ReservionDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<ReservionDataModel> response) {
                        isLoadingLivData.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                List<ResevisionModel> list = response.body().getData();
                                listMutableLiveData.setValue(list);
                            }
                        }
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoadingLivData.postValue(false);
                        Log.e(TAG, "onError: ", e);
                    }
                });

    }

    public void addRate(UserModel userModel, String service_id, int rate,Context context) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).addRate("Bearer " + userModel.getData().getToken(), Tags.api_key, userModel.getData().getId() + "", service_id, rate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> statusResponseResponse) {
                        dialog.dismiss();

                        if (statusResponseResponse.isSuccessful()) {
                            if (statusResponseResponse.body() != null) {
                                Log.e("TAGRate", statusResponseResponse.body().getStatus() + "");
                                if (statusResponseResponse.body().getStatus() == 200) {
                                   getReservionData(userModel);
                                }
                            }

                        } else {

                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dialog.dismiss();

                    }
                });


    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
