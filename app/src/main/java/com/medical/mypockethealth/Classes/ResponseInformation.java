package com.medical.mypockethealth.Classes;

import java.io.Serializable;

public class ResponseInformation implements Serializable {

    private String message,success,type,otp,id,modeType,bookingId,profileStatus,sheetUrl,requestId;

    public static String fail_response_type="0";
    public static String  success_response_type="1";
    public static String userMode="0";
    public static String  providerMode="1";
    public static String available="0";
    public static String unAvailable="1";

    public enum ResponseKeys
    {
        message,success,type,otp,modeType,details,id,data
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSheetUrl() {
        return sheetUrl;
    }

    public void setSheetUrl(String sheetUrl) {
        this.sheetUrl = sheetUrl;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getModeType() {
        return modeType;
    }

    public void setModeType(String modeType) {
        this.modeType = modeType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
