package com.blablaarthur.lab2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Артур on 03.12.2016.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, NotificationService.class);
        Log.d("A_R_T", "START BROAD");
        context.startService(serviceIntent);
    }
}
