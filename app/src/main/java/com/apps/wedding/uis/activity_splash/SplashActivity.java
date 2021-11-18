package com.apps.wedding.uis.activity_splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.apps.wedding.R;
import com.apps.wedding.uis.activity_base.BaseActivity;
import com.apps.wedding.uis.activity_home.HomeActivity;
import com.apps.wedding.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;
    private int locReq = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initView();

    }

    private void initView() {

        new Handler().postDelayed(this::NavigateToHomeActivity, 2000);


    }


    private void NavigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


}

