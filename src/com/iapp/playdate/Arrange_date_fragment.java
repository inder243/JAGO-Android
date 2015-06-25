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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iapp.playdate.R;
import com.iapptechnologies.time.util.Constants;
import com.mixpanel.android.mpmetrics.MixpanelAPI;


public class Arrange_date_fragment extends android.support.v4.app.Fragment {
	
Float x1,y1,x2,y2;
EditText date,starttime,endtime,location,notes_edittext;
Button send_request,save_request,add_child,add_friend,add_child_view;
int myYear,myMonth,myDay;
int hours,minutes;
String time="",time_to="",user_guardian_id,url,facebook_friends,friend_id="",child_id="",eventdate,eventdate1,eventdate2,eventdate3,image_url;
//Spinner child,friend;
String publish;
SharedPreferences settings;
SharedPreferences.Editor editor;
TextView txt_child_friend_name,txt_child_name;
ArrayList<childname> child_name_list = new ArrayList<childname>();
ArrayList<childname> child_name_list_friend = new ArrayList<childname>();
ArrayList<String> child_name_forspinner = new ArrayList<String>();
ArrayList<String> child_id_forspinner = new ArrayList<String>();
ArrayList<childfriendname> child_friend_name = new ArrayList<childfriendname>();
ArrayList<String> friend_name_forspinner = new ArrayList<String>();
ArrayList<String> friend_id_forspinner = new ArrayList<String>();
ArrayList<String> friend_profilepic = new ArrayList<String>();
ArrayList<String>friend_id_repeat_check=new ArrayList<String>();
String receiver_id,notes="",date_selected,starttime_selected,endtime_selected,location_selected,success,date_selected1,starttime_selected1,endtime_selected1,date_selected2,starttime_selected2,endtime_selected2,date_selected3,starttime_selected3,endtime_selected3;
int i=0;//
Boolean isInternetPresent = false;
ConnectionDetector cd;
int date_dialog=0,start_dialog=0,end_dialog=0;
String parent_id,date_comparision;
ImageView child_image,child_friend_image;
com.iapptechnologies.time.util.ImageLoader imageLoader;
boolean clicked=false;
int count_alert=0;
LazyAdapter adapter;
	public Arrange_date_fragment(){
		
	}
	@SuppressWarnings("unused")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.request_playdate, container,
                false);
		settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
		editor = settings.edit();
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());
        Home.menu.setVisibility(View.GONE);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		date_comparision = df.format(c.getTime());
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		child_image=(ImageView)view.findViewById(R.id.imageView_child_arrange);
		child_friend_image=(ImageView)view.findViewById(R.id.imageView_child_friend_arrange);
        send_request=(Button)view.findViewById(R.id.button1_requestdate_send);
        add_child_view=(Button)view.findViewById(R.id.addchild_view);
        add_child=(Button)view.findViewById(R.id.add_more_child);
        add_friend=(Button)view.findViewById(R.id.add_more_friend);
		user_guardian_id=settings.getString("USERID","");
		facebook_friends=settings.getString("FBFRIEND","");
		date=(EditText)view.findViewById(R.id.edit_requestdate_date);
		starttime=(EditText)view.findViewById(R.id.edit_requestdate_starttime);
		endtime=(EditText)view.findViewById(R.id.edit_requestdate_endtime);
		notes_edittext=(EditText)view.findViewById(R.id.edit_requestdate_notes);
		txt_child_friend_name=(TextView)view.findViewById(R.id.txt_friend_name);
        txt_child_name=(TextView)view.findViewById(R.id.txt_child_name);
		location=(EditText)view.findViewById(R.id.edit_requestdate_location);
		
		
		 int density = getResources().getDisplayMetrics().densityDpi;
		  switch (density) {
		  case DisplayMetrics.DENSITY_LOW:
			  child_friend_image.getLayoutParams().height = 40;
			  child_friend_image.getLayoutParams().width = 40;
			  child_image.getLayoutParams().height = 40;
			  child_image.getLayoutParams().width = 40;
			 
			  break;
		  case DisplayMetrics.DENSITY_MEDIUM:
			  child_friend_image.getLayoutParams().height = 85;
			  child_friend_image.getLayoutParams().width = 85;
			  child_image.getLayoutParams().height = 85;
			  child_image.getLayoutParams().width = 85;
			  break;
		  case DisplayMetrics.DENSITY_HIGH:
			  child_friend_image.getLayoutParams().height = 85;
			  child_friend_image.getLayoutParams().width = 85;
			  child_image.getLayoutParams().height = 85;
			  child_image.getLayoutParams().width = 85;
			  break;
		  case DisplayMetrics.DENSITY_XHIGH:
			  child_friend_image.getLayoutParams().height = 120;
			  child_friend_image.getLayoutParams().width = 120;
			  child_image.getLayoutParams().height = 120;
			  child_image.getLayoutParams().width = 120;
			  break;
		  case DisplayMetrics.DENSITY_XXHIGH:
			  child_friend_image.getLayoutParams().height = 120;
			  child_friend_image.getLayoutParams().width = 120;
			  child_image.getLayoutParams().height = 120;
			  child_image.getLayoutParams().width = 120;
			  break;
			  
		  }
		  
		imageLoader=new com.iapptechnologies.time.util.ImageLoader(getActivity());
		 cd=new ConnectionDetector(getActivity());
		 isInternetPresent = cd.isConnectingToInternet();

		 if (isInternetPresent) {
			 new getchild_webservice().execute();
				
		 }
		 else{
			 Toast.makeText(getActivity(),"Please check internet connection",2000).show();
			
		 }
		
		
		date.setFocusable(false);
		date.setClickable(true);

		starttime.setFocusable(false);
		starttime.setClickable(true);
		
		endtime.setFocusable(false);
		endtime.setClickable(true);
		

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
		
		add_child_view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				Bundle bundle = new Bundle();
				bundle.putString("name", "");
				bundle.putString("url", url);
				bundle.putString("freetime", "");
				bundle.putString("dob", "");
				bundle.putString("guardiantype", "");
				bundle.putString("location", "");
				bundle.putString("firstname", "");
				bundle.putString("facebook_id", "");
				bundle.putString("user_guardian_id", user_guardian_id);
				bundle.putString("facebook_friends", facebook_friends);
				bundle.putString("phone", "");*/
				
				android.support.v4.app.Fragment fragment = new Add_Child();
		//		fragment.setArguments(bundle);
				
				android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.addToBackStack("first");
				fragmentTransaction.commit();
				
			}
		});
		
		
         add_child.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				 final Dialog dialog = new Dialog(getActivity());
				 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.home);
				
				//dialog.setTitle("SELECT CHILD");
				ListView list_child=(ListView)dialog.findViewById(R.id.listView1_home);
				list_child.setDivider(null);
				list_child.setDividerHeight(0);
				
				if(child_name_list.size()>0){
					adapter = new LazyAdapter(getActivity(), child_name_list);

					list_child.setAdapter(adapter);
					
					
				}else{
					Toast.makeText(getActivity(),"Please add child",1).show();
				}
				list_child.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						
						child_id=child_name_list.get(arg2).id;
						image_url=child_name_list.get(arg2).child_profile;
				         imageLoader.DisplayImage(image_url, child_image);
				         child_image.requestLayout();
				         txt_child_name.setText(child_name_list.get(arg2).name.toUpperCase());
				         
				         int density = getResources().getDisplayMetrics().densityDpi;
						  switch (density) {
						  case DisplayMetrics.DENSITY_LOW:
							  child_image.getLayoutParams().height = 40;
							  child_image.getLayoutParams().width = 40;
							 
							  break;
						  case DisplayMetrics.DENSITY_MEDIUM:
							  child_image.getLayoutParams().height = 85;
							  child_image.getLayoutParams().width = 85;
							  break;
						  case DisplayMetrics.DENSITY_HIGH:
							  child_image.getLayoutParams().height = 85;
							  child_image.getLayoutParams().width = 85;
							  break;
						  case DisplayMetrics.DENSITY_XHIGH:
							  child_image.getLayoutParams().height = 120;
							  child_image.getLayoutParams().width = 120;
							  break;
						  case DisplayMetrics.DENSITY_XXHIGH:
							  child_image.getLayoutParams().height = 120;
							  child_image.getLayoutParams().width = 120;
							  break;
							  
						  }
						  dialog.dismiss();
						
					}
				});
					
				
				dialog.show();
			
				
			}
		});
