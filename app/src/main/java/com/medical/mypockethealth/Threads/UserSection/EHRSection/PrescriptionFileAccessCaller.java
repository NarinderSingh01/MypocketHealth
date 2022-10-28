package com.medical.mypockethealth.Threads.UserSection.EHRSection;

import com.google.gson.Gson;
import com.medical.mypockethealth.Classes.EHRSection.PrescriptionInformation.PrescriptionInformation;
import com.medical.mypockethealth.Classes.EHRSection.PrescriptionInformation.PrescriptionInformationRoot;
import com.medical.mypockethealth.Classes.EHRSection.SickNoteInformation.SickInformationRoot;
import com.medical.mypockethealth.Classes.EHRSection.SickNoteInformation.SickNoteInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;

import org.json.JSONObject;

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

public class PrescriptionFileAccessCaller implements Runnable{

    private static final String TAG = "FollowUpInformationCall";

    private final int type;
    private final CallbackFromPrescriptionFileAccessCaller callbackFromPrescriptionFileAccessCaller;
    private final String userId;


    public PrescriptionFileAccessCaller(int type, CallbackFromPrescriptionFileAccessCaller callbackFromPrescriptionFileAccessCaller, String userId) {
        this.type = type;
        this.callbackFromPrescriptionFileAccessCaller = callbackFromPrescriptionFileAccessCaller;
        this.userId = userId;
    }

    public interface CallbackFromPrescriptionFileAccessCaller
    {

        void confirmationPrescriptionFileAccessCaller(ResponseInformation responseInformation);
        void informationPrescriptionFileAccessCaller(List<PrescriptionInformation> prescriptionInformation, String baseUrl);
    }

    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+ URLBuilder.UrlMethods.getFiles).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);

            String data= URLEncoder.encode(URLBuilder.Parameter.userId.toString(),"UTF-8")+"="+URLEncoder.encode(userId,"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.type.toString(),"UTF-8")+"="+URLEncoder.encode(String.valueOf(type),"UTF-8");


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


            if(callbackFromPrescriptionFileAccessCaller!=null)
            {
                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromPrescriptionFileAccessCaller.confirmationPrescriptionFileAccessCaller(responseInformation);
            }
        }

    }

    private void jsonParser(String value) throws Exception
    {

        ResponseInformation responseInformation=new ResponseInformation();
        JSONObject jsonObject=new JSONObject(value);
        responseInformation.setMessage(jsonObject.get(ResponseInformation.ResponseKeys.message.toString()).toString());
        responseInformation.setSuccess(jsonObject.get(ResponseInformation.ResponseKeys.success.toString()).toString());


        if(responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.success_response_type)))
        {

            Gson gson = new Gson();

            PrescriptionInformationRoot prescriptionInformationRoot = gson.fromJson(value, PrescriptionInformationRoot.class);

            if(callbackFromPrescriptionFileAccessCaller!=null)
            {

                callbackFromPrescriptionFileAccessCaller.informationPrescriptionFileAccessCaller(prescriptionInformationRoot.getDetails(),prescriptionInformationRoot.getUrl());
            }

        }

        else
        {


            if(callbackFromPrescriptionFileAccessCaller!=null)
            {

                callbackFromPrescriptionFileAccessCaller.confirmationPrescriptionFileAccessCaller(responseInformation);
            }

        }

    }

}