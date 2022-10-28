package com.medical.mypockethealth.Classes.SlotsSection.MainSlotInformationSection;

import java.io.Serializable;
import java.util.List;

public class MainSlotInformation implements Serializable {

    public String date;
    public String doctor_id;
    public String morning_type;
    public String morning_start_time;
    public String morning_end_time;
    public String afternoon_type;
    public String afternoon_start_time;
    public String afternoon_end_time;
    public String evening_type;
    public String evening_start_time;
    public String evening_end_time;
    public List<MorningSlotInformation> morning_slots;
    public List<AfternoonSlotInformation> afternoon_slots;
    public List<EveningSlotInformation> evening_slots;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getMorning_type() {
        return morning_type;
    }

    public void setMorning_type(String morning_type) {
        this.morning_type = morning_type;
    }

    public String getMorning_start_time() {
        return morning_start_time;
    }

    public void setMorning_start_time(String morning_start_time) {
        this.morning_start_time = morning_start_time;
    }

    public String getMorning_end_time() {
        return morning_end_time;
    }

    public void setMorning_end_time(String morning_end_time) {
        this.morning_end_time = morning_end_time;
    }

    public String getAfternoon_type() {
        return afternoon_type;
    }

    public void setAfternoon_type(String afternoon_type) {
        this.afternoon_type = afternoon_type;
    }

    public String getAfternoon_start_time() {
        return afternoon_start_time;
    }

    public void setAfternoon_start_time(String afternoon_start_time) {
        this.afternoon_start_time = afternoon_start_time;
    }

    public String getAfternoon_end_time() {
        return afternoon_end_time;
    }

    public void setAfternoon_end_time(String afternoon_end_time) {
        this.afternoon_end_time = afternoon_end_time;
    }

    public String getEvening_type() {
        return evening_type;
    }

    public void setEvening_type(String evening_type) {
        this.evening_type = evening_type;
    }

    public String getEvening_start_time() {
        return evening_start_time;
    }

    public void setEvening_start_time(String evening_start_time) {
        this.evening_start_time = evening_start_time;
    }

    public String getEvening_end_time() {
        return evening_end_time;
    }

    public void setEvening_end_time(String evening_end_time) {
        this.evening_end_time = evening_end_time;
    }

    public List<MorningSlotInformation> getMorning_slots() {
        return morning_slots;
    }

    public void setMorning_slots(List<MorningSlotInformation> morning_slots) {
        this.morning_slots = morning_slots;
    }

    public List<AfternoonSlotInformation> getAfternoon_slots() {
        return afternoon_slots;
    }

    public void setAfternoon_slots(List<AfternoonSlotInformation> afternoon_slots) {
        this.afternoon_slots = afternoon_slots;
    }

    public List<EveningSlotInformation> getEvening_slots() {
        return evening_slots;
    }

    public void setEvening_slots(List<EveningSlotInformation> evening_slots) {
        this.evening_slots = evening_slots;
    }
}
