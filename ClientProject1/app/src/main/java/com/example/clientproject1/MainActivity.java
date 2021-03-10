package com.example.clientproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    static final String PROVIDER_NAME = "com.example.controllerproject.ControllerProvider";
    static final String LOG_URL = "content://" + PROVIDER_NAME + "/logtable";
    static final Uri LOG_TABLE_URI = Uri.parse(LOG_URL);
    static final String TASK_URL = "content://" + PROVIDER_NAME + "/tasktable";
    static final Uri TASK_TABLE_URI = Uri.parse(TASK_URL);

    //    static final String ID = "id";
    static final String LOGNO = "logno";
    static final String SRC = "src";
    static final String ACTIONS = "actions";
    static final String DATA = "data";
    static final String TIMESTAMP = "timestamp";

    //  task table
    static final String SL = "sl";
    static final String MSG = "msg";
    static final String ASSIGNEDTO = "assignedto";
    static final String ENTRYDATETIME = "entrydatetime";
    static final String ASSIGNDATETIME = "assigndatetime";
    static final String COMPLETEDATETIME = "completedatetime";
    static final String ISCOMPLETE = "icComplete";

    private EditText mLogNoEditText, mSrcEditText, mDataEditText;
    private Button mInsertBtn, mUpdateBtn, mDeleteBtn, mShowBtn, mAssignBtn, mCompleteBtn ;
    private TextView mShowTextView ;

    private String mLogNo, mAssignedTo, mMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        getAllEditText();

        onDataShow( mAssignedTo) ;

    }




    public void OnUpdateClick(View v){
        long currentTime = Calendar.getInstance().getTimeInMillis();

        getAllEditText();

        ContentValues values = new ContentValues();
        values.put(MSG, mMsg);
//        values.put(ASSIGNDATETIME, currentTime);
//        values.put(COMPLETEDATETIME, 0);
//        values.put(ISCOMPLETE, 0);

//        Uri students = Uri.parse(URL);
        int c= getContentResolver().update(TASK_TABLE_URI, values, "assignedto=\""+ mAssignedTo +"\"", null) ;

//        Toast.makeText(this, " update ", Toast.LENGTH_SHORT).show();
        mShowTextView.setText("updated msg by c1");

        LogTableUri(mAssignedTo, mMsg, mAssignedTo +" msg updated by c1", currentTime ) ;

        ClearAllText();

//        Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();

    }
    private void LogTableUri(String mSrc, String mData, String msg, long currentTime) {
        ContentValues log_Values = new ContentValues();
        log_Values.put(SRC, mSrc);
        log_Values.put(DATA, mData);
        log_Values.put(ACTIONS, msg);
        log_Values.put(TIMESTAMP, currentTime );

        Uri log_uri = getContentResolver().insert( LOG_TABLE_URI, log_Values);
        Log.e("TAG", "log_uri: "+log_uri) ;
    }
    public void OnAssignedClick(View v){
        long currentTime = Calendar.getInstance().getTimeInMillis();

        getAllEditText();

        ContentValues values = new ContentValues();
//        values.put(ControllerProvider.MSG, mMsg);
        values.put(ASSIGNDATETIME, currentTime);
        values.put(COMPLETEDATETIME, 0);
        values.put(ISCOMPLETE, 0);

//        Uri students = Uri.parse(URL);
        int c= getContentResolver().update(TASK_TABLE_URI, values, "assignedto=\""+ mAssignedTo +"\"", null) ;

        Toast.makeText(this, " assign "+c+"  "+ mAssignedTo, Toast.LENGTH_SHORT).show();
        mShowTextView.setText("assigndatetime updated by c1");

        LogTableUri(mAssignedTo, mMsg, "assigndatetime updated by c1", currentTime ) ;

        ClearAllText();

//        Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();

    }
    public void OnCompletedClick(View v){
        long currentTime = Calendar.getInstance().getTimeInMillis();

        getAllEditText();

        ContentValues values = new ContentValues();
//        values.put(ControllerProvider.MSG, mMsg);
        values.put(COMPLETEDATETIME, currentTime);
        values.put(ISCOMPLETE, 1);

//        Uri students = Uri.parse(URL);
        int c= getContentResolver().update(TASK_TABLE_URI, values, "assignedto=\""+ mAssignedTo +"\"", null) ;

        Toast.makeText(this, " complete "+c+"  "+ mAssignedTo, Toast.LENGTH_SHORT).show();

        mShowTextView.setText("completeddatetime updated by c1");

        LogTableUri(mAssignedTo, mMsg, " completeddatetime updated by c1", currentTime ) ;

        ClearAllText();

//        Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();

    }



    public void OnShowClick(View v){
            getAllEditText();

        onDataShow( "c1") ;

        ClearAllText();
    }

    private void onDataShow(String mAssignedTo) {

        Uri students = Uri.parse(String.valueOf(TASK_TABLE_URI));
        Cursor c = managedQuery(students, null, mAssignedTo, null, "");

        if (c.moveToFirst()) {
            do{
                mShowTextView.setText(" MSG: "+c.getString(c.getColumnIndex(MSG)) +
                        " \n assignedto: " +  c.getString(c.getColumnIndex( ASSIGNEDTO)) +
                        " \n entrydatetime: " + c.getString(c.getColumnIndex( ENTRYDATETIME ))+
                        " \n assigndatetime: " + c.getString(c.getColumnIndex( ASSIGNDATETIME ))+
                        " \n Complete datetime: " + c.getString(c.getColumnIndex( COMPLETEDATETIME ))+
                        " \n isComplete: " + c.getString(c.getColumnIndex( ISCOMPLETE )));

            } while (c.moveToNext());
        }else{
            mShowTextView.setText("No task assigned");
        }

    }

    private void initialize() {
        mLogNoEditText = findViewById(R.id.edit_log_no) ;
        mSrcEditText = findViewById(R.id.edit_src) ;
        mDataEditText = findViewById(R.id.edit_data) ;
        mInsertBtn = findViewById(R.id.btn_insert) ;
        mUpdateBtn = findViewById(R.id.btn_update) ;
        mDeleteBtn = findViewById(R.id.btn_delete) ;
        mShowBtn = findViewById(R.id.btn_show) ;
        mAssignBtn = findViewById(R.id.btn_assigned) ;
        mCompleteBtn = findViewById(R.id.btn_completed) ;
        mShowTextView = findViewById(R.id.text_show) ;
    }

    private void getAllEditText(){
        mLogNo = mLogNoEditText.getText().toString() ;
        mAssignedTo = mSrcEditText.getText().toString() ;
        mMsg = mDataEditText.getText().toString() ;

    }

    private void ClearAllText(){
//        mLogNoEditText.setText("");
//        mSrcEditText.setText("");
        mDataEditText.setText("");
    }
}