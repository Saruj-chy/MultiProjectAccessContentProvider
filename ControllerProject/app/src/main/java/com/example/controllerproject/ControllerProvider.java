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

import java.util.HashMap;

public class ControllerProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.controllerproject.ControllerProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/controls";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String ID = "id";
    static final String PROJECT_NO = "project_no";
    static final String PROJECT_NAME = "project_name";
    static final String PROJECT_STATUS = "project_status";

    private static HashMap<String, String> STUDENTS_PROJECTION_MAP;

    static final int CONTROLS = 1;
    static final int CONTROL_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "controls", CONTROLS);
        uriMatcher.addURI(PROVIDER_NAME, "control/s#", CONTROL_ID);
    }

    /**
     * Database specific constant declarations
     */

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Controller";
    static final String CONTROLS_TABLE_NAME = "control";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + CONTROLS_TABLE_NAME +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " project_no TEXT NOT NULL, " +
                    " project_name TEXT NOT NULL, " +
                    " project_status TEXT NOT NULL);";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CONTROLS_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new student record
         */
        long rowID = db.insert(CONTROLS_TABLE_NAME, "", values);

        /**
         * If record is added successfully
         */
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
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//        qb.setTables(STUDENTS_TABLE_NAME);
//
//        switch (uriMatcher.match(uri)) {
//            case STUDENTS:
//                qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
//                break;
//
//            case STUDENT_ID:
//                qb.appendWhere( ID + "=" + uri.getPathSegments().get(1));
//                break;
//
//            default:
//        }
//
//        if (sortOrder == null || sortOrder == ""){
//            /**
//             * By default sort on student names
//             */
//            sortOrder = PROJECT_NAME;
//        }

//        Cursor c = qb.query(db,	projection,	selection,
//                selectionArgs,null, null, sortOrder);

        String selectQuery = "SELECT  * FROM  "+ CONTROLS_TABLE_NAME +" WHERE project_no = "+selection ;

        Cursor c = db.rawQuery(selectQuery, null);


        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case CONTROLS:
                count = db.delete(CONTROLS_TABLE_NAME, selection, selectionArgs);
                break;

            case CONTROL_ID:
                String project_no = uri.getPathSegments().get(2);
                count = db.delete(CONTROLS_TABLE_NAME, PROJECT_NO +  " = " + project_no +
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
                count = db.update(CONTROLS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case CONTROL_ID:

                count = db.update(CONTROLS_TABLE_NAME, values,
                        ID + " = " + uri.getPathSegments().get(1) +
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
            /**
             * Get all student records
             */
            case CONTROLS:
                return "com.android.cursor.dir/com.example.controls";
            /**
             * Get a particular student
             */
            case CONTROL_ID:
                return "com.android.cursor.item/com.example.controls";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}