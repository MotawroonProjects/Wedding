package com.apps.wedding.activities_fragments.activity_home.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apps.wedding.R;
import com.apps.wedding.activities_fragments.activity_base.BaseFragment;
import com.apps.wedding.activities_fragments.activity_home.HomeActivity;

import com.apps.wedding.adapter.OrderAdapter;
import com.apps.wedding.databinding.FragmentOrderBinding;
import com.apps.wedding.model.OrderModel;
import com.apps.wedding.remote.Api;
import com.apps.wedding.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviousOrderFragment extends BaseFragment {
    private FragmentOrderBinding binding;
    private List<OrderModel> list;
    private OrderAdapter adapter;
    private HomeActivity activity;

    public static PreviousOrderFragment newInstance() {
        PreviousOrderFragment fragment = new PreviousOrderFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        list = new ArrayList<>();
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new OrderAdapter(list, activity, this);
        binding.recView.setAdapter(adapter);
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefresh.setOnRefreshListener(this::getOrder);
        getOrder();
    }

    private void getOrder() {
        binding.tvNoOrders.setVisibility(View.GONE);
        list.clear();
        adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

      /*  Api.getService(Tags.base_url).getOrders("Bearer " + getUserModel().getData().getToken(), "old-client-orders", getUserModel().getData().getId())
                .enqueue(new Callback<OrderDataModel>() {
                    @Override
                    public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData().size() > 0) {
                                list.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();

                            } else {
                                binding.tvNoOrders.setVisibility(View.VISIBLE);
                            }

                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<OrderDataModel> call, Throwable t) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);

                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());

                            }

                        } catch (Exception e) {
                        }

                    }
                });*/

    }


    public void setItemData(OrderModel orderModel) {


    }

    public void call(OrderModel orderModel) {
        String phone = "+" + orderModel.getDriver().getPhone_code() + orderModel.getDriver().getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + phone));
        startActivity(intent);
    }
}