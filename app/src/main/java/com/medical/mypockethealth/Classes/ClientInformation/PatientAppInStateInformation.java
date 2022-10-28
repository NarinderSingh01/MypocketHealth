package com.medical.mypockethealth.Classes.ClientInformation;

import java.io.Serializable;

public class PatientAppInStateInformation implements Serializable {

    private String patientAppInState;
    private String objectKey;

    public PatientAppInStateInformation(String patientAppInState, String objectKey) {
        this.patientAppInState = patientAppInState;
        this.objectKey = objectKey;
    }

    public PatientAppInStateInformation() {
    }

    public String getPatientAppInState() {
        return patientAppInState;
    }

    public String getObjectKey() {
        return objectKey;
    }
}
