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
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iapp.playdate.R;
import com.iapptechnologies.time.util.Constants;


public class Event_accept_reject_edit extends android.support.v4.app.Fragment{
       boolean firstTime;
	int i=1,count_alert=0;
	int date_dialog=0,start_dialog=0,end_dialog=0;
	int myYear,myMonth,myDay;
	int hours,minutes;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	String child_name,child_id,child_pic,friend_name,friend_pic,friend_id,guardian_id,event_id,start_time,end_time,date,location_of_event,notes,ststus_of_event,time="",time_to="",eventdate,eventdate1,eventdate2,eventdate3;
	ImageView child_image,friend_image,accepted_pending;
	Button accept_request,reject_request,edit_request,sendback,cancle;
	EditText date_of_event,time_of_event,place_of_event,notes_of_event,end_time_txt;
	TextView child_name_txt,friend_name_txt,child_name_event,friend_name_event,starttime_appear;
	RelativeLayout block1_date,block1_starttime,block1_endtime,block2_date,block2_starttime,block2_endtime,block3_date,block3_starttime,block3_endtime,block_endtime;
	String date_comparision,status,sender_id,receiver_id,success,url_create,date_selected,starttime_selected,endtime_selected,date_selected1,starttime_selected1,endtime_selected1,date_selected2,starttime_selected2,endtime_selected2,date_selected3,starttime_selected3,endtime_selected3;
	boolean clicked=false,clicke_on_timepicker=false;;
	Boolean isInternetPresent = false;
    ConnectionDetector cd;
    String child_dob,child_hobbies,child_allergies,child_freetime,child_school,child_youthclub,child_friends_dob,child_friends_allergies,child_friends_hobbies,child_friends_freetime,child_friends_school,child_friends_youthclub;
    String calenderScreen;
	public Event_accept_reject_edit(){
		
	}
	@SuppressLint("DefaultLocale")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.eventshown, container,
                false);
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
		editor = settings.edit();
		
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		 date_comparision = df.format(c.getTime());
		 com.iapptechnologies.time.util.ImageLoader imageLoader=new com.iapptechnologies.time.util.ImageLoader(getActivity());
		accept_request=(Button)view.findViewById(R.id.button1_accept_event);
		reject_request=(Button)view.findViewById(R.id.button2_reject_event);
		edit_request=(Button)view.findViewById(R.id.button3_edit_event);
		sendback=(Button)view.findViewById(R.id.button1_sendback);
		cancle=(Button)view.findViewById(R.id.button3_cancle);
		child_name_txt=(TextView)view.findViewById(R.id.textView1_childname);
		friend_name_txt=(TextView)view.findViewById(R.id.textView3_friendname);
		child_name_event=(TextView)view.findViewById(R.id.textView1_child_name_event);
		///friend_name_event=(TextView)view.findViewById(R.id.textView3_child_friend_name_event);
		starttime_appear=(TextView)view.findViewById(R.id.textView_event3);
		//alternatedatetime=(TextView)view.findViewById(R.id.text_alternatedate12);
		date_of_event=(EditText)view.findViewById(R.id.edittext_when_request);
		time_of_event=(EditText)view.findViewById(R.id.textView_time_ofevent);
		place_of_event=(EditText)view.findViewById(R.id.textView_where_event);
		notes_of_event=(EditText)view.findViewById(R.id.textView_notes_event);
	//	block1_date=(RelativeLayout)view.findViewById(R.id.end_date1_gone);
	//	block1_starttime=(RelativeLayout)view.findViewById(R.id.end_starttime1_gone);
	//	block1_endtime=(RelativeLayout)view.findViewById(R.id.end_time1_gone);
	//	block2_date=(RelativeLayout)view.findViewById(R.id.date2_gone);
	//	block2_starttime=(RelativeLayout)view.findViewById(R.id.start_time2_gone);
	//	block2_endtime=(RelativeLayout)view.findViewById(R.id.end_time2_gone);
	//	block3_date=(RelativeLayout)view.findViewById(R.id.date3_gone);
	//	block3_starttime=(RelativeLayout)view.findViewById(R.id.start_time3_gone);
	//	block3_endtime=(RelativeLayout)view.findViewById(R.id.end_time3_gone);
		block_endtime=(RelativeLayout)view.findViewById(R.id.end_time_gone);
	//	date1=(EditText)view.findViewById(R.id.edit_requestdate_date1_event);
	//	starttime1=(EditText)view.findViewById(R.id.edit_requestdate_starttime1_event);
	//	endtime1=(EditText)view.findViewById(R.id.edit_time_alternate_event);
	//	date2=(EditText)view.findViewById(R.id.edit_requestdate_date2_event);
	//	starttime2=(EditText)view.findViewById(R.id.edit_requestdate_starttime2_event);
	//	endtime2=(EditText)view.findViewById(R.id.edit_requestdate_endtime2_event);
	//	date3=(EditText)view.findViewById(R.id.edit_requestdate_date3_event);
	//	starttime3=(EditText)view.findViewById(R.id.edit_requestdate_starttime3_event);
	//	endtime3=(EditText)view.findViewById(R.id.edit_requestdate_endtime3_event);
		end_time_txt=(EditText)view.findViewById(R.id.edit_requestdate_date_event);
		child_image=(ImageView)view.findViewById(R.id.imageView2_child_event);
		friend_image=(ImageView)view.findViewById(R.id.imageView_childfriend_event);
		accepted_pending=(ImageView)view.findViewById(R.id.image_accepted_pending);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		try {
			
			/*name_get=settings.getString("NAME","");
			strURL=settings.getString("URLPROFILE","");
			
			firstname=settings.getString("FIRSTNAME","");
			fb_id=settings.getString("FACEBOOKID","");
			dob_get=settings.getString("DOB","");
			guardiantype=settings.getString("GURTYPE","");
			location=settings.getString("LOCATION","");
			facebook_friends=settings.getString("FBFRIEND","");
			phone_number=settings.getString("PHONE","");*/
			
			
			guardian_id=settings.getString("USERID","");
		child_name = getArguments().getString("child_name"); 
		child_id = getArguments().getString("Child_id"); 
		child_pic=getArguments().getString("Child_profilepic");
		friend_name = getArguments().getString("Child_friend_name"); 
		friend_pic = getArguments().getString("Child_friend_pic"); 
		friend_id = getArguments().getString("Child_frient_id"); 
		//guardian_id = getArguments().getString("Child_guardianId"); 
		event_id = getArguments().getString("event_id"); 
		start_time = getArguments().getString("event_start"); 
		end_time = getArguments().getString("event_end_time"); 
		date = getArguments().getString("event_date"); 
		location_of_event = getArguments().getString("location_ofevent"); 
		notes = getArguments().getString("notes"); 
		sender_id= getArguments().getString("sender_id"); 
		receiver_id= getArguments().getString("receiver_id"); 
		status= getArguments().getString("status");
		//
		child_dob = getArguments().getString("child_dob"); 
		//child_hobbies = getArguments().getString("child_hobbies"); 
		child_allergies = getArguments().getString("child_allergies"); 
		//child_freetime = getArguments().getString("child_freetime"); 
		//child_school = getArguments().getString("child_school"); 
		//child_youthclub = getArguments().getString("child_youthclub"); 
		child_friends_dob = getArguments().getString("child_friends_dob"); 
		child_friends_allergies = getArguments().getString("child_friends_allergies"); 
		//child_friends_hobbies = getArguments().getString("child_friends_hobbies"); 
		//child_friends_freetime= getArguments().getString("child_friends_freetime"); 
		//child_friends_school= getArguments().getString("child_friends_school"); 
		//child_friends_youthclub= getArguments().getString("child_friends_youthclub");
		
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		calenderScreen= getArguments().getString("CalenderScreen"); 
		if(calenderScreen!=null && calenderScreen.length()>0)
		{
			if(calenderScreen.equalsIgnoreCase("calenderscreen"))
			{
				edit_request.setVisibility(View.GONE);
				accept_request.setVisibility(View.INVISIBLE);
				reject_request.setVisibility(View.INVISIBLE);
				
			}
		
		}else
		{
			edit_request.setVisibility(View.VISIBLE);
		}
		
		
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int width = display.getWidth();
		@SuppressWarnings("deprecation")
		int height = display.getHeight();

		float mReqPercentages = 30;
		float mCalculatedWidth = (mReqPercentages / 100) * width;

		child_name_txt.getLayoutParams().width = (int) (mCalculatedWidth );
		friend_name_txt.getLayoutParams().width = (int) (mCalculatedWidth );
	
		
		//Alternate date/time
		/*try {
			date_1alt=getArguments().getString("date1_event"); 
			date_2alt=getArguments().getString("date2_event"); 
			date_3alt=getArguments().getString("date3_event"); 
			start_time1alt=getArguments().getString("start_time1_event");
			System.out.println("start_time1alt"+ start_time1alt);
			start_time2alt=getArguments().getString("start_time2_event");
			System.out.println("start_time2alt"+ start_time2alt);
			start_time3alt=getArguments().getString("start_time3_event");
			System.out.println("start_time3alt"+ start_time3alt);
			end_time1alt=getArguments().getString("end_time1_event");
			System.out.println("end_time1alt"+ end_time1alt);
			end_time2alt=getArguments().getString("end_time2_event");
			System.out.println("end_time2alt"+ end_time2alt);
			end_time3alt=getArguments().getString("end_time3_event");
			System.out.println("end_time3alt"+ end_time3alt);
			
		} catch (Exception e) {
			// TODO: handle exception
		}*/
	//try {
		
		
	/*	if(date_1alt.equals("0000-00-00")){
			
		}else{
			i=2;
			 DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			 Date date_of_event1=null;
			 try {
				 date_of_event1=sdf.parse(date_1alt);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//String reportDate = df.format(today);
			// birthDay=sdf.format(date_of_birth);
			 DateFormat destDf = new SimpleDateFormat("dd/MM/yy");
			 String date_of_event = destDf.format(date_of_event1);
			block1_date.setVisibility(View.VISIBLE);
			block1_endtime.setVisibility(View.VISIBLE);
			block1_starttime.setVisibility(View.VISIBLE);
			
//		    String 	monthFulls="";
//			String fullDate[]=date_of_event.split("-");
//			
//			String monthFull=fullDate[1];
//			if(monthFull.equalsIgnoreCase("01"))
//			{
//			monthFulls="jan";
//			}else if(monthFull.equalsIgnoreCase("02"))
//			{
//				monthFulls="Feb";
//			}else if(monthFull.equalsIgnoreCase("03"))
//			{
//				monthFulls="Mar";
//			}else if(monthFull.equalsIgnoreCase("04"))
//			{
//				monthFulls="Apr";
//			}else if(monthFull.equalsIgnoreCase("05"))
//			{
//				monthFulls="May";
//			}else if(monthFull.equalsIgnoreCase("06"))
//			{
//				monthFulls="Jun";
//			}else if(monthFull.equalsIgnoreCase("07"))
//			{
//				monthFulls="Jul";
//			}else if(monthFull.equalsIgnoreCase("08"))
//			{
//			monthFull="Aug";
//			}else if(monthFull.equalsIgnoreCase("09"))
//			{
//				monthFulls="Sep";
//			}else if(monthFull.equalsIgnoreCase("10"))
//			{
//				monthFulls="Oct";
//			}else if(monthFull.equalsIgnoreCase("11"))
//			{
//				monthFulls="Nov";
//			}else if(monthFull.equalsIgnoreCase("12"))
//			{
//				monthFulls="Dec";
//			}
//			
//			String dayFull=fullDate[0];
//			String yearFull=fullDate[2];
			
			date1.setText(date_of_event);
			
			starttime1.setText(start_time1alt);
			endtime1.setText(end_time1alt);
		}*/
       /* if(date_2alt.equals("0000-00-00")){
			
		}else{
			i=3;
			 DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			 Date date_of_event1=null;
			 try {
				 date_of_event1=sdf.parse(date_2alt);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//String reportDate = df.format(today);
			// birthDay=sdf.format(date_of_birth);
			 DateFormat destDf = new SimpleDateFormat("dd/MM/yy");
			 String date_of_event = destDf.format(date_of_event1);
			block2_date.setVisibility(View.VISIBLE);
			block2_endtime.setVisibility(View.VISIBLE);
			block2_starttime.setVisibility(View.VISIBLE);
			date2.setText(date_of_event);
			starttime2.setText(start_time2alt);
			endtime2.setText(end_time2alt);
		}*/
    /*  if(date_3alt.equals("0000-00-00")){
	
      }else{
    	  DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			 Date date_of_event1=null;
			 try {
				 date_of_event1=sdf.parse(date_3alt);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//String reportDate = df.format(today);
			// birthDay=sdf.format(date_of_birth);
			 DateFormat destDf = new SimpleDateFormat("dd/MM/yy");
			 String date_of_event = destDf.format(date_of_event1);
    	    block3_date.setVisibility(View.VISIBLE);
			block3_endtime.setVisibility(View.VISIBLE);
			block3_starttime.setVisibility(View.VISIBLE);
	      date3.setText(date_of_event);
	      starttime3.setText(start_time3alt);
	      endtime3.setText(end_time3alt);
     }*/
//	} catch (Exception e) {
		// TODO: handle exception
//	}
///		view.setFocusableInTouchMode(true);
//		view.requestFocus();
//		view.setOnKeyListener(new View.OnKeyListener() {
//		        @Override
//		        public boolean onKey(View v, int keyCode, KeyEvent event) {
//		            //Log.i(tag, "keyCode: " + keyCode);
//		            if( keyCode == KeyEvent.KEYCODE_BACK ) {
//		                  //  Log.i(tag, "onKey Back listener is working!!!");
//		            	Log.e("ONBackPressed==","WorkingNow");
//		                //getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//		               
//		            	FragmentTransaction tx = FragmentManager.beginTransation();
//		            	tx.replace( R.id.fragment, new MyFragment() ).addToBackStack( "tag" ).commit();
//		                
//		                return true;
//		            } else {
//		                return false;
//		            }
//		        }
//		    });
		
		
		
		 DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 Date date_of_birth=null;
		 try {
			 date_of_birth=sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//String reportDate = df.format(today);
		// birthDay=sdf.format(date_of_birth);
		 DateFormat destDf = new SimpleDateFormat("dd/MM/yy");
		 String date_of_event1 = destDf.format(date_of_birth);
		
		 date_of_event.setText(date_of_event1)   ; 
	
		             // format the date into another format
		
		
		if(status.equalsIgnoreCase("accepted")){
			sender_id=receiver_id;
			 accept_request.setVisibility(View.INVISIBLE);
				reject_request.setVisibility(View.INVISIBLE);
			accepted_pending.setImageResource(R.drawable.accepted);
		}
		
		if(sender_id.equals(GlobalVariable.guardian_Id)){
			sender_id=receiver_id;
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	          
	            params.gravity=Gravity.CENTER;
	            //accept_request.setLayoutParams(params);
	            reject_request.setLayoutParams(params);
	            edit_request.setLayoutParams(params);
	            accept_request.setVisibility(View.INVISIBLE);
				reject_request.setVisibility(View.INVISIBLE);
		
		}
		
		child_name_txt.setText(child_name.toUpperCase());
		friend_name_txt.setText(friend_name.toUpperCase());
	//	child_name_event.setText(child_name.toUpperCase());
	//	friend_name_event.setText(friend_name.toUpperCase());
		
		//date_of_event.setText(date_of_event1);
		
	   /* String 	monthFulls="";
		String fullDate[]=date_of_event1.split("/");
		
		String monthFull=fullDate[1];
		if(monthFull.equalsIgnoreCase("01"))
		{
		monthFulls="jan";
		}else if(monthFull.equalsIgnoreCase("02"))
		{
			monthFulls="Feb";
		}else if(monthFull.equalsIgnoreCase("03"))
		{
			monthFulls="Mar";
		}else if(monthFull.equalsIgnoreCase("04"))
		{
			monthFulls="Apr";
		}else if(monthFull.equalsIgnoreCase("05"))
		{
			monthFulls="May";
		}else if(monthFull.equalsIgnoreCase("06"))
		{
			monthFulls="Jun";
		}else if(monthFull.equalsIgnoreCase("07"))
		{
			monthFulls="Jul";
		}else if(monthFull.equalsIgnoreCase("08"))
		{
		monthFulls="Aug";
		}else if(monthFull.equalsIgnoreCase("09"))
		{
			monthFulls="Sep";
		}else if(monthFull.equalsIgnoreCase("10"))
		{
			monthFulls="Oct";
		}else if(monthFull.equalsIgnoreCase("11"))
		{
			monthFulls="Nov";
		}else if(monthFull.equalsIgnoreCase("12"))
		{
			monthFulls="Dec";
		}
		
		String dayFull=fullDate[0];
		String yearFull=fullDate[2];
		
		
		date_of_event.setText(dayFull+"-"+monthFulls+"-"+yearFull);*/
		
		time_of_event.setText(start_time +" TO "+end_time);
		place_of_event.setText(location_of_event.toUpperCase());
		notes_of_event.setText(notes.toUpperCase());
		//notes_of_event.setFocusable(false);
		
		/*date_of_event.setFocusable(false);
		date_of_event.setClickable(false);
		time_of_event.setFocusable(false);
		time_of_event.setClickable(false);*/
		//place_of_event.setFocusable(false);
		
		
	/*
		date1.setEnabled(false);
		date2.setEnabled(false);
		date3.setEnabled(false);
		starttime1.setEnabled(false);
		starttime2.setEnabled(false);
		starttime3.setEnabled(false);
		endtime1.setEnabled(false);
		endtime2.setEnabled(false);
		endtime3.setEnabled(false);*/
	
		end_time_txt.setFocusable(false);
		end_time_txt.setClickable(true);
	
		
		date_of_event.setEnabled(false);
		time_of_event.setEnabled(false);
		place_of_event.setEnabled(false);
		notes_of_event.setEnabled(false);
		end_time_txt.setEnabled(false);
		
		imageLoader.DisplayImage(child_pic, child_image);
		imageLoader.DisplayImage(friend_pic, friend_image);
		
		child_image.requestLayout();
		friend_image.requestLayout();
		
		
//Display display = getActivity().getWindowManager().getDefaultDisplay();
		
		//int height = display.getHeight();
		//int width = display.getWidth();
		
		System.out.println(height  +  "   "+width);
		
		float mReqPercentages1 = 20;
		float mCalculatedWidth1 = (mReqPercentages1 / 100) * width;

	

		
		child_image.getLayoutParams().height = (int) (mCalculatedWidth1);
		child_image.getLayoutParams().width = (int) mCalculatedWidth1;
		
		
		friend_image.getLayoutParams().height = (int) (mCalculatedWidth1);
		friend_image.getLayoutParams().width = (int) mCalculatedWidth1;
		
		
		accepted_pending.getLayoutParams().height = (int) (mCalculatedWidth1);
		accepted_pending.getLayoutParams().width = (int) mCalculatedWidth1;
		
		
		/* int density = getResources().getDisplayMetrics().densityDpi;
		  switch (density) {
		  case DisplayMetrics.DENSITY_LOW:
			  child_image.getLayoutParams().height = 70;
			  child_image.getLayoutParams().width = 70;
			  friend_image.getLayoutParams().height = 70;
			  friend_image.getLayoutParams().width = 70;
			  
			  break;
		  case DisplayMetrics.DENSITY_MEDIUM:
			  child_image.getLayoutParams().height = 100;
			  child_image.getLayoutParams().width = 100;
			  friend_image.getLayoutParams().height = 100;
			  friend_image.getLayoutParams().width = 100;
			  break;
		  case DisplayMetrics.DENSITY_HIGH:
			  child_image.getLayoutParams().height = 120;
			  child_image.getLayoutParams().width = 120;
			  friend_image.getLayoutParams().height = 120;
			  friend_image.getLayoutParams().width = 120;
			  break;
		  case DisplayMetrics.DENSITY_XHIGH:
			  child_image.getLayoutParams().height = 150;
			  child_image.getLayoutParams().width = 150;
			  friend_image.getLayoutParams().height = 150;
			  friend_image.getLayoutParams().width = 150;
			  break;
		  case DisplayMetrics.DENSITY_XXHIGH:
			  child_image.getLayoutParams().height = 150;
			  child_image.getLayoutParams().width = 150;
			  friend_image.getLayoutParams().height = 150;
			  friend_image.getLayoutParams().width = 150;
			  break;
		  }*/
		  accept_request.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				new AlertDialog.Builder(getActivity())
			    .setTitle("Selected Action")
			    .setMessage("Are you sure ")
			    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // continue with delete
			        	ststus_of_event="accepted";
			        	cd=new ConnectionDetector(getActivity());
						 isInternetPresent = cd.isConnectingToInternet();
						 if (isInternetPresent) {
								new accept_reject().execute();
						 }
						 else{
							 Toast.makeText(getActivity(),"Please check internet connection",2000).show();
							
						 }
					
			        }
			     })
			    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	
			        }
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
			}
		});
		  reject_request.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					
					new AlertDialog.Builder(getActivity())
				    .setTitle("Selected Action")
				    .setMessage("Are you sure ")
				    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				            // continue with delete
				        	ststus_of_event="rejected";
							
							cd=new ConnectionDetector(getActivity());
							 isInternetPresent = cd.isConnectingToInternet();
							 if (isInternetPresent) {
								 new accept_reject().execute();
							 }
							 else{
								 Toast.makeText(getActivity(),"Please check internet connection",2000).show();
								
							 }
				        }
				     })
				    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        	
				        }
				     })
				    .setIcon(android.R.drawable.ic_dialog_alert)
				     .show();
					
					
				}
			});
		  
		  
		  child_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(getActivity());
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.child_detail_arrange_view);
				
				// set the custom dialog components - text, image and button
				TextView text_name = (TextView) dialog.findViewById(R.id.arrange_childname);
				TextView text_dob = (TextView) dialog.findViewById(R.id.textchild_dob_arrange);
			//	TextView text_freetime = (TextView) dialog.findViewById(R.id.freetime_child_arrange);
				TextView text_allergies = (TextView) dialog.findViewById(R.id.text_child_allergies_arrange);
			//	TextView text_hobbies = (TextView) dialog.findViewById(R.id.text_child_hobbies_arrange);
			//	TextView text_school = (TextView) dialog.findViewById(R.id.text_child_school_arrange);
				//TextView text_youthclub = (TextView) dialog.findViewById(R.id.text_child_youth_arrange);
				
				text_name.setText(child_name.toUpperCase());
				text_dob.setText(child_dob.toUpperCase());
			//	text_freetime.setText(child_freetime.toUpperCase());
				text_allergies.setText(child_allergies.toUpperCase());
			//	text_hobbies.setText(child_hobbies.toUpperCase());
			//	text_school.setText(child_school.toUpperCase());
				//text_youthclub.setText(child_youthclub.toUpperCase());
	 

	 
				dialog.show();
			}
		});
		  friend_image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final Dialog dialog = new Dialog(getActivity());
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.child_detail_arrange_view);
					
		 
					// set the custom dialog components - text, image and button
					TextView text_name = (TextView) dialog.findViewById(R.id.arrange_childname);
					TextView text_dob = (TextView) dialog.findViewById(R.id.textchild_dob_arrange);
				//	TextView text_freetime = (TextView) dialog.findViewById(R.id.freetime_child_arrange);
					TextView text_allergies = (TextView) dialog.findViewById(R.id.text_child_allergies_arrange);
				//	TextView text_hobbies = (TextView) dialog.findViewById(R.id.text_child_hobbies_arrange);
				//	TextView text_school = (TextView) dialog.findViewById(R.id.text_child_school_arrange);
					//TextView text_youthclub = (TextView) dialog.findViewById(R.id.text_child_youth_arrange);
				
					text_name.setText(friend_name.toUpperCase());
					text_dob.setText(child_friends_dob.toUpperCase());
				//	text_freetime.setText(child_friends_freetime.toUpperCase());
					text_allergies.setText(child_friends_allergies.toUpperCase());
				//	text_hobbies.setText(child_friends_hobbies.toUpperCase());
				//	text_school.setText(child_friends_school.toUpperCase());
				//	//text_youthclub.setText(child_friends_youthclub.toUpperCase());
		 

		 
					dialog.show();
				}
			});
		  edit_request.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					
					accept_request.setVisibility(View.GONE);
					reject_request.setVisibility(View.GONE);
					
					block_endtime.setVisibility(View.VISIBLE);
					//alternatedatetime.setVisibility(View.VISIBLE);
					cancle.setVisibility(View.GONE);
					sendback.setVisibility(View.VISIBLE);
					starttime_appear.setText("START TIME");
					//time_of_event.setText("");
					clicked=true;
					date_of_event.setEnabled(true);
					time_of_event.setEnabled(true);
					place_of_event.setEnabled(true);
					notes_of_event.setEnabled(true);
					end_time_txt.setEnabled(true);
					//place_of_event.requestFocus();
					place_of_event.setFocusable(true);
					//notes_of_event.requestFocus();
					notes_of_event.setFocusable(true);
					//date_of_event.requestFocus();
					date_of_event.setFocusable(false);
					date_of_event.setClickable(true);
					//time_of_event.requestFocus();
					time_of_event.setFocusable(false);
					time_of_event.setClickable(true);
					/*date1.setEnabled(true);
					
					date2.setEnabled(true);
					date3.setEnabled(true);
					starttime1.setEnabled(true);
					starttime2.setEnabled(true);
					starttime3.setEnabled(true);
					endtime1.setEnabled(true);
					endtime2.setEnabled(true);
					endtime3.setEnabled(true);*/
					time_of_event.setText(start_time);
					end_time_txt.setText(end_time);
					edit_request.setVisibility(View.GONE);
					/*date1.setFocusable(false);
					date1.setClickable(true);

					starttime1.setFocusable(false);
					starttime1.setClickable(true);
					
					endtime1.setFocusable(false);
					endtime1.setClickable(true);
					

					date2.setFocusable(false);
					date2.setClickable(true);

					starttime2.setFocusable(false);
					starttime2.setClickable(true);
					
					endtime2.setFocusable(false);
					endtime2.setClickable(true);
					

					date3.setFocusable(false);
					date3.setClickable(true);

					starttime3.setFocusable(false);
					starttime3.setClickable(true);
					
					endtime3.setFocusable(false);
					endtime3.setClickable(true);*/
				}
			});
		
		  cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendback.setVisibility(View.GONE);
				accept_request.setVisibility(View.VISIBLE);
				reject_request.setVisibility(View.VISIBLE);
				edit_request.setVisibility(View.VISIBLE);
				cancle.setVisibility(View.GONE);
			}
		});
		sendback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				
				/*accept_request.setVisibility(View.VISIBLE);
				reject_request.setVisibility(View.VISIBLE);
				edit_request.setVisibility(View.VISIBLE);
				cancle.setVisibility(View.GONE);
				sendback.setVisibility(View.GONE);*/
				
				location_of_event=place_of_event.getText().toString();
				notes=notes_of_event.getText().toString();
				date_selected=date_of_event.getText().toString();
				 starttime_selected=time_of_event.getText().toString();
					 endtime_selected=end_time_txt.getText().toString();
					 
					// 30-Nov-0002
				/*	
					 date_selected1=date1.getText().toString();
					 starttime_selected1=starttime1.getText().toString();
					 endtime_selected1=endtime1.getText().toString();
					 
					 date_selected2=date2.getText().toString();
					 starttime_selected2=starttime2.getText().toString();
					 endtime_selected2=endtime2.getText().toString();
					 
					 date_selected3=date3.getText().toString();
					 starttime_selected3=starttime3.getText().toString();
					 endtime_selected3=endtime3.getText().toString();
					 */
					 
					
					 
					/* if(date_selected!=null && date_selected.length()>0 && date_selected1!=null && date_selected1.length()>0)
					 {
						 if(date_selected.equals(date_selected1)){
							 if(starttime_selected.equals(starttime_selected1)&&endtime_selected.equals(endtime_selected1)){
								 new AlertDialog.Builder(getActivity())
								    .setTitle("Invalid entry")
								    .setMessage("Date/Time can't same as alternate date/time")
								    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
								        public void onClick(DialogInterface dialog, int which) { 
								        	
								        }
								     })
								   
								    .setIcon(android.R.drawable.ic_dialog_alert)
								     .show();
								 return;
							 }
						 } 
					 }*/
					 
					/* if(date_selected!=null && date_selected.length()>0 && date_selected2!=null && date_selected2.length()>0)
					 {
						 if(date_selected.equals(date_selected2)){
							 if(starttime_selected.equals(starttime_selected2)&&endtime_selected.equals(endtime_selected2)){
								 new AlertDialog.Builder(getActivity())
								    .setTitle("Invalid entry")
								    .setMessage("Date/Time can't same as alternate date/time")
								    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
								        public void onClick(DialogInterface dialog, int which) { 
								        	
								        }
								     })
								   
								    .setIcon(android.R.drawable.ic_dialog_alert)
								     .show();
								 return;
							 }
						 } 
					 }
						*/ 
					/* if(date_selected!=null && date_selected.length()>0 && date_selected3!=null && date_selected3.length()>0)
					 {
						 if(date_selected.equals(date_selected3)){
							 if(starttime_selected.equals(starttime_selected3)&&endtime_selected.equals(endtime_selected3)){
								 new AlertDialog.Builder(getActivity())
								    .setTitle("Invalid entry")
								    .setMessage("Date/Time can't same as alternate date/time")
								    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
								        public void onClick(DialogInterface dialog, int which) { 
								        	
								        }
								     })
								   
								    .setIcon(android.R.drawable.ic_dialog_alert)
								     .show();
								 return; 
							 }
						 }
					 }*/
			
		/*	if(date_selected1!=null && date_selected1.length()>0 && date_selected2!=null && date_selected2.length()>0)
			{
				 if(date_selected1.equals(date_selected2)){
					 if(starttime_selected1.equals(starttime_selected2)&&endtime_selected1.equals(endtime_selected2)){
						 new AlertDialog.Builder(getActivity())
						    .setTitle("Invalid entry")
						    .setMessage("Alternate Date/Time can't same as alternate date/time")
						    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						        	
						        }
						     })
						   
						    .setIcon(android.R.drawable.ic_dialog_alert)
						     .show();
						 return; 
					 }
				 }
			}
			*/
				/*	 if(date_selected1!=null && date_selected1.length()>0 && date_selected3!=null && date_selected3.length()>0)
					 {
						 if(date_selected1.equals(date_selected3)){
							 if(starttime_selected1.equals(starttime_selected3)&&endtime_selected1.equals(endtime_selected3)){
								 new AlertDialog.Builder(getActivity())
								    .setTitle("Invalid entry")
								    .setMessage("Alternate Date/Time can't same as alternate date/time")
								    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
								        public void onClick(DialogInterface dialog, int which) { 
								        	
								        }
								     })
								   
								    .setIcon(android.R.drawable.ic_dialog_alert)
								     .show();
								 return;	 
							 }
						 } 
					 }
		*/
	/*	if(date_selected2!=null && date_selected2.length()>0 && date_selected3!=null && date_selected3.length()>0)
		{
			 if(date_selected2.equals(date_selected3)){
				 if(starttime_selected2.equals(starttime_selected3)&&endtime_selected2.equals(endtime_selected3)){
					 new AlertDialog.Builder(getActivity())
					    .setTitle("Invalid entry")
					    .setMessage("Alternate Date/Time can't same as alternate date/time")
					    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					        	
					        }
					     })
					   
					    .setIcon(android.R.drawable.ic_dialog_alert)
					     .show();
					 return; 
				 }
			 }
		}*/
					 SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
					// SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yy");
					 Date date_1=null,date_2=null,date_3=null,date_4=null;
					 try {
						 date_1=sdf.parse(date_selected);
						
						 
						 
					} catch (ParseException e) {
						
						e.printStackTrace();
					}
					 SimpleDateFormat destDf = new SimpleDateFormat("yyyy-MM-dd");
					
					               
				
					             // format the date into another format
					try {
						 eventdate = destDf.format(date_1);
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					/*if(date_selected1.equals(null)){
						date_selected1="";
					}
					else{
						 try {
							date_2=sdf.parse(date_selected1);
							 eventdate1 = destDf.format(date_2);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}*/
					/*if(date_selected2.equals(null)){
						date_selected2="";
					}
					else{
						try {
							date_3=sdf.parse(date_selected2);
							 eventdate2 = destDf.format(date_3);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}*/
					/*if(date_selected3.equals(null)){
						date_selected3="";
					}
					else{
						try {
							date_4=sdf.parse(date_selected3);
							 eventdate3 = destDf.format(date_4);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(starttime_selected1.equals(null)){
						starttime_selected1="";
					}
					if(starttime_selected2.equals(null)){
						starttime_selected2="";
					}
					if(starttime_selected3.equals(null)){
						starttime_selected3="";
					}
					if(endtime_selected3.equals(null)){
						endtime_selected3="";
					}
					if(endtime_selected2.equals(null)){
						endtime_selected2="";
					}
					if(endtime_selected1.equals(null)){
						endtime_selected1="";
					} */
					/*System.out.println(">>>>>>>>>>>>"+date_selected1+" "+starttime_selected1+" "+endtime_selected1);
					 if(date_selected1.equals("")&&starttime_selected1.equals("")&&endtime_selected1.equals("")){
						 System.out.println("1");
					 }else if(date_selected1.length()>0 && starttime_selected1.length()>0 && endtime_selected1.length()>0 ){
						 System.out.println("2");
					 }else{
						 System.out.println("3");
						 new AlertDialog.Builder(getActivity())
						    .setTitle("Invalid entry")
						    .setMessage("Please fill all alternate date/time for first block ")
						    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						        	
						        	return;
						        }
						     })
						   
						    .setIcon(android.R.drawable.ic_dialog_alert)
						     .show();
						 return;
					 }*/
					/* if(date_selected2.equals("")&&starttime_selected2.equals("")&&endtime_selected2.equals("")){
						 System.out.println("1");
					 }else if(date_selected2.length()>0 && starttime_selected2.length()>0 && endtime_selected2.length()>0 ){
						 System.out.println("2");
					 }else{
						 System.out.println("3");
						 new AlertDialog.Builder(getActivity())
						    .setTitle("Invalid entry")
						    .setMessage("Please fill all alternate date/time for second block ")
						    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						        	
						        	return;
						        }
						     })
						   
						    .setIcon(android.R.drawable.ic_dialog_alert)
						     .show();
						 return;
					 }*/
					/* if(date_selected3.equals("")&&starttime_selected3.equals("")&&endtime_selected3.equals("")){
						 System.out.println("1");
					 }else if(date_selected3.length()>0 && starttime_selected3.length()>0 && endtime_selected3.length()>0 ){
						 System.out.println("2");
					 }else{
						 System.out.println("3");
						 new AlertDialog.Builder(getActivity())
						    .setTitle("Invalid entry")
						    .setMessage("Please fill all alternate date/time for third block ")
						    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						        	
						        	return;
						        }
						     })
						   
						    .setIcon(android.R.drawable.ic_dialog_alert)
						     .show();
						 return;
					 }*/
					// location_selected=location.getText().toString();
					if(date_of_event.getText().toString().length()>0 && time_of_event.getText().toString().length()>0 && end_time_txt.getText().toString().length()>0 && place_of_event.getText().toString().length()>0){
						
						
						cd=new ConnectionDetector(getActivity());
						 isInternetPresent = cd.isConnectingToInternet();
						 if (isInternetPresent) {
							 
							/* String dateSelectedArr[]=date_selected.split("/");
							 String daySelected=dateSelectedArr[0];
							 String monthhSelected=dateSelectedArr[1];
							 String yearSelected=dateSelectedArr[2];
							 
							 String monthsSelected="";
							 if(monthhSelected.equalsIgnoreCase("jan"))
							 {
							  monthsSelected="01";
							 }else if(monthhSelected.equalsIgnoreCase("Feb"))
							 {
								  monthsSelected="02";
							 }else if(monthhSelected.equalsIgnoreCase("Mar"))
							 {
								  monthsSelected="03";
							 }else if(monthhSelected.equalsIgnoreCase("Apr"))
							 {
								  monthsSelected="04";
							 }else if(monthhSelected.equalsIgnoreCase("May"))
							 {
								  monthsSelected="05";
							 }else if(monthhSelected.equalsIgnoreCase("Jun"))
							 {
								  monthsSelected="06";
							 }else if(monthhSelected.equalsIgnoreCase("Jul"))
							 {
								  monthsSelected="07";
							 }else if(monthhSelected.equalsIgnoreCase("Aug"))
							 {
								  monthsSelected="08";
							 }else if(monthhSelected.equalsIgnoreCase("Sep"))
							 {
								  monthsSelected="09";
							 }else if(monthhSelected.equalsIgnoreCase("Oct"))
							 {
								  monthsSelected="10";
							 }else if(monthhSelected.equalsIgnoreCase("Nov"))
							 {
								  monthsSelected="11";
							 }else if(monthhSelected.equalsIgnoreCase("Dec"))
							 {
								  monthsSelected="12";
							 }
							String selectedDate=daySelected+"/"+monthsSelected+"/"+yearSelected;
							 Log.e("SelectedDate===",""+selectedDate);*/
							

							 
							 /* final Calendar c = Calendar.getInstance();
			 					 String mmmm = "",dddd="";
			 					 int mmm;
			 					    int mYear = c.get(Calendar.YEAR);
			 					    Log.e("Year==",""+mYear);
			 					    
			 					    int mMonth = c.get(Calendar.MONTH);
			 					     mmm=mMonth+1;
			 					    String ConvertMonth=String.valueOf(mmm);
			 					    Log.e("Month==",""+ConvertMonth);
			 					    if(mmm<10)
			 					    {
			 					    mmmm="0"+ConvertMonth;
			 					    }else
			 					    {
			 					    mmmm=ConvertMonth;
			 					    }
			 				
			 					    int mDay = c.get(Calendar.DAY_OF_MONTH);
			 					    Log.e("Day==",""+mDay);
			 					   String ConvertDay=String.valueOf(mDay);
			 					   if(mDay<10)
			 					   {
			 					   dddd="0"+ConvertDay;  
			 					   }else
			 					   {
			 					   dddd=ConvertDay;  
			 					   }
			 					  String currentDateStr=dddd+"/"+mmmm+"/"+mYear;
			 					    Log.e("FullDate==",""+currentDateStr);*/
							 SimpleDateFormat sdf_1 = new SimpleDateFormat("dd/MM/yy");
							 String currentDateStr = sdf.format(new Date());
			 					   SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yy");
			 					   
			 					  try {
									if (dfDate.parse(currentDateStr).equals(dfDate.parse(date_selected))) {
										Log.e("Equal==","Equal");
										clicked=false;
										new create_event().execute();
									  }
									else if (dfDate.parse(currentDateStr).before(dfDate.parse(date_selected))) {
										Log.e("Before==","Equal");
										clicked=false;
										new create_event().execute();
									  }else if (dfDate.parse(currentDateStr).after(dfDate.parse(date_selected)))
									  {
										  /*date_of_event.setEnabled(true);
										  time_of_event.setEnabled(true);
										  end_time_txt.setEnabled(true);
										  date_of_event.setClickable(true);
										  time_of_event.setClickable(true);
										  end_time_txt.setClickable(true);*/
										  Log.e("After==","Equal");
										  Toast.makeText(getActivity(), "Please change date", 1).show();
										  
									  }
								} catch (ParseException e) {
									e.printStackTrace();
								}
			 				
			 					   
//			 					    try {
//			 					        if (dfDate.parse(currentDateStr).before(dfDate.parse(endDate))) {
//			 					            b = true;  // If start date is before end date.
//			 					        } else if (dfDate.parse(startDate).equals(dfDate.parse(endDate))) {
//			 					            b = true;  // If two dates are equal.
//			 					        } else {
//			 					            b = false; // If start date is after the end date.
//			 					        }
//			 					    } catch (ParseException e) {
//			 					        e.printStackTrace();
//			 					    }
			 				
							 
//							 if (new Date().after(strDate)) {
//									//new create_event().execute();
//							 }else
//							 {
//							 Toast.makeText(getActivity(),"Please change date",2000).show(); 
//							 }
							 
								//new create_event().execute();
								
								
						 }
						 else{
							 Toast.makeText(getActivity(),"Please check internet connection",2000).show();
							
						 }
						
					
						
					}
					else{
						Toast.makeText(getActivity(), "No field will left blank", 2000).show();
						return;
					}
				
			}
		});
		
		
		
