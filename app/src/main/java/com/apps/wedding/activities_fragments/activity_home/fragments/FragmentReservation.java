package com.apps.wedding.activities_fragments.activity_home.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


import com.apps.wedding.R;
import com.apps.wedding.activities_fragments.activity_base.BaseFragment;
import com.apps.wedding.activities_fragments.activity_home.HomeActivity;
import com.apps.wedding.activities_fragments.activity_login.LoginActivity;
import com.apps.wedding.activities_fragments.activity_user_sign_up.UserSignUpActivity;
import com.apps.wedding.databinding.FragmentProfileBinding;
import com.apps.wedding.share.Common;

public class FragmentReservation extends BaseFragment {

    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private int request;

    private boolean canChangeStatus = false;

    public static FragmentReservation newInstance() {
        return new FragmentReservation();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {
        activity = (HomeActivity) getActivity();
        binding.llLogout.setOnClickListener(view -> {
            logout();
        });


        binding.layoutUser.llEditProfile.setOnClickListener(view -> {
            request = 1;
            Intent intent = new Intent(activity, UserSignUpActivity.class);
            intent.putExtra("phone", getUserModel().getData().getPhone());
            intent.putExtra("phone_code", getUserModel().getData().getPhone_code());
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);

        });


        binding.layoutNotSignIn.btnContinue.setOnClickListener(view -> {
            request = 1;
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);
        });

    }



    public void logout() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
/*        Api.getService(Tags.base_url)
                .logout("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId(), getUserModel().getData().getFirebase_token())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            Log.e("code", response.body().getStatus() + "__");
                            if (response.body() != null && response.body().getStatus() == 200) {
                                Log.e("loggedout", "logout");
                                canChangeStatus = false;
                                activity.stopUpdateLocation();
                                clearUserData();
                                updateUserData();
                            }

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");


                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });*/

    }



}
