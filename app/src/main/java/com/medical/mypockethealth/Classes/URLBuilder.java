package com.medical.mypockethealth.Classes;

public class URLBuilder {

    public static final String base_url="http://3.129.200.183/app/api/";
    public static final String policy_link= "http://3.129.200.183/app/privacy-policy";
    public static final String appUpdateFile="appUpdateFile";
    public static final String appUpdateKey="appUpdateKey";
    public static final String  X_RapidAPI_Host="priaid-symptom-checker-v1.p.rapidapi.com";
    public static final String X_RapidAPI_Key="11637b8a6emsha68c160e8250a3ap1d3fe6jsn82310cdf389c";

    public static class UrlMethods {
        public static String medicalAid="medical-aid";
        public static String uniqueApi="verify-phone";
        public static String updatePassword="update-password";
        public static String forgotPassword="forgot-password";
        public static String userRegister="user-register";
        public static String morbidity="comorbidities";
        public static String clientInformation="care-journey-information";
        public static String logout="logout";
        public static String login="login";
        public static String socialLogin="social-login";
        public static String providerRegister="provider-register";
        public static String specialityList="speciality-list";
        public static String emailChecker="email-checker";
        public static String addDoctorSlot="add-provider-time-slot";
        public static String getDoctorSlot="get-provider-time-slot";
        public static String deleteDoctorSlot="delete-provider-time-slot";
        public static String getConsultCharges="get-charges-list";
        public static String getDoctorsList="get-all-providers";
        public static String getProviderType="get-provider-types";
        public static String getProviderSlots="get-provider-single-time-slot";
        public static String bookMySlot="book-doctor-slot";
        public static String getUpcomingBooking="get-upcoming-booking";
        public static String getPastBooking="get-complete-booking";
        public static String cancelBooking="cancel-booking";
        public static String changeBookingStatus="change-booking-status";
        public static String getTodayPatientList="today-booking";
        public static String getUpcomingPatientList="booking-list";
        public static String getCompletePatientList="complete-booking";
        public static String startVideoChatOperation="request-video-chat-operation";
        public static String accountActivationStatus="account-status";
        public static String createFollowUpSheet="follow-up-sheet";
        public static String createReferralSheet="referral-sheet";
        public static String createSickNoteSheet="medical-certificate-sheet";
        public static String createPrescriptionSheet ="prescription-sheet";
        public static String getEHRFilesStatus="get-documents";
        public static String getFiles ="get-documents";
        public static String otpVerification ="verify-otp";
        public static String getICDDetails ="get-icd-list";
        public static final String payStackEmail="chad@innohealthts.com";
        public static final String policy_link="privacy-policy";
        public static final String changePassword="change-password";
        public static final String editUserProfile="edit-user-profile";
        public static final String editProviderProfile="edit-provider-profile";
        public static final String medicalStatus="medical-aid-status";
        public static final String healthAlert="health-alert";
        public static String getDoctorsBySpecialist="getDoctorsBySpecialist";

    }
    
    public static class FirebaseDataNodes {
        public static String BookingRequest="booking-requests";
        public static String VideoChatRequest="video-chat-requests";
        public static String PatientStateInformation="patient-state-information";
        public static String PatientAppStateInformation="patient-appState-information";
        public static String Notification="notification";
        public static String myNotification="my-notification";
        public static String activationStatus="activation-status";
        public static String myScript="my-script";
        public static String panelChat="panel-chat";
        public static String lastMessage="last-message";
        
    }

    public enum PayStackKeys {
       pk_live_3cb8070e3130e6b187652a6dfbd11efd9cd3919f,
        ZAR
    }

    public enum Type {

        VideoCalling,Chat,BookingRequest,ChangeRequestStatus,VideoChatRequest

    }

    public enum Request {

        GET,POST

    }

    public static class Title {
        public static final String VideoCall="live session";
        public static final String Chat="Chat";
        public static final String Booking="Booking";
        public static final String RequestStatus="Request Status";
        public static final String complete="Session Status";
        public static final String unKnown="live session";
    }

    public enum CurrencySign
    {
        R
    }

    public enum Parameter
    {
        username,medicalNumber,phone,email,occupation,password,deviceType,userType,userTitle,deviceId,regId,modeType,IdNumber,MpNumber,providerRegister,
        name,surname,idNumber,mpNumber,fICA,registerAs,loginType,degreeType,speciality,experience,hospitalName,address,
        city,suburb,zipCode,consultCharges,doctorCertificate,about,qualification,providerId,id,profileImage,specialityId,specialistId,
        date,morningVisibilityStatus,morningStartTime,morningEndTime,morningPerSlot,afternoonVisibilitiyStatus,afternoonStartTime ,
        afternoonEndTime,afternoonPerSlot,eveningVisibilityStatus,eveningStartTime,eveningEndTime,eveningPerSlot,
        patientName,patientAge,patientProblem,userId,slotTime,startDate,endDate,orderId,socialId,
        channelName,bookingId,patientId,title,type,userMessage,status,phoneNumber,firstName,surName,medicalAid,morbidity,allergies,medicineList,
        ame,countryCode,postalCode,professionalType,professionalRegistrationNumber,department,workLocation,specialization,onlineOPDStatus,
        ficaDocuments,registrationDocuments,lat,log,bio,afternoonVisibilityStatus,prescriptionImage,bookingStatus,providerMessage,
        toke,rtmToken,mainId,message,aidNumber,ehrStatus,followUpDetail,followUpInformation,fillInformation,examinationDate,fromDate,upToDate,natureIllness,
        informationName,IDNumber,onDuty,providerName,requestId,otp,patientContact,diagnosis,description,quantity,partName,signature,scriptList,
        practiceNumber,practiceNumberPhone
    }

}


