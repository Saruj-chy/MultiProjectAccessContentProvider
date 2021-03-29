package com.example.clientproject1.services;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.clientproject1.MainActivity;
import com.example.clientproject1.constants.AppConstants;
import com.example.clientproject1.controller.AppController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.clientproject1.MainActivity.ASSIGNDATETIME;
import static com.example.clientproject1.MainActivity.ASSIGNEDTO;
import static com.example.clientproject1.MainActivity.TASK_TABLE_URI;

public class AlarmTriggerBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG_ALARM_TRIGGER_BROADCAST = "ALARM_TRIGGER_BROADCAST";
    int num = 0 ;

    @Override
    public void onReceive(Context context, Intent intent) {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        //WAKE UP DEVICE
//        WakeLocker.acquire(context);
//        Log.e("TAG", "AlarmTriggerBroadcastReceiver") ;
        //LAUNCH PAGE
//        Intent intent1 = new Intent();
//        intent1.setClassName(context.getPackageName(), SomeActivity.class.getName());
//        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent1);


        Toast.makeText(context, "Client 1 AlarmTriggerBroadcastReceiver after 2 sec", Toast.LENGTH_LONG).show();
        Log.e("TAG", "Client 1 triggered after 2 sec" + intent+"  "+ currentTime ) ;


            intent = new Intent();
            intent.setClassName(context.getPackageName(), OperationService.class.getName()) ;
            intent.putExtra("int", 1+"") ;
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startService(intent) ;


//        MainActivity.setAlarm(context, "CHECKMSG2", num);
//        WakeLocker.release();


    };


}
