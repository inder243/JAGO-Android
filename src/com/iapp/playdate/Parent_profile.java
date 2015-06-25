package com.iapp.playdate;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iapp.playdate.R;
import com.iapptechnologies.time.util.Constants;

public class Parent_profile extends android.support.v4.app.Fragment {
	
	boolean firstTime=true;
	ImageView img, img1;
	
	SharedPreferences.Editor editor;
	Bitmap bitmap;
	Float x1, y1, x2, y2;
	int RESULT_LOAD_IMAGE = 1, dayselect = 0,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 999;
	private ImageLoader imgLoader;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	Bitmap imageData;
	private String strURL;
	String fb_id;
	String name, dob, location, freetime, url, guardiantype, firstname,lastname, userguardiantype, userfreetime, userphone_no;
	EditText parentdob, parentlocation, parenttype, phoneNo;
	EditText txt_name;
	ListView child_list;
	ArrayList<Getcategory> parentItems;
	Button btn_update, addmore_freetime,addchild;
	TextView txt_free_time;
	private int myYear, myMonth, myDay;
	String days = "", daysadd = "", time;
	int hours, minutes,count_alert=0;
	String date_comparision,parent_location, parent_dob, parent_parentFreetime,parent_phonenumber, parent_name, parent_firstname = "",parent_lastname = "", urlreturned, userfirstname, userlocation,userdob;
	SharedPreferences settings;
	boolean checkk = false;
	boolean booleancheck = false;
	
	String daymon, daytue, daywed, daythu, dayfri, daysat, daysun,user_guardian_id, date_od_birth_parent, facebook_friends,phone_number;
	boolean childinfo = true, clicked = false;
	private Uri fileUri;
	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
	public static final int MEDIA_TYPE_IMAGE = 1;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	private boolean imgCapFlag = false;
	protected boolean taken;
	protected static final String PHOTO_TAKEN = "photo_taken";
	protected String path;
	
	com.iapptechnologies.time.util.ImageLoader imgLoaderParnt;
	//Bitmap bitmap;
	public Parent_profile() {

	}
@Override
public void onResume() {
	// TODO Auto-generated method stub
	if (childinfo == true) {
		cd = new ConnectionDetector(getActivity());
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
			new chilprofileshow_webservice().execute();

		} else {
			Toast.makeText(getActivity(),
					"Please check internet connection", 2000).show();

		}

	}
	super.onResume();
}
	@Override
	public View onCreateView(final LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.parent_profile,container, false);
		
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());
		settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
		editor = settings.edit();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		date_comparision = df.format(c.getTime());
		Home.menu.setVisibility(View.GONE);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		parentdob = (EditText) view.findViewById(R.id.textView_dob_parent);
		//txt_free_time = (TextView) view.findViewById(R.id.textView3212);
		parentlocation = (EditText) view.findViewById(R.id.textView_location_parent);
		img = (ImageView) view.findViewById(R.id.imageView_parent);
		parenttype = (EditText) view.findViewById(R.id.textView_user_parent);
		txt_name = (EditText) view.findViewById(R.id.parent_parentname);
		child_list = (ListView) view.findViewById(R.id.listView_parent_profile_child);
		btn_update = (Button) view.findViewById(R.id.button_save_parentupdate);
		addchild=(Button)view.findViewById(R.id.add_child);
		// addmore_freetime=(Button)view.findViewById(R.id.button1_add_moredays_parent);
		phoneNo = (EditText) view.findViewById(R.id.textView_parent_phone_no);
		child_list.setDivider(null);
		child_list.setDividerHeight(0);
		if(GlobalVariable.coming_from_child.equals("1")){
			GlobalVariable.coming_from_child="0";
		try {
			name = getArguments().getString("name");
			user_guardian_id = getArguments().getString("user_guardian_id");
			firstname = getArguments().getString("firstname");
			fb_id = getArguments().getString("facebook_id");
			dob = getArguments().getString("dob");
			location = getArguments().getString("location");
			//freetime = getArguments().getString("freetime");
			facebook_friends = getArguments().getString("facebook_friends");
			strURL = getArguments().getString("url");
			guardiantype = getArguments().getString("guardiantype");
			phone_number = getArguments().getString("phone");
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		}else{
		name=settings.getString("NAME","");
		strURL=settings.getString("URLPROFILE","");
		user_guardian_id=settings.getString("USERID","");
		firstname=settings.getString("FIRSTNAME","");
		fb_id=settings.getString("FACEBOOKID","");
		dob=settings.getString("DOB","");
		guardiantype=settings.getString("GURTYPE","");
		location=settings.getString("LOCATION","");
		facebook_friends=settings.getString("FBFRIEND","");
		phone_number=settings.getString("PHONE","");
		
		}
		try {
			name = name.toUpperCase();
		} catch (Exception e) {
		}
        try {
         location = location.toUpperCase();	
		} catch (Exception e) {
		}

        try {
        phoneNo.setText(phone_number);
        } catch (Exception e) {
               }
		try {
		txt_name.setText(name);
		} catch (Exception e) {
		}
		try {
		parentlocation.setText(location);
		} catch (Exception e) {
		}
		try {
			parentdob.setText(dob);
		} catch (Exception e) {
		}
		
      
		
	/*	//settings = getActivity().getSharedPreferences("MyPreferencesFiles", 0);
		if(GlobalVariable.pref==1){
			booleancheck = settings.getBoolean("checkk", checkk);
			if (booleancheck == true) {
				userfirstname = settings.getString("userfirstname", "");
				userlocation = settings.getString("userlocation", "");
				userdob = settings.getString("userdob", "");
				//userfreetime = settings.getString("userfreetime", "");
				userphone_no = settings.getString("userphone", "");
				guardiantype=settings.getString("userguardiantype", "");
				userfirstname = userfirstname.toUpperCase();
				userlocation = userlocation.toUpperCase();
				userdob = userdob.toUpperCase();
				userfreetime = userfreetime.toUpperCase();
				userphone_no = userphone_no.toUpperCase();
	            txt_name.setText(userfirstname);
				parentlocation.setText(userlocation);
				parentdob.setText(userdob);
				// parentfreetime.setText(userfreetime);
				phoneNo.setText(userphone_no);
                parenttype.setText(guardiantype);
				System.out.println("entered into parent class");
			} else {
				try {
					name = name.toUpperCase();
				} catch (Exception e) {
					// TODO: handle exception
				}
try {
	location = location.toUpperCase();	
				} catch (Exception e) {
					// TODO: handle exception
				}
try {
	freetime = freetime.toUpperCase();
} catch (Exception e) {
	// TODO: handle exception
}
try {
	phoneNo.setText(phone_number);
} catch (Exception e) {
	// TODO: handle exception
}
try {
	  txt_name.setText(name);
} catch (Exception e) {
	// TODO: handle exception
}
try {
	parentlocation.setText(location);
} catch (Exception e) {
	// TODO: handle exception
}

				
				
				try {
					parentdob.setText(dob);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
	          
			
				
				//guardiantype
				// parentfreetime.setText(freetime);
			}
		}
		else{
			
			name = name.toUpperCase();
			try {
				location = location.toUpperCase();
				freetime = freetime.toUpperCase();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			phoneNo.setText(phone_number);
	        txt_name.setText(name);
			parentlocation.setText(location);
			parentdob.setText(dob);
			GlobalVariable.pref=1;
		}
		*/
		
		

		final Home home = new Home();
try {
	if (guardiantype.equalsIgnoreCase("f")||guardiantype.equalsIgnoreCase("male")) {
		guardiantype = "FATHER";
	}else if(guardiantype.equalsIgnoreCase("m")||guardiantype.equalsIgnoreCase("female")) {
		guardiantype = "MOTHER";
	}else if(guardiantype.equalsIgnoreCase("b")) {
		guardiantype = "BROTHER";
	}
	else if(guardiantype.equalsIgnoreCase("s")) {
		guardiantype = "SISTER";
	}
	else if(guardiantype.equalsIgnoreCase("n")) {
		guardiantype = "NANNY";
	}
	else if(guardiantype.equalsIgnoreCase("gm")) {
		guardiantype = "GRAND MOTHER";
	}
	else if(guardiantype.equalsIgnoreCase("GF")) {
		guardiantype = "GRAND FATHER";
	}
	else if(guardiantype.equalsIgnoreCase("T")) {
		guardiantype = "TEACHER";
	}
	else if(guardiantype.equalsIgnoreCase("o")) {
		guardiantype = "OTHER";
	}
} catch (Exception e) {
	// TODO: handle exception
}

		parenttype.setText(guardiantype);
		parenttype.setFocusable(false);
		parentdob.setFocusable(false);
		parentdob.setClickable(true);
		parenttype.setClickable(true);
		// parentfreetime.setFocusable(false);
		// parentfreetime.setClickable(true);
		if (GlobalVariable.guardian_Id.equals(user_guardian_id)) {

		} else {
			
			txt_name.setEnabled(false);
			parentlocation.setEnabled(false);
			parentdob.setEnabled(false);
			// parentfreetime.setEnabled(false);
			phoneNo.setEnabled(false);
			parentdob.setClickable(false);
			// parentfreetime.setClickable(false);
			img.setClickable(false);
			parenttype.setEnabled(false);
			parenttype.setClickable(false);
			btn_update.setVisibility(View.GONE);
			// addmore_freetime.setVisibility(View.GONE);
		}
		final String relation_array[]={"FATHER","MOTHER","GRAND FATHER","GRAND MOTHER","NANNY","BROTHER","SISTER","TEACHER","OTHER"};
		parenttype.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			    builder.setTitle("RELATION")
			           .setItems(relation_array, new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int which) {
			            	   guardiantype=relation_array[which];
			               parenttype.setText(guardiantype);
			           }
			    });
			    builder.create().show();
			}
		});
		
		
		addchild.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			//	Bundle bundle = new Bundle();
			//	bundle.putString("name", name);
			//	bundle.putString("url", url);
				//bundle.putString("freetime", freetime);
			//	bundle.putString("dob", dob);
			//	bundle.putString("guardiantype", guardiantype);
			//	bundle.putString("location", location);
			//	bundle.putString("firstname", firstname);
			//	bundle.putString("facebook_id", fb_id);
			//	bundle.putString("user_guardian_id", user_guardian_id);
			//	bundle.putString("facebook_friends", facebook_friends);
			//	bundle.putString("phone", phone_number);
				
				android.support.v4.app.Fragment fragment = new Add_Child();
			//	fragment.setArguments(bundle);
				
				android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.addToBackStack("first");
				fragmentTransaction.commit();
				
				
			}
		});
		
		parentdob.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
			    switch(event.getAction())
	            {
	            case MotionEvent.ACTION_DOWN :
	            	count_alert=0;
					Calendar c = Calendar.getInstance();

					myYear = c.get(Calendar.YEAR);
					myMonth = c.get(Calendar.MONTH);
					myDay = c.get(Calendar.DAY_OF_MONTH);

					try {
						String date_from_edit_text = parentdob.getText().toString();
						
						SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
						// SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yy");
						 Date date_1=null,date_2=null,date_3=null,date_4=null;
						 try {
							 date_1=sdf.parse(date_from_edit_text);
							
							 
							 
						} catch (ParseException e) {
							
							e.printStackTrace();
						}
						 SimpleDateFormat destDf = new SimpleDateFormat("yyyy-MM-dd");
						
						               
					
						             // format the date into another format
						try {
							date_from_edit_text = destDf.format(date_1);
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						if (date_from_edit_text.equals("")
								|| date_from_edit_text.equals(null)) {

						} else {
							String[] dateArr = date_from_edit_text.split("-");

							myDay = Integer.parseInt(dateArr[2]);
							myMonth = Integer.parseInt(dateArr[1]) - 1;
							myYear = Integer.parseInt(dateArr[0]);
						}

					} catch (Exception e) {
					
					}
					
						DatePickerDialog d = new DatePickerDialog(getActivity(),mDateSetListener, myYear, myMonth, myDay);
						d.show();
	                break;
	            case MotionEvent.ACTION_UP  :
	                break;
	            }
				return true;
			}
		});
		
	
