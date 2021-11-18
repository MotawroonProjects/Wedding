package com.apps.wedding.activities_fragments.activity_base;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.apps.wedding.databinding.ToolbarBinding;
import com.apps.wedding.language.Language;
import com.apps.wedding.model.UserModel;
import com.apps.wedding.model.UserSettingsModel;
import com.apps.wedding.preferences.Preferences;

import io.paperdb.Paper;


public class BaseActivity extends AppCompatActivity {
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    public static final String READ_REQ = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_REQ = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String CAM_REQ = Manifest.permission.CAMERA;
    public static final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String bgLocPerm = Manifest.permission.ACCESS_BACKGROUND_LOCATION;

    public static final String FAMILY ="family";
    public static final String DRIVER ="driver";
    public static final String CHALETS ="chalets";
    public static final String CLIENT ="client";


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews(){

        preferences = Preferences.getInstance();


    }

    protected String getLang(){
        Paper.init(this);
        lang =Paper.book().read("lang","ar");
        return lang;
    }

    protected UserModel getUserModel(){
        userModel = preferences.getUserData(this);
        return userModel;
    }

    protected void setUserModel(UserModel userModel){
        this.userModel = userModel;
        preferences.createUpdateUserData(this,userModel);
    }


    public void setUserSettings(UserSettingsModel userSettingsModel){
        Preferences preferences = Preferences.getInstance();
        preferences.create_update_user_settings(this,userSettingsModel);
    }

    public UserSettingsModel getUserSettings(){
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserSettings(this);
    }

    public void setRoomId(String order_id){
        Preferences preferences = Preferences.getInstance();
        preferences.create_room_id(this,order_id);
    }

    public String getRoomId(){
        Preferences preferences = Preferences.getInstance();
        return preferences.getRoom_Id(this);
    }

    protected void setUpToolbar(ToolbarBinding binding,String title,int background,int arrowTitleColor){
        binding.setLang(getLang());
        binding.setTitle(title);
        binding.arrow.setColorFilter(ContextCompat.getColor(this,arrowTitleColor));
        binding.tvTitle.setTextColor(ContextCompat.getColor(this,arrowTitleColor));
        binding.toolbar.setBackgroundResource(background);
        binding.llBack.setOnClickListener(v -> finish());
    }


}