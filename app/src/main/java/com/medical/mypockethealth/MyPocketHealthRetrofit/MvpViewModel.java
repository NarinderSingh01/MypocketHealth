package com.medical.mypockethealth.MyPocketHealthRetrofit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.medical.mypockethealth.Classes.DoctorInformation.DoctorInformationRoot;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MvpViewModel extends ViewModel {

    private static final String TAG = "UserPartViewModel";

    ApiInterface apiInterface = RetrofitClient.getRetrofit().create(ApiInterface.class);



    private MutableLiveData<DoctorInformationRoot> getDoctorsAsPerSpecialistMutableLiveData;

    public LiveData<DoctorInformationRoot> getDoctorsAsPerSpecialistLiveData(String specialistId) {

        getDoctorsAsPerSpecialistMutableLiveData = new MutableLiveData<>();

        apiInterface.getDoctorsAsPerSpecialist(specialistId).enqueue(new Callback<DoctorInformationRoot>() {
            @Override
            public void onResponse(@NotNull Call<DoctorInformationRoot> call, @NotNull Response<DoctorInformationRoot> response) {

                if (response.body() != null) {

                    if (response.body().getSuccess().equalsIgnoreCase("1")) {

                        getDoctorsAsPerSpecialistMutableLiveData.postValue(response.body());

                    } else {

                        getDoctorsAsPerSpecialistMutableLiveData.postValue(null);

                    }

                } else {

                    getDoctorsAsPerSpecialistMutableLiveData.postValue(null);

                }

            }

            @Override
            public void onFailure(@NotNull Call<DoctorInformationRoot> call, @NotNull Throwable t) {

                getDoctorsAsPerSpecialistMutableLiveData.postValue(null);

            }
        });

        return getDoctorsAsPerSpecialistMutableLiveData;
    }

    private MutableLiveData<DoctorInformationRoot> getDocsMutableLiveData;

    public LiveData<DoctorInformationRoot> getDocsLiveData() {

        getDocsMutableLiveData = new MutableLiveData<>();

        apiInterface.getAllDocs().enqueue(new Callback<DoctorInformationRoot>() {
            @Override
            public void onResponse(@NotNull Call<DoctorInformationRoot> call, @NotNull Response<DoctorInformationRoot> response) {

                if (response.body() != null) {

                    if (response.body().getSuccess().equalsIgnoreCase("1")) {

                        getDocsMutableLiveData.postValue(response.body());

                    } else {

                        getDocsMutableLiveData.postValue(null);

                    }

                } else {

                    getDocsMutableLiveData.postValue(null);

                }

            }

            @Override
            public void onFailure(@NotNull Call<DoctorInformationRoot> call, @NotNull Throwable t) {

                getDocsMutableLiveData.postValue(null);

            }
        });

        return getDocsMutableLiveData;
    }

    private MutableLiveData<Map> notifyHealthAlertMutableLiveData;

    public LiveData<Map> notifyHealthAlertLiveData() {

        notifyHealthAlertMutableLiveData = new MutableLiveData<>();

        apiInterface.notifyHealthAlert().enqueue(new Callback<Map>() {
            @Override
            public void onResponse(@NotNull Call<Map> call, @NotNull Response<Map> response) {

                if (response.body() != null) {

                    notifyHealthAlertMutableLiveData.postValue(response.body());

                } else {

                    notifyHealthAlertMutableLiveData.postValue(null);

                }

            }

            @Override
            public void onFailure(@NotNull Call<Map> call, @NotNull Throwable t) {

                notifyHealthAlertMutableLiveData.postValue(null);

            }
        });

        return notifyHealthAlertMutableLiveData;
    }

}