//		parentdob.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				count_alert=0;
//				Calendar c = Calendar.getInstance();
//
//				myYear = c.get(Calendar.YEAR);
//				myMonth = c.get(Calendar.MONTH);
//				myDay = c.get(Calendar.DAY_OF_MONTH);
//
//				try {
//					String date_from_edit_text = parentdob.getText().toString();
//					if (date_from_edit_text.equals("")
//							|| date_from_edit_text.equals(null)) {
//
//					} else {
//						String[] dateArr = date_from_edit_text.split("-");
//
//						myDay = Integer.parseInt(dateArr[0]);
//						myMonth = Integer.parseInt(dateArr[1]) - 1;
//						myYear = Integer.parseInt(dateArr[2]);
//					}
//
//				} catch (Exception e) {
//				
//				}
//				firstTime=true;
//				if(firstTime==true)
//				{
//					DatePickerDialog d = new DatePickerDialog(getActivity(),mDateSetListener, myYear, myMonth, myDay);
//					d.show();
//					firstTime=false;
//				}else
//				{
//					
//				}
//			}
//		});

		/*
		 * parentfreetime.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * hours = new Time(System.currentTimeMillis()).getHours(); minutes =
		 * new Time(System.currentTimeMillis()).getMinutes();
		 * 
		 * LinearLayout viewGroup = (LinearLayout) getActivity()
		 * .findViewById(R.id.weekdays_selection);
		 * System.out.println("view Started11");
		 * 
		 * 
		 * System.out.println("view Started22"); View layout =
		 * inflater.inflate(R.layout.weekdays, viewGroup);
		 * System.out.println("view Started33");
		 * 
		 * final PopupWindow popup = new PopupWindow(getActivity());
		 * popup.setContentView(layout);
		 * 
		 * 
		 * popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		 * popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
		 * popup.setFocusable(true);
		 * 
		 * 
		 * int OFFSET_X = 0; int OFFSET_Y = 0;
		 * 
		 * popup.setBackgroundDrawable(new BitmapDrawable());
		 * 
		 * popup.showAtLocation(getView(), Gravity.NO_GRAVITY, OFFSET_X,
		 * OFFSET_Y);
		 * 
		 * 
		 * 
		 * 
		 * RelativeLayout monday = (RelativeLayout) layout
		 * .findViewById(R.id.Monday); monday.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub dayselect=1; clicked=true;
		 * 
		 * 
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenermonto, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show(); TimePickerDialog tiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenermon, hours, minutes,
		 * true); tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * popup.dismiss();
		 * 
		 * } }); RelativeLayout tuesday = (RelativeLayout) layout
		 * .findViewById(R.id.Tuesday); tuesday.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub dayselect=2; clicked=true;
		 * 
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenertueto, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show(); TimePickerDialog tiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenertue, hours, minutes,
		 * true); tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * popup.dismiss();
		 * 
		 * } }); RelativeLayout Wednesday = (RelativeLayout) layout
		 * .findViewById(R.id.Wednesday); Wednesday.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub dayselect=3;
		 * 
		 * clicked=true;
		 * 
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenerwedto, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show(); TimePickerDialog tiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenerwed, hours, minutes,
		 * true); tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * popup.dismiss();
		 * 
		 * } }); RelativeLayout Thursday = (RelativeLayout) layout
		 * .findViewById(R.id.Thursday); Thursday.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub dayselect=4;
		 * 
		 * clicked=true;
		 * 
		 * 
		 * 
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenerthuto, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show(); TimePickerDialog tiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenerthu, hours, minutes,
		 * true); tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * popup.dismiss(); } }); RelativeLayout Friday = (RelativeLayout)
		 * layout .findViewById(R.id.Friday); Friday.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub dayselect=5; clicked=true;
		 * 
		 * 
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenerfrito, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show(); TimePickerDialog tiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenerfri, hours, minutes,
		 * true);
		 * 
		 * tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * popup.dismiss();
		 * 
		 * } }); RelativeLayout Saturday = (RelativeLayout) layout
		 * .findViewById(R.id.Saturday); Saturday.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub dayselect=6;
		 * 
		 * clicked=true;
		 * 
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenersatto, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show(); TimePickerDialog tiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenersat, hours, minutes,
		 * true); tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * popup.dismiss(); } }); RelativeLayout Sunday = (RelativeLayout)
		 * layout .findViewById(R.id.Sunday); Sunday.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub dayselect=7;
		 * 
		 * clicked=true;
		 * 
		 * 
		 * 
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenersunto, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show(); TimePickerDialog tiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenersun, hours, minutes,
		 * true); tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * popup.dismiss(); } });
		 * 
		 * } });
		 * 
		 * addmore_freetime.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub hours = new Time(System.currentTimeMillis()).getHours(); minutes
		 * = new Time(System.currentTimeMillis()).getMinutes();
		 * 
		 * LinearLayout viewGroup = (LinearLayout) getActivity()
		 * .findViewById(R.id.weekdays_selection);
		 * System.out.println("view Started11");
		 * 
		 * 
		 * System.out.println("view Started22"); View layout =
		 * inflater.inflate(R.layout.weekdays, viewGroup);
		 * System.out.println("view Started33");
		 * 
		 * final PopupWindow popup = new PopupWindow(getActivity());
		 * popup.setContentView(layout);
		 * 
		 * 
		 * popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		 * popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
		 * popup.setFocusable(true);
		 * 
		 * 
		 * int OFFSET_X = 0; int OFFSET_Y = 0;
		 * 
		 * popup.setBackgroundDrawable(new BitmapDrawable());
		 * 
		 * popup.showAtLocation(getView(), Gravity.NO_GRAVITY, OFFSET_X,
		 * OFFSET_Y);
		 * 
		 * 
		 * 
		 * 
		 * RelativeLayout monday = (RelativeLayout) layout
		 * .findViewById(R.id.Monday); monday.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub // dayselect=1; clicked=true;
		 * 
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenermonto, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show(); TimePickerDialog tiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenermon, hours, minutes,
		 * true); tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * 
		 * popup.dismiss();
		 * 
		 * } }); RelativeLayout tuesday = (RelativeLayout) layout
		 * .findViewById(R.id.Tuesday); tuesday.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub //dayselect=2; clicked=true;
		 * 
		 * 
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenertueto, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show(); TimePickerDialog tiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenertue, hours, minutes,
		 * true); tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * 
		 * popup.dismiss(); } }); RelativeLayout Wednesday = (RelativeLayout)
		 * layout .findViewById(R.id.Wednesday);
		 * Wednesday.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub //dayselect=3; clicked=true;
		 * 
		 * 
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenerwedto, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show(); TimePickerDialog tiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenerwed, hours, minutes,
		 * true); tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * popup.dismiss();
		 * 
		 * 
		 * } }); RelativeLayout Thursday = (RelativeLayout) layout
		 * .findViewById(R.id.Thursday); Thursday.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub //dayselect=4; clicked=true;
		 * 
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenerthuto, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show(); TimePickerDialog tiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenerthu, hours, minutes,
		 * true); tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * 
		 * popup.dismiss();
		 * 
		 * 
		 * } }); RelativeLayout Friday = (RelativeLayout) layout
		 * .findViewById(R.id.Friday); Friday.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub //dayselect=5; clicked=true;
		 * 
		 * 
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenerfrito, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show(); TimePickerDialog tiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenerfri, hours, minutes,
		 * true);
		 * 
		 * tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * popup.dismiss();
		 * 
		 * 
		 * } }); RelativeLayout Saturday = (RelativeLayout) layout
		 * .findViewById(R.id.Saturday); Saturday.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub //dayselect=6; clicked=true;
		 * 
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenersatto, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show(); TimePickerDialog tiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenersat, hours, minutes,
		 * true); tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * System.out.println(timechk6); popup.dismiss();
		 * 
		 * 
		 * } }); RelativeLayout Sunday = (RelativeLayout) layout
		 * .findViewById(R.id.Sunday); Sunday.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { clicked=true;
		 * TimePickerDialog tiiPickerDialog = new
		 * TimePickerDialog(getActivity(), mTimesetlistenersunto, hours,
		 * minutes, true); tiiPickerDialog.setTitle("TO");
		 * tiiPickerDialog.show();
		 * 
		 * TimePickerDialog tiPickerDialog = new TimePickerDialog(getActivity(),
		 * mTimesetlistenersun, hours, minutes, true);
		 * tiPickerDialog.setTitle("FROM"); tiPickerDialog.show();
		 * System.out.println(timechk7); popup.dismiss(); } });
		 * 
		 * 
		 * 
		 * } });
		 */

