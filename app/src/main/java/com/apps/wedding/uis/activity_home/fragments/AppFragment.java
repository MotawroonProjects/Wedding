package com.apps.wedding.uis.activity_home.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wedding.R;
import com.apps.wedding.databinding.FragmentAppBinding;
import com.apps.wedding.uis.activity_base.BaseFragment;

public class AppFragment extends BaseFragment {
    private FragmentAppBinding binding;
    private String type;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_app, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        Log.e("type",type);
    }
}