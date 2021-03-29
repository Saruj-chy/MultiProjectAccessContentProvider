package com.example.clientproject3;

import android.content.Context;
import android.os.PowerManager;

public class WakeLocker {
    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context ctx) {
        //if (wakeLock != null) wakeLock.release();
//        Log.e("TAG", "WAKE_LOCK_TAG") ;

        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "myapp:WAKE_LOCK_TAG");
        wakeLock.acquire();
    }

    public static void release() {
//        Log.e("TAG", "Release") ;

        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
}
