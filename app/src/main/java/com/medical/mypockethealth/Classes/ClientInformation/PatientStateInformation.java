package com.medical.mypockethealth.Classes.ClientInformation;

import java.io.Serializable;

public class PatientStateInformation implements Serializable {

   private String patientCallingState;
   private String objectKey;


    public PatientStateInformation(String patientCallingState, String objectKey) {
        this.patientCallingState = patientCallingState;
        this.objectKey = objectKey;
    }

    public PatientStateInformation() {
    }


    public String getObjectKey() {
        return objectKey;
    }

    public String getPatientCallingState() {
        return patientCallingState;
    }
}
