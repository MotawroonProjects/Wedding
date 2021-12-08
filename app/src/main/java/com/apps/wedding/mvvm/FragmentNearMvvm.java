package com.apps.wedding.mvvm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.wedding.R;
import com.apps.wedding.model.DepartmentModel;
import com.apps.wedding.model.FilterModel;
import com.apps.wedding.model.FilterRangeModel;
import com.apps.wedding.model.FilterRateModel;
import com.apps.wedding.model.LocationModel;
import com.apps.wedding.model.PlaceGeocodeData;
import com.apps.wedding.model.WeddingHallDataModel;
import com.apps.wedding.model.WeddingHallModel;
import com.apps.wedding.remote.Api;
import com.apps.wedding.tags.Tags;
import com.apps.wedding.uis.activity_home.HomeActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentNearMvvm extends AndroidViewModel implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "FragmentNearMvvm";
    private float startRange = 0.0f;
    private float endRange = 100000.0f;
    private float steps = 500.0f;
    private String defaultRate ="1";
    private Context context;
    private HomeActivity activity;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private MutableLiveData<LocationModel> locationModelMutableLiveData;
    private MutableLiveData<GoogleMap> mMap;
    private MutableLiveData<List<WeddingHallModel>> weddingHallModelMutableLiveData;
    private MutableLiveData<List<FilterRateModel>> filterModelListLiveData;
    private MutableLiveData<FilterRateModel> filterRateModelMutableLiveData;
    private MutableLiveData<FilterRangeModel> filterRangeModelMutableLiveData;
    private MutableLiveData<Boolean> isLoadingLivData;

    private MutableLiveData<FilterModel> filter;
    private MutableLiveData<Boolean> isProgressUpdating;

    private CompositeDisposable disposable = new CompositeDisposable();


    public FragmentNearMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }
    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    public void setContext(Context context){
        activity = (HomeActivity) context;
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
                        int pos = 0;
                        for (int index = 0; index < list1.size(); index++) {
                            if (list1.get(index).getTitle().equals(getFilter().getValue().getRate())) {
                                pos = index;
                                break;
                            }
                        }
                        FilterRateModel model = list1.get(pos);
                        model.setSelected(true);
                        list1.set(pos, model);
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

    public LiveData<LocationModel> getLocationData() {
        if (locationModelMutableLiveData == null) {
            locationModelMutableLiveData = new MutableLiveData<>();
        }

        return locationModelMutableLiveData;
    }



    public LiveData<GoogleMap> getGoogleMap() {
        if (mMap == null) {
            mMap = new MutableLiveData<>();
        }

        return mMap;
    }




    public void setmMap(GoogleMap googleMap) {
        mMap.setValue(googleMap);
    }


    public LiveData<Boolean> getProgressStatus() {
        if (isProgressUpdating == null) {
            isProgressUpdating = new MutableLiveData<>();
        }
        return isProgressUpdating;
    }

    public void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(locationSettingsResult -> {
            Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    startLocationUpdate();
                    break;

                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(activity, 100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    public void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(activity)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        LocationModel locationModel = new LocationModel(lat, lng);
        locationModelMutableLiveData.setValue(locationModel);

        if (googleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        if (googleApiClient != null) {
            if (locationCallback != null) {
                LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);
                googleApiClient.disconnect();
                googleApiClient = null;
            }
        }
    }
    public void getWeddingHallData() {
        isLoadingLivData.postValue(true);

        filter = getFilter();
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

}
