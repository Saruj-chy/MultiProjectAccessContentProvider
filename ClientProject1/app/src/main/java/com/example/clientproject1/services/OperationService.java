package com.example.clientproject1.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.clientproject1.constants.AppConstants;
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
    private boolean dataReview = false;


    public OperationService() {
        super("name");
//        this.name1 = name ;
//        AppController.getAppController().getInAppNotifier().log("cons", " "+ name1 );

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        AppController.getAppController().getInAppNotifier().log("loop", " intent " );

        String trigger = intent.getStringExtra("int") ;
        AppController.getAppController().getInAppNotifier().log("loop", " int :"+trigger );


       if(dataReview == false){
           for(int i=0; i<50; i++){
//               dataReview = true ;
               AppController.getAppController().getInAppNotifier().log("loop", " loop "+ i );
//            Log.e("loop", " loop "+ i   );
//               OnNewTaskClick() ;
               try {
                   Thread.sleep(1000) ;
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               AppConstants.dataEnter(false);

//            if(dataError==true){
//                AppConstants.DATAENTER=false;
//                break;
//            }else
//
//               if(dataError == true){
//                   break;
//               }else if(i==49){
//                dataReview=false ;
//            }

           }

       }
    }

    public void OnNewTaskClick(){
//        }
        long currentTime = Calendar.getInstance().getTimeInMillis();
        ContentValues values = new ContentValues() ;
        values.put(ASSIGNEDTO, "Client1") ;
        values.put(ASSIGNDATETIME, currentTime);

        int c= getContentResolver().update(TASK_TABLE_URI, values, "sl = ( SELECT sl FROM task_table WHERE assignedto=\"\" LIMIT 1) ", null) ;
        AppController.getAppController().getInAppNotifier().log("loop", " c: "+ c );

        if(c>0){
            LogTableUri("Client1", "Client1 update task  ", "Client1 assigned new task ", currentTime ) ;
        }
        else{
            dataError=true;
            dataReview= false ;
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
