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

import java.util.HashMap;

public class ControllerProvider extends ContentProvider {
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


    private static HashMap<String, String> STUDENTS_PROJECTION_MAP;

    //  task table
    static final String SL = "sl";
    static final String MSG = "msg";
    static final String ASSIGNEDTO = "assignedto";
    static final String ENTRYDATETIME = "entrydatetime";
    static final String ASSIGNDATETIME = "assigndatetime";
    static final String COMPLETEDATETIME = "completedatetime";
    static final String ISCOMPLETE = "icComplete";

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

        Log.e("inserturi", log_table_ID+" "+ uri) ;

        throw new SQLException("Failed to add a record into " + uri);
    }

//    @Override
//    public Cursor query(Uri uri, String[] projection,
//                        String selection, String[] selectionArgs, String sortOrder) {
//
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//        qb.setTables(TASK_TABLE_NAME);
//
//        qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
//
////        switch (uriMatcher.match(uri)) {
////            case STUDENTS:
////                qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
////                break;
////
////            case STUDENT_ID:
////                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
////                break;
////
////            default:
////        }
//
////        if (sortOrder == null || sortOrder == ""){
////            /**
////             * By default sort on student names
////             */
////            sortOrder = NAME;
////        }
//
//        Cursor c = qb.query(db,	projection,	selection,
//                selectionArgs,null, null, sortOrder);
//        /**
//         * register to watch a content URI for changes
//         */
//        c.setNotificationUri(getContext().getContentResolver(), uri);
//        return c;
//
//
//
////
////
////        Log.e("query", selection+" :s: "+ sortOrder) ;
////        String selectQuery ;
////
//////        selectQuery = "SELECT  * FROM  "+ TASK_TABLE_NAME +" WHERE assignedto = \""+selection+"\" "  ;
////        selectQuery = "SELECT  * FROM  "+ TASK_TABLE_NAME   ;
//////        if(selection.equalsIgnoreCase("")) {
//////            selection = String.valueOf(1);
//////        }
//////        if(!sortOrder.equalsIgnoreCase("")){
//////
//////        }else{
//////            selectQuery = "SELECT  * FROM  "+ LOG_TABLE_NAME +" WHERE logno = "+selection+" " ;
//////        }
////        Cursor c = db.rawQuery(selectQuery, null);
////        c.setNotificationUri(getContext().getContentResolver(), uri);
////        return c;
//    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection,String[] selectionArgs, String sortOrder) {

        Cursor c;
        switch (selection){
            case "new_task":
                String selectQuery = "SELECT * FROM "+ TASK_TABLE_NAME +"  WHERE `assignedto` = \"\" ORDER BY sl ASC LIMIT 1" ;
                c = db.rawQuery(selectQuery, null);
                break;
            case "all_task":
                SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                qb.setTables(TASK_TABLE_NAME);
                qb.setProjectionMap(STUDENTS_PROJECTION_MAP);

                c = qb.query(db, projection,	"",
                        selectionArgs,null, null, sortOrder);

                c.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            default:
                SQLiteQueryBuilder qb1 = new SQLiteQueryBuilder();
                qb1.setTables(TASK_TABLE_NAME);
                qb1.setProjectionMap(STUDENTS_4PRO
                        JECTION_MAP);

                c = qb1.query(db, projection,	"",
                        selectionArgs,null, null, sortOrder);

                c.setNotificationUri(getContext().getContentResolver(), uri);

//                String singleSelectQuery = "SELECT * FROM "+ TASK_TABLE_NAME +"  WHERE assignedto =\""+ selection+"\"" ;
//                c = db.rawQuery(singleSelectQuery, null);
                break;

        }

//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//        qb.setTables(TASK_TABLE_NAME);
//        qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
//
//        c = qb.query(db, projection,	selection,
//                selectionArgs,null, null, sortOrder);
//        /**
//         * register to watch a content URI for changes
//         */
//        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        count = db.delete(LOG_TABLE_NAME, selection, selectionArgs);

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