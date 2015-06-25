package com.iapp.playdate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_Helper extends SQLiteOpenHelper {

	/*private static Database_Handler   objLogger;*/
	private static final String TABLE_EVENT="event";
	private static final String KEY_ID="_id";
	private static final String KEY_EVENT_ID="event_id";
	
	 //
	 private static final int DATABASE_VERSION = 1;

		// Database Name
		private static final String DATABASE_NAME = "eventdatabase";
	 
	 
	public DB_Helper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	/*public Database_Handler() {
		// TODO Auto-generated constructor stub
	}
*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_EVENT + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_EVENT_ID + " TEXT " + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);

		// Create tables again
		onCreate(db);
	}

	public void insert_into_table(String eventid ){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(KEY_EVENT_ID, eventid);
		System.out.println("insertion..........");
		db.insert(TABLE_EVENT, null, values);
		
	}
	public int getevent_id(String eventid){
		
		String selectQuery = "SELECT * FROM event WHERE "+ KEY_EVENT_ID + " = '" + eventid +"'";

		System.out.println(selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			/*do {
			
			} while (cursor.moveToNext());*/
			System.out.println("one row returned");
			return 1;
		}else{
System.out.println("returned 0 ..............");
		return 0;
		}
	}
	/*public ArrayList<Getcategory> get_chat(String sender_id, String receiver_id){
		ArrayList<Getcategory>messages=new ArrayList<Getcategory>();
		String selectQuery = "SELECT * FROM chat WHERE "+ KEY_SENDER_ID + " = '" + sender_id + "' AND " + KEY_RECEIVER_ID + " = '" + receiver_id + "' OR "+ KEY_SENDER_ID + " = '" + receiver_id + "' AND " + KEY_RECEIVER_ID + " = '" + sender_id +"'";

		System.out.println(selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Getcategory getcat=new Getcategory();
				getcat.sender_id=cursor.getString(1);
				getcat.message=cursor.getString(3);
				System.out.println("get value sender_id"+cursor.getString(1));
				System.out.println("get value msg"+cursor.getString(3));
				messages.add(getcat);
			} while (cursor.moveToNext());
		}

		return messages;
	}
	*/
	/*public void update_chat(ArrayList<Getcategory>messages){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values=new ContentValues();
		for(Getcategory getcat : messages){
			
			String senderid=getcat.sender_id;
			String receiverid=getcat.receiver_id;
			String message=getcat.message;
			System.out.println(senderid);
			System.out.println(receiverid);
			System.out.println(message);
			
			values.put(KEY_SENDER_ID, senderid);
			values.put(KEY_RECEIVER_ID, receiverid);
			values.put(KEY_MESSAGE, message);
			
			db.insert(TABLE_CHAT, null, values);
			System.out.println("after insertion...............");
			
		}
		db.close();
	}*/
}
