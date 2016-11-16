package com.blablaarthur.lab2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import static java.security.AccessController.getContext;

/**
 * Created by Артур on 16.11.2016.
 */

public class MyNotesProvider extends ContentProvider{

    private static final String AUTH = "com.blablaarthur.lab2.NotesProvider";
    public static final Uri NOTES_URI = Uri.parse("content://" + AUTH + "/" + Database.TABLE_NAME);

    final static int NOTE = 1;

    SQLiteDatabase db;
    DBHelper dbHelper;

    private final static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTH, Database.TABLE_NAME, NOTE);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        if(uriMatcher.match(uri) == NOTE) {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(Database.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        if(uriMatcher.match(uri) == NOTE) {
            return "Notes";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = dbHelper.getWritableDatabase();
        if(uriMatcher.match(uri) == NOTE){
            db.insert(Database.TABLE_NAME, null, values);
        }
        db.close();
        getContext().getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int res = 0;
        if(uriMatcher.match(uri) == NOTE) {
            db = dbHelper.getWritableDatabase();
            res = db.delete(Database.TABLE_NAME, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return res;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int res = 0;
        if(uriMatcher.match(uri) == NOTE) {
            db = dbHelper.getWritableDatabase();
            res = db.update(Database.TABLE_NAME, values, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return res;
    }
}