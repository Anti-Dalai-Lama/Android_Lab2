package com.blablaarthur.lab2;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Артур on 10.11.2016.
 */

public class DBHelper extends SQLiteOpenHelper{
    public DBHelper(Context context) {
        super(context, Database.DB_NAME, null, Database.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d("A_R_T", "onCreate DATABASE");
            db.execSQL(Database.CREATE_TABLE);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Database.DROP_TABLE);
        onCreate(db);
    }
}
