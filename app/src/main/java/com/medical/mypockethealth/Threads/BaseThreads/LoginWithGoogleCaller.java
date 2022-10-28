package com.medical.mypockethealth.Threads.BaseThreads;

import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformationRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.SettingSection.ProfileSection.ViewProfileSection.ViewProfileFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class LoginWithGoogleCaller implements Runnable{

    private static final String TAG = "LoginWithGoogleCaller";


    private final Object information;

    private final CallbackFromLoginWithGoogleCaller callbackFromLoginWithGoogleCaller;

    private final int mode;


    public LoginWithGoogleCaller(Object information, CallbackFromLoginWithGoogleCaller callbackFromLoginWithGoogleCaller, int mode) {
        this.information = information;
        this.callbackFromLoginWithGoogleCaller = callbackFromLoginWithGoogleCaller;
        this.mode = mode;
    }


    public interface CallbackFromLoginWithGoogleCaller {

        void confirmationLoginWithGoogleCaller(ResponseInformation responseInformation);

        void informationLoginWithGoogleCaller(Object information, String modeType);
    }

    @Override
    public void run() {


        switch (mode)
        {

            case 0:

                try {

                    ClientInformation clientInformation=(ClientInformation) information;

                    HttpURLConnection httpURLConnection = (HttpURLConnection) new
                            URL(URLBuilder.base_url+URLBuilder.UrlMethods.socialLogin).openConnection();

                    httpURLConnection.setRequestMethod(URLBuilder.Request.POST.toString());

                    httpURLConnection.setDoOutput(true);

                    httpURLConnection.setDoInput(true);

                    String data;

                    if(clientInformation.getProfileImage().equals(ViewProfileFragment.ImageStatus.notSelected.toString()))
                    {

                        data = URLEncoder.encode(URLBuilder.Parameter.username.toString(), "UTF-8") + "=" + URLEncoder.encode(clientInformation.getUsername(), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.email.toString(), "UTF-8") + "=" + URLEncoder.encode(clientInformation.getEmail(), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.profileImage.toString(), "UTF-8") + "=" + URLEncoder.encode(ViewProfileFragment.ImageStatus.notSelected.toString(), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.socialId.toString(), "UTF-8") + "=" + URLEncoder.encode(clientInformation.getSocialId(), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.modeType.toString(), "UTF-8") + "=" + URLEncoder.encode(String.valueOf(mode), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.regId.toString(), "UTF-8") + "=" + URLEncoder.encode(clientInformation.getRegId(), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.deviceId.toString(), "UTF-8") + "=" + URLEncoder.encode(clientInformation.getDeviceId(), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.deviceType.toString(), "UTF-8") + "=" + URLEncoder.encode(clientInformation.getDeviceType(), "UTF-8");

                    }

                    else
                    {


                        data = URLEncoder.encode(URLBuilder.Parameter.username.toString(), "UTF-8") + "=" + URLEncoder.encode(clientInformation.getUsername(), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.email.toString(), "UTF-8") + "=" + URLEncoder.encode(clientInformation.getEmail(), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.profileImage.toString(), "UTF-8") + "=" + URLEncoder.encode(clientInformation.getProfileImage(), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.socialId.toString(), "UTF-8") + "=" + URLEncoder.encode(clientInformation.getSocialId(), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.modeType.toString(), "UTF-8") + "=" + URLEncoder.encode(String.valueOf(mode), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.regId.toString(), "UTF-8") + "=" + URLEncoder.encode(clientInformation.getRegId(), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.deviceId.toString(), "UTF-8") + "=" + URLEncoder.encode(clientInformation.getDeviceId(), "UTF-8")
                                + "&" + URLEncoder.encode(URLBuilder.Parameter.deviceType.toString(), "UTF-8") + "=" + URLEncoder.encode(clientInformation.getDeviceType(), "UTF-8");


                    }


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


                    if (callbackFromLoginWithGoogleCaller != null) {
                        ResponseInformation responseInformation = new ResponseInformation();
                        responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                        responseInformation.setMessage("We are having technical issue. Please try again later");
                        callbackFromLoginWithGoogleCaller.confirmationLoginWithGoogleCaller(responseInformation);
                    }

                }

                break;

            case 1:


                break;

        }

    }


    private String getChatID()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

        return "chatID"+databaseReference.push().getKey();

    }

    private void jsonParser(String value) throws JSONException {

        Gson gson = new Gson();
        ClientInformationRoot clientInformationRoot = gson.fromJson(value,ClientInformationRoot.class);

        ClientInformation clientInformation=clientInformationRoot.getDetails();

        if(!clientInformationRoot.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type)))
        {

            if (callbackFromLoginWithGoogleCaller != null) {

                if (clientInformation.getModeType().equals(ResponseInformation.userMode)) {

                    callbackFromLoginWithGoogleCaller.informationLoginWithGoogleCaller(clientInformation,clientInformation.getModeType());

                } else if (clientInformation.getModeType().equals((ResponseInformation.providerMode))) {

                    callbackFromLoginWithGoogleCaller.informationLoginWithGoogleCaller(clientInformation,clientInformation.getModeType());
                }
            }
        }
        else
        {
            if (callbackFromLoginWithGoogleCaller != null) {

                ResponseInformation responseInformation = gson.fromJson(value,ResponseInformation.class);
                callbackFromLoginWithGoogleCaller.confirmationLoginWithGoogleCaller(responseInformation);
            }
        }

    }
}

