package com.example.clientproject2;

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

    private String mSLNo, mAssignedTo, mMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();


    }

    public void OnUpdateClick(View v){

        getAllEditText();

        long currentTime = Calendar.getInstance().getTimeInMillis();
        ContentValues values = new ContentValues();
        values.put(MSG, mMsg);

        int c= getContentResolver().update(TASK_TABLE_URI, values, "sl= "+ mSLNo +" and assignedto=\""+ mAssignedTo +"\"", null) ;

        if(c>0){
            Toast.makeText(this, " update msg successfull", Toast.LENGTH_SHORT).show();
            LogTableUri(mAssignedTo, mMsg, "sl no "+ mSLNo +" msg updated by "+mAssignedTo, currentTime ) ;
            ClearAllText();

        }


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
    public void OnCompletedClick(View v){
        getAllEditText();

        long currentTime = Calendar.getInstance().getTimeInMillis();
        ContentValues values = new ContentValues();
        values.put(COMPLETEDATETIME, currentTime);
        values.put(ISCOMPLETE, 1);

        int c= getContentResolver().update(TASK_TABLE_URI, values, "sl= "+ mSLNo +" and assignedto=\""+ mAssignedTo +"\"", null) ;
        if(c>0){
            Toast.makeText(this, " task completed sl no: "+ mSLNo, Toast.LENGTH_SHORT).show();
            LogTableUri(mAssignedTo, mMsg, "sl no "+ mSLNo +" completed by "+mAssignedTo, currentTime ) ;

            ClearAllText();
        }

//        mShowTextView.setText("completeddatetime updated by c1");


    }
    public void OnNewTaskClick(View v){
        Uri students = Uri.parse(String.valueOf(TASK_TABLE_URI));
        Cursor cursor = managedQuery(students, null, "new_task", null, null);

        String sl = null;
        if (cursor.moveToFirst()) {
            sl = cursor.getString(cursor.getColumnIndex(SL));
        }

        getAllEditText();

        long currentTime = Calendar.getInstance().getTimeInMillis();
        ContentValues values = new ContentValues() ;
        values.put(ASSIGNEDTO, mAssignedTo);
        values.put(ASSIGNDATETIME, currentTime);

        int c= getContentResolver().update(TASK_TABLE_URI, values, "sl=\""+ sl +"\"", null) ;
        if(c>0){
            LogTableUri(mAssignedTo, mMsg, mAssignedTo+" assigned new task sl no: "+ sl, currentTime ) ;
            Toast.makeText(this, " New Task Assigned successfull.", Toast.LENGTH_SHORT).show();
        }


//        ClearAllText();
    }
    public void OnShowClick(View v){
        getAllEditText();
        Uri students = Uri.parse(String.valueOf(TASK_TABLE_URI));
        Cursor c = managedQuery(students, null, mAssignedTo, null, "");

        Log.e("count", c.getCount() +"") ;
//        Log.e("task", c.getString(c.getColumnIndex(ControllerProvider.MSG)) ) ;

        StringBuilder stringBuilder = new StringBuilder();
        if (c.moveToFirst()) {
//            Log.e("task", c.getString(c.getColumnIndex(ControllerProvider.MSG)) ) ;
            do{
                stringBuilder.append(" SL: "+c.getString(c.getColumnIndex(SL)) +
                        " \n MSG: "+c.getString(c.getColumnIndex(MSG)) +
                        " \n assignedto: " +  c.getString(c.getColumnIndex( ASSIGNEDTO)) +
                        " \n entrydatetime: " + c.getString(c.getColumnIndex( ENTRYDATETIME ))+
                        " \n assigndatetime: " + c.getString(c.getColumnIndex( ASSIGNDATETIME ))+
                        " \n Complete datetime: " + c.getString(c.getColumnIndex( COMPLETEDATETIME ))+
                        " \n isComplete: " + c.getString(c.getColumnIndex( ISCOMPLETE ))+
                        " \n \n"    );
            } while (c.moveToNext());
        }
        mShowTextView.setText(stringBuilder) ;

        ClearAllText();
    }

    private void initialize() {
        mLogNoEditText = findViewById(R.id.edit_sl_no) ;
        mSrcEditText = findViewById(R.id.edit_assignedto) ;
        mDataEditText = findViewById(R.id.edit_msg) ;
        mInsertBtn = findViewById(R.id.btn_new_task) ;
        mUpdateBtn = findViewById(R.id.btn_update) ;
        mDeleteBtn = findViewById(R.id.btn_completed) ;
        mShowBtn = findViewById(R.id.btn_show) ;
        mCompleteBtn = findViewById(R.id.btn_completed) ;
        mShowTextView = findViewById(R.id.text_show) ;
    }
    private void getAllEditText(){
        mSLNo = mLogNoEditText.getText().toString() ;
        mAssignedTo = mSrcEditText.getText().toString() ;
        mMsg = mDataEditText.getText().toString() ;

    }
    private void ClearAllText(){
        mLogNoEditText.setText("");
//        mSrcEditText.setText("");
        mDataEditText.setText("");
    }
}