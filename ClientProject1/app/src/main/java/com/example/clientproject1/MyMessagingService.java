package com.example.clientproject1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.clientproject1.controller.AppController;
import com.example.clientproject1.services.OperationService;
import com.example.clientproject1.services.TestJobService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.clientproject1.MainActivity.RECIPENT_MSG_URI;


public class MyMessagingService extends FirebaseMessagingService {
    private CountDownTimer countDownTimer;
    long remainingRefreshTime = 1000 ;
    private static int kJobId = 0;
    ComponentName mServiceComponent;
    private boolean dataExist = false ;
    String rsNo, phoneNumber, message, name ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {

//            mServiceComponent = new ComponentName(getApplicationContext(), TestJobService.class);
//            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//            JobInfo.Builder builder = new JobInfo.Builder(kJobId++,mServiceComponent);
////            builder.setMinimumLatency(1000);
//            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
//
//            JobScheduler jobScheduler = (JobScheduler) getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//            jobScheduler.schedule(builder.build());

//            for(int i=0; i<50; i++){
//                AppController.getAppController().getInAppNotifier().log("intent", " i: "+ i );
//            }

            dataExist = true ;
            for(int i=1; i<=1000; i++){
            OnNewTaskClick() ;
//                Log.e("tag", "intent "+i+" client1") ;

                try {
                    Thread.sleep(60*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(dataExist == false){
                    break;
                }
            }


            Log.e("TAG", "jobservice: "+ mServiceComponent);
        }

    }
    private void OnNewTaskClick(){
        Uri students = Uri.parse(String.valueOf(RECIPENT_MSG_URI));
        ContentValues values = new ContentValues() ;
        values.put("clientName", "Client 1");
        int c= getContentResolver().update(students, values, "client", null) ;
        if(c==0){
            dataExist = false;
        }else{
            getClientPhnSms();

        }

        Log.e("TAG", "c: "+c ) ;
    }
    private void getClientPhnSms() {
        Uri students = Uri.parse(String.valueOf(RECIPENT_MSG_URI));
        Cursor c = getContentResolver().query(students, null, "Client 1", null, "") ;

        if (c.moveToFirst()) {
            do{
                rsNo = c.getString(c.getColumnIndex("rsno")) ;
                phoneNumber = c.getString(c.getColumnIndex("RecipientsNumber")) ;
                message = c.getString(c.getColumnIndex( "Message")) ;
                name= c.getString(c.getColumnIndex( "assignedClient" )) ;
                sendSMSandNotifyUser(phoneNumber, message, name);

                UpdateCurrentClient(rsNo);


            } while (c.moveToNext());
        }

    }

    private void UpdateCurrentClient(String rsNo) {
        Uri recipent_msg_uri = Uri.parse(String.valueOf(RECIPENT_MSG_URI));
        ContentValues values = new ContentValues() ;
        values.put("rsno", rsNo);
        int c= getContentResolver().update(recipent_msg_uri, values, "rsNo", null) ;
    }

    private void sendSMSandNotifyUser(String phoneNumber, String message, String name) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        Log.e("TAG", "client1 phoneNumber: "+ phoneNumber+"   name: "+ name +"  "+ message ) ;

    }


}
