package com.apps.wedding.mvvm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.wedding.model.DepartmentModel;
import com.apps.wedding.model.WeddingHallModel;

import java.util.List;

public class FragmentHomeMvvm extends AndroidViewModel {
    private Context context;
    private MutableLiveData<List<WeddingHallModel>> weddingHallModelMutableLiveData;
    private MutableLiveData<List<DepartmentModel>> departmentLivData;

    public FragmentHomeMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }
    public LiveData<List<WeddingHallModel>> getWeddingHall(){
        if (weddingHallModelMutableLiveData==null){
            weddingHallModelMutableLiveData = new MutableLiveData<>();
        }
        return weddingHallModelMutableLiveData;
    }

    public LiveData<List<DepartmentModel>> getCategoryWeddingHall(){
        if (departmentLivData==null){
            departmentLivData = new MutableLiveData<>();
        }
        return departmentLivData;
    }


}
