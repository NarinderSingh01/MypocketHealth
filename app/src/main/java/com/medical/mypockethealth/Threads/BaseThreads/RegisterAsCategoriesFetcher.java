package com.medical.mypockethealth.Threads.BaseThreads;

import android.util.Log;

import com.medical.mypockethealth.Classes.RegisterAsCategorySection.RegisterAsCategoryInformation;
import com.medical.mypockethealth.Classes.RegisterAsCategorySection.RegisterAsCategoryRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class RegisterAsCategoriesFetcher implements Runnable {

    private static final String TAG = "CategoriesFetcher";


    private final CallbackFromRegisterAsCategoriesFetcher callbackFromCategoriesFetcher;


    public RegisterAsCategoriesFetcher(CallbackFromRegisterAsCategoriesFetcher callbackFromCategoriesFetcher) {
        this.callbackFromCategoriesFetcher = callbackFromCategoriesFetcher;
    }

    public interface CallbackFromRegisterAsCategoriesFetcher {

        void confirmationCallbackFromRegisterAsCategoriesFetcher(ResponseInformation responseInformation);
        void informationCallbackFromRegisterAsCategoriesFetcher(List<RegisterAsCategoryInformation> registerAsCategoryInformation);
    }


    @Override
    public void run() {


        try {

            HttpURLConnection httpURLConnection = (HttpURLConnection) new
                    URL("").openConnection();

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

            if (callbackFromCategoriesFetcher != null) {
                ResponseInformation responseInformation = new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");

                callbackFromCategoriesFetcher.confirmationCallbackFromRegisterAsCategoriesFetcher(responseInformation);

            }

        }


    }

    private void jsonParser(String value) throws Exception {


        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {


            RegisterAsCategoryRoot registerAsCategoryRoot = gson.fromJson(value, RegisterAsCategoryRoot.class);


            if(callbackFromCategoriesFetcher!=null)
            {

                callbackFromCategoriesFetcher.informationCallbackFromRegisterAsCategoriesFetcher(registerAsCategoryRoot.getDetails());
            }

        }

        else
        {
            if (callbackFromCategoriesFetcher != null) {

                callbackFromCategoriesFetcher.confirmationCallbackFromRegisterAsCategoriesFetcher(responseInformation);
            }
        }

    }
}