/*date1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			i=2;
			count_alert=0;
date_dialog=1;
				 Calendar c = Calendar.getInstance();

				myYear = c.get(Calendar.YEAR);
				myMonth = c.get(Calendar.MONTH);
				myDay = c.get(Calendar.DAY_OF_MONTH);
				try {
					String date_from_edit_text=date1.getText().toString();
					if(date_from_edit_text.equals("")||date_from_edit_text.equals(null)){
						
					}else{
						SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
						// SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yy");
						 Date date_1=null,date_2=null,date_3=null,date_4=null;
						 try {
							 date_1=sdf.parse(date_from_edit_text);
							
							 
							 
						} catch (ParseException e) {
							
							e.printStackTrace();
						}
						 SimpleDateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");
						
						               
					
						             // format the date into another format
						try {
							date_from_edit_text = destDf.format(date_1);
						} catch (Exception e) {
							// TODO: handle exception
						}
						String[] dateArr = date_from_edit_text.split("/");
						
						myDay=Integer.parseInt(dateArr[0]);
						myMonth=Integer.parseInt(dateArr[1])-1;
						myYear=Integer.parseInt(dateArr[2]);
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				DatePickerDialog d = new DatePickerDialog(getActivity(),
				         mDateSetListener, myYear, myMonth, myDay);
			
				d.show();
			}
		});*/
		/*starttime1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				i=2;
				start_dialog=1;
				clicke_on_timepicker=true;
				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistener, hours, minutes, true);
				tiiPickerDialog.setTitle("START TIME");
				tiiPickerDialog.show();
				 CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), mTimelistener, 
		                    Calendar.getInstance().get(Calendar.HOUR), 
		                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
		        timePickerDialog.setTitle("START TIME");
		        timePickerDialog.show();
			}
		});
*/		/*endtime1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				end_dialog=1;
				i=2;
				clicke_on_timepicker=true;
				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistenerto, hours, minutes, true);
				tiiPickerDialog.setTitle("END TIME");
				tiiPickerDialog.show();
				 CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), mTimelistenerto, 
		                    Calendar.getInstance().get(Calendar.HOUR), 
		                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
		        timePickerDialog.setTitle("END TIME");
		        timePickerDialog.show();
			}
		});
		*/
		
