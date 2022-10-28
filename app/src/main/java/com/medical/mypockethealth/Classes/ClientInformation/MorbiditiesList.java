package com.medical.mypockethealth.Classes.ClientInformation;

import java.io.Serializable;

public class MorbiditiesList implements Serializable {

    public String id;
    public String morbidityName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMorbidityName() {
        return morbidityName;
    }

    public void setMorbidityName(String morbidityName) {
        this.morbidityName = morbidityName;
    }
}
