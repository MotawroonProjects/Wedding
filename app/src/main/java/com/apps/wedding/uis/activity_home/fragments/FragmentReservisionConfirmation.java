package com.apps.wedding.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apps.wedding.R;
import com.apps.wedding.adapter.AdditionalItemAdapter;
import com.apps.wedding.adapter.BaiscItemAdapter;
import com.apps.wedding.adapter.WeddingHallAdapter;
import com.apps.wedding.databinding.FragmentLoginBinding;
import com.apps.wedding.databinding.FragmentReservationConfirmationBinding;
import com.apps.wedding.model.LoginModel;
import com.apps.wedding.model.UserModel;
import com.apps.wedding.preferences.Preferences;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.uis.activity_home.HomeActivity;

public class FragmentReservisionConfirmation extends BaseFragment {
    private FragmentReservationConfirmationBinding binding;
    private HomeActivity activity;
    private BaiscItemAdapter baiscItemAdapter;
    private AdditionalItemAdapter additionalItemAdapter;
    private UserModel userModel;
    private Preferences preferences;
    private boolean login;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reservation_confirmation, container, false);
        return binding.getRoot();

    }

    private void initView() {
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(activity);
        binding.recViewBaiscItem.setLayoutManager(new LinearLayoutManager(activity));
        baiscItemAdapter = new BaiscItemAdapter(activity, this);
        binding.recViewBaiscItem.setAdapter(baiscItemAdapter);
        binding.recViewAdditionalItem.setLayoutManager(new LinearLayoutManager(activity));
        additionalItemAdapter = new AdditionalItemAdapter(activity, this);
        binding.recViewAdditionalItem.setAdapter(additionalItemAdapter);

    }
    @Override
    public void onResume() {
        super.onResume();
        NavBackStackEntry currentBackStackEntry = Navigation.findNavController(binding.getRoot()).getCurrentBackStackEntry();
        if (currentBackStackEntry!=null){
            SavedStateHandle savedStateHandle = currentBackStackEntry.getSavedStateHandle();
            if(savedStateHandle.contains("data")){
                login = savedStateHandle.get("data");
                if (login) {
                    userModel=preferences.getUserData(activity);
                    Log.e("dldll",userModel.getData().getName());
                }
            }}



    }

}