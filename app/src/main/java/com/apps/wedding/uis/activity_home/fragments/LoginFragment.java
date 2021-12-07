package com.apps.wedding.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wedding.R;
import com.apps.wedding.databinding.FragmentLoginBinding;
import com.apps.wedding.model.LoginModel;
import com.apps.wedding.model.UserModel;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.uis.activity_home.HomeActivity;

public class LoginFragment extends BaseFragment {
    private FragmentLoginBinding binding;
    private HomeActivity activity;
    private LoginModel model;
    private boolean login;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        model = new LoginModel();
        binding.setModel(model);


        binding.btnLogin.setOnClickListener(v -> {
            if (model.isDataValid(activity)) {
                displayFragmentVerificationCode();
            }
        });
    }

    private void displayFragmentVerificationCode() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", model);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.verificationcodeFragment, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("ldldldl","sssss");

        NavBackStackEntry currentBackStackEntry = Navigation.findNavController(binding.getRoot()).getCurrentBackStackEntry();
        if (currentBackStackEntry!=null){
            SavedStateHandle savedStateHandle = currentBackStackEntry.getSavedStateHandle();
            if(savedStateHandle!=null&&savedStateHandle.contains("data")){
            login = savedStateHandle.get("data");
            if (login) {
                Navigation.findNavController(binding.getRoot()).getPreviousBackStackEntry().getSavedStateHandle().set("data", true);
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        }}


    }
}