//		imgLoader = new ImageLoader(getActivity());
//		// new Login_webservice().execute();
//		bitmap = imgLoader.DisplayImage(strURL, img);

         imgLoaderParnt = new com.iapptechnologies.time.util.ImageLoader(getActivity());
         imgLoaderParnt.DisplayImage(strURL, img);
		img.requestLayout();
		child_list.requestLayout();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		
		int height = display.getHeight();
		int width = display.getWidth();
		
		System.out.println(height  +  "   "+width);
		
		float mReqPercentages = 35;
		float mCalculatedWidth = (mReqPercentages / 100) * width;

		float mReqPercentages1 = 50;
		float mCalculatedWidth1 = (mReqPercentages1 / 100) * width;

		
		img.getLayoutParams().height = (int) (mCalculatedWidth);
		img.getLayoutParams().width = (int) mCalculatedWidth;
		child_list.getLayoutParams().height = (int)mCalculatedWidth;
		child_list.getLayoutParams().width = (int)mCalculatedWidth1;
		
		/*int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_LOW:
			img.getLayoutParams().height = 100;
			img.getLayoutParams().width = 100;

			child_list.getLayoutParams().height = 100;

			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			img.getLayoutParams().height = 200;
			img.getLayoutParams().width = 180;
			child_list.getLayoutParams().height = 200;

			break;
		case DisplayMetrics.DENSITY_HIGH:
			img.getLayoutParams().height = 200;
			img.getLayoutParams().width = 180;
			child_list.getLayoutParams().height = 200;

			break;
		case DisplayMetrics.DENSITY_XHIGH:
			img.getLayoutParams().height = 250;
			img.getLayoutParams().width = 250;
			child_list.getLayoutParams().height = 250;

			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			img.getLayoutParams().height = 250;
			img.getLayoutParams().width = 250;
			child_list.getLayoutParams().height = 250;
			// android:src="@drawable/placeholder_large"
			break;
		}*/
		/*Drawable drawing = img.getDrawable();
	    if (drawing == null) {
	        //return; // Checking for null & return, as suggested in comments
	    }
	    Bitmap sourceBitmap = ((BitmapDrawable)drawing).getBitmap();
		Bitmap bmp=addWhiteBorder(sourceBitmap,50);
		img.setImageBitmap(bmp);*/
		System.out.println("fb_id" + fb_id);

		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new AlertDialog.Builder(getActivity())
						.setTitle("Select Picture")
						.setMessage("Complete action using")
						.setPositiveButton("Gallery",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// continue with delete
										final Intent intent = new Intent(
												Intent.ACTION_GET_CONTENT);
										intent.setType("image/*");
										/*
										 * intent.setFlags(Intent.
										 * FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
										 */
										getActivity().startActivityForResult(
												intent, RESULT_LOAD_IMAGE);
									}
								})
						.setNegativeButton("Camera",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										/*String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
												Locale.getDefault()).format(new Date());
										path = Environment.getExternalStorageDirectory() + "/IMG"
												+ timeStamp + ".jpg";
										startCameraActivity();*/
										
										GlobalVariable.parent_picute_update = 1;
										Intent it = new Intent(getActivity(),
												Home.class);
									//	String ph_number = phoneNo.getText()
									//			.toString();

									//	it.putExtra("url", strURL);
								//		it.putExtra("name", name);
								//		it.putExtra("firstname", firstname);
								//		it.putExtra("iD", fb_id);
								//		it.putExtra("location", location);
								//		it.putExtra("dob", dob);
								//		it.putExtra("guardiantype",
								//				guardiantype);
									//	it.putExtra("freetime", freetime);
								//		it.putExtra("user_guardian_id",
								//				user_guardian_id);
								//		it.putExtra("phone", ph_number);
								//		it.putExtra("facebook_friends",
								//				facebook_friends);
										getActivity().startActivity(it);
									}
								}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();

			}
		});

		child_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ArrayList<Getcategory> _items;
				_items = parentItems;
				String parent_name = txt_name.getText().toString();
				String date_of_birth_of_parent = parentdob.getText().toString();
				String parent_type = parenttype.getText().toString();
				if (parent_type.equals("") || parent_type.equals(null)) {
					parent_type = "";
				}else if(parent_type.equalsIgnoreCase("FATHER")){
					guardiantype="f";
				}else if(parent_type.equalsIgnoreCase("MOTHER")){
					guardiantype="m";
				}else if(parent_type.equalsIgnoreCase("BROTHER")){
					parent_type="b";
				}else if(guardiantype.equalsIgnoreCase("SISTER")){
					parent_type="s";
				}else if(guardiantype.equalsIgnoreCase("NANNY")){
					parent_type="n";
				}else if(guardiantype.equalsIgnoreCase("GRAND FATHER")){
					parent_type="gf";
				}else if(guardiantype.equalsIgnoreCase("GRAND MOTHER")){
					parent_type="gm";
				}else if(guardiantype.equalsIgnoreCase("TEACHER")){
					parent_type="t";
				}
				else if(guardiantype.equalsIgnoreCase("OTHER")){
					parent_type="o";
				}
				String parent_location = parentlocation.getText().toString();
				// String parent_freetime=parentfreetime.getText().toString();

				String ph_number = phoneNo.getText().toString();

				String child_name = _items.get(arg2).child_name;
				String Child_id = _items.get(arg2).child_id;
				String Child_profilepic = _items.get(arg2).profile_image;
				String dob = _items.get(arg2).date_of_birth;
				//String conditions = _items.get(arg2).condition_child;
				//String hobbies = _items.get(arg2).hobbies_child;
				String allergies = _items.get(arg2).allergies_child;
				//String school = _items.get(arg2).school_child;
				//String youthclub = _items.get(arg2).youthclub_child;
				//String free_time = _items.get(arg2).free_time;
				if(GlobalVariable.show_unlink_button){
				if(_items.get(arg2).guardian_identity.equals(GlobalVariable.guardian_Id)){
					
				}else{
					GlobalVariable.appers_unlink_button=true;
				}
				}
                
				System.out.println("Child_idChild_id>>>>>>>>>>>" + Child_id);

				Bundle bundle = new Bundle();
				bundle.putString("child_name", child_name);
				bundle.putString("Child_id", Child_id);
				bundle.putString("Child_profilepic", Child_profilepic);
				bundle.putString("dob", dob);
				//bundle.putString("conditions", conditions);
				//bundle.putString("hobbies", hobbies);
				bundle.putString("Child_guardianId", user_guardian_id);
				bundle.putString("allergies", allergies);
				//bundle.putString("school", school);
				//bundle.putString("youthclub", youthclub);
				//bundle.putString("free_time", free_time);
			//	bundle.putString("parent_name", parent_name);
			//	bundle.putString("parent_profilepic", strURL);
			//	bundle.putString("parent_freetime", "");
		//		bundle.putString("phone", ph_number);
		//		bundle.putString("parent_dob", date_of_birth_of_parent);
		//		bundle.putString("parent_type", parent_type);
		//		bundle.putString("parent_location", parent_location);
			//	bundle.putString("parent_fbid", fb_id);
			//	bundle.putString("facebook_friends", facebook_friends);
