package com.apps.wedding.uis.activity_home.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apps.wedding.R;
import com.apps.wedding.adapter.AdditionalItemAdapter;
import com.apps.wedding.adapter.BaiscItemAdapter;
import com.apps.wedding.databinding.FragmentChooseDayBinding;
import com.apps.wedding.databinding.FragmentReservationConfirmationBinding;
import com.apps.wedding.model.RequestServiceModel;
import com.apps.wedding.model.SingleWeddingHallDataModel;
import com.apps.wedding.model.UserModel;
import com.apps.wedding.model.WeddingHallModel;
import com.apps.wedding.mvvm.FragmentChooseDayMvvm;
import com.apps.wedding.preferences.Preferences;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.uis.activity_home.HomeActivity;
import com.apps.wedding.uis.activity_login.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FragmentChooseDay extends BaseFragment {
    private FragmentChooseDayBinding binding;
    private HomeActivity activity;
    private FragmentChooseDayMvvm fragmentChooseDayMvvm;
    private RequestServiceModel requestServiceModel;
    private String date;
    private String day;
    private int req;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                fragmentChooseDayMvvm.reserve(activity, requestServiceModel, getUserModel(), date, day);

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_day, container, false);
        return binding.getRoot();

    }

    private void initView() {
        binding.setLang(getLang());
        requestServiceModel = (RequestServiceModel) getArguments().getSerializable("data");

        fragmentChooseDayMvvm = ViewModelProviders.of(this).get(FragmentChooseDayMvvm.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEE", Locale.ENGLISH);

        date = simpleDateFormat.format(System.currentTimeMillis());
        day = simpleDateFormat1.format(System.currentTimeMillis());
        binding.tvDate.setText(date);

        fragmentChooseDayMvvm.suc.observe(activity, aBoolean -> {
            if (aBoolean) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.reservation);

            }
        });


        binding.btnBook.setOnClickListener(view -> {
            if (getUserModel() == null) {
                navigateToLoginActivity();

            } else {
                fragmentChooseDayMvvm.reserve(activity, requestServiceModel, getUserModel(), date, day);
            }

        });
        binding.calendarView.setOnDayClickListener(eventDay -> {
            Calendar clickedDayCalendar = eventDay.getCalendar();

            date = simpleDateFormat.format(new Date(clickedDayCalendar.getTimeInMillis()));
            day = simpleDateFormat1.format(new Date(clickedDayCalendar.getTimeInMillis()));
            binding.tvDate.setText(date);

        });
    }

    private void navigateToLoginActivity() {
        req = 1;
        Intent intent = new Intent(activity, LoginActivity.class);
        launcher.launch(intent);

    }


}