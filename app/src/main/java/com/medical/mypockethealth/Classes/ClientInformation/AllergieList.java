package com.medical.mypockethealth.Classes.ClientInformation;

import java.io.Serializable;

public class AllergieList implements Serializable {

    public String id;
    public String allergyName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAllergyName() {
        return allergyName;
    }

    public void setAllergyName(String allergyName) {
        this.allergyName = allergyName;
    }
}