add_friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 final Dialog dialog = new Dialog(getActivity());
				 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.home);
				//dialog.setTitle("SELECT FRIEND");
				ListView list_child=(ListView)dialog.findViewById(R.id.listView1_home);
				list_child.setDivider(null);
				list_child.setDividerHeight(0);
				if(child_name_list_friend.size()>0){
					adapter = new LazyAdapter(getActivity(), child_name_list_friend);

					list_child.setAdapter(adapter);
					
					
				}else{
					Toast.makeText(getActivity(),"Please add child",1).show();
					dialog.dismiss();
				}
				list_child.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						   
						     receiver_id=child_name_list_friend.get(arg2).g_id;
					         System.out.println("parent_child_friend.............."+parent_id);
					         friend_id=child_name_list_friend.get(arg2).id;
					         String image_url_1= friend_profilepic.get(arg2);
					         txt_child_friend_name.setText(child_name_list_friend.get(arg2).name.toUpperCase());
					         System.out.println("..................."+child_id);
					         imageLoader.DisplayImage(image_url_1, child_friend_image);
					         child_friend_image.requestLayout();
					         int density = getResources().getDisplayMetrics().densityDpi;
							  switch (density) {
							  case DisplayMetrics.DENSITY_LOW:
								  child_friend_image.getLayoutParams().height = 40;
								  child_friend_image.getLayoutParams().width = 40;
								 
								  break;
							  case DisplayMetrics.DENSITY_MEDIUM:
								  child_friend_image.getLayoutParams().height = 85;
								  child_friend_image.getLayoutParams().width = 85;
								  break;
							  case DisplayMetrics.DENSITY_HIGH:
								  child_friend_image.getLayoutParams().height = 85;
								  child_friend_image.getLayoutParams().width = 85;
								  break;
							  case DisplayMetrics.DENSITY_XHIGH:
								  child_friend_image.getLayoutParams().height = 120;
								  child_friend_image.getLayoutParams().width = 120;
								  break;
							  case DisplayMetrics.DENSITY_XXHIGH:
								  child_friend_image.getLayoutParams().height = 120;
								  child_friend_image.getLayoutParams().width = 120;
								  break;
								  
							  }
							  dialog.dismiss();
					}
				});
					
				
				dialog.show();
			
				
			}
		});

		
		child_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int id_child=0;
				
				if(child_id.equals("")||child_id.equals(null)){
					
				}else{
					for(childname child : child_name_list){
						if(child.id.equals(child_id)){
							String childname=	child_name_list.get(id_child).name;
							String childallergies=	child_name_list.get(id_child).allergies;
							String childdob=	child_name_list.get(id_child).date_of_birth;
						//	String childfreetime=	child_name_list.get(id_child).freetime;
						//	String childhobbies=	child_name_list.get(id_child).hobbies;
						//	String childschool=	child_name_list.get(id_child).school;
						//	String childyouthclub=	child_name_list.get(id_child).youthclub;
								
								
							final Dialog dialog = new Dialog(getActivity());
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.child_detail_arrange_view);
						//	dialog.setTitle("CHILD DETAILS");
				 
							// set the custom dialog components - text, image and button
							TextView text_name = (TextView) dialog.findViewById(R.id.arrange_childname);
							TextView text_dob = (TextView) dialog.findViewById(R.id.textchild_dob_arrange);
					//		TextView text_freetime = (TextView) dialog.findViewById(R.id.freetime_child_arrange);
							TextView text_allergies = (TextView) dialog.findViewById(R.id.text_child_allergies_arrange);
					//		TextView text_hobbies = (TextView) dialog.findViewById(R.id.text_child_hobbies_arrange);
					//		TextView text_school = (TextView) dialog.findViewById(R.id.text_child_school_arrange);
						//	TextView text_youthclub = (TextView) dialog.findViewById(R.id.text_child_youth_arrange);
							
							text_name.setText(childname.toUpperCase());
							text_dob.setText(childdob.toUpperCase());
					//		text_freetime.setText(childfreetime.toUpperCase());
							text_allergies.setText(childallergies.toUpperCase());
					//		text_hobbies.setText(childhobbies.toUpperCase());
					//		text_school.setText(childschool.toUpperCase());
						//	text_youthclub.setText(childyouthclub.toUpperCase());
				 

				 
							dialog.show();
						}
						id_child++;
						}
					}
					
				
			}
		});
