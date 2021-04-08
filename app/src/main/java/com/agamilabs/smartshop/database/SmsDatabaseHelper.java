package com.agamilabs.smartshop.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.agamilabs.smartshop.model.RecipientSmsModel;
import com.agamilabs.smartshop.model.Sms;
import com.agamilabs.smartshop.model.Test;

import java.util.ArrayList;
import java.util.Calendar;

public class SmsDatabaseHelper extends SQLiteOpenHelper {
    // Initialise constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SMS";
    private static final String SMS_TABLE_NAME = "Messages";
    public static final String RECIPIENT_SMS_TABLE_NAME = "RecipientMessages";
    private static final String CLIENT_APP_INFO_TABLE_NAME = "ClientAppInfos";
    private static final String TEST_TABLE_NAME = "SmsObserver";
    //private static final String SMSQueue_TABLE_NAME = "HandelSMSQueue";
    private static final String APPOBSERVER_TABLE_NAME = "AppObserver";
    //private static final String CLIENT_STATUS_TABLE_NAME = "ClientStatus";
    private static final String[] COLUMN_NAMES = {"SmsID", "CampaignName", "RecipientsNumber","Message",
            "MessageDate", "MessageTime","MessageStatus","APIType"};
    private static final String[] COLUMN_NAMES_TEST = {"SmsID","RecipientsNumber","Message", "MessageDateTime","SQLiteDeafultDateTime"};
    //private static final String[] COLUMN_NAMES_SMSQueue = {"SLNO","CampaignScheduleDateTime"};
    private static final String[] COLUMN_NAMES_APPOBSERVER = {"Action","CalenderDateTime","SQLiteDeafultDateTime"};
    //private static final String[] COLUMN_NAMES_CLIENT_STATUS = {"Client1Status","Client2Status","Client3Status", "Client4Status","Client5Status"};
    //
    private static final String TABLE_NAME = "client_status";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_NAME = "client_name";
    private static final String CLIENT_STATUS = "status";
    int rsNo;
    Context mContext;

    //saruj
    public static final String PROVIDER_NAME = "com.agamilabs.smartshop.database.ControllerProvider";
    public static final String RECIPENT_MSG = "content://" + PROVIDER_NAME + "/recipent_msg";
    public static final Uri RECIPENT_MSG_URI = Uri.parse(RECIPENT_MSG);

