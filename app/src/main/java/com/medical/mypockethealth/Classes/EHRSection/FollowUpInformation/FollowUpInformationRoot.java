package com.medical.mypockethealth.Classes.EHRSection.FollowUpInformation;

import java.io.Serializable;
import java.util.List;

public class FollowUpInformationRoot implements Serializable {

    public String success;
    public String message;
    public List<FollowUpInformation> details;
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

    public List<FollowUpInformation> getDetails() {
        return details;
    }

    public void setDetails(List<FollowUpInformation> details) {
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
