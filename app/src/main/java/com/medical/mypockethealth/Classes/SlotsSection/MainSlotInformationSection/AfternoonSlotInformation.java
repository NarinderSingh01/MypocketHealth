package com.medical.mypockethealth.Classes.SlotsSection.MainSlotInformationSection;

import java.io.Serializable;

public class AfternoonSlotInformation implements Serializable {

    public String slot_time;
    public String slot_status;

    public String getSlot_time() {
        return slot_time;
    }

    public void setSlot_time(String slot_time) {
        this.slot_time = slot_time;
    }

    public String getSlot_status() {
        return slot_status;
    }

    public void setSlot_status(String slot_status) {
        this.slot_status = slot_status;
    }
}
