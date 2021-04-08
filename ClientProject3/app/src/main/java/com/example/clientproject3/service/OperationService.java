package com.example.clientproject3.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;

import java.util.Calendar;

import static com.example.clientproject3.MainActivity.ACTIONS;
import static com.example.clientproject3.MainActivity.ASSIGNDATETIME;
import static com.example.clientproject3.MainActivity.ASSIGNEDTO;
import static com.example.clientproject3.MainActivity.DATA;
import static com.example.clientproject3.MainActivity.LOG_TABLE_URI;
import static com.example.clientproject3.MainActivity.SL;
import static com.example.clientproject3.MainActivity.SRC;
import static com.example.clientproject3.MainActivity.TASK_TABLE_URI;
import static com.example.clientproject3.MainActivity.TIMESTAMP;

public class OperationService extends IntentService {
    private CountDownTimer countDownTimer;
    long remainingRefreshTime = 10000 ;

    private String name, name1;
    private boolean dataError = false;


    public OperationService() {
        super("name");
//        this.name1 = name ;
//        AppController.getAppController().getInAppNotifier().log("cons", " "+ name1 );

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        for(int i=0; i<50; i++){
            OnNewTaskClick() ;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(dataError==true){
                break;
            }
        }
    }

    public void OnNewTaskClick(){
//        Uri students = Uri.parse(String.valueOf(TASK_TABLE_URI));
//        Cursor cursor = getContentResolver().query(students, null, "new_task", null, null);
//
//        String sl = null;
//        if (cursor.moveToFirst()) {
//            sl = cursor.getString(cursor.getColumnIndex(SL));
//        }
//        long currentTime = Calendar.getInstance().getTimeInMillis();
//        ContentValues values = new ContentValues() ;
//        values.put(ASSIGNEDTO, "c3");
//        values.put(ASSIGNDATETIME, currentTime);
//
//        int c= getContentResolver().update(TASK_TABLE_URI, values, "sl=\""+ sl +"\" and assignedto = \"\" ",  null) ;
//        if(c>0){
//            LogTableUri("c3", "c3 update task sl no.  "+sl,  "c3 assigned new task sl no: "+ sl, currentTime ) ;
//        }


        long currentTime = Calendar.getInstance().getTimeInMillis();
        ContentValues values = new ContentValues() ;
        values.put(ASSIGNEDTO, "Client3");
        values.put(ASSIGNDATETIME, currentTime);

        int c= getContentResolver().update(TASK_TABLE_URI, values, "sl = ( SELECT sl FROM task_table WHERE assignedto=\"\" LIMIT 1) ", null) ;

        if(c>0){
            LogTableUri("Client3", "Client3 update task  ", "Client3 assigned new task ", currentTime ) ;
        } else{
            dataError=true;
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

    }



}