/*date2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			i=3;
			count_alert=0;
				date_dialog=2;
				 Calendar c = Calendar.getInstance();

				myYear = c.get(Calendar.YEAR);
				myMonth = c.get(Calendar.MONTH);
				myDay = c.get(Calendar.DAY_OF_MONTH);
				try {
					String date_from_edit_text=date2.getText().toString();
					if(date_from_edit_text.equals("")||date_from_edit_text.equals(null)){
						
					}else{
						SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
						// SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yy");
						 Date date_1=null,date_2=null,date_3=null,date_4=null;
						 try {
							 date_1=sdf.parse(date_from_edit_text);
							
							 
							 
						} catch (ParseException e) {
							
							e.printStackTrace();
						}
						 SimpleDateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");
						
						               
					
						             // format the date into another format
						try {
							date_from_edit_text = destDf.format(date_1);
						} catch (Exception e) {
							// TODO: handle exception
						}
						String[] dateArr = date_from_edit_text.split("/");
						
						myDay=Integer.parseInt(dateArr[0]);
						myMonth=Integer.parseInt(dateArr[1])-1;
						myYear=Integer.parseInt(dateArr[2]);
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				DatePickerDialog d = new DatePickerDialog(getActivity(),
				         mDateSetListener, myYear, myMonth, myDay);
			
				d.show();
			}
		});*/
		/*starttime2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				start_dialog=2;
				i=3;
				clicke_on_timepicker=true;
				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistener, hours, minutes, true);
				tiiPickerDialog.setTitle("START TIME");
				tiiPickerDialog.show();
				 CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), mTimelistener, 
		                    Calendar.getInstance().get(Calendar.HOUR), 
		                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
		        timePickerDialog.setTitle("START TIME");
		        timePickerDialog.show();
			}
		});*/
		/*endtime2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				end_dialog=2;
				i=3;
				clicke_on_timepicker=true;
				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistenerto, hours, minutes, true);
				tiiPickerDialog.setTitle("END TIME");
				tiiPickerDialog.show();
				 CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), mTimelistenerto, 
		                    Calendar.getInstance().get(Calendar.HOUR), 
		                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
		        timePickerDialog.setTitle("END TIME");
		        timePickerDialog.show();
			}
		});
		*/
		
		
