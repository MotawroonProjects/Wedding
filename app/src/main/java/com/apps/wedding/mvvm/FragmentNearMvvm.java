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
import com.apps.wedding.model.LocationModel;
import com.apps.wedding.model.PlaceGeocodeData;
import com.apps.wedding.model.WeddingHallModel;
import com.apps.wedding.remote.Api;
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
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentNearMvvm extends AndroidViewModel implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "FragmentNearMvvm";
    private Context context;
    private HomeActivity activity;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private MutableLiveData<LocationModel> locationModelMutableLiveData;
    private MutableLiveData<GoogleMap> mMap;
    private MutableLiveData<List<WeddingHallModel>> weddingHallModelMutableLiveData;

    private MutableLiveData<Boolean> isProgressUpdating;

    private CompositeDisposable disposable = new CompositeDisposable();


    public FragmentNearMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
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
}
