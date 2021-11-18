package com.apps.wedding.notifications;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.apps.wedding.R;
import com.apps.wedding.activities_fragments.activity_notifications.NotificationActivity;

import com.apps.wedding.model.StatusResponse;
import com.apps.wedding.model.UserModel;
import com.apps.wedding.preferences.Preferences;
import com.apps.wedding.remote.Api;
import com.apps.wedding.tags.Tags;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.Map;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FireBaseMessaging extends FirebaseMessagingService {

    private Preferences preferences = Preferences.getInstance();
    private Map<String, String> map;
    private final String GROUP_KEY = "MyAppNotGroup1929Key";



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        map = remoteMessage.getData();

        for (String key : map.keySet()) {
            Log.e("Key=", key + "_value=" + map.get(key));
        }


    }





    private UserModel getUserData() {
        return preferences.getUserData(this);

    }

    private String getRoomId() {
        return preferences.getRoom_Id(this);

    }

}
