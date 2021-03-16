package com.example.clientproject2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

import static com.example.clientproject2.MainActivity.ACTIONS;
import static com.example.clientproject2.MainActivity.ASSIGNDATETIME;
import static com.example.clientproject2.MainActivity.ASSIGNEDTO;
import static com.example.clientproject2.MainActivity.DATA;
import static com.example.clientproject2.MainActivity.LOG_TABLE_URI;
import static com.example.clientproject2.MainActivity.SL;
import static com.example.clientproject2.MainActivity.SRC;
import static com.example.clientproject2.MainActivity.TASK_TABLE_URI;
import static com.example.clientproject2.MainActivity.TIMESTAMP;


public class MyMessagingService extends FirebaseMessagingService {
    String TAG = "TAG";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            createNotification();
            OnNewTaskClick();
        }

//        if (remoteMessage.getData().size() > 0) {
//            createNotification();
//            OnNewTaskClick();
//        }

//        if (remoteMessage.getData().size() > 0) {
//
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            Log.d("control", "size: " + remoteMessage.getData().size() );
//
//            for(int i=0; i<50;i++){
//                Log.d("control", "i: " + i );
//
//
//            }
//
//            createNotification();
//        }

        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            //data save in db
//            NotifyModel notifyModel = new NotifyModel(
//                    "",
//                    remoteMessage.getNotification().getTitle(),
//                    remoteMessage.getNotification().getBody(),
//                    remoteMessage.getTo()
//            );
////            db.insertUserInfo(notifyModel.getTitle(), notifyModel.getBody_text(), notifyModel.getTopic());
//
//            for(int i=0; i<50;i++){
//                Log.d("control", "i also: " + i );
//
//            }
//            createNotification();
//
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    public void OnNewTaskClick(){
        Uri students = Uri.parse(String.valueOf(TASK_TABLE_URI));
        Cursor cursor = getContentResolver().query(students, null, "new_task", null, null);

        String sl = null;
        if (cursor.moveToFirst()) {
            sl = cursor.getString(cursor.getColumnIndex(SL));
        }

//        getAllEditText();

        long currentTime = Calendar.getInstance().getTimeInMillis();
        ContentValues values = new ContentValues() ;
        values.put(ASSIGNEDTO, "c2");
        values.put(ASSIGNDATETIME, currentTime);

        int c= getContentResolver().update(TASK_TABLE_URI, values, "sl=\""+ sl +"\"", null) ;
        if(c>0){
            LogTableUri("c2", "mMsg", "c2 assigned new task sl no: "+ sl, currentTime ) ;
            Toast.makeText(this, " New Task Assigned successfull.", Toast.LENGTH_SHORT).show();

        }


//        ClearAllText();
    }
    private void LogTableUri(String mSrc, String mData, String msg, long currentTime) {
        ContentValues log_Values = new ContentValues();
        log_Values.put(SRC, mSrc);
        log_Values.put(DATA, mData);
        log_Values.put(ACTIONS, msg);
        log_Values.put(TIMESTAMP, currentTime );

        Uri log_uri = getContentResolver().insert( LOG_TABLE_URI, log_Values);
        Log.e("TAG", "log_uri: "+log_uri) ;
    }



    public void scheduleJob() {

    }

    public void handleNow() {

    }

    public void createNotification() {

        String NOTIFICATION_CHANNEL_ID = "AGAMi_Smart_Shop";

        long pattern[] = {100, 1000, 500, 1000};

        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Your Notifications",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("hello that's notification");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(pattern);
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            channel.canBypassDnd();
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setColor(ContextCompat.getColor(this, R.color.black))
//                .setContentTitle(getString(R.string.app_name))
                .setContentTitle("Name: "+getString(R.string.app_name))
//                .setContentText(remoteMessage.getNotification().getBody())
                .setContentText("getBody_text")
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true);

        mNotificationManager.notify(1000, notificationBuilder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
