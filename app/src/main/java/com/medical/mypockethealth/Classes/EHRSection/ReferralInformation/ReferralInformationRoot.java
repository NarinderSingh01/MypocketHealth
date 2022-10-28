package com.medical.mypockethealth.Classes.EHRSection.ReferralInformation;

import java.io.Serializable;
import java.util.List;

public class ReferralInformationRoot implements Serializable {

    public String success;
    public String message;
    public List<ReferralInformation> details;
    public String url;

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

    public List<ReferralInformation> getDetails() {
        return details;
    }

    public void setDetails(List<ReferralInformation> details) {
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
