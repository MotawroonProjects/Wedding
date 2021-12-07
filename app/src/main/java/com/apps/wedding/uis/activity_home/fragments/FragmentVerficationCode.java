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
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import com.apps.wedding.R;
import com.apps.wedding.databinding.FragmentVerificationCodeBinding;
import com.apps.wedding.model.LoginModel;
import com.apps.wedding.mvvm.FragmentVerificationMvvm;
import com.apps.wedding.preferences.Preferences;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.uis.activity_home.HomeActivity;

import java.util.Objects;

public class FragmentVerficationCode extends BaseFragment {
    private FragmentVerificationCodeBinding binding;
    private HomeActivity activity;
    private LoginModel model;
    private Preferences preferences;
    private FragmentVerificationMvvm fragmentVerificationMvvm;
    private boolean login;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verification_code, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        preferences=Preferences.getInstance();
        model = (LoginModel) getArguments().getSerializable("data");
        fragmentVerificationMvvm = ViewModelProviders.of(this).get(FragmentVerificationMvvm.class);
        fragmentVerificationMvvm.sendSmsCode(getLang(), model.getPhone_code(), model.getPhone(), activity);
        fragmentVerificationMvvm.smscode.observe(activity, smsCode -> {
            binding.edtCode.setText(smsCode);
        });
        fragmentVerificationMvvm.canresnd.observe(activity, canresnd -> {
            if (canresnd) {
                binding.tvResend.setVisibility(View.VISIBLE);
                binding.tvResend.setEnabled(true);
            } else {
                binding.tvResend.setVisibility(View.GONE);

                binding.tvResend.setEnabled(false);
            }
        });
        fragmentVerificationMvvm.timereturn.observe(activity, time -> {
            binding.tvCounter.setText(time);
        });
        fragmentVerificationMvvm.userModelMutableLiveData.observe(activity, userModel -> {
            preferences.createUpdateUserData(activity,userModel);
            Objects.requireNonNull(Navigation.findNavController(binding.getRoot()).getPreviousBackStackEntry()).getSavedStateHandle().set("data", true);
            Navigation.findNavController(binding.getRoot()).popBackStack();
        });
    fragmentVerificationMvvm.found.observe(activity, new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if(s!=null) {
//Log.e("d;ldkkdkkd","sllslslsl");
                Bundle bundle = new Bundle();
                bundle.putString("phone_code", model.getPhone_code());
                bundle.putString("phone", model.getPhone());

                Navigation.findNavController(binding.getRoot()).navigate(R.id.signupFragment, bundle);
            }
        }
    });
        binding.tvResend.setOnClickListener(view -> fragmentVerificationMvvm.sendSmsCode(getLang(), model.getPhone_code(), model.getPhone(), activity));
        binding.setPhone(model.getPhone_code() + model.getPhone());


        binding.btnConfirm.setOnClickListener(view -> {
            String smscode = binding.edtCode.getText().toString();
            if (!smscode.isEmpty()) {
                fragmentVerificationMvvm.checkValidCode(smscode, activity);
            } else {
                binding.edtCode.setError(getResources().getString(R.string.field_required));
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("ldldldl","dlkdkdk");
        NavBackStackEntry currentBackStackEntry = Navigation.findNavController(binding.getRoot()).getCurrentBackStackEntry();
        if (currentBackStackEntry!=null){
            SavedStateHandle savedStateHandle = currentBackStackEntry.getSavedStateHandle();
            if(savedStateHandle.contains("data")){
                Log.e("ldldldl","نينيننين");
                login = savedStateHandle.get("data");
                if (login) {
                    Navigation.findNavController(binding.getRoot()).getPreviousBackStackEntry().getSavedStateHandle().set("data", true);
                    Navigation.findNavController(binding.getRoot()).popBackStack();
                }
            }}


    }

}