/*date3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				count_alert=0;
				date_dialog=3;
				 Calendar c = Calendar.getInstance();

				myYear = c.get(Calendar.YEAR);
				myMonth = c.get(Calendar.MONTH);
				myDay = c.get(Calendar.DAY_OF_MONTH);
				try {
					String date_from_edit_text=date3.getText().toString();
					if(date_from_edit_text.equals("")||date_from_edit_text.equals(null)){
						
					}else{
						SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
						// SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yy");
						 Date date_1=null,date_2=null,date_3=null,date_4=null;
						 try {
							 date_1=sdf.parse(date_from_edit_text);
							
							 
							 
						} catch (ParseException e) {
							
							e.printStackTrace();
						}
						 SimpleDateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");
						
						               
					
						             // format the date into another format
						try {
							date_from_edit_text = destDf.format(date_1);
						} catch (Exception e) {
							// TODO: handle exception
						}
						String[] dateArr = date_from_edit_text.split("/");
						
						myDay=Integer.parseInt(dateArr[0]);
						myMonth=Integer.parseInt(dateArr[1])-1;
						myYear=Integer.parseInt(dateArr[2]);
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				DatePickerDialog d = new DatePickerDialog(getActivity(),
				         mDateSetListener, myYear, myMonth, myDay);
			
				d.show();
			}
		});*/
	/*	starttime3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				start_dialog=3;
				clicke_on_timepicker=true;
				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistener, hours, minutes, true);
				tiiPickerDialog.setTitle("START TIME");
				tiiPickerDialog.show();
				 CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), mTimelistener, 
		                    Calendar.getInstance().get(Calendar.HOUR), 
		                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
		        timePickerDialog.setTitle("START TIME");
		        timePickerDialog.show();
			}
		});*/
		/*endtime3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				end_dialog=3;
				clicke_on_timepicker=true;
				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistenerto, hours, minutes, true);
				tiiPickerDialog.setTitle("END TIME");
				tiiPickerDialog.show();
				 CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), mTimelistenerto, 
		                    Calendar.getInstance().get(Calendar.HOUR), 
		                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
		        timePickerDialog.setTitle("END TIME");
		        timePickerDialog.show();
			}
		});
		*/
		
