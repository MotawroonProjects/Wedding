package com.e_co.wedding.uis.activity_home.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.e_co.wedding.R;
import com.e_co.wedding.databinding.FragmentChooseDayBinding;
import com.e_co.wedding.model.RequestServiceModel;
import com.e_co.wedding.mvvm.FragmentChooseDayMvvm;
import com.e_co.wedding.uis.activity_base.BaseFragment;
import com.e_co.wedding.uis.activity_home.HomeActivity;
import com.e_co.wedding.uis.activity_login.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentChooseDay extends BaseFragment {
    private FragmentChooseDayBinding binding;
    private HomeActivity activity;
    private FragmentChooseDayMvvm fragmentChooseDayMvvm;
    private RequestServiceModel requestServiceModel;
    private String date;
    private String day;
    private int req;
    private ActivityResultLauncher<Intent> launcher;
    private String offer_id = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                fragmentChooseDayMvvm.reserve(activity, requestServiceModel, getUserModel(), date, day, offer_id);

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            requestServiceModel = (RequestServiceModel) bundle.getSerializable("data");
            offer_id = bundle.getString("data2");
        }

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

        Calendar calendar = Calendar.getInstance();
        fragmentChooseDayMvvm = ViewModelProviders.of(this).get(FragmentChooseDayMvvm.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEE", Locale.ENGLISH);

        date = simpleDateFormat.format(calendar.getTimeInMillis());
        day = simpleDateFormat1.format(calendar.getTimeInMillis());
        binding.tvDate.setText(date);

        fragmentChooseDayMvvm.onSuccess.observe(activity, aBoolean -> {
            if (aBoolean) {
                Navigation.findNavController(binding.getRoot()).popBackStack();

            }
        });


        binding.btnBook.setOnClickListener(view -> {
            if (getUserModel() == null) {
                navigateToLoginActivity();

            } else {
                fragmentChooseDayMvvm.reserve(activity, requestServiceModel, getUserModel(), date, day, offer_id);
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