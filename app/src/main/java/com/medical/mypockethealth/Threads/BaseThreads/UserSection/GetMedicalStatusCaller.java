package com.medical.mypockethealth.Threads.BaseThreads.UserSection;

import android.util.Log;

import com.google.gson.Gson;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformationRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GetMedicalStatusCaller implements Runnable {

    private static final String TAG = "GetMedicalStatusCaller";

    private final CallbackFromGetMedicalStatusCaller callbackFromGetMedicalStatusCaller;


    public GetMedicalStatusCaller(CallbackFromGetMedicalStatusCaller callbackFromGetMedicalStatusCaller) {
        this.callbackFromGetMedicalStatusCaller = callbackFromGetMedicalStatusCaller;
    }

    public interface CallbackFromGetMedicalStatusCaller
    {

        void confirmation(ResponseInformation responseInformation);
        void information(ClientInformation clientInformation);

    }

    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.medicalStatus).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);

            String data= URLEncoder.encode(URLBuilder.Parameter.userId.toString(),"UTF-8")+"="+URLEncoder.encode(ClientMainFrame.id,"UTF-8");

            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

            bufferedWriter.write(data);

            bufferedWriter.flush();

            bufferedWriter.close();

            outputStream.close();

            InputStream inputStream=httpURLConnection.getInputStream();

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));


            StringBuilder stringBuffer=new StringBuilder();

            for (String v=bufferedReader.readLine();v!=null;v=bufferedReader.readLine())
            {
                stringBuffer.append(v);

            }


            bufferedReader.close();

            inputStream.close();


            jsonParser(stringBuffer.toString());

        } catch (Exception e) {


            if(callbackFromGetMedicalStatusCaller!=null)
            {
                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromGetMedicalStatusCaller.confirmation(responseInformation);
            }
        }

    }

    private void jsonParser(String value) throws JSONException {

        Gson gson = new Gson();

        ResponseInformation responseInformation = gson.fromJson(value,ResponseInformation.class);

        if(!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type)))
        {

            ClientInformationRoot clientInformationRoot = gson.fromJson(value, ClientInformationRoot.class);

            if(callbackFromGetMedicalStatusCaller!=null)
            {

                callbackFromGetMedicalStatusCaller.information(clientInformationRoot.getDetails());
            }


        }

        else
        {
            if(callbackFromGetMedicalStatusCaller!=null)
            {

                callbackFromGetMedicalStatusCaller.confirmation(responseInformation);
            }

        }

    }

}
