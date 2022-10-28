package com.medical.mypockethealth.Classes.DoctorInformation;

import java.io.Serializable;
import java.util.List;

public class DoctorInformationRoot implements Serializable {

    public String success;
    public String message;
    public List<DoctorInformation> details;

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

    public List<DoctorInformation> getDetails() {
        return details;
    }

    public void setDetails(List<DoctorInformation> details) {
        this.details = details;
    }
}
