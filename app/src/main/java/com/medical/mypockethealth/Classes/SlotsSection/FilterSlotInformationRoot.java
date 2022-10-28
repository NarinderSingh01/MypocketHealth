package com.medical.mypockethealth.Classes.SlotsSection;

import java.io.Serializable;
import java.util.List;

public class FilterSlotInformationRoot implements Serializable {

    public String success;
    public String message;
    public SlotInformation details;
    public List<BookedSlotsInformation> booking;


    public List<BookedSlotsInformation> getBooking() {
        return booking;
    }

    public void setBooking(List<BookedSlotsInformation> booking) {
        this.booking = booking;
    }

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

    public SlotInformation getDetails() {
        return details;
    }

    public void setDetails(SlotInformation details) {
        this.details = details;
    }
}
