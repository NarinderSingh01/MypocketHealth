package com.medical.mypockethealth.Threads.BaseThreads.ProviderSection;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.gson.Gson;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformationRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.SettingSection.ProfileSection.CreateProfileSection.CreateProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class ProviderRegisterCaller implements Runnable {

    private static final String TAG = "MerchantRegisterCaller";
    private final ProviderInformation providerInformation;
    private final CallbackFromProviderRegisterCaller callbackFromMerchantRegisterCaller;

    public ProviderRegisterCaller(ProviderInformation providerInformation, CallbackFromProviderRegisterCaller callbackFromMerchantRegisterCaller) {
        this.providerInformation = providerInformation;
        this.callbackFromMerchantRegisterCaller = callbackFromMerchantRegisterCaller;
    }

    public interface CallbackFromProviderRegisterCaller {

        void confirmationProviderRegisterCaller(ResponseInformation responseInformation);

        void informationProviderRegisterCaller(ProviderInformation clientInformation);

    }

    @Override
    public void run() {

        if (!providerInformation.getProfileImage().equals(CreateProfileFragment.ImageStatus.notSelected.toString())) {

            Log.d(TAG, "run: " + providerInformation.getUserType());
            Log.d(TAG, "run: " + providerInformation.getUserTitle());

            AndroidNetworking.upload(URLBuilder.base_url + URLBuilder.UrlMethods.providerRegister)
                    .addMultipartParameter(URLBuilder.Parameter.email.toString(), providerInformation.getEmail())
                    .addMultipartParameter(URLBuilder.Parameter.firstName.toString(), providerInformation.getFirstName())
                    .addMultipartParameter(URLBuilder.Parameter.surName.toString(), providerInformation.getSurName())
                    .addMultipartParameter(URLBuilder.Parameter.countryCode.toString(), providerInformation.getCountryCode())
                    .addMultipartParameter(URLBuilder.Parameter.phoneNumber.toString(), providerInformation.getPhoneNumber())
                    .addMultipartParameter(URLBuilder.Parameter.consultCharges.toString(), providerInformation.getConsultCharges())
                    .addMultipartParameter(URLBuilder.Parameter.address.toString(), providerInformation.getAddress())
                    .addMultipartParameter(URLBuilder.Parameter.city.toString(), providerInformation.getCity())
                    .addMultipartParameter(URLBuilder.Parameter.practiceNumberPhone.toString(), providerInformation.getPracticeNumberPhone())
                    .addMultipartParameter(URLBuilder.Parameter.profileImage.toString(), providerInformation.getProfileImage())
                    .addMultipartFile(URLBuilder.Parameter.signature.toString(), providerInformation.getDigitalSignature())
                    .addMultipartParameter(URLBuilder.Parameter.postalCode.toString(), providerInformation.getPostalCode())
                    .addMultipartParameter(URLBuilder.Parameter.professionalType.toString(), providerInformation.getProfessionalType())
                    .addMultipartParameter(URLBuilder.Parameter.professionalRegistrationNumber.toString(), providerInformation.getProfessionalRegistrationNumber())
                    .addMultipartParameter(URLBuilder.Parameter.department.toString(), providerInformation.getDepartment())
                    .addMultipartParameter(URLBuilder.Parameter.practiceNumber.toString(), providerInformation.getPracticeNumber())
                    .addMultipartParameter(URLBuilder.Parameter.workLocation.toString(), providerInformation.getWorkLocation())
                    .addMultipartParameter(URLBuilder.Parameter.specialization.toString(), providerInformation.getSpecialization())
                    .addMultipartParameter(URLBuilder.Parameter.onlineOPDStatus.toString(), providerInformation.getOnlineOPDStatus())
                    .addMultipartParameter(URLBuilder.Parameter.experience.toString(), providerInformation.getExperience())
                    .addMultipartParameter(URLBuilder.Parameter.ficaDocuments.toString(), providerInformation.getFicaDocument())
                    .addMultipartParameter(URLBuilder.Parameter.registrationDocuments.toString(), providerInformation.getRegistrationDocument())
                    .addMultipartParameter(URLBuilder.Parameter.lat.toString(), providerInformation.getLat())
                    .addMultipartParameter(URLBuilder.Parameter.log.toString(), providerInformation.getLog())
                    .addMultipartParameter(URLBuilder.Parameter.bio.toString(), providerInformation.getBio())
                    .addMultipartParameter(URLBuilder.Parameter.password.toString(), providerInformation.getPassword())
                    .addMultipartParameter(URLBuilder.Parameter.regId.toString(), providerInformation.getRegId())
                    .addMultipartParameter(URLBuilder.Parameter.deviceId.toString(), providerInformation.getDeviceId())
                    .addMultipartParameter(URLBuilder.Parameter.deviceType.toString(), providerInformation.getDeviceType())
                    .addMultipartParameter(URLBuilder.Parameter.userType.toString(), providerInformation.getUserType())
                    .addMultipartParameter(URLBuilder.Parameter.userTitle.toString(), providerInformation.getUserTitle())
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

                                if (callbackFromMerchantRegisterCaller != null) {
                                    ResponseInformation responseInformation = new ResponseInformation();
                                    responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                                    responseInformation.setMessage("We are having technical issue. Please try again later");
                                    callbackFromMerchantRegisterCaller.confirmationProviderRegisterCaller(responseInformation);
                                }

                            }


                        }

                        @Override
                        public void onError(ANError error) {

                            if (callbackFromMerchantRegisterCaller != null) {
                                ResponseInformation responseInformation = new ResponseInformation();
                                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                                responseInformation.setMessage("We are having technical issue. Please try again later");
                                callbackFromMerchantRegisterCaller.confirmationProviderRegisterCaller(responseInformation);
                            }
                        }
                    });


        } else {

            Log.d(TAG, "run: " + providerInformation.getUserType());
            Log.d(TAG, "run: " + providerInformation.getUserTitle());

            AndroidNetworking.upload(URLBuilder.base_url + URLBuilder.UrlMethods.providerRegister)
                    .addMultipartParameter(URLBuilder.Parameter.email.toString(), providerInformation.getEmail())
                    .addMultipartParameter(URLBuilder.Parameter.firstName.toString(), providerInformation.getFirstName())
                    .addMultipartParameter(URLBuilder.Parameter.surName.toString(), providerInformation.getSurName())
                    .addMultipartParameter(URLBuilder.Parameter.countryCode.toString(), providerInformation.getCountryCode())
                    .addMultipartParameter(URLBuilder.Parameter.phoneNumber.toString(), providerInformation.getPhoneNumber())
                    .addMultipartParameter(URLBuilder.Parameter.address.toString(), providerInformation.getAddress())
                    .addMultipartParameter(URLBuilder.Parameter.city.toString(), providerInformation.getCity())
                    .addMultipartParameter(URLBuilder.Parameter.practiceNumberPhone.toString(), providerInformation.getPracticeNumberPhone())
                    .addMultipartParameter(URLBuilder.Parameter.profileImage.toString(), providerInformation.getProfileImage())
                    .addMultipartFile(URLBuilder.Parameter.signature.toString(), providerInformation.getDigitalSignature())
                    .addMultipartParameter(URLBuilder.Parameter.practiceNumber.toString(), providerInformation.getPracticeNumber())
                    .addMultipartParameter(URLBuilder.Parameter.experience.toString(), providerInformation.getExperience())
                    .addMultipartParameter(URLBuilder.Parameter.consultCharges.toString(), providerInformation.getConsultCharges())
                    .addMultipartParameter(URLBuilder.Parameter.postalCode.toString(), providerInformation.getPostalCode())
                    .addMultipartParameter(URLBuilder.Parameter.professionalType.toString(), providerInformation.getProfessionalType())
                    .addMultipartParameter(URLBuilder.Parameter.professionalRegistrationNumber.toString(), providerInformation.getProfessionalRegistrationNumber())
                    .addMultipartParameter(URLBuilder.Parameter.department.toString(), providerInformation.getDepartment())
                    .addMultipartParameter(URLBuilder.Parameter.workLocation.toString(), providerInformation.getWorkLocation())
                    .addMultipartParameter(URLBuilder.Parameter.specialization.toString(), providerInformation.getSpecialization())
                    .addMultipartParameter(URLBuilder.Parameter.onlineOPDStatus.toString(), providerInformation.getOnlineOPDStatus())
                    .addMultipartParameter(URLBuilder.Parameter.ficaDocuments.toString(), providerInformation.getFicaDocument())
                    .addMultipartParameter(URLBuilder.Parameter.registrationDocuments.toString(), providerInformation.getRegistrationDocument())
                    .addMultipartParameter(URLBuilder.Parameter.lat.toString(), providerInformation.getLat())
                    .addMultipartParameter(URLBuilder.Parameter.log.toString(), providerInformation.getLog())
                    .addMultipartParameter(URLBuilder.Parameter.bio.toString(), providerInformation.getBio())
                    .addMultipartParameter(URLBuilder.Parameter.password.toString(), providerInformation.getPassword())
                    .addMultipartParameter(URLBuilder.Parameter.regId.toString(), providerInformation.getRegId())
                    .addMultipartParameter(URLBuilder.Parameter.deviceId.toString(), providerInformation.getDeviceId())
                    .addMultipartParameter(URLBuilder.Parameter.deviceType.toString(), providerInformation.getDeviceType())
                    .addMultipartParameter(URLBuilder.Parameter.userType.toString(), providerInformation.getUserType())
                    .addMultipartParameter(URLBuilder.Parameter.userTitle.toString(), providerInformation.getUserTitle())
                    .setTag("uploadTest").setPriority(Priority.HIGH).build().setUploadProgressListener(new UploadProgressListener() {
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

                                if (callbackFromMerchantRegisterCaller != null) {
                                    ResponseInformation responseInformation = new ResponseInformation();
                                    responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                                    responseInformation.setMessage("We are having technical issue. Please try again later");
                                    callbackFromMerchantRegisterCaller.confirmationProviderRegisterCaller(responseInformation);
                                }

                            }


                        }

                        @Override
                        public void onError(ANError error) {

                            if (callbackFromMerchantRegisterCaller != null) {
                                ResponseInformation responseInformation = new ResponseInformation();
                                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
                                responseInformation.setMessage("We are having technical issue. Please try again later");
                                callbackFromMerchantRegisterCaller.confirmationProviderRegisterCaller(responseInformation);
                            }
                        }
                    });

        }
    }


    private void jsonParser(String value) throws JSONException {

        Gson gson = new Gson();

        ResponseInformation responseInformation = gson.fromJson(value, ResponseInformation.class);

        if (!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {

            ProviderInformationRoot providerInformationRoot = gson.fromJson(value, ProviderInformationRoot.class);

            if (callbackFromMerchantRegisterCaller != null) {

                callbackFromMerchantRegisterCaller.informationProviderRegisterCaller(providerInformationRoot.getDetails());
            }


        } else {
            if (callbackFromMerchantRegisterCaller != null) {

                callbackFromMerchantRegisterCaller.confirmationProviderRegisterCaller(responseInformation);
            }

        }

    }
}