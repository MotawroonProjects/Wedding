package com.e_co.wedding.mvvm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.e_co.wedding.model.FilterModel;
import com.e_co.wedding.model.FilterRangeModel;
import com.e_co.wedding.model.FilterRateModel;
import com.e_co.wedding.model.WeddingHallDataModel;
import com.e_co.wedding.model.WeddingHallModel;
import com.e_co.wedding.remote.Api;
import com.e_co.wedding.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentNearMvvm extends AndroidViewModel  {
    private static final String TAG = "FragmentNearMvvm";
    private float startRange = 0.0f;
    private float endRange = 100000.0f;
    private float steps = 500.0f;
    private String defaultRate = null;
    private Context context;


    private MutableLiveData<List<WeddingHallModel>> weddingHallModelMutableLiveData;
    private MutableLiveData<List<FilterRateModel>> filterModelListLiveData;
    private MutableLiveData<FilterRateModel> filterRateModelMutableLiveData;
    private MutableLiveData<FilterRangeModel> filterRangeModelMutableLiveData;
    private MutableLiveData<FilterModel> filter;
    private MutableLiveData<Boolean> isLoadingLivData;

    private CompositeDisposable disposable = new CompositeDisposable();


    public FragmentNearMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }




    public LiveData<List<WeddingHallModel>> getWeddingHall() {
        if (weddingHallModelMutableLiveData == null) {
            weddingHallModelMutableLiveData = new MutableLiveData<>();
        }
        return weddingHallModelMutableLiveData;
    }


    public MutableLiveData<FilterRangeModel> getFilterRangeModel() {
        if (filterRangeModelMutableLiveData == null) {
            filterRangeModelMutableLiveData = new MutableLiveData<>();
            FilterRangeModel model = new FilterRangeModel(startRange, endRange, steps);
            model.setSelectedFromValue(startRange);
            model.setSelectedToValue(endRange);
            filterRangeModelMutableLiveData.setValue(model);
        }
        return filterRangeModelMutableLiveData;
    }


    public LiveData<FilterRateModel> getFilterRateModel() {
        if (filterRateModelMutableLiveData == null) {
            filterRateModelMutableLiveData = new MutableLiveData<>();
            String rate = defaultRate;
            if (getFilter() != null && getFilter().getValue() != null) {
                rate = getFilter().getValue().getRate();
            }
            FilterRateModel model = new FilterRateModel(rate);
            filterRateModelMutableLiveData.setValue(model);
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
            FilterModel filterModel = new FilterModel(null, getFilterRangeModel().getValue().getSelectedFromValue() + "", getFilterRangeModel().getValue().getSelectedToValue() + "", getFilterRateModel().getValue().getTitle());
            filter.setValue(filterModel);
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
                        int pos = -1;
                        for (int index = 0; index < list1.size(); index++) {
                            if (list1.get(index).getTitle().equals(getFilter().getValue().getRate())) {
                                pos = index;
                                break;
                            }
                        }
                        if (pos != -1) {
                            FilterRateModel model = list1.get(pos);
                            model.setSelected(true);
                            list1.set(pos, model);
                        }

                        return true;
                    })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listData -> {
                        filterModelListLiveData.setValue(listData);
                    }, error -> {
                        Log.e(TAG, "getFilterModelList: ", error);
                    });


        }
        return filterModelListLiveData;
    }


    public void clearFilterModel() {
        filterModelListLiveData = null;
    }


    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    //_________________________hitting api_________________________________

    public void getWeddingHallData() {
        isLoadingLivData.postValue(true);

        filter = getFilter();

        Log.e("sada",filter.getValue().getRate()+"__"+filter.getValue().getFromRange()+"__"+filter.getValue().getToRange());
        Api.getService(Tags.base_url)
                .getWeddingHall(Tags.api_key, null, filter.getValue().getRate(), filter.getValue().getFromRange(), filter.getValue().getToRange())
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
                                Log.e("size",list.size()+"");
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
