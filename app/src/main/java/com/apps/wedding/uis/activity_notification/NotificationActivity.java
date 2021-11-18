package com.apps.wedding.uis.activity_notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import com.apps.wedding.R;
import com.apps.wedding.databinding.ActivityHomeBinding;
import com.apps.wedding.databinding.ActivityNotificationBinding;
import com.apps.wedding.uis.activity_base.BaseActivity;
import com.apps.wedding.uis.activity_home.HomeActivity;

public class NotificationActivity extends BaseActivity {

    private ActivityNotificationBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        initView();


    }


    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.notifications), R.color.white, R.color.black);
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        getData();
    }

    private void getData() {
        binding.cardNoData.setVisibility(View.VISIBLE);
    }
}