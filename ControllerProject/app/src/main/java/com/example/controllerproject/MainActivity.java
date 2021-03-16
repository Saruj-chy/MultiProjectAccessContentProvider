package com.example.controllerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.controllerproject.notify.NotificationActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String URL = "http://192.168.1.149/android/AgamiLab/smart_shop/sendPushNotification.php" ;

    private EditText mLogNoEditText, mSrcEditText, mDataEditText;
    private Button mInsertBtn, mUpdateBtn, mDeleteBtn, mShowBtn, mAssignBtn, mCompleteBtn ;
    private TextView mShowTextView ;

    private String mSLNo, mAssignedTo, mMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        FirebaseMessaging.getInstance().subscribeToTopic("com.example.controllerproject") ;
    }



    public void OnInsertClick(View v){
        getAllEditText();

        long currentTime = Calendar.getInstance().getTimeInMillis();
        ContentValues task_Values = new ContentValues();
        task_Values.put(ControllerProvider.MSG, mMsg);
        task_Values.put(ControllerProvider.ASSIGNEDTO, mAssignedTo);
        task_Values.put(ControllerProvider.ENTRYDATETIME, currentTime);
        task_Values.put(ControllerProvider.ASSIGNDATETIME, 0 );
        task_Values.put(ControllerProvider.COMPLETEDATETIME, 0 );
        task_Values.put(ControllerProvider.ISCOMPLETE, 0 );

        Uri task_uri = getContentResolver().insert( ControllerProvider.TASK_TABLE_URI, task_Values);
        Log.e("TAG", "task_uri: "+task_uri) ;

        LogTableUri(mAssignedTo, mMsg, " inserted by Controller", currentTime ) ;


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
        getAllEditText();

        long currentTime = Calendar.getInstance().getTimeInMillis();
        ContentValues values = new ContentValues();
        values.put(ControllerProvider.MSG, mMsg);
        values.put(ControllerProvider.ASSIGNEDTO, mAssignedTo);


//        Uri students = Uri.parse(URL);
        if(mSLNo.equalsIgnoreCase("")){
            mSLNo=0+"";
        }
//        Toast.makeText(this, "sl no: "+mSLNo , Toast.LENGTH_SHORT).show();
        int c= getContentResolver().update(ControllerProvider.TASK_TABLE_URI, values, "sl= "+ mSLNo , null) ;

        if (c > 0) {
            Toast.makeText(this, " updated successfull.", Toast.LENGTH_SHORT).show();
            LogTableUri(mAssignedTo, mMsg, "sl no. "+mSLNo +" updated by Controller", currentTime ) ;

        }
//        ClearAllText();
    }
    private void sendDataMessageFromServer(String title, String message, String data, String topic) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response) ;

