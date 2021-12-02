package com.apps.wedding.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wedding.R;
import com.apps.wedding.databinding.FragmentLoginBinding;
import com.apps.wedding.model.LoginModel;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.uis.activity_home.HomeActivity;

public class LoginFragment extends BaseFragment {
    private FragmentLoginBinding binding;
    private HomeActivity activity;
    private LoginModel model;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        model = new LoginModel();
        binding.setModel(model);


        binding.btnLogin.setOnClickListener(v -> {
            if (model.isDataValid(activity)){
                displayFragmentVerificationCode();
            }
        });
    }

    private void displayFragmentVerificationCode() {


    }
}