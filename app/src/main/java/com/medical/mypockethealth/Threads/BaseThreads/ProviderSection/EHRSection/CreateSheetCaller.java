package com.medical.mypockethealth.Threads.BaseThreads.ProviderSection.EHRSection;

import android.util.Log;

import com.google.gson.Gson;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateSheetCaller implements Runnable {

    private static final String TAG = "CreateSheetCaller";

    private final int mode;
    private final CallbackFromCreateSheetCaller callbackFromCreateSheetCaller;
    private final Map<String,String> sheetInformation;

    public CreateSheetCaller(int mode, CallbackFromCreateSheetCaller callbackFromCreateSheetCaller, Map<String, String> sheetInformation) {
        this.mode = mode;
        this.callbackFromCreateSheetCaller = callbackFromCreateSheetCaller;
        this.sheetInformation = sheetInformation;
    }

    public interface CallbackFromCreateSheetCaller
    {
        void confirmationCreateSheetCaller(ResponseInformation responseInformation,int mode);
    }

    @Override
    public void run() {

        switch (mode)
        {
            case 0:

                // for follow up

                try {

                    HttpURLConnection httpURLConnection=(HttpURLConnection)new
                            URL(URLBuilder.base_url+URLBuilder.UrlMethods.createFollowUpSheet).openConnection();

                    httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

                    httpURLConnection.setDoOutput(true);

                    httpURLConnection.setDoInput(true);

                    String data= URLEncoder.encode(URLBuilder.Parameter.userId.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.userId.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.phone.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.phone.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.email.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.email.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.address.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.address.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.date.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.date.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.providerId.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.providerId.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.speciality.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.speciality.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.providerName.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.providerName.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.followUpInformation.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.followUpInformation.toString()),"UTF-8");


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

                    if(callbackFromCreateSheetCaller !=null)
                    {
                        ResponseInformation responseInformation=new ResponseInformation();
                        responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                        responseInformation.setMessage("We are having technical issue. Please try again later");

                        callbackFromCreateSheetCaller.confirmationCreateSheetCaller(responseInformation,mode);
                    }
                }

                break;

            case 1:

                // for prescription
                
                try {

                    HttpURLConnection httpURLConnection=(HttpURLConnection)new
                            URL(URLBuilder.base_url+URLBuilder.UrlMethods.createPrescriptionSheet).openConnection();

                    httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

                    httpURLConnection.setDoOutput(true);

                    httpURLConnection.setDoInput(true);


                    String data= URLEncoder.encode(URLBuilder.Parameter.userId.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.userId.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.providerId.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.providerId.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.name.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.name.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.phone.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.phone.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.email.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.email.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.address.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.address.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.speciality.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.speciality.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.date.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.date.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.patientName.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.patientName.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.patientAge.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.patientAge.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.medicalAid.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.medicalAid.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.patientContact.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.patientContact.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.diagnosis.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.diagnosis.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.description.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.description.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.quantity.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.quantity.toString()),"UTF-8");


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

                    if(callbackFromCreateSheetCaller !=null)
                    {
                        ResponseInformation responseInformation=new ResponseInformation();
                        responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                        responseInformation.setMessage("We are having technical issue. Please try again later");

                        callbackFromCreateSheetCaller.confirmationCreateSheetCaller(responseInformation,mode);
                    }
                }





                break;

            case 2:

                // for sick note

                try {

                    HttpURLConnection httpURLConnection=(HttpURLConnection)new
                            URL(URLBuilder.base_url+URLBuilder.UrlMethods.createSickNoteSheet).openConnection();

                    httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

                    httpURLConnection.setDoOutput(true);

                    httpURLConnection.setDoInput(true);

                    String data= URLEncoder.encode(URLBuilder.Parameter.userId.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.userId.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.providerId.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.providerId.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.name.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.name.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.phone.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.phone.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.email.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.email.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.address.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.address.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.date.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.date.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.fillInformation.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.fillInformation.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.fromDate.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.fromDate.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.examinationDate.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.examinationDate.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.upToDate.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.upToDate.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.speciality.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.speciality.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.natureIllness.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.natureIllness.toString()),"UTF-8");


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

                    if(callbackFromCreateSheetCaller !=null)
                    {
                        ResponseInformation responseInformation=new ResponseInformation();
                        responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                        responseInformation.setMessage("We are having technical issue. Please try again later");

                        callbackFromCreateSheetCaller.confirmationCreateSheetCaller(responseInformation,mode);
                    }
                }


                break;

            case 3:

                // for referral note


                Log.d(TAG, "run: calling");


                try {

                    HttpURLConnection httpURLConnection=(HttpURLConnection)new
                            URL(URLBuilder.base_url+URLBuilder.UrlMethods.createReferralSheet).openConnection();

                    httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

                    httpURLConnection.setDoOutput(true);

                    httpURLConnection.setDoInput(true);


                    String data= URLEncoder.encode(URLBuilder.Parameter.userId.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.userId.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.name.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.name.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.phone.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.phone.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.speciality.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.speciality.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.email.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.email.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.address.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.address.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.date.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.date.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.informationName.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.informationName.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.IDNumber.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.IDNumber.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.onDuty.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.onDuty.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.followUpInformation.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.followUpInformation.toString()),"UTF-8")
                            +"&"+URLEncoder.encode(URLBuilder.Parameter.providerId.toString(),"UTF-8")+"="+URLEncoder.encode(sheetInformation.get(URLBuilder.Parameter.providerId.toString()),"UTF-8");


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


                    if(callbackFromCreateSheetCaller !=null)
                    {
                        ResponseInformation responseInformation=new ResponseInformation();
                        responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                        responseInformation.setMessage("We are having technical issue. Please try again later");

                        callbackFromCreateSheetCaller.confirmationCreateSheetCaller(responseInformation,mode);
                    }
                }

                break;
        }

    }


    private void jsonParser(String value) throws JSONException {
        
        Gson gson = new Gson();

        ResponseInformation responseInformation = gson.fromJson(value,ResponseInformation.class);


        if (callbackFromCreateSheetCaller != null) {

            callbackFromCreateSheetCaller.confirmationCreateSheetCaller(responseInformation,mode);
        }


    }

}
