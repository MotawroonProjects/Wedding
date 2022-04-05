package com.e_co.wedding.mvvm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.e_co.wedding.R;
import com.e_co.wedding.model.DepartmentDataModel;
import com.e_co.wedding.model.DepartmentModel;
import com.e_co.wedding.model.FilterModel;
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

public class FragmentHomeMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentHomeMvvm";
    public static float startRange = 0.0f;
    public static float endRange = 100000.0f;
    public static float steps = 500.0f;
    private String defaultRate = null;
    private Context context;
    private MutableLiveData<List<WeddingHallModel>> weddingHallModelMutableLiveData;
    private MutableLiveData<List<DepartmentModel>> departmentLivData;
    private MutableLiveData<List<FilterRateModel>> rateList;

    private MutableLiveData<FilterModel> filter;
    private MutableLiveData<Boolean> isLoadingLivData;

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


    public MutableLiveData<List<FilterRateModel>> getRateListData() {
        if (rateList == null) {
            rateList = new MutableLiveData<>();
            List<FilterRateModel> list = new ArrayList<>();
            for (int x = 1; x < 6; x++) {
                FilterRateModel model = new FilterRateModel(String.valueOf(x));
                FilterModel filterModel = getFilter().getValue();
                if (filterModel != null && filterModel.getRate() != null && filterModel.getRate().equals(model.getTitle())) {
                    model.setSelected(true);
                }
                list.add(model);
            }
            rateList.setValue(list);

        } else {
            List<FilterRateModel> list = rateList.getValue();
            FilterModel filterModel = getFilter().getValue();
            if (list != null && filterModel != null) {
                for (int x = 0; x < list.size(); x++) {
                    FilterRateModel model = list.get(x);
                    if (filterModel.getRate() != null && filterModel.getRate().equals(model.getTitle())) {
                        model.setSelected(true);
                        list.set(x, model);
                    } else {
                        model.setSelected(false);
                    }
                }
                rateList.setValue(list);

            }

        }
        return rateList;
    }


    public MutableLiveData<FilterModel> getFilter() {
        if (filter == null) {
            filter = new MutableLiveData<>();
            FilterModel filterModel = new FilterModel(null, startRange + "", endRange + "", null);
            filter.setValue(filterModel);
        }
        return filter;
    }


    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    //_________________________hitting api_________________________________

    public void getDepartment() {
        isLoadingLivData.postValue(true);
        Api.getService(Tags.base_url)
                .getDepartments(Tags.api_key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new SingleObserver<Response<DepartmentDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<DepartmentDataModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                List<DepartmentModel> list = response.body().getData();
                                if (list.size() > 0) {
                                    list.add(0, new DepartmentModel(null, context.getString(R.string.all), true, ""));
                                    departmentLivData.setValue(list);
                                    getWeddingHallData();
                                }


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

    public void getWeddingHallData() {
        isLoadingLivData.postValue(true);

        filter = getFilter();
        Log.e("cat_id", filter.getValue().getCategory_id() + "rate" + filter.getValue().getRate() + "from" + filter.getValue().getFromRange() + "to" + filter.getValue().getToRange());


        Api.getService(Tags.base_url)
                .getWeddingHall(Tags.api_key, filter.getValue().getCategory_id(), filter.getValue().getRate(), filter.getValue().getFromRange(), filter.getValue().getToRange())
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
