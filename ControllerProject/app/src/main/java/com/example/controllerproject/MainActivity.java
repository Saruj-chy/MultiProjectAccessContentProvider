package com.example.controllerproject;

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

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText mLogNoEditText, mSrcEditText, mDataEditText;
    private Button mInsertBtn, mUpdateBtn, mDeleteBtn, mShowBtn, mAssignBtn, mCompleteBtn ;
    private TextView mShowTextView ;

    private String mLogNo, mAssignedTo, mMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    public void OnInsertClick(View v){
        long currentTime = Calendar.getInstance().getTimeInMillis();
        Log.e("TAG", "currentTime1: "+currentTime) ;


        getAllEditText();

        LogTableUri(mAssignedTo, mMsg, mAssignedTo +" inserted by Controller", currentTime ) ;


        ContentValues task_Values = new ContentValues();
        task_Values.put(ControllerProvider.MSG, mMsg);
        task_Values.put(ControllerProvider.ASSIGNEDTO, mAssignedTo);
        task_Values.put(ControllerProvider.ENTRYDATETIME, currentTime);
        task_Values.put(ControllerProvider.ASSIGNDATETIME, 0+"" );
        task_Values.put(ControllerProvider.COMPLETEDATETIME, 0+"" );
        task_Values.put(ControllerProvider.ISCOMPLETE, 0+"" );

        Uri task_uri = getContentResolver().insert( ControllerProvider.TASK_TABLE_URI, task_Values);
        Log.e("TAG", "task_uri: "+task_uri) ;

//        Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
//        mShowTextView.setText("Inserted log no "+ mLogNo);
//
//        Log.e("TAG", "uri: "+uri) ;
        ClearAllText();

    }

    private void LogTableUri(String mSrc, String mData, String msg, long currentTime) {
        ContentValues log_Values = new ContentValues();
        log_Values.put(ControllerProvider.SRC, mSrc);
        log_Values.put(ControllerProvider.DATA, mData);
        log_Values.put(ControllerProvider.ACTIONS, msg);
        log_Values.put(ControllerProvider.TIMESTAMP, currentTime );

        Uri log_uri = getContentResolver().insert( ControllerProvider.LOG_TABLE_URI, log_Values);
        Log.e("TAG", "log_uri: "+log_uri) ;
    }

    public void OnUpdateClick(View v){
        long currentTime = Calendar.getInstance().getTimeInMillis();

        getAllEditText();

        ContentValues values = new ContentValues();
        values.put(ControllerProvider.MSG, mMsg);
        values.put(ControllerProvider.ASSIGNDATETIME, currentTime);
        values.put(ControllerProvider.COMPLETEDATETIME, 0);
        values.put(ControllerProvider.ISCOMPLETE, 0);

//        Uri students = Uri.parse(URL);
        int c= getContentResolver().update(ControllerProvider.TASK_TABLE_URI, values, "assignedto=\""+ mAssignedTo +"\"", null) ;

//        Toast.makeText(this, " update ", Toast.LENGTH_SHORT).show();
        mShowTextView.setText("updated assignedto no "+ mAssignedTo);

        LogTableUri(mAssignedTo, mMsg, mAssignedTo +" updated by Controller", currentTime ) ;

        ClearAllText();

//        Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();

    }
    public void OnAssignedClick(View v){
        long currentTime = Calendar.getInstance().getTimeInMillis();

        getAllEditText();

        ContentValues values = new ContentValues();
//        values.put(ControllerProvider.MSG, mMsg);
        values.put(ControllerProvider.ASSIGNDATETIME, currentTime);
        values.put(ControllerProvider.COMPLETEDATETIME, 0);
        values.put(ControllerProvider.ISCOMPLETE, 0);

//        Uri students = Uri.parse(URL);
        int c= getContentResolver().update(ControllerProvider.TASK_TABLE_URI, values, "assignedto=\""+ mAssignedTo +"\"", null) ;

//        Toast.makeText(this, " update ", Toast.LENGTH_SHORT).show();
        mShowTextView.setText("assigndatetime updated by Controller");

        LogTableUri(mAssignedTo, mMsg, "assigndatetime updated by Controller", currentTime ) ;

        ClearAllText();

//        Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();

    }
    public void OnCompletedClick(View v){
        long currentTime = Calendar.getInstance().getTimeInMillis();

        getAllEditText();

        ContentValues values = new ContentValues();
//        values.put(ControllerProvider.MSG, mMsg);
        values.put(ControllerProvider.COMPLETEDATETIME, currentTime);
        values.put(ControllerProvider.ISCOMPLETE, 1);

//        Uri students = Uri.parse(URL);
        int c= getContentResolver().update(ControllerProvider.TASK_TABLE_URI, values, "assignedto=\""+ mAssignedTo +"\"", null) ;

        mShowTextView.setText("completeddatetime updated by Controller  ");

        LogTableUri(mAssignedTo, mMsg, " completeddatetime updated by Controller", currentTime ) ;

        ClearAllText();

//        Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();

    }

    public void OnDeleteClick(View v){

        getAllEditText();

//        ContentValues values = new ContentValues();
//        values.put(ControllerProvider.LOGNO, mLogNo);
//        values.put(ControllerProvider.SRC, mSrc);
//        values.put(ControllerProvider.DATA, mData);
//        values.put(ControllerProvider.ACTIONS, "Client1 inserted by Controller");

//        Uri students = Uri.parse(URL);
//        int c= getContentResolver().update(ControllerProvider.CONTENT_URI, values, "project_no="+mProjectNo, null) ;
        int c= getContentResolver().delete(ControllerProvider.LOG_TABLE_URI, "assignedto=\""+ mAssignedTo +"\"", null) ;

//        Toast.makeText(this, " Delete Project No "+ mProjectNo, Toast.LENGTH_SHORT).show();
        mShowTextView.setText("deleted logno "+ mLogNo);

        ClearAllText();

//        Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();


    }

    public void OnShowClick(View v){
        // Retrieve student records
//        String URL = "content://com.example.contentproviderproject01.StudentsProvider";

        getAllEditText();

        Uri students = Uri.parse(String.valueOf(ControllerProvider.TASK_TABLE_URI));
        Cursor c = managedQuery(students, null, mAssignedTo, null, "");

        if (c.moveToFirst()) {
            do{

                mShowTextView.setText(" MSG: "+c.getString(c.getColumnIndex(ControllerProvider.MSG)) +
                        " \n assignedto: " +  c.getString(c.getColumnIndex( ControllerProvider.ASSIGNEDTO)) +
                        " \n entrydatetime: " + c.getString(c.getColumnIndex( ControllerProvider.ENTRYDATETIME ))+
                        " \n assigndatetime: " + c.getString(c.getColumnIndex( ControllerProvider.ASSIGNDATETIME ))+
                        " \n Complete datetime: " + c.getString(c.getColumnIndex( ControllerProvider.COMPLETEDATETIME ))+
                        " \n isComplete: " + c.getString(c.getColumnIndex( ControllerProvider.ISCOMPLETE )));

//                Toast.makeText(this,
//                        c.getString(c.getColumnIndex(ControllerProvider.PROJECT_NO)) +
//                                " \n" +  c.getString(c.getColumnIndex( ControllerProvider.PROJECT_NAME)) +
//                                " \n" + c.getString(c.getColumnIndex( ControllerProvider.PROJECT_STATUS )),
//                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }

        ClearAllText();

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
        mLogNoEditText.setText("");
        mSrcEditText.setText("");
        mDataEditText.setText("");
    }
}