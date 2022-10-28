package com.medical.mypockethealth.Threads.BaseThreads.ProviderSection.EHRSection;

import com.google.gson.Gson;
import com.medical.mypockethealth.Classes.ICRInformation.ICRInformationRoot;
import com.medical.mypockethealth.Classes.RegisterAsCategorySection.RegisterAsCategoryRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;

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

public class DiagnosisDetailFetcher implements Runnable {

    private static final String TAG = "DiagnosisDetailFetcher";

    private final String endPointName;
    private final CallbackFromDiagnosisDetailFetcher callbackFromDiagnosisDetailFetcher;

    public DiagnosisDetailFetcher(String endPointName, CallbackFromDiagnosisDetailFetcher callbackFromDiagnosisDetailFetcher) {
        this.endPointName = endPointName;
        this.callbackFromDiagnosisDetailFetcher = callbackFromDiagnosisDetailFetcher;
    }

    public interface CallbackFromDiagnosisDetailFetcher
    {
        void confirmationDiagnosisDetailFetcher(ResponseInformation responseInformation);
        void informationDiagnosisDetailFetcher(ICRInformationRoot icrInformationRoot);
    }

    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+ URLBuilder.UrlMethods.getICDDetails).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);

            String data= URLEncoder.encode(URLBuilder.Parameter.partName.toString(),"UTF-8")+"="+URLEncoder.encode(endPointName,"UTF-8");

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


            if(callbackFromDiagnosisDetailFetcher!=null)
            {
                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromDiagnosisDetailFetcher.confirmationDiagnosisDetailFetcher(responseInformation);
            }
        }


    }


    private void jsonParser(String value) throws Exception {


        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {


            ICRInformationRoot icrInformationRoot = gson.fromJson(value, ICRInformationRoot.class);


            if(callbackFromDiagnosisDetailFetcher!=null)
            {

                callbackFromDiagnosisDetailFetcher.informationDiagnosisDetailFetcher(icrInformationRoot);
            }

        }

        else
        {
            if (callbackFromDiagnosisDetailFetcher != null) {

                callbackFromDiagnosisDetailFetcher.confirmationDiagnosisDetailFetcher(responseInformation);
            }
        }

    }

}
