package com.apps.wedding.uis.activity_home.fragments_home_navigaion;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wedding.R;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.databinding.FragmentProfileBinding;


public class FragmentProfile extends BaseFragment {

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        binding.setLang(getLang());
        binding.llContactUs.setOnClickListener(v -> {

            Navigation.findNavController(v).navigate(R.id.activity_contact_us);
        });
        binding.llAbout.setOnClickListener(v -> {

            navigateToFragmentApp(v,"about");
        });
        binding.llPrivacy.setOnClickListener(v -> {
            navigateToFragmentApp(v,"policy");
        });
        binding.llTerms.setOnClickListener(v -> {
            navigateToFragmentApp(v,"terms");
        });
    }

    private void navigateToFragmentApp(View v ,String type) {
        Bundle bundle = new Bundle();
        bundle.putString("data",type);
        Navigation.findNavController(v).navigate(R.id.appFragment,bundle);

    }
}