package com.example.clientproject1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.clientproject1.JobDispatcher.MyService;
import com.example.clientproject1.JobDispatcher.ScheduledJobService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

/*
* created by Sarose Datta
* date: 23-03-2021
*/

public class FirebaseDispatcherActivity extends AppCompatActivity {

    private static final int REMINDER_INTERVAL_SECONDS = 10000;
    private static final int SYNC_FLEXTIME_SECONDS = 1000;
    private static final String JOB_TAG = "my_job_tag";
    private FirebaseJobDispatcher jobDispatcher ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_dispatcher);

        jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));
//        WorkManager.getInstance(this).cancelUniqueWork(JOB_TAG);


//        Job myJob = firebaseJobDispatcher.newJobBuilder()
//                .setService(ReminderService.class)
//                .setTag(REMINDER_JOB_TAG)
//                .setConstraints(Constraint.DEVICE_CHARGING)
//                .setLifetime(Lifetime.FOREVER)
//                .setRecurring(true)
//                .setTrigger(Trigger.executionWindow(
//                        REMINDER_INTERVAL_SECONDS,
//                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS
//                ))
//                .setReplaceCurrent(true)
//                .build();
//
//        firebaseJobDispatcher.mustSchedule(myJob);

//        Bundle myExtrasBundle = new Bundle();
//        myExtrasBundle.putString("some_key", "some_value");
//
//        Job myJob = firebaseJobDispatcher.newJobBuilder()
//                // the JobService that will be called
//                .setService(ReminderService.class)
//                // uniquely identifies the job
//                .setTag("my-unique-tag")
//                // one-off job
//                .setRecurring(false)
//                // don't persist past a device reboot
//                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
//                // start between 0 and 60 seconds from now
//                .setTrigger(Trigger.executionWindow(0, 60))
//                // don't overwrite an existing job with the same tag
//                .setReplaceCurrent(false)
//                // retry with exponential backoff
//                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
//                // constraints that need to be satisfied for the job to run
//                .setConstraints(
//                        // only run on an unmetered network
//                        Constraint.ON_UNMETERED_NETWORK,
//                        // only run when the device is charging
//                        Constraint.DEVICE_CHARGING
//                )
//                .setExtras(myExtrasBundle)
//                .build();
//
//        jobDispatcher.mustSchedule(myJob);
    }

    public void startJob(View view) {
//        if(android.os.Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
//
////            addAppToAutoStartList();
//            Toast.makeText(this, "Job xiaomi  ...", Toast.LENGTH_SHORT).show();
//
//
//        }


        //first applied
        Job job = jobDispatcher.newJobBuilder().
                setService(MyService.class).
                setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag(JOB_TAG)
                .setTrigger(Trigger.executionWindow(5, 10))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build() ;

        jobDispatcher.mustSchedule(job);

        Toast.makeText(this, "Job Sheduled...", Toast.LENGTH_SHORT).show();
        Log.e("TAG","Job Sheduled..." ) ;

    }
    private void addAppToAutoStartList(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Warning!");
        alertDialogBuilder.setMessage("Please add this app to the Auto Start list of your device for better performance.");
        alertDialogBuilder.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                try {
//                    AppPreferences.getInstance(this).setMiSpecialSetting(true);
                    Intent intent = new Intent();
                    intent.setComponent(new
                            ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Unable to add!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            } });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static Job createJob(FirebaseJobDispatcher dispatcher){
        Job job = dispatcher.newJobBuilder()
                // persist the task across boots
                .setLifetime(Lifetime.FOREVER)
                // Call this service when the criteria are met.
                .setService(ScheduledJobService.class)
                // unique id of the task
                .setTag("OneTimeJob")
                // We are mentioning that the job is not periodic.
                .setRecurring(false)
                // Run between 30 - 60 seconds from now.
                .setTrigger(Trigger.executionWindow(30, 60))
                //Run this job only when the network is avaiable.
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        return job;
    }

    public static Job updateJob(FirebaseJobDispatcher dispatcher) {
        Job newJob = dispatcher.newJobBuilder()
                //update if any task with the given tag exists.
                .setReplaceCurrent(true)
                .setService(ScheduledJobService.class)
                .setTag("OneTimeJob")
                // Run between 60 - 120 seconds from now.
                .setTrigger(Trigger.executionWindow(60, 120))
                .build();
        return newJob;
    }
    public static void scheduleJob(Context context) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job job = createJob(dispatcher);
        dispatcher.schedule(job);
    }




    public void stopJob(View view) {

        jobDispatcher.cancel(JOB_TAG) ;
        Toast.makeText(this, "job Canceled...", Toast.LENGTH_SHORT).show();
    }

//    public static void scheduleJob(Context context) {
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
//        Job job = createJob(dispatcher);
//        dispatcher.schedule(job);
//    }
}