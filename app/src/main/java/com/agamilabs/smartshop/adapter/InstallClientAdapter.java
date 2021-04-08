package com.agamilabs.smartshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.activity.InstallAllClient;
import com.agamilabs.smartshop.model.AllClientAppInfo;

import java.io.File;
import java.util.ArrayList;

public class InstallClientAdapter extends RecyclerView.Adapter<InstallClientAdapter.InstallClientViewHolder> {
    Context mContext;
    ArrayList<AllClientAppInfo> allClientsAppInfos;

    String[] packageNameOfAllApps = { "com.agamilabs.maa",
            "com.kwanovations.ColorBlind", "com.agamilabs.cuadmissionnotice",
            "com.backstage.backstagefan","com.Serkode.RingBall"};

    String[] apkDownloadLinkOfApps = {"https://www.apkfollow.com/download/apks_new_com.agamilabs.maa_2018-05-14.apk/",
            "https://www.apkfollow.com/download/apks_com.kwanovations.ColorBlind_2013-06-19.apk/",
            "https://www.apkfollow.com/download/apks_new_com.agamilabs.cuadmissionnotice_2019-10-24.apk/",
            "https://www.apkfollow.com/download/arb_new_com.backstage.backstagefan_2018-11-14.apk/",
            "https://www.apkfollow.com/download/arb_new_com.Serkode.RingBall_2019-01-10.apk/"};

    public InstallClientAdapter(Context mContext, ArrayList<AllClientAppInfo> allClientsAppInfos) {
        this.mContext = mContext;
        this.allClientsAppInfos = allClientsAppInfos;
    }

    @NonNull
    @Override
    public InstallClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_client_info, parent, false);
        return new InstallClientAdapter.InstallClientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InstallClientViewHolder holder, int position) {
        AllClientAppInfo allClientAppInfo = allClientsAppInfos.get(position);
        holder.txt_app_name.setText(allClientAppInfo.getClientAppName());
        holder.btn_action_for_app.setText(allClientAppInfo.getClientAppStatus());

        if(allClientAppInfo.getClientAppStatus().equalsIgnoreCase("Download")){
            holder.txt_app_status.setText("Not downloaded");
        }
        else if(allClientAppInfo.getClientAppStatus().equalsIgnoreCase("Install")){
            holder.txt_app_status.setText("Not installed");
        }
        else if(allClientAppInfo.getClientAppStatus().equalsIgnoreCase("Open")){
            holder.txt_app_status.setText("Installed");
        }

        holder.btn_action_for_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btn_action_for_app.getText().equals("Open")){
                    InstallAllClient.getActivityInstance().openApp(packageNameOfAllApps[position]);
                }
                else if(holder.btn_action_for_app.getText().equals("Install")){
                    InstallAllClient.getActivityInstance().openAppForInstallation(position);
                }
                else if(holder.btn_action_for_app.getText().equals("Download")) {
                    InstallAllClient.getActivityInstance().downloadUpdate(apkDownloadLinkOfApps[position],position);
                    allClientAppInfo.setClientAppStatus("Downloading...");
                    notifyDataSetChanged();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return allClientsAppInfos.size();
    }

    public class InstallClientViewHolder extends RecyclerView.ViewHolder {
        TextView txt_app_name,txt_app_status;
        Button btn_action_for_app;
        public InstallClientViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_app_name = itemView.findViewById(R.id.txt_app_name);
            txt_app_status = itemView.findViewById(R.id.txt_app_status);
            btn_action_for_app = itemView.findViewById(R.id.btn_action_for_app);
        }
    }
}
