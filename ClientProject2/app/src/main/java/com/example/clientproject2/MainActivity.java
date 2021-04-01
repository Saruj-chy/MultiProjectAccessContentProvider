package com.example.clientproject2;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    static final String PROVIDER_NAME = "com.agamilabs.smartshop.database.ControllerProvider";
    static final String LOG_URL = "content://" + PROVIDER_NAME + "/logtable";
    static final Uri LOG_TABLE_URI = Uri.parse(LOG_URL);
    static final String TASK_URL = "content://" + PROVIDER_NAME + "/tasktable";
    static final Uri TASK_TABLE_URI = Uri.parse(TASK_URL);

    public static final String PROVIDER_NAME1 = "com.agamilabs.smartshop.database.ControllerProvider";
    public static final String RECIPENT_MSG = "content://" + PROVIDER_NAME1 + "/recipent_msg";
    public static final Uri RECIPENT_MSG_URI = Uri.parse(RECIPENT_MSG);

    //    static final String ID = "id";
    static final String LOGNO = "logno";
    static final String SRC = "src";
    static final String ACTIONS = "actions";
    static final String DATA = "data";
    static final String TIMESTAMP = "timestamp";

    //  task table
    static final String SL = "sl";
    static final String MSG = "msg";
    static final String ASSIGNEDTO = "assignedto";
    static final String ENTRYDATETIME = "entrydatetime";
    static final String ASSIGNDATETIME = "assigndatetime";
    static final String COMPLETEDATETIME = "completedatetime";
    static final String ISCOMPLETE = "icComplete";

    private EditText mLogNoEditText, mSrcEditText, mDataEditText;
    private Button mInsertBtn, mUpdateBtn, mDeleteBtn, mShowBtn, mAssignBtn, mCompleteBtn ;
    private TextView mShowTextView ;

    private String mSLNo, mAssignedTo, mMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("ALLCLIENT") ;


    }
}