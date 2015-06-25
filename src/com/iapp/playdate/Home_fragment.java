package com.iapp.playdate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iapp.playdate.R;
import com.iapptechnologies.time.util.Constants;

public class Home_fragment extends android.support.v4.app.Fragment {
	String user_guardian_id;
	JSONArray customMessageStr;
	ListView list_home;
	ConnectionDetector cd;
	DB_Helper db;
	Button startarrange,addchild;
	RelativeLayout main_layout;
	TextView first_testview, second_textview, third_TextView, forth_textview;
	ProgressDialog dialog;// =new ProgressDialog(getActivity());
	Boolean isInternetPresent = false;
	ArrayList<String> welcome_message_list = new ArrayList<String>();
	ArrayList<Getcatagory_forlist> params;// = new
	SharedPreferences settings;
	SharedPreferences.Editor editor;									// ArrayList<Getcatagory_forlist>();

	public Home_fragment() {

	}

	@Override
	public void onResume() {
		super.onResume();
		params = new ArrayList<Getcatagory_forlist>();
		cd = new ConnectionDetector(getActivity());
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
			new getEvents_detail().execute();
		} else {
			Toast.makeText(getActivity(), "Please check internet connection",
					2000).show();
			return;
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.home, container,
				false);
		db = new DB_Helper(getActivity());
		dialog = new ProgressDialog(getActivity());
		settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
		editor = settings.edit();
		
		user_guardian_id=settings.getString("USERID","");
		//user_guardian_id = getArguments().getString("user_guardian_id");
		
		
		System.out.println(">>>>>>>>>>>>>>" + user_guardian_id);
		list_home = (ListView) view.findViewById(R.id.listView1_home);
		
		startarrange=(Button)view.findViewById(R.id.startarrange);
		addchild=(Button)view.findViewById(R.id.addchild);
	
