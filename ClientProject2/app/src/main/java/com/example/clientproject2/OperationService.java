package com.example.clientproject2;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.Nullable;


import java.util.Calendar;

import static com.example.clientproject2.MainActivity.SRC;
import static com.example.clientproject2.MainActivity.DATA;
import static com.example.clientproject2.MainActivity.ACTIONS;
import static com.example.clientproject2.MainActivity.TIMESTAMP;
import static com.example.clientproject2.MainActivity.LOG_TABLE_URI;
import static com.example.clientproject2.MainActivity.ASSIGNDATETIME;
import static com.example.clientproject2.MainActivity.ASSIGNEDTO;
import static com.example.clientproject2.MainActivity.SL;
import static com.example.clientproject2.MainActivity.TASK_TABLE_URI;

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

        Log.e("tag", "intent") ;
        for(int i=0; i<50; i++){
            OnNewTaskClick() ;
            Log.e("tag", "intent") ;

            try {
                Thread.sleep(1000);
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
        values.put(ASSIGNEDTO, "c2");
        values.put(ASSIGNDATETIME, currentTime);

        int c= getContentResolver().update(TASK_TABLE_URI, values, "sl=\""+ sl +"\" and assignedto = \"\" ", null) ;
        if(c>0){
            LogTableUri("c2", "mMsg", "c2 assigned new task sl no: "+ sl, currentTime ) ;
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
