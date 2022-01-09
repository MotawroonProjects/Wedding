package com.e_co.wedding.uis.activity_home;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.e_co.wedding.interfaces.Listeners;
import com.e_co.wedding.model.NotModel;
import com.e_co.wedding.model.UserModel;
import com.e_co.wedding.mvvm.ActivityHomeShareModelMvvm;
import com.e_co.wedding.mvvm.HomeActivityMvvm;
import com.e_co.wedding.uis.activity_base.BaseActivity;

import com.e_co.wedding.R;

import com.e_co.wedding.databinding.ActivityHomeBinding;
import com.e_co.wedding.language.Language;
import com.e_co.wedding.uis.activity_login.LoginActivity;
import com.e_co.wedding.uis.activity_notification.NotificationActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.paperdb.Paper;

public class HomeActivity extends BaseActivity implements Listeners.VerificationListener {
    private ActivityHomeBinding binding;
    private NavController navController;
    private HomeActivityMvvm homeActivityMvvm;
    private ActivityHomeShareModelMvvm activityHomeShareModelMvvm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
        getDataFromIntent();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("from_firebase")) {
            if (getUserModel() != null) {
                binding.setCount("0");
                Intent intent1 = new Intent(this, NotificationActivity.class);
                startActivity(intent1);
            } else {
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
            }
        }
    }


    private void initView() {

        homeActivityMvvm = ViewModelProviders.of(this).get(HomeActivityMvvm.class);
        activityHomeShareModelMvvm = ViewModelProviders.of(this).get(ActivityHomeShareModelMvvm.class);
        homeActivityMvvm.getCount().observe(this, countNumber -> {
            binding.setCount(countNumber);
        });
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
        homeActivityMvvm.firebase.observe(this, token -> {
            if (getUserModel() != null) {
                UserModel userModel = getUserModel();
                userModel.getData().setFirebase_token(token);
                setUserModel(userModel);
            }
        });

        binding.imgNotification.setOnClickListener(v -> {
            if (getUserModel() != null) {
                binding.setCount("0");
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        });
        if (getUserModel() != null) {
            homeActivityMvvm.updateFirebase(this, getUserModel());
            EventBus.getDefault().register(this);

        }


        homeActivityMvvm.getNotificationCount(getUserModel());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewNotificationListener(NotModel model) {
        homeActivityMvvm.getNotificationCount(getUserModel());
        activityHomeShareModelMvvm.setRefresh(true);


    }


    @Override
    public void onBackPressed() {
        int currentFragmentId = navController.getCurrentDestination().getId();
        if (currentFragmentId == R.id.home) {
            finish();

        } else {
            navController.popBackStack();
        }

    }

    @Override
    public void onVerificationSuccess() {

    }


    public void updateFirebase() {
        if (getUserModel() != null) {
            homeActivityMvvm.updateFirebase(this, getUserModel());

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