child_friend_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int id_child=0;
				
				if(friend_id.equals("")||friend_id.equals(null)){
					
				}else{
					for(childname child : child_name_list_friend){
						if(child.id.equals(friend_id)){
							String childname=	child_name_list_friend.get(id_child).name;
							String childallergies=	child_name_list_friend.get(id_child).allergies;
							String childdob="";	
						//	String childfreetime=	child_name_list_friend.get(id_child).freetime;
						//	String childhobbies=	child_name_list_friend.get(id_child).hobbies;
						//	String childschool=	child_name_list_friend.get(id_child).school;
						//	String childyouthclub=	child_name_list_friend.get(id_child).youthclub;
								
								
							final Dialog dialog = new Dialog(getActivity());
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.child_detail_arrange_view);
							
							//dialog.setTitle("CHILD DETAILS");
				 
							// set the custom dialog components - text, image and button
							TextView text_name = (TextView) dialog.findViewById(R.id.arrange_childname);
							TextView text_dob = (TextView) dialog.findViewById(R.id.textchild_dob_arrange);
						//	TextView text_freetime = (TextView) dialog.findViewById(R.id.freetime_child_arrange);
							TextView text_allergies = (TextView) dialog.findViewById(R.id.text_child_allergies_arrange);
						//	TextView text_hobbies = (TextView) dialog.findViewById(R.id.text_child_hobbies_arrange);
						//	TextView text_school = (TextView) dialog.findViewById(R.id.text_child_school_arrange);
						//	TextView text_youthclub = (TextView) dialog.findViewById(R.id.text_child_youth_arrange);
							
							text_name.setText(childname.toUpperCase());
							text_dob.setText(childdob.toUpperCase());
					//		text_freetime.setText(childfreetime.toUpperCase());
							text_allergies.setText(childallergies.toUpperCase());
					//		text_hobbies.setText(childhobbies.toUpperCase());
					//		text_school.setText(childschool.toUpperCase());
						//	text_youthclub.setText(childyouthclub.toUpperCase());
				 

				 
							dialog.show();
						}
						id_child++;
						}
					}
					
				
			}
		});
		
/*alternatedatetime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		time="";
		time_to="";
				String date_pick=date.getText().toString();
				String start_pick=starttime.getText().toString();
				String end_pick=endtime.getText().toString();
				if(i==1){
				if(date.getText().toString().length()>0 && starttime.getText().toString().length()>0 && endtime.getText().toString().length()>0){
					date1.setVisibility(View.VISIBLE);
					starttime1.setVisibility(View.VISIBLE);
					endtime1.setVisibility(View.VISIBLE);					
				}else{
					Toast.makeText(getActivity(), "Date, Start time and End time can not be null",2000).show();
				}
				}else if(i==2){
					if(date1.getText().toString().length()>0 && starttime1.getText().toString().length()>0 && endtime1.getText().toString().length()>0){
						date2.setVisibility(View.VISIBLE);
						starttime2.setVisibility(View.VISIBLE);
						endtime2.setVisibility(View.VISIBLE);					
					}else{
						Toast.makeText(getActivity(), "Date1, Start time1 and End time1 can not be null",2000).show();
					}
					
				}else if(i==3){
					if(date2.getText().toString().length()>0 && starttime2.getText().toString().length()>0 && endtime2.getText().toString().length()>0){
						date3.setVisibility(View.VISIBLE);
						starttime3.setVisibility(View.VISIBLE);
						endtime3.setVisibility(View.VISIBLE);					
					}else{
						Toast.makeText(getActivity(), "Date2, Start time2 and End time2 can not be null",2000).show();
					}
					
				}
				
			}
		});*/
		
		date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			i=1;
			count_alert=0;
				 Calendar c = Calendar.getInstance();

				myYear = c.get(Calendar.YEAR);
				myMonth = c.get(Calendar.MONTH);
				myDay = c.get(Calendar.DAY_OF_MONTH);
			
				try {
					String date_from_edit_text=date.getText().toString();
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
		});
		starttime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				i=1;
				clicked=true;
				//time="";time_to="";
//				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistener, hours, minutes, true);
//				tiiPickerDialog.setTitle("START TIME");
//				tiiPickerDialog.show();
				
				 CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), R.style.AndroDev,mTimelistener, 
		         Calendar.getInstance().get(Calendar.HOUR),CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
		         timePickerDialog.setTitle("START TIME");
		        
		        timePickerDialog.show();
				
			}
		});
		endtime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				i=1;
				clicked=true;
				//time="";time_to="";
//				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistenerto, hours, minutes, true);
//				tiiPickerDialog.setTitle("END TIME");
//				tiiPickerDialog.show();
				
				CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), R.style.AppTheme,mTimelistenerto, 
	                    Calendar.getInstance().get(Calendar.HOUR), 
	                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
		        timePickerDialog.setTitle("END TIME");
		        timePickerDialog.show();
				
				
			}
		});
/*		
date1.setOnClickListener(new OnClickListener() {
			
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
	/*	starttime1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				i=2;
				start_dialog=1;
				clicked=true;
				//time="";time_to="";
//				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistener, hours, minutes, true);
//				tiiPickerDialog.setTitle("START TIME");
//				tiiPickerDialog.show();
				
				 CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), R.style.AppTheme,mTimelistener, 
		                    Calendar.getInstance().get(Calendar.HOUR), 
		                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
		        timePickerDialog.setTitle("START TIME");
		        timePickerDialog.show();
			}
		});*/
