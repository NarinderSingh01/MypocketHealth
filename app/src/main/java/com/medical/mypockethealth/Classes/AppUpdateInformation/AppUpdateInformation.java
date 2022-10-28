package com.medical.mypockethealth.Classes.AppUpdateInformation;

import java.io.Serializable;

public class AppUpdateInformation implements Serializable {

    public int versionCode;

    public AppUpdateInformation(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getVersionCode() {
        return versionCode;
    }
}
