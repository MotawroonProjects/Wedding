package com.e_co.wedding.mvvm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.disposables.CompositeDisposable;

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
