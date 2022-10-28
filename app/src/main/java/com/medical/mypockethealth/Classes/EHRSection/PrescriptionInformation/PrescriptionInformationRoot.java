package com.medical.mypockethealth.Classes.EHRSection.PrescriptionInformation;

import java.io.Serializable;
import java.util.List;

public class PrescriptionInformationRoot implements Serializable {

    public String success;
    public String message;
    public List<PrescriptionInformation> details;
    public String url;

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

    public List<PrescriptionInformation> getDetails() {
        return details;
    }

    public void setDetails(List<PrescriptionInformation> details) {
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
