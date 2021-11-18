package com.apps.wedding.activities_fragments.activity_notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apps.wedding.R;
import com.apps.wedding.activities_fragments.activity_base.BaseActivity;
import com.apps.wedding.adapter.NotificationAdapter;
import com.apps.wedding.databinding.ActivityNotificationBinding;
import com.apps.wedding.language.Language;
import com.apps.wedding.model.NotificationDataModel;
import com.apps.wedding.model.NotificationModel;
import com.apps.wedding.model.UserModel;
import com.apps.wedding.preferences.Preferences;
import com.apps.wedding.remote.Api;
import com.apps.wedding.tags.Tags;


import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends BaseActivity {
    private ActivityNotificationBinding binding;
    private NotificationAdapter adapter;
    private List<NotificationModel> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        initView();
    }


    private void initView() {
        setUpToolbar(binding.toolbar,getString(R.string.notifications),R.color.white,R.color.black);
        list = new ArrayList<>();

        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this, list);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.shimmer.startShimmer();
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefresh.setOnRefreshListener(this::getNotifications);

        getNotifications();

    }

    private void getNotifications() {
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        binding.tvNoData.setVisibility(View.GONE);
        list.clear();
        adapter.notifyDataSetChanged();
        if (getUserModel() == null) {
            binding.tvNoData.setVisibility(View.VISIBLE);
            binding.swipeRefresh.setRefreshing(false);
            binding.shimmer.setVisibility(View.GONE);
            binding.shimmer.stopShimmer();
            return;
        }
/*        Api.getService(Tags.base_url)
                .getNotification("Bearer "+getUserModel().getData().getToken(), getUserModel().getData().getId())
                .enqueue(new Callback<NotificationDataModel>() {
                    @Override
                    public void onResponse(Call<NotificationDataModel> call, Response<NotificationDataModel> response) {
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);

                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData().size() > 0) {
                                    list.addAll(response.body().getData());
                                    adapter.notifyDataSetChanged();
                                    binding.tvNoData.setVisibility(View.GONE);
                                } else {
                                    binding.tvNoData.setVisibility(View.VISIBLE);

                                }
                            }


                        } else {
                            binding.swipeRefresh.setRefreshing(false);
                            binding.shimmer.stopShimmer();
                            binding.shimmer.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<NotificationDataModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage() + "__");

                        } catch (Exception e) {

                        }
                    }
                });*/

    }



}