package com.medical.mypockethealth.Classes.VideoChatInformation;

import java.io.Serializable;

public class VideoChatInformation implements Serializable {

    public String toke;
    public String channelName;
    public String rtmToken;
    public String mainId;
    public String practiceNumberPhone;

    public String getPracticeNumberPhone() {
        return practiceNumberPhone;
    }

    public void setPracticeNumberPhone(String practiceNumberPhone) {
        this.practiceNumberPhone = practiceNumberPhone;
    }

    public String getToke() {
        return toke;
    }

    public void setToke(String toke) {
        this.toke = toke;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getRtmToken() {
        return rtmToken;
    }

    public void setRtmToken(String rtmToken) {
        this.rtmToken = rtmToken;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }
}
