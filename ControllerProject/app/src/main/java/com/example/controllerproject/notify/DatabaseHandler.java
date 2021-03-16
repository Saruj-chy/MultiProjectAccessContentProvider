package com.example.controllerproject.notify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.controllerproject.ControllerProvider;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DatabaseHandler extends SQLiteOpenHelper {


    //-------1
    private static final String TAG_Mgs = "DatabaseHandoler";

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "NOTIFICATION";
    // table name
    private static final String TABLE_USERINFO = "notification";


    private static final String NOTIFY_ID = "Id";
    private static final String NOTIFY_TITLE = "TITLE";
    private static final String NOTIFY_BODY_TEXT = "BODY_TEXT";
    private static final String NOTIFY_TOPIC = "TOPIC";

//    private static final String EMPLOY_ID = "Id";
//    private static final String EMPLOY_NAME = "name";
//    private static final String EMPLOY_POSITION = "position";
//    private static final String EMPLOY_CONTACT = "contact";
//    private static final String EMPLOY_WEBPAGE = "webpage";
//    private static final String EMPLOY_EMAIL = "email";
//    private static final String EMPLOY_ADDRESS = "address";


    //-----------2--------


    //---4

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //----------4

    //------extends SQLiteOpenHelper- by add--3--
    @Override
    public void onCreate(SQLiteDatabase db) {


        try {
            String CREATE_USERINFO = "CREATE TABLE " + TABLE_USERINFO + "("
                    + NOTIFY_ID + "  INTEGER PRIMARY KEY, "
                    + NOTIFY_TITLE + " TEXT, "
                    + NOTIFY_BODY_TEXT + " TEXT, "
                    + NOTIFY_TOPIC+ " TEXT"
                    +")";

            db.execSQL(CREATE_USERINFO);
        } catch (Exception e) {

        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);

    }
    //------extends SQLiteOpenHelper- by add--3--


    // insert data
    public void insertUserInfo(String title, String body_text, String topic ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOTIFY_TITLE, title);
        values.put(NOTIFY_BODY_TEXT, body_text);
        values.put(NOTIFY_TOPIC, topic);


        // Inserting Row
        db.insert(TABLE_USERINFO, null, values);
        Log.d("TAG", "Insert success");
        db.close(); // Closing database connection
    }


    //----------------------view data



    public ArrayList<NotifyModel> getAllInfo(Context context) {

        ArrayList<NotifyModel> userInfoList = new ArrayList<NotifyModel>();
        try {

            String selectQuery = "SELECT  * FROM  "+ TABLE_USERINFO+"" ;
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("CURSOR", "cursor size: "+ cursor.getCount() ) ;

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
//                    String title = cursor.getString(cursor.getColumnIndex(NOTIFY_TITLE));
//                    Log.e("title", title) ;
//                    cursor.moveToNext();

                    NotifyModel customList = new NotifyModel();

                    String Id = cursor.getString(0).toString() ;
                    Log.e("id", Id) ;

                    customList.setId(Id);


                    String title = cursor.getString(1).toString();
                    customList.setTitle(title);
                    Log.e("title", title) ;


                    String body_text = cursor.getString(2).toString();
                    customList.setBody_text(body_text);

                    String topic = cursor.getString(3).toString();
                    customList.setTopic(topic);

                    userInfoList.add(customList);
                }
                Log.e("TAG", "userInfo: "+ userInfoList) ;
            }






//            cursor.moveToFirst();  //Move the cursor to the first row.

//            if (cursor.moveToFirst()) {
////            Log.e("task", c.getString(c.getColumnIndex(ControllerProvider.MSG)) ) ;
//                do{
//                    NotifyModel customList = new NotifyModel();
//
//                    String Id = cursor.getString(0).toString() ;
//                    Log.e("id", Id) ;
//
//                    customList.setId(Id);
//
//
//                    String title = cursor.getString(1).toString();
//                    customList.setTitle(title);
//                    Log.e("title", title) ;
//
//
//                    String body_text = cursor.getString(2).toString();
//                    customList.setBody_text(body_text);
//
//                    String topic = cursor.getString(3).toString();
//                    customList.setTopic(topic);
//
//                    userInfoList.add(customList);
////                    cursor.moveToNext();  //Move the cursor to the next row.
//
//                } while (cursor.moveToNext());
////                cursor.close();
////                db.close();
//            }

            //Returns whether the cursor is pointing to the position after the last row.
//            while (!cursor.isAfterLast()) {
//                NotifyModel customList = new NotifyModel();
//
//                String Id =cursor.getString(cursor.getColumnIndex(NOTIFY_ID));
////                String Id = cursor.getString(0).toString() ;
//                Log.e("database id", Id) ;
//
//                customList.setId(Id);
//
//
//                String title = cursor.getString(cursor.getColumnIndex(NOTIFY_TITLE));
//                customList.setTitle(title);
//
//                String body_text = cursor.getString(cursor.getColumnIndex(NOTIFY_BODY_TEXT));
//                customList.setBody_text(body_text);
//
//                String topic = cursor.getString(cursor.getColumnIndex(NOTIFY_TOPIC));
//                customList.setTopic(topic);
//
//
//                userInfoList.add(customList);
//                cursor.moveToNext();  //Move the cursor to the next row.
//
//            }
//            cursor.close();
//            db.close();

        }

        catch (Exception ex) {
            Log.e("getCampaignId Excep. :", ex.toString());
        }

        Log.e("size", userInfoList.size()+"") ;
        return userInfoList;
    }


    //----------------------view data





}//lb