package com.medical.mypockethealth.Classes.ClientInformation;

import java.io.Serializable;

public class MedicationsList implements Serializable {

    public String id;
    public String medicationName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }
}
