package com.medical.mypockethealth.Classes.SliderClasses;

import java.io.Serializable;

public class SliderInformation implements Serializable {
    
    private  int image;
    private  int backgroundColor;
    private  String title;

    public static final String symptom_checker="Symptom checker";
    public static final String doctor_booking="Doctor booking";
    public static final String mental_health="Mental health";
    public static final String ehr="EHR";
    public static final String pharma="E-Pharma";


    public SliderInformation(int image, int backgroundColor, String title) {
        this.image = image;
        this.backgroundColor = backgroundColor;
        this.title = title;
    }

    public SliderInformation(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public String getTitle() {
        return title;
    }
}
