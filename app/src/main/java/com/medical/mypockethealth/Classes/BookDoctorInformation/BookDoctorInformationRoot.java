package com.medical.mypockethealth.Classes.BookDoctorInformation;

import java.io.Serializable;
import java.util.List;

public class BookDoctorInformationRoot implements Serializable {

    public String success;
    public String message;
    public List<BookDoctorInformation> details;


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

    public List<BookDoctorInformation> getDetails() {
        return details;
    }

    public void setDetails(List<BookDoctorInformation> details) {
        this.details = details;
    }
}
