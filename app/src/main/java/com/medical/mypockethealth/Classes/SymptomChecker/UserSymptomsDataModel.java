package com.medical.mypockethealth.Classes.SymptomChecker;

import java.io.Serializable;
import java.util.List;

public class UserSymptomsDataModel implements Serializable {

    private String check_up_for;
    private String gender;
    private String age;
    private String overweight_or_obese;
    private String smoke_cigarettes;
    private String suffered_an_injury;
    private String high_cholesterol;
    private String hypertension;
    private String dob;
    private List<SymptomInformation> symptomsList;
    private String region;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCheck_up_for() {
        return check_up_for;
    }

    public void setCheck_up_for(String check_up_for) {
        this.check_up_for = check_up_for;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getOverweight_or_obese() {
        return overweight_or_obese;
    }

    public void setOverweight_or_obese(String overweight_or_obese) {
        this.overweight_or_obese = overweight_or_obese;
    }

    public String getSmoke_cigarettes() {
        return smoke_cigarettes;
    }

    public void setSmoke_cigarettes(String smoke_cigarettes) {
        this.smoke_cigarettes = smoke_cigarettes;
    }

    public String getSuffered_an_injury() {
        return suffered_an_injury;
    }

    public void setSuffered_an_injury(String suffered_an_injury) {
        this.suffered_an_injury = suffered_an_injury;
    }

    public String getHigh_cholesterol() {
        return high_cholesterol;
    }

    public void setHigh_cholesterol(String high_cholesterol) {
        this.high_cholesterol = high_cholesterol;
    }

    public String getHypertension() {
        return hypertension;
    }

    public void setHypertension(String hypertension) {
        this.hypertension = hypertension;
    }

    public List<SymptomInformation> getSymptomsList() {
        return symptomsList;
    }

    public void setSymptomsList(List<SymptomInformation> symptomsList) {
        this.symptomsList = symptomsList;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
