package com.blablaarthur.lab2;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by Артур on 26.10.2016.
 */

public class CalendarConverter {
    public static String getTime(Calendar calendar){
        String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        if(hour.length() == 1)
            hour = "0" + hour;
        String minute = Integer.toString(calendar.get(Calendar.MINUTE));
        if(minute.length() == 1)
            minute = "0" + minute;

        return hour + ":" + minute;
    }

    public static String getDate(Calendar calendar){
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        if(day.length() == 1)
            day = "0" + day;
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        if(month.length() == 1)
            month = "0" + month;
        return day + "." + month + "."
                + calendar.get(Calendar.YEAR);
    }

    public static long getCalendar(String datetime){
        Calendar c = Calendar.getInstance();
        String [] td = datetime.split(" ");
        String year = td[1].split("\\.")[2];
        String month = td[1].split("\\.")[1];
        if(month.charAt(0) == '0')
            month = month.substring(1);
        String day = td[1].split("\\.")[0];
        if(day.charAt(0) == '0')
            day = day.substring(1);

        String hour = td[0].split(":")[1];
        if(hour.charAt(0) == '0')
            hour = hour.substring(1);
        String minute = td[0].split(":")[0];
        if(minute.charAt(0) == '0')
            minute = minute.substring(1);
        Log.d("A_R_T", year + " " + month + " " + day + " " + hour + " " + minute);
        c.set(Integer.parseInt(year), Integer.parseInt(month) - 1 ,Integer.parseInt(day),Integer.parseInt(hour),Integer.parseInt(minute));
        return c.getTimeInMillis();
    }

    public static int compareTime(String time1, String time2){
        int hour1 = 0;
        if(time1.split(":")[0].charAt(0) == '0')
            hour1 = Integer.parseInt(time1.split(":")[0].substring(1));
        else
            hour1 = Integer.parseInt(time1.split(":")[0]);

        int minute1 = 0;
        if(time1.split(":")[1].split(":")[0].charAt(0) == '0')
            minute1 = Integer.parseInt(time1.split(":")[1].substring(1));
        else
            minute1 = Integer.parseInt(time1.split(":")[1]);

        int hour2 = 0;
        if(time2.split(":")[0].charAt(0) == '0')
            hour2 = Integer.parseInt(time2.split(":")[0].substring(1));
        else
            hour2 = Integer.parseInt(time2.split(":")[0]);

        int minute2 = 0;
        if(time2.split(":")[1].split(":")[0].charAt(0) == '0')
            minute2 = Integer.parseInt(time2.split(":")[1].substring(1));
        else
            minute2 = Integer.parseInt(time2.split(":")[1]);

        //Log.d("A_R_T", String.valueOf(hour1) + " " + String.valueOf(minute1));
        //Log.d("A_R_T", String.valueOf(hour2) + " " + String.valueOf(minute2));

        if(hour1 > hour2)
            return 1;
        else if(hour1 == hour2 && minute1 > minute2)
            return 1;
        else if(hour1 == hour2 && minute1 == minute2)
            return 0;
        else
            return -1;
    }

}
