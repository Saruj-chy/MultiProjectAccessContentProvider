package com.example.clientproject1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.clientproject1.services.OperationService;
import com.example.clientproject1.services.TestJobService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MyMessagingService extends FirebaseMessagingService {
    private CountDownTimer countDownTimer;
    long remainingRefreshTime = 1000 ;
    private static int kJobId = 0;
    ComponentName mServiceComponent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {

            mServiceComponent = new ComponentName(getApplicationContext(), TestJobService.class);
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            JobInfo.Builder builder = new JobInfo.Builder(kJobId++,mServiceComponent);
//            builder.setMinimumLatency(1000);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

            JobScheduler jobScheduler = (JobScheduler) getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());
            Log.e("TAG", "jobservice: "+ mServiceComponent);
        }

    }




    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