    private static final String SMS_TABLE_CREATE =
            "CREATE TABLE " + SMS_TABLE_NAME + " (" +
                    COLUMN_NAMES[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAMES[1] + " TEXT, " +
                    COLUMN_NAMES[2] + " TEXT, " +
                    COLUMN_NAMES[3] + " TEXT, " +
                    COLUMN_NAMES[4] + " TEXT, " +
                    COLUMN_NAMES[5] + " TEXT, " +
                    COLUMN_NAMES[6] + " TEXT, " +
                    COLUMN_NAMES[7] + " TEXT);";

    private static final String RECIPIENT_SMS_TABLE_CREATE =
            "CREATE TABLE " + RECIPIENT_SMS_TABLE_NAME + " (" +
                    RecipientSms.RSNO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RecipientSms.SMSID + " INTEGER, " +
                    RecipientSms.APITYPE + " TEXT, " +
                    RecipientSms.ASSIGNDATETIME + " INTEGER, " +
                    RecipientSms.ASSIGNEDCLIENT + " TEXT, " +
                    RecipientSms.COMPLETEDATETIME + " INTEGER, " +
                    RecipientSms.ENTRYDATETIME + " INTEGER, " +
                    RecipientSms.MESSAGE + " TEXT, " +
                    RecipientSms.MESSAGESTATUS + " INTEGER, " +
                    RecipientSms.RECIPIENTSNUMBER + " TEXT, " +
                    RecipientSms.RETRYCOUNT + " INTEGER);";

    private static final String CLIENT_APP_INFO_TABLE_CREATE =
            "CREATE TABLE " + CLIENT_APP_INFO_TABLE_NAME + " (" +
                    ClientAppInfo.CSLNO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ClientAppInfo.FILENAME + " TEXT, " +
                    ClientAppInfo.CREATEDATETIME + " INTEGER, " +
                    ClientAppInfo.DOWNLOADSTARTDATETIME + " INTEGER, " +
                    ClientAppInfo.COMPLETEDATETIME + " INTEGER);";

    private static final String TEST_TABLE_CREATE =
            "CREATE TABLE " + TEST_TABLE_NAME + " (" +
                    COLUMN_NAMES_TEST[0] + " TEXT, " +
                    COLUMN_NAMES_TEST[1] + " TEXT, " +
                    COLUMN_NAMES_TEST[2] + " TEXT, " +
                    COLUMN_NAMES_TEST[3] + " TEXT, " +
                    COLUMN_NAMES_TEST[4] + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    /*private static final String CLIENT_STATUS_TABLE_CREATE =
            "CREATE TABLE " + CLIENT_STATUS_TABLE_NAME + " (" +
                    COLUMN_NAMES_CLIENT_STATUS[0] + " TEXT, " +
                    COLUMN_NAMES_CLIENT_STATUS[1] + " TEXT, " +
                    COLUMN_NAMES_CLIENT_STATUS[2] + " TEXT, " +
                    COLUMN_NAMES_CLIENT_STATUS[3] + " TEXT, " +
                    COLUMN_NAMES_CLIENT_STATUS[4] + " TEXT);";*/

   /* private static final String SMSQueue_TABLE_CREATE =
            "CREATE TABLE " + SMSQueue_TABLE_NAME + " (" +
                    COLUMN_NAMES_SMSQueue[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAMES_SMSQueue[1] + " TEXT);";*/

    private static final String APPOBSERVER_TABLE_CREATE =
            "CREATE TABLE " + APPOBSERVER_TABLE_NAME + " (" +
                    COLUMN_NAMES_APPOBSERVER[0] + " TEXT, " +
                    COLUMN_NAMES_APPOBSERVER[1] + " TEXT, " +
                    COLUMN_NAMES_APPOBSERVER[2] + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";



    public SmsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates the database if it doesn't exist and adds Messages table
        // Execute SQL query.
        //db.execSQL(SMSQueue_TABLE_CREATE);
        db.execSQL(SMS_TABLE_CREATE);
        db.execSQL(TEST_TABLE_CREATE);
        db.execSQL(APPOBSERVER_TABLE_CREATE);
        db.execSQL(RECIPIENT_SMS_TABLE_CREATE);
        db.execSQL(CLIENT_APP_INFO_TABLE_CREATE);
        //db.execSQL(CLIENT_STATUS_TABLE_CREATE);

        try {
            String CREATE_USERINFO = "CREATE TABLE " + TABLE_NAME + "("
                    + CLIENT_ID + "  INTEGER PRIMARY KEY, "
                    + CLIENT_NAME + " TEXT, "
                    + CLIENT_STATUS + " TEXT "
                    +")";

            db.execSQL(CREATE_USERINFO);
        } catch (Exception e) {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /*public void addSmsIntoQueue(String campaignScheduleDateTime) {
        // Put Sms data into ContentValues object to insert into database
        ContentValues row = new ContentValues();
        row.put(this.COLUMN_NAMES_SMSQueue[1], campaignScheduleDateTime);

        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // insert sms details into a new row, return the id of the new row.
        db.insert(SMSQueue_TABLE_NAME, null, row);
        db.close();
    }*/
    /* public int addTestInfo(Test test) {
        // Put Sms data into ContentValues object to insert into database
        ContentValues row = new ContentValues();
        row.put(this.COLUMN_NAMES_TEST[0], test.SmsID);
        row.put(this.COLUMN_NAMES_TEST[1], test.recipientsNumber);
        row.put(this.COLUMN_NAMES_TEST[2], test.message);
        row.put(this.COLUMN_NAMES_TEST[3], test.messageDateTime);

        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // insert sms details into a new row, return the id of the new row.
        long longid = db.insert(TEST_TABLE_NAME, null, row);
        db.close();

        // convert id from long to int and return
        int SlNO = (int) longid;
        return SlNO;
    }

    public void addAppObserverInfo(String action,String calenderDateTime) {
        // Put Sms data into ContentValues object to insert into database
        ContentValues row = new ContentValues();
        row.put(this.COLUMN_NAMES_APPOBSERVER[0], action);
        row.put(this.COLUMN_NAMES_APPOBSERVER[1], calenderDateTime);

        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // insert sms details into a new row, return the id of the new row.
        db.insert(APPOBSERVER_TABLE_NAME, null, row);
        db.close();
    }

    public void removeSms(Integer SmsID) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Create where clause
        String whereClause = "SmsID = '" + SmsID + "'";

        // Remove row from table with SmsID passed.
        db.delete(SMS_TABLE_NAME, whereClause, null);
    }

    public ArrayList<Sms> getSmsByID(int searchID) {
        // Get the readable database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Get sms by ID
        Cursor result = db.rawQuery("SELECT * FROM Messages WHERE SmsID =" + searchID, null);

        // Create list of sms objects
        ArrayList<Sms> sms = new ArrayList<Sms>();

        // For number of sms retrieved create a sms object with name, number, message, message date, message time.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            sms.add(new Sms(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6),result.getString(7)));
        }

        return sms;
    }*/
    /* public ArrayList<RecipientSmsModel> getRecipientSmsByID(int searchID) {
        // Get the readable database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Get sms by ID
        Cursor result = db.rawQuery("SELECT * FROM RecipientMessages WHERE SmsID =" + searchID, null);

        // Create list of sms objects
        ArrayList<RecipientSmsModel> recipientSms = new ArrayList<RecipientSmsModel>();

        // For number of sms retrieved create a sms object with name, number, message, message date, message time.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            recipientSms.add(new RecipientSmsModel(
                    result.getInt(result.getColumnIndex(RecipientSms.RSNO)),
                    result.getInt(result.getColumnIndex(RecipientSms.SMSID)),
                    result.getString(result.getColumnIndex(RecipientSms.RECIPIENTSNUMBER)),
                    result.getString(result.getColumnIndex(RecipientSms.MESSAGE)),
                    result.getInt(result.getColumnIndex(RecipientSms.MESSAGESTATUS)),
                    result.getString(result.getColumnIndex(RecipientSms.APITYPE)),
                    result.getLong(result.getColumnIndex(RecipientSms.ENTRYDATETIME)),
                    result.getString(result.getColumnIndex(RecipientSms.ASSIGNEDCLIENT)),
                    result.getLong(result.getColumnIndex(RecipientSms.ASSIGNDATETIME)),
                    result.getLong(result.getColumnIndex(RecipientSms.COMPLETEDATETIME)),
                    result.getInt(result.getColumnIndex(RecipientSms.RETRYCOUNT))
                    ));
        }

        return recipientSms;
    }

    public void updateSmsToSent(Integer SmsID) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Put Sms ID into ContentValues object
        ContentValues cv = new ContentValues();
        cv.put("messageStatus", "Sent");

        // Update messageStatus to Sent where SMSID is equal to SMS ID passed
        db.update(SMS_TABLE_NAME, cv, "SmsID=" + SmsID, null);
    }

    *//*public void updateClientStatus(String clientName,String status) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Put Sms ID into ContentValues object
        ContentValues cv = new ContentValues();
        cv.put(clientName, status);

        // Update messageStatus to Sent where SMSID is equal to SMS ID passed
        db.update(CLIENT_STATUS_TABLE_NAME, cv, null, null);
    }*//*

    public void updateSmsToFailed(Integer SmsID) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Put Sms ID into ContentValues object update SMS
        ContentValues cv = new ContentValues();
        cv.put("messageStatus", "Failed");

        // Update messageStatus to Failed where SMSID is equal to SMS ID passed
        db.update(SMS_TABLE_NAME, cv, "SmsID=" + SmsID, null);
    }

    public int retrieveSmsID(Sms sms) {
        // Get the readable database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Create where clause from details of SMS object
        String whereClause = "Name = '" + sms.campaignName + "' AND Number = '" + sms.recipientsNumber + "' AND messageDate= '" + sms.messageDate + "' AND messageTime= '" + sms.messageTime + "' AND message = '" + sms.message + "'";

        // Returns the number of affected rows. 0 means no rows were deleted.
        Cursor result = db.rawQuery("SELECT SmsID FROM Messages WHERE " + whereClause, null);
        int retrievedID = 0;

        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            retrievedID = result.getInt(0);
        }
        return retrievedID;
    }

    public ArrayList<Sms> getPendingSmsList() {
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for all sms where messageStatus is pending
        Cursor result = db.rawQuery("SELECT * FROM ClientStatus WHERE messageStatus ='Pending' ORDER BY SmsID DESC", null);

        // Create list of sms objects
        ArrayList<Sms> sms = new ArrayList<Sms>();

        // For number of sms retrieved create a sms object with name, number, message, message date, message time.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            sms.add(new Sms(result.getString(1), result.getString(2), result.getString(3), result.getString(4),
                    result.getString(5), result.getString(6),result.getString(7)));
        }
        return sms;
    }*/
    /*public String getClientStatus(String clientName){
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for all sms where messageStatus is pending
        Cursor result = db.rawQuery("SELECT * FROM ClientStatus WHERE ClientName = "+clientName, null);
        return result.getString(0);
    }*/
    /* // insert data
    public void insertClienInfo(String name, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLIENT_NAME, name);
        values.put(CLIENT_STATUS, status);

        db.insert(TABLE_NAME, null, values);
        Log.d("TAG", "Insert success");
        db.close(); // Closing database connection
    }

    public String getClientStatus(String name) {
        String status = null;
        try {
            String selectQuery = "SELECT status FROM "+ TABLE_NAME +" WHERE client_name= '"+ name +"'" ;

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            cursor.moveToFirst();

            status = cursor.getString(0) ;
//            AppController.getAppController().getInAppNotifier().log("token", token);

        }

        catch (Exception ex) {
            Log.e("TAG", ex.toString());
        }

        return status;
    }

    public int getClientExtistence() {
        String status = null;
        try {
            String selectQuery = "SELECT COUNT(*) FROM "+ TABLE_NAME +" " ;

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            cursor.moveToFirst();

            status = cursor.getString(0) ;
//            AppController.getAppController().getInAppNotifier().log("token", token);

        }

        catch (Exception ex) {
            Log.e("TAG", ex.toString());
        }

        return Integer.valueOf(status);
    }

    public boolean updateClientStatus( String name, String status ) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        long l = db1.update(TABLE_NAME, contentValues, "client_name=?", new String[]{name});

        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }
*/


    public int addSms(Sms sms) {
        // Put Sms data into ContentValues object to insert into database
        ContentValues row = new ContentValues();
        row.put(this.COLUMN_NAMES[1], sms.campaignName);
        row.put(this.COLUMN_NAMES[2], sms.recipientsNumber);
        row.put(this.COLUMN_NAMES[3], sms.message);
        row.put(this.COLUMN_NAMES[4], sms.messageDate);
        row.put(this.COLUMN_NAMES[5], sms.messageTime);
        row.put(this.COLUMN_NAMES[6], sms.messageStatus);
        row.put(this.COLUMN_NAMES[7], sms.apiType);

        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // insert sms details into a new row, return the id of the new row.
        long longid = db.insert(SMS_TABLE_NAME, null, row);
        db.close();

        // convert id from long to int and return
        int SmsID = (int) longid;

        String[] numbers = sms.recipientsNumber.split(",");
        long currentTimeStamp = Calendar.getInstance().getTimeInMillis();

        for (int i=0 ;i<numbers.length;i++){
            //status 0 for pending msg,1 for assignTo a client,2 for complete
            //assignedClient 0 if no client is assigned                 //up
            //assigneddatetime 0 if no client is assigned                  //curr
            //completeDateTime 0 if is not sent yet
            //retry count mean number of clients process this sms
            //smsId foreign key ,Campaign number
            if(!numbers[i].isEmpty()){
                addRecipientSms(SmsID,numbers[i],sms.message,0,sms.apiType,currentTimeStamp,"0",
                        0,0,0);
            }
        }
        return SmsID;
    }

    class RecipientSms {
        public static final String SMSID = "SmsID";
        public static final String RECIPIENTSNUMBER = "RecipientsNumber";
        public static final String MESSAGE = "Message";
        public static final String MESSAGESTATUS = "MessageStatus";
        public static final String APITYPE = "APIType";
        public static final String ENTRYDATETIME = "entryDateTime";
        public static final String ASSIGNEDCLIENT = "assignedClient";
        public static final String ASSIGNDATETIME = "assignDateTime";
        public static final String COMPLETEDATETIME = "completeDateTime";
        public static final String RETRYCOUNT = "retryCount";
        public static final String RSNO = "rsno";

    }

    public int addRecipientSms(int smsId,String recipientsNumber,String message,int messageStatus,String apiType,long entryDateTime,
                               String assignedClient,long assignDateTime,long completeDateTime,int retryCount) {
        // Put Sms data into ContentValues object to insert into database
        ContentValues row = new ContentValues();
        row.put(RecipientSms.SMSID, smsId);
        row.put(RecipientSms.RECIPIENTSNUMBER, recipientsNumber);
        row.put(RecipientSms.MESSAGE, message);
        row.put(RecipientSms.MESSAGESTATUS, messageStatus);
        row.put(RecipientSms.APITYPE, apiType);
        row.put(RecipientSms.ENTRYDATETIME, entryDateTime);
        row.put(RecipientSms.ASSIGNEDCLIENT, assignedClient);
        row.put(RecipientSms.ASSIGNDATETIME, assignDateTime);
        row.put(RecipientSms.COMPLETEDATETIME, completeDateTime);
        row.put(RecipientSms.RETRYCOUNT, retryCount);

        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // insert sms details into a new row, return the id of the new row.
        long longid = db.insert(RECIPIENT_SMS_TABLE_NAME, null, row);
        //saruj
        if (longid > 0) {
            Uri recipent_uri = ContentUris.withAppendedId(RECIPENT_MSG_URI, longid);
            Log.e("r_uri", "recipent_uri: "+recipent_uri) ;
//            return task_uri;
        }
        db.close();


        /*String saveData = new CampaignDataManagement(mContext).getSharePrefData();
        new CampaignDataManagement(mContext).saveSharePrefData(smsId+"-->"+recipientsNumber+"-->"+message+"-->"+messageStatus+"-->"+apiType
                +"-->"+entryDateTime+"-->"+assignedClient+"-->"+assignDateTime+"-->"+completeDateTime+"-->"+retryCount+"\n\n\n"+saveData);*/

        // convert id from long to int and return
        int SmsID = (int) longid;
        return SmsID;
    }

    public ArrayList<RecipientSmsModel> getSmsTaskForClient(String clientId,int limit){
        // Get the readable database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Get sms by ID
        //Cursor result = db.rawQuery("SELECT * FROM RecipientMessages WHERE MessageStatus = 0 order by entryDateTime Limit "+limit, null);
        Cursor result = db.rawQuery("SELECT * FROM RecipientMessages WHERE MessageStatus = 0 and assignedClient = '"+clientId+"' order by entryDateTime Limit "+limit, null);


        // Create list of sms objects
        ArrayList<RecipientSmsModel> recipientSms = new ArrayList<RecipientSmsModel>();

        // For number of sms retrieved create a sms object with name, number, message, message date, message time.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            rsNo = result.getInt(result.getColumnIndex(RecipientSms.RSNO));
            recipientSms.add(new RecipientSmsModel(
                    result.getInt(result.getColumnIndex(RecipientSms.RSNO)),
                    result.getInt(result.getColumnIndex(RecipientSms.SMSID)),
                    result.getString(result.getColumnIndex(RecipientSms.RECIPIENTSNUMBER)),
                    result.getString(result.getColumnIndex(RecipientSms.MESSAGE)),
                    result.getInt(result.getColumnIndex(RecipientSms.MESSAGESTATUS)),
                    result.getString(result.getColumnIndex(RecipientSms.APITYPE)),
                    result.getLong(result.getColumnIndex(RecipientSms.ENTRYDATETIME)),
                    result.getString(result.getColumnIndex(RecipientSms.ASSIGNEDCLIENT)),
                    result.getLong(result.getColumnIndex(RecipientSms.ASSIGNDATETIME)),
                    result.getLong(result.getColumnIndex(RecipientSms.COMPLETEDATETIME)),
                    result.getInt(result.getColumnIndex(RecipientSms.RETRYCOUNT))
            ));
        }

        SQLiteDatabase dbW = this.getWritableDatabase();

        long currentTimeStamp = Calendar.getInstance().getTimeInMillis();
        // Put Sms ID into ContentValues object update SMS
        ContentValues cv = new ContentValues();
        cv.put(RecipientSms.MESSAGESTATUS, 1);
        cv.put(RecipientSms.COMPLETEDATETIME, currentTimeStamp);

        dbW.update(RECIPIENT_SMS_TABLE_NAME, cv, RecipientSms.RSNO + " = ? ", new String[]{String.valueOf(rsNo)});

        return recipientSms;
    }

    public void updateAssignedStatusOfClient(String clientId){
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        long currentTimeStamp = Calendar.getInstance().getTimeInMillis();
        // Put Sms ID into ContentValues object update SMS
        ContentValues cv = new ContentValues();
        cv.put(RecipientSms.ASSIGNEDCLIENT, clientId);
        cv.put(RecipientSms.ASSIGNDATETIME, currentTimeStamp);

        // Update messageStatus to Failed where SMSID is equal to SMS ID passed
        //db.update(RECIPIENT_SMS_TABLE_NAME, cv, "assignedClient=?"+new Integer[]{0}, null);

       /* db.update(RECIPIENT_SMS_TABLE_NAME, cv, RecipientSms.ASSIGNEDCLIENT + " = ? " ,
                new String[]{String.valueOf(0)});*/

       /* db.update(RECIPIENT_SMS_TABLE_NAME, cv, RecipientSms.RSNO + " in (SELECT rsno from "+RECIPIENT_SMS_TABLE_NAME
                        +" where "+RecipientSms.ASSIGNEDCLIENT+" = ? Limit 1)" , new String[]{String.valueOf(0)}); */


        db.update(RECIPIENT_SMS_TABLE_NAME, cv, RecipientSms.RSNO + " in (SELECT rsno from "+RECIPIENT_SMS_TABLE_NAME
                        +" where "+RecipientSms.ASSIGNEDCLIENT+" = ? and "+RecipientSms.MESSAGESTATUS+" = 0 "+"Limit 1)" , new String[]{String.valueOf(0)});

    }
    //TODO:: saruj Update
    public int updateAssignedTable(String name, String select){
        int num = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        long currentTimeStamp = Calendar.getInstance().getTimeInMillis();
        ContentValues cv = new ContentValues();
        switch (select){
            case "client":
                cv.put(RecipientSms.ASSIGNEDCLIENT, name);
                cv.put(RecipientSms.ASSIGNDATETIME, currentTimeStamp);

                num = db.update(RECIPIENT_SMS_TABLE_NAME, cv, RecipientSms.RSNO + " in (SELECT rsno from "+RECIPIENT_SMS_TABLE_NAME
                        +" where "+RecipientSms.ASSIGNEDCLIENT+" = ? and "+RecipientSms.MESSAGESTATUS+" = 0 "+"Limit 1)" , new String[]{String.valueOf(0)});
                break;
            case "rsNo":
                cv.put(RecipientSms.MESSAGESTATUS, 1);
                cv.put(RecipientSms.COMPLETEDATETIME, currentTimeStamp);

                num = db.update(RECIPIENT_SMS_TABLE_NAME, cv, RecipientSms.RSNO + " = ? ", new String[]{String.valueOf(name)});
                break;
            default:
                break;
        }




        return num ;
    }

    class ClientAppInfo {
        public static final String CSLNO = "cslno";
        public static final String FILENAME = "fileName";
        public static final String CREATEDATETIME = "createDateTime";
        public static final String DOWNLOADSTARTDATETIME = "downloadStartDateTime";
        public static final String COMPLETEDATETIME = "completeDateTime";

    }

    public void addClientAppInfo(String fileName,long createDateTime,long downloadStartDateTime,long completeDateTime) {
        // Put Sms data into ContentValues object to insert into database
        ContentValues row = new ContentValues();
        row.put(ClientAppInfo.FILENAME, fileName);
        row.put(ClientAppInfo.CREATEDATETIME, createDateTime);
        row.put(ClientAppInfo.DOWNLOADSTARTDATETIME, downloadStartDateTime);
        row.put(ClientAppInfo.COMPLETEDATETIME, completeDateTime);

        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // insert sms details into a new row, return the id of the new row.
        db.insert(CLIENT_APP_INFO_TABLE_NAME, null, row);
        db.close();
    }

    public int getNumberOfClients(){
        int totalClients = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM ClientAppInfos", null);
        totalClients = result.getCount();
        return totalClients;
    }

    public int getDownloadStartDateTime(int cslNo){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM ClientAppInfos WHERE cslno = "+String.valueOf(cslNo), null);
        result.moveToFirst();
        int download_start_date_time = result.getInt(result.getColumnIndex(ClientAppInfo.DOWNLOADSTARTDATETIME));
        return download_start_date_time;
    }

    public int getCompleteDateTime(int cslNo){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM ClientAppInfos WHERE cslno = "+String.valueOf(cslNo), null);
        result.moveToFirst();
        int complete_date_time = result.getInt(result.getColumnIndex(ClientAppInfo.COMPLETEDATETIME));
        return complete_date_time;
    }

    public String getFileName(int cslNo){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM ClientAppInfos WHERE cslno = "+String.valueOf(cslNo), null);
        result.moveToFirst();
        return result.getString(result.getColumnIndex(ClientAppInfo.FILENAME));
    }

    public void updateDownloadStartDateTime(int cslNo,long currentTimeStamp){
        SQLiteDatabase dbW = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ClientAppInfo.DOWNLOADSTARTDATETIME,currentTimeStamp);
        dbW.update(CLIENT_APP_INFO_TABLE_NAME, cv, ClientAppInfo.CSLNO + " = ? ", new String[]{String.valueOf(cslNo)});
    }

    public void updateCompleteDateTime(int cslNo,long currentTimeStamp){
        SQLiteDatabase dbW = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ClientAppInfo.COMPLETEDATETIME,currentTimeStamp);
        dbW.update(CLIENT_APP_INFO_TABLE_NAME, cv, ClientAppInfo.CSLNO + " = ? ", new String[]{String.valueOf(cslNo)});
    }


   /* public String getRecipientSmsInfo() {
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for all sms where messageStatus is pending
        Cursor result = db.rawQuery("SELECT * FROM RecipientMessages ORDER BY rsno DESC", null);

        // Create list of sms objects
        ArrayList<Sms> sms = new ArrayList<Sms>();

        // For number of sms retrieved create a sms object with name, number, message, message date, message time.
        String info = "";
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            info = info + result.getString(0)+"-->"+result.getString(1)+"-->"+result.getString(2)+"-->"+result.getString(3)+"-->"+result.getString(4)
                    +"-->"+result.getString(5)+"-->"+result.getString(6)+"-->"+result.getString(7)+"-->"+result.getString(8)+"-->"+
                    result.getString(9)+"-->"+result.getString(10)+"\n\n\n";

        }
        return info;
    }*/


}



