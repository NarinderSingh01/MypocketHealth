package com.medical.mypockethealth.Classes.ChatSection;

import java.io.Serializable;

public class ChatInformation implements Serializable {

    private String message;
    private String type;
    private String date;
    private String objectKey;

    public ChatInformation() {
    }

    public ChatInformation(String message, String type, String date, String objectKey) {
        this.message = message;
        this.type = type;
        this.date = date;
        this.objectKey = objectKey;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getObjectKey() {
        return objectKey;
    }
}
