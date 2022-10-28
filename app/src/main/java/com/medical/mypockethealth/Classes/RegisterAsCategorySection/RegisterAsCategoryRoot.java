package com.medical.mypockethealth.Classes.RegisterAsCategorySection;

import java.io.Serializable;
import java.util.List;

public class RegisterAsCategoryRoot implements Serializable {

    public String success;
    public String message;
    public List<RegisterAsCategoryInformation> details;

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

    public List<RegisterAsCategoryInformation> getDetails() {
        return details;
    }

    public void setDetails(List<RegisterAsCategoryInformation> details) {
        this.details = details;
    }
}
