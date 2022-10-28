package com.medical.mypockethealth.Threads.UserSection.BookingSection;

import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.SlotsSection.ClientSection.SlotDetailsInformationRoot;
import com.medical.mypockethealth.Classes.SlotsSection.ClientSection.SlotsDetailsInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
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

public class SlotByDateFetcher implements Runnable{

    private static final String TAG = "SlotByDateFetcher";

    private final String providerId;
    private final String date;
    private final CallbackFromSlotByDateFetcher callbackFromSlotByDateFetcher;


    public SlotByDateFetcher(String providerId, String date, CallbackFromSlotByDateFetcher callbackFromSlotByDateFetcher) {
        this.providerId = providerId;
        this.date = date;
        this.callbackFromSlotByDateFetcher = callbackFromSlotByDateFetcher;
    }

    public interface CallbackFromSlotByDateFetcher {

        void confirmationSlotByDateFetcher(ResponseInformation responseInformation);
        void informationSlotByDateFetcher(SlotsDetailsInformation slotsDetailsInformation);
    }

    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.getProviderSlots).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);

            String data= URLEncoder.encode(URLBuilder.Parameter.providerId.toString(),
                    "UTF-8")+"="+URLEncoder.encode(providerId,"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.date.toString(),"UTF-8")+"="+URLEncoder.encode(date,"UTF-8");

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


            if(callbackFromSlotByDateFetcher!=null)
            {
                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromSlotByDateFetcher.confirmationSlotByDateFetcher(responseInformation);
            }
        }
        
    }

    private void jsonParser(String value) throws Exception {

        
        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {

            SlotDetailsInformationRoot slotInformationRoot = gson.fromJson(value, SlotDetailsInformationRoot.class);

            if(callbackFromSlotByDateFetcher!=null)
            {

                callbackFromSlotByDateFetcher.informationSlotByDateFetcher(slotInformationRoot.getDetails());
            }

        }

        else
        {

            if (callbackFromSlotByDateFetcher != null) {

                callbackFromSlotByDateFetcher.confirmationSlotByDateFetcher(responseInformation);
            }
        }

    }

}
