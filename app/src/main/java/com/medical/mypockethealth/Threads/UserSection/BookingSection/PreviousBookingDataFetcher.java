package com.medical.mypockethealth.Threads.UserSection.BookingSection;

import com.medical.mypockethealth.Classes.BookDoctorInformation.BookDoctorInformation;
import com.medical.mypockethealth.Classes.BookDoctorInformation.BookDoctorInformationRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.google.gson.Gson;

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

public class PreviousBookingDataFetcher implements Runnable{

    private static final String TAG = "PreviousBookingDataFetc";

    private String startDate, endDate;
    private CallbackFromPreviousBookingDataFetcher callbackFromPreviousBookingDataFetcher;

    public PreviousBookingDataFetcher(String startDate, String endDate, CallbackFromPreviousBookingDataFetcher callbackFromPreviousBookingDataFetcher) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.callbackFromPreviousBookingDataFetcher = callbackFromPreviousBookingDataFetcher;
    }

    public interface CallbackFromPreviousBookingDataFetcher
    {
        void getFinalData(List<BookDoctorInformation> bookDoctorInformation);
        void confirmation(ResponseInformation responseInformation);
    }

    @Override
    public void run() {
        
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.getPastBooking).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            String data = URLEncoder.encode(URLBuilder.Parameter.userId.toString(), "UTF-8") + "=" + URLEncoder.encode(ClientMainFrame.id, "UTF-8")
                    + "&" + URLEncoder.encode(URLBuilder.Parameter.startDate.toString(), "UTF-8") + "=" + URLEncoder.encode(startDate, "UTF-8")
                    + "&" + URLEncoder.encode(URLBuilder.Parameter.endDate.toString(), "UTF-8") + "=" + URLEncoder.encode(endDate, "UTF-8");

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

            bufferedWriter.write(data);

            bufferedWriter.flush();

            bufferedWriter.close();

            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            StringBuilder stringBuffer = new StringBuilder();

            for (String v = bufferedReader.readLine(); v != null; v = bufferedReader.readLine()) {

                stringBuffer.append(v);

            }


            bufferedReader.close();

            inputStream.close();

            jsonParser(stringBuffer.toString());


        } catch (Exception e) {

            if (callbackFromPreviousBookingDataFetcher != null) {
                ResponseInformation responseInformation = new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");

                callbackFromPreviousBookingDataFetcher.confirmation(responseInformation);

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
            BookDoctorInformationRoot bookDoctorInformationRoot = gson.fromJson(value, BookDoctorInformationRoot.class);

            if(callbackFromPreviousBookingDataFetcher!=null)
            {

                callbackFromPreviousBookingDataFetcher.getFinalData(bookDoctorInformationRoot.getDetails());
            }


        }

        else
        {


            if(callbackFromPreviousBookingDataFetcher!=null)
            {

                callbackFromPreviousBookingDataFetcher.confirmation(responseInformation);
            }

        }

    }

}