//                if(response.equalsIgnoreCase("true")){
//                    titleEditText.setText("");
//                    editText.setText("");
//
//                    Toast.makeText(getApplicationContext(), "Post upload Successfully...", Toast.LENGTH_SHORT).show();
//                } else{
//                    Toast.makeText(getApplicationContext(), "Please check Connection...", Toast.LENGTH_SHORT).show();
//                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please check Connection...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("title", title);
                parameters.put("message", message);
                parameters.put("data", data);
                parameters.put("topic", topic);

                Log.e("TAG", title+" "+message) ;

                return parameters;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Log.d("TAG", "stringRequest: "+stringRequest ) ;


    }

    public void OnDeleteClick(View v){

        sendDataMessageFromServer("title", "message", "data", "com.example.client1");

//        startActivity(new Intent(getApplicationContext(), NotificationActivity.class));

//        getAllEditText();
//        if(mSLNo.equalsIgnoreCase("")){
//            mSLNo=0+"";
//        }
//
////        Toast.makeText(this, "sl no: "+mSLNo, Toast.LENGTH_SHORT).show();
//
//        long currentTime = Calendar.getInstance().getTimeInMillis();
//        int c= getContentResolver().delete(ControllerProvider.TASK_TABLE_URI, "sl= "+ mSLNo , null) ;
//        if (c > 0) {
//            Toast.makeText(this, "sl no. "+mSLNo+" deleted successfully", Toast.LENGTH_SHORT).show();
//            LogTableUri(mAssignedTo, mMsg, "sl no. "+mSLNo +" deleted by Controller", currentTime ) ;
//        }
//        ClearAllText();
    }

    public void OnTaskShowClick(View v){

        Uri students = Uri.parse(String.valueOf(ControllerProvider.TASK_TABLE_URI));
        Cursor c = managedQuery(students, null, "all_task", null, "");

        Log.e("count", c.getCount() +"") ;
//        Log.e("task", c.getString(c.getColumnIndex(ControllerProvider.MSG)) ) ;

        StringBuilder stringBuilder = new StringBuilder();
        if (c.moveToFirst()) {
//            Log.e("task", c.getString(c.getColumnIndex(ControllerProvider.MSG)) ) ;
            do{
                stringBuilder.append(" SL: "+c.getString(c.getColumnIndex(ControllerProvider.SL)) +
                        " \n MSG: " +  c.getString(c.getColumnIndex( ControllerProvider.MSG)) +
                        " \n assignedto: " +  c.getString(c.getColumnIndex( ControllerProvider.ASSIGNEDTO)) +
                        " \n entrydatetime: " + c.getString(c.getColumnIndex( ControllerProvider.ENTRYDATETIME ))+
                        " \n assigndatetime: " + c.getString(c.getColumnIndex( ControllerProvider.ASSIGNDATETIME ))+
                        " \n Complete datetime: " + c.getString(c.getColumnIndex( ControllerProvider.COMPLETEDATETIME ))+
                        " \n isComplete: " + c.getString(c.getColumnIndex( ControllerProvider.ISCOMPLETE ))+
                        " \n \n"    );
            } while (c.moveToNext());
        }
        mShowTextView.setText(stringBuilder) ;
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
    public void OnLogShowClick(View v){

        Uri students = Uri.parse(String.valueOf(ControllerProvider.LOG_TABLE_URI));
        Cursor c = managedQuery(students, null, "all_log", null, "");

        Log.e("count", c.getCount() +"") ;
//        Log.e("task", c.getString(c.getColumnIndex(ControllerProvider.MSG)) ) ;

        StringBuilder stringBuilder = new StringBuilder();
        if (c.moveToFirst()) {
//            Log.e("task", c.getString(c.getColumnIndex(ControllerProvider.MSG)) ) ;
            do{
                stringBuilder.append(" Log no: "+c.getString(c.getColumnIndex(ControllerProvider.LOGNO)) +
                        " \n Src: " +  c.getString(c.getColumnIndex( ControllerProvider.SRC)) +
                        " \n Actions: " +  c.getString(c.getColumnIndex( ControllerProvider.ACTIONS)) +
                        " \n Data: " + c.getString(c.getColumnIndex( ControllerProvider.DATA ))+
                        " \n Timestamp: " + c.getString(c.getColumnIndex( ControllerProvider.TIMESTAMP ))+

                        " \n \n"    );
            } while (c.moveToNext());
        }
        mShowTextView.setText(stringBuilder) ;
//        ClearAllText();

    }

    private void initialize() {
        mLogNoEditText = findViewById(R.id.edit_sl_no) ;
        mSrcEditText = findViewById(R.id.edit_src) ;
        mDataEditText = findViewById(R.id.edit_data) ;
        mInsertBtn = findViewById(R.id.btn_insert) ;
        mUpdateBtn = findViewById(R.id.btn_update) ;
        mDeleteBtn = findViewById(R.id.btn_delete) ;
        mShowBtn = findViewById(R.id.btn_task_table_show) ;
        mShowTextView = findViewById(R.id.text_show) ;
    }

    private void getAllEditText(){
        mSLNo = mLogNoEditText.getText().toString() ;
        mAssignedTo = mSrcEditText.getText().toString() ;
        mMsg = mDataEditText.getText().toString() ;

    }

    private void ClearAllText(){
        mLogNoEditText.setText("");
        mSrcEditText.setText("");
        mDataEditText.setText("");
    }


    public void OnNewTaskClick(View v){
        Uri students = Uri.parse(String.valueOf(ControllerProvider.TASK_TABLE_URI));
        Cursor cursor = managedQuery(students, null, "new_task", null, "");

        String sl = null;
        if (cursor.moveToFirst()) {
            sl = cursor.getString(cursor.getColumnIndex(ControllerProvider.SL));
        }
        Toast.makeText(this, "sl"+sl, Toast.LENGTH_SHORT).show();

        long currentTime = Calendar.getInstance().getTimeInMillis();
//
        getAllEditText();

        ContentValues values = new ContentValues() ;
        values.put(ControllerProvider.ASSIGNEDTO, "c1");
        values.put(ControllerProvider.ASSIGNDATETIME, currentTime);
        values.put(ControllerProvider.COMPLETEDATETIME, 0);
        values.put(ControllerProvider.ISCOMPLETE, 0);

        int c= getContentResolver().update(ControllerProvider.TASK_TABLE_URI, values, "sl=\""+ sl +"\"", null) ;

        Toast.makeText(this, " update ", Toast.LENGTH_SHORT).show();
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

}