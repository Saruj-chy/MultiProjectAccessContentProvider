package com.example.controllerproject;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class ControllerProvider2 extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.controllerproject.ControllerProvider2";
    static final String LOG_URL = "content://" + PROVIDER_NAME + "/tasktable";
    static final Uri LOG_TABLE_URI = Uri.parse(LOG_URL);
//    static final String TASK_URL = "content://" + PROVIDER_NAME + "/tasktable";
//    static final Uri TASK_TABLE_URI = Uri.parse(TASK_URL);

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

    private static HashMap<String, String> STUDENTS_PROJECTION_MAP;

    static final int LOGTABLES = 1;
    static final int LOGTABLE_ID = 2;
//    static final int TASKTABLES = 1;
//    static final int TASKTABLE_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "tasktable", LOGTABLES);
        uriMatcher.addURI(PROVIDER_NAME, "tasktable/s#", LOGTABLE_ID);
//        uriMatcher.addURI(PROVIDER_NAME, "tasktable", TASKTABLES);
//        uriMatcher.addURI(PROVIDER_NAME, "tasktable/s#", TASKTABLE_ID);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "TaskControllers";
    static final String TASK_TABLE_NAME = "task_table";

    static final int DATABASE_VERSION = 1;
//    static final String CREATE_LOG_TABLE =
//            " CREATE TABLE " + LOG_TABLE_NAME +
//                    " ("+ SL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    MSG + " TEXT NOT NULL, " +
//                    ACTIONS + " TEXT , " +
//                    DATA + " TEXT NOT NULL, " +
//                    TIMESTAMP+" INTEGER NOT NULL);" ;

    static final String CREATE_TASK_TABLE =
            " CREATE TABLE " + TASK_TABLE_NAME +
                    " ("+ SL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MSG + " TEXT NOT NULL, " +
                    ASSIGNEDTO + " TEXT NOT NULL, " +
                    ENTRYDATETIME + " TEXT NOT NULL, " +
                    ASSIGNDATETIME + " TEXT NOT NULL, " +
                    COMPLETEDATETIME + " TEXT NOT NULL, " +
                    ISCOMPLETE + " TEXT NOT NULL);" ;


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//            db.execSQL(CREATE_LOG_TABLE);
            db.execSQL(CREATE_TASK_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null)? false : true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long log_table_ID = db.insert(TASK_TABLE_NAME, "", values);
//        long task_table_ID = db.insert(TASK_TABLE_NAME, "", values);
        if (log_table_ID > 0) {
            Uri log_uri = ContentUris.withAppendedId(LOG_TABLE_URI, log_table_ID);
            getContext().getContentResolver().notifyChange(log_uri, null);
            return log_uri;
        }
//        if (task_table_ID > 0) {
//            Uri task_uri = ContentUris.withAppendedId(TASK_TABLE_URI, task_table_ID);
//            getContext().getContentResolver().notifyChange(task_uri, null);
//            return task_uri;
//        }

        Log.e("inserturi", log_table_ID+" "+ uri) ;

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {

        Log.e("query", selection+" :s: "+ sortOrder) ;
        String selectQuery ;

        selectQuery = "SELECT  * FROM  "+ TASK_TABLE_NAME +" WHERE assignedto = "+selection  ;
//        if(selection.equalsIgnoreCase("")) {
//            selection = String.valueOf(1);
//        }
//        if(!sortOrder.equalsIgnoreCase("")){
//
//        }else{
//            selectQuery = "SELECT  * FROM  "+ LOG_TABLE_NAME +" WHERE logno = "+selection+" " ;
//        }
        Cursor c = db.rawQuery(selectQuery, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case LOGTABLES:
                count = db.delete(TASK_TABLE_NAME, selection, selectionArgs);
                break;

            case LOGTABLE_ID:
                String log_no = uri.getPathSegments().get(2);
                count = db.delete(TASK_TABLE_NAME, LOGNO +  " = " + log_no +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case LOGTABLES:
                count = db.update(TASK_TABLE_NAME, values, selection, selectionArgs);
                break;

            case LOGTABLE_ID:
                count = db.update(TASK_TABLE_NAME, values,
                        LOGNO + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){

            case LOGTABLES:
                return "com.android.cursor.dir/com.example.controls";
            case LOGTABLE_ID:
                return "com.android.cursor.item/com.example.controls";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}