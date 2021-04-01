package com.example.clientproject3;

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
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.clientproject3.MainActivity.ACTIONS;
import static com.example.clientproject3.MainActivity.ASSIGNDATETIME;
import static com.example.clientproject3.MainActivity.ASSIGNEDTO;
import static com.example.clientproject3.MainActivity.DATA;
import static com.example.clientproject3.MainActivity.LOG_TABLE_URI;
import static com.example.clientproject3.MainActivity.SL;
import static com.example.clientproject3.MainActivity.TASK_TABLE_URI;
import static com.example.clientproject3.MainActivity.SRC;
import static com.example.clientproject3.MainActivity.TIMESTAMP;

public class MyMessagingService extends FirebaseMessagingService {
    String TAG = "TAG";
    private static int kJobId = 0;
    ComponentName mServiceComponent;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {

            mServiceComponent = new ComponentName(getApplicationContext(), TestJobService.class);
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            JobInfo.Builder builder = new JobInfo.Builder(kJobId++,mServiceComponent);
//            builder.setMinimumLatency(5000);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

            JobScheduler jobScheduler =
                    (JobScheduler) getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());
            Log.e("TAG", "jobservice: "+ mServiceComponent) ;


        }
}
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
