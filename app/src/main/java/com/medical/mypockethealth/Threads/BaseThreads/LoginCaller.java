package com.medical.mypockethealth.Threads.BaseThreads;

import android.util.Log;

import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformationRoot;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformationRoot;
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

public class LoginCaller implements Runnable{

    private static final String TAG = "LoginCaller";
    private final Object object;
    private final CallbackFromLoginCaller callbackFromLoginCaller;
    private final int modeType;

    public LoginCaller(Object object, CallbackFromLoginCaller callbackFromLoginCaller, int modeType) {
        this.object = object;
        this.callbackFromLoginCaller = callbackFromLoginCaller;
        this.modeType = modeType;
    }

    public interface CallbackFromLoginCaller
    {

        void confirmation(ResponseInformation responseInformation);
        void information(Object information, String modeType);
    }

    @Override
    public void run() {

         switch (modeType)
         {

             case 0:

                 ClientInformation clientInformation=(ClientInformation) object;

                 try {

                     HttpURLConnection httpURLConnection=(HttpURLConnection)new
                             URL(URLBuilder.base_url+ URLBuilder.UrlMethods.login).openConnection();

                     httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

                     httpURLConnection.setDoOutput(true);

                     httpURLConnection.setDoInput(true);

                     String data= URLEncoder.encode(URLBuilder.Parameter.phoneNumber.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getLoginType(),"UTF-8")
                             +"&"+URLEncoder.encode(URLBuilder.Parameter.countryCode.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getCountryCode(),"UTF-8")
                             +"&"+URLEncoder.encode(URLBuilder.Parameter.password.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getPassword(),"UTF-8")
                             +"&"+URLEncoder.encode(URLBuilder.Parameter.regId.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getRegId(),"UTF-8")
                             +"&"+URLEncoder.encode(URLBuilder.Parameter.deviceId.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getDeviceId(),"UTF-8")
                             +"&"+URLEncoder.encode(URLBuilder.Parameter.deviceType.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getDeviceType(),"UTF-8");



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

                     if(callbackFromLoginCaller!=null)
                     {
                         ResponseInformation responseInformation=new ResponseInformation();
                         responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                         responseInformation.setMessage("We are having technical issue. Please try again later");
                         callbackFromLoginCaller.confirmation(responseInformation);
                     }
                 }

                 break;

             case 1:

                 ProviderInformation providerInformation=(ProviderInformation)object;

                 try {

                     HttpURLConnection httpURLConnection=(HttpURLConnection)new
                             URL(URLBuilder.base_url+ URLBuilder.UrlMethods.login).openConnection();

                     httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

                     httpURLConnection.setDoOutput(true);

                     httpURLConnection.setDoInput(true);

                     String data= URLEncoder.encode(URLBuilder.Parameter.phoneNumber.toString(),"UTF-8")+"="+URLEncoder.encode(providerInformation.getLoginType(),"UTF-8")
                             +"&"+URLEncoder.encode(URLBuilder.Parameter.countryCode.toString(),"UTF-8")+"="+URLEncoder.encode(providerInformation.getCountryCode(),"UTF-8")
                             +"&"+URLEncoder.encode(URLBuilder.Parameter.password.toString(),"UTF-8")+"="+URLEncoder.encode(providerInformation.getPassword(),"UTF-8")
                             +"&"+URLEncoder.encode(URLBuilder.Parameter.regId.toString(),"UTF-8")+"="+URLEncoder.encode(providerInformation.getRegId(),"UTF-8")
                             +"&"+URLEncoder.encode(URLBuilder.Parameter.deviceId.toString(),"UTF-8")+"="+URLEncoder.encode(providerInformation.getDeviceId(),"UTF-8")
                             +"&"+URLEncoder.encode(URLBuilder.Parameter.deviceType.toString(),"UTF-8")+"="+URLEncoder.encode(providerInformation.getDeviceType(),"UTF-8");


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

                     if(callbackFromLoginCaller!=null)
                     {
                         ResponseInformation responseInformation=new ResponseInformation();
                         responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                         responseInformation.setMessage("We are having technical issue. Please try again later");
                         callbackFromLoginCaller.confirmation(responseInformation);
                     }
                 }

                 break;
         }


    }


    private void jsonParser(String value) throws JSONException {


        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value,ResponseInformation.class);

        ProviderInformation providerInformation=new ProviderInformation();
        ClientInformation clientInformation=new ClientInformation();

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {

            if (responseInformation.getModeType().equals(ResponseInformation.userMode)) {

                // loading client information

                ClientInformationRoot clientInformationRoot = gson.fromJson(value,ClientInformationRoot.class);

                clientInformation=clientInformationRoot.getDetails();

            } else if (responseInformation.getModeType().equals(ResponseInformation.providerMode)) {

                ProviderInformationRoot providerInformationRoot = gson.fromJson(value,ProviderInformationRoot.class);

                Log.d(TAG, "jsonParser: "+providerInformationRoot.getDetails().getUserType());

                providerInformation=providerInformationRoot.getDetails();

            }


            if (callbackFromLoginCaller != null) {

                if (responseInformation.getModeType().equals(ResponseInformation.userMode)) {

                    callbackFromLoginCaller.information(clientInformation,responseInformation.getModeType());

                } else if (responseInformation.getModeType().equals(ResponseInformation.providerMode)) {

                    callbackFromLoginCaller.information(providerInformation,responseInformation.getModeType());
                }
            }


        } else {

            if (callbackFromLoginCaller != null) {

                callbackFromLoginCaller.confirmation(responseInformation);
            }

        }

    }
}