package com.example.controllerproject.notify;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.controllerproject.ControllerProvider;
import com.example.controllerproject.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MyMessagingService extends FirebaseMessagingService {
    String TAG = "TAG";
    DatabaseHandler db = new DatabaseHandler(this);

    String URL = "http://192.168.1.6/android/AgamiLab/smart_shop/sendPushNotification.php" ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Log.d("control", "size: " + remoteMessage.getData().size() );

            for(int i=0; i<50;i++){
                Log.d("control", "i: " + i );
                Map<String, String> map = remoteMessage.getData();


                long currentTime = Calendar.getInstance().getTimeInMillis();
                ContentValues task_Values = new ContentValues();
                task_Values.put(ControllerProvider.MSG, map.containsKey("message"));
                task_Values.put(ControllerProvider.ASSIGNEDTO, "");
                task_Values.put(ControllerProvider.ENTRYDATETIME, currentTime);
                task_Values.put(ControllerProvider.ASSIGNDATETIME, 0 );
                task_Values.put(ControllerProvider.COMPLETEDATETIME, 0 );
                task_Values.put(ControllerProvider.ISCOMPLETE, 0 );

                Uri task_uri = getContentResolver().insert( ControllerProvider.TASK_TABLE_URI, task_Values);
                Log.e("TAG", "task_uri: "+task_uri) ;

                LogTableUri("", map.get("data")+"", map.get("message")+"", currentTime ) ;

                if(i==49){
                    String topic = "ALLCLIENT";
                    String title= map.get("title")+"";
                    String message= map.get("message")+"";
                    String data= map.get("data")+"";
                    Log.e("logshow", title+"   "+message+" "+ data ) ;
                    sendDataMessageFromServer(title, message, data, topic);

                }

            }

            createNotification();
        }

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
    private void LogTableUri(String mSrc, String mData, String msg, long currentTime) {
        ContentValues log_Values = new ContentValues();
        log_Values.put(ControllerProvider.SRC, mSrc);
        log_Values.put(ControllerProvider.DATA, mData);
        log_Values.put(ControllerProvider.ACTIONS, msg);
        log_Values.put(ControllerProvider.TIMESTAMP, currentTime );

        Uri log_uri = getContentResolver().insert( ControllerProvider.LOG_TABLE_URI, log_Values);
        Log.e("TAG", "log_uri: "+log_uri) ;
    }

    private void sendDataMessageFromServer(String title, String message, String data, String topic) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response) ;

//                if(response.equalsIgnoreCase("true")){
//                    titleEditText.setText("");
//                    editText.setText("");
//
//                    Toast.makeText(getApplicationContext(), "Post upload Successfully...", Toast.LENGTH_SHORT).show();
//                } else{
//                    Toast.makeText(getApplicationContext(), "Please check Connection...", Toast.LENGTH_SHORT).show();
//                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please check Connection...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("title", title);
                parameters.put("message", message);
                parameters.put("data", data);
                parameters.put("topic", topic);

                Log.e("TAG", title+" "+message) ;

                return parameters;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Log.d("TAG", "stringRequest: "+stringRequest ) ;


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

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            channel.canBypassDnd();
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setColor(ContextCompat.getColor(this, R.color.black))
//                .setContentTitle(getString(R.string.app_name))
                .setContentTitle(getString(R.string.app_name))
//                .setContentText(remoteMessage.getNotification().getBody())
                .setContentText("content Text")
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
