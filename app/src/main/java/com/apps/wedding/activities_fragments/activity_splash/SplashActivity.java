package com.apps.wedding.activities_fragments.activity_splash;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.apps.wedding.R;
import com.apps.wedding.activities_fragments.activity_base.BaseActivity;
import com.apps.wedding.activities_fragments.activity_home.HomeActivity;
import com.apps.wedding.activities_fragments.activity_language.LanguageActivity;
import com.apps.wedding.databinding.ActivitySplashBinding;
import com.apps.wedding.language.Language;
import com.apps.wedding.model.UserSettingsModel;
import com.apps.wedding.preferences.Preferences;

import io.paperdb.Paper;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;
    private int locReq =1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initView();

    }

    private void initView() {

            new Handler().postDelayed(() -> {

                if (getUserSettings() != null && getUserSettings().isLanguageSelected()) {
                        NavigateToHomeActivity();


                } else {
                    NavigateToLanguageActivity();

                }


            }, 2000);



    }



    private void NavigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }



    private void NavigateToLanguageActivity() {

        Intent intent = new Intent(this, LanguageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 100);
    }

    private void refreshActivity(String lang) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Paper.init(this);
            Paper.book().write("lang", lang);
            Language.updateResources(this, lang);
            UserSettingsModel model =getUserSettings();
            if (getUserSettings()== null) {
                model = new UserSettingsModel();
            }
            model.setLanguageSelected(true);
            setUserSettings(model);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }, 1500);


    }

    private void startSplash(){


        new Handler().postDelayed(() -> {

            if (getUserSettings() != null && getUserSettings().isLanguageSelected()) {

                    NavigateToHomeActivity();


            } else {
                NavigateToLanguageActivity();

            }


        }, 2000);
    }

}

