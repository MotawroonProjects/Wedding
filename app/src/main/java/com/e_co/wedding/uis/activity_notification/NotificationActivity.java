package com.e_co.wedding.uis.activity_notification;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.e_co.wedding.R;
import com.e_co.wedding.adapter.NotificationAdapter;
import com.e_co.wedding.databinding.ActivityHomeBinding;
import com.e_co.wedding.databinding.ActivityNotificationBinding;
import com.e_co.wedding.model.NotModel;
import com.e_co.wedding.mvvm.ActivityNotificationMvvm;
import com.e_co.wedding.uis.activity_base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NotificationActivity extends BaseActivity {

    private ActivityNotificationBinding binding;
    private NotificationAdapter adapter;
    private ActivityNotificationMvvm activityNotificationMvvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        initView();


    }


    private void initView() {
        activityNotificationMvvm = ViewModelProviders.of(this).get(ActivityNotificationMvvm.class);
        activityNotificationMvvm.getIsLoading().observe(this, loading -> {
            binding.swipeRefresh.setRefreshing(loading);

        });
        activityNotificationMvvm.getNotification().observe(this, list -> {
            if (list.size() > 0) {
                adapter.updateList(list);
                binding.cardNoData.setVisibility(View.GONE);
            } else {
                binding.cardNoData.setVisibility(View.VISIBLE);

            }
        });
        setUpToolbar(binding.toolbar, getString(R.string.notifications), R.color.white, R.color.black);
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this);
        binding.recView.setAdapter(adapter);
        activityNotificationMvvm.getNotifications(getUserModel());

        EventBus.getDefault().register(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewNotificationListener(NotModel model){
        activityNotificationMvvm.getNotifications(getUserModel());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}