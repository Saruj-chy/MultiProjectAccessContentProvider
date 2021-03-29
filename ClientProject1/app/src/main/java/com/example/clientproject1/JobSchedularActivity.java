package com.example.clientproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.clientproject1.services.TestJobService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JobSchedularActivity extends AppCompatActivity {
    private static int kJobId = 0;
    ComponentName mServiceComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_schedular);

        mServiceComponent = new ComponentName(this, TestJobService.class);

    }


    public void onJobScdular(View view) {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        JobInfo.Builder builder = new JobInfo.Builder(kJobId++,mServiceComponent);
        builder.setMinimumLatency(5 * 1000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        JobScheduler jobScheduler =
                (JobScheduler) getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);

        // Schedule the job
        for (int i=0; i<10; i++){
//            Log.e("TAG", "loop "+i+"  "+ currentTime ) ;
            jobScheduler.schedule(builder.build());
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }

        Toast.makeText(getApplicationContext(), "Schedule the job", Toast.LENGTH_SHORT).show();

    }
}