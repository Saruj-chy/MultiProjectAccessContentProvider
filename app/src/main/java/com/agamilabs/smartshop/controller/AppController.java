package com.agamilabs.smartshop.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import androidx.multidex.MultiDexApplication;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;

import com.agamilabs.smartshop.database.CampaignDataManagement;
import com.agamilabs.smartshop.database.SmsDatabaseHelper;
import com.agamilabs.smartshop.model.RecipientSmsModel;
import com.agamilabs.smartshop.services.AlarmReceiver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import SeparatePk.IRemoteServiceCallback;
import SeparatePk.aidlInterface;


/**
 * Created by sazzad on 6/27/2018.
 */

public class AppController extends MultiDexApplication {

    private static volatile AppController appController;
    private InAppNotifier inAppNotifier;
    private AppNetworkController appNetworkController;
    SmsDatabaseHelper smsDatabaseHelper;
    //private constructor.
    public AppController(){

        //Prevent form the reflection api.
        if (appController != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();

        appController = this;

        //FacebookSdk.sdkInitialize(getApplicationContext());
        smsDatabaseHelper = new SmsDatabaseHelper(getApplicationContext());
        bindToAIDLService();
        startRepeatingAlarm();
        getKeyHash();

    }

    private void startRepeatingAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("SmsID", 400);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        /*alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);*/

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    private void getKeyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.agamilabs.smartshop",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                AppController.getAppController().getInAppNotifier().log("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

//            AppController.getAppController().getInAppNotifier().log("KeyHash:", info.);
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            AppController.getAppController().getInAppNotifier().log("KeyHashException", e.toString());
        }
    }

    public static AppController getAppController(){
        if(appController==null){
            synchronized (AppController.class){
                if(appController==null) appController = new AppController();
            }
        }

        return appController;
    }

    public InAppNotifier getInAppNotifier(){
        if(inAppNotifier==null){
            inAppNotifier = InAppNotifier.getInstance(getApplicationContext());
        }

        return inAppNotifier;
    }

    public AppNetworkController getAppNetworkController() {
        if(appNetworkController==null){
            appNetworkController = AppNetworkController.getInstance(getApplicationContext());
        }

        return appNetworkController;
    }

    private final ArrayList<aidlInterface> aidlInterfaceArrayList = new ArrayList<>();
    private final ArrayList<String> componentNameList = new ArrayList<>();
    private final static int NUMBEROFCLIENTS = 1 ;
    //private aidlInterface aidlObject;
    ServiceConnection serviceConnectionObject = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            InAppNotifier.getInstance(getApplicationContext()).log("insideServiceConnected",componentName+"");
            componentNameList.add(componentName.getPackageName());
            aidlInterfaceArrayList.add(aidlInterface.Stub.asInterface(iBinder));
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            InAppNotifier.getInstance(getApplicationContext()).log("insideServiceDisConnected",componentName+"");
        }
    };

    public void bindToAIDLService() {
        for (int i=0;i<NUMBEROFCLIENTS;i++){
            Intent aidlServiceIntent = new Intent("connect_to_aidl_service"+i);
//            bindService(implicitIntentToExplicitIntent(aidlServiceIntent,this),serviceConnectionObject,BIND_AUTO_CREATE);
        }
    }

    public Intent implicitIntentToExplicitIntent(Intent implicitIntent, Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(implicitIntent, 0);

        if (resolveInfoList == null || resolveInfoList.size() != 1) {
            return null;
        }
        ResolveInfo serviceInfo = resolveInfoList.get(0);
        ComponentName component = new ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }


    public void callAllClients(){
        try {
            for(int i=0 ; i < aidlInterfaceArrayList.size() ; i++){
                InAppNotifier.getInstance(getApplicationContext()).log("componentPackageName"+i,componentNameList.get(i)+"");
                InAppNotifier.getInstance(getApplicationContext()).log("clientObjectReference"+i,aidlInterfaceArrayList.get(i)+"");
                aidlInterfaceArrayList.get(i).trigger(myOfficeNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    IRemoteServiceCallback myOfficeNumber = new IRemoteServiceCallback.Stub() {
        @Override
        public void feedBack(String clientId, String lastProcessTaskJSON, boolean isAvailable) throws RemoteException {
            InAppNotifier.getInstance(getApplicationContext()).log("InsideFeedBack",clientId);
            InAppNotifier.getInstance(getApplicationContext()).log("lastProcessTask",lastProcessTaskJSON);

            //update using lastProcessJson if lastProcessJson is not null

            //check is lastProcessTaskJson valid
            //update recipientMsgTable using RSNO from lastProcesstaskJson
            if(aidlInterfaceArrayList.size() == 0 || aidlInterfaceArrayList.get(0) == null){
                return;
            }
            //when feedBack comes
            int searchIndex = componentNameList.indexOf(clientId);
            smsDatabaseHelper.updateAssignedStatusOfClient(clientId);
            InAppNotifier.getInstance(getApplicationContext()).log("called","called");
            ArrayList<RecipientSmsModel> clientSmsTask = smsDatabaseHelper.getSmsTaskForClient(clientId,1);
            if(clientSmsTask.size() > 0){
                InAppNotifier.getInstance(getApplicationContext()).log("searchIndex",searchIndex+"");
                aidlInterfaceArrayList.get(searchIndex).sendSingleSms(clientSmsTask.get(0).getRSNO(),
                        clientSmsTask.get(0).getMESSAGE(),clientSmsTask.get(0).getRECIPIENTSNUMBER());

                /*String saveData = new CampaignDataManagement(getApplicationContext()).getSharePrefData();
                String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());*/
                /*new CampaignDataManagement(getApplicationContext()).saveSharePrefData("Sending Msg RSNO: "+clientSmsTask.get(0).getRSNO()
                +" Message: "+clientSmsTask.get(0).getMESSAGE()+" Number: "+clientSmsTask.get(0).getRECIPIENTSNUMBER()+" Msg Send at "+currentDateTimeString+"\n\n\n"+saveData);*/
            }

        }
    };


}