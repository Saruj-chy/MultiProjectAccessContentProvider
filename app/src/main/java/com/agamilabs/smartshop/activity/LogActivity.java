package com.agamilabs.smartshop.activity;
import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.agamilabs.smartshop.BuildConfig;
import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.controller.AppController;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LogActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 200;
    String[] apkFilesOfAgamiLabs = {"com.agamilabs.bax","com.agamilabs.maa","com.agamilabs.cuap",
            "com.agamilabs.colorblind", "com.agamilabs.cuadmissionnotice"};
    String[] apkDownloadLink = {"https://www.apkfollow.com/download/apks_new_com.agamilabs.maa_2018-05-14.apk/",
            "https://www.apkfollow.com/download/apks_new_com.miniclip.eightballpool_2021-02-26.apk/",
            "https://www.apkfollow.com/download/apks_com.kwanovations.ColorBlind_2013-06-19.apk/",
            "https://www.apkfollow.com/download/apks_new_com.agamilabs.cuadmissionnotice_2019-10-24.apk/"};


    private Button btn_maa,btn_bax,btn_cuAntiproxy,btn_color_blind,btn_mail_runner;
    int i=0,sum=0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        //Download pdf/apk using download manager,Ok
        /*if(LogActivity.readAndWriteExternalStorage(this)) {
            DownloadManager downloadmanager = (DownloadManager)this.getSystemService(this.DOWNLOAD_SERVICE);
            //Uri uri = Uri.parse("https://www.tutorialspoint.com/software_engineering/software_engineering_tutorial.pdf");
            Uri uri = Uri.parse("https://www.apkfollow.com/download/apks_new_com.agamilabs.maa_2018-05-14.apk/");
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"/MaaAgamiLabs");
            downloadmanager.enqueue(request);
        }*/

        //install App from Google Play link of that App,Ok
        /*Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.agamilabs.maa"));
        startActivity(installIntent);*/

        //downloadAppAndInstallSimultaneously();

        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(new File(Environment.DIRECTORY_DOWNLOADS + "/MaaAgamiLabs").getPath()), "application/vnd.android.package-archive");
        startActivity(intent);*/

        btn_maa = findViewById(R.id.btn_click_maa);
        btn_bax = findViewById(R.id.btn_click_bax);
        btn_color_blind = findViewById(R.id.btn_click_color_blind);
        btn_mail_runner = findViewById(R.id.btn_click_color_mail_runner);
        btn_cuAntiproxy = findViewById(R.id.btn_click_cu);

        btn_maa.setOnClickListener(this);
        btn_bax.setOnClickListener(this);
        btn_color_blind.setOnClickListener(this);
        btn_mail_runner.setOnClickListener(this);
        btn_cuAntiproxy.setOnClickListener(this);

        AppController.getAppController().getInAppNotifier().log("ExternalStorageDic",Environment.getExternalStorageDirectory()+"");
        AppController.getAppController().getInAppNotifier().log("DirectoryDownload",Environment.DIRECTORY_DOWNLOADS+"");

    }

    public static void installApp(Context context, String appPackageName) {
        String playStoreMarketUrl = "market://details?id=";
        String playStoreWebUrl = "https://play.google.com/store/apps/details?id=";

        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreMarketUrl + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreWebUrl + appPackageName)));
        }
    }

    public static boolean isAppInstalled(Context context, String appPackageName) {
        try {
            context.getPackageManager().getApplicationInfo(appPackageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean readAndWriteExternalStorage(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        } else {
            return true;
        }
    }

    public void downloadUpdate(String url) {
        AppController.getAppController().getInAppNotifier().log("AppUrl",url);
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = "clientApp"+(i+1)+".apk";
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        File file = new File(destination);
        if (file.exists()){
            file.delete();
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationUri(uri);
        dm.enqueue(request);

        final String finalDestination = destination;
        final BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                AppController.getAppController().getInAppNotifier().log("finalDestination",finalDestination+""+sum);
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
    public void onClick(View view) {
        if(view == btn_maa){
            if(!isAppInstalled(LogActivity.this,"com.agamilabs.maa")){
                installApp(LogActivity.this,"com.agamilabs.maa");
            }
        }
        else if(view == btn_bax){
            if(!isAppInstalled(LogActivity.this,"com.agamilabs.bax")){
                installApp(LogActivity.this,"com.agamilabs.bax");
            }
        }
        else if(view == btn_color_blind){
            if(!isAppInstalled(LogActivity.this,"com.agamilabs.colorblind")){
                installApp(LogActivity.this,"com.agamilabs.colorblind");
            }
        }
        else if(view == btn_cuAntiproxy){
            if(!isAppInstalled(LogActivity.this,"com.agamilabs.cuap")){
                installApp(LogActivity.this,"com.agamilabs.cuap");
            }
        }
        else if(view == btn_mail_runner){
            if(!isAppInstalled(LogActivity.this,"com.MiddayDreamz.MailRunner")){
                installApp(LogActivity.this,"com.MiddayDreamz.MailRunner");
            }
        }
    }
}