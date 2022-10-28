package com.medical.mypockethealth.Threads.BaseThreads;

import android.util.Log;

import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.google.gson.Gson;

import org.json.JSONException;

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

public class OtpCaller implements Runnable{

    private static final String TAG = "OtpCaller";

    private final Object object;

    private final CallbackFromOtpCaller callbackFromOtpCaller;

    public interface CallbackFromOtpCaller
    {

        void confirmation(ResponseInformation responseInformation);
    }

    public OtpCaller(Object object, CallbackFromOtpCaller callbackFromOtpCaller) {
        this.object = object;
        this.callbackFromOtpCaller = callbackFromOtpCaller;
    }

    @Override
    public void run() {

        try {

            String phoneNumber;
            if(object instanceof ProviderInformation)
             {
                 ProviderInformation providerInformation=(ProviderInformation)object;

                 phoneNumber ="+"+providerInformation.getCountryCode()+providerInformation.getPhoneNumber();
             }
             else
             {
                 ClientInformation clientInformation=(ClientInformation)object;

                 phoneNumber ="+"+clientInformation.getCountryCode()+clientInformation.getPhone();

             }

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+ URLBuilder.UrlMethods.uniqueApi).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);


            Log.d(TAG, "run: " + phoneNumber);

            String data= URLEncoder.encode(URLBuilder.Parameter.phoneNumber.toString(),"UTF-8")+"="+URLEncoder.encode(phoneNumber,"UTF-8");

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


            if(callbackFromOtpCaller!=null)
            {

                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromOtpCaller.confirmation(responseInformation);
            }
        }


    }

    private void jsonParser(String value) throws JSONException {

        Gson gson = new Gson();

        ResponseInformation responseInformation = gson.fromJson(value,ResponseInformation.class);

        if (callbackFromOtpCaller != null) {

            callbackFromOtpCaller.confirmation(responseInformation);
        }


    }
}
