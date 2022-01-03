package com.apps.wedding.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wedding.model.NotificationDataModel;
import com.apps.wedding.model.NotificationModel;
import com.apps.wedding.model.UserModel;
import com.apps.wedding.remote.Api;
import com.apps.wedding.tags.Tags;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityHomeShareModelMvvm extends AndroidViewModel {
    private static final String TAG = "ActivityHomeShareMvvm";
    private Context context;

    private MutableLiveData<Boolean> isDataRefreshed;

    private CompositeDisposable disposable = new CompositeDisposable();


    public ActivityHomeShareModelMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }




    public MutableLiveData<Boolean> getIsDataRefreshed() {
        if (isDataRefreshed == null) {
            isDataRefreshed = new MutableLiveData<>();
        }
        return isDataRefreshed;
    }

    public void setRefresh(boolean value){
        isDataRefreshed.setValue(value);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

    }

}
