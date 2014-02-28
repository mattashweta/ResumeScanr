package com.hackday.resumescanr.database;

import com.hackday.resumescanr.database.ResumeReaderContract.ResumeEntry;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ResumeReaderDatabaseAPI {
	//Inserts data into the database and returns the new Row id
	public static long insertData(ResumeReaderDBHelper mDbHelper, String id, 
			String name, String email, String gpa, String branch,
			String univ)
	{
		// Gets the data repository in write mode
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(ResumeEntry.COLUMN_NAME_ENTRY_ID, id);
		values.put(ResumeEntry.COLUMN_NAME_STUDNAME, name);
		values.put(ResumeEntry.COLUMN_NAME_STUDEMAIL, email);
		values.put(ResumeEntry.COLUMN_NAME_STUDGPA, gpa);
		values.put(ResumeEntry.COLUMN_NAME_BRANCH, branch);
		values.put(ResumeEntry.COLUMN_NAME_UNIV, univ);
		
		// Insert the new row, returning the primary key value of the new row
		long newRowId=-1;
		newRowId = db.insert(
		         ResumeEntry.TABLE_NAME,
		         null,
		         values);
		
		return newRowId;
	}
	
	public static Cursor readData(ResumeReaderDBHelper mDbHelper, String[] projection,
			String selection, String selectionArgs[],String sortColumn)
	{
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor c = db.query(
		    ResumeEntry.TABLE_NAME,  // The table to query
		    projection,                               // The columns to return
		    selection,                                // The columns for the WHERE clause
		    selectionArgs,                            // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    sortColumn                               // The sort order
		    );
		
		return c;
	}
	
	//Update database 
	//Returns number of rows affected.
	public static int update(ResumeReaderDBHelper mDbHelper, String columnName,
			String newValue, int rowId)
	{
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// New value for one column
		ContentValues values = new ContentValues();
		values.put(columnName, newValue);

		// Which row to update, based on the ID
		String selection = ResumeEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(rowId) };

		int count = db.update(
		    ResumeEntry.TABLE_NAME,
		    values,
		    selection,
		    selectionArgs);
		
		return count;
	}
}
