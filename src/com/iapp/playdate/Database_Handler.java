package com.iapp.playdate;

import java.util.ArrayList;

import com.iapptechnologies.time.models.NotificationModels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database_Handler extends SQLiteOpenHelper {

	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "notificationdatabase";
	
	
	// For Notification table
	private static final String TABLE_NOTIFICATION = "notification";
	private static final String KEY_MESSAGE_NOTIFICATION = "message";
	private static final String KEY_ID = "_id";
	
	
	
	public Database_Handler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + TABLE_NOTIFICATION + "("+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_MESSAGE_NOTIFICATION +  " TEXT" + ")";
		
		
		db.execSQL(CREATE_NOTIFICATION_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);

		// Create tables again
		onCreate(db);
	}
	
	
	

	/*
	 * public static Database_Handler getInstance() { if (objLogger == null) {
	 * objLogger = new Database_Handler(); } return objLogger; }
	 */

	
	
	
	public void insert_notification(String message_notification) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		/*Cursor i = db.rawQuery("SELECT COUNT(*) FROM notification WHERE read = '1' ",null);
		i.moveToFirst();
		int count = i.getInt(0);
		i.close();
		System.out.println("count...." + count);*/
		
		/*if(count>20){
			int limit=count-19;
			String delete_query="DELETE FROM notification WHERE _id LIMIT 1,"+limit;
			i=db.rawQuery(delete_query, null);
			int length=i.getCount();
Log.d("cursor count while deleting", ""+length);
}*/
		
		ContentValues values = new ContentValues();
		values.put(KEY_MESSAGE_NOTIFICATION, message_notification);
		
		db.insert(TABLE_NOTIFICATION, null, values);
		
		
		
		
		
		db.close();
		
	}
	
	public ArrayList<NotificationModels> get_notification() {
		
SQLiteDatabase db = this.getReadableDatabase();
		
		
		
		
		ArrayList<NotificationModels> Notifications = new ArrayList<NotificationModels>();
		// limit_offset_to=
		// limit_offset_from
		String selectQuery = "SELECT * FROM notification ORDER BY _id DESC";

		//SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				
				NotificationModels nn=new NotificationModels();
				String aa=cursor.getString(cursor
						.getColumnIndex(KEY_MESSAGE_NOTIFICATION));
				
				nn.setNotificationData(aa);
				
				
			
				
				
				Notifications.add(nn);
			} while (cursor.moveToNext());
		}
		
		return Notifications;
	}
	
	

		
	
}
