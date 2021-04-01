package com.example.clientproject1.services;


import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.example.clientproject1.FirebaseShedulerActivity;
import com.example.clientproject1.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import static com.example.clientproject1.MainActivity.RECIPENT_MSG_URI;


@SuppressLint("SpecifyJobSchedulerIdRange")
public class TestJobService extends JobService {
    private static final String TAG = "HardWorkService";

    @Override
    public boolean onStartJob(JobParameters params) {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        for(int i=1; i<=50; i++){
            OnNewTaskClick() ;
            Log.e("tag", "intent "+i+" client1") ;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        Log.i(TAG, "on stop job: " + params.getJobId() +"  currentTime: "+ currentTime );
        return false;
    }

    public void OnNewTaskClick(){

        Uri students = Uri.parse(String.valueOf(RECIPENT_MSG_URI));

        ContentValues values = new ContentValues() ;
        values.put("clientName", "Client 1");
        int c= getContentResolver().update(students, values, "", null) ;

        Log.e("TAG", "c: "+c ) ;
    }




}