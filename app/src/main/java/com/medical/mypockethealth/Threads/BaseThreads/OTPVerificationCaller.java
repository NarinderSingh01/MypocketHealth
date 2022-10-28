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

public class OTPVerificationCaller implements Runnable{

    private static final String TAG = "OTPVerificationCaller";

    private final String otp;
    private final String requestId;
    private final CallbackFromOTPVerificationCaller callbackFromOTPVerificationCaller;

    public OTPVerificationCaller(String otp, String requestId, CallbackFromOTPVerificationCaller callbackFromOTPVerificationCaller) {
        this.otp = otp;
        this.requestId = requestId;
        this.callbackFromOTPVerificationCaller = callbackFromOTPVerificationCaller;
    }


    public interface CallbackFromOTPVerificationCaller
    {

        void confirmationOTPVerificationCaller(ResponseInformation responseInformation);
    }

    @Override
    public void run() {
        
        
        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+ URLBuilder.UrlMethods.otpVerification).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);

            String data= URLEncoder.encode(URLBuilder.Parameter.otp.toString(),"UTF-8")+"="+URLEncoder.encode(otp,"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.requestId.toString(),"UTF-8")+"="+URLEncoder.encode(requestId,"UTF-8");

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

            if(callbackFromOTPVerificationCaller!=null)
            {

                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromOTPVerificationCaller.confirmationOTPVerificationCaller(responseInformation);
            }
        }


    }

    private void jsonParser(String value) throws JSONException {
        

        Gson gson = new Gson();

        ResponseInformation responseInformation = gson.fromJson(value,ResponseInformation.class);

        if (callbackFromOTPVerificationCaller != null) {

            callbackFromOTPVerificationCaller.confirmationOTPVerificationCaller(responseInformation);
        }


    }
}
