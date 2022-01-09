package com.e_co.wedding.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.e_co.wedding.R;
import com.e_co.wedding.model.RequestServiceModel;
import com.e_co.wedding.model.StatusResponse;
import com.e_co.wedding.model.UserModel;
import com.e_co.wedding.remote.Api;
import com.e_co.wedding.share.Common;
import com.e_co.wedding.tags.Tags;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentChooseDayMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentChooseDayMvvm";
    private Context context;

    public MutableLiveData<Boolean> onSuccess = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentChooseDayMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();


    }


    public void reserve(Context context, RequestServiceModel requestServiceModel, UserModel userModel, String date, String day, String offer_id) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).reserve("Bearer " + userModel.getData().getToken(), Tags.api_key, requestServiceModel.getWeddingHallModel().getId(), userModel.getData().getId() + "", date, day, requestServiceModel.getIds(), offer_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new SingleObserver<Response<StatusResponse>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull Response<StatusResponse> statusResponseResponse) {
                dialog.dismiss();

                if (statusResponseResponse.isSuccessful()) {
                    if (statusResponseResponse.body() != null) {
                        Log.e(TAG, statusResponseResponse.body().getStatus() + "");
                        if (statusResponseResponse.body().getStatus() == 200) {
                            onSuccess.setValue(true);

                        } else if (statusResponseResponse.body().getStatus() == 412) {
                            Toast.makeText(context, R.string.cnt_book, Toast.LENGTH_SHORT).show();
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
