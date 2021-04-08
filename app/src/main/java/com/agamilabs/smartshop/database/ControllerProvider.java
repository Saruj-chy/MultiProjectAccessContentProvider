package com.agamilabs.smartshop.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.agamilabs.smartshop.controller.AppController;

import java.util.HashMap;

public class ControllerProvider extends ContentProvider {
    public static final String PROVIDER_NAME = "com.agamilabs.smartshop.database.ControllerProvider";
    public static final String RECIPANT_MSG_URL = "content://" + PROVIDER_NAME + "/RecipientMessages";

    private SQLiteDatabase db;
    SmsDatabaseHelper smsDatabaseHelper;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        SmsDatabaseHelper dbHelper = new SmsDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        smsDatabaseHelper = new SmsDatabaseHelper(context) ;
        return (db == null)? false : true;
    }
    private static HashMap<String, String> CONTROLLER_TASK_MAP;

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor c = null;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        db.beginTransaction();
        try {
//            Cursor result = db.rawQuery("SELECT * FROM RecipientMessages WHERE MessageStatus = 0 and assignedClient = '"+selection+"' order by entryDateTime Limit 1", null);

            String selectQuery = "SELECT * FROM "+ SmsDatabaseHelper.RECIPIENT_SMS_TABLE_NAME +
                    "  WHERE MessageStatus = 0 and assignedClient = '"+selection+"' order by entryDateTime Limit 1" ;
            AppController.getAppController().getInAppNotifier().log("selectQuery","selectQuery :"+ selectQuery );
            c = db.rawQuery(selectQuery, null);
            db.setTransactionSuccessful();
        }  finally {
            db.endTransaction();
        }

        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;

        switch (selection){
            case "client":
                String clientName = values.getAsString("clientName") ;
                count = smsDatabaseHelper.updateAssignedTable(clientName, selection) ;
//                Log.e("Update", "count: "+ count+ "  clientName:" + clientName  ) ;
                break;
            case "rsNo":
                String rsNo = values.getAsString("rsno") ;
                count = smsDatabaseHelper.updateAssignedTable(rsNo, selection) ;
                break;
            default:
                break;
        }

        return count;
    }
}
