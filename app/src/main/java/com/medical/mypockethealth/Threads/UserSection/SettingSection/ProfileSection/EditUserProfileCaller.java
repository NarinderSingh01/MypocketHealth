package com.medical.mypockethealth.Threads.UserSection.SettingSection.ProfileSection;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.gson.Gson;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformationRoot;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformationRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.SettingSection.ProfileSection.CreateProfileSection.CreateProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class EditUserProfileCaller implements Runnable {

    private static final String TAG = "EditProfileCaller";

    private final ClientInformation clientInformation;
    private final CallbackFromEditProfileCaller callbackFromEditProfileCaller;

    public EditUserProfileCaller(ClientInformation clientInformation, CallbackFromEditProfileCaller callbackFromEditProfileCaller) {
        this.clientInformation = clientInformation;
        this.callbackFromEditProfileCaller = callbackFromEditProfileCaller;
    }


    public interface CallbackFromEditProfileCaller{

        void confirmationEditProfileCaller(ResponseInformation responseInformation);

        void informationEditProfileCaller(ClientInformation clientInformation);

    }

    @Override
    public void run() {


        if (!clientInformation.getProfileImage().equals(CreateProfileFragment.ImageStatus.notSelected.toString())) {

            AndroidNetworking.upload(URLBuilder.base_url + URLBuilder.UrlMethods.editUserProfile)
                    .addMultipartParameter(URLBuilder.Parameter.userId.toString(), ClientMainFrame.id)
                    .addMultipartParameter(URLBuilder.Parameter.email.toString(), clientInformation.getEmail())
                    .addMultipartParameter(URLBuilder.Parameter.firstName.toString(), clientInformation.getFirstName())
                    .addMultipartParameter(URLBuilder.Parameter.surName.toString(), clientInformation.getSurName())
                    .addMultipartFile(URLBuilder.Parameter.profileImage.toString(),new File(clientInformation.getProfileImage()))
                    .setTag("uploadTest")
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {

                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                jsonParser(response.toString());

                            } catch (JSONException e) {


                                if (callbackFromEditProfileCaller != null) {
                                    ResponseInformation responseInformation = new ResponseInformation();
                                    responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                                    responseInformation.setMessage("We are having technical issue. Please try again later");
                                    callbackFromEditProfileCaller.confirmationEditProfileCaller(responseInformation);
                                }

                            }


                        }

                        @Override
                        public void onError(ANError error) {

                            if (callbackFromEditProfileCaller != null) {
                                ResponseInformation responseInformation = new ResponseInformation();
                                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                                responseInformation.setMessage("We are having technical issue. Please try again later");
                                callbackFromEditProfileCaller.confirmationEditProfileCaller(responseInformation);
                            }
                        }
                    });


        } else {

            Log.d(TAG, "run: else called");

            AndroidNetworking.upload(URLBuilder.base_url + URLBuilder.UrlMethods.editUserProfile)
                    .addMultipartParameter(URLBuilder.Parameter.userId.toString(), ClientMainFrame.id)
                    .addMultipartParameter(URLBuilder.Parameter.email.toString(), clientInformation.getEmail())
                    .addMultipartParameter(URLBuilder.Parameter.firstName.toString(), clientInformation.getFirstName())
                    .addMultipartParameter(URLBuilder.Parameter.surName.toString(), clientInformation.getSurName())

                    .setTag("uploadTest")
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {

                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                jsonParser(response.toString());

                            } catch (JSONException e) {

                                if (callbackFromEditProfileCaller != null) {
                                    ResponseInformation responseInformation = new ResponseInformation();
                                    responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                                    responseInformation.setMessage("We are having technical issue. Please try again later");
                                    callbackFromEditProfileCaller.confirmationEditProfileCaller(responseInformation);
                                }

                            }


                        }

                        @Override
                        public void onError(ANError error) {

                            if (callbackFromEditProfileCaller != null) {
                                ResponseInformation responseInformation = new ResponseInformation();
                                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                                responseInformation.setMessage("We are having technical issue. Please try again later");
                                callbackFromEditProfileCaller.confirmationEditProfileCaller(responseInformation);
                            }
                        }
                    });


        }


    }

    private void jsonParser(String value) throws JSONException {


        Log.d(TAG, "jsonParser: " + value);
        Gson gson = new Gson();

        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {

            ClientInformationRoot clientInformationRoot = gson.fromJson(value, ClientInformationRoot.class);

            if (callbackFromEditProfileCaller != null) {

                callbackFromEditProfileCaller.informationEditProfileCaller(clientInformationRoot.getDetails());
            }


        } else {
            if (callbackFromEditProfileCaller != null) {

                callbackFromEditProfileCaller.confirmationEditProfileCaller(responseInformation);
            }

        }

    }
}
