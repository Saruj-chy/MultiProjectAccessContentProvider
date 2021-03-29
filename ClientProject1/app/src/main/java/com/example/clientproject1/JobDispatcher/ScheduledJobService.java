package com.example.clientproject1.JobDispatcher;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class ScheduledJobService extends JobService {

    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.e("TAG", "startjob") ;
        //Offloading work to a new thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                completeJob(params);
            }
        }).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e("TAG", "stopjob") ;
        return false;
    }

    public void completeJob(final JobParameters parameters) {
        try {
            //This task takes 2 seconds to complete.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //Tell the framework that the job has completed and doesnot needs to be reschedule
            jobFinished(parameters, false);
        }
    }


}
