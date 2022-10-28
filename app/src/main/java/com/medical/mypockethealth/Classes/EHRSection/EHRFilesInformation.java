package com.medical.mypockethealth.Classes.EHRSection;

import java.io.Serializable;

public class EHRFilesInformation implements Serializable {

    public int followUpSheets;
    public int medicalCertificateSheets;
    public int prescriptionSheets;
    public int referralSheets;

    public int getFollowUpSheets() {
        return followUpSheets;
    }

    public void setFollowUpSheets(int followUpSheets) {
        this.followUpSheets = followUpSheets;
    }

    public int getMedicalCertificateSheets() {
        return medicalCertificateSheets;
    }

    public void setMedicalCertificateSheets(int medicalCertificateSheets) {
        this.medicalCertificateSheets = medicalCertificateSheets;
    }

    public int getPrescriptionSheets() {
        return prescriptionSheets;
    }

    public void setPrescriptionSheets(int prescriptionSheets) {
        this.prescriptionSheets = prescriptionSheets;
    }

    public int getReferralSheets() {
        return referralSheets;
    }

    public void setReferralSheets(int referralSheets) {
        this.referralSheets = referralSheets;
    }
}
