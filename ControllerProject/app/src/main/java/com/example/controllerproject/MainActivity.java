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

public class MainActivity extends AppCompatActivity {

    private EditText mLogNoEditText, mSrcEditText, mDataEditText;
    private Button mInsertBtn, mUpdateBtn, mDeleteBtn, mShowBtn ;
    private TextView mShowTextView ;

    private String mLogNo, mSrc, mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    public void OnInsertClick(View v){
        getAllEditText();

        ContentValues values = new ContentValues();
//        values.put(ControllerProvider.LOGNO, mLogNo);
        values.put(ControllerProvider.SRC, mSrc);
        values.put(ControllerProvider.DATA, mData);
        values.put(ControllerProvider.ACTIONS, mSrc+" inserted by Controller");


        Uri uri = getContentResolver().insert( ControllerProvider.CONTENT_URI, values);

        Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
        mShowTextView.setText("Inserted log no "+ mLogNo);

        Log.e("TAG", "uri: "+uri) ;
        ClearAllText();

    }

    public void OnUpdateClick(View v){
        getAllEditText();

        ContentValues values = new ContentValues();
//        values.put(ControllerProvider.LOGNO, mLogNo);
        values.put(ControllerProvider.SRC, mSrc);
        values.put(ControllerProvider.DATA, mData);
        values.put(ControllerProvider.ACTIONS, "data Updated by Controller");

//        Uri students = Uri.parse(URL);
        int c= getContentResolver().update(ControllerProvider.CONTENT_URI, values, "src=\""+mSrc+"\"", null) ;

//        Toast.makeText(this, " update ", Toast.LENGTH_SHORT).show();
        mShowTextView.setText("updated Log no "+ mLogNo);


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
        int c= getContentResolver().delete(ControllerProvider.CONTENT_URI, "src=\""+mSrc+"\"", null) ;

//        Toast.makeText(this, " Delete Project No "+ mProjectNo, Toast.LENGTH_SHORT).show();
        mShowTextView.setText("deleted logno "+ mLogNo);

        ClearAllText();

//        Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show();


    }

    public void OnShowClick(View v){
        // Retrieve student records
//        String URL = "content://com.example.contentproviderproject01.StudentsProvider";

        getAllEditText();

        Uri students = Uri.parse(String.valueOf(ControllerProvider.CONTENT_URI));
        Cursor c = managedQuery(students, null, mSrc, null, "");

        if (c.moveToFirst()) {
            do{

                mShowTextView.setText(" LogNo: "+c.getString(c.getColumnIndex(ControllerProvider.LOGNO)) +
                        " \n Src: " +  c.getString(c.getColumnIndex( ControllerProvider.SRC)) +
                        " \n Action: " + c.getString(c.getColumnIndex( ControllerProvider.ACTIONS ))+
                        " \n Data: " + c.getString(c.getColumnIndex( ControllerProvider.DATA ))+
                        " \n Current TimeStamp: " + c.getString(c.getColumnIndex( ControllerProvider.TIMESTAMP )));

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
        mShowTextView = findViewById(R.id.text_show) ;
    }

    private void getAllEditText(){
        mLogNo = mLogNoEditText.getText().toString() ;
        mSrc = mSrcEditText.getText().toString() ;
        mData = mDataEditText.getText().toString() ;

    }

    private void ClearAllText(){
        mLogNoEditText.setText("");
        mSrcEditText.setText("");
        mDataEditText.setText("");
    }
}