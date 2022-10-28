package com.medical.mypockethealth.Classes.SymptomChecker;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.medical.mypockethealth.Classes.URLBuilder;

import java.util.List;

public class GetSymptomDetails implements Runnable {

    private static final String TAG = "GetSymptomDetails";

    CallBackFromGetSymptomDetails callBackFromGetSymptomDetail;

    public GetSymptomDetails(CallBackFromGetSymptomDetails callBackFromGetSymptomDetail) {
        this.callBackFromGetSymptomDetail = callBackFromGetSymptomDetail;
    }

    public interface CallBackFromGetSymptomDetails {
        void symptomList(List<SymptomInformation> symptomInformation);
    }

    @Override
    public void run() {

        Log.d(TAG, "run: 1");

        try {

            Log.d(TAG, "run: 2");

            AndroidNetworking.get("https://priaid-symptom-checker-v1.p.rapidapi.com/symptoms?language=en-gb&format=json")
                    .addHeaders("X-RapidAPI-Host", URLBuilder.X_RapidAPI_Host)
                    .addHeaders("X-RapidAPI-Key", URLBuilder.X_RapidAPI_Key)
                    .setTag(this)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsObjectList(SymptomInformation.class, new ParsedRequestListener<List<SymptomInformation>>() {
                        @Override
                        public void onResponse(List<SymptomInformation> users) {

                            Log.d(TAG, "run: 3");

                            if (users != null && users.size() != 0) {

                                // do anything with response

                                callBackFromGetSymptomDetail.symptomList(users);

                            } else {

                                callBackFromGetSymptomDetail.symptomList(null);

                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                            Log.d(TAG, "run: 4");

                            // handle error

                            callBackFromGetSymptomDetail.symptomList(null);

                        }
                    });

        } catch (Exception e) {

            Log.d(TAG, "run: 5");

            callBackFromGetSymptomDetail.symptomList(null);

            Log.d(TAG, "run: " + e.getMessage());
            Log.d(TAG, "run: " + e.getStackTrace());

        }

    }
}