		Home.menu.setVisibility(View.GONE);
		list_home.setDivider(null);
		list_home.setDividerHeight(0);
		main_layout=(RelativeLayout)view.findViewById(R.id.home_fragment_layout);
		
		
		startarrange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				android.support.v4.app.Fragment fragment;	 fragment = new Arrange_date_fragment();
				android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.addToBackStack("first11");
				fragmentTransaction.commit();
			}
		});
        addchild.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				android.support.v4.app.Fragment fragment;	 fragment = new Add_Child();
				android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.addToBackStack("first11");
				fragmentTransaction.commit();
			}
		});
		
		

		list_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ArrayList<Getcatagory_forlist> _items;
				_items = params;
				Home.menu.setVisibility(View.GONE);

				String child_name = _items.get(arg2).child_name;
				String Child_id = _items.get(arg2).child_id;
				String Child_profilepic = _items.get(arg2).child_profile_image;
				String Child_friend_name = _items.get(arg2).childfriend_name;
				String Child_frient_id = _items.get(arg2).friend_id;
				String Child_friend_pic = _items.get(arg2).friendprofile_image;
				String child_dob = _items.get(arg2).child_dob;
				//String child_hobbies = _items.get(arg2).child_hobbies;
				String child_allergies = _items.get(arg2).child_allergies;
				//String child_freetime = _items.get(arg2).child_freetime;
				//String child_school = _items.get(arg2).child_school;
				//String child_youthclub = _items.get(arg2).child_youthclub;
				String child_friends_dob = _items.get(arg2).child_friends_dob;
				String child_friends_allergies = _items.get(arg2).child_friends_allergies;
				//String child_friends_hobbies = _items.get(arg2).child_friends_hobbies;
				//String child_friends_freetime = _items.get(arg2).child_friends_freetime;
				//String child_friends_school = _items.get(arg2).child_friends_school;
				//String child_friends_youthclub = _items.get(arg2).child_friends_youthclub;
				// String Child_guardianId=_items.get(arg2).guardian_id;
				String event_id = _items.get(arg2).eventid;
				String event_date = _items.get(arg2).date_ofevent;
				String event_start = _items.get(arg2).start_time_of_event;
				String event_end_time = _items.get(arg2).end_time_of_event;
				String notes = _items.get(arg2).notes_event;
				String location_ofevent = _items.get(arg2).location_event;
				String sender_id = _items.get(arg2).senderid;
				String receiver_id = _items.get(arg2).receiverid;
				String status_of_event_ = _items.get(arg2).status_ofevent;

				//String date1_event = _items.get(arg2).date_of_event1;
				//String date2_event = _items.get(arg2).date_of_event2;
			//	String date3_event = _items.get(arg2).date_of_event3;

			//	String start_time1_event = _items.get(arg2).start_time_of_event1;
			//	String start_time2_event = _items.get(arg2).start_time_of_event2;
			//	String start_time3_event = _items.get(arg2).start_time_of_event3;

			//	String end_time1_event = _items.get(arg2).end_time_of_event1;
			//	String end_time2_event = _items.get(arg2).end_time_of_event2;
		//		String end_time3_event = _items.get(arg2).end_time_of_event3;

				Bundle bundle = new Bundle();
				bundle.putString("child_dob", child_dob);
				//bundle.putString("child_hobbies", child_hobbies);
				bundle.putString("child_allergies", child_allergies);
				//bundle.putString("child_freetime", child_freetime);
				//bundle.putString("child_school", child_school);
				//bundle.putString("child_youthclub", child_youthclub);
				bundle.putString("child_friends_dob", child_friends_dob);
				bundle.putString("child_friends_allergies",child_friends_allergies);
				//bundle.putString("child_friends_hobbies", child_friends_hobbies);
				//bundle.putString("child_friends_freetime",child_friends_freetime);
				//bundle.putString("child_friends_school", child_friends_school);
				//bundle.putString("child_friends_youthclub",child_friends_youthclub);
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
				//bundle.putString("date1_event", date1_event);
			//	//bundle.putString("date2_event", date2_event);
			//	bundle.putString("date3_event", date3_event);
			//	bundle.putString("start_time1_event", start_time1_event);
			//	System.out.println("start_time1_event" + start_time1_event);
			//	bundle.putString("start_time2_event", start_time2_event);
			//	System.out.println("start_time2_event" + start_time2_event);
			//	bundle.putString("start_time3_event", start_time3_event);
			//	System.out.println("start_time3_event" + start_time3_event);
			//	bundle.putString("end_time1_event", end_time1_event);
			//	System.out.println("end_time1_event" + end_time1_event);
			//	bundle.putString("end_time2_event", end_time2_event);
			///	System.out.println("end_time2_event" + end_time2_event);
			//	bundle.putString("end_time3_event", end_time3_event);
			//	System.out.println("end_time3_event" + end_time3_event);

				android.support.v4.app.Fragment fragment = new Event_accept_reject_edit();
				fragment.setArguments(bundle);
				android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.addToBackStack("first11");
				fragmentTransaction.commit();
				/*
				 * android.support.v4.app.FragmentManager fragmentManager =
				 * getFragmentManager(); fragmentManager.beginTransaction()
				 * .replace(R.id.content_frame, fragment).commit();
				 */

			}
		});
		return view;
	}

	public class getEvents_detail extends AsyncTask<String, Integer, String> {

		String url;
		InputStream is;
		String result;
		JSONObject jArray = null;
		String data;
		LazyAdapter adapter;
		boolean check_null = false;

		@Override
		protected void onPreExecute() {
			try {
				dialog.setMessage("Loading.......please wait");
				dialog.setCancelable(false);
				dialog.show();
			} catch (Exception e) {
				// TODO: handle exception
			}

			url = Constants.GET_EVENTS;//"http://54.191.67.152/playdate/event_with.php";// ?g_id=46;
																	// get event
		}

		@Override
		protected String doInBackground(String... arg0) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url);
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("g_id", user_guardian_id));

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
				System.out.println(httpPost);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			HttpResponse response = null;
			try {
				response = httpClient.execute(httpPost, localContext);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				check_null = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				check_null = true;
			}
			catch (Exception e) {
				// TODO: handle exception
				check_null = true;
			}
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				check_null = true;
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				check_null = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				check_null = true;
			}
			catch (Exception e) {
				// TODO: handle exception
				check_null = true;
			}
			String sResponse = null;

			try {
				sResponse = reader.readLine();

				System.out.println("response" + sResponse);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				check_null = true;
			}
			catch (Exception e) {
				// TODO: handle exception
				check_null = true;
			}
			try {
				JSONObject json = new JSONObject(sResponse);

				JSONArray jarray = json.getJSONArray("data");
				for (int i = 0; i < jarray.length(); i++) {
					JSONObject c = jarray.getJSONObject(i);
					Getcatagory_forlist getcat = new Getcatagory_forlist();
					String child_dob = c.getString("child_dob");
					//String child_c_set_fixed_freetime = c.getString("child_c_set_fixed_freetime");
					//String child_school = c.getString("child_school");
					String child_allergies = c.getString("child_allergies");
					//String child_hobbies = c.getString("child_hobbies");
					//String child_youth_club = c.getString("child_youth_club");
					String friend_dob = c.getString("friend_dob");
					//String friend_c_set_fixed_freetime = c.getString("friend_c_set_fixed_freetime");
					//String friend_school = c.getString("friend_school");
					String friend_allergies = c.getString("friend_allergies");
					//String friend_hobbies = c.getString("friend_hobbies");
					//String friend_youth_club = c.getString("friend_youth_club");

					//
					String eventid = c.getString("Eventid");
					String childname = c.getString("child_name");
					String childid = c.getString("Child_id");
					String childpicurl = c.getString("profile_image");
					String friendname = c.getString("friendname");
					String friendid = c.getString("friend_childid");
					String friendpicurl = c.getString("friend_profile_image");
					String parentname = c.getString("name");
					String senderid = c.getString("senderid");
					String receiverid = c.getString("receiver_id");
					String parentpic = c.getString("parent_profile_image");
					String date = c.getString("date");
					String starttime = c.getString("Starttime");
					String endtime = c.getString("endtime");
					String location = c.getString("location");
					String notes = c.getString("notes");
					String status_ofevent = c.getString("status");
					// Alternate date/Time
					//String altdate1 = c.getString("date1");
					//String altStarttime1 = c.getString("Starttime1");
					//String altendtime1 = c.getString("endtime1");
					//System.out.println(altStarttime1 + altendtime1);
					//String altdate2 = c.getString("date2");
					//String altStarttime2 = c.getString("Starttime2");
					//String altendtime2 = c.getString("endtime2");
					//System.out.println(altStarttime2 + altendtime2);
					//String altdate3 = c.getString("date3");
					//String altStarttime3 = c.getString("Starttime3");
					//String altendtime3 = c.getString("endtime3");
					//System.out.println(altStarttime3 + altendtime3);
					// add alternate date/time to list

					getcat.child_dob = child_dob;
					getcat.child_allergies = child_allergies;
					//getcat.child_freetime = child_c_set_fixed_freetime;
					//getcat.child_hobbies = child_hobbies;
					//getcat.child_school = child_school;
					//getcat.child_youthclub = child_youth_club;
					getcat.child_friends_allergies = friend_allergies;
					getcat.child_friends_dob = friend_dob;
					//getcat.child_friends_freetime = friend_c_set_fixed_freetime;
					//getcat.child_friends_school = friend_school;
					//getcat.child_friends_hobbies = friend_hobbies;
					//getcat.child_friends_youthclub = friend_youth_club;

					//getcat.date_of_event1 = altdate1;
					//getcat.date_of_event2 = altdate2;
					//getcat.date_of_event3 = altdate3;

					//getcat.start_time_of_event1 = altStarttime1;
					//getcat.start_time_of_event2 = altStarttime2;
					//getcat.start_time_of_event3 = altStarttime3;

					//getcat.end_time_of_event1 = altendtime1;
					//getcat.end_time_of_event2 = altendtime2;
					//getcat.end_time_of_event3 = altendtime3;

					getcat.child_id = childid;
					getcat.child_name = childname;
					getcat.child_profile_image = childpicurl;
					getcat.childfriend_name = friendname;
					getcat.date_ofevent = date;
					getcat.end_time_of_event = endtime;
					getcat.friend_id = friendid;
					getcat.friendprofile_image = friendpicurl;
					
					getcat.senderid = senderid;
					getcat.receiverid = receiverid;
					getcat.location_event = location;
					getcat.parent_name = parentname;
					getcat.notes_event = notes;
					getcat.parent_profile_image = parentpic;
					getcat.start_time_of_event = starttime;
					getcat.status_ofevent = status_ofevent;
					getcat.eventid = eventid;

					// getcat.senderid=sender_id;
					String date_of_event_with_time = null, date_of_event_with_end_time = null;
					try {
						String[] timeArr_start = starttime.split(":");
						String time_1 = timeArr_start[0];
						String time2 = timeArr_start[1].substring(0, 2);
						String[] timeArr_end = endtime.split(":");
						String time_1_end = timeArr_end[0];
						String time2_end = timeArr_end[1].substring(0, 2);

						int length_time = time_1.length();
						if (length_time == 1) {
							time_1 = "0" + time_1;
						}
						int length_time_end = time_1_end.length();
						if (length_time_end == 1) {
							time_1_end = "0" + time_1_end;
						}
						System.out.println(time_1);
						System.out.println(time2);
						System.out.println(time_1_end);
						System.out.println(time2_end);

						date_of_event_with_time = date + " " + time_1 + ":"
								+ time2;
						date_of_event_with_end_time = date + " " + time_1_end
								+ ":" + time2_end;
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						date_of_event_with_time = date + "" + starttime;
						date_of_event_with_end_time = date + " " + endtime;
					}

					if (status_ofevent.equalsIgnoreCase("accepted")) {
						DateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm");
						Date date_of_event = null;
						Date last_date_of_event = null;
						try {
							date_of_event = sdf.parse(date_of_event_with_time);
							last_date_of_event = sdf
									.parse(date_of_event_with_end_time);
						} catch (ParseException e) {

							e.printStackTrace();
						}
						int check_event_exist = db.getevent_id(eventid);
						if (check_event_exist == 1) {

						} else {
							db.insert_into_table(eventid);
							String eventUriString = "content://com.android.calendar/events";
							ContentValues eventValues = new ContentValues();

							eventValues.put("calendar_id", 1);
							long dtstart = date_of_event.getTime();
							long lastdate = last_date_of_event.getTime();
							eventValues.put("title", "Playdate " + childname);
							eventValues.put("description", childname
									+ " wants play with " + friendname);
							eventValues.put("eventLocation", location);
							eventValues.put("dtstart", dtstart);
							eventValues.put("dtend", lastdate);
							eventValues.put("eventStatus", "status"); // This
																		// information
																		// is
							eventValues.put("eventTimezone", "US");
							eventValues.put("hasAlarm", 1); // 0 for false, 1
															// for true

							Uri eventUri = getActivity().getContentResolver()
									.insert(Uri.parse(eventUriString),
											eventValues);
							long eventID = Long.parseLong(eventUri
									.getLastPathSegment());
						}
					}
					params.add(getcat);

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				check_null = true;
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO: handle exception
				check_null = true;
			}
			return null;
		}

		protected void onPostExecute(String resultt) {
			// dialog.dismiss();

			if (check_null) {
				try {
					list_home.setVisibility(View.GONE);
					int sdk = android.os.Build.VERSION.SDK_INT;
					if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						main_layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.tree_children_bg) );
					} else {
						main_layout.setBackground( getResources().getDrawable(R.drawable.tree_children_bg));
					}
					
					startarrange.setVisibility(View.VISIBLE);
					addchild.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
			} else {
				
				
				try {
					
				
				/*params.clear();*/

				if (params.size() > 0) {
					/*first_testview.setVisibility(View.GONE);
					second_textview.setVisibility(View.GONE);
					third_TextView.setVisibility(View.GONE);
					forth_textview.setVisibility(View.GONE);
					
				
					list_home.setVisibility(View.VISIBLE);*/
					adapter = new LazyAdapter(getActivity(), params);

					list_home.setAdapter(adapter);
				}else{
					list_home.setVisibility(View.GONE);
					int sdk = android.os.Build.VERSION.SDK_INT;
					if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						main_layout.setBackgroundDrawable( getResources().getDrawable(R.drawable.arrangement_bg) );
					
					} else {
						main_layout.setBackground( getResources().getDrawable(R.drawable.arrangement_bg));
					}
					
					
					startarrange.setVisibility(View.VISIBLE);
					addchild.setVisibility(View.VISIBLE);
					/*customMessageStr = GlobalVariable.custom_Jsonarray;
					if (customMessageStr != null && customMessageStr.length() > 0) {
						String first_msg = "", second_msg = "", third_msg = "", fourth_msg = "", fifth_msg = "";
						try {
							first_msg = customMessageStr.getString(0);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							second_msg = customMessageStr.getString(1);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							third_msg = customMessageStr.getString(2);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							fourth_msg = customMessageStr.getString(3);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							fifth_msg = customMessageStr.getString(4);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						first_testview.setVisibility(View.VISIBLE);
						second_textview.setVisibility(View.VISIBLE);
						third_TextView.setVisibility(View.VISIBLE);
						forth_textview.setVisibility(View.VISIBLE);
						third_TextView
								.setText("On the HOME screen you'll find all your playdate arrangements");
						forth_textview
								.setText("On this NOTIFICATION screen you'll find messages and updates from the JAGO team");
						first_testview.setText(first_msg);
						second_textview.setText(second_msg + "\n\n" + third_msg + "\n"
								+ fourth_msg + "\n" + fifth_msg);
					}
				*/}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			if ((dialog != null) && dialog.isShowing()) {
				dialog.dismiss();
			}

		}
	}

	public class LazyAdapter extends BaseAdapter {
		String _imgurlchild = "", _imgurlfriend = "", _imgparent = "";
		private Activity activity;
		private ArrayList<Getcatagory_forlist> _items;
		private LayoutInflater inflater = null;

		public com.playdate.contactlist.ImageLoader imageLoader;

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

			imageLoader = new com.playdate.contactlist.ImageLoader(getActivity());
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
			public TextView child_name, friend_name, date_odevent,
					time_start, parent_name, time_end;
			public ImageView _imageofchild, _imageoffriend,status,
					_imageofparent = null;

		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View vi = convertView;
			ViewHolder _holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_home_event, null);
				_holder = new ViewHolder();

				_holder.child_name = (TextView) convertView
						.findViewById(R.id.textView1_list_childname);
				_holder.friend_name = (TextView) convertView
						.findViewById(R.id.textView1_list_childfriend_name);
				_holder.parent_name = (TextView) convertView
						.findViewById(R.id.textView1_parent_name_list);

				_holder.date_odevent = (TextView) convertView
						.findViewById(R.id.textView3_dateofevent);
				_holder.status = (ImageView) convertView.findViewById(R.id.img4_status);
				_holder.time_start = (TextView) convertView
						.findViewById(R.id.textView2_time_ofevent);
				_holder.time_end = (TextView) convertView
						.findViewById(R.id.textView2_time_ofevent_end);
				_holder._imageofchild = (ImageView) convertView
						.findViewById(R.id.imageView1_list_child_pic);

				_holder._imageoffriend = (ImageView) convertView
						.findViewById(R.id.imageView1_childfriend_pic);
				_holder._imageofparent = (ImageView) convertView
						.findViewById(R.id.imageView1_paremt_pic_event);

				convertView.setTag(_holder);
			} else {
				_holder = (ViewHolder) convertView.getTag();
			}
			String name_of_child = _items.get(position).child_name;
			String name_of_childfriend = _items.get(position).childfriend_name;
			String name_of_childparent = _items.get(position).parent_name;
			String date_ofevent = _items.get(position).date_ofevent;

			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date_of_birth = null;
			try {
				date_of_birth = sdf.parse(date_ofevent);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// String reportDate = df.format(today);
				// birthDay=sdf.format(date_of_birth);
			DateFormat destDf = new SimpleDateFormat("EEEE, MMMM d, yy");
			String date_of_event1 = destDf.format(date_of_birth);

			String time_of_event = _items.get(position).start_time_of_event;
			String status = _items.get(position).status_ofevent;
			String end_time = _items.get(position).end_time_of_event;
			String status_caps = status.toUpperCase();

			if (status_caps.equalsIgnoreCase("ACCEPTED")) {
				_holder.status.setBackgroundResource(R.drawable.accepted);
			}
			if (status_caps.equalsIgnoreCase("PENDING")) {
				_holder.status.setBackgroundResource(R.drawable.pending);
			}
			if (status_caps.equalsIgnoreCase("REQUESTED")) {
				_holder.status.setBackgroundResource(R.drawable.pending);
			}

			String name_of_child_caps = name_of_child.toUpperCase();

			String parent_name = name_of_childparent.toUpperCase();
			String name_of_childfriend_caps = name_of_childfriend.toUpperCase();
			_holder.child_name.setText(name_of_child_caps);
			_holder.friend_name.setText(name_of_childfriend_caps);
			_holder.date_odevent.setText(date_of_event1);
			_holder.time_start.setText(time_of_event);
			_holder.time_end.setText(end_time);
			_holder.parent_name.setText(parent_name);
			
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			@SuppressWarnings("deprecation")
			int width = display.getWidth();
			@SuppressWarnings("deprecation")
			int height = display.getHeight();

			float mReqPercentages = 22;
			float mCalculatedWidth = (mReqPercentages / 100) * width;

			_holder.child_name.getLayoutParams().width = (int) (mCalculatedWidth );
			_holder.friend_name.getLayoutParams().width = (int) (mCalculatedWidth );
			_holder.parent_name.getLayoutParams().width = (int) (mCalculatedWidth );

			_imgurlchild = _items.get(position).child_profile_image;

			_imgurlfriend = _items.get(position).friendprofile_image;
			_imgparent = _items.get(position).parent_profile_image;

			_holder._imageofchild.setTag(_imgurlchild);
			_holder._imageoffriend.setTag(_imgurlfriend);
			_holder._imageofparent.setTag(_imgparent);

			imageLoader.DisplayImage(_imgurlchild, _holder._imageofchild);
			imageLoader.DisplayImage(_imgurlfriend, _holder._imageoffriend);
			imageLoader.DisplayImage(_imgparent, _holder._imageofparent);

			_holder._imageofchild.requestLayout();
			_holder._imageoffriend.requestLayout();
			_holder._imageofparent.requestLayout();
			
			float mReqPercentages1 = 17;
			float mCalculatedWidth1 = (mReqPercentages1 / 100) * width;

			
			_holder._imageofchild.getLayoutParams().height = (int) (mCalculatedWidth1 );
			_holder._imageofchild.getLayoutParams().width = (int) (mCalculatedWidth1 );
			_holder.status.getLayoutParams().height = (int) (mCalculatedWidth1 );
			_holder.status.getLayoutParams().width = (int) (mCalculatedWidth1 );
			_holder._imageoffriend.getLayoutParams().height = (int) (mCalculatedWidth1 );
			_holder._imageoffriend.getLayoutParams().width = (int) (mCalculatedWidth1 );
			_holder._imageofparent.getLayoutParams().height = (int) (mCalculatedWidth1 );
			_holder._imageofparent.getLayoutParams().width = (int) (mCalculatedWidth1 );

			
			
		/*	int density = getResources().getDisplayMetrics().densityDpi;
			switch (density) {
			case DisplayMetrics.DENSITY_LOW:
				_holder._imageofchild.getLayoutParams().height = 40;
				_holder._imageofchild.getLayoutParams().width = 40;
				_holder.status.getLayoutParams().height = 40;
				_holder.status.getLayoutParams().width = 40;
				_holder._imageoffriend.getLayoutParams().height = 40;
				_holder._imageoffriend.getLayoutParams().width = 40;
				_holder._imageofparent.getLayoutParams().height = 40;
				_holder._imageofparent.getLayoutParams().width = 40;

				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				_holder._imageofchild.getLayoutParams().height = 70;
				_holder._imageofchild.getLayoutParams().width = 70;
				_holder._imageoffriend.getLayoutParams().height = 70;
				_holder._imageoffriend.getLayoutParams().width = 70;
				_holder._imageofparent.getLayoutParams().height = 70;
				_holder._imageofparent.getLayoutParams().width = 70;
				_holder.status.getLayoutParams().height = 70;
				_holder.status.getLayoutParams().width = 70;
				// child_list.setLayoutParams(new LayoutParams(90,90));
				break;
			case DisplayMetrics.DENSITY_HIGH:
				_holder._imageofchild.getLayoutParams().height = 75;
				_holder._imageofchild.getLayoutParams().width = 75;
				_holder._imageoffriend.getLayoutParams().height = 75;
				_holder._imageoffriend.getLayoutParams().width = 75;
				_holder._imageofparent.getLayoutParams().height = 75;
				_holder._imageofparent.getLayoutParams().width = 75;
				_holder.status.getLayoutParams().height = 75;
				_holder.status.getLayoutParams().width = 75;
				// child_list.setLayoutParams(new LayoutParams(110,110));
				break;
			case DisplayMetrics.DENSITY_XHIGH:
				_holder._imageofchild.getLayoutParams().height = 120;
				_holder._imageofchild.getLayoutParams().width = 120;
				_holder._imageoffriend.getLayoutParams().height = 120;
				_holder._imageoffriend.getLayoutParams().width = 120;
				_holder._imageofparent.getLayoutParams().height = 120;
				_holder._imageofparent.getLayoutParams().width = 120;
				_holder.status.getLayoutParams().height = 120;
				_holder.status.getLayoutParams().width = 120;
				break;
			case DisplayMetrics.DENSITY_XXHIGH:
				_holder._imageofchild.getLayoutParams().height = 120;
				_holder._imageofchild.getLayoutParams().width = 120;
				_holder._imageoffriend.getLayoutParams().height = 120;
				_holder._imageoffriend.getLayoutParams().width = 120;
				_holder._imageofparent.getLayoutParams().height = 120;
				_holder._imageofparent.getLayoutParams().width = 120;
				_holder.status.getLayoutParams().height = 120;
				_holder.status.getLayoutParams().width = 120;
				break;
			}
*/
			return convertView;
		}

	}
	// Create new fragment and transaction

}
