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
import java.sql.Time;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iapp.playdate.R;
import com.iapp.playdate.Parent_profile.parent_pic_update;
import com.iapptechnologies.time.util.Constants;

public class Child_profile extends android.support.v4.app.Fragment {
	int RESULT_LOAD_IMAGE = 1, dayselect = 0, count_alert = 0;
	EditText dob,  allergies,  name, editallergies;//youthclub
	ImageView child_image;
	String data, url, child_name, child_ID, child_profile_pic, child_hobbies,
			child_allergies, child_conditions, child_school, child_youthclub,
			guardian_id, child_dob, free_time_child, urlreturned,
			child_parent_id;
	 String picker_days="";
	 String picker_hr_from="", picker_min_from="", picker_hr_to="", picker_min_to="",free_time_picker="";
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	com.iapptechnologies.time.util.ImageLoader imageLoader;
	ListView list_parent;
	Bitmap imageData;
	boolean clicked = false;
	ArrayList<String> parent_id;
	ArrayList<Getcatagory_for_sets> parentItems;
	LazyAdapter adapter;
	Button addmore_freetime, update_info, back_button, unlinkButton;
	String name_update, dob_update, freetime_update, allergies_update,
			hobbies_update, conditions_update, school_update, youthclub_update,
			phone_number;
	boolean timechk1 = false, timechk2 = false, timechk3 = false,
			timechk4 = false, timechk5 = false, timechk6 = false,
			timechk7 = false;
	String daymon, daytue, daywed, daythu, dayfri, daysat, daysun,
			user_guardian_id, date_of_birth_child, parent_name,
			parent_profilepic;
	private int myYear, myMonth, myDay;
	String date_comparision, days = "", daysadd = "", time, facebook_friends;
	int hours, minutes;
	String allergies_selected = "", hobbies_selected = "", parent_freetime,
			date_of_birth_of_parent, parent_type, parent_location, fb_id;
	// for checking checkboxes clicking allergies
	int i_none = 1, i = 1, i2 = 1, i3 = 1, i4 = 1, i5 = 1, i6 = 1, i7 = 1,
			i8 = 1, i9 = 1, i10 = 1, i11 = 1, i12 = 1, i13 = 1, i14 = 1,
			i15 = 1;;
	// for allergies
	String none = "", nutMilk = "", egg = "", wheat = "", soyfish = "",
			corn = "", gelate = "", seed = "", spices = "", grass = "",
			banana = "", dairy = "", hay = "", insect = "", stings = "",
			celiacGluten = "", other = "";
	
	Boolean isInternetPresent = false;
	ConnectionDetector cd;

	public Child_profile() {

	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.child_profile_show, container, false);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		Calendar c = Calendar.getInstance();
		settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
		editor = settings.edit();
		
		System.out.println("Current time => " + c.getTime());
        Home.menu.setVisibility(View.GONE);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		date_comparision = df.format(c.getTime());
		imageLoader = new com.iapptechnologies.time.util.ImageLoader(getActivity());
		dob = (EditText) view.findViewById(R.id.textView_dob_child_profile);
		/*setfixedfree_time = (EditText) view
				.findViewById(R.id.textView_freetime_child_profile);*/
		// conditions = (EditText)
		// view.findViewById(R.id.textView_condition_childprofile);
		allergies = (EditText) view
				.findViewById(R.id.textView_child_profile_allergies);
		/*hobbies = (EditText) view
				.findViewById(R.id.textView_child_profile_hobbies);
		school = (EditText) view
				.findViewById(R.id.textView_child_profile_school);*/
		/*youthclub = (EditText) view
				.findViewById(R.id.textView_child_profile_youthclub);*/
		name = (EditText) view.findViewById(R.id.child_profile_name);
		child_image = (ImageView) view
				.findViewById(R.id.imageView_child_profile);
		list_parent = (ListView) view
				.findViewById(R.id.listView_child_profile_child);
		/*addmore_freetime = (Button) view
				.findViewById(R.id.button1_add_moredays_child_profile);*/
		update_info = (Button) view.findViewById(R.id.button_save_child_update);
		unlinkButton = (Button) view.findViewById(R.id.unlink_child);
		// back_button=(Button) view.findViewById(R.id.button_back);
		try {

			//parent_name=settings.getString("NAME","");
			parent_profilepic=settings.getString("URLPROFILE","");
			user_guardian_id=settings.getString("USERID","");
			parent_name=settings.getString("FIRSTNAME","");
			fb_id=settings.getString("FACEBOOKID","");
			date_of_birth_of_parent=settings.getString("DOB","");
			parent_type=settings.getString("GURTYPE","");
			parent_location=settings.getString("LOCATION","");
			facebook_friends=settings.getString("FBFRIEND","");
			phone_number=settings.getString("PHONE","");
			
			
			//parent_freetime = getArguments().getString("parent_freetime");
			//date_of_birth_of_parent = getArguments().getString("parent_dob");
			//parent_type = getArguments().getString("parent_type");
			//parent_location = getArguments().getString("parent_location");
			//fb_id = getArguments().getString("parent_fbid");
			//parent_name = getArguments().getString("parent_name");
			//parent_profilepic = getArguments().getString("parent_profilepic");
			child_dob = getArguments().getString("dob");
			child_name = getArguments().getString("child_name");
			child_ID = getArguments().getString("Child_id");
			child_profile_pic = getArguments().getString("Child_profilepic");
			//child_hobbies = getArguments().getString("hobbies");
			child_allergies = getArguments().getString("allergies");
			// child_conditions = getArguments().getString("conditions");
			//child_school = getArguments().getString("school");
			//child_youthclub = getArguments().getString("youthclub");
			guardian_id = getArguments().getString("Child_guardianId");
			//free_time_child = getArguments().getString("free_time");
			//facebook_friends = getArguments().getString("facebook_friends");
			//phone_number = getArguments().getString("phone");
		} catch (Exception e) {
			// TODO: handle exception
		}
		cd = new ConnectionDetector(getActivity());
		if (GlobalVariable.appers_unlink_button) {
			GlobalVariable.appers_unlink_button = false;
			unlinkButton.setVisibility(View.VISIBLE);
		} else {

		}

