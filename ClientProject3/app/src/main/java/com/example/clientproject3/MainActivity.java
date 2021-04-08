package com.example.clientproject3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String PROVIDER_NAME1 = "com.agamilabs.smartshop.database.ControllerProvider";
    public static final String RECIPENT_MSG = "content://" + PROVIDER_NAME1 + "/recipent_msg";
    public static final Uri RECIPENT_MSG_URI = Uri.parse(RECIPENT_MSG);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("ALLCLIENT") ;



        ActivityCompat.requestPermissions(MainActivity.this,
                new String[] {Manifest.permission.SEND_SMS}, 100);


    }

}