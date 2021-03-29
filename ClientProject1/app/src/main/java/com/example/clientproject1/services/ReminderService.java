package com.example.clientproject1.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class ReminderService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here
        Log.e("tag", "onStartJob") ;
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.e("tag", "onStopJob") ;
        return false; // Answers the question: "Should this job be retried?"
    }
}