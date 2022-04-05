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
    private String rateNum = null;

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

        FilterModel filterModel = fragmentHomeMvvm.getFilter().getValue();


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

        rateFilterAdapter.updateData(fragmentHomeMvvm.getRateListData().getValue());

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

                rateFilterAdapter.updateData(fragmentHomeMvvm.getRateListData().getValue());
            }

            fragmentHomeMvvm.getWeddingHallData();
            fragmentHomeMvvm.getFilter().setValue(filterModel);
            dialog.dismiss();
        });


        bottomSheetDialogBinding.btnShowFilterResults.setOnClickListener(v -> {
            filterModel.setRate(rateNum);
            filterModel.setFromRange(bottomSheetDialogBinding.slider.getValues().get(0) + "");
            filterModel.setToRange(bottomSheetDialogBinding.slider.getValues().get(1) + "");

            fragmentHomeMvvm.getFilter().setValue(filterModel);
            fragmentHomeMvvm.getWeddingHallData();

            dialog.dismiss();
        });
        dialog.show();
    }

    public void updateFilterRate(FilterRateModel model) {
        rateNum = model.getTitle();

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