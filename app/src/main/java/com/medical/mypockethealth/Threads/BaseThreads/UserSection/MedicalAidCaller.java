package com.medical.mypockethealth.Threads.BaseThreads.UserSection;

import android.util.Log;

import com.medical.mypockethealth.Classes.MedicalAidInformation.MedicalAidInformation;
import com.medical.mypockethealth.Classes.MedicalAidInformation.MedicalAidInformationRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MedicalAidCaller implements Runnable{

    private static final String TAG = "MedicalAidCaller";

    private final CallbackFromMedicalAidCaller callbackFromMedicalAidCaller;


    public MedicalAidCaller(CallbackFromMedicalAidCaller callbackFromMedicalAidCaller) {
        this.callbackFromMedicalAidCaller = callbackFromMedicalAidCaller;
    }



    public interface CallbackFromMedicalAidCaller {

        void confirmationCallbackFromMedicalAidCaller(ResponseInformation responseInformation);
        void informationCallbackFromMedicalAidCaller(List<MedicalAidInformation> registerAsCategoryInformation);
    }

    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection = (HttpURLConnection) new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.medicalAid).openConnection();

            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod(URLBuilder.Request.GET.toString());
            httpURLConnection.connect();
            int response = httpURLConnection.getResponseCode();

            if (response != 200) {
                Log.d(TAG, "get_json_data: response code error: " + response);
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder jsonData = new StringBuilder();

            for (String value = bufferedReader.readLine(); value != null; value = bufferedReader.readLine()) {

                jsonData.append(value);
            }


            jsonParser(jsonData.toString());

        } catch (Exception e) {

            if (callbackFromMedicalAidCaller != null) {
                ResponseInformation responseInformation = new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");

                callbackFromMedicalAidCaller.confirmationCallbackFromMedicalAidCaller(responseInformation);

            }

        }

    }

    private void jsonParser(String value) throws Exception {


        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {


            MedicalAidInformationRoot medicalAidInformationRoot = gson.fromJson(value, MedicalAidInformationRoot.class);

            if(callbackFromMedicalAidCaller!=null)
            {

                callbackFromMedicalAidCaller.informationCallbackFromMedicalAidCaller(medicalAidInformationRoot.getDetails());
            }

        }

        else
        {
            if (callbackFromMedicalAidCaller != null) {

                callbackFromMedicalAidCaller.confirmationCallbackFromMedicalAidCaller(responseInformation);
            }
        }

    }


}
