package com.medical.mypockethealth.Threads.ProviderSection.BookingSection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medical.mypockethealth.Classes.ClientInformation.PatientInformation;
import com.medical.mypockethealth.Classes.NotificationSection.ActivationStateInformation;
import com.medical.mypockethealth.Classes.NotificationSection.NotificationInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.DoctorBookingSection.DoctorBookingConfirmFragment;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeBookingStatusCaller implements Runnable{

    private static final String TAG = "ChangeBookingStatusCall";

    private final PatientInformation patientInformation;
    private final CallBackFromChangeBookingStatusCaller callBackFromChangeBookingStatusCaller;
    private final String bookingStatus;
    private final Context context;

    public ChangeBookingStatusCaller(PatientInformation patientInformation,
                                     CallBackFromChangeBookingStatusCaller callBackFromChangeBookingStatusCaller, String bookingStatus, Context context) {
        this.patientInformation = patientInformation;
        this.callBackFromChangeBookingStatusCaller = callBackFromChangeBookingStatusCaller;
        this.bookingStatus = bookingStatus;
        this.context = context;
    }

    public interface CallBackFromChangeBookingStatusCaller
    {

        void confirmationCancelBookingCaller(ResponseInformation responseInformation,PatientInformation patientInformation);

    }

    @Override
    public void run() {

        String userMessage=null;
        String title=null;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm");
        Date date = new Date();
        System.out.println(formatter.format(date));


        if(bookingStatus.equals("1"))
        {
            userMessage="Booking has been confirmed by Dr. "+getProviderName()+" on "+formatter.format(date) +"\n"+"Date : "+patientInformation.getDate()+"\nSlot Time : "+patientInformation.getSlotTime();
        }
        else if(bookingStatus.equals("4"))
        {
            userMessage="Video session done with Dr. "+getProviderName()+" on "+formatter.format(date) +"\n"+"Date : "+patientInformation.getDate()+"\nSlot Time : "+patientInformation.getSlotTime();
        }
        else
        {
            userMessage="Booking has been rejected by Dr. "+getProviderName()+" on "+formatter.format(date) +"\n"+"Date : "+patientInformation.getDate()+"\nSlot Time : "+patientInformation.getSlotTime();
        }

        if(bookingStatus.equals("4"))
        {
           title=URLBuilder.Title.complete;
        }
        else
        {
            title=URLBuilder.Title.RequestStatus;
        }


        setUpNotification(userMessage,title);



        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection)new
                    URL(URLBuilder.base_url+URLBuilder.UrlMethods.changeBookingStatus).openConnection();

            httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setDoInput(true);

            String data= URLEncoder.encode(URLBuilder.Parameter.bookingId.toString(),"UTF-8")+"="+URLEncoder.encode(patientInformation.getBookingId(),"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.bookingStatus.toString(),"UTF-8")+"="+URLEncoder.encode(bookingStatus,"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.title.toString(),"UTF-8")+"="+URLEncoder.encode(title,"UTF-8")
                    +"&"+URLEncoder.encode(URLBuilder.Parameter.type.toString(),"UTF-8")+"="+URLEncoder.encode(URLBuilder.Type.ChangeRequestStatus.toString(),"UTF-8")
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


            if(callBackFromChangeBookingStatusCaller!=null)
            {
                ResponseInformation responseInformation=new ResponseInformation();
                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                responseInformation.setMessage("We are having technical issue. Please try again later");
                callBackFromChangeBookingStatusCaller.confirmationCancelBookingCaller(responseInformation,patientInformation);
            }
        }


    }

    private void setUpNotification(String userMessage,String title)
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.Notification);

        String random_key_generator=databaseReference.push().getKey();

        NotificationInformation notificationInformation=new
                NotificationInformation(URLBuilder.Type.ChangeRequestStatus.toString(),title,userMessage,random_key_generator);

        assert random_key_generator != null;

        databaseReference.child(patientInformation.getPatientId()).child(URLBuilder.FirebaseDataNodes.myNotification).child(random_key_generator).setValue(notificationInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                 setNotification();

            }
        });

    }


    private void setNotification()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.Notification);

        databaseReference.child(patientInformation.getPatientId()).child(URLBuilder.
                FirebaseDataNodes.activationStatus).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                String random_key_generator=databaseReference.push().getKey();

                ActivationStateInformation activationStateInformation=new ActivationStateInformation("0",random_key_generator);

                databaseReference.child(patientInformation.getPatientId()).child(URLBuilder.FirebaseDataNodes.activationStatus)
                        .child(random_key_generator).setValue(activationStateInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {



                    }
                });
            }
        });




    }

    private String getProviderName()
    {

        SharedPreferences preferences= context.getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ProviderMainFrame.provider_information_key, "");
        Gson gson = new Gson();
        ProviderInformation providerInformation = gson.fromJson(json, ProviderInformation.class);

        return providerInformation.getFirstName().charAt(0)+" "+providerInformation.getSurName();
    }

    private void jsonParser(String value) throws JSONException {
        

        Gson gson = new Gson();
        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if(callBackFromChangeBookingStatusCaller!=null)
        {
            callBackFromChangeBookingStatusCaller.confirmationCancelBookingCaller(responseInformation,patientInformation);
        }

    }

}
