package com.medical.mypockethealth.Threads.ProviderSection.ScheduleSection;

import android.util.Log;

import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.SlotsSection.SlotInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
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

public class AddScheduleCaller implements Runnable{

    private static final String TAG = "AddScheduleCaller";

    private final SlotInformation slotInformation;
    private final CallbackFromAddScheduleCaller callbackFromAddScheduleCaller;


    public AddScheduleCaller(SlotInformation slotInformation, CallbackFromAddScheduleCaller callbackFromAddScheduleCaller) {
        this.slotInformation = slotInformation;
        this.callbackFromAddScheduleCaller = callbackFromAddScheduleCaller;
    }

    public interface CallbackFromAddScheduleCaller
    {

        void confirmation(ResponseInformation responseInformation);

    }

    @Override
    public void run() {


        Log.d(TAG, "run: morning " + slotInformation.getMorningVisibilityStatus());
        Log.d(TAG, "run: after " + slotInformation.getAfternoonVisibilityStatus());
        Log.d(TAG, "run: evennig " + slotInformation.getEveningVisibilityStatus());

        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.addDoctorSlot).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);

            String data= URLEncoder.encode(URLBuilder.Parameter.providerId.toString(),"UTF-8")+"="+URLEncoder.encode(ProviderMainFrame.id,"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.morningVisibilityStatus.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getMorningVisibilityStatus(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.afternoonVisibilityStatus.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getAfternoonVisibilityStatus(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.eveningVisibilityStatus.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getEveningVisibilityStatus(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.morningStartTime.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getMorningStartTime(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.morningEndTime.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getMorningEndTime(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.morningPerSlot.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getMorningPerSlot(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.afternoonStartTime.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getAfternoonStartTime(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.afternoonEndTime.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getAfternoonEndTime(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.afternoonPerSlot.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getAfternoonPerlSot(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.eveningStartTime.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getEveningStartTime(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.eveningEndTime.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getEveningEndTime(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.eveningPerSlot.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getEveningPerSlot(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.date.toString(),"UTF-8")+"="+URLEncoder.encode(slotInformation.getDate(),"UTF-8");



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


            if(callbackFromAddScheduleCaller!=null)
            {
                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromAddScheduleCaller.confirmation(responseInformation);
            }
        }

    }

    private void jsonParser(String value) throws JSONException {
        

        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if(callbackFromAddScheduleCaller!=null)
        {
            callbackFromAddScheduleCaller.confirmation(responseInformation);
        }

    }
}