//
		//		System.out.println("================" + userfirstname);
		//		System.out.println("================" + strURL);

				android.support.v4.app.Fragment fragment = new Child_profile();
				fragment.setArguments(bundle);
				
				android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.addToBackStack("first");
				fragmentTransaction.commit();
				
				/*fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();*/
				
				
				/*FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

				Fragment myFragment = new SecondSelectFishList(navigationBackButton);
				Bundle b = new Bundle();
				b.putString("id", idOfImage);
				myFragment.setArguments(b);
				fragmentTransaction.replace(R.id.frame_container, myFragment);
				fragmentTransaction.addToBackStack("first");
				fragmentTransaction.commit();*/
			}
		});
		btn_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (/*
					 * parentlocation.getText().toString().length()>0 &&
					 * parentdob.getText().toString().length()>0 &&
					 * parentfreetime.getText().toString().length()>0 &&
					 */txt_name.getText().toString().length() > 0) {
                    guardiantype=parenttype.getText().toString();
                   // parent_dob = parentdob.getText().toString();
					if (guardiantype.equals("") || guardiantype.equals(null)) {
						guardiantype = "";
					}else if(guardiantype.equalsIgnoreCase("FATHER")){
						guardiantype="f";
					}else if(guardiantype.equalsIgnoreCase("MOTHER")){
						guardiantype="m";
					}else if(guardiantype.equalsIgnoreCase("BROTHER")){
						guardiantype="b";
					}else if(guardiantype.equalsIgnoreCase("SISTER")){
						guardiantype="s";
					}else if(guardiantype.equalsIgnoreCase("NANNY")){
						guardiantype="n";
					}else if(guardiantype.equalsIgnoreCase("GRAND FATHER")){
						guardiantype="gf";
					}else if(guardiantype.equalsIgnoreCase("GRAND MOTHER")){
						guardiantype="gm";
					}
					else if(guardiantype.equalsIgnoreCase("TEACHER")){
						guardiantype="t";
					}else if(guardiantype.equalsIgnoreCase("OTHER")){
						guardiantype="o";
					}
					

					
					
					
					
					parent_location = parentlocation.getText().toString();
					if (parent_location.equals("")
							|| parent_location.equals(null)) {
						parent_location = "";
					}
					parent_dob = parentdob.getText().toString();
					if (parent_dob.equals("") || parent_dob.equals(null)) {
						parent_dob = "";
					}

					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
					Date date_of_birth = null;
					try {
						date_of_birth = sdf.parse(parent_dob);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}// String reportDate = df.format(today);
						// birthDay=sdf.format(date_of_birth);
					SimpleDateFormat destDf = new SimpleDateFormat("yyyy-MM-dd");

					// format the date into another format
try {
	date_od_birth_parent = destDf.format(date_of_birth);
} catch (Exception e) {
	// TODO: handle exception
}
					

					/*
					 * parent_parentFreetime=parentfreetime.getText().toString();
					 * if
					 * (parent_parentFreetime.equals("")||parent_parentFreetime
					 * .equals(null)){ parent_parentFreetime=""; }
					 */
					parent_phonenumber = phoneNo.getText().toString();
					parent_name = txt_name.getText().toString();
					if (parent_phonenumber.equals("")
							|| parent_phonenumber.equals(null)) {
						parent_phonenumber = "";
					}

					/*
					 * String[] items = parent_name.split(" "); for (String item
					 * : items) { firstname=item; System.out.println("item = " +
					 * item); }
					 */
					// firstname=parent_name.substring(0,);

					if (isInternetPresent) {
						new parent_detail_update().execute();

					} else {
						Toast.makeText(getActivity(),
								"Please check internet connection", 2000)
								.show();

					}

					// http://54.191.67.152/playdate/guardian_edit.php?guardian_id=1&firstname=sunny&lastname=singh&dob=1989-07-07&location=aa&phone=9872742345&facebook_id=123
				}

			}
		});

		RelativeLayout layout = (RelativeLayout) view
				.findViewById(R.id.parent_profile_layout);// get your root
															// layout
		layout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent touchevent) {
				Log.v(null, "TOUCH EVENT"); // handle your fragment number here
				switch (touchevent.getAction()) {
				// when user first touches the screen we get x and y coordinate
				case MotionEvent.ACTION_DOWN: {
					System.out.println("fffffffffffffffffffffffff");
					x1 = touchevent.getX();
					y1 = touchevent.getY();
					break;
				}
				case MotionEvent.ACTION_UP: {
					System.out.println("fffffffffffffffffffffffff");
					x2 = touchevent.getX();
					y2 = touchevent.getY();

					if (x1 > x2) {
						System.out.println("right to left swipe");
						home.clickeventimplementfragment();
					}

					break;
				}
				}
				return true;
			}
		});

		return view;
	}

	private Bitmap addWhiteBorder(Bitmap bmp, int borderSize) {
	    Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
	    Canvas canvas = new Canvas(bmpWithBorder);
	    canvas.drawColor(Color.WHITE);
	    canvas.drawBitmap(bmp, borderSize, borderSize, null);
	    return bmpWithBorder;
	}
	
	private void setsizeofimage() {
		// TODO Auto-generated method stub
		img.requestLayout();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		
		int height = display.getHeight();
		int width = display.getWidth();
		
		System.out.println(height  +  "   "+width);
		
		float mReqPercentages = 35;
		float mCalculatedWidth = (mReqPercentages / 100) * width;

		
		img.getLayoutParams().height = (int) (mCalculatedWidth);
		img.getLayoutParams().width = (int) mCalculatedWidth;
		/*int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_LOW:
			img.getLayoutParams().height = 100;
			img.getLayoutParams().width = 100;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			img.getLayoutParams().height = 180;
			img.getLayoutParams().width = 180;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			img.getLayoutParams().height = 200;
			img.getLayoutParams().width = 180;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			img.getLayoutParams().height = 250;
			img.getLayoutParams().width = 250;
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			img.getLayoutParams().height = 250;
			img.getLayoutParams().width = 250;
			break;
		}*/
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(resultCode, resultCode, data);
		System.out.println("resultCode  +  " + resultCode);
		System.out.println("data   +  " + data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == -1
				&& data != null) {
			try{
			
			Uri selectedImageURI = data.getData();
			File imageFile = new File(getRealPathFromURI(selectedImageURI));

			/*InputStream is = null;
			try {
				is = getActivity().getContentResolver().openInputStream(
						data.getData());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			imageData = BitmapFactory.decodeStream(is, null, null);*/
			String path_get=imageFile.getAbsolutePath();
			
	
			
			 try {
			       
			        ExifInterface exif = new ExifInterface(path_get);
			        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			        int angle = 0;

			        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
			            angle = 90;
			        } 
			        else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
			            angle = 180;
			        } 
			        else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
			            angle = 270;
			        }

			        Matrix mat = new Matrix();
			        mat.postRotate(angle);

			        Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(imageFile), null, null);
			        Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);                 
			        imageData=correctBmp;
			 }
			 
			 
			 
			    catch (IOException e) {
			        Log.w("TAG", "-- Error in setting image");
			    }   
			    catch(OutOfMemoryError oom) {
			        Log.w("TAG", "-- OOM Error in setting image");
			    }
			 
			 try {
				
			
			 Bitmap dstBmp;
			 if (imageData.getWidth() >= imageData.getHeight()){

				  dstBmp = Bitmap.createBitmap(
						  imageData, 
						  imageData.getWidth()/2 - imageData.getHeight()/2,
				     0,
				     imageData.getHeight(), 
				     imageData.getHeight()
				     );

				}else{

				  dstBmp = Bitmap.createBitmap(
						  imageData,
				     0, 
				     imageData.getHeight()/2 - imageData.getWidth()/2,
				     imageData.getWidth(),
				     imageData.getWidth() 
				     );
				}
			 imageData=dstBmp;
			 } catch (Exception e) {
					// TODO: handle exception
				}

			try {
				//is.close();

				img.setImageBitmap(imageData);
				
				setsizeofimage();
				addWhiteBorder(imageData,2);
				if (isInternetPresent) {
					new parent_pic_update().execute();

				} else {
					Toast.makeText(getActivity(),
							"Please check internet connection", 2000).show();

				}

			} catch (Exception e) {

				e.printStackTrace();
			}
			}catch(Exception e){
				InputStream is = null;
				try {
					is = getActivity().getContentResolver().openInputStream(
							data.getData());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				imageData = BitmapFactory.decodeStream(is, null, null);
				
				 Bitmap dstBmp;
				 if (imageData.getWidth() >= imageData.getHeight()){

					  dstBmp = Bitmap.createBitmap(
							  imageData, 
							  imageData.getWidth()/2 - imageData.getHeight()/2,
					     0,
					     imageData.getHeight(), 
					     imageData.getHeight()
					     );

					}else{

					  dstBmp = Bitmap.createBitmap(
							  imageData,
					     0, 
					     imageData.getHeight()/2 - imageData.getWidth()/2,
					     imageData.getWidth(),
					     imageData.getWidth() 
					     );
					}
				 imageData=dstBmp;
				

				try {
					//is.close();

					img.setImageBitmap(imageData);
					
					setsizeofimage();
					addWhiteBorder(imageData,2);
					if (isInternetPresent) {
						new parent_pic_update().execute();

					} else {
						Toast.makeText(getActivity(),
								"Please check internet connection", 2000).show();

					}

				} catch (Exception e2) {

					e.printStackTrace();
				}
			}
		}
