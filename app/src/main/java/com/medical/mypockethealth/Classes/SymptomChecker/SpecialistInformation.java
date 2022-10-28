package com.medical.mypockethealth.Classes.SymptomChecker;

import java.io.Serializable;

public class SpecialistInformation implements Serializable {


    public int ID;
    public String Name;
    public double Accuracy;
    public int Ranking;

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

    public double getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(double accuracy) {
        Accuracy = accuracy;
    }

    public int getRanking() {
        return Ranking;
    }

    public void setRanking(int ranking) {
        Ranking = ranking;
    }
}
