package com.medical.mypockethealth.Classes.SlotsSection.ClientSection;

import java.io.Serializable;

public class SlotDetailsInformationRoot implements Serializable {

    public String success;
    public String message;
    public SlotsDetailsInformation details;

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

    public SlotsDetailsInformation getDetails() {
        return details;
    }

    public void setDetails(SlotsDetailsInformation details) {
        this.details = details;
    }
}
