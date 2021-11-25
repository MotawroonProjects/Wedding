package com.apps.wedding.uis.activity_home.fragments_home_navigaion;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wedding.R;
import com.apps.wedding.databinding.FragmentServiceDetailsBinding;
import com.apps.wedding.uis.activity_base.BaseFragment;

public class ServiceDetailsFragment extends BaseFragment {

    private FragmentServiceDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_details, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {
        binding.setLang(getLang());

    }


}