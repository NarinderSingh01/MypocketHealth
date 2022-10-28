package com.medical.mypockethealth.Threads.ProviderSection.ProfileSection;

import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformationRoot;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.google.gson.Gson;

import org.json.JSONException;

public class CreateProfileCaller implements Runnable {

    private static final String TAG = "CreateProfileCaller";
    private final CallbackFromCreateProfileCaller callbackFromCreateProfileCaller;
    private final ProviderInformation providerInformation;


    public CreateProfileCaller(CallbackFromCreateProfileCaller callbackFromCreateProfileCaller, ProviderInformation providerInformation) {
        this.callbackFromCreateProfileCaller = callbackFromCreateProfileCaller;
        this.providerInformation = providerInformation;
    }

    public interface CallbackFromCreateProfileCaller
    {

        void confirmationCreateProfileCaller(ResponseInformation responseInformation);
        void informationCreateProfileCaller(ProviderInformation providerInformation);
    }

    @Override
    public void run() {


//        if(providerInformation.getProfileImage().equals(CreateProfileFragment.ImageStatus.notSelected.toString())) {
//
//            AndroidNetworking.upload("")
//                    .addMultipartParameter(URLBuilder.Parameter.name.toString(),providerInformation.getName())
//                    .addMultipartParameter(URLBuilder.Parameter.surname.toString(),providerInformation.getSurname())
//                    .addMultipartParameter(URLBuilder.Parameter.degreeType.toString(),providerInformation.getDegreeType())
//                    .addMultipartParameter(URLBuilder.Parameter.qualification.toString(),providerInformation.getQualification())
//                    .addMultipartFile(URLBuilder.Parameter.doctorCertificate.toString(),new File(providerInformation.getDoctorCertificate()))
//                    .addMultipartParameter(URLBuilder.Parameter.speciality.toString(),providerInformation.getSpeciality())
//                    .addMultipartParameter(URLBuilder.Parameter.experience.toString(),providerInformation.getExperience())
//                    .addMultipartParameter(URLBuilder.Parameter.hospitalName.toString(),providerInformation.getHospitalName())
//                    .addMultipartParameter(URLBuilder.Parameter.phone.toString(),providerInformation.getPhone())
//                    .addMultipartParameter(URLBuilder.Parameter.address.toString(),providerInformation.getAddress())
//                    .addMultipartParameter(URLBuilder.Parameter.suburb.toString(),providerInformation.getSuburb())
//                    .addMultipartParameter(URLBuilder.Parameter.city.toString(),providerInformation.getCity())
//                    .addMultipartParameter(URLBuilder.Parameter.zipCode.toString(),providerInformation.getZipCode())
//                    .addMultipartParameter(URLBuilder.Parameter.consultCharges.toString(),providerInformation.getConsultCharges())
//                    .addMultipartParameter(URLBuilder.Parameter.about.toString(),providerInformation.getAbout())
//                    .addMultipartParameter(URLBuilder.Parameter.providerId.toString(),ProviderMainFrame.id)
//
//                    .setTag("uploadTest")
//                    .setPriority(Priority.HIGH)
//                    .build()
//                    .setUploadProgressListener(new UploadProgressListener() {
//                        @Override
//                        public void onProgress(long bytesUploaded, long totalBytes) {
//
//                        }
//                    })
//                    .getAsJSONObject(new JSONObjectRequestListener() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            try {
//
//                                jsonParser(response.toString());
//
//                            } catch (JSONException e) {
//
//                                if(callbackFromCreateProfileCaller!=null)
//                                {
//                                    ResponseInformation responseInformation=new ResponseInformation();
//                                    responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
//                                    responseInformation.setMessage("We are having technical issue. Please try again later");
//                                    callbackFromCreateProfileCaller.confirmationCreateProfileCaller(responseInformation);
//                                }
//
//                            }
//
//
//                        }
//                        @Override
//                        public void onError(ANError error) {
//
//                            if(callbackFromCreateProfileCaller!=null)
//                            {
//                                ResponseInformation responseInformation=new ResponseInformation();
//                                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
//                                responseInformation.setMessage("We are having technical issue. Please try again later");
//                                callbackFromCreateProfileCaller.confirmationCreateProfileCaller(responseInformation);
//                            }
//
//                        }
//                    });
//
//
//        }
//        else
//        {
//
//            AndroidNetworking.upload("")
//                    .addMultipartParameter(URLBuilder.Parameter.name.toString(),providerInformation.getName())
//                    .addMultipartParameter(URLBuilder.Parameter.surname.toString(),providerInformation.getSurname())
//                    .addMultipartParameter(URLBuilder.Parameter.degreeType.toString(),providerInformation.getDegreeType())
//                    .addMultipartParameter(URLBuilder.Parameter.qualification.toString(),providerInformation.getQualification())
//                    .addMultipartFile(URLBuilder.Parameter.doctorCertificate.toString(),new File(providerInformation.getDoctorCertificate()))
//                    .addMultipartFile(URLBuilder.Parameter.profileImage.toString(),new File(providerInformation.getProfileImage()))
//                    .addMultipartParameter(URLBuilder.Parameter.speciality.toString(),providerInformation.getSpeciality())
//                    .addMultipartParameter(URLBuilder.Parameter.experience.toString(),providerInformation.getExperience())
//                    .addMultipartParameter(URLBuilder.Parameter.hospitalName.toString(),providerInformation.getHospitalName())
//                    .addMultipartParameter(URLBuilder.Parameter.phone.toString(),providerInformation.getPhone())
//                    .addMultipartParameter(URLBuilder.Parameter.address.toString(),providerInformation.getAddress())
//                    .addMultipartParameter(URLBuilder.Parameter.suburb.toString(),providerInformation.getSuburb())
//                    .addMultipartParameter(URLBuilder.Parameter.city.toString(),providerInformation.getCity())
//                    .addMultipartParameter(URLBuilder.Parameter.zipCode.toString(),providerInformation.getZipCode())
//                    .addMultipartParameter(URLBuilder.Parameter.consultCharges.toString(),providerInformation.getConsultCharges())
//                    .addMultipartParameter(URLBuilder.Parameter.about.toString(),providerInformation.getAbout())
//                    .addMultipartParameter(URLBuilder.Parameter.providerId.toString(),ProviderMainFrame.id)
//
//                    .setTag("uploadTest")
//                    .setPriority(Priority.HIGH)
//                    .build()
//                    .setUploadProgressListener(new UploadProgressListener() {
//                        @Override
//                        public void onProgress(long bytesUploaded, long totalBytes) {
//
//                        }
//                    })
//                    .getAsJSONObject(new JSONObjectRequestListener() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            try {
//
//                                jsonParser(response.toString());
//
//                            } catch (JSONException e) {
//
//                                if(callbackFromCreateProfileCaller!=null)
//                                {
//                                    ResponseInformation responseInformation=new ResponseInformation();
//                                    responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
//                                    responseInformation.setMessage("We are having technical issue. Please try again later");
//                                    callbackFromCreateProfileCaller.confirmationCreateProfileCaller(responseInformation);
//                                }
//
//                            }
//
//
//                        }
//                        @Override
//                        public void onError(ANError error) {
//
//                            if(callbackFromCreateProfileCaller!=null)
//                            {
//                                ResponseInformation responseInformation=new ResponseInformation();
//                                responseInformation.setSuccess(String.valueOf(ResponseInformation.fail_response_type));
//                                responseInformation.setMessage("We are having technical issue. Please try again later");
//                                callbackFromCreateProfileCaller.confirmationCreateProfileCaller(responseInformation);
//                            }
//
//                        }
//                    });
//
//        }
//



    }

    private void jsonParser(String value) throws JSONException {


        Gson gson = new Gson();

        ResponseInformation responseInformation = gson.fromJson(value,ResponseInformation.class);

        if(!responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type)))
        {

            ProviderInformationRoot providerInformationRoot = gson.fromJson(value, ProviderInformationRoot.class);

            if(callbackFromCreateProfileCaller!=null)
            {

                callbackFromCreateProfileCaller.informationCreateProfileCaller(providerInformationRoot.getDetails());
            }


        }

        else
        {
            if(callbackFromCreateProfileCaller!=null)
            {

                callbackFromCreateProfileCaller.confirmationCreateProfileCaller(responseInformation);
            }

        }

    }
}
