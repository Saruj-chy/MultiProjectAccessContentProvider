package com.example.clientproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.app.job.JobScheduler;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.clientproject1.JobDispatcher.ExampleJobService;
import com.example.clientproject1.services.TestJobService;

public class FirebaseShedulerActivity extends AppCompatActivity {

    public static final int MSG_UNCOLOUR_START = 0;
    public static final int MSG_UNCOLOUR_STOP = 1;
    public static final int MSG_SERVICE_OBJ = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_sheduler);

        Resources res = getResources();
        defaultColor = res.getColor(R.color.none_received);
        startJobColor = res.getColor(R.color.start_received);
        stopJobColor = res.getColor(R.color.stop_received);

        mDelayEditText = (EditText) findViewById(R.id.delay_time);
        mDeadlineEditText = (EditText) findViewById(R.id.deadline_time);
        mWiFiConnectivityRadioButton = (RadioButton) findViewById(R.id.checkbox_unmetered);
        mAnyConnectivityRadioButton = (RadioButton) findViewById(R.id.checkbox_any);
        mRequiresChargingCheckBox = (CheckBox) findViewById(R.id.checkbox_charging);
        mRequiresIdleCheckbox = (CheckBox) findViewById(R.id.checkbox_idle);
        mServiceComponent = new ComponentName(this, TestJobService.class);

    }

    // UI fields.
    int defaultColor;
    int startJobColor;
    int stopJobColor;
    private EditText mDelayEditText;
    private EditText mDeadlineEditText;
    private RadioButton mWiFiConnectivityRadioButton;
    private RadioButton mAnyConnectivityRadioButton;
    private CheckBox mRequiresChargingCheckBox;
    private CheckBox mRequiresIdleCheckbox;
    ComponentName mServiceComponent;

    /** Service object to interact scheduled jobs. */

    TestJobService mTestService;
    private static int kJobId = 0;


    /**
     * UI onclick listener to schedule a new job.
     */

    public void scheduleJob(View v) {

        // Create Job with all the constraints
        JobInfo.Builder builder = new JobInfo.Builder(kJobId++,mServiceComponent);
        String delay = mDelayEditText.getText().toString();
        if (delay != null && !TextUtils.isEmpty(delay)) {
            builder.setMinimumLatency(Long.valueOf(delay) * 1000);
        }
        String deadline = mDeadlineEditText.getText().toString();
        if (deadline != null && !TextUtils.isEmpty(deadline)) {
            builder.setOverrideDeadline(Long.valueOf(deadline) * 1000);
        }
        boolean requiresUnmetered = mWiFiConnectivityRadioButton.isChecked();
        boolean requiresAnyConnectivity = mAnyConnectivityRadioButton
                .isChecked();
        if (requiresUnmetered) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        } else if (requiresAnyConnectivity) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        }
        builder.setRequiresDeviceIdle(mRequiresIdleCheckbox.isChecked());
        builder.setRequiresCharging(mRequiresChargingCheckBox.isChecked());
        JobScheduler jobScheduler =
                (JobScheduler) getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);

        // Schedule the job
        jobScheduler.schedule(builder.build());

        Toast.makeText(getApplicationContext(), "Schedule the job", Toast.LENGTH_SHORT).show();
    }

    public void cancelAllJobs(View v) {
        JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.cancelAll();
    }

}