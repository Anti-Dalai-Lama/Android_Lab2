package com.blablaarthur.lab2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Артур on 03.12.2016.
 */

public class NotificationService extends Service {

    NotificationManager nm;
    NotificationCompat.Builder notification;
    DBAdapter db;

    @Override
    public void onCreate(){
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    public int onStartCommand(Intent intent, int flags, int startId){
        db = new DBAdapter(this);
        checkNotification();
        return START_STICKY;
    }

    void sendNotif(int noteid, String title) {
        Intent intent = new Intent(getApplicationContext(), CreateNote.class);
        intent.setAction("android.intent.myaction.WATCH");
        intent.putExtra("Id", noteid);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        notification = new NotificationCompat.Builder(this)
                .setContentIntent(pIntent)
                .setSmallIcon(R.drawable.ic_action_name)
                .setContentTitle(title)
                .setContentText("Tap to watch")
                .setTicker(title)
                .setAutoCancel(true);

        Notification n = notification.build();
        nm.notify(noteid, n);
    }

    void checkNotification(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Note topnote = null;
                while (true){
                    try {
                        TimeUnit.SECONDS.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Calendar now = Calendar.getInstance();
                    String datetime = CalendarConverter.getTime(now) + " " + CalendarConverter.getDate(now);
                    db.openDB();
                    Cursor c = db.getNotification(datetime);
                    if(c.moveToNext()) {
                        Note n = new Note(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4), c.getString(5));
                        if(topnote == null || (!topnote.Id.equals(n.Id) || !topnote.DateTime.equals(n.DateTime))){
                            topnote = n;
                            sendNotif(topnote.Id, topnote.Title);
                        }
                    }
                    db.closeDB();

                }
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
