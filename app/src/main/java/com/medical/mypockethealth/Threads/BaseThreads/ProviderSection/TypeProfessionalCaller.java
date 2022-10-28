package com.medical.mypockethealth.Threads.BaseThreads.ProviderSection;

import android.util.Log;

import com.medical.mypockethealth.Classes.ProfessionalTypeInformation.ProfessionalTypeInformation;
import com.medical.mypockethealth.Classes.ProfessionalTypeInformation.ProfessionalTypeInformationRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TypeProfessionalCaller implements Runnable{

    private static final String TAG = "TypeProfessionalCaller";

    private final CallbackFromTypeProfessionalCaller callbackFromTypeProfessionalCaller;

    public TypeProfessionalCaller(CallbackFromTypeProfessionalCaller callbackFromTypeProfessionalCaller) {
        this.callbackFromTypeProfessionalCaller = callbackFromTypeProfessionalCaller;
    }

    public interface CallbackFromTypeProfessionalCaller {

        void confirmationCallbackFromTypeProfessionalCaller(ResponseInformation responseInformation);
        void informationCallbackFromTypeProfessionalCaller(List<ProfessionalTypeInformation> professionalTypeInformations);
    }

    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection = (HttpURLConnection) new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.getProviderType).openConnection();

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

            if (callbackFromTypeProfessionalCaller != null) {
                ResponseInformation responseInformation = new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");

                callbackFromTypeProfessionalCaller.confirmationCallbackFromTypeProfessionalCaller(responseInformation);

            }

        }

    }

    private void jsonParser(String value) throws Exception {


        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {


            ProfessionalTypeInformationRoot professionalTypeInformationRoot = gson.fromJson(value, ProfessionalTypeInformationRoot.class);

            if(callbackFromTypeProfessionalCaller!=null)
            {

                callbackFromTypeProfessionalCaller.informationCallbackFromTypeProfessionalCaller(professionalTypeInformationRoot.getDetails());
            }

        }

        else
        {
            if (callbackFromTypeProfessionalCaller != null) {

                callbackFromTypeProfessionalCaller.confirmationCallbackFromTypeProfessionalCaller(responseInformation);
            }
        }

    }

}
