package com.apps.wedding.mvvm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.wedding.model.DepartmentModel;
import com.apps.wedding.model.FilterModel;
import com.apps.wedding.model.FilterRangeModel;
import com.apps.wedding.model.FilterRateModel;
import com.apps.wedding.model.WeddingHallModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentHomeMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentHomeMvvm";
    private Context context;
    private MutableLiveData<List<WeddingHallModel>> weddingHallModelMutableLiveData;
    private MutableLiveData<List<DepartmentModel>> departmentLivData;
    private MutableLiveData<List<FilterRateModel>> filterModelListLiveData;
    private MutableLiveData<FilterRangeModel> filterRangeModelLiveData;
    private MutableLiveData<FilterRateModel> filterRateModelMutableLiveData;
    private MutableLiveData<FilterModel> filter;

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentHomeMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<List<WeddingHallModel>> getWeddingHall() {
        if (weddingHallModelMutableLiveData == null) {
            weddingHallModelMutableLiveData = new MutableLiveData<>();
        }
        return weddingHallModelMutableLiveData;
    }

    public LiveData<List<DepartmentModel>> getCategoryWeddingHall() {
        if (departmentLivData == null) {
            departmentLivData = new MutableLiveData<>();
        }
        return departmentLivData;
    }

    public LiveData<FilterRangeModel> getFilterRange() {
        if (filterRangeModelLiveData == null) {
            filterRangeModelLiveData = new MutableLiveData<>();
        }
        return filterRangeModelLiveData;
    }


    public void updateFilterRange(FilterRangeModel model) {
        if (filterRangeModelLiveData == null) {
            filterRangeModelLiveData = new MutableLiveData<>();
        }
        filterRangeModelLiveData.setValue(model);
    }

    public LiveData<FilterRateModel> getFilterRateModel() {
        if (filterRateModelMutableLiveData == null) {
            filterRateModelMutableLiveData = new MutableLiveData<>();
        }
        return filterRateModelMutableLiveData;
    }

    public void updateFilterRateModel(FilterRateModel model) {
        if (filterRateModelMutableLiveData == null) {
            filterRateModelMutableLiveData = new MutableLiveData<>();
        }

        filterRateModelMutableLiveData.setValue(model);
    }

    public MutableLiveData<FilterModel> getFilter() {
        if (filter == null) {
            filter = new MutableLiveData<>();
        }
        return filter;
    }

    @SuppressLint("CheckResult")
    public LiveData<List<FilterRateModel>> getFilterModelList() {
        if (filterModelListLiveData == null) {
            filterModelListLiveData = new MutableLiveData<>();
            List<FilterRateModel> list = new ArrayList<>();
            for (int x = 1; x < 6; x++) {
                list.add(new FilterRateModel(String.valueOf(x)));
            }
            Observable.fromArray(list)
                    .filter(list1 -> {
                        FilterRateModel model = list1.get(0);
                        model.setSelected(true);
                        list1.set(0, model);
                        return true;
                    })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listData -> {
                        filterModelListLiveData.setValue(listData);
                    }, error -> {
                        Log.e(TAG, "getFilterModelList: ", error);
                    });
            FilterRateModel model = list.get(0);
            updateFilterRateModel(model);

        }
        return filterModelListLiveData;
    }

    public void clearFilterModel() {
        filterModelListLiveData = null;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
