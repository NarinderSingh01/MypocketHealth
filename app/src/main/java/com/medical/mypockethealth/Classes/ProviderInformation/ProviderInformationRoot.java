package com.medical.mypockethealth.Classes.ProviderInformation;

import java.io.Serializable;

public class ProviderInformationRoot implements Serializable {

    public String success;
    public String message;
    public ProviderInformation details;

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

    public ProviderInformation getDetails() {
        return details;
    }

    public void setDetails(ProviderInformation details) {
        this.details = details;
    }
}