/*		endtime1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				end_dialog=1;
				i=2;
				clicked=true;
			//	time="";time_to="";
				CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), R.style.AppTheme,mTimelistenerto, 
	                    Calendar.getInstance().get(Calendar.HOUR), 
	                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
		        timePickerDialog.setTitle("END TIME");
		        timePickerDialog.show();
//				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistenerto, hours, minutes, true);
//				tiiPickerDialog.setTitle("END TIME");
//				tiiPickerDialog.show();
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
	/*	starttime2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				start_dialog=2;
				i=3;
				clicked=true;
				//time="";time_to="";
//				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistener, hours, minutes, true);
//				tiiPickerDialog.setTitle("START TIME");
//				tiiPickerDialog.show();
//				
//				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistener, hours, minutes, true);
//				tiiPickerDialog.setTitle("START TIME");
//				tiiPickerDialog.show();
				
				
				CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), R.style.AppTheme,mTimelistener, 
	                    Calendar.getInstance().get(Calendar.HOUR), 
	                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
		        timePickerDialog.setTitle("START TIME");
		        timePickerDialog.show();
				
				
			}
		});*/
	/*	endtime2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				end_dialog=2;
				i=3;
				clicked=true;
			//	time="";time_to="";
//				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistenerto, hours, minutes, true);
//				tiiPickerDialog.setTitle("END TIME");
//				tiiPickerDialog.show();
				
				CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), R.style.AppTheme,mTimelistenerto, 
	                    Calendar.getInstance().get(Calendar.HOUR), 
	                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
		        timePickerDialog.setTitle("START TIME");
		        timePickerDialog.show();
				
			}
		});*/
		
		
/*		
date3.setOnClickListener(new OnClickListener() {
			
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
/*		starttime3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				start_dialog=3;
				clicked=true;
			//	time="";time_to="";
				 CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), R.style.AppTheme,mTimelistener, 
		                    Calendar.getInstance().get(Calendar.HOUR), 
		                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
		        timePickerDialog.setTitle("START TIME");
		        timePickerDialog.show();
				
//				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistener, hours, minutes, true);
//				tiiPickerDialog.setTitle("START TIME");
//				tiiPickerDialog.show();
			}
		});*/
/*		endtime3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				end_dialog=3;
				clicked=true;
				//time="";time_to="";
				 CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity(), R.style.AppTheme,mTimelistenerto, 
		                    Calendar.getInstance().get(Calendar.HOUR), 
		                    CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL), true);
				
				 timePickerDialog.setTitle("END TIME");
				 
		        timePickerDialog.show();
				
				
				
//				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistenerto, hours, minutes, true);
//				tiiPickerDialog.setTitle("END TIME");
//				tiiPickerDialog.show();
				
//				TimePickerDialog tiiPickerDialog = new TimePickerDialog(getActivity(), mTimelistenerto, hours, minutes, true);
//				tiiPickerDialog.setTitle("END TIME");
//				tiiPickerDialog.show();
			}
		});*/
		
	
		send_request.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				publish="1";
				notes=notes_edittext.getText().toString();
				try {
					if(notes.equals("")||notes.equals(null)){
						notes="";
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			
				
				 date_selected=date.getText().toString();
			     starttime_selected=starttime.getText().toString();
				 endtime_selected=endtime.getText().toString();
				/* 
				 date_selected1=date1.getText().toString();
				 starttime_selected1=starttime1.getText().toString();
				 endtime_selected1=endtime1.getText().toString();
				 
				 date_selected2=date2.getText().toString();
				 starttime_selected2=starttime2.getText().toString();
				 endtime_selected2=endtime2.getText().toString();
				 
				 date_selected3=date3.getText().toString();
				 starttime_selected3=starttime3.getText().toString();
				 endtime_selected3=endtime3.getText().toString();*/
				 
				 
					
					/*if(date_selected1.equals("")||date_selected1.equals(null)){
						
					}*/
				
				/* if(date_selected.equals(date_selected1)){
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
				 }*/
				/* if(date_selected.equals(date_selected2)){
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
				 }*/
				/* if(date_selected.equals(date_selected3)){
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
				 */
				/* if(date_selected1!=null && date_selected1.length()>0 && date_selected2!=null && date_selected2.length()>0)
				 {
					 if(date_selected1.equals(date_selected2)){
						 if(starttime_selected1.equals(starttime_selected2)&& endtime_selected1.equals(endtime_selected2)){
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
				 }*/
				
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
				 Date date_1=null,date_2=null,date_3=null,date_4=null;
				 try {
					 date_1=sdf.parse(date_selected);
					
					 
					 
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
				 SimpleDateFormat destDf = new SimpleDateFormat("yyyy-MM-dd");
				
				               
			
				    try {
				    	 eventdate = destDf.format(date_1);
						 
					} catch (Exception e) {
						// TODO: handle exception
					}         // format the date into another format
				
				
				 
				 location_selected=location.getText().toString();
				 
				 if(child_id.equals(friend_id)){
						new AlertDialog.Builder(getActivity())
					    .setTitle("Invalid entry")
					    .setMessage("Your child and friend child can not be same")
					    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					        	child_id="";
					        	friend_id="";
					        	return;
					        }
					     })
					   
					    .setIcon(android.R.drawable.ic_dialog_alert)
					     .show();
return;
					}
				 
				 
				/* if(date_selected1.equals(null)){
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
					}*/
					/*if(starttime_selected1.equals(null)){
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
					}*/
					
					System.out.println(">>>>>>>>>>>>"+date_selected1+" "+starttime_selected1+" "+endtime_selected1);
				/* if(date_selected1.equals("")&&starttime_selected1.equals("")&&endtime_selected1.equals("")){
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
 
				 try {
					
				
				if(child_id.length()>0 && friend_id.length()>0 && date.getText().toString().length()>0 && starttime.getText().toString().length()>0 && endtime.getText().toString().length()>0 && location.getText().toString().length()>0){
					
					
					
					/*if(date_selected1.equals(null)){
						date_selected1="";
					}
					if(date_selected2.equals(null)){
						date_selected2="";
					}
					if(date_selected3.equals(null)){
						date_selected3="";
					}*/
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
					}
					if(date_selected2.equals(null)){
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
					}
					if(date_selected3.equals(null)){
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
					}*/
					cd=new ConnectionDetector(getActivity());
					isInternetPresent = cd.isConnectingToInternet();
					 if (isInternetPresent) {
						 new create_event().execute();
							
					 }
					 else{
						 Toast.makeText(getActivity(),"Please check internet connection",2000).show();
						
					 }
					
					
				}
				else{
					Toast.makeText(getActivity(), "No field will left blank", 2000).show();
					return;
				}
				 } catch (Exception e) {
						// TODO: handle exception
					}
			}
		});
		
