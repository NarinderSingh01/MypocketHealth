package com.medical.mypockethealth.Classes.ProfessionalTypeInformation;

import java.io.Serializable;
import java.util.List;

public class ProfessionalTypeInformationRoot implements Serializable {

    public String success;
    public String message;
    public List<ProfessionalTypeInformation> details;


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

    public List<ProfessionalTypeInformation> getDetails() {
        return details;
    }

    public void setDetails(List<ProfessionalTypeInformation> details) {
        this.details = details;
    }
}
