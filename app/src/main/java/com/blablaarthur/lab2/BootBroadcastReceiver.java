package com.blablaarthur.lab2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Артур on 03.12.2016.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, NotificationService.class);
        Toast.makeText(context, "Notification Boot Completed", Toast.LENGTH_LONG).show();
        context.startService(serviceIntent);
    }
}
