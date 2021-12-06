package com.apps.wedding.services;


import com.apps.wedding.model.DepartmentDataModel;
import com.apps.wedding.model.PlaceGeocodeData;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("geocode/json")
    Single<Response<PlaceGeocodeData>> getGeoData(@Query(value = "latlng") String latlng,
                                                  @Query(value = "language") String language,
                                                  @Query(value = "key") String key);

    @GET("api/departments")
    Single<Response<DepartmentDataModel>> getDepartments(@Query(value = "api_key") String api_key);


}