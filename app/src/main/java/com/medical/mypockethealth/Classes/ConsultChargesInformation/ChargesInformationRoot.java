package com.medical.mypockethealth.Classes.ConsultChargesInformation;

import java.io.Serializable;
import java.util.List;

public class ChargesInformationRoot implements Serializable {

    public String success;
    public String message;
    public List<ChargesInformation> details;


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

    public List<ChargesInformation> getDetails() {
        return details;
    }

    public void setDetails(List<ChargesInformation> details) {
        this.details = details;
    }
}
