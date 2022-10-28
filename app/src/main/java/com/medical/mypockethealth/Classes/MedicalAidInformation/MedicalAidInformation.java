package com.medical.mypockethealth.Classes.MedicalAidInformation;

import java.io.Serializable;

public class MedicalAidInformation implements Serializable {

    public String id;
    public String title;
    public String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
