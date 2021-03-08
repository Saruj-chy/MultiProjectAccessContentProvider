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

    private EditText mProjectNoEditText, mProjectNameEditText, mProjectStatusEditText ;
    private Button mInsertBtn, mUpdateBtn, mDeleteBtn, mShowBtn ;
    private TextView mShowTextView ;

    private String mProjectNo, mProjectName, mProjectStatus ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    public void OnInsertClick(View v){
        getAllEditText();

        ContentValues values = new ContentValues();
        values.put(ControllerProvider.PROJECT_NO, mProjectNo);
        values.put(ControllerProvider.PROJECT_NAME, mProjectName);
        values.put(ControllerProvider.PROJECT_STATUS, mProjectStatus);


        Uri uri = getContentResolver().insert( ControllerProvider.CONTENT_URI, values);


//        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();

        Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
        mShowTextView.setText("Inserted project no "+ mProjectNo);


        Log.e("TAG", "uri: "+uri) ;
        ClearAllText();

    }

    public void OnUpdateClick(View v){
        getAllEditText();

        ContentValues values = new ContentValues();
//        values.put(ControllerProvider.PROJECT_NO, mProjectNo);
        values.put(ControllerProvider.PROJECT_NAME, mProjectName);
        values.put(ControllerProvider.PROJECT_STATUS, mProjectStatus);

//        Uri students = Uri.parse(URL);
        int c= getContentResolver().update(ControllerProvider.CONTENT_URI, values, "project_no="+mProjectNo, null) ;

        Toast.makeText(this, " update ", Toast.LENGTH_SHORT).show();
        mShowTextView.setText("updated project no "+ mProjectNo);


        ClearAllText();

    }

    public void OnDeleteClick(View v){

        getAllEditText();

        ContentValues values = new ContentValues();
//        values.put(ControllerProvider.PROJECT_NO, mProjectNo);
        values.put(ControllerProvider.PROJECT_NAME, mProjectName);
        values.put(ControllerProvider.PROJECT_STATUS, mProjectStatus);

//        Uri students = Uri.parse(URL);
//        int c= getContentResolver().update(ControllerProvider.CONTENT_URI, values, "project_no="+mProjectNo, null) ;
        int c= getContentResolver().delete(ControllerProvider.CONTENT_URI, "project_no="+mProjectNo, null) ;

        Toast.makeText(this, " Delete Project No "+ mProjectNo, Toast.LENGTH_SHORT).show();
        mShowTextView.setText("deleted project no "+ mProjectNo);

        ClearAllText();

    }

    public void OnShowClick(View v){
        // Retrieve student records
//        String URL = "content://com.example.contentproviderproject01.StudentsProvider";

        getAllEditText();

        Uri students = Uri.parse(String.valueOf(ControllerProvider.CONTENT_URI));
        Cursor c = managedQuery(students, null, mProjectNo, null, "name");

        if (c.moveToFirst()) {
            do{

                mShowTextView.setText("Project No.: "+c.getString(c.getColumnIndex(ControllerProvider.PROJECT_NO)) +
                        " \n Project Name: " +  c.getString(c.getColumnIndex( ControllerProvider.PROJECT_NAME)) +
                        " \n Project Status: " + c.getString(c.getColumnIndex( ControllerProvider.PROJECT_STATUS )));

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
        mProjectNoEditText = findViewById(R.id.edit_project_no) ;
        mProjectNameEditText = findViewById(R.id.edit_project_name) ;
        mProjectStatusEditText = findViewById(R.id.edit_project_status) ;
        mInsertBtn = findViewById(R.id.btn_insert) ;
        mUpdateBtn = findViewById(R.id.btn_update) ;
        mDeleteBtn = findViewById(R.id.btn_delete) ;
        mShowBtn = findViewById(R.id.btn_show) ;
        mShowTextView = findViewById(R.id.text_show) ;
    }

    private void getAllEditText(){
        mProjectNo = mProjectNoEditText.getText().toString() ;
        mProjectName = mProjectNameEditText.getText().toString() ;
        mProjectStatus = mProjectStatusEditText.getText().toString() ;

    }

    private void ClearAllText(){
        mProjectNoEditText.setText("");
        mProjectNameEditText.setText("");
        mProjectStatusEditText.setText("");
    }
}