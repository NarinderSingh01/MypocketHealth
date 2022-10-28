package com.medical.mypockethealth.Classes.ClientInformation;

import java.io.Serializable;

public class ClientInformationRoot implements Serializable {

    public String success;
    public String message;
    public ClientInformation details;


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

    public ClientInformation getDetails() {
        return details;
    }

    public void setDetails(ClientInformation details) {
        this.details = details;
    }
}
