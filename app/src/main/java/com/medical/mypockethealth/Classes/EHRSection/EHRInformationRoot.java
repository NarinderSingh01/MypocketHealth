package com.medical.mypockethealth.Classes.EHRSection;

import java.io.Serializable;

public class EHRInformationRoot implements Serializable {

    public String success;
    public String message;
    public EHRFilesInformation details;


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

    public EHRFilesInformation getDetails() {
        return details;
    }

    public void setDetails(EHRFilesInformation details) {
        this.details = details;
    }
}
