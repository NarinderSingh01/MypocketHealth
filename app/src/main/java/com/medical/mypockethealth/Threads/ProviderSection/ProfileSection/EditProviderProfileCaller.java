package com.medical.mypockethealth.Threads.ProviderSection.ProfileSection;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformationRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.google.gson.Gson;
import com.medical.mypockethealth.Classes.URLBuilder;

import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.SettingSection.ProfileSection.CreateProfileSection.CreateProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class EditProviderProfileCaller implements Runnable{

    private static final String TAG = "EditProfileCaller";

    private final ProviderInformation providerInformation;

    private final CallbackFromEditProfileCaller callbackFromEditProfileCaller;

    public EditProviderProfileCaller(ProviderInformation providerInformation, CallbackFromEditProfileCaller callbackFromEditProfileCaller) {
        this.providerInformation = providerInformation;
        this.callbackFromEditProfileCaller = callbackFromEditProfileCaller;
    }


    public interface CallbackFromEditProfileCaller
    {

        void confirmationEditProfileCaller(ResponseInformation responseInformation);
        void informationEditProfileCaller(ProviderInformation providerInformation);
    }

    @Override
    public void run() {


        if (!providerInformation.getProfileImage().equals(CreateProfileFragment.ImageStatus.notSelected.toString())) {

            AndroidNetworking.upload(URLBuilder.base_url+URLBuilder.UrlMethods.editProviderProfile)
                    .addMultipartParameter(URLBuilder.Parameter.userId.toString(), ProviderMainFrame.id)
                    .addMultipartParameter(URLBuilder.Parameter.email.toString(), providerInformation.getEmail())
                    .addMultipartParameter(URLBuilder.Parameter.userTitle.toString(), providerInformation.getUserTitle())
                    .addMultipartParameter(URLBuilder.Parameter.firstName.toString(), providerInformation.getFirstName())
                    .addMultipartParameter(URLBuilder.Parameter.surName.toString(), providerInformation.getSurName())
                    .addMultipartParameter(URLBuilder.Parameter.practiceNumberPhone.toString(), providerInformation.getPracticeNumberPhone())
                    .addMultipartParameter(URLBuilder.Parameter.address.toString(), providerInformation.getAddress())
                    .addMultipartParameter(URLBuilder.Parameter.city.toString(), providerInformation.getCity())
                    .addMultipartParameter(URLBuilder.Parameter.postalCode.toString(), providerInformation.getPostalCode())
                    .addMultipartParameter(URLBuilder.Parameter.department.toString(), providerInformation.getDepartment())
                    .addMultipartParameter(URLBuilder.Parameter.workLocation.toString(), providerInformation.getWorkLocation())
                    .addMultipartParameter(URLBuilder.Parameter.experience.toString(), providerInformation.getExperience())
                    .addMultipartParameter(URLBuilder.Parameter.consultCharges.toString(), providerInformation.getConsultCharges())
                    .addMultipartFile(URLBuilder.Parameter.profileImage.toString(),new File(providerInformation.getProfileImage()))
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


            AndroidNetworking.upload(URLBuilder.base_url + URLBuilder.UrlMethods.editProviderProfile)
                    .addMultipartParameter(URLBuilder.Parameter.userId.toString(), ProviderMainFrame.id)
                    .addMultipartParameter(URLBuilder.Parameter.email.toString(), providerInformation.getEmail())
                    .addMultipartParameter(URLBuilder.Parameter.firstName.toString(), providerInformation.getFirstName())
                    .addMultipartParameter(URLBuilder.Parameter.surName.toString(), providerInformation.getSurName())
                    .addMultipartParameter(URLBuilder.Parameter.address.toString(), providerInformation.getAddress())
                    .addMultipartParameter(URLBuilder.Parameter.city.toString(), providerInformation.getCity())
                    .addMultipartParameter(URLBuilder.Parameter.practiceNumberPhone.toString(), providerInformation.getPracticeNumberPhone())
                    .addMultipartParameter(URLBuilder.Parameter.postalCode.toString(), providerInformation.getPostalCode())
                    .addMultipartParameter(URLBuilder.Parameter.department.toString(), providerInformation.getDepartment())
                    .addMultipartParameter(URLBuilder.Parameter.workLocation.toString(), providerInformation.getWorkLocation())
                    .addMultipartParameter(URLBuilder.Parameter.experience.toString(), providerInformation.getExperience())
                    .addMultipartParameter(URLBuilder.Parameter.consultCharges.toString(), providerInformation.getConsultCharges())

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


        Gson gson = new Gson();

        ResponseInformation responseInformation = gson.fromJson(value,ResponseInformation.class);

        if(!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type)))
        {

            ProviderInformationRoot providerInformationRoot = gson.fromJson(value, ProviderInformationRoot.class);

            if(callbackFromEditProfileCaller!=null)
            {

                callbackFromEditProfileCaller.informationEditProfileCaller(providerInformationRoot.getDetails());
            }


        }

        else
        {
            if(callbackFromEditProfileCaller!=null)
            {

                callbackFromEditProfileCaller.confirmationEditProfileCaller(responseInformation);
            }

        }

    }
}
