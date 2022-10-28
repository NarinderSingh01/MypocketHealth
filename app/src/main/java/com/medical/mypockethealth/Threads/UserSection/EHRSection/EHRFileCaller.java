package com.medical.mypockethealth.Threads.UserSection.EHRSection;

import android.util.Log;

import com.google.gson.Gson;
import com.medical.mypockethealth.Classes.BookDoctorInformation.BookDoctorInformationRoot;
import com.medical.mypockethealth.Classes.EHRSection.EHRFilesInformation;
import com.medical.mypockethealth.Classes.EHRSection.EHRInformationRoot;
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

public class EHRFileCaller implements Runnable {

    private static final String TAG = "EHRFileCaller";

    private final CallbackFromEHRFileCaller callbackFromEHRFileCaller;
    private final String userId;

    public EHRFileCaller(CallbackFromEHRFileCaller callbackFromEHRFileCaller, String userId) {
        this.callbackFromEHRFileCaller = callbackFromEHRFileCaller;
        this.userId = userId;
    }

    public interface CallbackFromEHRFileCaller
    {

        void confirmationEHRFileCaller(ResponseInformation responseInformation);
        void informationEHRFileCaller(EHRFilesInformation ehrFilesInformation);
    }

    @Override
    public void run() {


        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+ URLBuilder.UrlMethods.getEHRFilesStatus).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);

            String data= URLEncoder.encode(URLBuilder.Parameter.userId.toString(),"UTF-8")+"="+URLEncoder.encode(userId,"UTF-8");


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


            if(callbackFromEHRFileCaller!=null)
            {
                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromEHRFileCaller.confirmationEHRFileCaller(responseInformation);
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
            EHRInformationRoot ehrInformationRoot = gson.fromJson(value, EHRInformationRoot.class);

            if(callbackFromEHRFileCaller!=null)
            {

                callbackFromEHRFileCaller.informationEHRFileCaller(ehrInformationRoot.getDetails());
            }

        }

        else
        {


            if(callbackFromEHRFileCaller!=null)
            {

                callbackFromEHRFileCaller.confirmationEHRFileCaller(responseInformation);
            }

        }

    }

}