/*save_request.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				publish="0";
				
                 notes=notes_edittext.getText().toString();
				try {
					if(notes.equals("")||notes.equals(null)){
						notes="";
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				 date_selected=date.getText().toString();
				 starttime_selected=starttime.getText().toString();
				 endtime_selected=endtime.getText().toString();
				 
				 date_selected1=date1.getText().toString();
				 starttime_selected1=starttime1.getText().toString();
				 endtime_selected1=endtime1.getText().toString();
				 
				 date_selected2=date2.getText().toString();
				 starttime_selected2=starttime2.getText().toString();
				 endtime_selected2=endtime2.getText().toString();
				 
				 date_selected3=date3.getText().toString();
				 starttime_selected3=starttime3.getText().toString();
				 endtime_selected3=endtime3.getText().toString();
				 
				location_selected=location.getText().toString();
				if(child_id.length()>0 && friend_id.length()>0 && date.getText().toString().length()>0 && starttime.getText().toString().length()>0 && endtime.getText().toString().length()>0 && location.getText().toString().length()>0){
					if(date_selected1.equals(null)){
						date_selected1="";
					}
					if(date_selected2.equals(null)){
						date_selected2="";
					}
					if(date_selected3.equals(null)){
						date_selected3="";
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
					}
					
					cd=new ConnectionDetector(getActivity());
					isInternetPresent = cd.isConnectingToInternet();
					 if (isInternetPresent) {
						 new create_event().execute();
							
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
		});*/

/*child.setOnItemSelectedListener(new OnItemSelectedListener() 
{
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
    {
        String selectedItem = parent.getItemAtPosition(position).toString();
        
         child_id=child_id_forspinner.get(position);
         image_url=child_name_list.get(position).child_profile;
         imageLoader.DisplayImage(image_url, child_image);
         child_image.requestLayout();
         int density = getResources().getDisplayMetrics().densityDpi;
		  switch (density) {
		  case DisplayMetrics.DENSITY_LOW:
			  child_image.getLayoutParams().height = 40;
			  child_image.getLayoutParams().width = 40;
			 
			  break;
		  case DisplayMetrics.DENSITY_MEDIUM:
			  child_image.getLayoutParams().height = 85;
			  child_image.getLayoutParams().width = 85;
			  break;
		  case DisplayMetrics.DENSITY_HIGH:
			  child_image.getLayoutParams().height = 85;
			  child_image.getLayoutParams().width = 85;
			  break;
		  case DisplayMetrics.DENSITY_XHIGH:
			  child_image.getLayoutParams().height = 120;
			  child_image.getLayoutParams().width = 120;
			  break;
		  case DisplayMetrics.DENSITY_XXHIGH:
			  child_image.getLayoutParams().height = 120;
			  child_image.getLayoutParams().width = 120;
			  break;
			  
		  }
		
		  
		  
       System.out.println("..................."+child_id);
        
        if(selectedItem.equals("Add new category"))
        {childname child_name=new childname();
        
        String child_id=child_name.getId();
             // do your stuff
        }

     
    }

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		 
	}
});
friend.setOnItemSelectedListener(new OnItemSelectedListener() 
{
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
    {
        String selectedItem = parent.getItemAtPosition(position).toString();
        childfriendname friend_name_child= new childfriendname();
        ArrayList<childfriendname> _items;
       // String parent_id=friend_name_child.getparent_id(child_friend_name.get(position));
        _items=child_friend_name;
        
        receiver_id=child_name_list_friend.get(position).g_id;
        System.out.println("parent_child_friend.............."+parent_id);
         friend_id=friend_id_forspinner.get(position);
        String image_url_1= friend_profilepic.get(position);
         System.out.println("..................."+child_id);
         imageLoader.DisplayImage(image_url_1, child_friend_image);
         child_friend_image.requestLayout();
         int density = getResources().getDisplayMetrics().densityDpi;
		  switch (density) {
		  case DisplayMetrics.DENSITY_LOW:
			  child_friend_image.getLayoutParams().height = 40;
			  child_friend_image.getLayoutParams().width = 40;
			 
			  break;
		  case DisplayMetrics.DENSITY_MEDIUM:
			  child_friend_image.getLayoutParams().height = 85;
			  child_friend_image.getLayoutParams().width = 85;
			  break;
		  case DisplayMetrics.DENSITY_HIGH:
			  child_friend_image.getLayoutParams().height = 85;
			  child_friend_image.getLayoutParams().width = 85;
			  break;
		  case DisplayMetrics.DENSITY_XHIGH:
			  child_friend_image.getLayoutParams().height = 120;
			  child_friend_image.getLayoutParams().width = 120;
			  break;
		  case DisplayMetrics.DENSITY_XXHIGH:
			  child_friend_image.getLayoutParams().height = 120;
			  child_friend_image.getLayoutParams().width = 120;
			  break;
			  
		  }
        
         
        if(selectedItem.equals("Add new category"))
        {
           
        }

     
    }

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
});*/
		final Home home=new Home();
        
      
