package com.medical.mypockethealth.Threads.ProviderSection.ScheduleSection;

import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.SlotsSection.SlotInformation;
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

public class DeleteScheduleCaller implements Runnable {

    private static final String TAG = "DeleteScheduleCaller";


    private final SlotInformation slotInformation;
    private final CallbackFromDeleteScheduleCaller callbackFromDeleteScheduleCaller;


    public DeleteScheduleCaller(SlotInformation slotInformation, CallbackFromDeleteScheduleCaller callbackFromDeleteScheduleCaller) {
        this.slotInformation = slotInformation;
        this.callbackFromDeleteScheduleCaller = callbackFromDeleteScheduleCaller;
    }


    public interface CallbackFromDeleteScheduleCaller
    {

        void confirmationDeleteScheduleCaller(ResponseInformation responseInformation);

    }

    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.deleteDoctorSlot).openConnection();

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);

            String data= URLEncoder.encode(URLBuilder.Parameter.id.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getId(),"UTF-8");

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


            if(callbackFromDeleteScheduleCaller!=null)
            {
                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromDeleteScheduleCaller.confirmationDeleteScheduleCaller(responseInformation);
            }
        }



    }

    private void jsonParser(String value) throws JSONException {

        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if(callbackFromDeleteScheduleCaller!=null)
        {
            callbackFromDeleteScheduleCaller.confirmationDeleteScheduleCaller(responseInformation);
        }

    }
}
