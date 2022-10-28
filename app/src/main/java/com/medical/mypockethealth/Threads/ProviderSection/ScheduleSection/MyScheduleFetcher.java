package com.medical.mypockethealth.Threads.ProviderSection.ScheduleSection;

import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.SlotsSection.SlotInformation;
import com.medical.mypockethealth.Classes.SlotsSection.SlotInformationRoot;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.google.gson.Gson;

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

public class MyScheduleFetcher implements Runnable{

    private static final String TAG = "MyScheduleFetcher";
    
    private final CallbackFromMyScheduleFetcher callbackFromMyScheduleFetcher;

    public MyScheduleFetcher(CallbackFromMyScheduleFetcher callbackFromMyScheduleFetcher) {
        this.callbackFromMyScheduleFetcher = callbackFromMyScheduleFetcher;
    }

    public interface CallbackFromMyScheduleFetcher {

        void confirmation(ResponseInformation responseInformation);
        void information(List<SlotInformation> slotInformation);
    }

    @Override
    public void run() {


        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.getDoctorSlot).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);

            String data= URLEncoder.encode(URLBuilder.Parameter.providerId.toString(),"UTF-8")+"="+URLEncoder.encode(ProviderMainFrame.id,"UTF-8");

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


            if(callbackFromMyScheduleFetcher!=null)
            {
                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromMyScheduleFetcher.confirmation(responseInformation);
            }
        }

    }

    private void jsonParser(String value) throws Exception {
        
        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {


            SlotInformationRoot slotInformationRoot = gson.fromJson(value, SlotInformationRoot.class);


            if(callbackFromMyScheduleFetcher!=null)
            {

                callbackFromMyScheduleFetcher.information(slotInformationRoot.getDetails());
            }

        }

        else
        {
            if (callbackFromMyScheduleFetcher != null) {

                callbackFromMyScheduleFetcher.confirmation(responseInformation);
            }
        }

    }
}
