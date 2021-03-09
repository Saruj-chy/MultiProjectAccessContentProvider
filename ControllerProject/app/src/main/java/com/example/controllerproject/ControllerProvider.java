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

public class ControllerProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.controllerproject.ControllerProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/controls";
    static final Uri CONTENT_URI = Uri.parse(URL);

//    static final String ID = "id";
    static final String LOGNO = "logno";
    static final String SRC = "src";
    static final String ACTIONS = "actions";
    static final String DATA = "data";
    static final String TIMESTAMP = "timestamp";

    private static HashMap<String, String> STUDENTS_PROJECTION_MAP;

    static final int CONTROLS = 1;
    static final int CONTROL_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "controls", CONTROLS);
        uriMatcher.addURI(PROVIDER_NAME, "control/s#", CONTROL_ID);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Controller";
    static final String LOG_TABLE_NAME = "log_table";
    static final String TASK_TABLE_NAME = "task_table";

    static final int DATABASE_VERSION = 1;
    static final String CREATE_LOG_TABLE =
            " CREATE TABLE " + LOG_TABLE_NAME +
                    " ("+ LOGNO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    LOGNO + " TEXT NOT NULL, " +
                    SRC + " TEXT NOT NULL, " +
                    ACTIONS + " TEXT NOT NULL, " +
                    DATA + " TEXT NOT NULL, " +
                    TIMESTAMP+" DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL);" ;

//    static final String CREATE_TASK_TABLE =
//            " CREATE TABLE " + TASK_TABLE_NAME +
//                    " ("+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    LOGNO + " TEXT NOT NULL, " +
//                    SRC + " TEXT NOT NULL, " +
//                    ACTIONS + " TEXT NOT NULL, " +
//                    DATA + " TEXT NOT NULL, " +
//                    TIMESTAMP+" DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL);" ;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_LOG_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + LOG_TABLE_NAME);
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
        long rowID = db.insert(LOG_TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {

        Log.e("query", selection+" :s: "+ sortOrder) ;
        String selectQuery ;

        selectQuery = "SELECT  * FROM  "+ LOG_TABLE_NAME +" WHERE src = \""+selection+"\"" ;
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
            case CONTROLS:
                count = db.delete(LOG_TABLE_NAME, selection, selectionArgs);
                break;

            case CONTROL_ID:
                String log_no = uri.getPathSegments().get(2);
                count = db.delete(LOG_TABLE_NAME, LOGNO +  " = " + log_no +
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
            case CONTROLS:
                count = db.update(LOG_TABLE_NAME, values, selection, selectionArgs);
                break;

            case CONTROL_ID:
                count = db.update(LOG_TABLE_NAME, values,
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

            case CONTROLS:
                return "com.android.cursor.dir/com.example.controls";
            case CONTROL_ID:
                return "com.android.cursor.item/com.example.controls";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}