/*alternatedatetime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		time="";time_to="";
				String date_pick=date.getText().toString();
				String start_pick=starttime.getText().toString();
				String end_pick=endtime.getText().toString();
				if(i==1){
				if(date_of_event.getText().toString().length()>0 && time_of_event.getText().toString().length()>0 && end_time_txt.getText().toString().length()>0){
					block1_date.setVisibility(View.VISIBLE);
					block1_endtime.setVisibility(View.VISIBLE);
					block1_starttime.setVisibility(View.VISIBLE);
				}else{
					Toast.makeText(getActivity(), "Date, Start time and End time can not be null",2000).show();
				}
				}else if(i==2){
					if(date1.getText().toString().length()>0 && starttime1.getText().toString().length()>0 && endtime1.getText().toString().length()>0){
						block2_date.setVisibility(View.VISIBLE);
						block2_endtime.setVisibility(View.VISIBLE);
						block2_starttime.setVisibility(View.VISIBLE);				
					}else{
						Toast.makeText(getActivity(), "Date1, Start time1 and End time1 can not be null",2000).show();
					}
					
				}else if(i==3){
					if(date2.getText().toString().length()>0 && starttime2.getText().toString().length()>0 && endtime2.getText().toString().length()>0){
						block3_date.setVisibility(View.VISIBLE);
						block3_endtime.setVisibility(View.VISIBLE);
						block3_starttime.setVisibility(View.VISIBLE);				
					}else{
						Toast.makeText(getActivity(), "Date2, Start time2 and End time2 can not be null",2000).show();
					}
					
				}
				
			}
		});
		*/
