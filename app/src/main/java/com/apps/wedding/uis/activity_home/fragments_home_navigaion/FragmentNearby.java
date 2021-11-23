package com.apps.wedding.uis.activity_home.fragments_home_navigaion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apps.wedding.R;
import com.apps.wedding.model.LocationModel;
import com.apps.wedding.mvvm.FragmentNearMvvm;
import com.apps.wedding.uis.activity_base.BaseActivity;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.databinding.FragmentNearbyBinding;
import com.apps.wedding.uis.activity_home.HomeActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentNearby extends BaseFragment implements OnMapReadyCallback {
    private HomeActivity activity;
    private FragmentNearbyBinding binding;
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.0f;
    private ActivityResultLauncher<String> permissionLauncher;
    private FragmentNearMvvm fragmentNearMvvm;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                fragmentNearMvvm.initGoogleApi();

            } else {
                Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nearby, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        fragmentNearMvvm = ViewModelProviders.of(this).get(FragmentNearMvvm.class);
        fragmentNearMvvm.getLocationData().observe(activity, locationModel -> {
            addMarker(locationModel.getLat(), locationModel.getLng());
        });

        updateUI();
        checkPermission();
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(activity, BaseActivity.fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(BaseActivity.fineLocPerm);
        } else {

            fragmentNearMvvm.initGoogleApi();
        }
    }


    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            if (fragmentNearMvvm.getGoogleMap().getValue() == null) {
                fragmentNearMvvm.setmMap(mMap);

            }

        }
    }

    private void addMarker(double lat, double lng) {
        if (fragmentNearMvvm.getGoogleMap().getValue() != null) {
            mMap = fragmentNearMvvm.getGoogleMap().getValue();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            fragmentNearMvvm.startLocationUpdate();

        }
    }

}