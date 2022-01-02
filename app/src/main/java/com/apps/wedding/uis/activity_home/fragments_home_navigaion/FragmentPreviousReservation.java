package com.apps.wedding.uis.activity_home.fragments_home_navigaion;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apps.wedding.R;
import com.apps.wedding.adapter.OfferExtraItemsAdapter;
import com.apps.wedding.adapter.PreviousReservionAdapter;
import com.apps.wedding.adapter.ReservionAdapter;
import com.apps.wedding.databinding.BottomSheetRateBinding;
import com.apps.wedding.databinding.BottomSheetServiceDetailsBinding;
import com.apps.wedding.model.NotModel;
import com.apps.wedding.model.ResevisionModel;
import com.apps.wedding.mvvm.FragmentCurrentReservisonMvvm;
import com.apps.wedding.mvvm.FragmentPreviousReservisonMvvm;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.databinding.FragmentCurrentReservationBinding;
import com.apps.wedding.uis.activity_home.HomeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


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
                previousReservionAdapter.updateList(fragmentCurrentReservisonMvvm.getReservionList().getValue());
                binding.cardNoData.setVisibility(View.GONE);

            } else {
                previousReservionAdapter.updateList(null);

                binding.cardNoData.setVisibility(View.VISIBLE);

            }

        });
        binding.swipeRefresh.setOnRefreshListener(() -> {
            fragmentCurrentReservisonMvvm.getReservionData(getUserModel());
        });


        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        getData();
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        previousReservionAdapter = new PreviousReservionAdapter(activity, this);
        binding.recView.setAdapter(previousReservionAdapter);
        fragmentCurrentReservisonMvvm.getReservionData(getUserModel());

        EventBus.getDefault().register(activity);

    }


    private void getData() {
        binding.cardNoData.setVisibility(View.VISIBLE);
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
            fragmentCurrentReservisonMvvm.addRate(getUserModel(), model.getService().getId(), rate);
            dialog.dismiss();

        });
        dialog.show();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewNotificationListener(NotModel model){
        fragmentCurrentReservisonMvvm.getReservionData(getUserModel());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}