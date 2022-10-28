package com.medical.mypockethealth.Threads.BaseThreads;

import android.util.Log;

import com.google.gson.Gson;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;

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

public class ForgotPasswordCaller implements Runnable{

    private static final String TAG = "ForgotPasswordCaller";

    private final ClientInformation clientInformation;
    private String phoneNumber;
    private final CallbackFromForgotPasswordCaller callbackFromForgotPasswordCaller;


    public ForgotPasswordCaller(ClientInformation clientInformation, CallbackFromForgotPasswordCaller callbackFromForgotPasswordCaller) {
        this.clientInformation = clientInformation;
        this.callbackFromForgotPasswordCaller = callbackFromForgotPasswordCaller;
    }

    public interface CallbackFromForgotPasswordCaller
    {

        void confirmationCallbackFromForgotPasswordCaller(ResponseInformation responseInformation);
    }


    @Override
    public void run() {


        try {

            phoneNumber="+"+clientInformation.getCountryCode()+clientInformation.getPhone();

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+ URLBuilder.UrlMethods.forgotPassword).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);


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


            if(callbackFromForgotPasswordCaller!=null)
            {

                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromForgotPasswordCaller.confirmationCallbackFromForgotPasswordCaller(responseInformation);
            }
        }


    }


    private void jsonParser(String value) throws JSONException {

        Gson gson = new Gson();

        ResponseInformation responseInformation = gson.fromJson(value,ResponseInformation.class);

        if (callbackFromForgotPasswordCaller != null) {

            callbackFromForgotPasswordCaller.confirmationCallbackFromForgotPasswordCaller(responseInformation);
        }


    }

}
