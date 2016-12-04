package com.blablaarthur.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Артур on 10.11.2016.
 */

public class DBAdapter {
    Context c;
    SQLiteDatabase db;
    DBHelper helper;

    public DBAdapter(Context context){
        c = context;
        helper = new DBHelper(context);
    }

    //OPEN DB
    public void openDB(){
        try{
            db = helper.getWritableDatabase();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    //CLOSE DB
    public void closeDB(){
        try{
            helper.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    //INSERT
    public boolean insert(String title, String desc, int imp, String datetime, String image){
        try {
            ContentValues cv = new ContentValues();
            cv.put(Columns.TITLE,title);
            cv.put(Columns.DESC,desc);
            cv.put(Columns.IMP,imp);
            cv.put(Columns.DATETIME,datetime);
            cv.put(Columns.IMAGE,image);
            db.insert(Database.TABLE_NAME, Columns.ID, cv);
            return true;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    //UPDATE
    public boolean update(int id, String title, String desc, int imp, String datetime, String image){
        try {
            ContentValues cv = new ContentValues();
            cv.put(Columns.TITLE,title);
            cv.put(Columns.DESC,desc);
            cv.put(Columns.IMP,imp);
            cv.put(Columns.DATETIME,datetime);
            cv.put(Columns.IMAGE,image);
            db.update(Database.TABLE_NAME, cv, Columns.ID + " = ?", new String[]{String.valueOf(id)});
            return true;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    //RETRIEVE DATA AND FILTER
    public Cursor retrieve(String searchWord, int filter){
        String[] columns = {Columns.ID, Columns.TITLE, Columns.DESC, Columns.IMP, Columns.DATETIME, Columns.IMAGE};
        Cursor curs = null;
        String where = null;
        String [] whereparams = null;
        if(searchWord != null && searchWord.length() > 0){
            where = "(" + Columns.TITLE + " LIKE ? OR " + Columns.DESC + " LIKE ?" + ")";
            whereparams = new String[]{"%" + searchWord + "%", "%" +searchWord + "%"};
        }
        if(filter != 3){
            String f = "(" + Columns.IMP + " = " + String.valueOf(filter) + ")";
            if(where == null)
                where = f;
            else
                where += " and " + f;
        }
        curs = db.query(Database.TABLE_NAME, columns, where, whereparams, null, null, null );
        return curs;
    }

    public Cursor getNoteById(int id){
        String[] columns = {Columns.ID, Columns.TITLE, Columns.DESC, Columns.IMP, Columns.DATETIME, Columns.IMAGE};
        Cursor curs = null;
        curs = db.query(Database.TABLE_NAME, columns, Columns.ID + " = " + String.valueOf(id), null, null, null, null );
        return curs;
    }

    //DELETE
    public boolean delete(int id){
        try {
            int res = db.delete(Database.TABLE_NAME, Columns.ID + " = ?", new String[]{String.valueOf(id)});
            if(res > 0)
                return true;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Cursor filterByImp(int imp){
        String[] columns = {Columns.ID, Columns.TITLE, Columns.DESC, Columns.IMP, Columns.DATETIME, Columns.IMAGE};
        Cursor curs = null;
        curs = db.query(Database.TABLE_NAME, columns, Columns.IMP + " = " + String.valueOf(imp), null, null, null, null );
        return curs;
    }

    public Cursor getNotification(String date){
        String[] columns = {Columns.ID, Columns.TITLE, Columns.DESC, Columns.IMP, Columns.DATETIME, Columns.IMAGE};
        Cursor curs = null;
        String where = Columns.DATETIME + " LIKE ? ";
        String[] whereparams = new String[]{"%" + date + "%"};
        curs = db.query(Database.TABLE_NAME, columns, where, whereparams, null, null, Columns.DATETIME );
        return curs;
    }
}
