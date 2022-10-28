package com.medical.mypockethealth.Classes.SlotsSection;

import java.io.Serializable;
import java.util.List;

public class SlotInformationRoot implements Serializable {

    public String success;
    public List<SlotInformation> details;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<SlotInformation> getDetails() {
        return details;
    }

    public void setDetails(List<SlotInformation> details) {
        this.details = details;
    }
}
