package com.medical.mypockethealth.Classes.ClientInformation;

import java.io.Serializable;
import java.util.List;

public class BookedClientInformationRoot implements Serializable {

    public String success;
    public String message;
    public List<ClientInformation> details;


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

    public List<ClientInformation> getDetails() {
        return details;
    }

    public void setDetails(List<ClientInformation> details) {
        this.details = details;
    }
}
