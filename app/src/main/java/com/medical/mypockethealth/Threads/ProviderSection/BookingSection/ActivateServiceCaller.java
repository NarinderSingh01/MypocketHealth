package com.medical.mypockethealth.Threads.ProviderSection.BookingSection;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.Classes.VideoChatInformation.VideoChatInformation;
import com.medical.mypockethealth.Classes.VideoChatInformation.VideoChatInformationRoot;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class ActivateServiceCaller implements Runnable {

    private static final String TAG = "ActivateServiceCaller";

    private final ClientInformation clientInformation;
    private final CallbackFromActivateServiceCaller callbackFromActivateServiceCaller;
    private final Context context;

    public ActivateServiceCaller(ClientInformation clientInformation, CallbackFromActivateServiceCaller
            callbackFromActivateServiceCaller, Context context) {
        this.clientInformation = clientInformation;
        this.callbackFromActivateServiceCaller = callbackFromActivateServiceCaller;
        this.context = context;
    }

    public interface CallbackFromActivateServiceCaller {

        void confirmationActivateServiceCaller(ResponseInformation responseInformation);
        void informationActivateServiceCaller(VideoChatInformation videoChatInformation);
    }

    @Override
    public void run() {

        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.startVideoChatOperation).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());


            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);

            String userMessage="Dr. "+getProviderInformation().getFirstName().charAt(0)+" "+getProviderInformation().getSurName()+" invited you to join "+clientInformation.getSessionType()+" session\nCLICK TO JOIN";

            String data= URLEncoder.encode(URLBuilder.Parameter.channelName.toString(),"UTF-8")+"="+URLEncoder.encode(getChannelName(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.providerId.toString(),"UTF-8")+"="+URLEncoder.encode(ProviderMainFrame.id,"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.bookingId.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getId(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.patientId.toString(),"UTF-8")+"="+URLEncoder.encode(clientInformation.getUserId(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.title.toString(),"UTF-8")+"="+URLEncoder.encode(URLBuilder.Title.VideoCall,"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.type.toString(),"UTF-8")+"="+URLEncoder.encode(URLBuilder.Type.VideoChatRequest.toString(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.userMessage.toString(),"UTF-8")+"="+URLEncoder.encode(userMessage,"UTF-8");


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


            if(callbackFromActivateServiceCaller!=null)
            {
                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callbackFromActivateServiceCaller.confirmationActivateServiceCaller(responseInformation);
            }

        }


    }

    private ProviderInformation getProviderInformation()
    {

        SharedPreferences preferences= context.getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ProviderMainFrame.provider_information_key, "");
        Gson gson = new Gson();

        return gson.fromJson(json, ProviderInformation.class);

    }

    private String getChannelName()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

        return "channelName"+databaseReference.push().getKey();

    }


    private void jsonParser(String value) throws Exception {


        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {


            VideoChatInformationRoot videoChatInformationRoot = gson.fromJson(value, VideoChatInformationRoot.class);


            if(callbackFromActivateServiceCaller!=null)
            {

                callbackFromActivateServiceCaller.informationActivateServiceCaller(videoChatInformationRoot.getDetails());
            }

        }

        else
        {
            if (callbackFromActivateServiceCaller != null) {

                callbackFromActivateServiceCaller.confirmationActivateServiceCaller(responseInformation);
            }
        }

    }


}

