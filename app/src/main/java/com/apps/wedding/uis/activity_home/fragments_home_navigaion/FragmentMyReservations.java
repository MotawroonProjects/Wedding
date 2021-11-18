package com.apps.wedding.uis.activity_home.fragments_home_navigaion;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wedding.R;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.adapter.MyPagerAdapter;
import com.apps.wedding.databinding.FragmentMyReservationsBinding;

import java.util.ArrayList;
import java.util.List;

public class FragmentMyReservations extends BaseFragment {
    private FragmentMyReservationsBinding binding;
    private List<String> titles;
    private List<Fragment> fragments;
    private MyPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_reservations, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        titles = new ArrayList<>();
        fragments = new ArrayList<>();

        titles.add(getString(R.string.current));
        titles.add(getString(R.string.prev));
        fragments.add(FragmentCurrentReservation.newInstance());
        fragments.add(FragmentCurrentReservation.newInstance());

        adapter = new MyPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments, titles);
        binding.pager.setAdapter(adapter);
        binding.tab.setupWithViewPager(binding.pager);
        binding.pager.setOffscreenPageLimit(fragments.size());
    }
}