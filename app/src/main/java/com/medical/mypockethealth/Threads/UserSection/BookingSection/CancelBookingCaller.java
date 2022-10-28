package com.medical.mypockethealth.Threads.UserSection.BookingSection;

import android.util.Log;

import com.medical.mypockethealth.Classes.BookDoctorInformation.BookDoctorInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
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

public class CancelBookingCaller implements Runnable {

    private static final String TAG = "CancelBookingCaller";

    private final BookDoctorInformation bookDoctorInformation;
    private final CallBackFromCancelBookingCaller callBackFromCancelBookingCaller;

    public CancelBookingCaller(BookDoctorInformation bookDoctorInformation, CallBackFromCancelBookingCaller callBackFromCancelBookingCaller) {
        this.bookDoctorInformation = bookDoctorInformation;
        this.callBackFromCancelBookingCaller = callBackFromCancelBookingCaller;
    }


    public interface CallBackFromCancelBookingCaller
    {

        void confirmationCancelBookingCaller(ResponseInformation responseInformation);

    }

    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.cancelBooking).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);

            String data= URLEncoder.encode(URLBuilder.Parameter.bookingId.toString(),"UTF-8")+"="+URLEncoder.encode(bookDoctorInformation.getId(),"UTF-8");

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


            if(callBackFromCancelBookingCaller!=null)
            {
                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callBackFromCancelBookingCaller.confirmationCancelBookingCaller(responseInformation);
            }
        }

    }

    private void jsonParser(String value) throws JSONException {

        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if(callBackFromCancelBookingCaller!=null)
        {
            callBackFromCancelBookingCaller.confirmationCancelBookingCaller(responseInformation);
        }

    }
}
