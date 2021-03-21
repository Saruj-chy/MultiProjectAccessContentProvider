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
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.controllerproject.controller.AppController;

import java.util.HashMap;
import java.util.Random;

public class ControllerProvider extends ContentProvider {
   public static final String PROVIDER_NAME = "com.example.controllerproject.ControllerProvider";
    public static final String LOG_URL = "content://" + PROVIDER_NAME + "/logtable";
    public static final Uri LOG_TABLE_URI = Uri.parse(LOG_URL);
    public static final String TASK_URL = "content://" + PROVIDER_NAME + "/tasktable";
    public static final Uri TASK_TABLE_URI = Uri.parse(TASK_URL);

//    static final String ID = "id";
    public static final String LOGNO = "logno";
    public static final String SRC = "src";
    public static final String ACTIONS = "actions";
    public static final String DATA = "data";
    public static final String TIMESTAMP = "timestamp";


    private static HashMap<String, String> CONTROLLER_TASK_MAP;
    private static HashMap<String, String> CONTROLLER_LOG_MAP;
    private static HashMap<String, String>  CLIENT_TASK_MAP;

    //  task table
    public static final String SL = "sl";
    public static final String MSG = "msg";
    public static final String ASSIGNEDTO = "assignedto";
    public static final String ENTRYDATETIME = "entrydatetime";
    public static final String ASSIGNDATETIME = "assigndatetime";
    public static final String COMPLETEDATETIME = "completedatetime";
    public static final String ISCOMPLETE = "icComplete";

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "TASKLOGTABLE";
    static final String LOG_TABLE_NAME = "log_table";
    static final String TASK_TABLE_NAME = "task_table";

    static final int DATABASE_VERSION = 1;
    static final String CREATE_LOG_TABLE =
            " CREATE TABLE " + LOG_TABLE_NAME +
                    " ("+ LOGNO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SRC + " TEXT NOT NULL, " +
                    ACTIONS + " TEXT , " +
                    DATA + " TEXT NOT NULL, " +
                    TIMESTAMP+" INTEGER NOT NULL);" ;

    static final String CREATE_TASK_TABLE =
            " CREATE TABLE " + TASK_TABLE_NAME +
                    " ("+ SL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MSG + " TEXT NOT NULL, " +
                    ASSIGNEDTO + " INTEGER NOT NULL, " +
                    ENTRYDATETIME + " INTEGER NOT NULL, " +
                    ASSIGNDATETIME + " INTEGER NOT NULL, " +
                    COMPLETEDATETIME + " INTEGER NOT NULL, " +
                    ISCOMPLETE + " INTEGER NOT NULL);" ;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_LOG_TABLE);
            db.execSQL(CREATE_TASK_TABLE);
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
        long log_table_ID = db.insert(LOG_TABLE_NAME, "", values);
        long task_table_ID = db.insert(TASK_TABLE_NAME, "", values);
        if (log_table_ID > 0) {
            Uri log_uri = ContentUris.withAppendedId(LOG_TABLE_URI, log_table_ID);
            getContext().getContentResolver().notifyChange(log_uri, null);
            return log_uri;
        }
        if (task_table_ID > 0) {
            Uri task_uri = ContentUris.withAppendedId(TASK_TABLE_URI, task_table_ID);
            getContext().getContentResolver().notifyChange(task_uri, null);
            return task_uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }


    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection,String[] selectionArgs, String sortOrder) {

        Cursor c;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (selection){
            case "new_task":
                db.beginTransaction();
                try {
                    String selectQuery = "SELECT * FROM "+ TASK_TABLE_NAME +"  WHERE `assignedto` = \"\" ORDER BY sl ASC LIMIT 1" ;
                    c = db.rawQuery(selectQuery, null);
                    db.setTransactionSuccessful();
                }  finally {
                    db.endTransaction();
                }
                break;
            case "all_task":
                qb.setTables(TASK_TABLE_NAME);
                qb.setProjectionMap(CONTROLLER_TASK_MAP);

                c = qb.query(db, projection,	"",
                        selectionArgs,null, null, sortOrder);

                c.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            case "all_log":
                qb.setTables(LOG_TABLE_NAME);
                qb.setProjectionMap(CONTROLLER_LOG_MAP);

                c = qb.query(db, projection,	"",
                        selectionArgs,null, null, sortOrder);

                c.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            default:
                SQLiteQueryBuilder qb1 = new SQLiteQueryBuilder();
                qb1.setTables(TASK_TABLE_NAME);
                qb1.setProjectionMap(CLIENT_TASK_MAP);

                c = qb1.query(db, projection,	"assignedto= \""+selection+"\"",
                        selectionArgs,null, null, sortOrder);

                c.setNotificationUri(getContext().getContentResolver(), uri);
                break;
        }
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        count = db.delete(TASK_TABLE_NAME, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int count = 0;
        count = db.update(TASK_TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }
}