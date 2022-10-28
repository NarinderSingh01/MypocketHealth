package com.medical.mypockethealth.Classes.SymptomChecker;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;
import com.medical.mypockethealth.Classes.URLBuilder;

import java.util.List;

public class GetSpecialistCaller implements Runnable {


    private static final String TAG = "GetSymptomDetails";

    SymptomInformation symptomInformation;

    CallBackFromGetSpecialistCaller backFromGetSpecialistCaller;


    public GetSpecialistCaller(SymptomInformation symptomInformation, CallBackFromGetSpecialistCaller backFromGetSpecialistCaller) {
        this.symptomInformation = symptomInformation;
        this.backFromGetSpecialistCaller = backFromGetSpecialistCaller;
    }

    public interface CallBackFromGetSpecialistCaller {
        void symptomListGetSpecialistCaller(List<SpecialistInformation> specialisation);
    }


    @Override
    public void run() {

        String link = "https://priaid-symptom-checker-v1.p.rapidapi.com/diagnosis/specialisations?symptoms=" +
                "" + new Gson().toJson(symptomInformation.getSymptomId()) + "&gender="
                + symptomInformation.getGender() + "&year_of_birth=" + symptomInformation.getDate() + "&language=en-gb";

        AndroidNetworking.get(link)
                .addHeaders("X-RapidAPI-Host", URLBuilder.X_RapidAPI_Host)
                .addHeaders("X-RapidAPI-Key", URLBuilder.X_RapidAPI_Key)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObjectList(SpecialistInformation.class, new ParsedRequestListener<List<SpecialistInformation>>() {
                    @Override
                    public void onResponse(List<SpecialistInformation> specialisation) {

                        backFromGetSpecialistCaller.symptomListGetSpecialistCaller(specialisation);

                    }

                    @Override
                    public void onError(ANError anError) {

                        backFromGetSpecialistCaller.symptomListGetSpecialistCaller(null);

                        Log.d(TAG, "onError: " + anError.getMessage());
                    }
                });
    }
}