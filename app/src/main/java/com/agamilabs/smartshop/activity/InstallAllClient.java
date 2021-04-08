package com.agamilabs.smartshop.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.agamilabs.smartshop.BuildConfig;
import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.adapter.InstallClientAdapter;
import com.agamilabs.smartshop.controller.AppController;
import com.agamilabs.smartshop.database.SmsDatabaseHelper;
import com.agamilabs.smartshop.model.AllClientAppInfo;
import com.google.android.datatransport.cct.internal.ClientInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class InstallAllClient extends AppCompatActivity {
    private RecyclerView recyclerViewAllClientInfo;
    private InstallClientAdapter installClientAdapter;
    private ArrayList<AllClientAppInfo> allClientAppInfoList;
    private AllClientAppInfo allClientAppInfo;
    private SmsDatabaseHelper smsDatabaseHelper;
    String[] packageNameOfAllApps = { "com.agamilabs.maa",
            "com.kwanovations.ColorBlind", "com.agamilabs.cuadmissionnotice",
            "com.backstage.backstagefan","com.Serkode.RingBall"};

    String[] apkDownloadLinkOfApps = {"https://www.apkfollow.com/download/apks_new_com.agamilabs.maa_2018-05-14.apk/",
            "https://www.apkfollow.com/download/apks_com.kwanovations.ColorBlind_2013-06-19.apk/",
            "https://www.apkfollow.com/download/apks_new_com.agamilabs.cuadmissionnotice_2019-10-24.apk/",
            "https://www.apkfollow.com/download/arb_new_com.backstage.backstagefan_2018-11-14.apk/",
            "https://www.apkfollow.com/download/arb_new_com.Serkode.RingBall_2019-01-10.apk/"};

    private static InstallAllClient INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_all_client);

        AppController.getAppController().getInAppNotifier().log("InstallAllClient","onCreateCalled");

        INSTANCE =  this;

        recyclerViewAllClientInfo = findViewById(R.id.recycler_view_clients);
        smsDatabaseHelper = new SmsDatabaseHelper(this);
        allClientAppInfoList = new ArrayList<>();
        allClientAppInfo = new AllClientAppInfo();

        installClientAdapter = new InstallClientAdapter(this,allClientAppInfoList);
        recyclerViewAllClientInfo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAllClientInfo.setHasFixedSize(true);
        recyclerViewAllClientInfo.setAdapter(installClientAdapter);

        if(smsDatabaseHelper.getNumberOfClients() == 0){
            for(int i=0 ; i<30 ; i++){
                long currentTimeStamp = Calendar.getInstance().getTimeInMillis();
                String fileName = "Client"+(i+1)+generateRandomString()+(currentTimeStamp/1000)+".apk";
                smsDatabaseHelper.addClientAppInfo(fileName,currentTimeStamp,0,0);
            }
        }
        else if(smsDatabaseHelper.getNumberOfClients() == 30){
            for(int i=0 ; i<30 ; i++){
                if(!isFileExist(i)){
                    smsDatabaseHelper.updateCompleteDateTime(i+1,0);
                    smsDatabaseHelper.updateDownloadStartDateTime(i+1,0);
                }
            }
        }

        /*Toast.makeText(this,""+smsDatabaseHelper.getCompleteDateTime(2)+" "+smsDatabaseHelper.getDownloadStartDateTime(2)+" "+
                smsDatabaseHelper.getFileName(2),Toast.LENGTH_LONG).show();*/

    }

    public String generateRandomString(){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";  // create a string of all characters
        StringBuilder sb = new StringBuilder();   // create random string builder
        Random random = new Random();   // create an object of Random class
        int length = 10; // specify length of random string

        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());  // generate random index number
            char randomChar = alphabet.charAt(index); // get character specified by index from the string
            sb.append(randomChar); // append the character to string builder
        }

        String randomString = sb.toString();
        return randomString;
    }

    public static InstallAllClient getActivityInstance()
    {
        return INSTANCE;
    }

    //Open app ,if app is already installed in your device
    public void openApp(String packageName) {
        if (isAppInstalled(InstallAllClient.this, packageName)){
            startActivity(getPackageManager().getLaunchIntentForPackage(packageName));
        }
        else Toast.makeText(InstallAllClient.this, "App not installed", Toast.LENGTH_SHORT).show();
    }


    //if Downloaded complete of this app but not install yet
    public void openAppForInstallation(int position){
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = smsDatabaseHelper.getFileName(position+1);
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);
        final String finalDestination = destination;

        /*final BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                    allClientAppInfoList.get(position).setClientAppStatus("Open");
                    installClientAdapter.notifyDataSetChanged();
                }
            }
        };*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(InstallAllClient.this,
                    BuildConfig.APPLICATION_ID + ".provider", new File(finalDestination));
            Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
            openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            openFileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            openFileIntent.setAction(Intent.ACTION_INSTALL_PACKAGE);
            openFileIntent.setData(contentUri);
            startActivity(openFileIntent);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_INSTALL_PACKAGE);
            install.setDataAndType(uri, "application/vnd.android.package-archive");
            startActivity(install);
        }

        /*IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme(packageNameOfAllApps[position]);
        registerReceiver(br, intentFilter);*/
    }

    //check whether app is installed or not
    public boolean isAppInstalled(Activity activity, String packageName) {
        PackageManager pm = activity.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    //check whether app is downloaded or not
    public boolean isFileExist(int position){
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = smsDatabaseHelper.getFileName(position+1);
        destination += fileName;

        File file = new File(destination);

        if (file.exists()){
            return true;
        }

        return false;
    }

    public void downloadUpdate(String url,int position) {
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = smsDatabaseHelper.getFileName(position+1);
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        File file = new File(destination);
        if (file.exists()){
            file.delete();
        }

        long currentTimeStamp = Calendar.getInstance().getTimeInMillis();
        smsDatabaseHelper.updateDownloadStartDateTime(position+1,currentTimeStamp);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Downloading...");
        request.setTitle(fileName);
        request.setDestinationUri(uri);
        dm.enqueue(request);

        final String finalDestination = destination;
        final BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long currentTimeStamp = Calendar.getInstance().getTimeInMillis();
                    allClientAppInfoList.get(position).setClientAppStatus("Install");
                    smsDatabaseHelper.updateCompleteDateTime(position+1,currentTimeStamp);
                    installClientAdapter.notifyDataSetChanged();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri contentUri = FileProvider.getUriForFile(ctxt, BuildConfig.APPLICATION_ID + ".provider", new File(finalDestination));
                    Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
                    openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    openFileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    openFileIntent.setData(contentUri);
                    startActivity(openFileIntent);
                    unregisterReceiver(this);
                    finish();

                } else {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    install.setDataAndType(uri, "application/vnd.android.package-archive");
                    startActivity(install);
                    unregisterReceiver(this);
                    finish();
                }
            }
        };

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onBackPressed() {
        AppController.getAppController().getInAppNotifier().log("InstallAllClient","onBackCalled");
        installClientAdapter.notifyDataSetChanged();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        AppController.getAppController().getInAppNotifier().log("InstallAllClient","onPauseCalled");
        installClientAdapter.notifyDataSetChanged();
        super.onPause();
    }

    @Override
    protected void onResume() {
        AppController.getAppController().getInAppNotifier().log("InstallAllClient","onResumeCalled");

        allClientAppInfoList.clear();
       /* for(int i=0 ; i<packageNameOfAllApps.length ; i++){

            if(isAppInstalled(InstallAllClient.this,packageNameOfAllApps[i])){
                allClientAppInfoList.add(new AllClientAppInfo("Client "+(i+1),"Open"));
            }
            else if(smsDatabaseHelper.getCompleteDateTime(i+1) != 0 ){
                allClientAppInfoList.add(new AllClientAppInfo("Client "+(i+1),"Install"));
            }
            else {
                if(smsDatabaseHelper.getDownloadStartDateTime(i+1) != 0 ){
                    allClientAppInfoList.add(new AllClientAppInfo("Client "+(i+1),"Downloading..."));
                }
                else {
                    allClientAppInfoList.add(new AllClientAppInfo("Client "+(i+1),"Download"));
                }
            }
        }*/

        for(int i=0 ; i<packageNameOfAllApps.length ; i++){
            int downloadStartDateTime = smsDatabaseHelper.getDownloadStartDateTime(i+1);
            int completeStartDateTime = smsDatabaseHelper.getCompleteDateTime(i+1);

            if(isAppInstalled(InstallAllClient.this,packageNameOfAllApps[i])){
                allClientAppInfoList.add(new AllClientAppInfo("Client "+(i+1),"Open"));
            }
            else if(downloadStartDateTime!=0 && completeStartDateTime!=0){
                allClientAppInfoList.add(new AllClientAppInfo("Client "+(i+1),"Install"));
            }
            else {
                if(smsDatabaseHelper.getDownloadStartDateTime(i+1) != 0 ){
                    allClientAppInfoList.add(new AllClientAppInfo("Client "+(i+1),"Downloading..."));
                }
                else {
                    allClientAppInfoList.add(new AllClientAppInfo("Client "+(i+1),"Download"));
                }
            }
        }

        installClientAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onStart() {
        AppController.getAppController().getInAppNotifier().log("InstallAllClient","onStartCalled");
       /* if(smsDatabaseHelper.getNumberOfClients() == 30) {
            for (int i = 0; i < 30; i++) {
                if (!isFileExist(i)) {
                    smsDatabaseHelper.updateCompleteDateTime(i+1, 0);
                    smsDatabaseHelper.updateDownloadStartDateTime(i+1, 0);
                }
            }
        }*/
        installClientAdapter.notifyDataSetChanged();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        AppController.getAppController().getInAppNotifier().log("InstallAllClient","onReStartCalled");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        AppController.getAppController().getInAppNotifier().log("InstallAllClient","onStopCalled");
        super.onStop();
    }
}