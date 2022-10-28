package com.medical.mypockethealth.Classes.SlotsSection.ClientSection;

import java.io.Serializable;

public class AfternoonSlotsInformation implements Serializable {

    public String slotTime;
    public String status;


    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