LinearLayout layout = (LinearLayout)view.findViewById(R.id.linear_requestdate);// get your root  layout
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
	       
	       try{

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
			   	       date.setText(date_generated);
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
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		                        }
	       
	     
	    }
	};
	CustomTimePickerDialog.OnTimeSetListener mTimelistener = new CustomTimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		
			if(clicked==true){
				clicked=false;
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
					 starttime.setText(time);
				       }
				      /* if(start_dialog==1){
				    	   starttime1.setText(time);
					       }
				       if(start_dialog==2){
				    	   starttime2.setText(time);
					       }
				       if(start_dialog==3){
				    	   starttime3.setText(time);
					       } */
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
		                		 starttime.setText(time);
						       }
						      /* if(start_dialog==1){
						    	   starttime1.setText(time);
							       }
						       if(start_dialog==2){
						    	   starttime2.setText(time);
							       }
						       if(start_dialog==3){
						    	   starttime3.setText(time);
							       } */
		                }
		                
		              

		            } catch (ParseException e){
		                // Exception handling goes here
		            }
			 }
			
			    }
			 
		}
		
	};
	CustomTimePickerDialog.OnTimeSetListener mTimelistenerto = new CustomTimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			if(clicked==true){
				clicked=false;
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
   				 endtime.setText(time_to);
   			       }
   			     /*  if(end_dialog==1){
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
	        				 endtime.setText(time_to);
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

public  class getchild_webservice extends AsyncTask<String, Integer, String>{
ProgressDialog dialog=new ProgressDialog(getActivity());
	@Override
protected void onPreExecute() {
		friend_id_repeat_check.clear();
	
		try {
			dialog.setMessage("Loading.......please wait");
			dialog.setCancelable(false);
			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		url=Constants.GETCHILD;//"http://54.191.67.152/playdate/guardianfriend_child.php";//?g_id=46&friend_fbid=%2750%27,%2746%27
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("g_id",user_guardian_id));
        nameValuePairs.add(new BasicNameValuePair("friend_fbid",facebook_friends));
        System.out.println(facebook_friends);
        
        StringBuilder sbb = new StringBuilder();

		sbb.append(Constants.GETCHILD+"?");
		sbb.append(nameValuePairs.get(0)+"&");
		sbb.append(nameValuePairs.get(1));
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
			/*String sResponse = reader.readLine();
			
			System.out.println("response"+sResponse);
			JSONObject json = null;
			try {
				json = new JSONObject(sResponse);
				
				JSONArray jarray=json.getJSONArray("guardianchild");
				for(int i=0;i<jarray.length();i++){
					childname child_name = new childname();
					
					JSONObject jsonarrayobj=jarray.getJSONObject(i);
					String name=jsonarrayobj.optString("Childname");
					name=name.toUpperCase();
					child_name.name=name;
					child_name.id=jsonarrayobj.optString("child_id");
					child_name.date_of_birth=jsonarrayobj.optString("dob");
					child_name.freetime=jsonarrayobj.optString("c_set_fixed_freetime");
					child_name.allergies=jsonarrayobj.optString("allergies");
					child_name.hobbies=jsonarrayobj.optString("hobbies");
					child_name.school=jsonarrayobj.optString("school");
					child_name.youthclub=jsonarrayobj.optString("youth_club");
					child_name.child_profile=jsonarrayobj.optString("c_profile_image");
//					child_name.setId(jsonarrayobj.optString("child_id"));
					 child_name_list.add(child_name);
					 child_name_forspinner.add(jsonarrayobj.optString("Childname"));
					 child_id_forspinner.add(jsonarrayobj.optString("child_id"));
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
				
				JSONArray jarrayfriend;
				try {
					jarrayfriend = json.getJSONArray("data");
				
				for(int i=0;i<jarrayfriend.length();i++){
					
					childname child_name_friend = new childname();
					JSONObject jsonarrayobj1=jarrayfriend.getJSONObject(i);
					
					if(friend_id_repeat_check.contains(jsonarrayobj1.optString("child_id"))){
						
					}else{
						friend_id_repeat_check.add(jsonarrayobj1.optString("child_id"));
						String name=jsonarrayobj1.optString("name");
						String profile_pic_friend=jsonarrayobj1.optString("c_profile_image");
						String name1=name.toUpperCase();
						friend_name_child.setName(name1);
						friend_name_child.setId(jsonarrayobj1.optString("child_id"));
						friend_name_child.setparent_id(jsonarrayobj1.optString("g_id"));
						child_friend_name.add(friend_name_child);
						friend_profilepic.add(profile_pic_friend);
						friend_name_forspinner.add(jsonarrayobj1.optString("name"));
						friend_id_forspinner.add(jsonarrayobj1.optString("child_id"));
						
						
						childname child_name = new childname();
						
						JSONObject jsonarrayobj=jarray.getJSONObject(i);
						String name=jsonarrayobj1.optString("name");
						name=name.toUpperCase();
						child_name_friend.name=name;
						child_name_friend.g_id= jsonarrayobj1.optString("g_id");
						child_name_friend.id=jsonarrayobj1.optString("child_id");
						//child_name_friend.date_of_birth=jsonarrayobj1.optString("dob");
						child_name_friend.freetime=jsonarrayobj1.optString("c_set_fixed_freetime");
						child_name_friend.allergies=jsonarrayobj1.optString("allergies");
						child_name_friend.hobbies=jsonarrayobj1.optString("hobbies");
						child_name_friend.school=jsonarrayobj1.optString("school");
						child_name_friend.youthclub=jsonarrayobj1.optString("youth_club");
						child_name_friend.child_profile=jsonarrayobj1.optString("c_profile_image");
						friend_profilepic.add(jsonarrayobj1.optString("c_profile_image"));
//						child_name.setId(jsonarrayobj.optString("child_id"));
						 child_name_list_friend.add(child_name_friend);
						 friend_name_forspinner.add(jsonarrayobj1.optString("name"));
						 friend_id_forspinner.add(jsonarrayobj1.optString("child_id"));
					}
					
					
				}
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				// child_name.setName(json.optString("name"));
				 
			
			
			
				
		} catch (IOException e) {
			e.printStackTrace();
		}
        */
