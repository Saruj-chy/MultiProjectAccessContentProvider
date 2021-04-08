package com.example.clientproject3.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmTriggerBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG_ALARM_TRIGGER_BROADCAST = "ALARM_TRIGGER_BROADCAST";

    @Override
    public void onReceive(Context context, Intent intent) {
        //WAKE UP DEVICE
        WakeLocker.acquire(context);
//        Log.e("TAG", "AlarmTriggerBroadcastReceiver") ;
        //LAUNCH PAGE
//        Intent intent1 = new Intent();
//        intent1.setClassName(context.getPackageName(), SomeActivity.class.getName());
//        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent1);


//        Toast.makeText(context, "Client 3 triggered after 1 min", Toast.LENGTH_LONG).show();
//        Log.e("tag", "Client 3 triggered after 1 min") ;
//
//        MainActivity.setAlarm(context);
//        WakeLocker.release();


    };

    //SET NEW ALARM


}
