package com.apps.wedding.uis.activity_home.fragments_home_navigaion;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wedding.R;
import com.apps.wedding.adapter.WeddingHallAdapter;
import com.apps.wedding.adapter.WeddingHallDepartmentAdapter;
import com.apps.wedding.model.WeddingHallModel;
import com.apps.wedding.mvvm.FragmentHomeMvvm;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.databinding.FragmentHomeBinding;
import com.apps.wedding.uis.activity_home.HomeActivity;

import java.util.List;


public class FragmentHome extends BaseFragment {
    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private FragmentHomeMvvm fragmentHomeMvvm;
    private WeddingHallAdapter weddingHallAdapter;
    private WeddingHallDepartmentAdapter weddingHallDepartmentAdapter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        fragmentHomeMvvm = ViewModelProviders.of(this).get(FragmentHomeMvvm.class);
        fragmentHomeMvvm.getWeddingHall().observe(activity, weddingHallModels -> weddingHallAdapter.notifyDataSetChanged());
        fragmentHomeMvvm.getCategoryWeddingHall().observe(activity, weddingHallModels -> weddingHallDepartmentAdapter.notifyDataSetChanged());


        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        weddingHallDepartmentAdapter = new WeddingHallDepartmentAdapter(fragmentHomeMvvm.getCategoryWeddingHall().getValue(), activity);
        binding.recViewCategory.setAdapter(weddingHallDepartmentAdapter);

        binding.recViewHall.setLayoutManager(new LinearLayoutManager(activity));
        weddingHallAdapter = new WeddingHallAdapter(fragmentHomeMvvm.getWeddingHall().getValue(), activity);
        binding.recViewHall.setAdapter(weddingHallAdapter);

    }
}