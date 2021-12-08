package com.apps.wedding.uis.activity_home;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.apps.wedding.interfaces.Listeners;
import com.apps.wedding.model.UserModel;
import com.apps.wedding.mvvm.HomeActivityMvvm;
import com.apps.wedding.preferences.Preferences;
import com.apps.wedding.uis.activity_base.BaseActivity;

import com.apps.wedding.R;

import com.apps.wedding.databinding.ActivityHomeBinding;
import com.apps.wedding.language.Language;
import com.apps.wedding.uis.activity_home.fragments.LoginFragment;
import com.apps.wedding.uis.activity_home.fragments.ServiceDetailsFragment;
import com.apps.wedding.uis.activity_home.fragments_home_navigaion.FragmentHome;

import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends BaseActivity implements Listeners.VerificationListener {
    private ActivityHomeBinding binding;
    private NavController navController;
    private HomeActivityMvvm homeActivityMvvm;
    private Preferences preferences;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }


    private void initView() {
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(this);
        homeActivityMvvm = ViewModelProviders.of(this).get(HomeActivityMvvm.class);
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
        NavigationUI.setupWithNavController(binding.toolBar, navController);
        NavigationUI.setupActionBarWithNavController(this, navController);


        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (binding.toolBar.getNavigationIcon() != null) {
                binding.toolBar.getNavigationIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.black), PorterDuff.Mode.SRC_ATOP);

            }
        });
        binding.imgNotification.setOnClickListener(v -> {
            NavOptions navOptions = new NavOptions.Builder()
                    .setEnterAnim(R.anim.enter_anim)
                    .setPopExitAnim(R.anim.exit_anim)
                    .build();
            navController.navigate(R.id.activity_notification, null, navOptions);

        });
        homeActivityMvvm.firebase.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(userModel!=null){
                    // Log.e("lldld",s);

                    userModel.getData().setFirebase_token(s);
                    preferences.createUpdateUserData(HomeActivity.this,userModel);
                }
            }
        });
        if(userModel!=null){
            homeActivityMvvm.updatefirebase(this,userModel);
        }
    }


    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }


    @Override
    public void onBackPressed() {
        int currentFragmentId = navController.getCurrentDestination().getId();
        if (currentFragmentId == R.id.home) {
            finish();

        } else if (currentFragmentId == R.id.serviceDetailsFragment) {
            Fragment fragment = getSupportFragmentManager().getPrimaryNavigationFragment();
            Fragment childFragment = fragment.getChildFragmentManager().getFragments().get(0);
            if (childFragment instanceof ServiceDetailsFragment) {
                ServiceDetailsFragment serviceDetailsFragment = (ServiceDetailsFragment) childFragment;
                if (serviceDetailsFragment.isFullScreen()) {
                    serviceDetailsFragment.setToNormalScreen();
                } else {
                    navController.popBackStack();
                }
            }

        } else {
            navController.popBackStack();
        }

    }

    @Override
    public void onVerificationSuccess() {
        int currentFragmentId = navController.getCurrentDestination().getId();
        if (currentFragmentId == R.id.loginFragment) {
            Fragment fragment = getSupportFragmentManager().getPrimaryNavigationFragment();
            Fragment childFragment = fragment.getChildFragmentManager().getFragments().get(0);
            if (childFragment instanceof LoginFragment) {
                LoginFragment loginFragment = (LoginFragment) childFragment;

            }
        }
    }


    public void updatefirebase() {
        userModel=preferences.getUserData(this);
        if(userModel!=null){
            homeActivityMvvm.updatefirebase(this,userModel);
        }
    }


}
