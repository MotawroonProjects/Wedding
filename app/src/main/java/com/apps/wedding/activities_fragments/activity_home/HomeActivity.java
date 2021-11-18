package com.apps.wedding.activities_fragments.activity_home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.apps.wedding.activities_fragments.activity_base.BaseActivity;
import com.apps.wedding.activities_fragments.activity_notifications.NotificationActivity;
import com.apps.wedding.model.StatusResponse;
import com.apps.wedding.model.UserModel;
import com.apps.wedding.R;

import com.apps.wedding.activities_fragments.activity_splash.SplashActivity;
import com.apps.wedding.databinding.ActivityHomeBinding;
import com.apps.wedding.language.Language;
import com.apps.wedding.preferences.Preferences;
import com.apps.wedding.remote.Api;
import com.apps.wedding.tags.Tags;


import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {
    private ActivityHomeBinding binding;
    private NavController navController;
    private Preferences preferences;

    private UserModel userModel;
    private String lang;
    private boolean backPressed = false;
    private int locReq =1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getUserModel()!=null){
            updateFirebaseToken();
        }
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);

        navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
        binding.imgNotification.setOnClickListener(v -> {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);

        });




    }



    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }

    public void updateFirebaseToken() {
    /*    FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult().getToken();
                try {
                    Api.getService(Tags.base_url)
                            .updateFirebaseToken("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId(), token, "android")
                            .enqueue(new Callback<StatusResponse>() {
                                @Override
                                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        UserModel userModel = getUserModel();
                                        if (userModel!=null){
                                            userModel.getData().setFirebase_token(token);
                                            setUserModel(userModel);

                                        }

                                        Log.e("token", "updated successfully");
                                    } else {
                                        try {

                                            Log.e("errorToken", response.code() + "_" + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<StatusResponse> call, Throwable t) {
                                    try {

                                        if (t.getMessage() != null) {
                                            Log.e("errorToken2", t.getMessage());

                                        }

                                    } catch (Exception e) {
                                    }
                                }
                            });
                } catch (Exception e) {

                }
            }
        });*/
    }

    @Override
    public void onBackPressed() {

        finish();
    }




    private void navigateToSplashActivity() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();
    }

}
