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
    static boolean running = false;

    @Override
    public void onCreate(){
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    public int onStartCommand(Intent intent, int flags, int startId){
        running = true;
        db = new DBAdapter(this);
        Log.d("A_R_T", "START NOT");
        checkNotification();
        return START_STICKY;
    }

    void sendNotif(int noteid, String title) {
        try {

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
            //n.flags |= Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;

            nm.notify(noteid, n);
        }
        catch (Exception e){
            Log.d("A_R_T", e.toString());
        }
    }

    void checkNotification(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Note topnote = null;
                while (true){
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        Log.d("A_R_T", "NOT TICK");
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
                        Log.d("A_R_T", String.valueOf(n.Id) + " " + n.DateTime);
//                        now = Calendar.getInstance();
//                        String time = n.DateTime.split(" ")[0];
//                        //if(CalendarConverter.compareTime(time, String.valueOf(now.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(now.get(Calendar.MINUTE))) == 1){
//                        //    topnote = n;
//                        if (CalendarConverter.compareTime(time, String.valueOf(now.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(now.get(Calendar.MINUTE))) == 0) {
//                            Log.d("A_R_T", String.valueOf(n.Id));
//                            try {
//                                sendNotif(n.Id, n.Title);
//                            } catch (Exception e) {
//                                Log.d("A_R_T", e.toString());
//                            }
                        //}
                        //    break;
                        //}
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
