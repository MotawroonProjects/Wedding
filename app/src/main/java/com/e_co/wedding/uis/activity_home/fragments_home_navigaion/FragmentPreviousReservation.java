package com.e_co.wedding.uis.activity_home.fragments_home_navigaion;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.e_co.wedding.R;
import com.e_co.wedding.adapter.OfferExtraItemsAdapter;
import com.e_co.wedding.adapter.PreviousReservionAdapter;
import com.e_co.wedding.databinding.BottomSheetRateBinding;
import com.e_co.wedding.databinding.BottomSheetServiceDetailsBinding;
import com.e_co.wedding.model.ResevisionModel;
import com.e_co.wedding.mvvm.FragmentPreviousReservisonMvvm;
import com.e_co.wedding.uis.activity_base.BaseFragment;
import com.e_co.wedding.databinding.FragmentCurrentReservationBinding;
import com.e_co.wedding.uis.activity_home.HomeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class FragmentPreviousReservation extends BaseFragment {
    private FragmentCurrentReservationBinding binding;
    private FragmentPreviousReservisonMvvm fragmentCurrentReservisonMvvm;
    private PreviousReservionAdapter previousReservionAdapter;
    private HomeActivity activity;

    public static FragmentPreviousReservation newInstance() {
        FragmentPreviousReservation fragment = new FragmentPreviousReservation();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_reservation, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        fragmentCurrentReservisonMvvm = ViewModelProviders.of(this).get(FragmentPreviousReservisonMvvm.class);
        fragmentCurrentReservisonMvvm.getIsLoading().observe(activity, isLoading -> {
            if (isLoading) {
                binding.cardNoData.setVisibility(View.GONE);


            }
            binding.swipeRefresh.setRefreshing(isLoading);
        });

        fragmentCurrentReservisonMvvm.getReservionList().observe(activity, weddingHallModels -> {
            if (weddingHallModels.size() > 0) {
                if (previousReservionAdapter != null) {
                    previousReservionAdapter.updateList(fragmentCurrentReservisonMvvm.getReservionList().getValue());
                }
                binding.cardNoData.setVisibility(View.GONE);


            } else {
                if (previousReservionAdapter != null) {
                    previousReservionAdapter.updateList(null);

                }

                binding.cardNoData.setVisibility(View.VISIBLE);

            }

        });
        binding.swipeRefresh.setOnRefreshListener(() -> {
            fragmentCurrentReservisonMvvm.getReservionData(getUserModel());
        });


        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        previousReservionAdapter = new PreviousReservionAdapter(activity, this);
        binding.recView.setAdapter(previousReservionAdapter);
        fragmentCurrentReservisonMvvm.getReservionData(getUserModel());


    }


    public void createSheetDialog(ResevisionModel model) {
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        BottomSheetServiceDetailsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.bottom_sheet_service_details, null, false);
        dialog.setContentView(binding.getRoot());
        binding.setModel(model);
        double total = model.getPrice() + model.getExtra_item_price();
        binding.setTotal(String.valueOf(total));
        StringBuilder details;
        details = new StringBuilder(model.getService().getText());

        binding.setDetails(details.toString());
        if (model.getReservation_extra_items() != null) {
            binding.recView.setLayoutManager(new GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false));
            OfferExtraItemsAdapter adapter = new OfferExtraItemsAdapter(activity);
            binding.recView.setAdapter(adapter);
            adapter.updateList(model.getReservation_extra_items());
        }

        binding.btnChange.setVisibility(View.GONE);
        binding.imageQrCode.setVisibility(View.GONE);

        binding.imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });


        dialog.show();

    }

    public void createRateSheetDialog(ResevisionModel model) {
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        BottomSheetRateBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.bottom_sheet_rate, null, false);
        dialog.setContentView(binding.getRoot());
        binding.rateBar.setOnRatingBarChangeListener((simpleRatingBar, rating, fromUser) -> {
            int rate = (int) rating;
            binding.tvRate.setText(rate + "");
        });
        binding.imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

        binding.btnRate.setOnClickListener(view -> {
            int rate = (int) binding.rateBar.getRating();
            fragmentCurrentReservisonMvvm.addRate(getUserModel(), model.getService().getId(), rate, activity);
            dialog.dismiss();

        });
        dialog.show();

    }

    public void updateData() {
        fragmentCurrentReservisonMvvm.getReservionData(getUserModel());

    }
}