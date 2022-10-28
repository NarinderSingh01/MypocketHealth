package com.medical.mypockethealth.Classes.NotificationSection;

import java.io.Serializable;

public class ActivationStateInformation implements Serializable {

    private String state;
    private String key;

    public ActivationStateInformation(String state,String key) {
        this.state = state;
        this.key=key;
    }

    public ActivationStateInformation() {
    }


    public String getKey() {
        return key;
    }

    public String getState() {
        return state;
    }
}
