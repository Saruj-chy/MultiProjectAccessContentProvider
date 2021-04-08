package com.agamilabs.smartshop.model;

public class CampaignData {
    String campaignName,recipients,message,extraRecipient;

    final public static String KEY_CAMPAIGN_NAME = "CAMPAIGN_NAME";
    final public static String KEY_RECIPIENTS = "RECIPIENTS";
    final public static String KEY_MESSAGE = "MESSAGE";
    final public static String KEY_EXTRA_RECIPIENT = "EXTRA_RECIPIENT";

    public CampaignData() {

    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExtraRecipient() {
        return extraRecipient;
    }

    public void setExtraRecipient(String extraRecipient) {
        this.extraRecipient = extraRecipient;
    }
}
