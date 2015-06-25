package com.iapptechnologies.time.util;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseSqlliteHelper extends SQLiteOpenHelper 
{
	private static final String DATABASE_NAME = "bizproDB";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE = "events";

	public DataBaseSqlliteHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(DataBaseConstants.CREATE_TABLE_NOTIFICATION_MESSAGES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}
}
