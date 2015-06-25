package com.iapp.playdate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iapp.playdate.R;
import com.iapptechnologies.time.util.Constants;


public class Calander_event extends android.support.v4.app.Fragment {

	public Calendar month;
	public CalendarAdapter adapter;
	public Handler handler;
	public ArrayList<String> items;
	public ArrayList<String> _date;
	public ArrayList<Getcatagory_forlist> params;
	public ArrayList<Getcatagory_forlist> params_systems_event;
	TextView title;
	String sResponse = null, user_guardian_id;
	ListView list_event;
	LazyAdapter adapter1;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	JSONObject json_object = null;
	String data_json;
	boolean check_event = false;
	DB_Helper db;
	ArrayList<Getcatagory_forlist> searchList;
	Boolean check_list_click=false;
	ProgressDialog dialog;// = new ProgressDialog(getActivity(),android.R.style.Theme_Black);
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.calendar,
				container, false);
		dialog = new ProgressDialog(getActivity(),android.R.style.Theme_Black);
		settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
		editor = settings.edit();
		
		//user_guardian_id = getArguments().getString("user_guardian_id");
		user_guardian_id=settings.getString("USERID","");
		
		list_event = (ListView) view.findViewById(R.id.listView_calander_event);
		month = Calendar.getInstance();
		onNewIntent();
		db = new DB_Helper(getActivity());
		items = new ArrayList<String>();
		_date = new ArrayList<String>();
		adapter = new CalendarAdapter(getActivity(), month);

		GridView gridview = (GridView) view.findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		title = (TextView) view.findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
		cd = new ConnectionDetector(getActivity());
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
			new Get_Event().execute();

		} else {
			Toast.makeText(getActivity(), "Please check internet connection",
					2000).show();

		}
		/*
		 * String uriString = "content://com.android.calendar//events";
		 * Log.i("INFO", "Reading content from " + uriString);
		 * readContent(uriString);
		 */

		TextView previous = (TextView) view.findViewById(R.id.previous);
		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (month.get(Calendar.MONTH) == month
						.getActualMinimum(Calendar.MONTH)) {
					month.set((month.get(Calendar.YEAR) - 1),
							month.getActualMaximum(Calendar.MONTH), 1);
				} else {
					month.set(Calendar.MONTH, month.get(Calendar.MONTH) - 1);
				}
				refreshCalendar();
			}
		});

		TextView next = (TextView) view.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (month.get(Calendar.MONTH) == month
						.getActualMaximum(Calendar.MONTH)) {
					month.set((month.get(Calendar.YEAR) + 1),
							month.getActualMinimum(Calendar.MONTH), 1);
				} else {
					month.set(Calendar.MONTH, month.get(Calendar.MONTH) + 1);
				}
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				check_list_click=true;
				TextView date = (TextView) v.findViewById(R.id.date);
				if (date instanceof TextView && !date.getText().equals("")) {
					// v.setBackgroundResource(R.drawable.item_background_focused);
					// Intent intent = new Intent();
					String day = date.getText().toString();
					if (day.length() == 1) {
						day = "0" + day;
					}
				String date_for_gridviewshown = android.text.format.DateFormat
							.format("yyyy-MM", month) + "-" + day;
				String system_event_gridviewdate = android.text.format.DateFormat
							.format("MM", month) + "-" + day;
				
					try {

						 searchList = new ArrayList<Getcatagory_forlist>();
						String search = "a";
						int searchListLength = params.size();
						for (int i = 0; i < searchListLength; i++) {

							String event_id = params.get(i).eventid;
							if (event_id.equals("-1")) {
								String date_for_gridview = params.get(i).date_ofevent;
								String[] dateArr = date_for_gridview.split("-"); // date
								String month_forcalander = dateArr[1];
								String date_forcalander = dateArr[2];
								String datecomparasion = month_forcalander
										+ "-" + date_forcalander;
								//System.out
								//		.println("gridviewdate for comparision"
									//			+ datecomparasion);
							//	System.out.println("compare to"
								//		+ system_event_gridviewdate);
								if (system_event_gridviewdate
										.equals(datecomparasion)) {

									Getcatagory_forlist getcat = params.get(i);
									searchList.add(getcat);
								}
							} else {

								if (params.get(i).date_ofevent
										.contains(date_for_gridviewshown)) {
									// Do whatever you want here
									Getcatagory_forlist getcat = new Getcatagory_forlist();
									String child_name = params.get(i).child_name;
								//	System.out
								//			.println("child_namechild_namechild_name"
												//	+ child_name);
									String Child_id = params.get(i).child_id;
									String Child_profilepic = params.get(i).child_profile_image;
									String Child_friend_name = params.get(i).childfriend_name;
									String Child_frient_id = params.get(i).friend_id;
									String Child_friend_pic = params.get(i).friendprofile_image;
									// String
									// Child_guardianId=_items.get(arg2).guardian_id;

									String event_date = params.get(i).date_ofevent;
									String event_start = params.get(i).start_time_of_event;
									String event_end_time = params.get(i).end_time_of_event;
									String notes = params.get(i).notes_event;
									String location_ofevent = params.get(i).location_event;
									String sender_id = params.get(i).senderid;
									// String
									String  receiver_id=params.get(i).receiverid;
									String status_of_event_ = params.get(i).status_ofevent;
									
									String child_dob=params.get(i).child_dob;
									//String child_hobbies=params.get(i).child_hobbies;
									String child_allergies=params.get(i).child_allergies;
									//String child_freetime=params.get(i).child_freetime;
									////String child_school=params.get(i).child_school;
									//String child_youthclub=params.get(i).child_youthclub;
									String child_friends_dob=params.get(i).child_friends_dob;
									String child_friends_allergies=params.get(i).child_friends_allergies;
									//String child_friends_hobbies=params.get(i).child_friends_hobbies;
									//String child_friends_freetime=params.get(i).child_friends_freetime;
									//String child_friends_school=params.get(i).child_friends_school;
									//String child_friends_youthclub=params.get(i).child_friends_youthclub;
									

									getcat.child_id = Child_id;
									getcat.child_name = child_name;
									getcat.child_profile_image = Child_profilepic;
									getcat.childfriend_name = Child_friend_name;
									getcat.date_ofevent = event_date;
									getcat.end_time_of_event = event_end_time;
									getcat.friend_id = Child_frient_id;
									getcat.friendprofile_image = Child_friend_pic;
									// getcat.guardian_id=guardianid;
									getcat.senderid = sender_id;
								    getcat.receiverid=receiver_id;
									getcat.location_event = location_ofevent;
									// getcat.parent_name=parentname;
									getcat.notes_event = notes;
									// getcat.parent_profile_image=parentpic;
									getcat.start_time_of_event = event_start;
									getcat.status_ofevent = "ACCEPTED";
									getcat.eventid = event_id;
									
									 getcat.child_dob=child_dob;
									 getcat.child_allergies=child_allergies;
									 //getcat.child_freetime=child_freetime;
									 //getcat.child_hobbies=child_hobbies;
									// getcat.child_school=child_school;
									// getcat.child_youthclub=child_youthclub;
									 getcat.child_friends_allergies=child_friends_allergies;
									 getcat.child_friends_dob=child_friends_dob;
									 //getcat.child_friends_freetime=child_friends_freetime;
									 //getcat.child_friends_school=child_friends_school;
									 //getcat.child_friends_hobbies=child_friends_hobbies;
									// getcat.child_friends_youthclub=child_friends_youthclub;
									
									searchList.add(getcat);
								}
							}
						}
						
						if(searchList!=null && searchList.size()>0)
						{
							adapter1 = new LazyAdapter(getActivity(), searchList);

							list_event.setAdapter(adapter1);
							adapter1.notifyDataSetChanged();
						}else
						{
							adapter1 = new LazyAdapter(getActivity(), searchList);

							list_event.setAdapter(null);
							adapter1.notifyDataSetChanged();
						Toast.makeText(getActivity(),"No event", 1).show();
						}
					} catch (Exception e) {
					}
				}

			}
		});

		list_event.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ArrayList<Getcatagory_forlist> _items;
				
				if(check_list_click){
					_items = searchList;
				}else{
					_items = params;
				}
				
				String event_id = _items.get(arg2).eventid;
				if (event_id.equals("-1")) {
					String child_name = _items.get(arg2).child_name;
					String event_date = _items.get(arg2).date_ofevent;
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setTitle(child_name);
					builder.setMessage("DATE - " + event_date)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// FIRE ZE MISSILES!
										}
									});
					builder.create();
					builder.show();

				} else {
					String child_name = _items.get(arg2).child_name;
					String Child_id = _items.get(arg2).child_id;
					String Child_profilepic = _items.get(arg2).child_profile_image;
					String Child_friend_name = _items.get(arg2).childfriend_name;
					String Child_frient_id = _items.get(arg2).friend_id;
					String Child_friend_pic = _items.get(arg2).friendprofile_image;
					// String Child_guardianId=_items.get(arg2).guardian_id;

					String event_date = _items.get(arg2).date_ofevent;
					String event_start = _items.get(arg2).start_time_of_event;
					String event_end_time = _items.get(arg2).end_time_of_event;
					String notes = _items.get(arg2).notes_event;
					String location_ofevent = _items.get(arg2).location_event;
					String sender_id = _items.get(arg2).senderid;
				    String receiver_id=_items.get(arg2).receiverid;
					String status_of_event_ = _items.get(arg2).status_ofevent;

					String child_dob=_items.get(arg2).child_dob;
					//String child_hobbies=_items.get(arg2).child_hobbies;
					String child_allergies=_items.get(arg2).child_allergies;
					//String child_freetime=_items.get(arg2).child_freetime;
					//String child_school=_items.get(arg2).child_school;
					//String child_youthclub=_items.get(arg2).child_youthclub;
					String child_friends_dob=_items.get(arg2).child_friends_dob;
					String child_friends_allergies=_items.get(arg2).child_friends_allergies;
					//String child_friends_hobbies=_items.get(arg2).child_friends_hobbies;
					//String child_friends_freetime=_items.get(arg2).child_friends_freetime;
					//String child_friends_school=_items.get(arg2).child_friends_school;
					//String child_friends_youthclub=_items.get(arg2).child_friends_youthclub;
					
					
					Bundle bundle = new Bundle();
					
					bundle.putString("child_dob", child_dob);
					//bundle.putString("child_hobbies", child_hobbies);
					bundle.putString("child_allergies", child_allergies);
					//bundle.putString("child_freetime", child_freetime);
					//bundle.putString("child_school", child_school);
					//bundle.putString("child_youthclub", child_youthclub);
					bundle.putString("child_friends_dob", child_friends_dob);
					bundle.putString("child_friends_allergies", child_friends_allergies);
					//bundle.putString("child_friends_hobbies", child_friends_hobbies);
					//bundle.putString("child_friends_freetime", child_friends_freetime);
					//bundle.putString("child_friends_school", child_friends_school);
					//bundle.putString("child_friends_youthclub", child_friends_youthclub);
					
					bundle.putString("child_name", child_name);
					bundle.putString("Child_id", Child_id);
					bundle.putString("Child_profilepic", Child_profilepic);
					bundle.putString("Child_friend_name", Child_friend_name);
					bundle.putString("Child_frient_id", Child_frient_id);
					bundle.putString("Child_friend_pic", Child_friend_pic);
					bundle.putString("Child_guardianId", user_guardian_id);
					bundle.putString("event_id", event_id);
					bundle.putString("event_date", event_date);
					bundle.putString("event_start", event_start);
					bundle.putString("event_end_time", event_end_time);
					bundle.putString("notes", notes);
					bundle.putString("sender_id", sender_id);
				    bundle.putString("receiver_id", receiver_id);
					bundle.putString("status", status_of_event_);
					bundle.putString("location_ofevent", location_ofevent);
					
					bundle.putString("CalenderScreen", "calenderscreen");

					android.support.v4.app.Fragment fragment = new Event_accept_reject_edit();
					fragment.setArguments(bundle);
				//	android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);
					fragmentTransaction.addToBackStack("first2");
					fragmentTransaction.commit();
					/*fragmentManager.beginTransaction()
							.replace(R.id.content_frame, fragment).commit();*/
				}
			}
		});

		return view;
	}

	public void refreshCalendar() {
		check_list_click=false;
		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some random calendar items

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public void onNewIntent() {
		/* String date = intent.getStringExtra("date"); */
		// String[] dateArr = date.split("-"); // date format is yyyy-mm-dd
		// month.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]),
		// Integer.parseInt(dateArr[2]));

		/*
		 * Date now = new Date(); Date alsoNow =
		 * Calendar.getInstance().getTime(); String nowAsString = new
		 * SimpleDateFormat("yyyy-MM-dd").format(now);
		 */

		Calendar c = Calendar.getInstance();
		//System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = df.format(c.getTime());
		String[] dateArr = formattedDate.split("-"); // date format is
														// yyyy-mm-dd
		month.set(Integer.parseInt(dateArr[0]),
				Integer.parseInt(dateArr[1]) - 1, Integer.parseInt(dateArr[2]));
		// month.set(2014, 06, 14);
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items.clear();
			_date.clear();
			Calendar c = Calendar.getInstance();
			//System.out.println("Current time => " + c.getTime());
			ProgressDialog dialog_progress=null;
try {

	 dialog_progress=new ProgressDialog(getActivity());
	dialog_progress.setMessage("Loading events");
	dialog_progress.setCancelable(false);
	dialog_progress.show();
} catch (Exception e) {
	// TODO: handle exception
}
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = df.format(c.getTime());
			String[] dateArr1 = formattedDate.split("-"); // date format is
				String year_for_comparision=dateArr1[0];											// yyyy-mm-dd
			
			// format random values. You can implement a dedicated class to
			// provide real values
			/*
			 * for(int i=0;i<31;i++) { Random r = new Random();
			 * 
			 * if(r.nextInt(10)>6) { items.add(Integer.toString(i));
			 * System.out.println("================"+Integer.toString(i)); } }
			 */

		//	dialog.setTitle("Loading, Please wait...");
		//	dialog.setCancelable(false);
			//dialog.show();
			if (check_event == true) {
				try {
					String month_for_parsing = android.text.format.DateFormat
							.format("MM", month).toString();
					params = new ArrayList<Getcatagory_forlist>();
					params_systems_event = new ArrayList<Getcatagory_forlist>();
					String uriString = "content://com.android.calendar//events";
					//Log.i("INFO", "Reading content from " + uriString);
					readContent(uriString);
					int i = 0;
					for (Getcatagory_forlist temp : params_systems_event) {
						String monts_for_comparasion = params_systems_event
								.get(i).date_ofevent;
						String[] dateArr = monts_for_comparasion.split("-"); // date
						String date_forcalander = dateArr[1];
						if (date_forcalander.equals(month_for_parsing)) {

							Getcatagory_forlist getcat = params_systems_event
									.get(i);
							params.add(getcat);
							String date_for_gridview = dateArr[2];
							int date_int = Integer.parseInt(date_for_gridview);
							items.add(Integer.toString(date_int));
						}
						i++;
					}
				} catch (Exception e) {
					// TODO: handle exception

				}
				dialog_progress.dismiss();
				//dialog.dismiss();
				adapter1 = new LazyAdapter(getActivity(), params);

				list_event.setAdapter(adapter1);

				adapter.setItems(items);
				adapter.notifyDataSetChanged();
			} else {

				params = new ArrayList<Getcatagory_forlist>();
				JSONObject json = null;
				try {
					json = new JSONObject(sResponse);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String date_for_parsing = android.text.format.DateFormat
						.format("yyyy", month).toString();
				String month_for_parsing = android.text.format.DateFormat
						.format("MM", month).toString();
				int monthsforparsing = Integer.parseInt(month_for_parsing);

			//	System.out.println("month_for_parsingmonth_for_parsing"
			//			+ month_for_parsing);
				try {

					JSONObject jsondata = json.getJSONObject("data");
					JSONObject year = jsondata.getJSONObject(date_for_parsing);
					JSONArray jarray = year.getJSONArray(String
							.valueOf(monthsforparsing));

					// JSONArray jArray=json.getJSONArray("data");
					//Log.d("fdgn", "dsgfbkj" + jarray);
					for (int i = 0; i < jarray.length(); i++) {
						JSONObject c1 = jarray.getJSONObject(i);
						Getcatagory_forlist getcat = new Getcatagory_forlist();
						String eventid = c1.getString("event_id");
						String childname = c1.getString("child_name");
						String childid = c1.getString("child_id");
						String childpicurl = c1.getString("profile_image");
						String friendname = c1.getString("friendname");
						String friendid = c1.getString("friend_childid");
						String friendpicurl = c1.getString("friend_profile_image");
						// String parentname=c.getString("name");
						String senderid = c1.getString("senderid");
					    String receiverid=c1.getString("receiver_id");
						String parentpic = c1.getString("parent_profile_image");
						String date = c1.getString("date");
						String starttime = c1.getString("starttime");
						String endtime = c1.getString("endtime");
						String location = c1.getString("location");
						String notes = c1.getString("notes");
						//
						 String child_dob=c1.getString("child_dob");
						// String child_c_set_fixed_freetime=c1.getString("child_c_set_fixed_freetime");
						// String child_school=c1.getString("child_school");
						 String child_allergies=c1.getString("child_allergies");
						// String child_hobbies=c1.getString("child_hobbies");
						// String child_youth_club=c1.getString("child_youth_club");
						 String friend_dob=c1.getString("friend_dob");
						// String friend_c_set_fixed_freetime=c1.getString("friend_c_set_fixed_freetime");
					//	 String friend_school=c1.getString("friend_school");
						 String friend_allergies=c1.getString("friend_allergies");
					//	 String friend_hobbies=c1.getString("friend_hobbies");
					//	 String friend_youth_club=c1.getString("friend_youth_club");
						 
						// String status_ofevent=c.getString("status");
						getcat.child_id = childid;
						getcat.child_name = childname;
						getcat.child_profile_image = childpicurl;
						getcat.childfriend_name = friendname;
						getcat.date_ofevent = date;
						getcat.end_time_of_event = endtime;
						getcat.friend_id = friendid;
						getcat.friendprofile_image = friendpicurl;
						// getcat.guardian_id=guardianid;
						getcat.senderid = senderid;
					    getcat.receiverid=receiverid;
						getcat.location_event = location;
						// getcat.parent_name=parentname;
						getcat.notes_event = notes;
						getcat.parent_profile_image = parentpic;
						getcat.start_time_of_event = starttime;
						getcat.status_ofevent = "ACCEPTED";
						getcat.eventid = eventid;
						
						 getcat.child_dob=child_dob;
						 getcat.child_allergies=child_allergies;
						// getcat.child_freetime=child_c_set_fixed_freetime;
						// getcat.child_hobbies=child_hobbies;
					//	 getcat.child_school=child_school;
					//	 getcat.child_youthclub=child_youth_club;
						 getcat.child_friends_allergies=friend_allergies;
						 getcat.child_friends_dob=friend_dob;
					//	 getcat.child_friends_freetime=friend_c_set_fixed_freetime;
					//	 getcat.child_friends_school=friend_school;
					//	 getcat.child_friends_hobbies=friend_hobbies;
					//	 getcat.child_friends_youthclub=friend_youth_club;
						 
						 

						String[] dateArr = date.split("-"); // date format is
															// yyyy-mm-dd
						// month.set(Integer.parseInt(dateArr[0]),
						// Integer.parseInt(dateArr[1]),
						// Integer.parseInt(dateArr[2]));
						String date_forcalander = dateArr[2];
						int date_int = Integer.parseInt(date_forcalander);

						items.add(Integer.toString(date_int));
						_date.add(date);
						params.add(getcat);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					/*
					 * String month_for_parsing = android.text.format.DateFormat
					 * .format("MM", month).toString();
					 */
					params_systems_event = new ArrayList<Getcatagory_forlist>();
					String uriString = "content://com.android.calendar//events";
				//	Log.i("INFO", "Reading content from " + uriString);
					calander_getdate();
				//	readContent(uriString);
					int i = 0;
					for (Getcatagory_forlist temp : params_systems_event) {
						String monts_for_comparasion = params_systems_event
								.get(i).date_ofevent;
						String[] dateArr = monts_for_comparasion.split("-"); // date
						String year_of_event=dateArr[0];
						String date_forcalander = dateArr[1];
					//	if(date_for_parsing.equals(year_of_event)){
						if (date_forcalander.equals(month_for_parsing)) {
						//	System.out.println(i);
							Getcatagory_forlist getcat = params_systems_event
									.get(i);
							params.add(getcat);
							String date_for_gridview = dateArr[2];
							int date_int = Integer.parseInt(date_for_gridview);
							items.add(Integer.toString(date_int));
						}
						i++;
						}
					//}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();

				}

				
				adapter1 = new LazyAdapter(getActivity(), params);

				list_event.setAdapter(adapter1);

				adapter.setItems(items);
				adapter.notifyDataSetChanged();
			//	dialog.dismiss();
				try {
					dialog_progress.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				

			}
		}
	};

	public class Get_Event extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		String url_sets;
		boolean running = true;

		@Override
		protected void onPreExecute() {

			dialog.show();
			dialog.setMessage("Loading.......please wait");
			url_sets = Constants.EVENT_DATE;//"http://54.191.67.152/playdate/eventaccept_date_time.php";// ?g_id=47&status=accepted
		}

		@Override
		protected String doInBackground(String... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url_sets);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("g_id", user_guardian_id));
			nameValuePairs.add(new BasicNameValuePair("status", "accepted"));

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));

			//	System.out.println(httpPost);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			HttpResponse response = null;
			try {
				response = httpClient.execute(httpPost, localContext);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				running = false;
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				running = false;
				e.printStackTrace();
			}
			if (running == true) {
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new InputStreamReader(response
							.getEntity().getContent(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject joJsonObject = null;
				try {
					sResponse = reader.readLine();
					try {
						joJsonObject = new JSONObject(sResponse);

						json_object = joJsonObject.getJSONObject("data");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						try {
							check_event = true;
							data_json = joJsonObject.getString("data");
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				//	System.out
				//			.println("response.................................."
				//					+ sResponse);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}

		protected void onPostExecute(String resultt) {
if(dialog!=null && dialog.isShowing())
{
	dialog.dismiss();
}
			handler = new Handler();
			handler.post(calendarUpdater);
		}
	}

	public class LazyAdapter extends BaseAdapter {
		String _imgurlchild = "", _imgurlfriend = "", _imgparent = "";
		private Activity activity;
		private ArrayList<Getcatagory_forlist> _items;
		private LayoutInflater inflater = null;

		public ImageLoader imageLoader;

		public LazyAdapter(Activity activity,
				ArrayList<Getcatagory_forlist> parentItems) {

			// this.imageLoader.clearCache();
			this.activity = activity;
			this._items = parentItems;
			try {
				inflater = (LayoutInflater) getActivity().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			imageLoader = new ImageLoader(getActivity());
		}

		public int getCount() {
			return _items.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			public TextView guardian_name, date_of_event_list, time;
			public ImageView imageview;

		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View vi = convertView;
			String url;
			ViewHolder _holder;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.list_for_calander, null);
				_holder = new ViewHolder();
				_holder.imageview = (ImageView) convertView
						.findViewById(R.id.imageView1_calander);
				_holder.guardian_name = (TextView) convertView
						.findViewById(R.id.textView_list_calander_name);

				_holder.date_of_event_list = (TextView) convertView
						.findViewById(R.id.textView_list_calander_date);
				_holder.time = (TextView) convertView
						.findViewById(R.id.textView_list_calander_time);

				convertView.setTag(_holder);
			} else {
				_holder = (ViewHolder) convertView.getTag();
			}
			String name_of_child = _items.get(position).child_name;
			String name_of_childfriend = _items.get(position).childfriend_name;
			// String name_of_childparent=_items.get(position).parent_name;
			String date_ofevent = _items.get(position).date_ofevent;
			String start_time = _items.get(position).start_time_of_event;
			String endString = _items.get(position).end_time_of_event;

			String name_of_child_caps = name_of_child.toUpperCase();

			if (start_time.equals("") || start_time.equals(null)) {
				_holder.time.setText("");
			} else {
				_holder.time.setText(start_time + " - " + endString);
			}
			_holder.guardian_name.setText(name_of_child_caps);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date_start_time = null; 
			try {
				//date_start_time = convertDate(date_ofevent, "yyyy-MM-dd");
				date_start_time=sdf.parse(date_ofevent);
				

			} catch (Exception e) {
				// TODO: handle exception
			}
			String date_of_event1=null;
			try {
				SimpleDateFormat destDf = new SimpleDateFormat("dd/MM/yy");
				 date_of_event1 = destDf.format(date_start_time);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			_holder.date_of_event_list.setText(date_of_event1);

			url = _items.get(position).friendprofile_image;

			_holder.imageview.setTag(url);

			imageLoader.DisplayImage(url, _holder.imageview);

			_holder.imageview.requestLayout();

			int density = getResources().getDisplayMetrics().densityDpi;
			switch (density) {
			case DisplayMetrics.DENSITY_LOW:
				_holder.imageview.getLayoutParams().height = 40;
				_holder.imageview.getLayoutParams().width = 40;

				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				_holder.imageview.getLayoutParams().height = 85;
				_holder.imageview.getLayoutParams().width = 85;

				break;
			case DisplayMetrics.DENSITY_HIGH:
				_holder.imageview.getLayoutParams().height = 85;
				_holder.imageview.getLayoutParams().width = 85;

				break;
			case DisplayMetrics.DENSITY_XHIGH:
				_holder.imageview.getLayoutParams().height = 120;
				_holder.imageview.getLayoutParams().width = 120;

				break;
			case DisplayMetrics.DENSITY_XXHIGH:
				_holder.imageview.getLayoutParams().height = 120;
				_holder.imageview.getLayoutParams().width = 120;

				break;
			}

			return convertView;
		}
	}

	private void readContent(String uriString) {
		Calendar c = Calendar.getInstance();
		//System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = df.format(c.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date current = null;
		try {
			current = sdf.parse(formattedDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Uri uri = Uri.parse(uriString);

		Cursor cursor = getActivity().getContentResolver().query(uri, null,
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				String event_title = cursor.getString(cursor
						.getColumnIndex("title"));
				String dd_startevent = cursor.getString(cursor
						.getColumnIndex("dtstart"));
				String dd_end_event = cursor.getString(cursor
						.getColumnIndex("lastDate"));
				String date_start_time = null, date_end_time = null;
				try {
					date_start_time = convertDate(dd_startevent, "yyyy-MM-dd");
					
					date_end_time = convertDate(dd_end_event, "yyyy-MM-dd");

				} catch (Exception e) {
					// TODO: handle exception
				}
				String event_id = "-1";
				Getcatagory_forlist getcat = new Getcatagory_forlist();
				// System.out.println(date_start_time);

				getcat.start_time_of_event = "";
				getcat.end_time_of_event = "";
				getcat.date_ofevent = date_start_time;
				getcat.end_date_of_event = date_end_time;
				getcat.child_name = event_title;
				getcat.eventid = event_id;
				
				//params_systems_event.add(getcat);
				
				
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				Date date_of_event = null;
				try {
					date_of_event = sdf1.parse(date_start_time);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (event_title.equalsIgnoreCase("PLAYDATE EVENT")||event_title.contains("Playdate")) {

				} else {
					params_systems_event.add(getcat);
				}
				

				/*if(date_of_event.before(current)){
				    //Do Something
					if (event_title.equalsIgnoreCase("PLAYDATE EVENT")) {

					} else {
						params_systems_event.add(getcat);
					}
				}else{
					
				}

				*/
				
				
				
				

			} while (cursor.moveToNext());

		}

	}

	public static String convertDate(String dateInMilliseconds,
			String dateFormat) {
		return DateFormat
				.format(dateFormat, Long.parseLong(dateInMilliseconds))
				.toString();
	}
	
	@SuppressLint("InlinedApi")
	public static final String[] INSTANCE_PROJECTION = new String[] {
	    Instances.END,      // 0
	    Instances.BEGIN,         // 1
	    Instances.TITLE          // 2
	  };
	
	public void calander_getdate(){
	final String DEBUG_TAG = "MyActivity";
	
	  System.out.println("calender............................................");
	// The indices for the projection array above.
	  final int PROJECTION_ID_INDEX = 0;
	  final int PROJECTION_BEGIN_INDEX = 1;
	  final int PROJECTION_TITLE_INDEX = 2;
	String month_details=android.text.format.DateFormat.format("MM", month).toString();
	String year_details=android.text.format.DateFormat.format("yyyy", month).toString();

	// Specify the date range you want to search for recurring
	// event instances
	Calendar beginTime = Calendar.getInstance();
	beginTime.set(Integer.parseInt(year_details), Integer.parseInt(month_details)-1, 1, 8, 0);
	
	long startMillis = beginTime.getTimeInMillis();
	Calendar endTime = Calendar.getInstance();
	endTime.set(Integer.parseInt(year_details), Integer.parseInt(month_details)-1, 31, 8, 0);
	long endMillis = endTime.getTimeInMillis();
	  
	Cursor cur = null;
	ContentResolver cr = getActivity().getContentResolver();

	// The ID of the recurring event whose instances you are searching
	// for in the Instances table
	//String selection = Instances.EVENT_ID + " = ?";
	//String[] selectionArgs = new String[] {"207"};

	// Construct the query with the desired date range.
	Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
	ContentUris.appendId(builder, startMillis);
	ContentUris.appendId(builder, endMillis);

	// Submit the query
	cur =  cr.query(builder.build(), 
	    INSTANCE_PROJECTION, 
	    null, 
	    null, 
	    null);
	   
	while (cur.moveToNext()) {
	    String title = null;
	    long endVal = 0;
	    long beginVal = 0;    
	    
	    // Get the field values
	    endVal = cur.getLong(PROJECTION_ID_INDEX);
	    beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
	    title = cur.getString(PROJECTION_TITLE_INDEX);
	       System.out.println(title);       
	    // Do something with the values. 
	    Log.i(DEBUG_TAG, "Event:  " + title); 
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(beginVal);  
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    Log.i(DEBUG_TAG, "Date: " + formatter.format(calendar.getTime()));  
	    Calendar calendar_end = Calendar.getInstance();
	    calendar_end.setTimeInMillis(endVal);
	    Getcatagory_forlist getcat = new Getcatagory_forlist();
		// System.out.println(date_start_time);
	    String event_id = "-1";
		getcat.start_time_of_event = "";
		getcat.end_time_of_event = "";
		getcat.date_ofevent = formatter.format(calendar.getTime());
		getcat.end_date_of_event = formatter.format(calendar_end.getTime());
		getcat.child_name = title;
		getcat.eventid = event_id;
		
		//params_systems_event.add(getcat);
		
		
		/*SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date_of_event = null;
		try {
			date_of_event = sdf1.parse(date_start_time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if (title.equalsIgnoreCase("PLAYDATE EVENT")||title.contains("Playdate")) {

		} else {
			params_systems_event.add(getcat);
		}
	    }
	 }

}
	
	
	
	
	
	

	/*
	 * public void insert_event_intocalander(){ Calendar cal =
	 * Calendar.getInstance(); Intent intent = new Intent(Intent.ACTION_EDIT);
	 * intent.setType("vnd.android.cursor.item/event");
	 * intent.putExtra("beginTime", cal.getTimeInMillis());
	 * intent.putExtra("allDay", true); intent.putExtra("rrule", "FREQ=YEARLY");
	 * intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
	 * intent.putExtra("title", "A Test Event from android app");
	 * startActivity(intent);
	 * 
	 * // Another method
	 * 
	 * String eventUriString = "content://com.android.calendar/events";
	 * ContentValues eventValues = new ContentValues();
	 * 
	 * eventValues.put("calendar_id", 1); // id, We need to choose from // our
	 * mobile for primary // its 1 eventValues.put("title", "title");
	 * eventValues.put("description", ""); eventValues.put("eventLocation", "");
	 * 
	 * //long endDate = startDate + 1000 * 60 * 60; // For next 1hr
	 * 
	 * eventValues.put("dtstart", "startDate"); eventValues.put("dtend",
	 * "endDate");
	 * 
	 * eventValues.put("eventStatus", "status"); // This information is
	 * 
	 * eventValues.put("hasAlarm", 1); // 0 for false, 1 for true
	 * 
	 * Uri eventUri =
	 * getActivity().getContentResolver().insert(Uri.parse(eventUriString),
	 * eventValues); long eventID =
	 * Long.parseLong(eventUri.getLastPathSegment());
	 * 
	 * 
	 * // Another method
	 * 
	 * 
	 * if (Build.VERSION.SDK_INT >= 14) { Intent intent1 = new
	 * Intent(Intent.ACTION_INSERT) .setData(Events.CONTENT_URI)
	 * .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, "")
	 * .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, "")
	 * .putExtra(Events.TITLE, "Yoga") .putExtra(Events.DESCRIPTION,
	 * "Group class") .putExtra(Events.EVENT_LOCATION, "The gym")
	 * .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
	 * .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
	 * startActivity(intent1); }
	 * 
	 * else { Calendar cal1 = Calendar.getInstance(); Intent intent1 = new
	 * Intent(Intent.ACTION_EDIT);
	 * intent1.setType("vnd.android.cursor.item/event");
	 * intent1.putExtra("beginTime", cal1.getTimeInMillis());
	 * intent1.putExtra("allDay", true); intent1.putExtra("rrule",
	 * "FREQ=YEARLY"); intent1.putExtra("endTime",
	 * cal1.getTimeInMillis()+60*60*1000); intent1.putExtra("title",
	 * "A Test Event from android app"); startActivity(intent1); }
	 * 
	 * 
	 * // Another method
	 * 
	 * Calendar beginTime = Calendar.getInstance(); // beginTime.set("yearInt",
	 * "monthInt- 1", "dayInt", 7, 30);
	 * 
	 * 
	 * 
	 * ContentValues l_event = new ContentValues(); l_event.put("calendar_id",
	 * "CalIds[0]"); l_event.put("title", "event"); l_event.put("description",
	 * "This is test event"); l_event.put("eventLocation", "School");
	 * l_event.put("dtstart", beginTime.getTimeInMillis()); l_event.put("dtend",
	 * beginTime.getTimeInMillis()); l_event.put("allDay", 0);
	 * l_event.put("rrule", "FREQ=YEARLY"); // status: 0~ tentative; 1~
	 * confirmed; 2~ canceled // l_event.put("eventStatus", 1);
	 * 
	 * l_event.put("eventTimezone", "India"); Uri l_eventUri; if
	 * (Build.VERSION.SDK_INT >= 8) { l_eventUri =
	 * Uri.parse("content://com.android.calendar/events"); } else { l_eventUri =
	 * Uri.parse("content://calendar/events"); } Uri l_uri =
	 * getActivity().getContentResolver() .insert(l_eventUri, l_event);
	 * 
	 * }
	 */


