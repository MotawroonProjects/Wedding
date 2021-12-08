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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apps.wedding.R;
import com.apps.wedding.adapter.MapWeddingHallAdapter;
import com.apps.wedding.adapter.RateFilterAdapter;
import com.apps.wedding.adapter.WeddingHallAdapter;
import com.apps.wedding.databinding.BottomSheetDialogBinding;
import com.apps.wedding.model.FilterModel;
import com.apps.wedding.model.FilterRangeModel;
import com.apps.wedding.model.FilterRateModel;
import com.apps.wedding.model.LocationModel;
import com.apps.wedding.model.WeddingHallModel;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
    private MapWeddingHallAdapter adapter;
    private RateFilterAdapter rateFilterAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();

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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Observable.timer(130, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        initView();

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void initView() {
        fragmentNearMvvm = ViewModelProviders.of(this).get(FragmentNearMvvm.class);
        fragmentNearMvvm.setContext(activity);
        fragmentNearMvvm.getWeddingHall().observe(activity, weddingHallModels -> adapter.updateList(fragmentNearMvvm.getWeddingHall().getValue()));

        fragmentNearMvvm.getLocationData().observe(activity, locationModel -> {
          //  addMarker(locationModel.getLat(), locationModel.getLng());
        });
        fragmentNearMvvm.getIsLoading().observe(activity, isLoading -> {
            if (isLoading) {
               // binding.cardNoData.setVisibility(View.GONE);

            }
        });
        fragmentNearMvvm.getWeddingHallData();
        fragmentNearMvvm.getWeddingHall().observe(activity, weddingHallModels -> {
            if (weddingHallModels.size() > 0) {
                adapter.updateList(fragmentNearMvvm.getWeddingHall().getValue());
                updateMapData(weddingHallModels);
              //  binding.cardNoData.setVisibility(View.GONE);
            } else {
                //binding.cardNoData.setVisibility(View.VISIBLE);

            }

        });


        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.recView);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        adapter = new MapWeddingHallAdapter(activity, this);
        binding.recView.setAdapter(adapter);
        binding.cardFilter.setOnClickListener(v -> {
            createSheetDialog();
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
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.map, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);


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
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            fragmentNearMvvm.startLocationUpdate();

        }
    }

    public void setItemWeddingDetails(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("data", id);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.serviceDetailsFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
    }

    private void createSheetDialog() {


        Currency currency = Currency.getInstance("EGP");
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        BottomSheetDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.bottom_sheet_dialog, null, false);
        dialog.setContentView(binding.getRoot());

        binding.slider.setValues(fragmentNearMvvm.getFilterRangeModel().getValue().getSelectedFromValue(), fragmentNearMvvm.getFilterRangeModel().getValue().getSelectedToValue());
        binding.slider.setValueFrom(fragmentNearMvvm.getFilterRangeModel().getValue().getFromValue());
        binding.slider.setValueTo(fragmentNearMvvm.getFilterRangeModel().getValue().getToValue());
        binding.slider.setStepSize(fragmentNearMvvm.getFilterRangeModel().getValue().getStepValue());


        binding.tvFrom.setText(fragmentNearMvvm.getFilterRangeModel().getValue().getFromValue() + currency.getSymbol());
        binding.tvTo.setText(fragmentNearMvvm.getFilterRangeModel().getValue().getToValue() + currency.getSymbol());

        binding.slider.setLabelFormatter(value -> {
            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setMaximumFractionDigits(0);
            format.setCurrency(currency);
            return format.format(value);
        });

        binding.imageClose.setOnClickListener(v -> {
            fragmentNearMvvm.clearFilterModel();
            dialog.dismiss();
        });

        binding.recView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        rateFilterAdapter = new RateFilterAdapter(activity, this);
        binding.recView.setAdapter(rateFilterAdapter);
        rateFilterAdapter.updateData(fragmentNearMvvm.getFilterModelList().getValue(), fragmentNearMvvm.getFilter().getValue().getRate());


        fragmentNearMvvm.getFilterModelList().observe(activity, filterRateModels -> {
            rateFilterAdapter.updateData(fragmentNearMvvm.getFilterModelList().getValue(), fragmentNearMvvm.getFilter().getValue().getRate());

        });

        binding.btnShowFilterResults.setOnClickListener(v -> {

            float from = binding.slider.getValues().get(0);
            float to = binding.slider.getValues().get(1);
            FilterRangeModel filterRangeModel = fragmentNearMvvm.getFilterRangeModel().getValue();
            filterRangeModel.setSelectedFromValue(from);
            filterRangeModel.setSelectedToValue(to);
            fragmentNearMvvm.getFilterRangeModel().setValue(filterRangeModel);


            FilterModel filterModel = fragmentNearMvvm.getFilter().getValue();
            filterModel.setRate(fragmentNearMvvm.getFilterRateModel().getValue().getTitle());
            filterModel.setFromRange(filterRangeModel.getSelectedFromValue() + "");
            filterModel.setToRange(filterRangeModel.getSelectedToValue() + "");
            fragmentNearMvvm.getFilter().setValue(filterModel);

            fragmentNearMvvm.getWeddingHallData();
            fragmentNearMvvm.clearFilterModel();

            dialog.dismiss();
        });
        dialog.show();
    }

    public void updateFilterRate(FilterRateModel model) {
        fragmentNearMvvm.updateFilterRateModel(model);
    }

    private void updateMapData(List<WeddingHallModel> data) {

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        for (WeddingHallModel weddingHallModel:data)
        {
            bounds.include(new LatLng(Double.parseDouble(weddingHallModel.getLatitude()),Double.parseDouble(weddingHallModel.getLongitude())));
            addMarker(Double.parseDouble(weddingHallModel.getLatitude()),Double.parseDouble(weddingHallModel.getLongitude()));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),20));


    }


}