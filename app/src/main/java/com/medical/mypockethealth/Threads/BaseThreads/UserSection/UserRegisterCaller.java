package com.medical.mypockethealth.Threads.BaseThreads.UserSection;

import android.util.Log;

import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformationRoot;
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

public class UserRegisterCaller implements Runnable {

    private static final String TAG = "MerchantRegisterCaller";


    private final ClientInformation clientInformation;
    private final CallbackFromMerchantRegisterCaller callbackFromMerchantRegisterCaller;


    public UserRegisterCaller(ClientInformation clientInformation, CallbackFromMerchantRegisterCaller callbackFromMerchantRegisterCaller) {
        this.clientInformation = clientInformation;
        this.callbackFromMerchantRegisterCaller = callbackFromMerchantRegisterCaller;
    }



    public interface CallbackFromMerchantRegisterCaller
    {

        void confirmationUserRegisterCaller(ResponseInformation responseInformation);
        void informationUserRegisterCaller(ClientInformation clientInformation);

    }

    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.userRegister).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);

            String data= URLEncoder.encode(URLBuilder.Parameter.firstName.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getFirstName(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.surName.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getSurName(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.medicalAid.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getMedicalAid(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.idNumber.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getIdNumber(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.address.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getAddress(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.city.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getCity(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.postalCode.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getPostalCode(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.email.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getEmail(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.patientAge.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getPatientAge(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.phoneNumber.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getPhone(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.countryCode.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getCountryCode(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.password.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getPassword(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.regId.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getRegId(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.aidNumber.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getAidNumber(),"UTF-8")
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

            if(callbackFromMerchantRegisterCaller!=null)
            {

                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromMerchantRegisterCaller.confirmationUserRegisterCaller(responseInformation);
            }
        }
    }


    private void jsonParser(String value) throws JSONException {

        Gson gson = new Gson();

        ResponseInformation responseInformation = gson.fromJson(value,ResponseInformation.class);

        if(!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type)))
        {

            ClientInformationRoot clientInformationRoot = gson.fromJson(value, ClientInformationRoot.class);

            if(callbackFromMerchantRegisterCaller!=null)
            {

                callbackFromMerchantRegisterCaller.informationUserRegisterCaller(clientInformationRoot.getDetails());
            }


        }

        else
        {
            if(callbackFromMerchantRegisterCaller!=null)
            {

                callbackFromMerchantRegisterCaller.confirmationUserRegisterCaller(responseInformation);
            }

        }

    }
}


