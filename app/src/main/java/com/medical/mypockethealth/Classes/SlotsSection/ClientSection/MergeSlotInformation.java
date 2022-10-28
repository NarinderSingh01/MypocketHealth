package com.medical.mypockethealth.Classes.SlotsSection.ClientSection;

import java.io.Serializable;

public class MergeSlotInformation implements Serializable {

    public String slotTime;
    public String status;
    public boolean state;


    public MergeSlotInformation(String slotTime, String status,boolean state) {
        this.slotTime = slotTime;
        this.status = status;
        this.state=state;
    }

    public boolean isState() {
        return state;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public String getStatus() {
        return status;
    }
}
