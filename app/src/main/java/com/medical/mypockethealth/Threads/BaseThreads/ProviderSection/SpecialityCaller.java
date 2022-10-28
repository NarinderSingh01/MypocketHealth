package com.medical.mypockethealth.Threads.BaseThreads.ProviderSection;

import android.util.Log;

import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.SpecialitiesInformation.SpecialitiesInformation;
import com.medical.mypockethealth.Classes.SpecialitiesInformation.SpecialitiesInformationRoot;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SpecialityCaller implements Runnable {

    private static final String TAG = "SpecialityCaller";
    
    private final CallbackFromSpecialityCaller callbackFromSpecialityCaller;
    
    public SpecialityCaller(CallbackFromSpecialityCaller callbackFromSpecialityCaller) {
        this.callbackFromSpecialityCaller = callbackFromSpecialityCaller;
    }
    
    public interface CallbackFromSpecialityCaller {

        void confirmationCallbackFromSpecialityCaller(ResponseInformation responseInformation);
        void informationCallbackFromSpecialityCaller(List<SpecialitiesInformation> specialitiesInformation);
    }

    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection = (HttpURLConnection) new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.specialityList).openConnection();

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

            if (callbackFromSpecialityCaller != null) {
                ResponseInformation responseInformation = new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");

                callbackFromSpecialityCaller.confirmationCallbackFromSpecialityCaller(responseInformation);

            }

        }

    }

    private void jsonParser(String value) throws Exception {


        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {


            SpecialitiesInformationRoot specialitiesInformationRoot = gson.fromJson(value, SpecialitiesInformationRoot.class);

            if(callbackFromSpecialityCaller!=null)
            {

                callbackFromSpecialityCaller.informationCallbackFromSpecialityCaller(specialitiesInformationRoot.getDetails());
            }

        }

        else
        {
            if (callbackFromSpecialityCaller != null) {

                callbackFromSpecialityCaller.confirmationCallbackFromSpecialityCaller(responseInformation);
            }
        }

    }


}
