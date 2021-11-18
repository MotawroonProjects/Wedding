package com.apps.wedding.activities_fragments.activity_home.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


import com.apps.wedding.R;
import com.apps.wedding.activities_fragments.activity_about_app.AboutAppActivity;
import com.apps.wedding.activities_fragments.activity_base.BaseFragment;
import com.apps.wedding.activities_fragments.activity_contactus.ContactUsActivity;
import com.apps.wedding.activities_fragments.activity_home.HomeActivity;

import com.apps.wedding.databinding.FragmentProfileBinding;
import com.apps.wedding.tags.Tags;


import java.util.List;

public class FragmentProfile extends BaseFragment {

    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private int req;


    public static FragmentProfile newInstance() {
        return new FragmentProfile();
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {
        activity = (HomeActivity) getActivity();

      /*  binding.llCity.setOnClickListener(view -> {
            req = 1;
            Intent intent = new Intent(activity, LocationsActivity.class);
            launcher.launch(intent);

        });

        binding.llContactUs.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ContactUsActivity.class);
            startActivity(intent);

        });

        binding.llTerms.setOnClickListener(view -> {
            String url = Tags.base_url+"api/our-terms";
            navigateToAboutActivity("0",url);

        });
        binding.llPrivacy.setOnClickListener(view -> {
            String url = Tags.base_url+"api/our-policy";
            navigateToAboutActivity("2",url);

        });
        binding.llAbout.setOnClickListener(view -> {
            String url = Tags.base_url+"api/about-us";
            navigateToAboutActivity("1",url);

        });

        binding.llAds.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AddAdsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

        });

        binding.llRate.setOnClickListener(view -> {
            String appId = activity.getPackageName();
            Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appId));
            boolean marketFound = false;

            final List<ResolveInfo> otherApps =activity. getPackageManager()
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

        });*/
    }

    private void navigateToAboutActivity(String type, String url) {
        Intent intent = new Intent(activity, AboutAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("type",type);
        intent.putExtra("url",url);
        startActivity(intent);


    }


}
