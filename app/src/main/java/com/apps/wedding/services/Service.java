package com.apps.wedding.services;


import com.apps.wedding.model.DepartmentDataModel;
import com.apps.wedding.model.PlaceGeocodeData;
import com.apps.wedding.model.ReservionDataModel;
import com.apps.wedding.model.SingleWeddingHallDataModel;
import com.apps.wedding.model.StatusResponse;
import com.apps.wedding.model.UserModel;
import com.apps.wedding.model.WeddingHallDataModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @GET("geocode/json")
    Single<Response<PlaceGeocodeData>> getGeoData(@Query(value = "latlng") String latlng,
                                                  @Query(value = "language") String language,
                                                  @Query(value = "key") String key);

    @GET("api/departments")
    Single<Response<DepartmentDataModel>> getDepartments(@Query(value = "api_key") String api_key);

    @GET("api/services")
    Single<Response<WeddingHallDataModel>> getWeddingHall(@Query(value = "api_key") String api_key,
                                                          @Query(value = "department_id") String department_id,
                                                          @Query(value = "rate") String rate,
                                                          @Query(value = "price_from") String price_from,
                                                          @Query(value = "price_to") String price_to
    );

    @GET("api/services")
    Single<Response<WeddingHallDataModel>> getSearchWeddingHall(@Query(value = "api_key") String api_key,
                                                                @Query(value = "name") String name
    );


    @FormUrlEncoded
    @POST("api/login")
    Single<Response<UserModel>> login(@Field("api_key") String api_key,
                                      @Field("phone_code") String phone_code,
                                      @Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/client-register")
    Single<Response<UserModel>> signUp(@Field("api_key") String api_key,
                                       @Field("name") String name,
                                       @Field("phone_code") String phone_code,
                                       @Field("phone") String phone,
                                       @Field("software_type") String software_type


    );


    @Multipart
    @POST("api/client-register")
    Observable<Response<UserModel>> signUpwithImage(@Part("api_key") RequestBody api_key,
                                                    @Part("name") RequestBody name,
                                                    @Part("phone_code") RequestBody phone_code,
                                                    @Part("phone") RequestBody phone,
                                                    @Part("software_type") RequestBody software_type,
                                                    @Part MultipartBody.Part logo


    );

    @FormUrlEncoded
    @POST("api/client-update-profile")
    Single<Response<UserModel>> editProfile(@Header("AUTHORIZATION") String token,
                                            @Field("api_key") String api_key,
                                            @Field("name") String name,
                                            @Field("user_id") String user_id


    );


    @Multipart
    @POST("api/client-update-profile")
    Observable<Response<UserModel>> editProfilewithImage(@Header("AUTHORIZATION") String token,
                                                         @Part("api_key") RequestBody api_key,
                                                         @Part("name") RequestBody name,
                                                         @Part("user_id") RequestBody user_id,
                                                         @Part MultipartBody.Part logo


    );

    @FormUrlEncoded
    @POST("api/logout")
    Single<Response<StatusResponse>> logout(@Header("AUTHORIZATION") String token,
                                            @Field("api_key") String api_key,
                                            @Field("phone_token") String phone_token


    );

    @FormUrlEncoded
    @POST("api/firebase-tokens")
    Single<Response<StatusResponse>> updateFirebasetoken(@Header("AUTHORIZATION") String token,
                                                         @Field("api_key") String api_key,
                                                         @Field("phone_token") String phone_token,
                                                         @Field("user_id") String user_id,
                                                         @Field("software_type") String software_type


    );

    @FormUrlEncoded
    @POST("api/contact-us")
    Single<Response<StatusResponse>> contactUs(@Field("api_key") String api_key,
                                               @Field("name") String name,
                                               @Field("email") String email,
                                               @Field("phone") String phone,
                                               @Field("message") String message


    );

    @GET("api/one-service")
    Single<Response<SingleWeddingHallDataModel>> getSingleWeddingHall(@Query(value = "api_key") String api_key,
                                                                      @Query(value = "service_id") String service_id
    );

    @FormUrlEncoded
    @POST("api/book-service")
    Single<Response<StatusResponse>> reserve(@Header("AUTHORIZATION") String token,
                                             @Field("api_key") String api_key,
                                             @Field("service_id") String service_id,
                                             @Field("user_id") String user_id,
                                             @Field("date") String date,
                                             @Field("day") String day,
                                             @Field("service_item_ids[]") List<String> service_item_ids,
                                             @Field("offer_id") String offer_id
    );

    @FormUrlEncoded
    @POST("api/change-date")
    Single<Response<StatusResponse>> updateReservation(@Header("AUTHORIZATION") String token,
                                                       @Field("api_key") String api_key,
                                                       @Field("user_id") String user_id,
                                                       @Field("service_id") String service_id,
                                                       @Field("reservation_id") String reservation_id,
                                                       @Field("date") String date);

    @FormUrlEncoded
    @POST("api/delete-reservation")
    Single<Response<StatusResponse>> deleteReservation(@Header("AUTHORIZATION") String token,
                                                       @Field("api_key") String api_key,
                                                       @Field("user_id") String user_id,
                                                       @Field("reservation_id") String reservation_id);

    @GET("api/new-reservations")
    Single<Response<ReservionDataModel>> getCurrentReservion(@Header("AUTHORIZATION") String token,
                                                             @Query(value = "api_key") String api_key,
                                                             @Query(value = "user_id") String user_id
    );

    @GET("api/confirmed-reservations")
    Single<Response<ReservionDataModel>> getPreviousReservion(@Header("AUTHORIZATION") String token,
                                                              @Query(value = "api_key") String api_key,
                                                              @Query(value = "user_id") String user_id
    );

}