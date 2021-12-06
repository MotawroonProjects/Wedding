package com.apps.wedding.services;


import com.apps.wedding.model.DepartmentDataModel;
import com.apps.wedding.model.PlaceGeocodeData;
import com.apps.wedding.model.UserModel;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    @GET("geocode/json")
    Single<Response<PlaceGeocodeData>> getGeoData(@Query(value = "latlng") String latlng,
                                                  @Query(value = "language") String language,
                                                  @Query(value = "key") String key);

    @GET("api/departments")
    Single<Response<DepartmentDataModel>> getDepartments(@Query(value = "api_key") String api_key);
    @FormUrlEncoded
    @POST("api/login")
    Single<Response<UserModel>> login(@Field("api_key") String api_key,
                                      @Field("phone_code") String phone_code,
                                      @Field("phone") String phone);

}