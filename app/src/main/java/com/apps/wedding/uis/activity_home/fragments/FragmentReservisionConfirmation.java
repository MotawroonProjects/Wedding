package com.apps.wedding.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apps.wedding.R;
import com.apps.wedding.adapter.BaiscItemAdapter;
import com.apps.wedding.adapter.WeddingHallAdapter;
import com.apps.wedding.databinding.FragmentLoginBinding;
import com.apps.wedding.databinding.FragmentReservationConfirmationBinding;
import com.apps.wedding.model.LoginModel;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.uis.activity_home.HomeActivity;

public class FragmentReservisionConfirmation extends BaseFragment {
    private FragmentReservationConfirmationBinding binding;
    private HomeActivity activity;
    private BaiscItemAdapter baiscItemAdapter;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reservation_confirmation, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        binding.recViewBaiscItem.setLayoutManager(new LinearLayoutManager(activity));
        baiscItemAdapter = new BaiscItemAdapter(activity,this);
        binding.recViewBaiscItem.setAdapter(baiscItemAdapter);
    }


}