package com.medical.mypockethealth.Classes.ICRInformation;

import java.io.Serializable;
import java.util.ArrayList;

public class ICRInformationRoot implements Serializable {

    public String success;
    public String message;
    public ArrayList<ArrayList<String>> details;


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

    public ArrayList<ArrayList<String>> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<ArrayList<String>> details) {
        this.details = details;
    }
}
