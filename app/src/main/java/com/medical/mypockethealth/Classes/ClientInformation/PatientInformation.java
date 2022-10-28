package com.medical.mypockethealth.Classes.ClientInformation;

import java.io.Serializable;

public class PatientInformation implements Serializable {
    
    private String patientName;
    private String patientAge;
    private String date;
    private String slotTime;
    private String requestKey;
    private String profileImage;
    private String bookingId;
    private String patientId;

    public PatientInformation() {
    }

    public PatientInformation(String patientName, String patientAge, String date, String slotTime, String requestKey, String profileImage, String bookingId,String patientId) {
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.date = date;
        this.slotTime = slotTime;
        this.requestKey = requestKey;
        this.profileImage = profileImage;
        this.bookingId = bookingId;
        this.patientId=patientId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public String getDate() {
        return date;
    }

    public String getSlotTime() {
        return slotTime;
    }
}
