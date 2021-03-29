package com.example.clientproject1.services;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

public class WakeLocker {
    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context ctx) {
        //if (wakeLock != null) wakeLock.release();
//        Log.e("TAG", "WAKE_LOCK_TAG") ;

//        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
//        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "myapp:WAKE_LOCK_TAG");

//        wakeLock.acquire();
    }

    public static void release() {
//        Log.e("TAG", "Release") ;

        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
}
