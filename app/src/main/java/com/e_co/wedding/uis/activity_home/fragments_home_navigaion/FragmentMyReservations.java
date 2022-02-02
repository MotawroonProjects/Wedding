package com.e_co.wedding.uis.activity_home.fragments_home_navigaion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e_co.wedding.R;
import com.e_co.wedding.mvvm.ActivityHomeShareModelMvvm;
import com.e_co.wedding.uis.activity_base.BaseFragment;
import com.e_co.wedding.adapter.MyPagerAdapter;
import com.e_co.wedding.databinding.FragmentMyReservationsBinding;
import com.e_co.wedding.uis.activity_home.HomeActivity;
import com.e_co.wedding.uis.activity_login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentMyReservations extends BaseFragment {
    private FragmentMyReservationsBinding binding;
    private List<String> titles;
    private List<Fragment> fragments;
    private MyPagerAdapter adapter;
    private ActivityResultLauncher<Intent> launcher;
    private int req = 1;
    private HomeActivity activity;
    private ActivityHomeShareModelMvvm activityHomeShareModelMvvm;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                    updateUi();
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_reservations, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        activityHomeShareModelMvvm = ViewModelProviders.of(activity).get(ActivityHomeShareModelMvvm.class);
        activityHomeShareModelMvvm.getIsDataRefreshed().observe(activity, aBoolean -> {
            if (aBoolean) {
                updateData();
            }
        });
        titles = new ArrayList<>();
        fragments = new ArrayList<>();
        updateUi();


        if (getUserModel() == null) {
            Intent intent = new Intent(activity, LoginActivity.class);
            launcher.launch(intent);
        }

    }

    private void updateUi() {
        titles.add(getString(R.string.current));
        titles.add(getString(R.string.prev));
        fragments.add(FragmentCurrentReservation.newInstance());
        fragments.add(FragmentPreviousReservation.newInstance());

        adapter = new MyPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments, titles);
        binding.pager.setAdapter(adapter);
        binding.tab.setupWithViewPager(binding.pager);
        binding.pager.setOffscreenPageLimit(fragments.size());
    }

    public void updateData() {
        if (fragments.get(0) != null) {
            FragmentCurrentReservation fragmentCurrentReservation = (FragmentCurrentReservation) fragments.get(0);
            fragmentCurrentReservation.updateData();
        }

        if (fragments.get(1) != null) {
            FragmentPreviousReservation fragmentPreviousReservation = (FragmentPreviousReservation) fragments.get(1);
            fragmentPreviousReservation.updateData();
        }


    }
}