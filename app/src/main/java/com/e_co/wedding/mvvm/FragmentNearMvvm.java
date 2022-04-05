package com.e_co.wedding.mvvm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.e_co.wedding.model.DepartmentModel;
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
    public static float startRange = 0.0f;
    public static float endRange = 100000.0f;
    public static float steps = 500.0f;
    private String defaultRate = null;
    private Context context;


    private MutableLiveData<List<WeddingHallModel>> weddingHallModelMutableLiveData;
    private MutableLiveData<List<FilterRateModel>> rateList;

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