if(requestCode==CAMERA_CAPTURE_IMAGE_REQUEST_CODE){
	switch (resultCode) {
	// When user doesn't capture image, resultcode returns 0
	case 0:
		Log.i("AndroidProgrammerGuru", "User cancelled");
		
		break;
	// When user captures image, onPhotoTaken an user-defined method
	// to assign the capture image to ImageView
	case -1:
		onPhotoTaken();
		
		
	}
}
		

	}

	private String getRealPathFromURI(Uri contentURI) {
	    String result;
	    Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
	    if (cursor == null) { // Source is Dropbox or other similar local file path
	        result = contentURI.getPath();
	    } else { 
	        cursor.moveToFirst(); 
	        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	        result = cursor.getString(idx);
	        cursor.close();
	    }
	    return result;
	}
	
	public class parent_detail_update extends
			AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		String url;
		InputStream is;
		String result;
		JSONObject jArray = null;
		String data;

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading.......please wait");
			dialog.setCancelable(false);
			dialog.show();
			url =Constants.GUAR_EDIT;// "http://54.191.67.152/playdate/guardianinfo_edit.php";// ?profile_pic=pic&name=deepak&dob=1989/1/12&set_fixed_freetime=1989/2/4&school=DPS&conditions=TRUE&allergies=test&hobbies=demo&siblings=mother&youth_club=abc&g_id=10"
		}// http://54.191.67.152/playdate/guardianinfo_edit.php?guardian_id=1&name=abcde&dob=1989-07-08&location=aaabcde&phone=9872742345&set_fixed_freetime=12&facebook_id=123
			// dob=1989-07-07&location=aa&phone=9872742345&facebook_id=123

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("g_id", user_guardian_id));
			nameValuePairs.add(new BasicNameValuePair("name", parent_name));
			nameValuePairs.add(new BasicNameValuePair("dob",date_od_birth_parent));
			nameValuePairs.add(new BasicNameValuePair("guardian_type",guardiantype));
			nameValuePairs.add(new BasicNameValuePair("location",parent_location));
			nameValuePairs.add(new BasicNameValuePair("phone",parent_phonenumber));
			nameValuePairs.add(new BasicNameValuePair("set_fixed_freetime", ""));
			nameValuePairs.add(new BasicNameValuePair("facebook_id", fb_id));

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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			String sResponse = null;

			try {
				sResponse = reader.readLine();

				System.out.println("response" + sResponse);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				JSONObject json = new JSONObject(sResponse);

				data = json.getString("success");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@SuppressLint("CommitPrefEdits")
		protected void onPostExecute(String resultt) {

			dialog.dismiss();
			if (data.equalsIgnoreCase("1")) {
				Toast.makeText(getActivity(), "Updated", 1000).show();
				SharedPreferences.Editor editor = settings.edit(); // Opening
																	// editor
																	// for
				GlobalVariable.global_name=	parent_name;										// SharedPreferences
				//editor.putString("userfirstname", parent_name);
				//editor.putString("userlocation", parent_location);
				//editor.putString("userdob", parent_dob);
				//editor.putString("userphone", parent_phonenumber);
				//editor.putString("user_guardian_id", user_guardian_id);
				 editor.putString("userguardiantype",guardiantype); 
				//editor.putString("userfreetime", parent_parentFreetime);

				checkk = true;
				editor.putBoolean("checkk", checkk);
				//editor.commit();

				//editor.putString("URLPROFILE", userprofilepic);
				editor.putString("NAME", parent_name);
				editor.putString("FIRSTNAME", parent_name);
			//	editor.putString("FACEBOOKID", iD);
				editor.putString("LOCATION", parent_location);
				editor.putString("DOB", parent_dob);
				editor.putString("GURTYPE", guardiantype);
				//editor.putString("FBFRIEND", fbfriends);
			//	editor.putString("URLPROFILE", userprofilepic);
				editor.putString("PHONE", parent_phonenumber);
				editor.putString("USERID", user_guardian_id);
				editor.commit();
				
				
				
				
				
			} else {
				Toast.makeText(getActivity(),
						"Update not successful, please try again", 1000)
						.show();
			}
		}
	}

	public class parent_pic_update extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		String url;
		InputStream is;
		String result;
		JSONObject jArray = null;
		String data;

		@Override
		protected void onPreExecute() {
			// Toast.makeText(Login.this,"asynch task",Toast.LENGTH_LONG).show();
			dialog.setMessage("Loading.......please wait");
			dialog.setCancelable(false);
			dialog.show();
			url = Constants.GUARDIAN_PIC_UPDATE;//"http://54.191.67.152/playdate/guardian_edit.php";// ?profile_pic=pic&name=deepak&dob=1989/1/12&set_fixed_freetime=1989/2/4&school=DPS&conditions=TRUE&allergies=test&hobbies=demo&siblings=mother&youth_club=abc&g_id=10"
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url);

			// Bitmap bitmap =
			// BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			imageData.compress(Bitmap.CompressFormat.JPEG, 90, stream); // compress
																		// to
																		// which
																		// format
																		// you
																		// want.
			byte[] byte_arr = stream.toByteArray();
			String image_str = com.iapp.playdate.Base64
					.encodeBytes(byte_arr);
			System.out.println("image compressed");

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("profile_image", "12.jpg"));
			nameValuePairs
					.add(new BasicNameValuePair("g_id", user_guardian_id));
			nameValuePairs.add(new BasicNameValuePair("img", image_str));
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			String sResponse = null;

			try {
				sResponse = reader.readLine();

				System.out.println("response" + sResponse);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				JSONObject json = new JSONObject(sResponse);

				data = json.getString("success");
				urlreturned = json.getString("url");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String resultt) {

			dialog.dismiss();
			if (data.equalsIgnoreCase("1")) {
				Toast.makeText(getActivity(), "Updated", 1000).show();

				strURL = urlreturned;
				GlobalVariable.url = urlreturned;
				editor.putString("URLPROFILE", strURL);
				editor.commit();
			} else {
				Toast.makeText(getActivity(),
						"Update not successful, please try again", 1000)
						.show();
			}
		}
	}

	public class chilprofileshow_webservice extends
			AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		String url_child;
		LazyAdapter adapter;

		@Override
		protected void onPreExecute() {
			// Toast.makeText(Login.this,"asynch task",Toast.LENGTH_LONG).show();

			childinfo = true;
			dialog.setMessage("Loading.......please wait");
			dialog.setCancelable(false);
			dialog.show();
			url_child = Constants.GET_CHILD_USER;//"http://54.191.67.152/playdate/guardian_child.php";
		}

		@SuppressWarnings("unused")
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			parentItems = new ArrayList<Getcategory>();
			Getcategory getcategory = null;

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url_child);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("g_id", user_guardian_id));
			StringBuilder sbb = new StringBuilder();

			sbb.append(Constants.GET_CHILD_USER+"?");
			sbb.append(nameValuePairs.get(0));
			System.out.println(sbb);
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			String sResponse = null;

			try {
				sResponse = reader.readLine();

				System.out.println("response.................................."
						+ sResponse);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				JSONObject json = new JSONObject(sResponse);
				
				JSONObject info=json.getJSONObject("parentinfo");
				String id_parent=info.getString("guardian_id");
				
				if(id_parent.equals(GlobalVariable.guardian_Id)){
					GlobalVariable.show_unlink_button=true;
				}else{
					GlobalVariable.show_unlink_button=false;
				}

				JSONArray jarray = json.getJSONArray("data");

				for (int i = 0; i < jarray.length(); i++) {
					JSONObject c = jarray.getJSONObject(i);
					getcategory = new Getcategory();
					getcategory.guardian_identity=id_parent;
					getcategory.child_name = c.getString("Childname");
					getcategory.profile_image = c.getString("profile_image");
					getcategory.child_id = c.getString("child_id");
					//getcategory.free_time = c.getString("set_fixed_freetime");
					//getcategory.condition_child = c.getString("conditions");
					getcategory.allergies_child = c.getString("allergies");
					//getcategory.hobbies_child = c.getString("hobbies");
					getcategory.date_of_birth = c.getString("dob");
					//getcategory.school_child = c.getString("school");
					//getcategory.youthclub_child = c.getString("youth_club");
					if (c.getString("child_id").equals(null)
							|| c.getString("child_id").equals("")) {

					} else {
						parentItems.add(getcategory);
					}
				}

				JSONArray jarray_authchild = json.getJSONArray("auth_child");

				for (int i = 0; i < jarray_authchild.length(); i++) {
					JSONObject c = jarray_authchild.getJSONObject(i);
					getcategory = new Getcategory();
					getcategory.guardian_identity=c.getString("g_id");
					getcategory.child_name = c.getString("Childname");
					getcategory.profile_image = c.getString("profile_image");
					getcategory.child_id = c.getString("child_id");
					//getcategory.free_time = c.getString("set_fixed_freetime");
					//getcategory.condition_child = c.getString("conditions");
					getcategory.allergies_child = c.getString("allergies");
					//getcategory.hobbies_child = c.getString("hobbies");
					getcategory.date_of_birth = c.getString("dob");
					//getcategory.school_child = c.getString("school");
					//getcategory.youthclub_child = c.getString("youth_club");
					if (c.getString("child_id").equals(null)
							|| c.getString("child_id").equals("")) {

					} else {
						parentItems.add(getcategory);
					}

				}

				String success = json.getString("success");
				String msg = json.getString("msg");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String resultt) {
			try {
				if (parentItems.size() != 0) {
					adapter = new LazyAdapter(getActivity(), parentItems);

					child_list.setAdapter(adapter);
				}else{
					child_list.setVisibility(View.GONE);
					if (GlobalVariable.guardian_Id.equals(user_guardian_id)) {
						addchild.setVisibility(View.VISIBLE);
					} else {
				
					}
				}
			} catch (Exception e) {
				// TODO: handle exception

			}

			dialog.dismiss();

		}
	}

	public class LazyAdapter extends BaseAdapter {
		String _imgurl = "";
		private Activity activity;
		private ArrayList<Getcategory> _items;
		private LayoutInflater inflater = null;

		// public ImageLoader imageLoader;

		public ImageLoader imageLoader;

		// int count = 10;

		public LazyAdapter(Activity activity, ArrayList<Getcategory> parentItems) {

			// this.imageLoader.clearCache();
			this.activity = activity;
			this._items = parentItems;
			inflater = (LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
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

		// .............View holder use to hold the view comes in the form of
		// array list............//
		// ........................ Here we link the all the objects with the
		// xml class ...............//
		class ViewHolder {
			public TextView event_title, event_date;
			public ImageView _image = null;

		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View vi = convertView;
			ViewHolder _holder;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.child_parent_list, null);
				_holder = new ViewHolder();

				_holder.event_title = (TextView) convertView
						.findViewById(R.id.textView1);
				_holder._image = (ImageView) convertView
						.findViewById(R.id.imageView1);
				// _holder.event_date = (TextView) convertView
				// .findViewById(R.id.event_date);
				convertView.setTag(_holder);
			} else {
				_holder = (ViewHolder) convertView.getTag();
			}
			String name_of_child = _items.get(position).child_name;
			name_of_child = name_of_child.toUpperCase();
			_holder.event_title.setText(name_of_child);

			_imgurl = _items.get(position).profile_image;

			_holder._image.setTag(_imgurl);

			imageLoader.DisplayImage(_imgurl, _holder._image);

			_holder._image.requestLayout();
			
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			
			int height = display.getHeight();
			int width = display.getWidth();
			
			System.out.println(height  +  "   "+width);
			
			float mReqPercentages = 17;
			float mCalculatedWidth = (mReqPercentages / 100) * width;

			
			_holder._image.getLayoutParams().height = (int) (mCalculatedWidth);
			_holder._image.getLayoutParams().width = (int) mCalculatedWidth;
				
				
		/*	int density = getResources().getDisplayMetrics().densityDpi;
			switch (density) {
			case DisplayMetrics.DENSITY_LOW:
				_holder._image.getLayoutParams().height = 40;
				_holder._image.getLayoutParams().width = 40;

				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				_holder._image.getLayoutParams().height = 80;
				_holder._image.getLayoutParams().width = 80;
				// child_list.setLayoutParams(new LayoutParams(90,90));
				break;
			case DisplayMetrics.DENSITY_HIGH:
				_holder._image.getLayoutParams().height = 80;
				_holder._image.getLayoutParams().width = 80;
				// child_list.setLayoutParams(new LayoutParams(110,110));
				break;
			case DisplayMetrics.DENSITY_XHIGH:
				_holder._image.getLayoutParams().height = 120;
				_holder._image.getLayoutParams().width = 120;
				// child_list.setLayoutParams(new LayoutParams(150,150));
				break;
			case DisplayMetrics.DENSITY_XXHIGH:
				_holder._image.getLayoutParams().height = 120;
				_holder._image.getLayoutParams().width = 120;
				// child_list.setLayoutParams(new LayoutParams(150,150));
				break;
			}*/

			return convertView;
		}

	}

	/*
	 * TimePickerDialog.OnTimeSetListener mTimesetlistenermon = new
	 * TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) {
	 * 
	 * // TODO Auto-generated method stub timechk1=true;
	 * timechk2=false;timechk3=
	 * false;timechk4=false;timechk5=false;timechk6=false;timechk7=false; String
	 * hour_s=String.valueOf(hourOfDay); int length=hour_s.length();
	 * if(length==1){ hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * System.out.println("............."+hour_s);
	 * System.out.println("............."+minut_s);
	 * System.out.println("............."+length);
	 * System.out.println("............."+length1);
	 * System.out.println("............."+minute);
	 * System.out.println("............."+hourOfDay);
	 * 
	 * time=hour_s+":"+minut_s; if(dayselect==1){ days="MONDAY"+" "+time+ "-";
	 * daysadd=""; } else{ daymon="MONDAY"+" "+time+ "-";
	 * //daysadd=days+daysadd+"Monday"+time+ "-"; days=""; }
	 * 
	 * }
	 * 
	 * 
	 * };
	 * 
	 * TimePickerDialog.OnTimeSetListener mTimesetlistenertue = new
	 * TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) {
	 * 
	 * // TODO Auto-generated method stub timechk2=true;
	 * timechk1=false;timechk3=
	 * false;timechk4=false;timechk5=false;timechk6=false;timechk7=false; String
	 * hour_s=String.valueOf(hourOfDay); int length=hour_s.length();
	 * if(length==1){ hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * 
	 * 
	 * time=hour_s+":"+minut_s;
	 * 
	 * if(dayselect==2){ days="TUESDAY"+" "+time+ "-"; daysadd=""; } else{
	 * daytue="TUESDAY"+" "+time+ "-"; //daysadd=days+daysadd+"Tuesday"+time+
	 * "-"; days=""; }
	 * 
	 * 
	 * } }; TimePickerDialog.OnTimeSetListener mTimesetlistenerwed = new
	 * TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) { // TODO Auto-generated method stub
	 * 
	 * timechk3=true;
	 * timechk2=false;timechk1=false;timechk4=false;timechk5=false
	 * ;timechk6=false;timechk7=false; String hour_s=String.valueOf(hourOfDay);
	 * int length=hour_s.length(); if(length==1){
	 * hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * 
	 * 
	 * time=hour_s+":"+minut_s; if(dayselect==3){ days="WEDNESDAY"+" "+time+
	 * "-"; daysadd=""; } else{ daywed="WEDNESDAY"+" "+time+ "-";
	 * //daysadd=days+daysadd+"Wednesday"+time+ "-"; days=""; }
	 * 
	 * 
	 * } }; TimePickerDialog.OnTimeSetListener mTimesetlistenerthu = new
	 * TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) { // TODO Auto-generated method stub
	 * 
	 * timechk4=true;
	 * timechk2=false;timechk3=false;timechk1=false;timechk5=false
	 * ;timechk6=false;timechk7=false; String hour_s=String.valueOf(hourOfDay);
	 * int length=hour_s.length(); if(length==1){
	 * hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * 
	 * 
	 * time=hour_s+":"+minut_s;
	 * 
	 * if(dayselect==4){ days="THURSDAY"+" "+time+ "-"; daysadd=""; } else{
	 * daythu="THURSDAY"+" "+time+ "-"; // daysadd=days+daysadd+"Thursday"+time+
	 * "-"; days="";
	 * 
	 * 
	 * } } }; TimePickerDialog.OnTimeSetListener mTimesetlistenerfri = new
	 * TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) { // TODO Auto-generated method stub
	 * 
	 * timechk5=true;
	 * timechk2=false;timechk3=false;timechk4=false;timechk1=false
	 * ;timechk6=false;timechk7=false; String hour_s=String.valueOf(hourOfDay);
	 * int length=hour_s.length(); if(length==1){
	 * hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * 
	 * 
	 * time=hour_s+":"+minut_s;
	 * 
	 * 
	 * if(dayselect==5){ days="FRIDAY"+" "+time+ "-"; daysadd=""; } else{
	 * dayfri="FRIDAY"+" "+time+ "-"; // daysadd=days+daysadd+"Friday"+time+
	 * "-"; days="";
	 * 
	 * } } }; TimePickerDialog.OnTimeSetListener mTimesetlistenersat = new
	 * TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) { System.out.println("to .....saaaat");
	 * 
	 * // TODO Auto-generated method stub timechk6=true;
	 * timechk2=false;timechk3=
	 * false;timechk4=false;timechk5=false;timechk1=false;timechk7=false; String
	 * hour_s=String.valueOf(hourOfDay); int length=hour_s.length();
	 * if(length==1){ hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * 
	 * 
	 * time=hour_s+":"+minut_s; if(dayselect==6){ days="SATURDAY"+" "+time+ "-";
	 * daysadd=""; } else{ daysat="SATURDAY"+" "+time+ "-"; //
	 * daysadd=days+daysadd+"Saturday"+time+ "-"; days="";
	 * 
	 * 
	 * } } }; TimePickerDialog.OnTimeSetListener mTimesetlistenersun = new
	 * TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) { // TODO Auto-generated method stub
	 * 
	 * System.out.println("to ...."); timechk7=true;
	 * timechk2=false;timechk3=false
	 * ;timechk4=false;timechk5=false;timechk6=false;timechk1=false; String
	 * hour_s=String.valueOf(hourOfDay); int length=hour_s.length();
	 * if(length==1){ hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * 
	 * 
	 * time=hour_s+":"+minut_s;
	 * 
	 * if(dayselect==7){ days="SUNDAY"+" "+time+ "-"; daysadd=""; } else{
	 * daysun="SUNDAY"+" "+time+ "-"; // daysadd=days+daysadd+"Saturday"+time+
	 * "-"; days="";
	 * 
	 * 
	 * }
	 * 
	 * } }; TimePickerDialog.OnTimeSetListener mTimesetlistenermonto = new
	 * TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) { if(clicked==true){ clicked=false; // TODO Auto-generated method
	 * stub timechk7=false;
	 * timechk2=false;timechk3=false;timechk4=false;timechk5
	 * =false;timechk6=false;timechk1=false; String
	 * hour_s=String.valueOf(hourOfDay); int length=hour_s.length();
	 * if(length==1){ hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * 
	 * 
	 * time=hour_s+":"+minut_s;
	 * 
	 * if(dayselect==1){
	 * 
	 * 
	 * if(timechk1==true){ days=days+time+" "; parentfreetime.setText(days);
	 * }else{ alert(); }
	 * 
	 * } else{ if(timechk1==true){ daysadd=daysadd+daymon+time+"  ";
	 * parentfreetime.setText(daysadd); daymon=""; }else{ alert(); }
	 * dayselect=0; } }} }; public void alert(){ AlertDialog.Builder builder =
	 * new AlertDialog.Builder( getActivity());
	 * 
	 * builder.setTitle("Invalid entry");
	 * builder.setMessage("Please select from time ") .setCancelable(false)
	 * .setPositiveButton( "OK", new DialogInterface.OnClickListener() { public
	 * void onClick( DialogInterface dialog, int id) { //
	 * edit_email.setText("");
	 * 
	 * dialog.cancel();
	 * 
	 * }
	 * 
	 * });
	 * 
	 * AlertDialog alert = builder.create(); alert.show(); }
	 * TimePickerDialog.OnTimeSetListener mTimesetlistenertueto = new
	 * TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) {
	 * 
	 * if(clicked==true){ clicked=false; System.out.println("to ....."); // TODO
	 * Auto-generated method stub timechk7=false;
	 * timechk2=false;timechk3=false;timechk4
	 * =false;timechk5=false;timechk6=false;timechk1=false; String
	 * hour_s=String.valueOf(hourOfDay); int length=hour_s.length();
	 * if(length==1){ hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * 
	 * 
	 * time=hour_s+":"+minut_s; if(dayselect==2){ if(timechk2==true){
	 * days=days+time+" "; parentfreetime.setText(days); }else{ alert(); } }
	 * else{ if(timechk2==true){ daysadd=daysadd+daytue+time+"  ";
	 * parentfreetime.setText(daysadd); daytue=""; }else{ alert(); }
	 * dayselect=0; } }} }; TimePickerDialog.OnTimeSetListener
	 * mTimesetlistenerwedto = new TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) { // TODO Auto-generated method stub
	 * System.out.println("to ....."); if(clicked==true){ clicked=false;
	 * timechk7=false;
	 * timechk2=false;timechk3=false;timechk4=false;timechk5=false
	 * ;timechk6=false;timechk1=false; String hour_s=String.valueOf(hourOfDay);
	 * int length=hour_s.length(); if(length==1){
	 * hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * 
	 * 
	 * time=hour_s+":"+minut_s; if(dayselect==3){ if(timechk3==true){
	 * days=days+time+" "; parentfreetime.setText(days); }else{ alert(); } }
	 * else{ if(timechk3==true){ daysadd=daysadd+daywed+time+"  ";
	 * parentfreetime.setText(daysadd); daywed=""; }else{ alert(); }
	 * dayselect=0; } } } }; TimePickerDialog.OnTimeSetListener
	 * mTimesetlistenerthuto = new TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) { // TODO Auto-generated method stub timechk7=false;
	 * timechk2=false
	 * ;timechk3=false;timechk4=false;timechk5=false;timechk6=false
	 * ;timechk1=false; if(clicked==true){ clicked=false; String
	 * hour_s=String.valueOf(hourOfDay); int length=hour_s.length();
	 * if(length==1){ hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * 
	 * 
	 * time=hour_s+":"+minut_s; if(dayselect==4){ if(timechk4==true){
	 * days=days+time+" "; parentfreetime.setText(days); }else{ alert(); } }
	 * else{ if(timechk4==true){ daysadd=daysadd+daythu+time+"  ";
	 * parentfreetime.setText(daysadd); daythu=""; }else{ alert(); }
	 * dayselect=0; }
	 * 
	 * } } }; TimePickerDialog.OnTimeSetListener mTimesetlistenerfrito = new
	 * TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) { // TODO Auto-generated method stub timechk7=false;
	 * timechk2=false
	 * ;timechk3=false;timechk4=false;timechk5=false;timechk6=false
	 * ;timechk1=false; if(clicked==true){ clicked=false; String
	 * hour_s=String.valueOf(hourOfDay); int length=hour_s.length();
	 * if(length==1){ hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * 
	 * 
	 * time=hour_s+":"+minut_s; if(dayselect==5){ if(timechk5==true){
	 * days=days+time+" "; parentfreetime.setText(days); }else{ alert(); } }
	 * else{ if(timechk5==true){ daysadd=daysadd+dayfri+time+"  ";
	 * parentfreetime.setText(daysadd); dayfri=""; }else{ alert(); }
	 * dayselect=0; } } } }; TimePickerDialog.OnTimeSetListener
	 * mTimesetlistenersatto = new TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) { // TODO Auto-generated method stub
	 * System.out.println("to .....sat"); timechk7=false;
	 * timechk2=false;timechk3
	 * =false;timechk4=false;timechk5=false;timechk6=false;timechk1=false;
	 * 
	 * time=String.valueOf(hourOfDay)+":"+String.valueOf(minute);
	 * if(clicked==true){ clicked=false; String
	 * hour_s=String.valueOf(hourOfDay); int length=hour_s.length();
	 * if(length==1){ hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * 
	 * 
	 * time=hour_s+":"+minut_s; if(dayselect==6){ if(timechk6==true){
	 * days=days+time+" "; parentfreetime.setText(days); }else{ alert(); } }
	 * else{ if(timechk6==true){ daysadd=daysadd+daysat+time+"  ";
	 * parentfreetime.setText(daysadd); daysat=""; }else{ alert(); }
	 * dayselect=0; }
	 * 
	 * } } }; TimePickerDialog.OnTimeSetListener mTimesetlistenersunto = new
	 * TimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) { // TODO Auto-generated method stub if(clicked==true){
	 * clicked=false;
	 * 
	 * System.out.println("to .....sun"); timechk7=false;
	 * timechk2=false;timechk3
	 * =false;timechk4=false;timechk5=false;timechk6=false;timechk1=false;
	 * String hour_s=String.valueOf(hourOfDay); int length=hour_s.length();
	 * if(length==1){ hour_s="0"+String.valueOf(hourOfDay); } String
	 * minut_s=String.valueOf(minute); int length1=minut_s.length();
	 * if(length1==1){ minut_s="0"+String.valueOf(minute); }
	 * 
	 * 
	 * time=hour_s+":"+minut_s; if(dayselect==7){ if(timechk7==true){
	 * days=days+time; parentfreetime.setText(days); }else{ alert(); } } else{
	 * if(timechk7==true){ daysadd=daysadd+daysun+time+" ";
	 * parentfreetime.setText(daysadd); daysun=""; }else{ alert(); }
	 * dayselect=0; } }
	 * 
	 * } };
	 */
	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			/*
			 * myYear = year; myMonth = monthOfYear; myDay = dayOfMonth;
			 */
			monthOfYear = monthOfYear + 1;

			String day = String.valueOf(dayOfMonth);
			String month = String.valueOf(monthOfYear);
			String year1 = String.valueOf(year);
			int i = day.length();
			if (i == 1) {
				day = "0" + day;

			}
			int i1 = month.length();
			if (i1 == 1) {
				month = "0" + month;

			}
			int i2 = year1.length();
			if (i2 == 1) {
				year1 = "0" + year1;

			}

			String date = day + "/" + month + "/" + year1;
			
			
			 SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
				
			 Date date_1=null;
			 try {
				 date_1=sdf.parse(date);
				
				 
				 
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			 SimpleDateFormat destDf = new SimpleDateFormat("dd/MM/yy");
			
			  String date_to_set=null;             
		
			             // format the date into another format
			try {
				date_to_set = destDf.format(date_1);
			} catch (Exception e) {
				// TODO: handle exception
			}
    	   
    	   
    	   
    	   
    	   
    	   
    	   
			
			try{

			      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
			       Date date1 = formatter.parse(date_comparision);
			       Date date2 = formatter.parse(date_to_set);
			    if (date1.compareTo(date2)<0)
			    {
			    	if(count_alert==0){
			    		count_alert=count_alert+1;
			    	new AlertDialog.Builder(getActivity())
					
				    .setTitle("Invalid Entry")
				    .setMessage("can't select future date")
				    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        		return;			        	
				        }
				     })
				   
				    .setIcon(android.R.drawable.ic_dialog_alert)
				     .show();
			    	}
				                    
			    }else{
			    	count_alert=0;
			    	parentdob.setText(date_to_set);
			    }
			 } catch (ParseException e1) 
		      {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		                        }
			
		}
		
	};
	
	protected void onPhotoTaken() {
		// Log message
		Log.i("AndroidProgrammerGuru", "onPhotoTaken");
		// Flag used by Activity life cycle methods
		taken = true;
		// Flag used to check whether image captured or not
		imgCapFlag = true;
		// BitmapFactory- Create an object
		BitmapFactory.Options options = new BitmapFactory.Options();
		// Set image size
		options.inSampleSize = 4;
		// Read bitmap from the path where captured image is stored
		imageData = BitmapFactory.decodeFile(path, options);
		img.setImageBitmap(imageData);
		setsizeofimage();
		if (isInternetPresent) {
			new parent_pic_update().execute();

		} else {
			Toast.makeText(getActivity(),
					"Please check internet connection", 2000).show();

		}
	
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(PHOTO_TAKEN, taken);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("AndroidProgrammerGuru", "onRestoreInstanceState()");
		if (savedInstanceState.getBoolean(PHOTO_TAKEN)) {
			// onPhotoTaken();
		}
	}
	protected void startCameraActivity() {
		// Log message
		Log.i("AndroidProgrammerGuru", "startCameraActivity()");
		// Create new file with name mentioned in the path variable
		File file = new File(path);
		// Creates a Uri from a file
		Uri outputFileUri = Uri.fromFile(file);
		// Standard Intent action that can be sent to have the
		// camera application capture an image and return it.
		// You will be redirected to camera at this line
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Add the captured image in the path
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		// Start result method - Method handles the output
		// of the camera activity
		getActivity().startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}
	
}
