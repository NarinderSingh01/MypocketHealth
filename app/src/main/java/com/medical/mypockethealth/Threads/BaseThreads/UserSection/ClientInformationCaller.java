package com.medical.mypockethealth.Threads.BaseThreads.UserSection;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformationRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.google.gson.Gson;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ClientInformationCaller implements Runnable {

    private static final String TAG = "ClientInformationCaller";

    private final CallbackFromClientInformationCaller callbackFromClientInformationCaller;
    private final ClientInformation clientInformation;

    public ClientInformationCaller(CallbackFromClientInformationCaller callbackFromClientInformationCaller, ClientInformation clientInformation) {
        this.callbackFromClientInformationCaller = callbackFromClientInformationCaller;
        this.clientInformation = clientInformation;
    }

    public interface CallbackFromClientInformationCaller
    {

        void confirmation(ResponseInformation responseInformation);
        void information(ClientInformation clientInformation);
    }

    @Override
    public void run() {


          if(clientInformation.isScriptImageStatus())
          {
              AndroidNetworking.upload(URLBuilder.base_url + URLBuilder.UrlMethods.clientInformation)
                      .addMultipartParameter(URLBuilder.Parameter.userId.toString(), clientInformation.getUserId())
                      .addMultipartParameter(URLBuilder.Parameter.morbidity.toString(),new Gson().toJson(clientInformation.getMorbidityList()))
                      .addMultipartParameter(URLBuilder.Parameter.medicineList.toString(),new Gson().toJson(clientInformation.getMedicineList()))
                      .addMultipartParameter(URLBuilder.Parameter.allergies.toString(),new Gson().toJson(clientInformation.getAllergiesList()))
                      .addMultipartParameter(URLBuilder.Parameter.scriptList.toString(),clientInformation.getScriptImage())
                      .setTag("uploadTest")
                      .setPriority(Priority.HIGH)
                      .build()
                      .setUploadProgressListener(new UploadProgressListener() {
                          @Override
                          public void onProgress(long bytesUploaded, long totalBytes) {

                          }
                      })
                      .getAsJSONObject(new JSONObjectRequestListener() {
                          @Override
                          public void onResponse(JSONObject response) {

                              try {

                                  jsonParser(response.toString());

                              } catch (JSONException e) {


                                  if (callbackFromClientInformationCaller != null) {
                                      ResponseInformation responseInformation = new ResponseInformation();
                                      responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                                      responseInformation.setMessage("We are having technical issue. Please try again later");
                                      callbackFromClientInformationCaller.confirmation(responseInformation);
                                  }

                              }


                          }

                          @Override
                          public void onError(ANError error) {

                              if (callbackFromClientInformationCaller != null) {
                                  ResponseInformation responseInformation = new ResponseInformation();
                                  responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                                  responseInformation.setMessage("We are having technical issue. Please try again later");
                                  callbackFromClientInformationCaller.confirmation(responseInformation);
                              }

                          }
                      });


          }
          else
          {
              AndroidNetworking.upload(URLBuilder.base_url + URLBuilder.UrlMethods.clientInformation)
                      .addMultipartParameter(URLBuilder.Parameter.userId.toString(), clientInformation.getUserId())
                      .addMultipartParameter(URLBuilder.Parameter.morbidity.toString(),new Gson().toJson(clientInformation.getMorbidityList()))
                      .addMultipartParameter(URLBuilder.Parameter.medicineList.toString(),new Gson().toJson(clientInformation.getMedicineList()))
                      .addMultipartParameter(URLBuilder.Parameter.allergies.toString(),new Gson().toJson(clientInformation.getAllergiesList()))
                      .setTag("uploadTest")
                      .setPriority(Priority.HIGH)
                      .build()
                      .setUploadProgressListener(new UploadProgressListener() {
                          @Override
                          public void onProgress(long bytesUploaded, long totalBytes) {

                          }
                      })
                      .getAsJSONObject(new JSONObjectRequestListener() {
                          @Override
                          public void onResponse(JSONObject response) {

                              try {

                                  jsonParser(response.toString());

                              } catch (JSONException e) {

                                  if (callbackFromClientInformationCaller != null) {
                                      ResponseInformation responseInformation = new ResponseInformation();
                                      responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                                      responseInformation.setMessage("We are having technical issue. Please try again later");
                                      callbackFromClientInformationCaller.confirmation(responseInformation);
                                  }

                              }


                          }

                          @Override
                          public void onError(ANError error) {

                              if (callbackFromClientInformationCaller != null) {
                                  ResponseInformation responseInformation = new ResponseInformation();
                                  responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                                  responseInformation.setMessage("We are having technical issue. Please try again later");
                                  callbackFromClientInformationCaller.confirmation(responseInformation);
                              }
                          }
                      });

                  }

    }

    private void jsonParser(String value) throws JSONException {

        Gson gson = new Gson();

        ResponseInformation responseInformation = gson.fromJson(value,ResponseInformation.class);

        if(!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type)))
        {

            ClientInformationRoot clientInformationRoot = gson.fromJson(value, ClientInformationRoot.class);

            if(callbackFromClientInformationCaller!=null)
            {

                callbackFromClientInformationCaller.information(clientInformationRoot.getDetails());
            }


        }

        else
        {
            if(callbackFromClientInformationCaller!=null)
            {

                callbackFromClientInformationCaller.confirmation(responseInformation);
            }

        }

    }

}
