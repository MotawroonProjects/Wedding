package com.e_co.wedding.uis.activity_home.fragments_home_navigaion;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e_co.wedding.R;
import com.e_co.wedding.adapter.MapWeddingHallAdapter;
import com.e_co.wedding.adapter.RateFilterAdapter;
import com.e_co.wedding.databinding.BottomSheetDialogBinding;
import com.e_co.wedding.model.FilterModel;
import com.e_co.wedding.model.FilterRangeModel;
import com.e_co.wedding.model.FilterRateModel;
import com.e_co.wedding.model.WeddingHallModel;
import com.e_co.wedding.mvvm.FragmentHomeMvvm;
import com.e_co.wedding.mvvm.FragmentNearMvvm;
import com.e_co.wedding.uis.activity_base.BaseFragment;
import com.e_co.wedding.databinding.FragmentNearbyBinding;
import com.e_co.wedding.uis.activity_home.HomeActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class FragmentNearby extends BaseFragment implements OnMapReadyCallback {
    private HomeActivity activity;
    private FragmentNearbyBinding binding;
    private GoogleMap mMap;
    private float zoom = 15.0f;
    private ActivityResultLauncher<String> permissionLauncher;
    private FragmentNearMvvm fragmentNearMvvm;
    private BottomSheetDialogBinding bottomSheetDialogBinding;
    private MapWeddingHallAdapter adapter;
    private RateFilterAdapter rateFilterAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String rateNum = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

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


        try {
            fragmentNearMvvm.getWeddingHall().observe(activity, weddingHallModels -> {
                if (adapter != null && weddingHallModels != null) {
                    adapter.updateList(fragmentNearMvvm.getWeddingHall().getValue());
                }
            });

            fragmentNearMvvm.getIsLoading().observe(activity, isLoading -> {
                if (isLoading) {
                    binding.flLoading.setClickable(true);
                    binding.flLoading.setFocusable(true);
                    binding.progBar.setVisibility(View.VISIBLE);
                    binding.flLoading.setVisibility(View.VISIBLE);
                    binding.cardNoData.setVisibility(View.GONE);
                    if (adapter != null) {
                        adapter.updateList(null);

                    }

                }
            });

            fragmentNearMvvm.getWeddingHall().observe(activity, weddingHallModels -> {
                if (weddingHallModels.size() > 0) {
                    if (fragmentNearMvvm.getWeddingHall() != null && fragmentNearMvvm.getWeddingHall().getValue() != null) {
                        if (adapter != null) {
                            adapter.updateList(fragmentNearMvvm.getWeddingHall().getValue());
                            updateMapData(weddingHallModels);
                            binding.cardNoData.setVisibility(View.GONE);
                            binding.flLoading.setVisibility(View.GONE);
                        }

                    }


                } else {
                    binding.flLoading.setVisibility(View.VISIBLE);
                    binding.progBar.setVisibility(View.GONE);
                    binding.cardNoData.setVisibility(View.VISIBLE);
                    adapter.updateList(null);
                    mMap.clear();
                    binding.flLoading.setClickable(false);
                    binding.flLoading.setFocusable(false);
                }

            });


        } catch (Exception e) {

        }


        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.recView);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        adapter = new MapWeddingHallAdapter(activity, this);
        binding.recView.setAdapter(adapter);
        binding.cardFilter.setOnClickListener(v -> {
            createSheetDialog();
        });

        binding.cardViewSearch.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.fragmentSearch);
        });

        updateUI();
    }


    private void updateUI() {
        try {
            SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map, supportMapFragment).commitAllowingStateLoss();
            supportMapFragment.getMapAsync(this);

        }catch (Exception e){}



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            fragmentNearMvvm.getWeddingHallData();

        }
    }

    private void addMarker(double lat, double lng) {
        if (mMap != null) {
            try {
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            } catch (Exception e) {

            }
        }

    }


    public void setItemWeddingDetails(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("data", id);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.serviceDetailsFragment, bundle);
    }


    private void createSheetDialog() {
        Currency currency = Currency.getInstance("EGP");
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        bottomSheetDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.bottom_sheet_dialog, null, false);
        dialog.setContentView(bottomSheetDialogBinding.getRoot());

        FilterModel filterModel = fragmentNearMvvm.getFilter().getValue();


        bottomSheetDialogBinding.slider.setValues(Float.parseFloat(filterModel.getFromRange()), Float.parseFloat(filterModel.getToRange()));
        bottomSheetDialogBinding.slider.setValueFrom(FragmentHomeMvvm.startRange);
        bottomSheetDialogBinding.slider.setValueTo(FragmentHomeMvvm.endRange);
        bottomSheetDialogBinding.slider.setStepSize(FragmentHomeMvvm.steps);


        bottomSheetDialogBinding.tvFrom.setText(filterModel.getFromRange() + currency.getSymbol());
        bottomSheetDialogBinding.tvTo.setText(filterModel.getToRange() + currency.getSymbol());

        bottomSheetDialogBinding.slider.setLabelFormatter(value -> {
            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setMaximumFractionDigits(0);
            format.setCurrency(currency);
            return format.format(value);
        });

        bottomSheetDialogBinding.imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

        bottomSheetDialogBinding.recView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        rateFilterAdapter = new RateFilterAdapter(activity, this);
        bottomSheetDialogBinding.recView.setAdapter(rateFilterAdapter);

        rateFilterAdapter.updateData(fragmentNearMvvm.getRateListData().getValue());

        if (filterModel.getRate() != null) {

            bottomSheetDialogBinding.btnClear.setVisibility(View.VISIBLE);
        } else {
            if (!filterModel.getFromRange().equals(String.valueOf(FragmentHomeMvvm.startRange)) ||
                    !filterModel.getToRange().equals(String.valueOf(FragmentHomeMvvm.endRange))) {
                bottomSheetDialogBinding.btnClear.setVisibility(View.VISIBLE);

            } else {
                bottomSheetDialogBinding.btnClear.setVisibility(View.GONE);

            }

        }

        bottomSheetDialogBinding.btnClear.setOnClickListener(v -> {
            filterModel.setRate(null);
            filterModel.setCategory_id(null);
            filterModel.setFromRange(String.valueOf(FragmentHomeMvvm.startRange));
            filterModel.setToRange(String.valueOf(FragmentHomeMvvm.endRange));


            bottomSheetDialogBinding.slider.setValues(FragmentHomeMvvm.startRange, FragmentHomeMvvm.endRange);

            if (rateFilterAdapter != null) {

                rateFilterAdapter.updateData(fragmentNearMvvm.getRateListData().getValue());
            }

            fragmentNearMvvm.getWeddingHallData();
            fragmentNearMvvm.getFilter().setValue(filterModel);
            dialog.dismiss();
        });


        bottomSheetDialogBinding.btnShowFilterResults.setOnClickListener(v -> {
            filterModel.setRate(rateNum);
            filterModel.setFromRange(bottomSheetDialogBinding.slider.getValues().get(0) + "");
            filterModel.setToRange(bottomSheetDialogBinding.slider.getValues().get(1) + "");

            fragmentNearMvvm.getFilter().setValue(filterModel);
            fragmentNearMvvm.getWeddingHallData();

            dialog.dismiss();
        });
        dialog.show();
    }

    public void updateFilterRate(FilterRateModel model) {
        rateNum = model.getTitle();
    }

    private void updateMapData(List<WeddingHallModel> data) {

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        for (WeddingHallModel weddingHallModel : data) {
            bounds.include(new LatLng(Double.parseDouble(weddingHallModel.getLatitude()), Double.parseDouble(weddingHallModel.getLongitude())));
            addMarker(Double.parseDouble(weddingHallModel.getLatitude()), Double.parseDouble(weddingHallModel.getLongitude()));
        }

        if (mMap != null) {
            try {
                if (data.size() >= 2) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100));

                } else if (data.size() == 1) {
                    LatLng latLng = new LatLng(Double.parseDouble(data.get(0).getLatitude()), Double.parseDouble(data.get(0).getLongitude()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

                }
            } catch (Exception e) {
            }
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
    }
}