package com.apps.wedding.uis.activity_home.fragments_home_navigaion;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wedding.R;
import com.apps.wedding.model.UserModel;
import com.apps.wedding.mvvm.FragmentEditProfileMvvm;
import com.apps.wedding.mvvm.FragmentProfileMvvm;
import com.apps.wedding.preferences.Preferences;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.databinding.FragmentProfileBinding;
import com.apps.wedding.uis.activity_home.HomeActivity;

import java.util.List;


public class FragmentProfile extends BaseFragment {
    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private boolean login;
    private FragmentProfileMvvm fragmentProfileMvvm;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        fragmentProfileMvvm = ViewModelProviders.of(this).get(FragmentProfileMvvm.class);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        if (userModel != null) {
            binding.setModel(userModel);
        }
        binding.setLang(getLang());
        fragmentProfileMvvm.logout.observe(activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    logout();
                }
            }
        });
        binding.llContactUs.setOnClickListener(v -> {

            Navigation.findNavController(v).navigate(R.id.activity_contact_us);
        });
        binding.llAbout.setOnClickListener(v -> {

            navigateToFragmentApp(v, "about");
        });
        binding.llPrivacy.setOnClickListener(v -> {
            navigateToFragmentApp(v, "policy");
        });
        binding.llTerms.setOnClickListener(v -> {
            navigateToFragmentApp(v, "terms");
        });

        binding.llRate.setOnClickListener(v -> rateApp());
        binding.llProfile.setOnClickListener(view -> {
                    if (userModel == null) {
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.loginFragment);
                    } else {
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.editProfileFragment);

                        // Log.e("dlld",userModel.getData().getName());
                    }
                }
        );
        binding.llEditProfile.setOnClickListener(view -> {
                    if (userModel == null) {
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.loginFragment);
                    } else {
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.editProfileFragment);

                        // Log.e("dlld",userModel.getData().getName());
                    }
                }
        );
        binding.lllogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel == null) {
                    logout();
                } else {
                    fragmentProfileMvvm.logout(activity, userModel);
                }

            }
        });
    }

    private void logout() {
        preferences.createUpdateUserData(activity, null);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.loginFragment);
    }

    private void rateApp() {
        String appId = activity.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        final List<ResolveInfo> otherApps = activity.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                rateIntent.setComponent(componentName);
                startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appId));
            startActivity(webIntent);
        }

    }

    private void navigateToFragmentApp(View v, String type) {
        Bundle bundle = new Bundle();
        bundle.putString("data", type);
        Navigation.findNavController(v).navigate(R.id.appFragment, bundle);

    }

    @Override
    public void onResume() {
        super.onResume();
        NavBackStackEntry currentBackStackEntry = Navigation.findNavController(binding.getRoot()).getCurrentBackStackEntry();
        if (currentBackStackEntry != null) {
            SavedStateHandle savedStateHandle = currentBackStackEntry.getSavedStateHandle();
            if (savedStateHandle.contains("data")) {
                login = savedStateHandle.get("data");
                if (login) {
                    userModel = preferences.getUserData(activity);
                    binding.setModel(userModel);
                    //activity.updatefirebase();
                }
            }
        }
    }
}