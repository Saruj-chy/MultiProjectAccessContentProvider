package com.agamilabs.smartshop.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.agamilabs.smartshop.model.CampaignData;

public class CampaignDataManagement {
    Context context;
    SharedPreferences sharedPreferences;
    String db_name="campaign_data";

    public CampaignDataManagement(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences(db_name,Context.MODE_PRIVATE);
    }

    public CampaignData getCampaignData(){
        CampaignData campaignData = new CampaignData();
        campaignData.setCampaignName(sharedPreferences.getString(CampaignData.KEY_CAMPAIGN_NAME,""));
        campaignData.setRecipients(sharedPreferences.getString(CampaignData.KEY_RECIPIENTS,""));
        campaignData.setMessage(sharedPreferences.getString(CampaignData.KEY_MESSAGE,""));
        campaignData.setExtraRecipient(sharedPreferences.getString(CampaignData.KEY_EXTRA_RECIPIENT,""));
        return campaignData;
    }

    public void saveuserdata(CampaignData campaignData) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CampaignData.KEY_CAMPAIGN_NAME,campaignData.getCampaignName());
        editor.putString(CampaignData.KEY_RECIPIENTS,campaignData.getRecipients());
        editor.putString(CampaignData.KEY_MESSAGE,campaignData.getMessage());
        editor.putString(CampaignData.KEY_EXTRA_RECIPIENT,campaignData.getExtraRecipient());
        // Save the changes in SharedPreferences
        editor.apply(); // commit changes
    }


    public String getSharePrefData() {
        String logData = sharedPreferences.getString("logData","");
        return logData;
    }

    public void saveSharePrefData(String logData) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logData",logData);
        editor.apply(); // commit changes
    }

}