date_of_event.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		count_alert=0;
	if(clicked==false){
		
	}else{
		
		i=1;

		 Calendar c = Calendar.getInstance();

		myYear = c.get(Calendar.YEAR);
		myMonth = c.get(Calendar.MONTH);
		myDay = c.get(Calendar.DAY_OF_MONTH);
		try {
			String date_from_edit_text=date_of_event.getText().toString();
			if(date_from_edit_text.equals("")||date_from_edit_text.equals(null)){
				
			}else{
				SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
				// SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yy");
				 Date date_1=null,date_2=null,date_3=null,date_4=null;
				 try {
					 date_1=sdf.parse(date_from_edit_text);
					
					 
					 
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
				 SimpleDateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");
				
				               
			
				             // format the date into another format
				try {
					date_from_edit_text = destDf.format(date_1);
				} catch (Exception e) {
					// TODO: handle exception
				}
				String[] dateArr = date_from_edit_text.split("/");
				
				myDay=Integer.parseInt(dateArr[0]);
				myMonth=Integer.parseInt(dateArr[1])-1;
				myYear=Integer.parseInt(dateArr[2]);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		firstTime=true;
		if(firstTime=true)
		{
			DatePickerDialog d = new DatePickerDialog(getActivity(),mDateSetListener, myYear, myMonth, myDay);
			firstTime=false;
			d.show();
		}
	
	}
	}
});
time_of_event.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
	
		if(clicked==false){
			
		}else{
		i=1;
		clicke_on_timepicker=true;
		
		String timeEvent=time_of_event.getText().toString().trim();
		Log.e("TimeEvents==",""+timeEvent);
		String [] splitArr=timeEvent.split(":");
		String houursSplit=splitArr[0];
		int mHours=Integer.parseInt(houursSplit);
		Log.e("HInt==",""+mHours);
		
		
		String minSplit=splitArr[1];
		int mMin=Integer.parseInt(minSplit);
		Log.e("HMIN==",""+mHours);
		
		firstTime=true;
		if(firstTime=true)
		{
		/*TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistener, mHours, mMin, true);
		tiiPickerDialog.setTitle("START TIME");
		tiiPickerDialog.show();	*/
			 CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), mTimelistener, 
	                    Calendar.getInstance().get(Calendar.HOUR), 
	                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
	        timePickerDialog.setTitle("START TIME");
	        timePickerDialog.show();
		firstTime=false;
		}
	}}
});
end_time_txt.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(clicked==false){
			
		}else{
		i=1;
		clicke_on_timepicker=true;
		Log.e("Hello==","Hello");
		String endTimeStr=end_time_txt.getText().toString().trim();
		String endTimeArr[]=endTimeStr.split(":");
		
		String houursSplit=endTimeArr[0];
		int mHoursEnd=Integer.parseInt(houursSplit);
		Log.e("HInt==",""+mHoursEnd);
		
		
		String minSplitEnd=endTimeArr[1];
		int mMinEnd=Integer.parseInt(minSplitEnd);
		Log.e("HMIN==",""+mMinEnd);
		
		firstTime=true;
		if(firstTime==true)
		{
			/*TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistenerto, mHoursEnd, mMinEnd, true);
			tiiPickerDialog.setTitle("END TIME");
			tiiPickerDialog.show();*/
			 CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), mTimelistenerto, 
	                    Calendar.getInstance().get(Calendar.HOUR), 
	                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
	        timePickerDialog.setTitle("END TIME");
	        timePickerDialog.show();
			firstTime=false;
		}
	}
	}
});
		
		
		
		return view;
	}
	
	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		

	    public void onDateSet(DatePicker view, int year,
	            int monthOfYear, int dayOfMonth) {
	       /* myYear = year;
	        myMonth = monthOfYear;
	        myDay = dayOfMonth;*/
	        monthOfYear=monthOfYear+1;
	        String day=String.valueOf(dayOfMonth);
	        String month=String.valueOf(monthOfYear);
	        String year1=String.valueOf(year);
	        int i=day.length();
	        if(i==1){
	        	day="0"+day;
	        	
	        }
	        int i1=month.length();
	        if(i1==1){
	        	month="0"+month;
	        	
	        }
	        int i2=year1.length();
	        if(i2==1){
	        	year1="0"+year1;
	        	
	        }
	       String date_generated=day+"/"+month+"/"+year1; 
	       
	       try{
	    	   
	    	   SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
				
				 Date date_1=null;
				 try {
					 date_1=sdf.parse(date_generated);
					
					 
					 
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
	    	   
	    	   
	    	   
	    	   
	    	   
	    	   
	    	   
	    	   

			      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
			       Date date1_1 = formatter.parse(date_to_set);
			       Date date2_2 = formatter.parse(date_comparision);
			    if (date1_1.compareTo(date2_2)<0)
			    {
			    	if(count_alert==0){
			    		count_alert=count_alert+1;
			    	new AlertDialog.Builder(getActivity())
					
				    .setTitle("Invalid Entry")
				    .setMessage("can't select past date for event creation")
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
			    	if(date_dialog==0){
			    		
			    		
				    	   //date_of_event.setText(date_generated);
			    	  /*  String 	monthFulls="";
			    		String fullDate[]=date_generated.split("-");
			    		
			    		String monthFull=fullDate[1];
			    		if(monthFull.equalsIgnoreCase("01"))
			    		{
			    		monthFulls="jan";
			    		}else if(monthFull.equalsIgnoreCase("02"))
			    		{
			    			monthFulls="Feb";
			    		}else if(monthFull.equalsIgnoreCase("03"))
			    		{
			    			monthFulls="Mar";
			    		}else if(monthFull.equalsIgnoreCase("04"))
			    		{
			    			monthFulls="Apr";
			    		}else if(monthFull.equalsIgnoreCase("05"))
			    		{
			    			monthFulls="May";
			    		}else if(monthFull.equalsIgnoreCase("06"))
			    		{
			    			monthFulls="Jun";
			    		}else if(monthFull.equalsIgnoreCase("07"))
			    		{
			    			monthFulls="Jul";
			    		}else if(monthFull.equalsIgnoreCase("08"))
			    		{
			    		monthFull="Aug";
			    		}else if(monthFull.equalsIgnoreCase("09"))
			    		{
			    			monthFulls="Sep";
			    		}else if(monthFull.equalsIgnoreCase("10"))
			    		{
			    			monthFulls="Oct";
			    		}else if(monthFull.equalsIgnoreCase("11"))
			    		{
			    			monthFulls="Nov";
			    		}else if(monthFull.equalsIgnoreCase("12"))
			    		{
			    			monthFulls="Dec";
			    		}
			    		
			    		String dayFull=fullDate[0];
			    		String yearFull=fullDate[2];*/
			    		
			    		//date_of_event.setText(dayFull+"-"+monthFulls+"-"+yearFull);
			    		date_of_event.setText(date_generated);
				       }
				      /* if(date_dialog==1){
					       date1.setText(date_generated);
					       }
				       if(date_dialog==2){
					       date2.setText(date_generated);
					       }
				       if(date_dialog==3){
					       date3.setText(date_generated);
					       }*/
			    }
			 } catch (ParseException e1) 
		      {
		    e1.printStackTrace();
		                        }
	       
	       
	       
	    }
	};
	TimePickerDialog.OnTimeSetListener mTimelistener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			if(clicke_on_timepicker==true){
				clicke_on_timepicker=false;
			String hour_s=String.valueOf(hourOfDay);
			int length=hour_s.length();
			if(length==1){
				hour_s="0"+String.valueOf(hourOfDay);
			}
			String minut_s=String.valueOf(minute);
			int length1=minut_s.length();
			if(length1==1){
				minut_s="0"+String.valueOf(minute);
			}
			
			
			 time=hour_s+":"+minut_s;
			 if(time_to.equals("")||time.equals(null)){
				 if(start_dialog==0){
					 time_of_event.setText(time);
				       }
				      /* if(start_dialog==1){
				    	   starttime1.setText(time);
					       }
				       if(start_dialog==2){
				    	   starttime2.setText(time);
					       }
				       if(start_dialog==3){
				    	   starttime3.setText(time);
					       }*/
			 }else{
				 String pattern = "HH:mm";
		            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		            try {
		                Date date1_1 = sdf.parse(time);
		                Date date2_1 = sdf.parse(time_to);

		                
		                if(date1_1.compareTo(date2_1)>0 || date1_1.compareTo(date2_1)==0){
		                	
					    	new AlertDialog.Builder(getActivity())
							
						    .setTitle("Invalid Entry")
						    .setMessage("Start time can't greater than or equal end time")
						    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						        		return;			        	
						        }
						     })
						   
						    .setIcon(android.R.drawable.ic_dialog_alert)
						     .show();
					    	
		                }else{
		                	 if(start_dialog==0){
		        				 time_of_event.setText(time);
		        			       }
		        			       /*if(start_dialog==1){
		        			    	   starttime1.setText(time);
		        				       }
		        			       if(start_dialog==2){
		        			    	   starttime2.setText(time);
		        				       }
		        			       if(start_dialog==3){
		        			    	   starttime3.setText(time);
		        				       }*/
		                }
		                
		              

		            } catch (ParseException e){
		                // Exception handling goes here
		            }
			 }
			 
			
			    }
			 
		}
		
	};
