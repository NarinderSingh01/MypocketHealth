package com.medical.mypockethealth.Classes.SpecialitiesInformation;

import java.io.Serializable;
import java.util.List;

public class SpecialitiesInformationRoot implements Serializable {

    public String success;
    public String message;
    public List<SpecialitiesInformation> details;


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

    public List<SpecialitiesInformation> getDetails() {
        return details;
    }

    public void setDetails(List<SpecialitiesInformation> details) {
        this.details = details;
    }
}
