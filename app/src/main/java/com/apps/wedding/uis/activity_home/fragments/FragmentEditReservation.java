package com.apps.wedding.uis.activity_home.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.apps.wedding.R;
import com.apps.wedding.databinding.FragmentEditReservationBinding;
import com.apps.wedding.model.RequestServiceModel;
import com.apps.wedding.model.ResevisionModel;
import com.apps.wedding.mvvm.FragmentChooseDayMvvm;
import com.apps.wedding.mvvm.FragmentEditReservationMvvm;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.uis.activity_home.HomeActivity;
import com.apps.wedding.uis.activity_login.LoginActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentEditReservation extends BaseFragment {
    private FragmentEditReservationBinding binding;
    private HomeActivity activity;
    private FragmentEditReservationMvvm fragmentEditReservationMvvm;
    private ResevisionModel reservationModel;
    private String date;
    private String day;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            reservationModel = (ResevisionModel) bundle.getSerializable("data");
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_reservation, container, false);
        return binding.getRoot();

    }

    private void initView() {

        fragmentEditReservationMvvm = ViewModelProviders.of(this).get(FragmentEditReservationMvvm.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEE", Locale.ENGLISH);


        fragmentEditReservationMvvm.onSuccess.observe(activity, aBoolean -> {
            if (aBoolean) {
                Toast.makeText(activity, R.string.suc, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot()).popBackStack();

            }
        });

        try {
            Date d = null;
            d = simpleDateFormat.parse(reservationModel.getDate());
            binding.calendarView.setDate(d);


            date = simpleDateFormat.format(d.getTime());
            day = simpleDateFormat1.format(d.getTime());
            binding.tvDate.setText(date);
        } catch (ParseException | OutOfDateRangeException e) {
            e.printStackTrace();
        }

        binding.btnBook.setOnClickListener(view -> {
            fragmentEditReservationMvvm.updateReservation(activity, reservationModel, getUserModel(), date);
        });
        binding.calendarView.setOnDayClickListener(eventDay -> {
            Calendar clickedDayCalendar = eventDay.getCalendar();
            date = simpleDateFormat.format(new Date(clickedDayCalendar.getTimeInMillis()));
            day = simpleDateFormat1.format(new Date(clickedDayCalendar.getTimeInMillis()));
            binding.tvDate.setText(date);

        });

    }


}