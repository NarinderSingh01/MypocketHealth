package com.medical.mypockethealth.Threads.ProviderSection.BookingSection;

import com.medical.mypockethealth.Classes.ClientInformation.BookedClientInformationRoot;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
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

public class TodayPatientDetailsFetcher implements Runnable {

    private static final String TAG = "TodayPatientDetailsFetc";

    private final CallbackFromTodayPatientDetailsFetcher callbackFromTodayPatientDetailsFetcher;


    public TodayPatientDetailsFetcher(CallbackFromTodayPatientDetailsFetcher callbackFromTodayPatientDetailsFetcher) {
        this.callbackFromTodayPatientDetailsFetcher = callbackFromTodayPatientDetailsFetcher;
    }

    public interface CallbackFromTodayPatientDetailsFetcher {

        void confirmation(ResponseInformation responseInformation);
        void information(List<ClientInformation> clientInformation);
    }

    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.getTodayPatientList).openConnection();

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


            if(callbackFromTodayPatientDetailsFetcher!=null)
            {
                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromTodayPatientDetailsFetcher.confirmation(responseInformation);
            }
        }


    }

    private void jsonParser(String value) throws Exception {


        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {


            BookedClientInformationRoot bookedClientInformationRoot = gson.fromJson(value, BookedClientInformationRoot.class);


            if(callbackFromTodayPatientDetailsFetcher!=null)
            {

                callbackFromTodayPatientDetailsFetcher.information(bookedClientInformationRoot.getDetails());
            }

        }

        else
        {
            if (callbackFromTodayPatientDetailsFetcher != null) {

                callbackFromTodayPatientDetailsFetcher.confirmation(responseInformation);
            }
        }

    }


}
