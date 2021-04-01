package com.example.clientproject2;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import static com.example.clientproject2.MainActivity.ACTIONS;
import static com.example.clientproject2.MainActivity.ASSIGNDATETIME;
import static com.example.clientproject2.MainActivity.ASSIGNEDTO;
import static com.example.clientproject2.MainActivity.DATA;
import static com.example.clientproject2.MainActivity.LOG_TABLE_URI;
import static com.example.clientproject2.MainActivity.RECIPENT_MSG_URI;
import static com.example.clientproject2.MainActivity.SRC;
import static com.example.clientproject2.MainActivity.TASK_TABLE_URI;
import static com.example.clientproject2.MainActivity.TIMESTAMP;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TestJobService extends JobService {
    private static final String TAG = "HardWorkService";
    private boolean dataError = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        Log.i(TAG, "on start job: " + params.getJobId()+"  currentTime: "+ currentTime );
        for(int i=0; i>=0; i++){
            OnNewTaskClick() ;
            Log.e("tag", "intent: "+ i) ;

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

        long currentTime = Calendar.getInstance().getTimeInMillis();

        ContentValues values = new ContentValues() ;
        values.put("clientName", "Client 2");
        int c= getContentResolver().update(students, values, "", null) ;

        Log.e("TAG", "c: "+c ) ;
    }



}