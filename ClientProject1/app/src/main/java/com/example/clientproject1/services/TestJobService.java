package com.example.clientproject1.services;


import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.example.clientproject1.FirebaseShedulerActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import static com.example.clientproject1.MainActivity.ACTIONS;
import static com.example.clientproject1.MainActivity.ASSIGNDATETIME;
import static com.example.clientproject1.MainActivity.ASSIGNEDTO;
import static com.example.clientproject1.MainActivity.DATA;
import static com.example.clientproject1.MainActivity.LOG_TABLE_URI;
import static com.example.clientproject1.MainActivity.SRC;
import static com.example.clientproject1.MainActivity.TASK_TABLE_URI;
import static com.example.clientproject1.MainActivity.TIMESTAMP;

@SuppressLint("SpecifyJobSchedulerIdRange")
public class TestJobService extends JobService {
    private static final String TAG = "HardWorkService";
    private boolean dataError = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        // We don't do any real 'work' in this sample app. All we'll
        // do is track which jobs have landed on our service, and
        // update the UI accordingly.
        Log.i(TAG, "on start job: " + params.getJobId()+"  currentTime: "+ currentTime );
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



        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        Log.i(TAG, "on stop job: " + params.getJobId() +"  currentTime: "+ currentTime );
        return false;
    }

    FirebaseShedulerActivity mActivity;
    private final LinkedList<JobParameters> jobParamsMap = new LinkedList<JobParameters>();

    public void setUiCallback(FirebaseShedulerActivity activity) {

        mActivity = activity;
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
//        values.put(ASSIGNEDTO, "c2");
//        values.put(ASSIGNDATETIME, currentTime);
//
//        int c= getContentResolver().update(TASK_TABLE_URI, values, "sl=\""+ sl +"\" and assignedto = \"\" ", null) ;
//        if(c>0){
//            LogTableUri("c2", "c2 update task sl no.  "+sl,  "c2 assigned new task sl no: "+ sl, currentTime ) ;
//        }



        long currentTime = Calendar.getInstance().getTimeInMillis();
        ContentValues values = new ContentValues() ;
        values.put(ASSIGNEDTO, "Client1");
        values.put(ASSIGNDATETIME, currentTime);

        int c= getContentResolver().update(TASK_TABLE_URI, values, "sl = ( SELECT sl FROM task_table WHERE assignedto=\"\" LIMIT 1) ", null) ;


        if(c>0){
            LogTableUri("Client1", "Client1 update task  ", "Client1 assigned new task ", currentTime ) ;
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