String sResponse = reader.readLine();
			
			System.out.println("response"+sResponse);
			JSONObject json = null;
			try {
				json = new JSONObject(sResponse);
				
				JSONArray jarray=json.getJSONArray("guardianchild");
				for(int i=0;i<jarray.length();i++){
					childname child_name = new childname();
					
					JSONObject jsonarrayobj=jarray.getJSONObject(i);
					String name=jsonarrayobj.optString("Childname");
					name=name.toUpperCase();
					child_name.name=name;
					child_name.id=jsonarrayobj.optString("child_id");
					child_name.date_of_birth=jsonarrayobj.optString("dob");
					child_name.freetime=jsonarrayobj.optString("c_set_fixed_freetime");
					child_name.allergies=jsonarrayobj.optString("allergies");
					child_name.hobbies=jsonarrayobj.optString("hobbies");
					child_name.school=jsonarrayobj.optString("school");
					child_name.youthclub=jsonarrayobj.optString("youth_club");
					child_name.child_profile=jsonarrayobj.optString("c_profile_image");
//					child_name.setId(jsonarrayobj.optString("child_id"));
					 child_name_list.add(child_name);
					 //child_name_forspinner.add(jsonarrayobj.optString("Childname"));
					// child_id_forspinner.add(jsonarrayobj.optString("child_id"));
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
				
				JSONArray jarrayfriend;
				try {
					jarrayfriend = json.getJSONArray("data");
				
				for(int i=0;i<jarrayfriend.length();i++){
					
					childname child_name_friend = new childname();
					JSONObject jsonarrayobj1=jarrayfriend.getJSONObject(i);
					
					if(friend_id_repeat_check.contains(jsonarrayobj1.optString("child_id"))){
						
					}else{
						friend_id_repeat_check.add(jsonarrayobj1.optString("child_id"));
						/*String name=jsonarrayobj1.optString("name");
						String profile_pic_friend=jsonarrayobj1.optString("c_profile_image");
						String name1=name.toUpperCase();
						friend_name_child.setName(name1);
						friend_name_child.setId(jsonarrayobj1.optString("child_id"));
						friend_name_child.setparent_id(jsonarrayobj1.optString("g_id"));
						child_friend_name.add(friend_name_child);
						friend_profilepic.add(profile_pic_friend);
						friend_name_forspinner.add(jsonarrayobj1.optString("name"));
						friend_id_forspinner.add(jsonarrayobj1.optString("child_id"));
						
						
						childname child_name = new childname();
						
						JSONObject jsonarrayobj=jarray.getJSONObject(i);*/
						String name=jsonarrayobj1.optString("name");
						name=name.toUpperCase();
						child_name_friend.name=name;
						child_name_friend.g_id= jsonarrayobj1.optString("g_id");
						child_name_friend.id=jsonarrayobj1.optString("child_id");
						child_name_friend.date_of_birth=jsonarrayobj1.optString("dob");
						//child_name_friend.freetime=jsonarrayobj1.optString("c_set_fixed_freetime");
						child_name_friend.allergies=jsonarrayobj1.optString("allergies");
						//child_name_friend.hobbies=jsonarrayobj1.optString("hobbies");
						//child_name_friend.school=jsonarrayobj1.optString("school");
						//child_name_friend.youthclub=jsonarrayobj1.optString("youth_club");
						child_name_friend.child_profile=jsonarrayobj1.optString("c_profile_image");
						friend_profilepic.add(jsonarrayobj1.optString("c_profile_image"));
//						child_name.setId(jsonarrayobj.optString("child_id"));
						 child_name_list_friend.add(child_name_friend);
						 //friend_name_forspinner.add(jsonarrayobj1.optString("name"));
						// friend_id_forspinner.add(jsonarrayobj1.optString("child_id"));
					}
					
					
				}
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				// child_name.setName(json.optString("name"));
				 
			
			
			
				
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        
		return null;
	}
protected void onPostExecute(String resultt) {
	try {
		dialog.dismiss();
		
		if(child_name_list.size()==0){
			add_child_view.setVisibility(View.VISIBLE);
			add_child.setVisibility(View.INVISIBLE);
			child_image.setVisibility(View.INVISIBLE);
			add_child.setVisibility(View.INVISIBLE);
			txt_child_name.setVisibility(View.INVISIBLE);
			//child_name_list_friend.clear();
		}
	} catch (Exception e) {
		// TODO: handle exception
	}
//	friend.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.textview,android.R.layout.simple_list_item_1, friend_name_forspinner));	
	//	friend.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.textview, friend_name_forspinner));
	//	 child.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.textview, child_name_forspinner));
		 
		 }
}

public  class create_event extends AsyncTask<String, Integer, String>{
ProgressDialog dialog=new ProgressDialog(getActivity());
boolean check_response=false;
	@Override
protected void onPreExecute() {
		
		dialog.setMessage("Loading.......please wait");
		dialog.setCancelable(false);
		dialog.show();
		url=Constants.CREATE_EVENT;//"http://54.191.67.152/playdate/event.php";//?child_id=68&friend_childid=69&date=31-5-2014&starttime=11:04&endtime=3:00&location=delhi&publish=1
	}//http://112.196.34.179/playdate/event.php?child_id=68&friend_childid=69&date=31-5-2014&starttime=11:04&endtime=3:00&date1=31-1-2014&starttime1=12:34&endtime1=4:00&date2=28-3-2014&starttime2=1:34&endtime2=7:00&date3=3-4-2014&starttime3=2:34&endtime3=6:00&date4=6-1-2014&starttime4=2:04&endtime4=2:00&location=delhi&publish=1
	
	@Override
	protected String doInBackground(String... arg0) {
		
		HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);
        
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("child_id",child_id));
        nameValuePairs.add(new BasicNameValuePair("friend_childid",friend_id));
        nameValuePairs.add(new BasicNameValuePair("date",eventdate));
        nameValuePairs.add(new BasicNameValuePair("starttime",starttime_selected));
        nameValuePairs.add(new BasicNameValuePair("endtime",endtime_selected));
        
       // nameValuePairs.add(new BasicNameValuePair("date1",eventdate1));
       // nameValuePairs.add(new BasicNameValuePair("starttime1",starttime_selected1));
       // nameValuePairs.add(new BasicNameValuePair("endtime1",endtime_selected1));
        
     //   nameValuePairs.add(new BasicNameValuePair("date2",eventdate2));
        nameValuePairs.add(new BasicNameValuePair("g_id",user_guardian_id));
     //   nameValuePairs.add(new BasicNameValuePair("starttime2",starttime_selected2));
     //   nameValuePairs.add(new BasicNameValuePair("endtime2",endtime_selected2));
        
     //   nameValuePairs.add(new BasicNameValuePair("date3",eventdate3));
     //   nameValuePairs.add(new BasicNameValuePair("starttime3",starttime_selected3));
      //  nameValuePairs.add(new BasicNameValuePair("endtime3",endtime_selected3));
        
        nameValuePairs.add(new BasicNameValuePair("location",location_selected));
        
        nameValuePairs.add(new BasicNameValuePair("notes",notes));
        
        nameValuePairs.add(new BasicNameValuePair("receiver_id",receiver_id));
        nameValuePairs.add(new BasicNameValuePair("publish",publish));
        nameValuePairs.add(new BasicNameValuePair("receiver_status","requested"));
        StringBuilder sbb = new StringBuilder();

		sbb.append(Constants.CREATE_EVENT+"?");
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
        //receiver_status, requested"
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
			check_response=true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			check_response=true;
		} catch (IOException e) {
			e.printStackTrace();
			check_response=true;
		}

        try {
			String sResponse = reader.readLine();
			
			System.out.println("response"+sResponse);
			JSONObject json;
			try {
				json = new JSONObject(sResponse);
				
				
				success=json.getString("success");
				/*JSONArray jarray=json.getJSONArray("guardianchild");
				for(int i=0;i<jarray.length();i++){
					childname child_name = new childname();
					
					JSONObject jsonarrayobj=jarray.getJSONObject(i);
					child_name.setName(jsonarrayobj.optString("Childname"));
					child_name.setId(jsonarrayobj.optString("child_id"));
					 child_name_list.add(child_name);
					 child_name_forspinner.add(jsonarrayobj.optString("Childname"));
					
				}
				
				
				JSONArray jarrayfriend=json.getJSONArray("facebookfriendChild");
				for(int i=0;i<jarrayfriend.length();i++){
					
					 childfriendname friend_name_child= new childfriendname();
					JSONObject jsonarrayobj=jarray.getJSONObject(i);
					friend_name_child.setName(jsonarrayobj.optString("name"));
					friend_name_child.setId(jsonarrayobj.optString("child_id"));
					child_friend_name.add(friend_name_child);
					friend_name_forspinner.add(jsonarrayobj.optString("name"));
					
				}*/
				
				 
				// child_name.setName(json.optString("name"));
				 
			} catch (JSONException e) {
				e.printStackTrace();
				check_response=true;
			}
			
			
				
		} catch (IOException e) {
			e.printStackTrace();
			check_response=true;
		}
        
        
		return null;
	}
