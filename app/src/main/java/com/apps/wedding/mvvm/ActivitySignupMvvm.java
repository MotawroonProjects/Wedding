package com.apps.wedding.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wedding.R;
import com.apps.wedding.model.SignUpModel;
import com.apps.wedding.model.UserModel;
import com.apps.wedding.remote.Api;
import com.apps.wedding.share.Common;
import com.apps.wedding.tags.Tags;
import com.apps.wedding.uis.activity_home.HomeActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class ActivitySignupMvvm extends AndroidViewModel {
    private Context context;
    public MutableLiveData<UserModel> userModelMutableLiveData = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivitySignupMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();


    }

    public void signupWithOutImage(Context context, SignUpModel model, String phone_code, String phone) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).signUp(Tags.api_key, model.getFirst_name() + " " + model.getSeconed_name(), phone_code.replace("+", ""), phone, "android").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io()).subscribe(new SingleObserver<Response<UserModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull Response<UserModel> userModelResponse) {
                dialog.dismiss();
                if (userModelResponse.isSuccessful()) {
                    if (userModelResponse.body().getStatus() == 200) {

                        userModelMutableLiveData.postValue(userModelResponse.body());
                    } else if (userModelResponse.body().getStatus() == 405) {
                        Toast.makeText(context, context.getResources().getString(R.string.user_found), Toast.LENGTH_LONG).show();
                    }
                } else {

                }

            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                dialog.dismiss();

            }
        });
    }

    public void signupWithImage(Context context, SignUpModel model, String phone_code, String phone, Uri uri) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody api_part = Common.getRequestBodyText(Tags.api_key);
        RequestBody name_part = Common.getRequestBodyText(model.getFirst_name() + " " + model.getSeconed_name());
        RequestBody soft_part = Common.getRequestBodyText("android");
        RequestBody phone_part = Common.getRequestBodyText(phone);
        RequestBody phone_code_part = Common.getRequestBodyText(phone_code.replace("+", ""));


        MultipartBody.Part image = Common.getMultiPart(context, uri, "logo");


        Api.getService(Tags.base_url).signUpwithImage(api_part, name_part, phone_code_part, phone_part, soft_part, image).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io()).subscribe(new Observer<Response<UserModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull Response<UserModel> userModelResponse) {
                dialog.dismiss();
                if (userModelResponse.isSuccessful()) {
                    if (userModelResponse.body().getStatus() == 200) {

                        userModelMutableLiveData.postValue(userModelResponse.body());
                    } else if (userModelResponse.body().getStatus() == 405) {
                        Toast.makeText(context, context.getResources().getString(R.string.user_found), Toast.LENGTH_LONG).show();
                    }
                } else {

                }
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                dialog.dismiss();
            }

            @Override
            public void onComplete() {
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
