package com.example.clientproject1.JobDispatcher;


import android.os.AsyncTask;
import android.os.Build;
import android.text.PrecomputedText;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import javax.xml.transform.Result;

public class MyService extends JobService {
    BackgrondTask backgrondTask ;
    @Override
    public boolean onStartJob(JobParameters job) {
        Log.e("TAG","message from background task " ) ;

        backgrondTask = new BackgrondTask(){
            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(MyService.this, "message from background task "+ s , Toast.LENGTH_LONG ).show();
                Log.e("TAG","message from background task "+ s ) ;

                jobFinished(job, false);
            }
        };
        backgrondTask.execute() ;
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    public static class BackgrondTask extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... voids) {
            return "hello from background job";
        }
    }
}