protected void onPostExecute(String resultt) {
		dialog.dismiss();
		
		if(check_response){
			
		}else{

if(success.equals("1") && publish.equals("1")){
	time="";
	time_to="";
	
	location.setText("");
	date.setText("");
	starttime.setText("");
	endtime.setText("");
	
	/*date1.setText("");
	starttime1.setText("");
	endtime1.setText("");
	date2.setText("");
	starttime2.setText("");
	endtime2.setText("");
	date3.setText("");
	starttime3.setText("");
	endtime3.setText("");*/
	notes_edittext.setText("");
	

	/*date1.setVisibility(View.GONE);
	starttime1.setVisibility(View.GONE);
	endtime1.setVisibility(View.GONE);
	starttime2.setVisibility(View.GONE);
	endtime2.setVisibility(View.GONE);
	date2.setVisibility(View.GONE);
	date3.setVisibility(View.GONE);
	starttime3.setVisibility(View.GONE);
	endtime3.setVisibility(View.GONE);*/

	MixpanelAPI	 mixpanel =
	    MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
	 
	 JSONObject props = new JSONObject();
	 try {
		props.put("Gender", user_guardian_id);
	} catch (JSONException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	 mixpanel.identify(user_guardian_id);
	 mixpanel.track("Arrange Playdate", props);
	
	Toast.makeText(getActivity(), "successfully created",2000).show();
	
	android.support.v4.app.Fragment fragment = new Home_fragment();
	//fragment.setArguments(bundle);
	android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
	android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
	fragmentTransaction.replace(R.id.content_frame, fragment);

	fragmentTransaction.commit();

}
else if(success.equals("1") && publish.equals("0")){
	
	MixpanelAPI	 mixpanel =
	    MixpanelAPI.getInstance(getActivity(),Constants.MIXPANEL_TOKEN);
	 
	 JSONObject props = new JSONObject();
	 try {
		props.put("Gender", user_guardian_id);
	} catch (JSONException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	 mixpanel.identify(user_guardian_id);
	 mixpanel.track("Arrange Playdate", props);
	 
	 
	location.setText("");
	date.setText("");
	starttime.setText("");
	endtime.setText("");
	Toast.makeText(getActivity(), "successfully saved",2000).show();
	android.support.v4.app.Fragment fragment = new Home_fragment();
	//fragment.setArguments(bundle);
	android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
	android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
	fragmentTransaction.replace(R.id.content_frame, fragment);

	fragmentTransaction.commit();
}
else{
	Toast.makeText(getActivity(), "Please try again later",2000).show();
}
		}	 
		 }
}


public static class CustomTimePickerDialog extends TimePickerDialog{
	 
    public static final int TIME_PICKER_INTERVAL=15;
    private boolean mIgnoreEvent=false;

    public CustomTimePickerDialog(Context context, int apptheme,OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
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
public class LazyAdapter extends BaseAdapter {
	String _imgurl = "";
	private Activity activity;
	private ArrayList<childname> _items;
	private LayoutInflater inflater = null;

	

	public ImageLoader imageLoader;

	

	public LazyAdapter(Activity activity, ArrayList<childname> getcat_for_sets) {

		
		this.activity = activity;
		this._items = getcat_for_sets;
		try {
			inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		public TextView event_title, event_date;
		public ImageView _image = null;

	}

	public View getView(final int position, View convertView,
			ViewGroup parent) {
		View vi = convertView;
		ViewHolder _holder;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.parent_sets, null);
			_holder = new ViewHolder();

			_holder.event_title = (TextView) convertView
					.findViewById(R.id.textView11);
			_holder._image = (ImageView) convertView
					.findViewById(R.id.imageView11);
		
			convertView.setTag(_holder);
		} else {
			_holder = (ViewHolder) convertView.getTag();
		}
		String name_of_child=_items.get(position).name;
		name_of_child=name_of_child.toUpperCase();
		_holder.event_title.setText(name_of_child);
		
		_imgurl = _items.get(position).child_profile;
		
		Log.d("", "_imgurl" + _imgurl);
		Log.d("", "_imgurl" + _imgurl);
		Log.d("", "_imgurl" + _imgurl);
		Log.d("", "_imgurl" + _imgurl);
		Log.d("", "_imgurl" + _imgurl);
		_holder._image.setTag(_imgurl);
		imageLoader.DisplayImage(_imgurl,_holder._image);
		
		_holder._image.requestLayout();
		 int density = getResources().getDisplayMetrics().densityDpi;
		  switch (density) {
		  case DisplayMetrics.DENSITY_LOW:
			  _holder._image.getLayoutParams().height = 40;
			  _holder._image.getLayoutParams().width = 40;
			 
			  break;
		  case DisplayMetrics.DENSITY_MEDIUM:
			  _holder._image.getLayoutParams().height = 85;
			  _holder._image.getLayoutParams().width = 85;
			  break;
		  case DisplayMetrics.DENSITY_HIGH:
			  _holder._image.getLayoutParams().height = 85;
			  _holder._image.getLayoutParams().width = 85;
			  break;
		  case DisplayMetrics.DENSITY_XHIGH:
			  _holder._image.getLayoutParams().height = 120;
			  _holder._image.getLayoutParams().width = 120;
			  break;
		  case DisplayMetrics.DENSITY_XXHIGH:
			  _holder._image.getLayoutParams().height = 120;
			  _holder._image.getLayoutParams().width = 120;
			  break;
		  }
		
		return convertView;
	}
}
	}

