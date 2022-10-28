package com.medical.mypockethealth.MyPocketHealthRetrofit;

import com.medical.mypockethealth.Classes.DoctorInformation.DoctorInformationRoot;
import com.medical.mypockethealth.Classes.URLBuilder;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("getDoctorsAsPerSpecialist")
    Call<DoctorInformationRoot> getDoctorsAsPerSpecialist(
            @Field("specialistId") String specialistId
    );

    @GET("get-all-providers")
    Call<DoctorInformationRoot> getAllDocs();

    @GET(URLBuilder.UrlMethods.healthAlert)
    Call<Map> notifyHealthAlert();

}
