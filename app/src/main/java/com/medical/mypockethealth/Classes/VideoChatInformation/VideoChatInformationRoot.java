package com.medical.mypockethealth.Classes.VideoChatInformation;

import java.io.Serializable;

public class VideoChatInformationRoot implements Serializable {

    public String success;
    public String message;
    public VideoChatInformation details;

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

    public VideoChatInformation getDetails() {
        return details;
    }

    public void setDetails(VideoChatInformation details) {
        this.details = details;
    }
}
