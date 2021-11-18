package com.apps.wedding.activities_fragments.activity_base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.apps.wedding.model.UserModel;
import com.apps.wedding.model.UserSettingsModel;
import com.apps.wedding.preferences.Preferences;

import io.paperdb.Paper;

public class BaseFragment extends Fragment {
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public String getLang() {
        Paper.init(context);
        return Paper.book().read("lang", "ar");
    }

    public UserModel getUserModel() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserData(context);
    }

    public void setUserModel(UserModel userModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.createUpdateUserData(context, userModel);
    }


    public void setUserSettings(UserSettingsModel userSettingsModel){
        Preferences preferences = Preferences.getInstance();
        preferences.create_update_user_settings(context,userSettingsModel);
    }

    public UserSettingsModel getUserSettings(){
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserSettings(context);
    }

    public void clearUserData(){
        Preferences preferences = Preferences.getInstance();
        preferences.createUpdateUserData(context,null);
    }
}
