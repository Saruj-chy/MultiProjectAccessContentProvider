package com.example.clientproject1.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.clientproject1.controller.AppController;

import java.io.File;
import java.util.Calendar;

import static com.example.clientproject1.MainActivity.ACTIONS;
import static com.example.clientproject1.MainActivity.ASSIGNDATETIME;
import static com.example.clientproject1.MainActivity.ASSIGNEDTO;
import static com.example.clientproject1.MainActivity.DATA;
import static com.example.clientproject1.MainActivity.LOG_TABLE_URI;
import static com.example.clientproject1.MainActivity.SL;
import static com.example.clientproject1.MainActivity.SRC;
import static com.example.clientproject1.MainActivity.TASK_TABLE_URI;
import static com.example.clientproject1.MainActivity.TIMESTAMP;

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
            AppController.getAppController().getInAppNotifier().log("loop", " loop "+ i );
            AppController.getAppController().getInAppNotifier().log("dataError", " dataError: "+ dataError );
            Log.e("loop", " loop "+ i   );
            OnNewTaskClick() ;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            if(dataError==true){
//                break;
//            }
        }

    }

    public void OnNewTaskClick(){
        Uri students = Uri.parse(String.valueOf(TASK_TABLE_URI));
        Cursor cursor = getContentResolver().query(students, null, "new_task", null, null);

        String sl = null;
        if (cursor.moveToFirst()) {
            sl = cursor.getString(cursor.getColumnIndex(SL));
        }
        long currentTime = Calendar.getInstance().getTimeInMillis();
        ContentValues values = new ContentValues() ;
        values.put(ASSIGNEDTO, "c1");
        values.put(ASSIGNDATETIME, currentTime);

        int c= getContentResolver().update(TASK_TABLE_URI, values, "sl=\""+ sl +"\" and assignedto = \"\" ", null) ;
        if(c>0){
            LogTableUri("c1", "mMsg", "c1 assigned new task sl no: "+ sl, currentTime ) ;
        }else{
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
