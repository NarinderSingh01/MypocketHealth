package com.medical.mypockethealth.Classes.SymptomChecker;

import java.io.Serializable;
import java.util.List;

public class SymptomInformation implements Serializable, Comparable<SymptomInformation> {

    private int ID;
    private String Name;
    private String value;
    private String date;
    private String gender;
    private List<Integer> symptomId;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Integer> getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(List<Integer> symptomId) {
        this.symptomId = symptomId;
    }

    @Override
    public int compareTo(SymptomInformation obj) {

        if (obj.getID()==ID) {

            return 1;

        } else {

            return 0;

        }

    }
}
