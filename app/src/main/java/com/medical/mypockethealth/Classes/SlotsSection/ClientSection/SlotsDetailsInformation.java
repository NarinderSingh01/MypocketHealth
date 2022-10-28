package com.medical.mypockethealth.Classes.SlotsSection.ClientSection;

import java.io.Serializable;
import java.util.List;

public class SlotsDetailsInformation implements Serializable {

    public String id;
    public String providerId;
    public String date;
    public String morningVisibilityStatus;
    public String morningStartTime;
    public String morningEndTime;
    public String morningPerSlot;
    public String afternoonVisibilityStatus;
    public String afternoonStartTime;
    public String afternoonEndTime;
    public String afternoonPerlSot;
    public String eveningVisibilityStatus;
    public String eveningStartTime;
    public String eveningEndTime;
    public String eveningPerSlot;
    public List<MorningSlotsInformation> morningSlot;
    public List<AfternoonSlotsInformation> afternoonSlot;
    public List<EveningSlotsInformation> eveningSlot;
    public String created_at;
    public String updated_at;
    public Object deleted_at;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMorningVisibilityStatus() {
        return morningVisibilityStatus;
    }

    public void setMorningVisibilityStatus(String morningVisibilityStatus) {
        this.morningVisibilityStatus = morningVisibilityStatus;
    }

    public String getMorningStartTime() {
        return morningStartTime;
    }

    public void setMorningStartTime(String morningStartTime) {
        this.morningStartTime = morningStartTime;
    }

    public String getMorningEndTime() {
        return morningEndTime;
    }

    public void setMorningEndTime(String morningEndTime) {
        this.morningEndTime = morningEndTime;
    }

    public String getMorningPerSlot() {
        return morningPerSlot;
    }

    public void setMorningPerSlot(String morningPerSlot) {
        this.morningPerSlot = morningPerSlot;
    }

    public String getAfternoonVisibilityStatus() {
        return afternoonVisibilityStatus;
    }

    public void setAfternoonVisibilityStatus(String afternoonVisibilityStatus) {
        this.afternoonVisibilityStatus = afternoonVisibilityStatus;
    }

    public String getAfternoonStartTime() {
        return afternoonStartTime;
    }

    public void setAfternoonStartTime(String afternoonStartTime) {
        this.afternoonStartTime = afternoonStartTime;
    }

    public String getAfternoonEndTime() {
        return afternoonEndTime;
    }

    public void setAfternoonEndTime(String afternoonEndTime) {
        this.afternoonEndTime = afternoonEndTime;
    }

    public String getAfternoonPerlSot() {
        return afternoonPerlSot;
    }

    public void setAfternoonPerlSot(String afternoonPerlSot) {
        this.afternoonPerlSot = afternoonPerlSot;
    }

    public String getEveningVisibilityStatus() {
        return eveningVisibilityStatus;
    }

    public void setEveningVisibilityStatus(String eveningVisibilityStatus) {
        this.eveningVisibilityStatus = eveningVisibilityStatus;
    }

    public String getEveningStartTime() {
        return eveningStartTime;
    }

    public void setEveningStartTime(String eveningStartTime) {
        this.eveningStartTime = eveningStartTime;
    }

    public String getEveningEndTime() {
        return eveningEndTime;
    }

    public void setEveningEndTime(String eveningEndTime) {
        this.eveningEndTime = eveningEndTime;
    }

    public String getEveningPerSlot() {
        return eveningPerSlot;
    }

    public void setEveningPerSlot(String eveningPerSlot) {
        this.eveningPerSlot = eveningPerSlot;
    }

    public List<MorningSlotsInformation> getMorningSlot() {
        return morningSlot;
    }

    public void setMorningSlot(List<MorningSlotsInformation> morningSlot) {
        this.morningSlot = morningSlot;
    }

    public List<AfternoonSlotsInformation> getAfternoonSlot() {
        return afternoonSlot;
    }

    public void setAfternoonSlot(List<AfternoonSlotsInformation> afternoonSlot) {
        this.afternoonSlot = afternoonSlot;
    }

    public List<EveningSlotsInformation> getEveningSlot() {
        return eveningSlot;
    }

    public void setEveningSlot(List<EveningSlotsInformation> eveningSlot) {
        this.eveningSlot = eveningSlot;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Object getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Object deleted_at) {
        this.deleted_at = deleted_at;
    }
}
