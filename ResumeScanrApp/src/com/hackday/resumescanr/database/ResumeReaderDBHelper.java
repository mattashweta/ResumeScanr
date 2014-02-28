package com.hackday.resumescanr.database;

import com.hackday.resumescanr.database.ResumeReaderContract.ResumeEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ResumeReaderDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ResumeReader.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + ResumeEntry.TABLE_NAME + " (" +
        ResumeEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY," +
        ResumeEntry.COLUMN_NAME_STUDNAME + TEXT_TYPE + COMMA_SEP +
        ResumeEntry.COLUMN_NAME_STUDEMAIL + TEXT_TYPE + COMMA_SEP +
        ResumeEntry.COLUMN_NAME_STUDGPA + TEXT_TYPE + COMMA_SEP +
        ResumeEntry.COLUMN_NAME_BRANCH + TEXT_TYPE + COMMA_SEP +
        ResumeEntry.COLUMN_NAME_UNIV + TEXT_TYPE +  // Any other options for the CREATE command
        ")";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + ResumeEntry.TABLE_NAME;
    
    public ResumeReaderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
