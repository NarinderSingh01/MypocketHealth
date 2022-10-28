package com.medical.mypockethealth.Threads.UserSection.BookingSection;

import android.util.Log;

import com.medical.mypockethealth.Classes.DoctorInformation.DoctorInformation;
import com.medical.mypockethealth.Classes.DoctorInformation.DoctorInformationRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DoctorDetailsFetcher implements Runnable {

    private static final String TAG = "DoctorDetailsFetcher";

    private final CallbackFromDoctorDetailsFetcher callbackFromDoctorDetailsFetcher;

    public DoctorDetailsFetcher(CallbackFromDoctorDetailsFetcher callbackFromDoctorDetailsFetcher) {
        this.callbackFromDoctorDetailsFetcher = callbackFromDoctorDetailsFetcher;
    }

    public interface CallbackFromDoctorDetailsFetcher {

        void confirmationCallbackFromDoctorDetailsFetcher(ResponseInformation responseInformation);
        void informationCallbackFromDoctorDetailsFetcher(List<DoctorInformation> doctorInformation);
    }

    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection = (HttpURLConnection) new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.getDoctorsList).openConnection();

            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());
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

            if (callbackFromDoctorDetailsFetcher != null) {
                ResponseInformation responseInformation = new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");

                callbackFromDoctorDetailsFetcher.confirmationCallbackFromDoctorDetailsFetcher(responseInformation);

            }

        }

    }

    private void jsonParser(String value) throws Exception {


        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {


            DoctorInformationRoot doctorInformationRoot = gson.fromJson(value, DoctorInformationRoot.class);


            if(callbackFromDoctorDetailsFetcher!=null)
            {
                if (doctorInformationRoot.getDetails()!=null && doctorInformationRoot.getDetails().size()!=0){

                    callbackFromDoctorDetailsFetcher.informationCallbackFromDoctorDetailsFetcher(doctorInformationRoot.getDetails());

                }

            }

        }

        else
        {
            if (callbackFromDoctorDetailsFetcher != null) {

                callbackFromDoctorDetailsFetcher.confirmationCallbackFromDoctorDetailsFetcher(responseInformation);
            }
        }

    }
}
