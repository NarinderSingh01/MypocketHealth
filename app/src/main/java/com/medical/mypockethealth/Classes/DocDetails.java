package com.medical.mypockethealth.Classes;

import java.io.Serializable;

public class DocDetails implements Serializable {

    public DocDetails() {
    }

    private String docId;

    public String getDocId() {
        return docId;
    }

    public DocDetails(String docId) {
        this.docId = docId;
    }

}
