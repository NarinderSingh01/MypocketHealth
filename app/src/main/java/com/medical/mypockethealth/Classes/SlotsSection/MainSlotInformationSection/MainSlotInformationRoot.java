package com.medical.mypockethealth.Classes.SlotsSection.MainSlotInformationSection;

import java.io.Serializable;

public class MainSlotInformationRoot implements Serializable {

    public String success;
    public String message;
    public MainSlotInformation details;

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

    public MainSlotInformation getDetails() {
        return details;
    }

    public void setDetails(MainSlotInformation details) {
        this.details = details;
    }
}
