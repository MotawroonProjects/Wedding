package com.apps.wedding.uis.activity_home.fragments_home_navigaion;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wedding.R;
import com.apps.wedding.adapter.RateFilterAdapter;
import com.apps.wedding.adapter.WeddingHallAdapter;
import com.apps.wedding.adapter.WeddingHallDepartmentAdapter;
import com.apps.wedding.databinding.BottomSheetDialogBinding;
import com.apps.wedding.model.DepartmentModel;
import com.apps.wedding.model.FilterModel;
import com.apps.wedding.model.FilterRangeModel;
import com.apps.wedding.model.FilterRateModel;
import com.apps.wedding.mvvm.FragmentHomeMvvm;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.databinding.FragmentHomeBinding;
import com.apps.wedding.uis.activity_home.HomeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class FragmentHome extends BaseFragment {
    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private FragmentHomeMvvm fragmentHomeMvvm;
    private WeddingHallAdapter weddingHallAdapter;
    private WeddingHallDepartmentAdapter weddingHallDepartmentAdapter;
    private RateFilterAdapter rateFilterAdapter;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
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
        fragmentHomeMvvm = ViewModelProviders.of(this).get(FragmentHomeMvvm.class);
        fragmentHomeMvvm.getIsLoading().observe(activity, isLoading -> {
            if (isLoading) {
                binding.cardNoData.setVisibility(View.GONE);


            }
            binding.swipeRefresh.setRefreshing(isLoading);
        });

        fragmentHomeMvvm.getWeddingHall().observe(activity, weddingHallModels -> {
            if (weddingHallModels.size() > 0) {
                weddingHallAdapter.updateList(fragmentHomeMvvm.getWeddingHall().getValue());
                binding.cardNoData.setVisibility(View.GONE);

            } else {
                weddingHallAdapter.updateList(null);

                binding.cardNoData.setVisibility(View.VISIBLE);

            }

        });
        fragmentHomeMvvm.getCategoryWeddingHall().observe(activity, weddingHallModels -> {
            if (weddingHallModels.size() > 0) {
                weddingHallDepartmentAdapter.updateList(fragmentHomeMvvm.getCategoryWeddingHall().getValue());
                binding.cardNoData.setVisibility(View.GONE);
            } else {
                binding.cardNoData.setVisibility(View.VISIBLE);

            }
        });


        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        weddingHallDepartmentAdapter = new WeddingHallDepartmentAdapter(activity, this);
        binding.recViewCategory.setAdapter(weddingHallDepartmentAdapter);

        binding.recViewHall.setLayoutManager(new LinearLayoutManager(activity));
        weddingHallAdapter = new WeddingHallAdapter(activity, this);
        binding.recViewHall.setAdapter(weddingHallAdapter);
        binding.imageFilter.setOnClickListener(v -> {
            createSheetDialog();
        });
        binding.swipeRefresh.setOnRefreshListener(() -> {
            fragmentHomeMvvm.getDepartment();
        });

        fragmentHomeMvvm.getDepartment();

    }


    private void createSheetDialog() {


        Currency currency = Currency.getInstance("EGP");
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        BottomSheetDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.bottom_sheet_dialog, null, false);
        dialog.setContentView(binding.getRoot());

        binding.slider.setValues(fragmentHomeMvvm.getFilterRangeModel().getValue().getSelectedFromValue(), fragmentHomeMvvm.getFilterRangeModel().getValue().getSelectedToValue());
        binding.slider.setValueFrom(fragmentHomeMvvm.getFilterRangeModel().getValue().getFromValue());
        binding.slider.setValueTo(fragmentHomeMvvm.getFilterRangeModel().getValue().getToValue());
        binding.slider.setStepSize(fragmentHomeMvvm.getFilterRangeModel().getValue().getStepValue());


        binding.tvFrom.setText(fragmentHomeMvvm.getFilterRangeModel().getValue().getFromValue() + currency.getSymbol());
        binding.tvTo.setText(fragmentHomeMvvm.getFilterRangeModel().getValue().getToValue() + currency.getSymbol());

        binding.slider.setLabelFormatter(value -> {
            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setMaximumFractionDigits(0);
            format.setCurrency(currency);
            return format.format(value);
        });

        binding.imageClose.setOnClickListener(v -> {
            fragmentHomeMvvm.clearFilterModel();
            dialog.dismiss();
        });

        binding.recView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        rateFilterAdapter = new RateFilterAdapter(activity, this);
        binding.recView.setAdapter(rateFilterAdapter);
        rateFilterAdapter.updateData(fragmentHomeMvvm.getFilterModelList().getValue(), fragmentHomeMvvm.getFilter().getValue().getRate());


        fragmentHomeMvvm.getFilterModelList().observe(activity, filterRateModels -> {
            rateFilterAdapter.updateData(fragmentHomeMvvm.getFilterModelList().getValue(), fragmentHomeMvvm.getFilter().getValue().getRate());

        });

        binding.btnShowFilterResults.setOnClickListener(v -> {

            float from = binding.slider.getValues().get(0);
            float to = binding.slider.getValues().get(1);
            FilterRangeModel filterRangeModel = fragmentHomeMvvm.getFilterRangeModel().getValue();
            filterRangeModel.setSelectedFromValue(from);
            filterRangeModel.setSelectedToValue(to);
            fragmentHomeMvvm.getFilterRangeModel().setValue(filterRangeModel);


            FilterModel filterModel = fragmentHomeMvvm.getFilter().getValue();
            filterModel.setRate(fragmentHomeMvvm.getFilterRateModel().getValue().getTitle());
            filterModel.setFromRange(filterRangeModel.getSelectedFromValue() + "");
            filterModel.setToRange(filterRangeModel.getSelectedToValue() + "");
            fragmentHomeMvvm.getFilter().setValue(filterModel);

            fragmentHomeMvvm.getWeddingHallData();
            fragmentHomeMvvm.clearFilterModel();

            dialog.dismiss();
        });
        dialog.show();
    }

    public void updateFilterRate(FilterRateModel model) {
        fragmentHomeMvvm.updateFilterRateModel(model);
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

    public void setItemDepartment(DepartmentModel model) {
        FilterModel filterModel = fragmentHomeMvvm.getFilter().getValue();
        filterModel.setCategory_id(model.getId());
        fragmentHomeMvvm.getWeddingHallData();
    }
}