package com.agamilabs.smartshop.model;

public class AllClientAppInfo {
    String clientAppName;
    String clientAppStatus;

    public AllClientAppInfo() {

    }

    public AllClientAppInfo(String clientAppName, String clientAppStatus) {
        this.clientAppName = clientAppName;
        this.clientAppStatus = clientAppStatus;
    }

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }

    public String getClientAppStatus() {
        return clientAppStatus;
    }

    public void setClientAppStatus(String clientAppStatus) {
        this.clientAppStatus = clientAppStatus;
    }
}
