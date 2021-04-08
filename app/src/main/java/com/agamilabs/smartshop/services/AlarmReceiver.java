package com.agamilabs.smartshop.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.agamilabs.smartshop.activity.NewCampaignActivity;
import com.agamilabs.smartshop.controller.AppController;
import com.agamilabs.smartshop.database.CampaignDataManagement;
import com.agamilabs.smartshop.database.SmsDatabaseHelper;
import com.agamilabs.smartshop.model.Sms;
import com.agamilabs.smartshop.model.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import SeparatePk.IRemoteServiceCallback;
import SeparatePk.aidlInterface;

import static android.content.Context.BIND_AUTO_CREATE;

public class AlarmReceiver extends BroadcastReceiver {
    public Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        setContext(context);

        /*// Receive SmsID from alarm extra
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            SmsID = bundle.getInt("SmsID");
            AppController.getAppController().addToSMSQueue(SmsID);
            //smsQueue.add(SmsID);
        }*/

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String saveData = new CampaignDataManagement(getContext()).getSharePrefData();
            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
            int SmsID = bundle.getInt("SmsID");
            if(SmsID == 400){
                new CampaignDataManagement(getContext()).saveSharePrefData(currentDateTimeString+"\n\n\n"+saveData);
            }
        }


        NewCampaignActivity.getActivityInstance().TriggerMsgToClient();
        AppController.getAppController().bindToAIDLService();
        AppController.getAppController().callAllClients();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


}

