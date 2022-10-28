package com.medical.mypockethealth.ClientFragments.SymptomCheckerSection;

import android.util.Log;

import com.google.gson.Gson;
import com.medical.mypockethealth.Classes.DoctorInformation.DoctorInformation;
import com.medical.mypockethealth.Classes.DoctorInformation.DoctorInformationRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.SlotsSection.ClientSection.SlotDetailsInformationRoot;
import com.medical.mypockethealth.Classes.SlotsSection.ClientSection.SlotsDetailsInformation;
import com.medical.mypockethealth.Classes.URLBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DoctorDetailsAsPerSpecialistFetcher implements Runnable {

    private static final String TAG = "SlotByDateFetcher";

    private final String specialistId;
    private final CallbackFromDoctorDetailsAsPerSpecialistFetcher callbackFromDoctorDetailsAsPerSpecialistFetcher;

    public DoctorDetailsAsPerSpecialistFetcher(String specialistId, CallbackFromDoctorDetailsAsPerSpecialistFetcher callbackFromDoctorDetailsAsPerSpecialistFetcher) {
        this.specialistId = specialistId;
        this.callbackFromDoctorDetailsAsPerSpecialistFetcher = callbackFromDoctorDetailsAsPerSpecialistFetcher;
    }

    public interface CallbackFromDoctorDetailsAsPerSpecialistFetcher {

        void confirmationSlotByDoctorDetailsAsPerSpecialistFetcher(ResponseInformation responseInformation);

        void informationSlotByDoctorDetailsAsPerSpecialistFetcher(List<DoctorInformation> doctorInformation);
    }

    @Override
    public void run() {

        try {

            Log.d(TAG, "run: entered 1");

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

//            HttpURLConnection httpURLConnection = (HttpURLConnection) new
//                    URL(URLBuilder.base_url + URLBuilder.UrlMethods.getDoctorsBySpecialist).openConnection();
//
//            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());
//
//            httpURLConnection.setDoOutput(true);
//
//            httpURLConnection.setDoInput(true);
//
//            String data = URLEncoder.encode(URLBuilder.Parameter.providerId.toString());
//
//            OutputStream outputStream = httpURLConnection.getOutputStream();
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
//
//            bufferedWriter.write(data);
//
//            bufferedWriter.flush();
//
//            bufferedWriter.close();
//
//            outputStream.close();
//
//            InputStream inputStream = httpURLConnection.getInputStream();
//
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//
//            StringBuilder stringBuffer = new StringBuilder();
//
//            for (String v = bufferedReader.readLine(); v != null; v = bufferedReader.readLine()) {
//                stringBuffer.append(v);
//
//            }
//
//            bufferedReader.close();
//
//            inputStream.close();
//
//            jsonParser(stringBuffer.toString());

        } catch (Exception e) {

            Log.d(TAG, "run: catch");

            if (callbackFromDoctorDetailsAsPerSpecialistFetcher != null) {

                Log.d(TAG, "run: catch 2");

                ResponseInformation responseInformation = new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromDoctorDetailsAsPerSpecialistFetcher.confirmationSlotByDoctorDetailsAsPerSpecialistFetcher(responseInformation);

            }

        }

    }

    private void jsonParser(String value) throws Exception {

        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {

            Log.d(TAG, "jsonParser: if");

            DoctorInformationRoot doctorInformationRoot = gson.fromJson(value, DoctorInformationRoot.class);

            if (callbackFromDoctorDetailsAsPerSpecialistFetcher != null) {

                Log.d(TAG, "jsonParser: if 2");

                callbackFromDoctorDetailsAsPerSpecialistFetcher.informationSlotByDoctorDetailsAsPerSpecialistFetcher(doctorInformationRoot.getDetails());
            }

        } else {

            Log.d(TAG, "jsonParser: else");

            if (callbackFromDoctorDetailsAsPerSpecialistFetcher != null) {

                Log.d(TAG, "jsonParser: else 2");

                callbackFromDoctorDetailsAsPerSpecialistFetcher.confirmationSlotByDoctorDetailsAsPerSpecialistFetcher(responseInformation);

            }
        }

    }

}
