package com.e_co.wedding.uis.activity_home.fragments_home_navigaion;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e_co.wedding.R;
import com.e_co.wedding.mvvm.FragmentProfileMvvm;
import com.e_co.wedding.uis.activity_base.BaseFragment;
import com.e_co.wedding.databinding.FragmentProfileBinding;
import com.e_co.wedding.uis.activity_home.HomeActivity;
import com.e_co.wedding.uis.activity_language.LanguageActivity;
import com.e_co.wedding.uis.activity_login.LoginActivity;

import java.util.List;


public class FragmentProfile extends BaseFragment {
    private static final String TAG = FragmentProfile.class.getName();
    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private boolean login;
    private FragmentProfileMvvm fragmentProfileMvvm;
    private ActivityResultLauncher<Intent> launcher;
    private int req = 1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                binding.setModel(getUserModel());
            } else if (req == 2 && result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                String lang = result.getData().getStringExtra("lang");
                activity.refreshActivity(lang);
            }
        });
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
        Log.e(TAG, "onViewCreated: ");

    }

    private void initView() {
        fragmentProfileMvvm = ViewModelProviders.of(this).get(FragmentProfileMvvm.class);
        if (getUserModel() != null) {
            binding.setModel(getUserModel());
        }
        binding.setLang(getLang());

        fragmentProfileMvvm.logout.observe(activity, aBoolean -> {
            if (aBoolean) {
                logout();
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

        binding.llLanguage.setOnClickListener(v -> {
            req = 2;
            Intent intent = new Intent(activity, LanguageActivity.class);
            launcher.launch(intent);
        });

        binding.llRate.setOnClickListener(v -> rateApp());

        binding.tvName.setOnClickListener(v -> {
            if (getUserModel() == null) {
                navigateToLoginActivity();
            }
        });

        binding.llEditProfile.setOnClickListener(view -> {
            if (getUserModel() != null) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.editProfileFragment);
            }
        });

        binding.cardViewLogout.setOnClickListener(view -> {
            if (getUserModel() == null) {
                logout();
            } else {
                fragmentProfileMvvm.logout(activity, getUserModel());
            }

        });
    }


    private void navigateToLoginActivity() {
        req = 1;
        Intent intent = new Intent(activity, LoginActivity.class);
        launcher.launch(intent);

    }

    private void logout() {
        clearUserModel(activity);
        binding.setModel(null);
        binding.image.setImageResource(R.drawable.circle_avatar);
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
                    binding.setModel(getUserModel());
                    activity.updateFirebase();
                }
            }
        }
    }
}