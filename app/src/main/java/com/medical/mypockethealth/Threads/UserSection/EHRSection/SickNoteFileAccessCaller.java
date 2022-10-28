package com.medical.mypockethealth.Threads.UserSection.EHRSection;

import com.google.gson.Gson;
import com.medical.mypockethealth.Classes.EHRSection.FollowUpInformation.FollowUpInformation;
import com.medical.mypockethealth.Classes.EHRSection.FollowUpInformation.FollowUpInformationRoot;
import com.medical.mypockethealth.Classes.EHRSection.SickNoteInformation.SickInformationRoot;
import com.medical.mypockethealth.Classes.EHRSection.SickNoteInformation.SickNoteInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;

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
import java.util.ArrayList;
import java.util.List;

public class SickNoteFileAccessCaller implements Runnable{

    private static final String TAG = "FollowUpInformationCall";

    private final int type;
    private final CallbackFromSickNoteFileAccessCaller callbackFromEhrFileAccessCaller;
    private final String userId;


    public SickNoteFileAccessCaller(int type, CallbackFromSickNoteFileAccessCaller callbackFromEhrFileAccessCaller, String userId) {
        this.type = type;
        this.callbackFromEhrFileAccessCaller = callbackFromEhrFileAccessCaller;
        this.userId = userId;
    }

    public interface CallbackFromSickNoteFileAccessCaller
    {

        void confirmationSickNoteFileAccessCaller(ResponseInformation responseInformation);
        void informationSickNoteFileAccessCaller(List<SickNoteInformation> sickNoteInformation,String baseUrl);
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


            if(callbackFromEhrFileAccessCaller!=null)
            {
                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromEhrFileAccessCaller.confirmationSickNoteFileAccessCaller(responseInformation);
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

            SickInformationRoot sickInformationRoot = gson.fromJson(value, SickInformationRoot.class);

            if(callbackFromEhrFileAccessCaller!=null)
            {

                callbackFromEhrFileAccessCaller.informationSickNoteFileAccessCaller(sickInformationRoot.getDetails(),sickInformationRoot.getUrl());
            }

        }

        else
        {


            if(callbackFromEhrFileAccessCaller!=null)
            {

                callbackFromEhrFileAccessCaller.confirmationSickNoteFileAccessCaller(responseInformation);
            }

        }

    }

}
