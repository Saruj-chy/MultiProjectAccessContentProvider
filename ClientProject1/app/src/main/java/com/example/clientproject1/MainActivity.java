package com.example.clientproject1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
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

import com.example.clientproject1.constants.AppConstants;
import com.example.clientproject1.controller.AppController;
import com.example.clientproject1.services.AlarmTriggerBroadcastReceiver;
import com.example.clientproject1.services.OperationService;
import com.example.clientproject1.services.WakeLocker;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String PROVIDER_NAME = "com.agamilabs.smartshop.database.ControllerProvider";
    public static final String LOG_URL = "content://" + PROVIDER_NAME + "/logtable";
    public static final Uri LOG_TABLE_URI = Uri.parse(LOG_URL);
    public static final String TASK_URL = "content://" + PROVIDER_NAME + "/tasktable";
    public static final Uri TASK_TABLE_URI = Uri.parse(TASK_URL);

    public static final String PROVIDER_NAME1 = "com.agamilabs.smartshop.database.ControllerProvider";
    public static final String RECIPENT_MSG = "content://" + PROVIDER_NAME1 + "/recipent_msg";
    public static final Uri RECIPENT_MSG_URI = Uri.parse(RECIPENT_MSG);

    //    static final String ID = "id";
    public static final String LOGNO = "logno";
    public static final String SRC = "src";
    public static final String ACTIONS = "actions";
    public static final String DATA = "data";
    public static final String TIMESTAMP = "timestamp";

    //  task table
    public static final String SL = "sl";
    public static final String MSG = "msg";
    public static final String ASSIGNEDTO = "assignedClient";
//    public static final String ASSIGNEDTO = "assignedto";
    public static final String ENTRYDATETIME = "entrydatetime";
    public static final String ASSIGNDATETIME = "assignDateTime";
//    public static final String ASSIGNDATETIME = "assigndatetime";
    public static final String COMPLETEDATETIME = "completedatetime";
    public static final String ISCOMPLETE = "isComplete";

    private EditText mLogNoEditText, mSrcEditText, mDataEditText;
    private Button mInsertBtn, mUpdateBtn, mDeleteBtn, mShowBtn, mAssignBtn, mCompleteBtn ;
    private TextView mShowTextView ;

    private String mSLNo, mAssignedTo, mMsg;
    private static final String ACTION_ONE = "one";
    private boolean dataError = false;
    private boolean wakeError = false;

    static int count = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseMessaging.getInstance().subscribeToTopic("ALLCLIENT") ;

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[] {Manifest.permission.SEND_SMS}, 100);

//        getClientPhnSms() ;
    }

    public void getClientPhnSms() {
        Uri students = Uri.parse(String.valueOf(RECIPENT_MSG_URI));
//        Cursor c = ContentResolver.managedQuery(students, null, "Client 1", null, "");
        Cursor c = getContentResolver().query(students, null, "Client 1", null, "") ;

        Log.e("count", c.getCount() +"") ;
//        Log.e("task", c.getString(c.getColumnIndex(ControllerProvider.MSG)) ) ;

        StringBuilder stringBuilder = new StringBuilder();
        if (c.moveToFirst()) {
//            Log.e("task", c.getString(c.getColumnIndex(ControllerProvider.MSG)) ) ;
            do{
                stringBuilder.append(" SMSID: "+c.getString(c.getColumnIndex("SmsID")) +
                        " \n RecipientsNumber: " +  c.getString(c.getColumnIndex("RecipientsNumber")) +
                        " \n Message: " +  c.getString(c.getColumnIndex( "Message")) +
                        " \n MessageStatus: " + c.getString(c.getColumnIndex( "MessageStatus" ))+
                        " \n APIType: " + c.getString(c.getColumnIndex( "APIType" ))+
                        " \n assignedClient: " + c.getString(c.getColumnIndex( "assignedClient" ))+
                        " \n rsno: " + c.getString(c.getColumnIndex( "rsno" ))+
                        " \n \n"    );
            } while (c.moveToNext());
        }
        Log.e("show", stringBuilder+"==");

    }




}