		new parent_profile_show().execute();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date_of_birth = null;
		try {
			date_of_birth = sdf.parse(child_dob);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		/*
		 * back_button.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub System.out.println("back pressed"); FragmentManager
		 * fm=getActivity().getFragmentManager(); fm.popBackStack();
		 * android.support.v4.app.FragmentManager
		 * fm=getActivity().getSupportFragmentManager(); fm.popBackStack(); }
		 * });
		 */
		DateFormat destDf = new SimpleDateFormat("dd/MM/yy");
		String dob_formated = destDf.format(date_of_birth);

		dob.setText(dob_formated);
		/*setfixedfree_time.setText(free_time_child);
		Log.e("FreeeTime===", "" + free_time_child);
*/
		// conditions.setText(child_conditions);
		allergies.setText(child_allergies);
	//	hobbies.setText(child_hobbies);
	//	school.setText(child_school);
		//youthclub.setText(child_youthclub);
		name.setText(child_name.toUpperCase());

		dob.setFocusable(false);
		dob.setClickable(true);
	//	setfixedfree_time.setFocusable(false);
	//	setfixedfree_time.setClickable(true);
		allergies.setFocusable(false);
		allergies.setClickable(true);
	//	hobbies.setFocusable(false);
	//	hobbies.setClickable(true);

		imageData = imageLoader.DisplayImage(child_profile_pic, child_image);

		child_image.requestLayout();
		list_parent.requestLayout();
		
        Display display = getActivity().getWindowManager().getDefaultDisplay();
		
		int height = display.getHeight();
		int width = display.getWidth();
		
		System.out.println(height  +  "   "+width);
		
		float mReqPercentages = 35;
		float mCalculatedWidth = (mReqPercentages / 100) * width;

		float mReqPercentages1 = 40;
		float mCalculatedWidth1 = (mReqPercentages1 / 100) * width;

		
		child_image.getLayoutParams().height = (int) (mCalculatedWidth);
		child_image.getLayoutParams().width = (int) mCalculatedWidth;
		list_parent.getLayoutParams().height = (int)mCalculatedWidth;
		list_parent.getLayoutParams().width = (int)mCalculatedWidth1;
		
		int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_LOW:
			//child_image.getLayoutParams().height = 120;
		//	child_image.getLayoutParams().width = 120;
		//	list_parent.getLayoutParams().height = 100;
		//	list_parent.getLayoutParams().width = 150;
			unlinkButton.getLayoutParams().width = 150;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
		//	child_image.getLayoutParams().height = 200;
		//	child_image.getLayoutParams().width = 200;
		//	list_parent.getLayoutParams().height = 180;
		//	list_parent.getLayoutParams().width = 250;
			unlinkButton.getLayoutParams().width = 250;
			break;
		case DisplayMetrics.DENSITY_HIGH:
		//	child_image.getLayoutParams().height = 200;
		//	child_image.getLayoutParams().width = 200;
		//	list_parent.getLayoutParams().height = 180;
		//	list_parent.getLayoutParams().width = 300;
			unlinkButton.getLayoutParams().width = 300;

			break;
		case DisplayMetrics.DENSITY_XHIGH:
		//	child_image.getLayoutParams().height = 280;
		//	child_image.getLayoutParams().width = 280;
	//		list_parent.getLayoutParams().height = 250;
	//		list_parent.getLayoutParams().width = 350;
			unlinkButton.getLayoutParams().width = 350;

			break;
		case DisplayMetrics.DENSITY_XXHIGH:
		//	child_image.getLayoutParams().height = 300;
		//	child_image.getLayoutParams().width = 300;
	//		list_parent.getLayoutParams().height = 250;
	//		list_parent.getLayoutParams().width = 350;
			unlinkButton.getLayoutParams().width = 350;

			break;
		}
		isInternetPresent = cd.isConnectingToInternet();
		unlinkButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new AlertDialog.Builder(getActivity())
				.setTitle("Unlink child")
				.setMessage("Would you like to unlink child? ")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (isInternetPresent) {
									new unlink_child().execute();
								} else {
									Toast.makeText(getActivity(), "Please check internet",
											Toast.LENGTH_LONG).show();
								}
								
							}
						})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								return;
							}
						}).setIcon(android.R.drawable.ic_dialog_alert)
				.show();


				

			}
		});

		parentItems = new ArrayList<Getcatagory_for_sets>();

		list_parent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				parent_name = parentItems.get(arg2).parent_name;
				parent_profilepic = parentItems.get(arg2).parent_profilepic;
				//parent_freetime = parentItems.get(arg2).parent_freetime;
				date_of_birth_of_parent = parentItems.get(arg2).parent_date_ofbirth;
				parent_type = parentItems.get(arg2).parent_type;
				parent_location = parentItems.get(arg2).parent_location;
				guardian_id = parentItems.get(arg2).parent_id;
				phone_number = parentItems.get(arg2).parent_phone;
				Bundle bundle = new Bundle();
				bundle.putString("name", parent_name);
				bundle.putString("url", parent_profilepic);
				//bundle.putString("freetime", parent_freetime);
				bundle.putString("dob", date_of_birth_of_parent);
				bundle.putString("guardiantype", parent_type);
				bundle.putString("location", parent_location);
				bundle.putString("firstname", parent_name);
				bundle.putString("facebook_id", fb_id);
				bundle.putString("user_guardian_id", guardian_id);
				bundle.putString("facebook_friends", facebook_friends);
				bundle.putString("phone", phone_number);
				GlobalVariable.coming_from_child="1";
			//	if(guardian_id.equals(GlobalVariable.guardian_Id))
			//	{
					android.support.v4.app.Fragment fragment = new Parent_profile();
					fragment.setArguments(bundle);
					android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);
					fragmentTransaction.addToBackStack("first4");
					fragmentTransaction.commit();
				//}
			
				/*
				 * android.support.v4.app.FragmentManager fragmentManager =
				 * getFragmentManager(); fragmentManager.beginTransaction()
				 * .replace(R.id.content_frame, fragment).commit();
				 */

			}
		});

		child_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

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
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
										getActivity().startActivityForResult(
												intent, RESULT_LOAD_IMAGE);
									}
								})
						.setNegativeButton("Camera",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										GlobalVariable.parent_picute_update = 2;
										Intent it = new Intent(getActivity(),
												Home.class);

										it.putExtra("child_dob", child_dob);

										it.putExtra("child_name", child_name);
										it.putExtra("child_ID", child_ID);
										it.putExtra("child_profile_pic",
												child_profile_pic);
										/*it.putExtra("child_hobbies",
												child_hobbies);*/
										it.putExtra("child_allergies",
												child_allergies);
									//	it.putExtra("child_conditions", "");
										/*it.putExtra("child_school",
												child_school);*/
										/*it.putExtra("child_youthclub",
												child_youthclub);*/
										/*it.putExtra("free_time_child",
												free_time_child);*/
									//	it.putExtra("phone", phone_number);
									//	it.putExtra("url", parent_profilepic);
									//	it.putExtra("name", parent_name);
								///		it.putExtra("firstname", parent_name);
									//	it.putExtra("iD", fb_id);
									//	it.putExtra("location", parent_location);
									//	it.putExtra("dob",
								//				date_of_birth_of_parent);
								//		it.putExtra("guardiantype", parent_type);
										//it.putExtra("freetime", parent_freetime);
								//		it.putExtra("user_guardian_id",
								//				guardian_id);
								//		it.putExtra("facebook_friends",
								//				facebook_friends);

										getActivity().startActivity(it);
									}
								}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();

			}
		});

		dob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				count_alert = 0;
				Calendar c = Calendar.getInstance();

				myYear = c.get(Calendar.YEAR);
				myMonth = c.get(Calendar.MONTH);
				myDay = c.get(Calendar.DAY_OF_MONTH);

				
					
					try {
						String date_from_edit_text = dob.getText().toString();
						
						SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
						// SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yy");
						 Date date_1=null;//,date_2=null,date_3=null,date_4=null;
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
					// TODO: handle exception
				}

				DatePickerDialog d = new DatePickerDialog(getActivity(),
						mDateSetListener, myYear, myMonth, myDay);

				d.show();
			}
		});
		/*setfixedfree_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				hours = new Time(System.currentTimeMillis()).getHours();
				minutes = new Time(System.currentTimeMillis()).getMinutes();

				LinearLayout viewGroup = (LinearLayout) getActivity()
						.findViewById(R.id.weekdays_selection);
				System.out.println("view Started11");

				System.out.println("view Started22");
				View layout = inflater.inflate(R.layout.weekdays, viewGroup);
				System.out.println("view Started33");

				final PopupWindow popup = new PopupWindow(getActivity());
				popup.setContentView(layout);

				popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
				popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
				popup.setFocusable(true);

				int OFFSET_X = 0;
				int OFFSET_Y = 0;

				popup.setBackgroundDrawable(new BitmapDrawable());

				popup.showAtLocation(getView(), Gravity.NO_GRAVITY, OFFSET_X,
						OFFSET_Y);

				RelativeLayout monday = (RelativeLayout) layout
						.findViewById(R.id.Monday);
				monday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dayselect = 1;

						clicked = true;

						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenermonto, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenermonto,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenermon, hours,
						// minutes, true);
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();

						CustomTimePickerDialog timePickerDialogg = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenermon,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialogg.setTitle("FROM");
						timePickerDialogg.show();
						popup.dismiss();

					}
				});
				RelativeLayout tuesday = (RelativeLayout) layout
						.findViewById(R.id.Tuesday);
				tuesday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dayselect = 2;

						clicked = true;

						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenertueto, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenertueto,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenertue, hours,
						// minutes, true);
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();

						CustomTimePickerDialog tiPickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenertue,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						tiPickerDialog.setTitle("FROM");
						tiPickerDialog.show();
						popup.dismiss();

					}
				});
				RelativeLayout Wednesday = (RelativeLayout) layout
						.findViewById(R.id.Wednesday);
				Wednesday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dayselect = 3;

						clicked = true;

						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenerwedto, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenerwedto,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenerwed, hours,
						// minutes, true);
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();

						CustomTimePickerDialog tiPickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenerwed,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						tiPickerDialog.setTitle("FROM");
						tiPickerDialog.show();

						popup.dismiss();

					}
				});
				RelativeLayout Thursday = (RelativeLayout) layout
						.findViewById(R.id.Thursday);
				Thursday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dayselect = 4;
						clicked = true;

						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenerthuto, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenerthuto,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenerthu, hours,
						// minutes, true);
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();

						CustomTimePickerDialog tiPickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenerthu,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						tiPickerDialog.setTitle("FROM");
						tiPickerDialog.show();

						popup.dismiss();
					}
				});
				RelativeLayout Friday = (RelativeLayout) layout
						.findViewById(R.id.Friday);
				Friday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dayselect = 5;

						clicked = true;

						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenerfrito, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenerfrito,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
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
				bundle.putString("phone", "");
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						//
						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenerfri, hours,
						// minutes, true);
						//
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();

						CustomTimePickerDialog tiPickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenerfri,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						tiPickerDialog.setTitle("FROM");
						tiPickerDialog.show();

						popup.dismiss();

					}
				});
				RelativeLayout Saturday = (RelativeLayout) layout
						.findViewById(R.id.Saturday);
				Saturday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dayselect = 6;

						clicked = true;

						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenersatto, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenersatto,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenersat, hours,
						// minutes, true);
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();

						CustomTimePickerDialog tiPickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenersat,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						tiPickerDialog.setTitle("FROM");
						tiPickerDialog.show();

						popup.dismiss();
					}
				});
				RelativeLayout Sunday = (RelativeLayout) layout
						.findViewById(R.id.Sunday);
				Sunday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dayselect = 7;

						clicked = true;

						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenersunto, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenersunto,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenersun, hours,
						// minutes, true);
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();

						CustomTimePickerDialog tiPickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenersun,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						tiPickerDialog.setTitle("FROM");
						tiPickerDialog.show();

						popup.dismiss();
					}
				});

				free_time_picker="";picker_days="";picker_hr_from="";picker_hr_to="";picker_min_from="";picker_min_to="";
				LinearLayout viewGroup = (LinearLayout) getActivity().findViewById(R.id.numberpicker);
				View layout = inflater.inflate(R.layout.number_picker, viewGroup);
				final PopupWindow popup = new PopupWindow(getActivity());
				popup.setContentView(layout);
				popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
				popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
				popup.setFocusable(true);
				int OFFSET_X = 0;
				int OFFSET_Y = 0;
				popup.setBackgroundDrawable(new BitmapDrawable());
				popup.showAtLocation(getView(), Gravity.NO_GRAVITY, OFFSET_X,OFFSET_Y);
				
				
				
				NumberPicker npdays_=
						   (NumberPicker) layout.findViewById(R.id.numberPicker_days);
				NumberPicker np_hr_from=
						   (NumberPicker) layout.findViewById(R.id.numberPicker_hr_from);
				NumberPicker np_min_from=
						   (NumberPicker) layout.findViewById(R.id.numberPicker_min_from);
				NumberPicker np_hr_to=
						   (NumberPicker) layout.findViewById(R.id.numberPicker_hr_to);
				NumberPicker np_min_to=
						   (NumberPicker) layout.findViewById(R.id.numberPicker_min_to);
				
				TextView btn_done_picker=(TextView) layout.findViewById(R.id.done_numberpicker);
				TextView btn_cancel_picker=(TextView) layout.findViewById(R.id.cancel_numberpicker);
				final TextView time_display=(TextView) layout.findViewById(R.id.time_numberpicker);
				btn_cancel_picker.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						popup.dismiss();
					}
				});
				
				btn_done_picker.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(picker_hr_from.equals("") || picker_hr_to.equals("") ){
							Toast.makeText(getActivity(),"Please select time", 2000).show();
						}else{
							if(picker_days.equals("")){
								picker_days="Monday";
							}
							if(picker_min_from.equals("")){
								picker_min_from="00";
							}
							if(picker_min_to.equals("")){
								picker_min_to="00";
							}
							 int hr_picker_from=Integer.parseInt(picker_hr_from);
						     	int min_picker_from=Integer.parseInt(picker_min_from);
						     	int hr_picker_to=Integer.parseInt(picker_hr_to);
						     	int min_picker_to=Integer.parseInt(picker_min_to);
						     	
						     	if(hr_picker_from<hr_picker_to){
						     		 free_time_picker=picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to;
						     		setfixedfree_time.setText(free_time_picker);
						     		popup.dismiss();
						     	}else if(hr_picker_from==hr_picker_to){
						     		if(min_picker_from<min_picker_to){
						     			 free_time_picker=picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to;
						     			setfixedfree_time.setText(free_time_picker);
						     			popup.dismiss();
						     		}
						     		else{
						     			Toast.makeText(getActivity(), "Time from can't greater or equal to time to", 2000).show();
						     		}
						     	}else{
						     		Toast.makeText(getActivity(), "Time from can't greater or equal to time to", 2000).show();
						     	}
						     	
						
						}
					}
				});
				
				String[] values=new String[7];
				values[0]="Monday";
				values[1]="Tuesday";
				values[2]="Wednesday";
				values[3]="Thursday";
				values[4]="Friday";
				values[5]="Saturday";
				values[6]="Sunday";
				npdays_.setMaxValue(values.length-1);
				npdays_.setMinValue(0);
				npdays_.setDisplayedValues(values);
				
				npdays_.setOnValueChangedListener(new OnValueChangeListener() {
					
					@Override
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						// TODO Auto-generated method stub
						
						
						String[] values_picked=picker.getDisplayedValues();
						
						picker_days=values_picked[newVal];
						time_display.setText(picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to);

					}
				});
				
				String[] values_hr=new String[24];
				values_hr[0]="01";
				values_hr[1]="02";
				values_hr[2]="03";
				values_hr[3]="04";
				values_hr[4]="05";
				values_hr[5]="06";
				values_hr[6]="07";
				values_hr[7]="08";
				values_hr[8]="09";
				values_hr[9]="10";
				values_hr[10]="11";
				values_hr[11]="12";
				values_hr[12]="13";
				values_hr[13]="14";
				values_hr[14]="15";
				values_hr[15]="16";
				values_hr[16]="17";
				values_hr[17]="18";
				values_hr[18]="19";
				values_hr[19]="20";
				values_hr[20]="21";
				values_hr[21]="22";
				values_hr[22]="23";
				values_hr[23]="24";
				
				
				np_hr_from.setMaxValue(values_hr.length-1);
				np_hr_from.setMinValue(0);
				np_hr_from.setDisplayedValues(values_hr);
				
				np_hr_from.setOnValueChangedListener(new OnValueChangeListener() {
					
					@Override
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						// TODO Auto-generated method stub
						
						
						String[] values_picked=picker.getDisplayedValues();
						
						picker_hr_from=values_picked[newVal];
						time_display.setText(picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to);

					}
				});

				np_hr_to.setMaxValue(values_hr.length-1);
				np_hr_to.setMinValue(0);
				np_hr_to.setDisplayedValues(values_hr);
				
				np_hr_to.setOnValueChangedListener(new OnValueChangeListener() {
					
					@Override
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						// TODO Auto-generated method stub
						
						
						String[] values_picked=picker.getDisplayedValues();
						
						picker_hr_to=values_picked[newVal];
						time_display.setText(picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to);

					}
				});
				
				String[] values_min=new String[4];
				values_min[0]="00";
				values_min[1]="15";
				values_min[2]="30";
				values_min[3]="45";
				
				np_min_from.setMaxValue(values_min.length-1);
				np_min_from.setMinValue(0);
				np_min_from.setDisplayedValues(values_min);
				
				np_min_from.setOnValueChangedListener(new OnValueChangeListener() {
					
					@Override
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						// TODO Auto-generated method stub
						
						
						String[] values_picked=picker.getDisplayedValues();
						
						picker_min_from=values_picked[newVal];
						time_display.setText(picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to);

					}
				});
				
				np_min_to.setMaxValue(values_min.length-1);
				np_min_to.setMinValue(0);
				np_min_to.setDisplayedValues(values_min);
				
				np_min_to.setOnValueChangedListener(new OnValueChangeListener() {
					
					@Override
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						// TODO Auto-generated method stub
						
						
						String[] values_picked=picker.getDisplayedValues();
						
						picker_min_to=values_picked[newVal];
						time_display.setText(picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to);

					}
				});
				

			}
		});*/

	/*	addmore_freetime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hours = new Time(System.currentTimeMillis()).getHours();
				minutes = new Time(System.currentTimeMillis()).getMinutes();

				LinearLayout viewGroup = (LinearLayout) getActivity()
						.findViewById(R.id.weekdays_selection);
				System.out.println("view Started11");

				System.out.println("view Started22");
				View layout = inflater.inflate(R.layout.weekdays, viewGroup);
				System.out.println("view Started33");

				final PopupWindow popup = new PopupWindow(getActivity());
				popup.setContentView(layout);

				popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
				popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
				popup.setFocusable(true);

				int OFFSET_X = 0;
				int OFFSET_Y = 0;

				popup.setBackgroundDrawable(new BitmapDrawable());

				popup.showAtLocation(getView(), Gravity.NO_GRAVITY, OFFSET_X,
						OFFSET_Y);

				RelativeLayout monday = (RelativeLayout) layout
						.findViewById(R.id.Monday);
				monday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// dayselect=1;
						clicked = true;

						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenermonto, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenermonto,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenermon, hours,
						// minutes, true);
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();

						CustomTimePickerDialog tiPickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenermon,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						tiPickerDialog.setTitle("FROM");
						tiPickerDialog.show();

						popup.dismiss();

					}
				});
				RelativeLayout tuesday = (RelativeLayout) layout
						.findViewById(R.id.Tuesday);
				tuesday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// dayselect=2;
						clicked = true;

						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenertueto, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenertueto,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenertue, hours,
						// minutes, true);
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();

						CustomTimePickerDialog tiPickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenertue,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						tiPickerDialog.setTitle("FROM");
						tiPickerDialog.show();

						popup.dismiss();
					}
				});
				RelativeLayout Wednesday = (RelativeLayout) layout
						.findViewById(R.id.Wednesday);
				Wednesday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// dayselect=3;
						clicked = true;

						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenerwedto, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenerwedto,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenerwed, hours,
						// minutes, true);
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();

						CustomTimePickerDialog tiPickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenerwed,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						tiPickerDialog.setTitle("FROM");
						tiPickerDialog.show();

						popup.dismiss();

					}
				});
				RelativeLayout Thursday = (RelativeLayout) layout
						.findViewById(R.id.Thursday);
				Thursday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// dayselect=4;
						clicked = true;

						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenerthuto, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenerthuto,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenerthu, hours,
						// minutes, true);
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();

						CustomTimePickerDialog tiPickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenerthu,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						tiPickerDialog.setTitle("FROM");
						tiPickerDialog.show();

						popup.dismiss();

					}
				});
				RelativeLayout Friday = (RelativeLayout) layout
						.findViewById(R.id.Friday);
				Friday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// dayselect=5;
						clicked = true;
						//
						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenerfrito, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenerfrito,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenerfri, hours,
						// minutes, true);
						//
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();

						CustomTimePickerDialog tiPickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenerfri,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						tiPickerDialog.setTitle("FROM");
						tiPickerDialog.show();

						popup.dismiss();

					}
				});
				RelativeLayout Saturday = (RelativeLayout) layout
						.findViewById(R.id.Saturday);
				Saturday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// dayselect=6;
						clicked = true;

						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenersatto, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenerfrito,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenersat, hours,
						// minutes, true);
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();

						CustomTimePickerDialog tiPickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenersat,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						tiPickerDialog.setTitle("FROM");
						tiPickerDialog.show();

						System.out.println(timechk6);
						popup.dismiss();

					}
				});
				RelativeLayout Sunday = (RelativeLayout) layout
						.findViewById(R.id.Sunday);
				Sunday.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						clicked = true;
						// TimePickerDialog tiiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenersunto, hours,
						// minutes, true);
						// tiiPickerDialog.setTitle("TO");
						// tiiPickerDialog.show();

						CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenersunto,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						timePickerDialog.setTitle("TO");
						timePickerDialog.show();

						// TimePickerDialog tiPickerDialog = new
						// TimePickerDialog(
						// getActivity(), mTimesetlistenersun, hours,
						// minutes, true);
						// tiPickerDialog.setTitle("FROM");
						// tiPickerDialog.show();
						// System.out.println(timechk7);

						CustomTimePickerDialog tiPickerDialog = new CustomTimePickerDialog(
								getActivity(),
								mTimesetlistenersun,
								Calendar.getInstance().get(Calendar.HOUR),
								CustomTimePickerDialog
										.getRoundedMinute(Calendar
												.getInstance().get(
														Calendar.MINUTE)
												+ CustomTimePickerDialog.TIME_PICKER_INTERVAL),
								true);
						tiPickerDialog.setTitle("FROM");
						tiPickerDialog.show();

						popup.dismiss();
					}
				});

				
				picker_days="";picker_hr_from="";picker_hr_to="";picker_min_from="";picker_min_to="";
				LinearLayout viewGroup = (LinearLayout) getActivity().findViewById(R.id.numberpicker);
				View layout = inflater.inflate(R.layout.number_picker, viewGroup);
				final PopupWindow popup = new PopupWindow(getActivity());
				popup.setContentView(layout);
				popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
				popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
				popup.setFocusable(true);
				int OFFSET_X = 0;
				int OFFSET_Y = 0;
				popup.setBackgroundDrawable(new BitmapDrawable());
				popup.showAtLocation(getView(), Gravity.NO_GRAVITY, OFFSET_X,OFFSET_Y);
				
				NumberPicker npdays_=
						   (NumberPicker) layout.findViewById(R.id.numberPicker_days);
				NumberPicker np_hr_from=
						   (NumberPicker) layout.findViewById(R.id.numberPicker_hr_from);
				NumberPicker np_min_from=
						   (NumberPicker) layout.findViewById(R.id.numberPicker_min_from);
				NumberPicker np_hr_to=
						   (NumberPicker) layout.findViewById(R.id.numberPicker_hr_to);
				NumberPicker np_min_to=
						   (NumberPicker) layout.findViewById(R.id.numberPicker_min_to);
				final TextView time_display=(TextView) layout.findViewById(R.id.time_numberpicker);
				TextView btn_done_picker=(TextView) layout.findViewById(R.id.done_numberpicker);
				TextView btn_cancel_picker=(TextView) layout.findViewById(R.id.cancel_numberpicker);
				
				btn_cancel_picker.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						popup.dismiss();
					}
				});
				
				btn_done_picker.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(picker_hr_from.equals("") || picker_hr_to.equals("") ){
							Toast.makeText(getActivity(),"Please select time", 2000).show();
						}else{
							if(picker_days.equals("")){
								picker_days="Monday";
							}
							if(picker_min_from.equals("")){
								picker_min_from="00";
							}
							if(picker_min_to.equals("")){
								picker_min_to="00";
							}
							 int hr_picker_from=Integer.parseInt(picker_hr_from);
						     	int min_picker_from=Integer.parseInt(picker_min_from);
						     	int hr_picker_to=Integer.parseInt(picker_hr_to);
						     	int min_picker_to=Integer.parseInt(picker_min_to);
						     	
						     	if(hr_picker_from<hr_picker_to){
						     		 free_time_picker=free_time_picker +" "+picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to;
						     		setfixedfree_time.setText(free_time_picker);
						     		popup.dismiss();
						     	}else if(hr_picker_from==hr_picker_to){
						     		if(min_picker_from<min_picker_to){
						     			 free_time_picker=free_time_picker +" "+picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to;
						     			setfixedfree_time.setText(free_time_picker);
						     			popup.dismiss();
						     		}
						     		else{
						     			Toast.makeText(getActivity(), "Time from can't greater or equal to time to", 2000).show();
						     		}
						     	}else{
						     		Toast.makeText(getActivity(), "Time from can't greater or equal to time to", 2000).show();
						     	}
						}
					}
				});
				
				String[] values=new String[7];
				values[0]="Monday";
				values[1]="Tuesday";
				values[2]="Wednesday";
				values[3]="Thursday";
				values[4]="Friday";
				values[5]="Saturday";
				values[6]="Sunday";
				npdays_.setMaxValue(values.length-1);
				npdays_.setMinValue(0);
				npdays_.setDisplayedValues(values);
				
				npdays_.setOnValueChangedListener(new OnValueChangeListener() {
					
					@Override
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						// TODO Auto-generated method stub
						
						
						String[] values_picked=picker.getDisplayedValues();
						
						 picker_days=values_picked[newVal];
						 time_display.setText(picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to);

					}
				});
				
				String[] values_hr=new String[24];
				values_hr[0]="01";
				values_hr[1]="02";
				values_hr[2]="03";
				values_hr[3]="04";
				values_hr[4]="05";
				values_hr[5]="06";
				values_hr[6]="07";
				values_hr[7]="08";
				values_hr[8]="09";
				values_hr[9]="10";
				values_hr[10]="11";
				values_hr[11]="12";
				values_hr[12]="13";
				values_hr[13]="14";
				values_hr[14]="15";
				values_hr[15]="16";
				values_hr[16]="17";
				values_hr[17]="18";
				values_hr[18]="19";
				values_hr[19]="20";
				values_hr[20]="21";
				values_hr[21]="22";
				values_hr[22]="23";
				values_hr[23]="24";
				
				np_hr_from.setMaxValue(values_hr.length-1);
				np_hr_from.setMinValue(0);
				np_hr_from.setDisplayedValues(values_hr);
				
				np_hr_from.setOnValueChangedListener(new OnValueChangeListener() {
					
					@Override
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						// TODO Auto-generated method stub
						
						
						String[] values_picked=picker.getDisplayedValues();
						
						picker_hr_from=values_picked[newVal];
						time_display.setText(picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to);

					}
				});

				np_hr_to.setMaxValue(values_hr.length-1);
				np_hr_to.setMinValue(0);
				np_hr_to.setDisplayedValues(values_hr);
				
				np_hr_to.setOnValueChangedListener(new OnValueChangeListener() {
					
					@Override
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						// TODO Auto-generated method stub
						
						
						String[] values_picked=picker.getDisplayedValues();
						
						picker_hr_to=values_picked[newVal];
						time_display.setText(picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to);

					}
				});
				
				String[] values_min=new String[4];
				values_min[0]="00";
				values_min[1]="15";
				values_min[2]="30";
				values_min[3]="45";
				
				np_min_from.setMaxValue(values_min.length-1);
				np_min_from.setMinValue(0);
				np_min_from.setDisplayedValues(values_min);
				
				np_min_from.setOnValueChangedListener(new OnValueChangeListener() {
					
					@Override
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						// TODO Auto-generated method stub
						
						
						String[] values_picked=picker.getDisplayedValues();
						
						picker_min_from=values_picked[newVal];
						time_display.setText(picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to);

					}
				});
				
				np_min_to.setMaxValue(values_min.length-1);
				np_min_to.setMinValue(0);
				np_min_to.setDisplayedValues(values_min);
				
				np_min_to.setOnValueChangedListener(new OnValueChangeListener() {
					
					@Override
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						// TODO Auto-generated method stub
						
						
						String[] values_picked=picker.getDisplayedValues();
						
						picker_min_to=values_picked[newVal];
						time_display.setText(picker_days.toUpperCase()+" "+picker_hr_from+":"+picker_min_from+" - "+picker_hr_to+":"+picker_min_to);

					}
				});
				
			}
		});*/
		allergies.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//allergies.setText("");

				// Inflate the popup_layout.xml
				LinearLayout viewGroup = (LinearLayout) getActivity()
						.findViewById(R.id.popup_allergies);
				System.out.println("view Started11");

				System.out.println("view Started22");
				View layout = inflater.inflate(R.layout.allergies, viewGroup);
				System.out.println("view Started33");
				// Creating the PopupWindow
				final PopupWindow popup = new PopupWindow(getActivity());
				popup.setContentView(layout);

				popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
				popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
				popup.setFocusable(true);

				// Some offset to align the popup a bit to the right, and a bit
				// down,
				// relative to button's position.
				int OFFSET_X = 0;
				int OFFSET_Y = 0;

				// Clear the default translucent background
				popup.setBackgroundDrawable(new BitmapDrawable());

				// Displaying the popup at the specified location, + offsets.
				popup.showAtLocation(getView(), Gravity.NO_GRAVITY, OFFSET_X,
						OFFSET_Y);

				RelativeLayout nutmilk = (RelativeLayout) layout
						.findViewById(R.id.Nutmilk);
				final CheckBox chk = (CheckBox) nutmilk
						.findViewById(R.id.checkBox1_1);
				chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (i % 2 == 0) {
							nutMilk = "";
							System.out.println("even" + nutMilk);
							i++;
						} else {
							nutMilk = "Nutmilk";
							System.out.println("odd" + nutMilk);
							i++;
						}
					}
				});

				RelativeLayout egg1 = (RelativeLayout) layout
						.findViewById(R.id.egg);
				final CheckBox chk2 = (CheckBox) egg1
						.findViewById(R.id.checkBox1_2);
				chk2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (i2 % 2 == 0) {
							egg = "";
							System.out.println("even" + egg);
							i2++;
						} else {
							egg = "Egg";
							System.out.println("odd" + egg);
							i2++;
						}
					}
				});

				RelativeLayout wheat1 = (RelativeLayout) layout
						.findViewById(R.id.wheat);
				final CheckBox chk3 = (CheckBox) wheat1
						.findViewById(R.id.checkBox1_3);
				chk3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (i3 % 2 == 0) {
							wheat = "";
							System.out.println("even" + wheat);
							i3++;
						} else {
							wheat = "Wheat";
							System.out.println("odd" + wheat);
							i3++;
						}
					}
				});
				RelativeLayout soyfish1 = (RelativeLayout) layout
						.findViewById(R.id.SoyFish);
				final CheckBox chk4 = (CheckBox) soyfish1
						.findViewById(R.id.checkBox1_4);
				chk4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (i4 % 2 == 0) {
							soyfish = "";
							System.out.println("even" + soyfish);
							i4++;
						} else {
							soyfish = "Soyfish";
							System.out.println("odd" + soyfish);
							i4++;
						}
					}
				});
				RelativeLayout Corn = (RelativeLayout) layout
						.findViewById(R.id.Corn);
				final CheckBox chk5 = (CheckBox) Corn
						.findViewById(R.id.checkBox1_5);
				chk5.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (i5 % 2 == 0) {
							corn = "";
							System.out.println("even" + corn);
							i5++;
						} else {
							corn = "Corn";
							System.out.println("odd" + corn);
							i5++;
						}
					}
				});
				RelativeLayout GelatinMeat = (RelativeLayout) layout
						.findViewById(R.id.GelatinMeat);
				final CheckBox chk6 = (CheckBox) GelatinMeat
						.findViewById(R.id.checkBox1_6);
				chk6.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (i6 % 2 == 0) {
							gelate = "";
							System.out.println("even" + gelate);
							i6++;
						} else {
							gelate = "GelatinMeat";
							System.out.println("odd" + gelate);
							i6++;
						}
					}
				});
				RelativeLayout Seeds = (RelativeLayout) layout
						.findViewById(R.id.Seeds);
				final CheckBox chk7 = (CheckBox) Seeds
						.findViewById(R.id.checkBox1_7);
				chk7.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (i7 % 2 == 0) {
							seed = "";
							System.out.println("even" + seed);
							i7++;
						} else {
							seed = "Seeds";
							System.out.println("odd" + seed);
							i7++;
						}
					}
				});

				RelativeLayout Spices = (RelativeLayout) layout
						.findViewById(R.id.Spices);
				final CheckBox chk8 = (CheckBox) Spices
						.findViewById(R.id.checkBox1_8);
				chk8.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (i8 % 2 == 0) {
							spices = "";
							System.out.println("even" + spices);
							i8++;
						} else {
							spices = "Spices";
							System.out.println("odd" + spices);
							i8++;
						}
					}
				});
				RelativeLayout Grass = (RelativeLayout) layout
						.findViewById(R.id.Grass);
				final CheckBox chk9 = (CheckBox) Grass
						.findViewById(R.id.checkBox1_9);
				chk9.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (i9 % 2 == 0) {
							grass = "";
							System.out.println("even" + grass);
							i9++;
						} else {
							grass = "Grass";
							System.out.println("odd" + grass);
							i9++;
						}
					}
				});
				RelativeLayout Banana = (RelativeLayout) layout
						.findViewById(R.id.Banana);
				final CheckBox chk10 = (CheckBox) Banana
						.findViewById(R.id.checkBox1_10);
				chk10.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (i10 % 2 == 0) {
							banana = "";
							System.out.println("even" + banana);
							i10++;
						} else {
							banana = "Banana";
							System.out.println("odd" + banana);
							i10++;
						}
					}
				});

				RelativeLayout DairyAnaphylaxis = (RelativeLayout) layout
						.findViewById(R.id.DairyAnaphylaxis);
				final CheckBox chk11 = (CheckBox) DairyAnaphylaxis
						.findViewById(R.id.checkBox1_11);
				chk11.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (i11 % 2 == 0) {
							dairy = "";
							System.out.println("even" + dairy);
							i11++;
						} else {
							dairy = "Dairy Anaphy laxis";
							System.out.println("odd" + dairy);
							i11++;
						}
					}
				});

				RelativeLayout HayfeaverInsect = (RelativeLayout) layout
						.findViewById(R.id.HayfeaverInsect);
				final CheckBox chk12 = (CheckBox) HayfeaverInsect
						.findViewById(R.id.checkBox1_12);
				chk12.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (i12 % 2 == 0) {
							hay = "";
							System.out.println("even" + hay);
							i12++;
						} else {
							hay = "Hay feaver Insect";
							System.out.println("odd" + hay);
							i12++;
						}
					}
				});
				RelativeLayout Insect = (RelativeLayout) layout
						.findViewById(R.id.Insect);
				final CheckBox chk13 = (CheckBox) Insect
						.findViewById(R.id.checkBox1_13);
				chk13.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (i13 % 2 == 0) {
							insect = "";
							System.out.println("even" + insect);
							i13++;
						} else {
							insect = "Insect";
							System.out.println("odd" + insect);
							i13++;
						}
					}
				});

				RelativeLayout StingsLactose = (RelativeLayout) layout
						.findViewById(R.id.StingsLactose);
				final CheckBox chk14 = (CheckBox) StingsLactose
						.findViewById(R.id.checkBox1_14);
				chk14.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (i14 % 2 == 0) {
							stings = "";
							System.out.println("even" + stings);
							i14++;
						} else {
							stings = "Stings Lactose";
							System.out.println("odd" + stings);
							i14++;
						}
					}
				});
				RelativeLayout CeliacGluten = (RelativeLayout) layout
						.findViewById(R.id.CeliacGluten);
				final CheckBox chk15 = (CheckBox) CeliacGluten
						.findViewById(R.id.checkBox1_15);
				chk15.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						String nutmilk = "";

						if (i15 % 2 == 0) {
							celiacGluten = "";
							System.out.println("even" + celiacGluten);
							i15++;
						} else {
							celiacGluten = "CeliacGluten";
							System.out.println("odd" + celiacGluten);
							i15++;
						}
					}
				});
				RelativeLayout none_layout = (RelativeLayout) layout
						.findViewById(R.id.None);
				CheckBox chk_none = (CheckBox) none_layout
						.findViewById(R.id.checkBox1_1_none);
				chk_none.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (i_none % 2 == 0) {
							none = "";
							System.out.println("even" + nutMilk);
							i_none++;
							nutMilk = "";
							egg = "";
							wheat = "";
							soyfish = "";
							corn = "";
							gelate = "";
							seed = "";
							spices = "";
							grass = "";
							banana = "";
							dairy = "";
							hay = "";
							insect = "";
							stings = "";
							celiacGluten = "";
							other = "";
							i = 1;
							i2 = 1;
							i3 = 1;
							i4 = 1;
							i5 = 1;
							i6 = 1;
							i7 = 1;
							i8 = 1;
							i9 = 1;
							i10 = 1;
							i11 = 1;
							i12 = 1;
							i13 = 1;
							i14 = 1;
							i15 = 1;

							chk15.setVisibility(View.VISIBLE);
							chk14.setVisibility(View.VISIBLE);
							chk13.setVisibility(View.VISIBLE);
							chk12.setVisibility(View.VISIBLE);
							chk11.setVisibility(View.VISIBLE);
							chk10.setVisibility(View.VISIBLE);
							chk9.setVisibility(View.VISIBLE);
							chk8.setVisibility(View.VISIBLE);
							chk7.setVisibility(View.VISIBLE);
							chk6.setVisibility(View.VISIBLE);
							chk5.setVisibility(View.VISIBLE);
							chk4.setVisibility(View.VISIBLE);
							chk3.setVisibility(View.VISIBLE);
							chk2.setVisibility(View.VISIBLE);
							chk.setVisibility(View.VISIBLE);

						} else {

							none = "None";
							System.out.println("odd" + nutMilk);
							i_none++;
							nutMilk = "";
							egg = "";
							wheat = "";
							soyfish = "";
							corn = "";
							gelate = "";
							seed = "";
							spices = "";
							grass = "";
							banana = "";
							dairy = "";
							hay = "";
							insect = "";
							stings = "";
							celiacGluten = "";
							other = "";
							i = 1;
							i2 = 1;
							i3 = 1;
							i4 = 1;
							i5 = 1;
							i6 = 1;
							i7 = 1;
							i8 = 1;
							i9 = 1;
							i10 = 1;
							i11 = 1;
							i12 = 1;
							i13 = 1;
							i14 = 1;
							i15 = 1;
							chk15.setVisibility(View.INVISIBLE);
							chk14.setVisibility(View.INVISIBLE);
							chk13.setVisibility(View.INVISIBLE);
							chk12.setVisibility(View.INVISIBLE);
							chk11.setVisibility(View.INVISIBLE);
							chk10.setVisibility(View.INVISIBLE);
							chk9.setVisibility(View.INVISIBLE);
							chk8.setVisibility(View.INVISIBLE);
							chk7.setVisibility(View.INVISIBLE);
							chk6.setVisibility(View.INVISIBLE);
							chk5.setVisibility(View.INVISIBLE);
							chk4.setVisibility(View.INVISIBLE);
							chk3.setVisibility(View.INVISIBLE);
							chk2.setVisibility(View.INVISIBLE);
							chk.setVisibility(View.INVISIBLE);
							chk15.setChecked(false);
							chk14.setChecked(false);
							chk13.setChecked(false);
							chk12.setChecked(false);
							chk11.setChecked(false);
							chk10.setChecked(false);
							chk9.setChecked(false);
							chk8.setChecked(false);
							chk7.setChecked(false);
							chk.setChecked(false);
							chk6.setChecked(false);
							chk5.setChecked(false);
							chk4.setChecked(false);
							chk3.setChecked(false);
							chk2.setChecked(false);

						}
					}
				});
				RelativeLayout edittext = (RelativeLayout) layout
						.findViewById(R.id.edit_allergies);
				editallergies = (EditText) edittext
						.findViewById(R.id.editText_allergies_xml);

				RelativeLayout submit = (RelativeLayout) layout
						.findViewById(R.id.button_submit_allergies);
				Button submit1 = (Button) submit
						.findViewById(R.id.button1_allergies);
				submit1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						allergies_selected = "";
						if (none.equals("")) {

							other = editallergies.getText().toString();
							if (other.equals("") || other.equals(null)) {

							} else {

								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = other;
								} else {
									allergies_selected = allergies_selected
											+ "," + other;
								}
							}
							if (!nutMilk.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = nutMilk;
								} else {
									allergies_selected = allergies_selected
											+ "," + nutMilk;
								}
							}
							if (!egg.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = egg;
								} else {
									allergies_selected = allergies_selected
											+ "," + egg;
								}
							}
							if (!wheat.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = wheat;
								} else {
									allergies_selected = allergies_selected
											+ "," + wheat;
								}
							}
							if (!soyfish.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = soyfish;
								} else {
									allergies_selected = allergies_selected
											+ "," + soyfish;
								}
							}
							if (!corn.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = corn;
								} else {
									allergies_selected = allergies_selected
											+ "," + corn;
								}
							}
							if (!gelate.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = gelate;
								} else {
									allergies_selected = allergies_selected
											+ "," + gelate;
								}
							}
							if (!seed.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = seed;
								} else {
									allergies_selected = allergies_selected
											+ "," + seed;
								}
							}
							if (!spices.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = spices;
								} else {
									allergies_selected = allergies_selected
											+ "," + spices;
								}
							}
							if (!grass.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = grass;
								} else {
									allergies_selected = allergies_selected
											+ "," + grass;
								}
							}
							if (!dairy.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = dairy;
								} else {
									allergies_selected = allergies_selected
											+ "," + banana;
								}
							}
							if (!dairy.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = dairy;
								} else {
									allergies_selected = allergies_selected
											+ "," + dairy;
								}
							}
							if (!hay.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = hay;
								} else {
									allergies_selected = allergies_selected
											+ "," + hay;
								}
							}
							if (!insect.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = insect;
								} else {
									allergies_selected = allergies_selected
											+ "," + insect;
								}
							}
							if (!stings.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = stings;
								} else {
									allergies_selected = allergies_selected
											+ "," + stings;
								}
							}
							if (!celiacGluten.equals("")) {
								if (allergies_selected.equals("")
										|| allergies_selected.equals(null)) {
									allergies_selected = celiacGluten;
								} else {
									allergies_selected = allergies_selected
											+ "," + celiacGluten;
								}
							}
						} else {
							allergies_selected = "None";
						}
						System.out.println(allergies_selected);
						
						if(allergies_selected.equals("")||allergies_selected.equals(null)){
							
						}else{
							allergies.setText(allergies_selected.toUpperCase());
						}
						
						none = "";
						nutMilk = "";
						egg = "";
						wheat = "";
						soyfish = "";
						corn = "";
						gelate = "";
						seed = "";
						spices = "";
						grass = "";
						banana = "";
						dairy = "";
						hay = "";
						insect = "";
						stings = "";
						celiacGluten = "";
						other = "";
						i_none = 1;
						i = 1;
						i2 = 1;
						i3 = 1;
						i4 = 1;
						i5 = 1;
						i6 = 1;
						i7 = 1;
						i8 = 1;
						i9 = 1;
						i10 = 1;
						i11 = 1;
						i12 = 1;
						i13 = 1;
						i14 = 1;
						i15 = 1;

						popup.dismiss();
					}

				});

			}

		});

	/*	hobbies.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hobbies.setText("");

				// Inflate the popup_layout.xml
				LinearLayout viewGroup = (LinearLayout) getActivity()
						.findViewById(R.id.popup_hobbies);
				System.out.println("view Started11");

				System.out.println("view Started22");
				View layout = inflater.inflate(R.layout.hobbies, viewGroup);
				System.out.println("view Started33");
				// Creating the PopupWindow
				final PopupWindow popup = new PopupWindow(getActivity());
				popup.setContentView(layout);

				popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
				popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
				popup.setFocusable(true);

				// Some offset to align the popup a bit to the right, and a bit
				// down,
				// relative to button's position.
				int OFFSET_X = 0;
				int OFFSET_Y = 0;

				// Clear the default translucent background
				popup.setBackgroundDrawable(new BitmapDrawable());

				// Displaying the popup at the specified location, + offsets.
				popup.showAtLocation(getView(), Gravity.NO_GRAVITY, OFFSET_X,
						OFFSET_Y);

				RelativeLayout cooKing = (RelativeLayout) layout
						.findViewById(R.id.Cooking);
				final CheckBox chk = (CheckBox) cooKing
						.findViewById(R.id.checkBox11_1);
				chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii % 2 == 0) {
							cooking = "";
							System.out.println("even" + cooking);
							ii++;
						} else {
							cooking = "Cooking";
							System.out.println("odd" + cooking);
							ii++;
						}
					}
				});

				RelativeLayout Dance = (RelativeLayout) layout
						.findViewById(R.id.Dance);
				final CheckBox chk2 = (CheckBox) Dance
						.findViewById(R.id.checkBox11_2);
				chk2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii2 % 2 == 0) {
							dance = "";
							System.out.println("even" + dance);
							ii2++;
						} else {
							dance = "Dance";
							System.out.println("odd" + dance);
							ii2++;
						}
					}
				});

				RelativeLayout Drama = (RelativeLayout) layout
						.findViewById(R.id.Drama);
				final CheckBox chk3 = (CheckBox) Drama
						.findViewById(R.id.checkBox11_3);
				chk3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii3 % 2 == 0) {
							drama = "";
							System.out.println("even" + drama);
							ii3++;
						} else {
							drama = "Drama";
							System.out.println("odd" + drama);
							ii3++;
						}
					}
				});
				RelativeLayout Drawing = (RelativeLayout) layout
						.findViewById(R.id.Drawing);
				final CheckBox chk4 = (CheckBox) Drawing
						.findViewById(R.id.checkBox11_4);
				chk4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii4 % 2 == 0) {
							drawing = "";
							System.out.println("even" + drawing);
							ii4++;
						} else {
							drawing = "Drawing";
							System.out.println("odd" + drawing);
							ii4++;
						}
					}
				});
				RelativeLayout Lego = (RelativeLayout) layout
						.findViewById(R.id.Lego);
				final CheckBox chk5 = (CheckBox) Lego.findViewById(R.id.checkBox11_5);
				chk5.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii5 % 2 == 0) {
							lego = "";
							System.out.println("even" + lego);
							ii5++;
						} else {
							lego = "Lego";
							System.out.println("odd" + lego);
							ii5++;
						}
					}
				});
				RelativeLayout BuildingMagicModel = (RelativeLayout) layout
						.findViewById(R.id.BuildingMagicModel);
				final CheckBox chk6 = (CheckBox) BuildingMagicModel
						.findViewById(R.id.checkBox11_6);
				chk6.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii6 % 2 == 0) {
							buildingMagicModel = "";
							System.out.println("even" + buildingMagicModel);
							ii6++;
						} else {
							buildingMagicModel = "Building Magic Model";
							System.out.println("odd" + buildingMagicModel);
							ii6++;
						}
					}
				});
				RelativeLayout Painting = (RelativeLayout) layout
						.findViewById(R.id.Painting);
				final CheckBox chk7 = (CheckBox) Painting
						.findViewById(R.id.checkBox11_7);
				chk7.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii7 % 2 == 0) {
							painting = "";
							System.out.println("even" + painting);
							ii7++;
						} else {
							painting = "Painting";
							System.out.println("odd" + painting);
							ii7++;
						}
					}
				});

				RelativeLayout Puzzles = (RelativeLayout) layout
						.findViewById(R.id.Puzzles);
				final CheckBox chk8 = (CheckBox) Puzzles
						.findViewById(R.id.checkBox11_8);
				chk8.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii8 % 2 == 0) {
							puzzles = "";
							System.out.println("even" + puzzles);
							ii8++;
						} else {
							puzzles = "Puzzles";
							System.out.println("odd" + puzzles);
							ii8++;
						}
					}
				});
				RelativeLayout Scrapbooking = (RelativeLayout) layout
						.findViewById(R.id.Scrapbooking);
				final CheckBox chk9 = (CheckBox) Scrapbooking
						.findViewById(R.id.checkBox11_9);
				chk9.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii9 % 2 == 0) {
							scrapbooking = "";
							System.out.println("even" + scrapbooking);
							ii9++;
						} else {
							scrapbooking = "Scrapbooking";
							System.out.println("odd" + scrapbooking);
							ii9++;
						}
					}
				});
				RelativeLayout Sewing = (RelativeLayout) layout
						.findViewById(R.id.Sewing);
				final CheckBox chk10 = (CheckBox) Sewing
						.findViewById(R.id.checkBox11_10);
				chk10.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii10 % 2 == 0) {
							sewing = "";
							System.out.println("even" + sewing);
							ii10++;
						} else {
							sewing = "Sewing";
							System.out.println("odd" + banana);
							ii10++;
						}
					}
				});

				RelativeLayout Singing = (RelativeLayout) layout
						.findViewById(R.id.Singing);
				final CheckBox chk11 = (CheckBox) Singing
						.findViewById(R.id.checkBox11_11);
				chk11.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii11 % 2 == 0) {
							singing = "";
							System.out.println("even" + singing);
							ii11++;
						} else {
							singing = "Singing";
							System.out.println("odd" + singing);
							ii11++;
						}
					}
				});

				RelativeLayout Videogaming = (RelativeLayout) layout
						.findViewById(R.id.Videogaming);
				final CheckBox chk12 = (CheckBox) Videogaming
						.findViewById(R.id.checkBox11_12);
				chk12.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii12 % 2 == 0) {
							videogaming = "";
							System.out.println("even" + videogaming);
							ii12++;
						} else {
							videogaming = "Videogaming";
							System.out.println("odd" + videogaming);
							ii12++;
						}
					}
				});
				RelativeLayout Woodworking = (RelativeLayout) layout
						.findViewById(R.id.Woodworking);
				final CheckBox chk13 = (CheckBox) Woodworking
						.findViewById(R.id.checkBox11_13);
				chk13.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii13 % 2 == 0) {
							woodworking = "";
							System.out.println("even" + woodworking);
							ii13++;
						} else {
							woodworking = "Woodworking";
							System.out.println("odd" + woodworking);
							ii13++;
						}
					}
				});

				RelativeLayout Writing = (RelativeLayout) layout
						.findViewById(R.id.Writing);
				final CheckBox chk14 = (CheckBox) Writing
						.findViewById(R.id.checkBox11_14);
				chk14.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii14 % 2 == 0) {
							writing = "";
							System.out.println("even" + writing);
							ii14++;
						} else {
							writing = "Writing";
							System.out.println("odd" + writing);
							ii14++;
						}
					}
				});

				RelativeLayout Skating = (RelativeLayout) layout
						.findViewById(R.id.Skating);
				final CheckBox chk15 = (CheckBox) Skating
						.findViewById(R.id.checkBox11_15);
				chk15.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						String nutmilk = "";

						if (ii15 % 2 == 0) {
							skating = "";
							System.out.println("even" + skating);
							ii15++;
						} else {
							skating = "Skating";
							System.out.println("odd" + skating);
							ii15++;
						}
					}
				});
				RelativeLayout SkiingSurfing = (RelativeLayout) layout
						.findViewById(R.id.SkiingSurfing);
				final CheckBox chk16 = (CheckBox) SkiingSurfing
						.findViewById(R.id.checkBox11_16);
				chk16.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii16 % 2 == 0) {
							skiingSurfing = "";
							System.out.println("even" + skiingSurfing);
							ii16++;
						} else {
							skiingSurfing = "Skiing Surfing";
							System.out.println("odd" + skiingSurfing);
							ii16++;
						}
					}
				});
				RelativeLayout Snowboarding = (RelativeLayout) layout
						.findViewById(R.id.Snowboarding);
				final CheckBox chk17 = (CheckBox) Snowboarding
						.findViewById(R.id.checkBox11_17);
				chk17.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii17 % 2 == 0) {
							snowboarding = "";
							System.out.println("even" + snowboarding);
							ii17++;
						} else {
							snowboarding = "Snow boarding";
							System.out.println("odd" + snowboarding);
							ii17++;
						}
					}
				});
				RelativeLayout SwimmingWater = (RelativeLayout) layout
						.findViewById(R.id.SwimmingWater);
				final CheckBox chk18 = (CheckBox) SwimmingWater
						.findViewById(R.id.checkBox11_18);
				chk18.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii18 % 2 == 0) {
							swimmingWater = "";
							System.out.println("even" + swimmingWater);
							ii18++;
						} else {
							swimmingWater = "Swimming Water";
							System.out.println("odd" + swimmingWater);
							ii18++;
						}
					}
				});
				RelativeLayout Football = (RelativeLayout) layout
						.findViewById(R.id.Football);
				final CheckBox chk19 = (CheckBox) Football
						.findViewById(R.id.checkBox11_19);
				chk19.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii19 % 2 == 0) {
							football = "";
							System.out.println("even" + football);
							ii19++;
						} else {
							football = "Football";
							System.out.println("odd" + buildingMagicModel);
							ii19++;
						}
					}
				});
				RelativeLayout Baseball = (RelativeLayout) layout
						.findViewById(R.id.Baseball);
				final CheckBox chk20 = (CheckBox) Baseball
						.findViewById(R.id.checkBox11_20);
				chk20.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii20 % 2 == 0) {
							baseball = "";
							System.out.println("even" + baseball);
							ii20++;
						} else {
							baseball = "Baseball";
							System.out.println("odd" + baseball);
							ii20++;
						}
					}
				});

				RelativeLayout Basketball = (RelativeLayout) layout
						.findViewById(R.id.Basketball);
				final CheckBox chk21 = (CheckBox) Basketball
						.findViewById(R.id.checkBox11_21);
				chk21.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii21 % 2 == 0) {
							basketball = "";
							System.out.println("even" + basketball);
							ii21++;
						} else {
							basketball = "Basketball";
							System.out.println("odd" + basketball);
							ii21++;
						}
					}
				});
				RelativeLayout Climbing = (RelativeLayout) layout
						.findViewById(R.id.Climbing);
				final CheckBox chk22 = (CheckBox) Climbing
						.findViewById(R.id.checkBox11_22);
				chk22.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii22 % 2 == 0) {
							climbing = "";
							System.out.println("even" + climbing);
							ii22++;
						} else {
							climbing = "Climbing";
							System.out.println("odd" + climbing);
							ii22++;
						}
					}
				});
				RelativeLayout Cricket = (RelativeLayout) layout
						.findViewById(R.id.Cricket);
				final CheckBox chk23 = (CheckBox) Cricket
						.findViewById(R.id.checkBox11_23);
				chk23.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii23 % 2 == 0) {
							cricket = "";
							System.out.println("even" + cricket);
							ii23++;
						} else {
							cricket = "Cricket";
							System.out.println("odd" + cricket);
							ii23++;
						}
					}
				});

				RelativeLayout Cycling = (RelativeLayout) layout
						.findViewById(R.id.Cycling);
				final CheckBox chk24 = (CheckBox) Cycling
						.findViewById(R.id.checkBox11_24);
				chk24.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii24 % 2 == 0) {
							cycling = "";
							System.out.println("even" + cycling);
							ii24++;
						} else {
							cycling = "Cycling";
							System.out.println("odd" + cycling);
							ii24++;
						}
					}
				});

				RelativeLayout Judo = (RelativeLayout) layout
						.findViewById(R.id.Judo);
				final CheckBox chk25 = (CheckBox) Judo
						.findViewById(R.id.checkBox11_25);
				chk25.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii25 % 2 == 0) {
							judo = "";
							System.out.println("even" + judo);
							ii25++;
						} else {
							judo = "judo";
							System.out.println("odd" + judo);
							ii25++;
						}
					}
				});
				RelativeLayout Running = (RelativeLayout) layout
						.findViewById(R.id.Running);
				final CheckBox chk26 = (CheckBox) Running
						.findViewById(R.id.checkBox11_26);
				chk26.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii26 % 2 == 0) {
							running = "";
							System.out.println("even" + running);
							ii26++;
						} else {
							running = "Running";
							System.out.println("odd" + running);
							ii26++;
						}
					}
				});

				RelativeLayout Table = (RelativeLayout) layout
						.findViewById(R.id.Table);
				final CheckBox chk27 = (CheckBox) Table
						.findViewById(R.id.checkBox11_27);
				chk27.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii27 % 2 == 0) {
							table = "";
							System.out.println("even" + table);
							ii27++;
						} else {
							table = "Table Tennis";
							System.out.println("odd" + writing);
							ii27++;
						}
					}
				});
				RelativeLayout LawnTennis = (RelativeLayout) layout
						.findViewById(R.id.LawnTennis);
				final CheckBox chk28 = (CheckBox) LawnTennis
						.findViewById(R.id.checkBox11_28);
				chk28.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii28 % 2 == 0) {
							lawnTennis = "";
							System.out.println("even" + lawnTennis);
							ii28++;
						} else {
							lawnTennis = "Lawn Tennis";
							System.out.println("odd" + lawnTennis);
							ii28++;
						}
					}
				});
				RelativeLayout Reading = (RelativeLayout) layout
						.findViewById(R.id.Reading);
				final CheckBox chk29 = (CheckBox) Reading
						.findViewById(R.id.checkBox11_29);
				chk29.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii29 % 2 == 0) {
							reading = "";
							System.out.println("even" + reading);
							ii29++;
						} else {
							reading = "Reading";
							System.out.println("odd" + reading);
							ii29++;
						}
					}
				});
				RelativeLayout none_layout = (RelativeLayout) layout
						.findViewById(R.id.None);
			final	CheckBox chk_none = (CheckBox) none_layout
						.findViewById(R.id.checkBox1_1_none);
				chk_none.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (ii_none % 2 == 0) {
							none_hobbies = "";
							System.out.println("even" + nutMilk);
							ii_none++;
							otherhobbies="";
							cooking = ""; dance = ""; drama = ""; drawing = ""; lego = "";
									buildingMagicModel = ""; painting = ""; puzzles = "";
									scrapbooking = ""; sewing = ""; singing = ""; videogaming = "";
									woodworking = ""; writing = ""; skating = ""; otherhobbies = "";
									skiingSurfing = ""; snowboarding = ""; swimmingWater = "";
									football = ""; baseball = ""; basketball = ""; climbing = "";
									cricket = ""; cycling = ""; judo = ""; running = ""; table = "";
									lawnTennis = ""; reading = "";
							ii = 1;ii16=1;ii17=1;ii18=1;ii19=1;ii20=1;ii21=1;ii22=1;ii23=1;ii24=1;ii25=1;ii26=1;ii27=1;ii28=1;ii29=1;
							ii2 = 1;
							ii3 = 1;
							ii4 = 1;
							ii5 = 1;
							ii6 = 1;
							ii7 = 1;
							ii8 = 1;
							ii9 = 1;
							ii10 = 1;
							ii11 = 1;
							ii12 = 1;
							ii13 = 1;
							ii14 = 1;
							ii15 = 1;

							chk29.setVisibility(View.VISIBLE);
							chk28.setVisibility(View.VISIBLE);
							chk27.setVisibility(View.VISIBLE);
							chk26.setVisibility(View.VISIBLE);
							chk25.setVisibility(View.VISIBLE);
							chk24.setVisibility(View.VISIBLE);
							chk23.setVisibility(View.VISIBLE);
							chk22.setVisibility(View.VISIBLE);
							chk21.setVisibility(View.VISIBLE);
							chk20.setVisibility(View.VISIBLE);
							chk19.setVisibility(View.VISIBLE);
							chk18.setVisibility(View.VISIBLE);
							chk17.setVisibility(View.VISIBLE);
							chk16.setVisibility(View.VISIBLE);
							chk15.setVisibility(View.VISIBLE);
							chk14.setVisibility(View.VISIBLE);
							chk13.setVisibility(View.VISIBLE);
							chk12.setVisibility(View.VISIBLE);
							chk11.setVisibility(View.VISIBLE);
							chk10.setVisibility(View.VISIBLE);
							chk9.setVisibility(View.VISIBLE);
							chk8.setVisibility(View.VISIBLE);
							chk7.setVisibility(View.VISIBLE);
							chk6.setVisibility(View.VISIBLE);
							chk5.setVisibility(View.VISIBLE);
							chk4.setVisibility(View.VISIBLE);
							chk3.setVisibility(View.VISIBLE);
							chk2.setVisibility(View.VISIBLE);
							chk.setVisibility(View.VISIBLE);

						} else {

							none_hobbies = "None";
							System.out.println("odd" + nutMilk);
							ii_none++;
							otherhobbies="";
							cooking = ""; dance = ""; drama = ""; drawing = ""; lego = "";
							buildingMagicModel = ""; painting = ""; puzzles = "";
							scrapbooking = ""; sewing = ""; singing = ""; videogaming = "";
							woodworking = ""; writing = ""; skating = ""; otherhobbies = "";
							skiingSurfing = ""; snowboarding = ""; swimmingWater = "";
							football = ""; baseball = ""; basketball = ""; climbing = "";
							cricket = ""; cycling = ""; judo = ""; running = ""; table = "";
							lawnTennis = ""; reading = "";
							i = 1;ii16=1;ii17=1;ii18=1;ii19=1;ii20=1;ii21=1;ii22=1;ii23=1;ii24=1;ii25=1;ii26=1;ii27=1;ii28=1;ii29=1;
							i2 = 1;
							i3 = 1;
							i4 = 1;
							i5 = 1;
							i6 = 1;
							i7 = 1;
							i8 = 1;
							i9 = 1;
							i10 = 1;
							i11 = 1;
							i12 = 1;
							i13 = 1;
							i14 = 1;
							i15 = 1;
							
							chk29.setVisibility(View.INVISIBLE);
							chk28.setVisibility(View.INVISIBLE);
							chk27.setVisibility(View.INVISIBLE);
							chk26.setVisibility(View.INVISIBLE);
							chk25.setVisibility(View.INVISIBLE);
							chk24.setVisibility(View.INVISIBLE);
							chk23.setVisibility(View.INVISIBLE);
							chk22.setVisibility(View.INVISIBLE);
							chk21.setVisibility(View.INVISIBLE);
							chk20.setVisibility(View.INVISIBLE);
							chk19.setVisibility(View.INVISIBLE);
							chk18.setVisibility(View.INVISIBLE);
							chk17.setVisibility(View.INVISIBLE);
							chk16.setVisibility(View.INVISIBLE);
							chk15.setVisibility(View.INVISIBLE);
							chk14.setVisibility(View.INVISIBLE);
							chk13.setVisibility(View.INVISIBLE);
							chk12.setVisibility(View.INVISIBLE);
							chk11.setVisibility(View.INVISIBLE);
							chk10.setVisibility(View.INVISIBLE);
							chk9.setVisibility(View.INVISIBLE);
							chk8.setVisibility(View.INVISIBLE);
							chk7.setVisibility(View.INVISIBLE);
							chk6.setVisibility(View.INVISIBLE);
							chk5.setVisibility(View.INVISIBLE);
							chk4.setVisibility(View.INVISIBLE);
							chk3.setVisibility(View.INVISIBLE);
							chk2.setVisibility(View.INVISIBLE);
							chk.setVisibility(View.INVISIBLE);
							
							chk29.setChecked(false);
							chk28.setChecked(false);
							chk27.setChecked(false);
							chk26.setChecked(false);
							chk25.setChecked(false);
							chk24.setChecked(false);
							chk23.setChecked(false);
							chk22.setChecked(false);
							chk21.setChecked(false);
							chk20.setChecked(false);
							chk19.setChecked(false);
							chk18.setChecked(false);
							chk17.setChecked(false);
							chk16.setChecked(false);
							
							chk15.setChecked(false);
							chk14.setChecked(false);
							chk13.setChecked(false);
							chk12.setChecked(false);
							chk11.setChecked(false);
							chk10.setChecked(false);
							chk9.setChecked(false);
							chk8.setChecked(false);
							chk7.setChecked(false);
							chk.setChecked(false);
							chk6.setChecked(false);
							chk5.setChecked(false);
							chk4.setChecked(false);
							chk3.setChecked(false);
							chk2.setChecked(false);

						}
					}
				});
				RelativeLayout edittext = (RelativeLayout) layout
						.findViewById(R.id.edit_hobbies);
				edit_hobbies = (EditText) edittext
						.findViewById(R.id.editText_hobbies_xml);

				RelativeLayout submit2 = (RelativeLayout) layout
						.findViewById(R.id.button_layout_hobbies);
				Button submit1 = (Button) submit2
						.findViewById(R.id.button1_hobbies);
				submit1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						hobbies_selected = "";
						
						if(none_hobbies.equals("")){
						otherhobbies = edit_hobbies.getText().toString();
						if (otherhobbies.equals("")
								|| otherhobbies.equals(null)) {

						} else {

							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = otherhobbies;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ otherhobbies;
							}
						}

						if (!cooking.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = cooking;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ cooking;
							}
						}
						if (!dance.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = dance;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ dance;
							}
						}
						if (!drama.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = drama;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ drama;
							}
						}
						if (!drawing.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = drawing;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ drawing;
							}
						}

						if (!lego.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = lego;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ lego;
							}
						}
						if (!buildingMagicModel.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = buildingMagicModel;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ buildingMagicModel;
							}
						}
						if (!painting.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = painting;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ painting;
							}
						}
						if (!puzzles.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = puzzles;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ puzzles;
							}
						}
						if (!scrapbooking.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = scrapbooking;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ scrapbooking;
							}
						}
						if (!sewing.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = sewing;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ sewing;
							}
						}
						if (!singing.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = singing;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ singing;
							}
						}
						if (!videogaming.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = videogaming;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ videogaming;
							}
						}
						if (!woodworking.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = woodworking;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ woodworking;
							}
						}
						if (!writing.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = writing;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ writing;
							}
						}

						if (!skating.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = skating;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ skating;
							}
						}
						if (!skiingSurfing.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = skiingSurfing;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ skiingSurfing;
							}
						}
						if (!snowboarding.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = snowboarding;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ snowboarding;
							}
						}
						if (!swimmingWater.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = swimmingWater;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ swimmingWater;
							}
						}
						if (!football.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = football;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ football;
							}
						}
						if (!baseball.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = baseball;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ baseball;
							}
						}
						if (!basketball.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = basketball;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ basketball;
							}
						}
						if (!climbing.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = climbing;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ climbing;
							}
						}
						if (!cricket.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = cricket;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ cricket;
							}
						}
						if (!cycling.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = cycling;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ cycling;
							}
						}
						if (!judo.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = judo;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ judo;
							}
						}
						if (!lawnTennis.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = singing;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ lawnTennis;
							}
						}
						if (!running.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = running;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ running;
							}
						}
						if (!table.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = table;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ table;
							}
						}
						
						 * if (!writing.equals("")) { if
						 * (hobbies_selected.equals("") ||
						 * hobbies_selected.equals(null)) { hobbies_selected =
						 * writing; } else { hobbies_selected = hobbies_selected
						 * + "," + writing; } }
						 
						if (!reading.equals("")) {
							if (hobbies_selected.equals("")
									|| hobbies_selected.equals(null)) {
								hobbies_selected = reading;
							} else {
								hobbies_selected = hobbies_selected + ","
										+ reading;
							}
						}
						System.out.println(hobbies_selected);
						//hobbies.setText(hobbies_selected.toUpperCase());
						//popup.dismiss();
					
					}else {
						hobbies_selected = "None";
					}
					System.out.println(hobbies_selected);
					hobbies.setText(hobbies_selected.toUpperCase());
					none_hobbies="";
					cooking = ""; dance = ""; drama = ""; drawing = ""; lego = "";
					buildingMagicModel = ""; painting = ""; puzzles = "";
					scrapbooking = ""; sewing = ""; singing = ""; videogaming = "";
					woodworking = ""; writing = ""; skating = ""; otherhobbies = "";
					skiingSurfing = ""; snowboarding = ""; swimmingWater = "";
					football = ""; baseball = ""; basketball = ""; climbing = "";
					cricket = ""; cycling = ""; judo = ""; running = ""; table = "";
					lawnTennis = ""; reading = "";
					ii_none=1;
			ii = 1;ii16=1;ii17=1;ii18=1;ii19=1;ii20=1;ii21=1;ii22=1;ii23=1;ii24=1;ii25=1;ii26=1;ii27=1;ii28=1;ii29=1;
			ii2 = 1;
			ii3 = 1;
			ii4 = 1;
			ii5 = 1;
			ii6 = 1;
			ii7 = 1;
			ii8 = 1;
			ii9 = 1;
			ii10 = 1;
			ii11 = 1;
			ii12 = 1;
			ii13 = 1;
			ii14 = 1;
			ii15 = 1;

					popup.dismiss();
				}

				});

			}
		});
*/
		update_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				name_update = name.getText().toString();
				dob_update = dob.getText().toString();
				//freetime_update = setfixedfree_time.getText().toString();
				// conditions_update = conditions.getText().toString();
				allergies_update = allergies.getText().toString();
			//	hobbies_update = hobbies.getText().toString();
			//	school_update = school.getText().toString();
			//	youthclub_update = youthclub.getText().toString();
				// siBlings=sibling.getText().toString();
				if (name.length() > 0) {

//					if (freetime_update.equals(null)
//							|| freetime_update.equals("")) {
//						freetime_update = "";
//					}
					/*
					 * if (conditions_update.equals(null) ||
					 * conditions_update.equals("")) { conditions_update = ""; }
					 */
					if (allergies_update.equals(null)
							|| allergies_update.equals("")) {
						allergies_update = "";
					}
//					if (hobbies_update.equals(null)
//							|| hobbies_update.equals("")) {
//						hobbies_update = "";
//					}
//					if (school_update.equals(null) || school_update.equals("")) {
//						school_update = "";
//					}
					/*if (youthclub_update.equals(null)
							|| youthclub_update.equals("")) {
						youthclub_update = "";
					}*/
					if (dob_update.equals(null) || dob_update.equals("")) {
						dob_update = "";
					}

					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
					Date date_of_birth = null;
					try {
						date_of_birth = sdf.parse(dob_update);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}// String reportDate = df.format(today);
						// birthDay=sdf.format(date_of_birth);
					SimpleDateFormat destDf = new SimpleDateFormat("yyyy-MM-dd");

					// format the date into another format

					date_of_birth_child = destDf.format(date_of_birth);

					System.out.println("date_of_birthdate_of_birth"
							+ date_of_birth_child);

					isInternetPresent = cd.isConnectingToInternet();
					if (isInternetPresent) {
						new chilprofileupdate_webservice().execute();
					} else {
						Toast.makeText(getActivity(),
								"Please check internet connection", 2000)
								.show();

					}

				} else {
					Toast.makeText(getActivity(), "No field will left blank",
							2000).show();
				}

			}
		});

		return view;
	}

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
    	   
			

			try {

				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
				Date date1 = formatter.parse(date_comparision);
				Date date2 = formatter.parse(date_to_set);
				if (date1.compareTo(date2) < 0) {
					if (count_alert == 0) {
						count_alert = count_alert + 1;
						new AlertDialog.Builder(getActivity())

								.setTitle("Invalid Entry")
								.setMessage("can't select future date")
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												return;
											}
										})

								.setIcon(android.R.drawable.ic_dialog_alert)
								.show();
					}

				} else {
					count_alert = 0;
					dob.setText(date_to_set);
				}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	};

	/*CustomTimePickerDialog.OnTimeSetListener mTimesetlistenermon = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			timechk1 = true;
			timechk2 = false;
			timechk3 = false;
			timechk4 = false;
			timechk5 = false;
			timechk6 = false;
			timechk7 = false;
			String hour_s = String.valueOf(hourOfDay);
			int length = hour_s.length();
			if (length == 1) {
				hour_s = "0" + String.valueOf(hourOfDay);
			}
			String minut_s = String.valueOf(minute);
			int length1 = minut_s.length();
			if (length1 == 1) {
				minut_s = "0" + String.valueOf(minute);
			}
			System.out.println("............." + hour_s);
			System.out.println("............." + minut_s);
			System.out.println("............." + length);
			System.out.println("............." + length1);
			System.out.println("............." + minute);
			System.out.println("............." + hourOfDay);

			time = hour_s + ":" + minut_s;
			if (dayselect == 1) {
				days = "MONDAY" + " " + time + "-";
				daysadd = "";
			} else {
				daymon = "MONDAY" + " " + time + "-";
				// daysadd=days+daysadd+"Monday"+time+ "-";
				days = "";
			}

		}

	};

	CustomTimePickerDialog.OnTimeSetListener mTimesetlistenertue = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			timechk2 = true;
			timechk1 = false;
			timechk3 = false;
			timechk4 = false;
			timechk5 = false;
			timechk6 = false;
			timechk7 = false;
			String hour_s = String.valueOf(hourOfDay);
			int length = hour_s.length();
			if (length == 1) {
				hour_s = "0" + String.valueOf(hourOfDay);
			}
			String minut_s = String.valueOf(minute);
			int length1 = minut_s.length();
			if (length1 == 1) {
				minut_s = "0" + String.valueOf(minute);
			}

			time = hour_s + ":" + minut_s;

			if (dayselect == 2) {
				days = "TUESDAY" + " " + time + "-";
				daysadd = "";
			} else {
				daytue = "TUESDAY" + " " + time + "-";
				// daysadd=days+daysadd+"Tuesday"+time+ "-";
				days = "";
			}

		}
	};
	CustomTimePickerDialog.OnTimeSetListener mTimesetlistenerwed = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			timechk3 = true;
			timechk2 = false;
			timechk1 = false;
			timechk4 = false;
			timechk5 = false;
			timechk6 = false;
			timechk7 = false;
			String hour_s = String.valueOf(hourOfDay);
			int length = hour_s.length();
			if (length == 1) {
				hour_s = "0" + String.valueOf(hourOfDay);
			}
			String minut_s = String.valueOf(minute);
			int length1 = minut_s.length();
			if (length1 == 1) {
				minut_s = "0" + String.valueOf(minute);
			}

			time = hour_s + ":" + minut_s;
			if (dayselect == 3) {
				days = "WEDNESDAY" + " " + time + "-";
				daysadd = "";
			} else {
				daywed = "WEDNESDAY" + " " + time + "-";
				// daysadd=days+daysadd+"Wednesday"+time+ "-";
				days = "";
			}

		}
	};
	CustomTimePickerDialog.OnTimeSetListener mTimesetlistenerthu = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			timechk4 = true;
			timechk2 = false;
			timechk3 = false;
			timechk1 = false;
			timechk5 = false;
			timechk6 = false;
			timechk7 = false;
			String hour_s = String.valueOf(hourOfDay);
			int length = hour_s.length();
			if (length == 1) {
				hour_s = "0" + String.valueOf(hourOfDay);
			}
			String minut_s = String.valueOf(minute);
			int length1 = minut_s.length();
			if (length1 == 1) {
				minut_s = "0" + String.valueOf(minute);
			}

			time = hour_s + ":" + minut_s;

			if (dayselect == 4) {
				days = "THURSDAY" + " " + time + "-";
				daysadd = "";
			} else {
				daythu = "THURSDAY" + " " + time + "-";
				// daysadd=days+daysadd+"Thursday"+time+ "-";
				days = "";
			}

		}
	};
	CustomTimePickerDialog.OnTimeSetListener mTimesetlistenerfri = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			timechk5 = true;
			timechk2 = false;
			timechk3 = false;
			timechk4 = false;
			timechk1 = false;
			timechk6 = false;
			timechk7 = false;
			String hour_s = String.valueOf(hourOfDay);
			int length = hour_s.length();
			if (length == 1) {
				hour_s = "0" + String.valueOf(hourOfDay);
			}
			String minut_s = String.valueOf(minute);
			int length1 = minut_s.length();
			if (length1 == 1) {
				minut_s = "0" + String.valueOf(minute);
			}

			time = hour_s + ":" + minut_s;

			if (dayselect == 5) {
				days = "FRIDAY" + " " + time + "-";
				daysadd = "";
			} else {
				dayfri = "FRIDAY" + " " + time + "-";
				// daysadd=days+daysadd+"Friday"+time+ "-";
				days = "";
			}

		}
	};
	CustomTimePickerDialog.OnTimeSetListener mTimesetlistenersat = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			System.out.println("to .....saaaat");
			// TODO Auto-generated method stub
			timechk6 = true;
			timechk2 = false;
			timechk3 = false;
			timechk4 = false;
			timechk5 = false;
			timechk1 = false;
			timechk7 = false;
			String hour_s = String.valueOf(hourOfDay);
			int length = hour_s.length();
			if (length == 1) {
				hour_s = "0" + String.valueOf(hourOfDay);
			}
			String minut_s = String.valueOf(minute);
			int length1 = minut_s.length();
			if (length1 == 1) {
				minut_s = "0" + String.valueOf(minute);
			}

			time = hour_s + ":" + minut_s;
			if (dayselect == 6) {
				days = "SATURDAY" + " " + time + "-";
				daysadd = "";
			} else {
				daysat = "SATURDAY" + " " + time + "-";
				// daysadd=days+daysadd+"Saturday"+time+ "-";
				days = "";
			}

		}
	};
	CustomTimePickerDialog.OnTimeSetListener mTimesetlistenersun = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			System.out.println("to ....");
			timechk7 = true;
			timechk2 = false;
			timechk3 = false;
			timechk4 = false;
			timechk5 = false;
			timechk6 = false;
			timechk1 = false;
			String hour_s = String.valueOf(hourOfDay);
			int length = hour_s.length();
			if (length == 1) {
				hour_s = "0" + String.valueOf(hourOfDay);
			}
			String minut_s = String.valueOf(minute);
			int length1 = minut_s.length();
			if (length1 == 1) {
				minut_s = "0" + String.valueOf(minute);
			}

			time = hour_s + ":" + minut_s;

			if (dayselect == 7) {
				days = "SUNDAY" + " " + time + "-";
				daysadd = "";
			} else {
				daysun = "SUNDAY" + " " + time + "-";
				// daysadd=days+daysadd+"Saturday"+time+ "-";
				days = "";
			}

		}
	};
	CustomTimePickerDialog.OnTimeSetListener mTimesetlistenermonto = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			if (clicked == true) {
				clicked = false;
				String hour_s = String.valueOf(hourOfDay);
				int length = hour_s.length();
				if (length == 1) {
					hour_s = "0" + String.valueOf(hourOfDay);
				}
				String minut_s = String.valueOf(minute);
				int length1 = minut_s.length();
				if (length1 == 1) {
					minut_s = "0" + String.valueOf(minute);
				}

				time = hour_s + ":" + minut_s;

				if (dayselect == 1) {

					if (timechk1 == true) {
						days = days + time + " ";
						setfixedfree_time.setText(days);
					} else {
						alert();
					}

				} else {
					if (timechk1 == true) {
						daysadd = daysadd + daymon + time + "  ";
						setfixedfree_time.setText(daysadd);
						daymon = "";
					} else {
						alert();
					}
					dayselect = 0;
				}
			}
		}
	};
*/
	public void alert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("Invalid entry");
		builder.setMessage("Please select from time ").setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// edit_email.setText("");

						dialog.cancel();

					}

				});

		AlertDialog alert = builder.create();
		alert.show();
	}

	/*CustomTimePickerDialog.OnTimeSetListener mTimesetlistenertueto = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if (clicked == true) {
				clicked = false;

				System.out.println("to .....");

				String hour_s = String.valueOf(hourOfDay);
				int length = hour_s.length();
				if (length == 1) {
					hour_s = "0" + String.valueOf(hourOfDay);
				}
				String minut_s = String.valueOf(minute);
				int length1 = minut_s.length();
				if (length1 == 1) {
					minut_s = "0" + String.valueOf(minute);
				}

				time = hour_s + ":" + minut_s;
				if (dayselect == 2) {
					if (timechk2 == true) {
						days = days + time + " ";
						setfixedfree_time.setText(days);
					} else {
						alert();
					}
				} else {
					if (timechk2 == true) {
						daysadd = daysadd + daytue + time + "  ";
						setfixedfree_time.setText(daysadd);
						daytue = "";
					} else {
						alert();
					}
					dayselect = 0;
				}
			}
		}
	};
	CustomTimePickerDialog.OnTimeSetListener mTimesetlistenerwedto = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			if (clicked == true) {
				clicked = false;
				System.out.println("to .....");

				String hour_s = String.valueOf(hourOfDay);
				int length = hour_s.length();
				if (length == 1) {
					hour_s = "0" + String.valueOf(hourOfDay);
				}
				String minut_s = String.valueOf(minute);
				int length1 = minut_s.length();
				if (length1 == 1) {
					minut_s = "0" + String.valueOf(minute);
				}

				time = hour_s + ":" + minut_s;
				if (dayselect == 3) {
					if (timechk3 == true) {
						days = days + time + " ";
						setfixedfree_time.setText(days);
					} else {
						alert();
					}
				} else {
					if (timechk3 == true) {
						daysadd = daysadd + daywed + time + "  ";
						setfixedfree_time.setText(daysadd);
						daywed = "";
					} else {
						alert();
					}
					dayselect = 0;
				}
			}
		}
	};
	CustomTimePickerDialog.OnTimeSetListener mTimesetlistenerthuto = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			if (clicked == true) {
				clicked = false;
				String hour_s = String.valueOf(hourOfDay);
				int length = hour_s.length();
				if (length == 1) {
					hour_s = "0" + String.valueOf(hourOfDay);
				}
				String minut_s = String.valueOf(minute);
				int length1 = minut_s.length();
				if (length1 == 1) {
					minut_s = "0" + String.valueOf(minute);
				}

				time = hour_s + ":" + minut_s;
				if (dayselect == 4) {
					if (timechk4 == true) {
						days = days + time + " ";
						setfixedfree_time.setText(days);
					} else {
						alert();
					}
				} else {
					if (timechk4 == true) {
						daysadd = daysadd + daythu + time + "  ";
						setfixedfree_time.setText(daysadd);
						daythu = "";
					} else {
						alert();
					}
					dayselect = 0;
				}

			}
		}
	};
	CustomTimePickerDialog.OnTimeSetListener mTimesetlistenerfrito = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			if (clicked == true) {
				clicked = false;
				String hour_s = String.valueOf(hourOfDay);
				int length = hour_s.length();
				if (length == 1) {
					hour_s = "0" + String.valueOf(hourOfDay);
				}
				String minut_s = String.valueOf(minute);
				int length1 = minut_s.length();
				if (length1 == 1) {
					minut_s = "0" + String.valueOf(minute);
				}

				time = hour_s + ":" + minut_s;
				if (dayselect == 5) {
					if (timechk5 == true) {
						days = days + time + " ";
						setfixedfree_time.setText(days);
					} else {
						alert();
					}
				} else {
					if (timechk5 == true) {
						daysadd = daysadd + dayfri + time + "  ";
						setfixedfree_time.setText(daysadd);
						dayfri = "";
					} else {
						alert();
					}
					dayselect = 0;
				}
			}
		}
	};
	CustomTimePickerDialog.OnTimeSetListener mTimesetlistenersatto = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			System.out.println("to .....sat");

			if (clicked == true) {
				clicked = false;
				String hour_s = String.valueOf(hourOfDay);
				int length = hour_s.length();
				if (length == 1) {
					hour_s = "0" + String.valueOf(hourOfDay);
				}
				String minut_s = String.valueOf(minute);
				int length1 = minut_s.length();
				if (length1 == 1) {
					minut_s = "0" + String.valueOf(minute);
				}

				time = hour_s + ":" + minut_s;
				if (dayselect == 6) {
					if (timechk6 == true) {
						days = days + time + " ";
						setfixedfree_time.setText(days);
					} else {
						alert();
					}
				} else {
					if (timechk6 == true) {
						daysadd = daysadd + daysat + time + "  ";
						setfixedfree_time.setText(daysadd);
						daysat = "";
					} else {
						alert();
					}
					dayselect = 0;
				}

			}
		}
	};
	CustomTimePickerDialog.OnTimeSetListener mTimesetlistenersunto = new CustomTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			if (clicked == true) {
				clicked = false;
				System.out.println("to .....sun");
				
				 * timechk7=false;
				 * timechk2=false;timechk3=false;timechk4=false;timechk5
				 * =false;timechk6=false;timechk1=false;
				 
				String hour_s = String.valueOf(hourOfDay);
				int length = hour_s.length();
				if (length == 1) {
					hour_s = "0" + String.valueOf(hourOfDay);
				}
				String minut_s = String.valueOf(minute);
				int length1 = minut_s.length();
				if (length1 == 1) {
					minut_s = "0" + String.valueOf(minute);
				}

				time = hour_s + ":" + minut_s;
				if (dayselect == 7) {
					if (timechk7 == true) {
						days = days + time;
						setfixedfree_time.setText(days);
					} else {
						alert();
					}
				} else {
					if (timechk7 == true) {
						daysadd = daysadd + daysun + time + " ";
						setfixedfree_time.setText(daysadd);
						daysun = "";
					} else {
						alert();
					}
					dayselect = 0;
				}
			}

		}
	};*/

	private void setsizeofimage() {
		// TODO Auto-generated method stub
		child_image.requestLayout();
Display display = getActivity().getWindowManager().getDefaultDisplay();
		
		int height = display.getHeight();
		int width = display.getWidth();
		
		System.out.println(height  +  "   "+width);
		
		float mReqPercentages = 35;
		float mCalculatedWidth = (mReqPercentages / 100) * width;

	//	float mReqPercentages1 = 40;
	//	float mCalculatedWidth1 = (mReqPercentages1 / 100) * width;

		
		child_image.getLayoutParams().height = (int) (mCalculatedWidth);
		child_image.getLayoutParams().width = (int) mCalculatedWidth;
	//	list_parent.getLayoutParams().height = (int)mCalculatedWidth;
	//	list_parent.getLayoutParams().width = (int)mCalculatedWidth1;
		/*int density = getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_LOW:
			child_image.getLayoutParams().height = 100;
			child_image.getLayoutParams().width = 100;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			child_image.getLayoutParams().height = 180;
			child_image.getLayoutParams().width = 180;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			child_image.getLayoutParams().height = 180;
			child_image.getLayoutParams().width = 180;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			child_image.getLayoutParams().height = 250;
			child_image.getLayoutParams().width = 250;
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			child_image.getLayoutParams().height = 250;
			child_image.getLayoutParams().width = 250;
			break;
		}*/
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		System.out.println("resultCode  +  " + resultCode);
		System.out.println("data   +  " + data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == -1
				&& data != null) {

			/*
			 * InputStream is = null; try { is =
			 * getActivity().getContentResolver().openInputStream(
			 * data.getData()); } catch (FileNotFoundException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 */

			// imageData = BitmapFactory.decodeStream(is, null, null);
			try {

				Uri selectedImageURI = data.getData();
				File imageFile = new File(getRealPathFromURI(selectedImageURI));
				String path_get = imageFile.getAbsolutePath();
				try {

					ExifInterface exif = new ExifInterface(path_get);
					int orientation = exif.getAttributeInt(
							ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_NORMAL);

					int angle = 0;

					if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
						angle = 90;
					} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
						angle = 180;
					} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
						angle = 270;
					}

					Matrix mat = new Matrix();
					mat.postRotate(angle);

					Bitmap bmp = BitmapFactory.decodeStream(
							new FileInputStream(imageFile), null, null);
					Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0,
							bmp.getWidth(), bmp.getHeight(), mat, true);
					imageData = correctBmp;
				}

				catch (IOException e) {
					Log.w("TAG", "-- Error in setting image");
				} catch (OutOfMemoryError oom) {
					Log.w("TAG", "-- OOM Error in setting image");
				}
				try {

					Bitmap dstBmp;
					if (imageData.getWidth() >= imageData.getHeight()) {

						dstBmp = Bitmap.createBitmap(
								imageData,
								imageData.getWidth() / 2
										- imageData.getHeight() / 2, 0,
								imageData.getHeight(), imageData.getHeight());

					} else {

						dstBmp = Bitmap.createBitmap(
								imageData,
								0,
								imageData.getHeight() / 2
										- imageData.getWidth() / 2,
								imageData.getWidth(), imageData.getWidth());
					}
					imageData = dstBmp;
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					// is.close();

					child_image.setImageBitmap(imageData);
					setsizeofimage();
					cd = new ConnectionDetector(getActivity());
					isInternetPresent = cd.isConnectingToInternet();
					if (isInternetPresent) {
						new child_pic_update().execute();
					} else {
						Toast.makeText(getActivity(),
								"Please check internet connection", 2000)
								.show();

					}

				} catch (Exception e) {

					e.printStackTrace();

				}
			} catch (Exception e) {
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

					child_image.setImageBitmap(imageData);
					
					setsizeofimage();
					
					if (isInternetPresent) {
						new child_pic_update().execute();

					} else {
						Toast.makeText(getActivity(),
								"Please check internet connection", 2000).show();

					}

				} catch (Exception e2) {

					e.printStackTrace();
				}
			}
		}
	}

	private String getRealPathFromURI(Uri contentURI) {
		String result;
		Cursor cursor = getActivity().getContentResolver().query(contentURI,
				null, null, null, null);
		if (cursor == null) { // Source is Dropbox or other similar local file
								// path
			result = contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		return result;
	}

	public class child_pic_update extends AsyncTask<String, Integer, String> {
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
			url = Constants.CHILD_PIC_UPDATE;//"http://54.191.67.152/playdate/childprofile_pic_edit.php";// ?childid=4&profile_image=1.jpg";//"http://54.191.67.152/playdate/guardian_edit.php";//?profile_pic=pic&name=deepak&dob=1989/1/12&set_fixed_freetime=1989/2/4&school=DPS&conditions=TRUE&allergies=test&hobbies=demo&siblings=mother&youth_club=abc&g_id=10"
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
			nameValuePairs.add(new BasicNameValuePair("g_id", guardian_id));
			nameValuePairs.add(new BasicNameValuePair("childid", child_ID));
			System.out.println(child_ID);
			nameValuePairs.add(new BasicNameValuePair("img", image_str));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));

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
				System.out
						.println("urlreturnurlretururlreturned" + urlreturned);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String resultt) {

			dialog.dismiss();
			if (data.equalsIgnoreCase("1")) {
				Toast.makeText(getActivity(), "Update successful", 1000).show();

				child_profile_pic = urlreturned;

			} else {
				Toast.makeText(getActivity(),
						"Update not successful, please try again", 1000)
						.show();
			}
		}
	}

	public class chilprofileupdate_webservice extends
			AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading.......please wait");
			dialog.setCancelable(false);
			dialog.show();
			url = Constants.CHILD_UPDATE;//"http://54.191.67.152/playdate/childinfo_edit.php";
			// http://54.191.67.152/playdate/childinfo_edit.php?childid=4&name=pooja&dob=1989-12-1&set_fixed_freetime=tue&school=aa&conditions=false&hobbies=bb&allergies=false
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("name", name_update));
			nameValuePairs.add(new BasicNameValuePair("dob",
					date_of_birth_child));
			nameValuePairs.add(new BasicNameValuePair("set_fixed_freetime",
					""));
			nameValuePairs.add(new BasicNameValuePair("school", ""));
			nameValuePairs.add(new BasicNameValuePair("conditions", ""));
			nameValuePairs.add(new BasicNameValuePair("allergies",
					allergies_update));
			nameValuePairs
					.add(new BasicNameValuePair("hobbies", ""));
			nameValuePairs.add(new BasicNameValuePair("youth_club",
					""));
			nameValuePairs.add(new BasicNameValuePair("childid", child_ID));
			Log.d("nameValuePairs", "nameValuePairs" + nameValuePairs);
			Log.d("nameValuePairs", "nameValuePairs" + nameValuePairs);
			Log.d("nameValuePairs", "nameValuePairs" + nameValuePairs);

			StringBuilder sbb = new StringBuilder();

			sbb.append("http://54.191.67.152/playdate/child.php?");
			sbb.append(nameValuePairs.get(0) + "&");
			sbb.append(nameValuePairs.get(1) + "&");
			sbb.append(nameValuePairs.get(2) + "&");
			sbb.append(nameValuePairs.get(3) + "&");
			sbb.append(nameValuePairs.get(4) + "&");
			sbb.append(nameValuePairs.get(5) + "&");
			sbb.append(nameValuePairs.get(6) + "&");
			sbb.append(nameValuePairs.get(7) + "&");
			sbb.append(nameValuePairs.get(8));

			System.out.println("string builder...\n");

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));

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

			try {
				String sResponse = reader.readLine();

				System.out.println("response" + sResponse);
				JSONObject json;
				try {
					json = new JSONObject(sResponse);
					data = json.getString("success");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String resultt) {

			dialog.dismiss();

			if (data.equals("1")) {
				Toast.makeText(getActivity(), "Update Successful", 2000)
						.show();

				//Bundle bundle = new Bundle();
				//bundle.putString("user_guardian_id", GlobalVariable.guardian_Id);
				android.support.v4.app.Fragment fragment = new Home_fragment();
			//	fragment.setArguments(bundle);
				android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();

			}

		}
	}

	public class LazyAdapter extends BaseAdapter {
		String _imgurl = "";
		private Activity activity;
		private ArrayList<Getcatagory_for_sets> _items;
		private LayoutInflater inflater = null;

		// public ImageLoader imageLoader;

		public ImageLoader imageLoader;

		// int count = 10;

		public LazyAdapter(Activity activity,
				ArrayList<Getcatagory_for_sets> parentItems) {

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

		class ViewHolder {
			public TextView event_title, event_date;
			public ImageView _image = null;

		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder _holder;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.child_parent_list, null);
				_holder = new ViewHolder();

				_holder.event_title = (TextView) convertView
						.findViewById(R.id.textView1);
				_holder._image = (ImageView) convertView
						.findViewById(R.id.imageView1);
				convertView.setTag(_holder);
			} else {
				_holder = (ViewHolder) convertView.getTag();
			}
			String name_of_child = _items.get(position).parent_name;
			name_of_child = name_of_child.toUpperCase();
			_holder.event_title.setText(name_of_child);
			_imgurl = _items.get(position).parent_profilepic;

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
			
			/*int density = getResources().getDisplayMetrics().densityDpi;
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
			}*/

			return convertView;
		}

	}

	public class parent_profile_show extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		String parentinfo;
		LazyAdapter adapter;

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading.......please wait");
			dialog.setCancelable(false);
			dialog.show();
			parentinfo = Constants.CHILD_PARENTS;//"http://54.191.67.152/playdate/child_parent_auth.php";// ?child_id=287";
		}

		@SuppressWarnings("unused")
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			parentItems = new ArrayList<Getcatagory_for_sets>();
			Getcatagory_for_sets getcategory = null;

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(parentinfo);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("child_id", child_ID));

			System.out.println("child_id......" + child_ID);
			StringBuilder sbb = new StringBuilder();

			sbb.append("http://54.191.67.152/playdate/child_parent_auth.php?");
			sbb.append(nameValuePairs.get(0));
			System.out.println(sbb);
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));

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
			parent_id = new ArrayList<String>();
			try {

				if (sResponse.equals(null) || sResponse.equals("")) {

				} else {

					try {
						JSONObject json = new JSONObject(sResponse);
						JSONObject jobj_parent = json.getJSONObject("parent");
						getcategory = new Getcatagory_for_sets();
						getcategory.parent_name = jobj_parent
								.getString("Parentname");
						getcategory.parent_id = jobj_parent.getString("g_id");
						child_parent_id = jobj_parent.getString("g_id");
						getcategory.parent_profilepic = jobj_parent
								.getString("profile_image");
						getcategory.parent_location = jobj_parent
								.getString("location");
					//	getcategory.parent_freetime = jobj_parent
					//			.getString("set_fixed_freetime");
						getcategory.parent_phone = jobj_parent
								.getString("phone");
						getcategory.parent_date_ofbirth = jobj_parent
								.getString("dob");
						getcategory.parent_type = jobj_parent
								.getString("guardian_type");
						parent_id.add(child_parent_id);
						parentItems.add(getcategory);
						try {
							JSONArray jarray = json.getJSONArray("auth_parent");

							for (int i = 0; i < jarray.length(); i++) {
								JSONObject c = jarray.getJSONObject(i);
								getcategory = new Getcatagory_for_sets();
								getcategory.parent_name = c
										.getString("Parentname");
								getcategory.parent_id = c
										.getString("auth_g_id");
								getcategory.parent_profilepic = c
										.getString("profile_image");
								getcategory.parent_location = c
										.getString("location");
							//	getcategory.parent_freetime = c
							//			.getString("set_fixed_freetime");
								getcategory.parent_phone = c.getString("phone");
								getcategory.parent_date_ofbirth = c
										.getString("dob");
								getcategory.parent_type = c
										.getString("guardian_type");
								parent_id.add(c.getString("auth_g_id"));
								parentItems.add(getcategory);

							}
						} catch (Exception e) {
							// TODO: handle exception
						}

						String success = json.getString("success");
						String msg = json.getString("msg");

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		protected void onPostExecute(String resultt) {

			/* if(child_parent_id.equals(GlobalVariable.guardian_Id)) */
			if (parent_id.contains(GlobalVariable.guardian_Id)) {

			} else {
				dob.setEnabled(false);
			//	setfixedfree_time.setEnabled(false);
				// conditions.setEnabled(false);
				allergies.setEnabled(false);
		//		hobbies.setEnabled(false);
		//		school.setEnabled(false);
			//	youthclub.setEnabled(false);
				name.setEnabled(false);
				dob.setFocusable(false);
				dob.setClickable(false);
		//		setfixedfree_time.setFocusable(false);
		//		setfixedfree_time.setClickable(false);
				allergies.setFocusable(false);
				allergies.setClickable(false);
		//		hobbies.setFocusable(false);
		//		hobbies.setClickable(false);
				child_image.setClickable(false);
				//addmore_freetime.setClickable(false);
				update_info.setVisibility(View.GONE);
			}
			adapter = new LazyAdapter(getActivity(), parentItems);

			list_parent.setAdapter(adapter);

			dialog.dismiss();

		}
	}

	public static class CustomTimePickerDialog extends TimePickerDialog {

		public static final int TIME_PICKER_INTERVAL = 15;
		private boolean mIgnoreEvent = false;

		public CustomTimePickerDialog(Context context,
				OnTimeSetListener callBack, int hourOfDay, int minute,
				boolean is24HourView) {
			super(context, callBack, hourOfDay, minute, is24HourView);
		}

		@Override
		public void onTimeChanged(TimePicker timePicker, int hourOfDay,
				int minute) {
			super.onTimeChanged(timePicker, hourOfDay, minute);
			if (!mIgnoreEvent) {
				minute = getRoundedMinute(minute);
				mIgnoreEvent = true;
				timePicker.setCurrentMinute(minute);
				mIgnoreEvent = false;
			}
		}

		public static int getRoundedMinute(int minute) {
			if (minute % TIME_PICKER_INTERVAL != 0) {
				int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
				minute = minuteFloor
						+ (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
				if (minute == 60)
					minute = 0;
			}

			return minute;
		}
	}

	public class unlink_child extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		String msg;
		String url_unlink_child = Constants.UNLINK_CHILD;//"http://54.191.67.152/playdate/all_assign_child_remove.php";// ?assign_gid=&child_id=

		@Override
		protected void onPreExecute() {
			try {
				dialog.setMessage("Loading.......please wait");
				dialog.setCancelable(false);
				dialog.show();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		@Override
		protected String doInBackground(String... arg0) {

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url_unlink_child);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("child_id", child_ID));
			nameValuePairs.add(new BasicNameValuePair("assign_gid",
					GlobalVariable.guardian_Id));

			System.out.println("child_id......" + child_ID);
			System.out.println("assign_gid......" + GlobalVariable.guardian_Id);
			StringBuilder sbb = new StringBuilder();

			sbb.append("http://54.191.67.152/playdate/all_assign_child_remove.php?");
			sbb.append(nameValuePairs.get(0) + "&");
			sbb.append(nameValuePairs.get(1));
			System.out.println(sbb);
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));

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

				System.out
						.println("response.............Nitesh....................."
								+ sResponse);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				JSONObject jobj=new JSONObject(sResponse);
				 msg=jobj.getString("msg");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String resultt) {
			dialog.dismiss();
			Toast.makeText(getActivity(), msg,2000).show();
			//Bundle bundle = new Bundle();
			//bundle.putString("user_guardian_id", GlobalVariable.guardian_Id);
			android.support.v4.app.Fragment fragment = new Home_fragment();
			//fragment.setArguments(bundle);
			android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();
		}

	}
}
