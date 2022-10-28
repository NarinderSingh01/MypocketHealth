package com.medical.mypockethealth.Classes.MedicalAidInformation;

import java.io.Serializable;
import java.util.List;

public class MedicalAidInformationRoot implements Serializable {

    public String success;
    public String message;
    public List<MedicalAidInformation> details;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MedicalAidInformation> getDetails() {
        return details;
    }

    public void setDetails(List<MedicalAidInformation> details) {
        this.details = details;
    }
}
