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
import android.widget.Toast;

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
        task_Values.put(ControllerProvider.ASSIGNDATETIME, 0 );
        task_Values.put(ControllerProvider.COMPLETEDATETIME, 0 );
        task_Values.put(ControllerProvider.ISCOMPLETE, 0 );

        Uri task_uri = getContentResolver().insert( ControllerProvider.TASK_TABLE_URI, task_Values);
        Log.e("TAG", "task_uri: "+task_uri) ;

//        Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
//        mShowTextView.setText("Inserted log no "+ mLogNo);
//
//        Log.e("TAG", "uri: "+uri) ;
//        ClearAllText();

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

//        ClearAllText();

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

//        ClearAllText();

//        Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();

    }
    public void OnCompletedClick(View v){


//        long currentTime = Calendar.getInstance().getTimeInMillis();
//
//        getAllEditText();
//
//        ContentValues values = new ContentValues();
////        values.put(ControllerProvider.MSG, mMsg);
//        values.put(ControllerProvider.COMPLETEDATETIME, currentTime);
//        values.put(ControllerProvider.ISCOMPLETE, 1);
//
////        Uri students = Uri.parse(URL);
//        int c= getContentResolver().update(ControllerProvider.TASK_TABLE_URI, values, "assignedto=\""+ mAssignedTo +"\"", null) ;
//
//        mShowTextView.setText("completeddatetime updated by Controller  ");
//
//        LogTableUri(mAssignedTo, mMsg, " completeddatetime updated by Controller", currentTime ) ;
//
////        ClearAllText();
//
////        Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();

    }

    public void OnDeleteClick(View v){

        getAllEditText();
        int c= getContentResolver().delete(ControllerProvider.LOG_TABLE_URI, "assignedto=\""+ mAssignedTo +"\"", null) ;
        mShowTextView.setText("deleted assignedto "+ mAssignedTo);

//        ClearAllText();
    }

    public void OnShowClick(View v){

        Uri students = Uri.parse(String.valueOf(ControllerProvider.TASK_TABLE_URI));
        Cursor c = managedQuery(students, null, null, null, "");

        StringBuilder stringBuilder = new StringBuilder();
        if (c.moveToFirst()) {
            do{
                stringBuilder.append(" MSG: "+c.getString(c.getColumnIndex(ControllerProvider.MSG)) +
                        " \n assignedto: " +  c.getString(c.getColumnIndex( ControllerProvider.ASSIGNEDTO)) +
                        " \n entrydatetime: " + c.getString(c.getColumnIndex( ControllerProvider.ENTRYDATETIME ))+
                        " \n assigndatetime: " + c.getString(c.getColumnIndex( ControllerProvider.ASSIGNDATETIME ))+
                        " \n Complete datetime: " + c.getString(c.getColumnIndex( ControllerProvider.COMPLETEDATETIME ))+
                        " \n isComplete: " + c.getString(c.getColumnIndex( ControllerProvider.ISCOMPLETE ))+
                        " \n \n"    );
            } while (c.moveToNext());
        }

        mShowTextView.setText(stringBuilder);
//
//        getAllEditText();
//
//        Uri students = Uri.parse(String.valueOf(ControllerProvider.TASK_TABLE_URI));
//        Cursor c = managedQuery(students, null, null, null, "");
//
//        Log.e("count", c.getCount() +"") ;
//
////        while (c.isAfterLast() == false) {
////            String Title = c.getString(c.getColumnIndex(ControllerProvider.ASSIGNEDTO));
////            Log.e("MSG"," MSG: "+ Title ) ;
////
//////            stringList.add(Title); //This I use to create listlayout dynamically and show all the Titles in it
////            c.moveToNext();
////        }
//
//        while (!c.isAfterLast()) {
//            Log.e("MSG"," MSG: " + c.isAfterLast()) ;
//            c.moveToNext();
//        }
//
//
//        if (c.moveToFirst()) {
//            Log.e("MSG"," MSG c: " ) ;
//            do{
////
//                mShowTextView.setText(" MSG: "+c.getString(c.getColumnIndex(ControllerProvider.MSG)) +
//                        " \n assignedto: " +  c.getString(c.getColumnIndex( ControllerProvider.ASSIGNEDTO)) +
//                        " \n entrydatetime: " + c.getString(c.getColumnIndex( ControllerProvider.ENTRYDATETIME ))+
//                        " \n assigndatetime: " + c.getString(c.getColumnIndex( ControllerProvider.ASSIGNDATETIME ))+
//                        " \n Complete datetime: " + c.getString(c.getColumnIndex( ControllerProvider.COMPLETEDATETIME ))+
//                        " \n isComplete: " + c.getString(c.getColumnIndex( ControllerProvider.ISCOMPLETE )));
//                Log.e("MSG"," MSG inside: " ) ;
//
//            } while (c.moveToNext());
//        }

//        ClearAllText();

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