TimePickerDialog.OnTimeSetListener mTimelistenerto = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			if(clicke_on_timepicker==true){
				clicke_on_timepicker=false;
			String hour_s=String.valueOf(hourOfDay);
			int length=hour_s.length();
			if(length==1){
				hour_s="0"+String.valueOf(hourOfDay);
			}
			String minut_s=String.valueOf(minute);
			int length1=minut_s.length();
			if(length1==1){
				minut_s="0"+String.valueOf(minute);
			}
			
			
			 time_to=hour_s+":"+minut_s;
			
			 if(time.equals("")||time.equals(null)){
				 if(end_dialog==0){
					 end_time_txt.setText(time_to);
				       }
				      /* if(end_dialog==1){
				    	   endtime1.setText(time_to);
					       }
				       if(end_dialog==2){
				    	   endtime2.setText(time_to);
					       }
				       if(end_dialog==3){
				    	   endtime3.setText(time_to);
					       }*/
				}else{
				 String pattern = "HH:mm";
		            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		            try {
		                Date date1_1 = sdf.parse(time);
		                Date date2_1 = sdf.parse(time_to);

		                
		                if(date1_1.compareTo(date2_1)>0 || date1_1.compareTo(date2_1)==0){
		                	
					    	new AlertDialog.Builder(getActivity())
							
						    .setTitle("Invalid Entry")
						    .setMessage("End Time can't less than or equal start time")
						    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						        		return;			        	
						        }
						     })
						   
						    .setIcon(android.R.drawable.ic_dialog_alert)
						     .show();
					    	
		                }else{
		                	 if(end_dialog==0){
		        				 end_time_txt.setText(time_to);
		        			       }
		        			      /* if(end_dialog==1){
		        			    	   endtime1.setText(time_to);
		        				       }
		        			       if(end_dialog==2){
		        			    	   endtime2.setText(time_to);
		        				       }
		        			       if(end_dialog==3){
		        			    	   endtime3.setText(time_to);
		        				       }*/
		                }
		                
		                // Outputs -1 as date1 is before date2
		               /* System.out.println(date1.compareTo(date2));

		                // Outputs 1 as date1 is after date1
		                System.out.println(date2.compareTo(date1));

		                date2 = sdf.parse("19:28");         
		                // Outputs 0 as the dates are now equal
		                System.out.println(date1.compareTo(date2));*/

		            } catch (ParseException e){
		                // Exception handling goes here
		            }
				 
				 
				}
			 
			
			}
		}
};
	
	public  class accept_reject extends AsyncTask<String, Integer, String>{
		ProgressDialog dialog=new ProgressDialog(getActivity());
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
				url=Constants.EVENT_ACCEPT_REJECT;//"http://54.191.67.152/playdate/event_accept_or_reject.php";//?event_id=64&status=rejected;
				}
			
			
			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				
				
					HttpClient httpClient = new DefaultHttpClient();
			        HttpContext localContext = new BasicHttpContext();
			        HttpPost httpPost = new HttpPost(url);

			        
			      ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			      nameValuePairs.add(new BasicNameValuePair("event_id",event_id));
			      nameValuePairs.add(new BasicNameValuePair("status",ststus_of_event));
			     
			    
			      
					
					try {
						httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));

						System.out.println(httpPost);
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}


			        HttpResponse response = null;
					try {
						response = httpClient.execute(httpPost,
						        localContext);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        BufferedReader reader = null;
					try {
						reader = new BufferedReader(
						        new InputStreamReader(
						                response.getEntity().getContent(), "UTF-8"));
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
						
						System.out.println("response"+sResponse);
			        } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				try {
					JSONObject json= new JSONObject(sResponse);
					
				 data=json.getString("success");
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			      
			       
			      
				
				
				return null;
			}
			
			protected void onPostExecute(String resultt) {
				

				dialog.dismiss();
				if(data.equals("1")){
				
			//	Bundle bundle = new Bundle();
			//	bundle.putString("user_guardian_id", guardian_id);
				android.support.v4.app.Fragment  fragment=new Home_fragment();
		    //    fragment.setArguments(bundle);
		        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.addToBackStack("first10");
				fragmentTransaction.commit();
				/*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
				        .replace(R.id.content_frame, fragment).commit();*/
				
				}
				else{
					Toast.makeText(getActivity(),"Please Try again Later", 2000).show();	
				//	Bundle bundle = new Bundle();
				//	bundle.putString("user_guardian_id", guardian_id);
					android.support.v4.app.Fragment  fragment=new Home_fragment();
			    //    fragment.setArguments(bundle);
			        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);
					fragmentTransaction.addToBackStack("first9");
					fragmentTransaction.commit();
					/*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
					        .replace(R.id.content_frame, fragment).commit();*/
					}
	}
	/*@Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub

            FragmentManager manager = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();

        super.destroyItem(container, position, object);
    }*/
}
	
	public  class create_event extends AsyncTask<String, Integer, String>{
		ProgressDialog dialog=new ProgressDialog(getActivity());
			@Override
		protected void onPreExecute() {
				
				dialog.setMessage("Loading.......please wait");
				dialog.setCancelable(false);
				dialog.show();
				url_create=Constants.EDIT_EVENT;//"http://54.191.67.152/playdate/event_edit.php";//?child_id=68&friend_childid=69&date=31-5-2014&starttime=11:04&endtime=3:00&date1=31-1-2014&starttime1=12:34&endtime1=4:00&date2=28-3-2014&starttime2=1:34&endtime2=7:00&date3=3-4-2014&starttime3=2:34&endtime3=6:00&location=delhi&notes=abc&publish=1&event_id=";
			}
			//http://54.191.67.152/playdate/event_edit.php?starttime=1:03AM&publish=1&friend_childid=323&child_id=325&location=LOCATION&g_id=100001122280002&receiver_id=100001122280002&notes=NOTES&endtime=1:01AM&event_id=17&receiver_status=requested&date=2020/07/16
			@Override
			protected String doInBackground(String... arg0) {
				
				HttpClient httpClient = new DefaultHttpClient();
		        HttpContext localContext = new BasicHttpContext();
		        HttpPost httpPost = new HttpPost(url_create);
		       
		        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		        nameValuePairs.add(new BasicNameValuePair("child_id",child_id));
		        nameValuePairs.add(new BasicNameValuePair("friend_childid",friend_id));
		        nameValuePairs.add(new BasicNameValuePair("date",eventdate));
		        nameValuePairs.add(new BasicNameValuePair("starttime",starttime_selected));
		        nameValuePairs.add(new BasicNameValuePair("endtime",endtime_selected));
		        
		       // nameValuePairs.add(new BasicNameValuePair("date1",eventdate1));
		       // nameValuePairs.add(new BasicNameValuePair("starttime1",starttime_selected1));
		     //   nameValuePairs.add(new BasicNameValuePair("endtime1",endtime_selected1));
		        
		     //   nameValuePairs.add(new BasicNameValuePair("date2",eventdate2));
		        nameValuePairs.add(new BasicNameValuePair("g_id",guardian_id));
		        System.out.println("guardian id"+guardian_id);
		       nameValuePairs.add(new BasicNameValuePair("receiver_id",sender_id));
		        System.out.println("receiver id"+sender_id);
		    //    nameValuePairs.add(new BasicNameValuePair("starttime2",starttime_selected2));
		   //     nameValuePairs.add(new BasicNameValuePair("endtime2",endtime_selected2));
		        
		    //    nameValuePairs.add(new BasicNameValuePair("date3",eventdate3));
		  //      nameValuePairs.add(new BasicNameValuePair("starttime3",starttime_selected3));
		  //      nameValuePairs.add(new BasicNameValuePair("endtime3",endtime_selected3));
		        
		        nameValuePairs.add(new BasicNameValuePair("location",location_of_event));
		        
		        nameValuePairs.add(new BasicNameValuePair("notes",notes));
		        
		        nameValuePairs.add(new BasicNameValuePair("event_id",event_id));
		        nameValuePairs.add(new BasicNameValuePair("publish","1"));
		        
		        StringBuilder sbb = new StringBuilder();

				sbb.append("http://54.191.67.152/playdate/event_edit.php?");
				sbb.append(nameValuePairs.get(0)+"&");
				sbb.append(nameValuePairs.get(1)+"&");
				
				sbb.append(nameValuePairs.get(2)+"&");
				sbb.append(nameValuePairs.get(3)+"&");
				sbb.append(nameValuePairs.get(4)+"&");
				sbb.append(nameValuePairs.get(5)+"&");
				sbb.append(nameValuePairs.get(6)+"&");
				sbb.append(nameValuePairs.get(7)+"&");
				sbb.append(nameValuePairs.get(8)+"&");
				sbb.append(nameValuePairs.get(9)+"&");
				
				sbb.append(nameValuePairs.get(10));
				System.out.println(sbb);
		        try {
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));

					System.out.println(httpPost);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}


		        HttpResponse response = null;
				try {
					response = httpClient.execute(httpPost,
					        localContext);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		        BufferedReader reader = null;
				try {
					reader = new BufferedReader(
					        new InputStreamReader(
					                response.getEntity().getContent(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

		        try {
					String sResponse = reader.readLine();
					
					System.out.println("response"+sResponse);
					JSONObject json;
					try {
						json = new JSONObject(sResponse);
						
						
						 success=json.getString("success");
						
						 
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					
						
				} catch (IOException e) {
					e.printStackTrace();
				}
		        
		        
				return null;
			}
		protected void onPostExecute(String resultt) {
				dialog.dismiss();
				if(success.equals("1")){
					
				//	Bundle bundle = new Bundle();
				//	bundle.putString("user_guardian_id", guardian_id);
					android.support.v4.app.Fragment  fragment=new Home_fragment();
			   //     fragment.setArguments(bundle);
			        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);
					fragmentTransaction.addToBackStack("first8");
					fragmentTransaction.commit();
					/*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();*/
					}
					else{
						Toast.makeText(getActivity(),"Please Try again Later", 2000).show();	
				//		Bundle bundle = new Bundle();
				//		bundle.putString("user_guardian_id", guardian_id);
						android.support.v4.app.Fragment  fragment=new Home_fragment();
				//        fragment.setArguments(bundle);
				        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
						android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
						fragmentTransaction.replace(R.id.content_frame, fragment);
						fragmentTransaction.addToBackStack("first7");
						fragmentTransaction.commit();
					/*	android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();*/
						}		
		}
	}
	public static class CustomTimePickerDialog extends TimePickerDialog{
		 
	    public static final int TIME_PICKER_INTERVAL=15;
	    private boolean mIgnoreEvent=false;

	    public CustomTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
	    super(context, callBack, hourOfDay, minute, is24HourView);
	    }

	    @Override
	    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
	        super.onTimeChanged(timePicker, hourOfDay, minute);
	        if (!mIgnoreEvent){
	            minute = getRoundedMinute(minute);
	            mIgnoreEvent=true;
	            timePicker.setCurrentMinute(minute);
	            mIgnoreEvent=false;
	        }
	    }

	    public static  int getRoundedMinute(int minute){
	         if(minute % TIME_PICKER_INTERVAL != 0){
	            int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
	            minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
	            if (minute == 60)  minute=0;
	         }

	        return minute;
	    }
	}
}
