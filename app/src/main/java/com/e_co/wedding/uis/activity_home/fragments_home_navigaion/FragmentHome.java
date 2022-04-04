package com.e_co.wedding.uis.activity_home.fragments_home_navigaion;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e_co.wedding.R;
import com.e_co.wedding.adapter.RateFilterAdapter;
import com.e_co.wedding.adapter.WeddingHallAdapter;
import com.e_co.wedding.adapter.WeddingHallDepartmentAdapter;
import com.e_co.wedding.databinding.BottomSheetDialogBinding;
import com.e_co.wedding.model.DepartmentModel;
import com.e_co.wedding.model.FilterModel;
import com.e_co.wedding.model.FilterRangeModel;
import com.e_co.wedding.model.FilterRateModel;
import com.e_co.wedding.model.WeddingHallModel;
import com.e_co.wedding.mvvm.FragmentHomeMvvm;
import com.e_co.wedding.uis.activity_base.BaseFragment;
import com.e_co.wedding.databinding.FragmentHomeBinding;
import com.e_co.wedding.uis.activity_home.HomeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class FragmentHome extends BaseFragment {
    private static final String TAG = FragmentHome.class.getName();
    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private FragmentHomeMvvm fragmentHomeMvvm;
    private WeddingHallAdapter weddingHallAdapter;
    private WeddingHallDepartmentAdapter weddingHallDepartmentAdapter;
    private RateFilterAdapter rateFilterAdapter;
    private BottomSheetDialogBinding bottomSheetDialogBinding;
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

                weddingHallAdapter.updateList(weddingHallModels);
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

        fragmentHomeMvvm.getIsFilterRateSelected().observe(activity, aBoolean -> {
            if (bottomSheetDialogBinding != null) {
                if (aBoolean) {
                    bottomSheetDialogBinding.tvClearFilter.setVisibility(View.VISIBLE);

                } else {
                    bottomSheetDialogBinding.tvClearFilter.setVisibility(View.GONE);

                }
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

        binding.llSearch.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.fragmentSearch);
        });

        fragmentHomeMvvm.getDepartment();

    }


    private void createSheetDialog() {


        Currency currency = Currency.getInstance("EGP");
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        bottomSheetDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.bottom_sheet_dialog, null, false);
        dialog.setContentView(bottomSheetDialogBinding.getRoot());

        bottomSheetDialogBinding.slider.setValues(fragmentHomeMvvm.getFilterRangeModel().getValue().getSelectedFromValue(), fragmentHomeMvvm.getFilterRangeModel().getValue().getSelectedToValue());
        bottomSheetDialogBinding.slider.setValueFrom(fragmentHomeMvvm.getFilterRangeModel().getValue().getFromValue());
        bottomSheetDialogBinding.slider.setValueTo(fragmentHomeMvvm.getFilterRangeModel().getValue().getToValue());
        bottomSheetDialogBinding.slider.setStepSize(fragmentHomeMvvm.getFilterRangeModel().getValue().getStepValue());


        bottomSheetDialogBinding.tvFrom.setText(fragmentHomeMvvm.getFilterRangeModel().getValue().getFromValue() + currency.getSymbol());
        bottomSheetDialogBinding.tvTo.setText(fragmentHomeMvvm.getFilterRangeModel().getValue().getToValue() + currency.getSymbol());

        bottomSheetDialogBinding.slider.setLabelFormatter(value -> {
            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setMaximumFractionDigits(0);
            format.setCurrency(currency);
            return format.format(value);
        });

        bottomSheetDialogBinding.imageClose.setOnClickListener(v -> {
            fragmentHomeMvvm.clearFilterModel();
            dialog.dismiss();
        });

        bottomSheetDialogBinding.recView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        rateFilterAdapter = new RateFilterAdapter(activity, this);
        bottomSheetDialogBinding.recView.setAdapter(rateFilterAdapter);

        rateFilterAdapter.updateData(fragmentHomeMvvm.getFilterModelList().getValue(), fragmentHomeMvvm.getFilter().getValue().getRate());

        if (fragmentHomeMvvm.getIsFilterRateSelected().getValue() != null && fragmentHomeMvvm.getIsFilterRateSelected().getValue()) {
            bottomSheetDialogBinding.tvClearFilter.setVisibility(View.VISIBLE);
        } else {
            bottomSheetDialogBinding.tvClearFilter.setVisibility(View.GONE);

        }

        bottomSheetDialogBinding.tvClearFilter.setOnClickListener(v -> {
            fragmentHomeMvvm.getFilterRangeModel().getValue().setSelectedFromValue(FragmentHomeMvvm.startRange);
            fragmentHomeMvvm.getFilterRangeModel().getValue().setSelectedToValue(FragmentHomeMvvm.endRange);
            bottomSheetDialogBinding.slider.setValues(fragmentHomeMvvm.getFilterRangeModel().getValue().getSelectedFromValue(), fragmentHomeMvvm.getFilterRangeModel().getValue().getSelectedToValue());

            if (rateFilterAdapter != null && fragmentHomeMvvm.getFilterModelList().getValue() != null) {

                rateFilterAdapter.updateData(fragmentHomeMvvm.getFilterModelList().getValue(), fragmentHomeMvvm.getFilter().getValue().getRate());
            }
            if (fragmentHomeMvvm.getFilter().getValue() != null) {
                FilterRateModel model = fragmentHomeMvvm.getFilterRateModel().getValue();
                model.setSelected(false);
                model.setTitle(null);
                fragmentHomeMvvm.getFilterRateModel().setValue(model);
            }


            if (fragmentHomeMvvm.getFilter().getValue() != null) {
                FilterModel filterModel = fragmentHomeMvvm.getFilter().getValue();
                filterModel.setRate(null);
                filterModel.setFromRange(String.valueOf(FragmentHomeMvvm.startRange));
                filterModel.setToRange(String.valueOf(FragmentHomeMvvm.endRange));
                fragmentHomeMvvm.getFilter().setValue(filterModel);

            }

            fragmentHomeMvvm.getIsFilterRateSelected().setValue(false);
            fragmentHomeMvvm.getWeddingHallData();
            dialog.dismiss();
        });

        fragmentHomeMvvm.getFilterModelList().observe(activity, filterRateModels -> {
            rateFilterAdapter.updateData(fragmentHomeMvvm.getFilterModelList().getValue(), fragmentHomeMvvm.getFilter().getValue().getRate());

        });


        bottomSheetDialogBinding.btnShowFilterResults.setOnClickListener(v -> {

            float from = bottomSheetDialogBinding.slider.getValues().get(0);
            float to = bottomSheetDialogBinding.slider.getValues().get(1);
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