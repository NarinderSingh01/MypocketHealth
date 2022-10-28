package com.medical.mypockethealth.Classes.NotificationSection;

import java.io.Serializable;

public class NotificationInformation implements Serializable {
    
    public String type;
    public String title;
    public String message;
    public String random_key_generator;


    public NotificationInformation() {
    }

    public NotificationInformation(String type, String title, String message, String random_key_generator) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.random_key_generator=random_key_generator;
    }


    public String getRandom_key_generator() {
        return random_key_generator;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
