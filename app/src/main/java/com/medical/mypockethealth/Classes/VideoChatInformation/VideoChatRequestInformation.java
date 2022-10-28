package com.medical.mypockethealth.Classes.VideoChatInformation;

import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;

import java.io.Serializable;

public class VideoChatRequestInformation implements Serializable {

    public String toke;
    public String channelName;
    public String rtmToken;
    public String mainId;
    public ProviderInformation providerInformation;
    public String objectKey;


    public VideoChatRequestInformation(String toke, String channelName, String rtmToken,
                                       String mainId, ProviderInformation providerInformation, String objectKey) {
        this.toke = toke;
        this.channelName = channelName;
        this.rtmToken = rtmToken;
        this.mainId = mainId;
        this.providerInformation = providerInformation;
        this.objectKey = objectKey;
    }


    public String getObjectKey() {
        return objectKey;
    }

    public VideoChatRequestInformation() {
    }

    public String getToke() {
        return toke;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getRtmToken() {
        return rtmToken;
    }

    public String getMainId() {
        return mainId;
    }

    public ProviderInformation getProviderInformation() {
        return providerInformation;
    }
}
