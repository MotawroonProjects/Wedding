package com.apps.wedding.activities_fragments.activity_home.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.apps.wedding.R;
import com.apps.wedding.activities_fragments.activity_base.BaseFragment;

import com.apps.wedding.activities_fragments.activity_home.HomeActivity;


import com.apps.wedding.adapter.SliderAdapter;
import com.apps.wedding.databinding.FragmentHomeBinding;

import com.apps.wedding.model.Slider_Model;
import com.apps.wedding.model.UserSettingsModel;
import com.apps.wedding.preferences.Preferences;
import com.apps.wedding.remote.Api;
import com.apps.wedding.tags.Tags;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends BaseFragment {

    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private SliderAdapter sliderAdapter;
    private Timer timer;
    private TimerTask timerTask;
    private ActivityResultLauncher<Intent> launcher;
    private int req;

    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();

        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();




        getSlider();





    }


    public void getSlider() {
    /*    binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url).get_slider(getArea().getId()).enqueue(new Callback<Slider_Model>() {
            @Override
            public void onResponse(Call<Slider_Model> call, Response<Slider_Model> response) {
                binding.progBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200 && response.body().getData() != null) {
                            sliderAdapter = new SliderAdapter(response.body().getData(), activity);
                            binding.pager.setAdapter(sliderAdapter); timer = new Timer();
                            if(response.body().getData().size()>1){
                                timerTask = new MyTask();
                                timer.scheduleAtFixedRate(timerTask, 6000, 6000);
                            }

                        } else {
                            Toast.makeText(activity, getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();

                        }

                    }

                } else {
                    try {
                        Toast.makeText(activity, getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Slider_Model> call, Throwable t) {
                try {
                    binding.progBar.setVisibility(View.GONE);

                    Log.e("Error", t.getMessage());

                } catch (Exception e) {

                }

            }
        });
*/
    }


    public class MyTask extends TimerTask {
        @Override
        public void run() {
         /* activity.runOnUiThread(() -> {
                int current_page = binding.pager.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);

                }
            });
*/
        }
    }

}
