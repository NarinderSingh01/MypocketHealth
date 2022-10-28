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
import java.util.ArrayList;
import java.util.List;

public class MorbidityCaller implements Runnable {

    private static final String TAG = "MorbidityCaller";

    private final CallbackFromMorbidityCaller callbackFromMorbidityCaller;

    public MorbidityCaller(CallbackFromMorbidityCaller callbackFromMorbidityCaller) {
        this.callbackFromMorbidityCaller = callbackFromMorbidityCaller;
    }

    public interface CallbackFromMorbidityCaller {

        void confirmationCallbackFromMorbidityCaller(ResponseInformation responseInformation);
        void informationCallbackFromMorbidityCaller(List<MedicalAidInformation> registerAsCategoryInformation);
    }


    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection = (HttpURLConnection) new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.morbidity).openConnection();

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

            if (callbackFromMorbidityCaller != null) {
                ResponseInformation responseInformation = new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");


//                 callbackFromMorbidityCaller.confirmationCallbackFromMorbidityCaller(responseInformation);

                List<MedicalAidInformation> registerAsCategoryInformation=new ArrayList<>();


                MedicalAidInformation medicalAidInformation=new MedicalAidInformation();
                medicalAidInformation.setTitle("Assda");
                MedicalAidInformation medicalAidInformation1=new MedicalAidInformation();
                medicalAidInformation.setTitle("other");
                registerAsCategoryInformation.add(medicalAidInformation);
                registerAsCategoryInformation.add(medicalAidInformation1);

                callbackFromMorbidityCaller.informationCallbackFromMorbidityCaller(registerAsCategoryInformation);

            }

        }

    }

    private void jsonParser(String value) throws Exception {


        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {


            MedicalAidInformationRoot medicalAidInformationRoot = gson.fromJson(value, MedicalAidInformationRoot.class);

            if(callbackFromMorbidityCaller!=null)
            {

                callbackFromMorbidityCaller.informationCallbackFromMorbidityCaller(medicalAidInformationRoot.getDetails());
            }

        }

        else
        {
            if (callbackFromMorbidityCaller != null) {

                callbackFromMorbidityCaller.confirmationCallbackFromMorbidityCaller(responseInformation);
            }
        }

    }

}
