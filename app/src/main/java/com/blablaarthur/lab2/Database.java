package com.blablaarthur.lab2;

/**
 * Created by Артур on 10.11.2016.
 */

public class Database {
    static final String DB_NAME = "NotesDB";
    static final String TABLE_NAME = "Notes";
    static final int VERSION = 1;

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Columns.TITLE + " TEXT NOT NULL, "
                + Columns.DESC + " TEXT NOT NULL, "
                + Columns.IMP + " INTEGER NOT NULL, "
                + Columns.DATETIME + " TEXT NOT NULL, "
                + Columns.IMAGE + " TEXT NOT NULL"
                + " )";

    static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
