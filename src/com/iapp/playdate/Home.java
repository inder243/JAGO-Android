package com.iapp.playdate;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

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
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.iapp.playdate.R;
import com.iapp.playdate.NavigationDrawerclass.SizeCallback;
import com.iapptechnologies.time.util.Constants;
import com.iapptechnologies.time.util.GoogleAnalaticsApp;
import com.iapptechnologies.time.util.ImageLoader;
import com.iapptechnologies.time.util.GoogleAnalaticsApp.TrackerName;
import com.playdate.contactlist.CustomListIndex;
import com.playdate.contactlist.IndexTableActivity;

public class Home extends FragmentActivity implements View.OnTouchListener {
	
	
	static int count;
	Button btnBack;
	SharedPreferences.Editor editor;
	static NavigationDrawerclass scrollView;
	static View menu;
	View app;
	Button btnSlide, btn_arrangedate, btn_calander, btn_home;
	static boolean menuOut = false;
	boolean isScan = false;
	Handler handler = new Handler();
	int btnWidth;
	CustomDrawerAdapter adapter;
	List<DrawerItem> dataList;
	static TextView txt_name;
	View.OnTouchListener gestureListener;
	float x1, x2;
	float y1, y2;
	static ImageView image_profile;
	private static  com.iapptechnologies.time.util.ImageLoader imgLoader;
	String firstname, dob, location, freetime;
	static String url;
	String guardiantype;
	String name;
	String lastname, facebook_friends, url_picupdate, urlpic_update_child;
	String facebook_id, user_guardian_id, phone_number;
	 SharedPreferences settings;
	boolean booleancheck = false;
	static boolean chk = false;                       
	private boolean imgCapFlag = false;
	protected boolean taken;
	public static final String PHOTO_TAKEN = "photo_taken";
	protected String path;
	Bitmap bitmap;
	ImageView img;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	String child_dob, child_name, child_ID, child_profile_pic, child_hobbies,
			child_allergies, child_conditions, child_school, child_youthclub,
			free_time_child, url_child_image;
	JSONArray customMessageStr;
	String view_data;
	ListView listView;
	int height,width;
	static float mCalculatedWidth;
	public Home() {

	}
	android.support.v4.app.Fragment fragment;
	
	
	
	 
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Display display = getWindowManager().getDefaultDisplay();
		
		 height = display.getHeight();
		 width = display.getWidth();
		 float mReqPercentages = 35;
			 mCalculatedWidth = (mReqPercentages / 100) * width;
		dataList = new ArrayList<DrawerItem>();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		settings = getSharedPreferences(Constants.PREFS_NAME, 0);
		editor = settings.edit();
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (NavigationDrawerclass) inflater.inflate(R.layout.screen_scroll_with_list_menu, null);
		setContentView(scrollView);
		Crittercism.initialize(getApplicationContext(), "548035400729df790700000d");
		cd = new ConnectionDetector(this);
		isInternetPresent = cd.isConnectingToInternet();
		
		
		
		com.google.android.gms.analytics.Tracker t = ((GoogleAnalaticsApp) getApplication()).getTracker(TrackerName.APP_TRACKER);
		
        t.setScreenName("Home");

        t.send(new HitBuilders.AppViewBuilder().build());


		
		
		name=settings.getString("NAME","");
		url=settings.getString("URLPROFILE","");
		user_guardian_id=settings.getString("USERID","");
		firstname=settings.getString("FIRSTNAME","");
		facebook_id=settings.getString("FACEBOOKID","");
		dob=settings.getString("DOB","");
		guardiantype=settings.getString("GURTYPE","");
		location=settings.getString("LOCATION","");
		facebook_friends=settings.getString("FBFRIEND","");
		phone_number=settings.getString("PHONE","");
		
		
		
		
		
		
		if (GlobalVariable.parent_picute_update == 1) {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
			path = Environment.getExternalStorageDirectory() + "/IMG"+ timeStamp + ".jpg";
			startCameraActivity();
		} else if (GlobalVariable.parent_picute_update == 2) {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
			Intent intent = getIntent();
			path = Environment.getExternalStorageDirectory() + "/IMG"+ timeStamp + ".jpg";
			child_dob = intent.getExtras().getString("child_dob");
			child_name = intent.getExtras().getString("child_name");
			child_ID = intent.getExtras().getString("child_ID");
			child_profile_pic = intent.getExtras().getString("child_profile_pic");
			child_allergies = intent.getExtras().getString("child_allergies");
			startCameraActivity();
		}

		else if (GlobalVariable.parent_picute_update == 3) {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
			path = Environment.getExternalStorageDirectory() + "/IMG"+ timeStamp + ".jpg";
			startCameraActivity();
			
		} else {
			
		}
		//settings = this.getSharedPreferences("MyPreferencesFiles1", 0);
		booleancheck = settings.getBoolean("checkk", chk);
		final Stack stack = new Stack();
		menu = inflater.inflate(R.layout.sliderview_home, null);
		menu.setVisibility(View.GONE);
		image_profile = (ImageView) menu.findViewById(R.id.imageView1_parent_drawer);
	    txt_name = (TextView) menu.findViewById(R.id.textView1_profilename);
		String name_global=GlobalVariable.global_name;
		txt_name.setText(name_global);
		imgLoader = new ImageLoader(this);
		
		imgLoader.DisplayImage(url,image_profile);
		image_profile.requestLayout();
		
		
		
		
		

		
		image_profile.getLayoutParams().height = (int) (mCalculatedWidth);
		image_profile.getLayoutParams().width = (int) mCalculatedWidth;
		
		app = inflater.inflate(R.layout.homeview, null);
		image_profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GlobalVariable.custom_Jsonarray=null;
				count++;
			
				fragment=new  Parent_profile();
		//		fragment.setArguments(bundle);
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.addToBackStack("first12");
				fragmentTransaction.commit();
				Context context = menu.getContext();
				int menuWidth = menu.getMeasuredWidth();
				menu.setVisibility(View.GONE);
				if (!menuOut) {
					int left = 20;
				} else {
					int left = menuWidth;
					scrollView.smoothScrollTo(left, 0);
				}
				menuOut = !menuOut;
				
			}
		}); 
		app.setOnTouchListener(this);
		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.tabBar);
	//	dataList.add(new DrawerItem("Home", R.drawable.home_icon));
	//	dataList.add(new DrawerItem("Profile", R.drawable.profile_icon));
		dataList.add(new DrawerItem("Arrange", R.drawable.add_icon));
		dataList.add(new DrawerItem("Add Child", R.drawable.child_icon));
		dataList.add(new DrawerItem("Sets", R.drawable.sets_icon));
		dataList.add(new DrawerItem("Friends", R.drawable.friends_icon));
		dataList.add(new DrawerItem("Guardians", R.drawable.upgrade_icon));
		dataList.add(new DrawerItem("Notifications", R.drawable.notification_icon));
		
		
		
		
	//	dataList.add(new DrawerItem("Calender", R.drawable.calender_icon));
		dataList.add(new DrawerItem("Invite", R.drawable.invite_icon));
		
		dataList.add(new DrawerItem("feedback", R.drawable.feedback_icon));
		adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,dataList);
		listView = (ListView) menu.findViewById(R.id.list);
		listView.setDivider(null);
		listView.setDividerHeight(0);
		listView.setAdapter(adapter);
		btn_arrangedate = (Button) app.findViewById(R.id.button_arrange);
		btn_arrangedate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GlobalVariable.custom_Jsonarray=null;
			//	Bundle bundle = new Bundle();
			//	bundle.putString("user_guardian_id", user_guardian_id);
		//		bundle.putString("facebook_friends", facebook_friends);
                fragment = new Arrange_date_fragment();
		//		fragment.setArguments(bundle);
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.addToBackStack("first13");
				fragmentTransaction.commit();
				Context context = menu.getContext();
			}
		});
		btn_calander = (Button) app.findViewById(R.id.button_calander);
		btn_calander.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GlobalVariable.custom_Jsonarray=null;
			//	Bundle bundle = new Bundle();
				menu.setVisibility(View.GONE);
		//		bundle.putString("user_guardian_id", user_guardian_id);
                fragment = new Calander_event();
		//		fragment.setArguments(bundle);
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.addToBackStack("first14");
				fragmentTransaction.commit();
				Context context = menu.getContext();
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int Position, long arg3) {
				Fragment fragment = null;
				switch (Position) {//Arrange, Add Child, Sets, Friends, Guardians, Notification, Invite, Feedback
				case 0: {
					
					fragment = new Arrange_date_fragment();
					
					break;
				}
				case 1: {
					fragment = new Add_Child();
					
					break;
				}
				case 2: 
				{
					fragment = new Sets();
					
					break;
				}
				case 3: {
					Bundle bundle = new Bundle();
					bundle.putString("response_data", "");
					bundle.putString("check", "1");
					
					fragment= new friendlist();
					fragment.setArguments(bundle);
				

				}
					break;
				case 4: {
				 	fragment = new authentication();
				
					break;
				}
				case 5: {
					Bundle bundle = new Bundle();
					
						bundle.putString("view",view_data );
						fragment=new notification_fragement();
						fragment.setArguments(bundle);
						count++;
				

				}
					break;
				case 6:

				{
					Resources resources = getResources();
					Intent shareIntent = new Intent(Intent.ACTION_SEND);
					shareIntent.setType("text/plain");
					shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out Playdate by JAGO for the fastest and simplest way to schedule kids playdates  www.jago.nu");
					shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Fast Playdate Planning from JAGO.");
					PackageManager pm = getPackageManager();
					IntentChoserBuilder.createChoserIntent(Home.this, shareIntent, pm, "Check out Playdate by JAGO for the fastest and simplest way to schedule kids playdates  www.jago.nu", "Fast Playdate Planning from JAGO.");	
				
				}

					break;
				case 7: {
					fragment = new Send_mail();
			
					break;
				}
				

				default:
					break;
				}
				if (fragment != null) {
					android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);
					fragmentTransaction.addToBackStack("first1");
					fragmentTransaction.commit();
					Context context = menu.getContext();
					int menuWidth = menu.getMeasuredWidth();
					menu.setVisibility(View.GONE);
					if (!menuOut) {
						Log.d("===slide==", "Scroll to right");
						Log.d("===clicked==", "clicked");
						int left = 20;
					} else {
						Log.d("===slide==", "Scroll to left");
						Log.d("===clicked==", "clicked");
						int left = menuWidth;
						scrollView.smoothScrollTo(left, 0);
					}
					menuOut = !menuOut;
				} else {
				}
			}
		});

		btnSlide = (Button) tabBar.findViewById(R.id.button_back);
		btn_home = (Button) tabBar.findViewById(R.id.button_home);
		btn_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GlobalVariable.custom_Jsonarray=null;
			//	Bundle bundle = new Bundle();
			//	bundle.putString("user_guardian_id", user_guardian_id);
				fragment = new Home_fragment();
			//	fragment.setArguments(bundle);
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.addToBackStack("first15");
				fragmentTransaction.commit();
				Context context = menu.getContext();
			}
		});

		btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView,menu));
		final View[] children = new View[] { menu, app };
		int scrollToViewIdx = 1;
        menu.setVisibility(View.VISIBLE);
		scrollView.initViews(children, scrollToViewIdx,new SizeCallbackForMenu(btnSlide));
		 customMessageStr  = GlobalVariable.custom_Jsonarray;
		 Context context;
	
					 fragment = new Home_fragment();
						
								android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
								android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
								fragmentTransaction.replace(R.id.content_frame, fragment);
								fragmentTransaction.commit();
								 context = menu.getContext();
		
			 
	}

	protected void startCameraActivity() {
		File file = new File(path);
		Uri outputFileUri = Uri.fromFile(file);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(GlobalVariable.parent_picute_update == 0){
		Fragment fragment1 = getSupportFragmentManager().findFragmentById(R.id.content_frame);
	    fragment1.onActivityResult(requestCode, resultCode, data);
		}
		switch (resultCode) {
		case 0:
			Log.i("AndroidProgrammerGuru", "User cancelled");
			if (GlobalVariable.parent_picute_update == 1) {
				GlobalVariable.parent_picute_update = 0;
			//	final Bundle bundle = new Bundle();
			//	bundle.putString("name", name);
			//	bundle.putString("url", url);
			//	bundle.putString("freetime", freetime);
			//	bundle.putString("dob", dob);
			//	bundle.putString("guardiantype", guardiantype);
			//	bundle.putString("location", location);
			//	bundle.putString("firstname", firstname);
			//	bundle.putString("facebook_id", facebook_id);
			//	bundle.putString("user_guardian_id", user_guardian_id);
			//	bundle.putString("phone", phone_number);
new Thread(new Runnable() {
					
					@Override
					
					public void run() {
					
					runOnUiThread(new Runnable() {
				
					@Override
					
					public void run() {
				fragment = new Parent_profile();
		//		fragment.setArguments(bundle);
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.commit();
				Context context = menu.getContext();
				int menuWidth = menu.getMeasuredWidth();
				menu.setVisibility(View.GONE);
				if (!menuOut) {
					int left = 20;
				} else {
					int left = menuWidth;
					scrollView.smoothScrollTo(left, 0);
				}
				menuOut = !menuOut;
					 }
					});
					}
					
					}).start();
			}
			if (GlobalVariable.parent_picute_update == 2) {
				GlobalVariable.parent_picute_update = 0;
				final Bundle bundle = new Bundle();
				bundle.putString("child_name", child_name);
				bundle.putString("Child_id", child_ID);
				bundle.putString("Child_profilepic", child_profile_pic);
				bundle.putString("dob", child_dob);
				//bundle.putString("conditions", child_conditions);
				//bundle.putString("hobbies", child_hobbies);
				bundle.putString("Child_guardianId", user_guardian_id);
				bundle.putString("allergies", child_allergies);
				//bundle.putString("school", child_school);
				//bundle.putString("youthclub", child_youthclub);
				//bundle.putString("free_time", free_time_child);
		//		bundle.putString("parent_name", name);
		//		bundle.putString("parent_profilepic", url);
		//		bundle.putString("parent_freetime", freetime);
		//		bundle.putString("phone", phone_number);
		//		bundle.putString("parent_dob", dob);
		//		bundle.putString("parent_type", guardiantype);
		//		bundle.putString("parent_location", location);
		//		bundle.putString("parent_fbid", facebook_id);
		//		bundle.putString("facebook_friends", facebook_friends);
new Thread(new Runnable() {
					
					@Override
					
					public void run() {
					
					runOnUiThread(new Runnable() {
				
					@Override
					
					public void run() {
				fragment = new Child_profile();
				fragment.setArguments(bundle);
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.commit();
				Context context = menu.getContext();
				int menuWidth = menu.getMeasuredWidth();
				menu.setVisibility(View.GONE);
				if (!menuOut) {
					int left = 20;
				} else {
					int left = menuWidth;
					scrollView.smoothScrollTo(left, 0);
				}
				menuOut = !menuOut;
			            }
					});
					}
					
					}).start();
				 }
			if (GlobalVariable.parent_picute_update == 3) {
				GlobalVariable.parent_picute_update =0;
				/*try {
					new Handler().post(new Runnable() {
			            public void run() {
			            	android.support.v4.app.Fragment fragment = new Add_Child();
			    			android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
			    			fragmentManager.beginTransaction()
			    					.replace(R.id.content_frame, fragment).commit();
			            }
			});
				} catch (Exception e) {
					// TODO: handle exception
				}*/
				
				new Thread(new Runnable() {
					
					@Override
					
					public void run() {
					
					runOnUiThread(new Runnable() {
				
					@Override
					
					public void run() {
						fragment = new Add_Child();
						
						android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
						android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
						fragmentTransaction.replace(R.id.content_frame, fragment);
						fragmentTransaction.commit();
						Context context = menu.getContext();
						int menuWidth = menu.getMeasuredWidth();
						menu.setVisibility(View.GONE);
						if (!menuOut) {
							int left = 20;
						} else {
							int left = menuWidth;
							scrollView.smoothScrollTo(left, 0);
						}
						menuOut = !menuOut;
					
					}
					
					});
					}
					
					}).start();
				
				 
			}
			break;
		case -1:
			onPhotoTaken();
			if (GlobalVariable.parent_picute_update == 1) {
				if (isInternetPresent) {
					new parent_pic_update().execute();
				} else {
					Toast.makeText(Home.this,"Please check internet connection", 2000).show();
				}
			}
			if (GlobalVariable.parent_picute_update == 2) {

				if (isInternetPresent) 
				{
					new child_pic_update().execute();

				} else {
					Toast.makeText(Home.this,"Please check internet connection", 2000).show();
				}
			}
			if (GlobalVariable.parent_picute_update == 3) {

				if (isInternetPresent) 
				{
					new child_pic_update1().execute();

				} else {
					Toast.makeText(Home.this,"Please check internet connection", 2000).show();

				}
			}
			break;
		}
	}

	protected void onPhotoTaken() {
		taken = true;
		imgCapFlag = true;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		bitmap = BitmapFactory.decodeFile(path, options);
		 try {
		       
		        ExifInterface exif = new ExifInterface(path);
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
		        Bitmap correctBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);                 
		    bitmap=correctBmp;
		 }
		    catch (IOException e) {
		        Log.w("TAG", "-- Error in setting image");
		    }   
		    catch(OutOfMemoryError oom) {
		        Log.w("TAG", "-- OOM Error in setting image");
		    }
		 catch (Exception e) {
		}
		 try {
			 Bitmap dstBmp;
			 if (bitmap.getWidth() >= bitmap.getHeight()){

				  dstBmp = Bitmap.createBitmap(
						 bitmap, 
						 bitmap.getWidth()/2 - bitmap.getHeight()/2,
				     0,
				     bitmap.getHeight(), 
				     bitmap.getHeight()
				     );

				}else{

				  dstBmp = Bitmap.createBitmap(
						  bitmap,
				     0, 
				     bitmap.getHeight()/2 - bitmap.getWidth()/2,
				     bitmap.getWidth(),
				     bitmap.getWidth() 
				     );
				}
			 bitmap=dstBmp;
		} catch (Exception e) {
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(Home.PHOTO_TAKEN, taken);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.getBoolean(Home.PHOTO_TAKEN)) {
		}
	}

	static class ClickListenerForScrolling implements OnClickListener {
		NavigationDrawerclass scrollView;
		View menu;
		public ClickListenerForScrolling(NavigationDrawerclass scrollView,
				View menu) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
		}

		@Override
		public void onClick(View v) {

			if (GlobalVariable.url.equals("")) {

			} else {
				url = GlobalVariable.url;
				
			}
			String name_global=GlobalVariable.global_name;
			txt_name.setText(name_global);
			imgLoader.DisplayImage(url,image_profile);
			image_profile.requestLayout();
			
			
			
			
			

			
			image_profile.getLayoutParams().height = (int) (mCalculatedWidth);
			image_profile.getLayoutParams().width = (int) mCalculatedWidth;
			/*int density = v.getResources().getDisplayMetrics().densityDpi;
			switch (density) {
			case DisplayMetrics.DENSITY_LOW:
				image_profile.getLayoutParams().height = 70;
				image_profile.getLayoutParams().width = 70;
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				image_profile.getLayoutParams().height = 90;
				image_profile.getLayoutParams().width = 90;
				break;
			case DisplayMetrics.DENSITY_HIGH:
				image_profile.getLayoutParams().height = 120;
				image_profile.getLayoutParams().width = 120;
				break;
			case DisplayMetrics.DENSITY_XHIGH:
				image_profile.getLayoutParams().height = 170;
				image_profile.getLayoutParams().width = 170;
				break;
			case DisplayMetrics.DENSITY_XXHIGH:
				image_profile.getLayoutParams().height = 180;
				image_profile.getLayoutParams().width = 180;
				break;
			}*/

			clickeventimplement();
		}
	}

	static class SizeCallbackForMenu implements SizeCallback {
		int btnWidth;
		View btnSlide;

		public SizeCallbackForMenu(View btnSlide) {
			super();
			this.btnSlide = btnSlide;
		}

		@Override
		public void onGlobalLayout() {
			btnWidth = btnSlide.getMeasuredWidth();
			System.out.println("btnWidth=" + btnWidth);
		}

		@Override
		public void getViewSize(int idx, int w, int h, int[] dims) {
			dims[0] = w;
			dims[1] = h;
			final int menuIdx = 0;
			if (idx == menuIdx) {
				dims[0] = w - btnWidth;
			}
		}
	}

	private void scrollWebviw(NavigationDrawerclass scrollView, View menu) {
		Context context = menu.getContext();
		int menuWidth = menu.getMeasuredWidth();
		menu.setVisibility(View.VISIBLE);
		if (!menuOut) {
			// Scroll to 0 to reveal menu
			Log.d("===slide==", "Scroll to right");
			int left = 0;
			scrollView.smoothScrollTo(left, 0);
		} else {
			// Scroll to menuWidth so menu isn't on screen.
			Log.d("===slide==", "Scroll to left");
			int left = menuWidth;
			scrollView.smoothScrollTo(left, 0);

		}
		menuOut = false;
	}

	public static void clickeventimplement() {
		Context context = menu.getContext();

		int menuWidth = menu.getMeasuredWidth();

		// Ensure menu is visible
		menu.setVisibility(View.VISIBLE);

		if (!menuOut) {
			// Scroll to 0 to reveal menu
			Log.d("===slide==", "Scroll to right");
			Log.d("===clicked==", "clicked");
			int left = 20;
			scrollView.smoothScrollTo(left, 0);
		} else {
			// Scroll to menuWidth so menu isn't on screen.
			Log.d("===slide==", "Scroll to left");
			Log.d("===clicked==", "clicked");
			int left = menuWidth;
			scrollView.smoothScrollTo(left, 0);
		}
		menuOut = !menuOut;
	}

	public static void clickeventimplementfragment() {
		// TODO Auto-generated method stub
		Context context = menu.getContext();

		int menuWidth = menu.getMeasuredWidth();

		// Ensure menu is visible
		menu.setVisibility(View.GONE);

		// Scroll to menuWidth so menu isn't on screen.
		Log.d("===slide==", "Scroll to left");
		Log.d("===clicked==", "clicked");
		int left = menuWidth;
		scrollView.smoothScrollTo(left, 0);

		menuOut = !menuOut;
	}

	@Override
	protected void onResume() {
		super.onResume();
		com.facebook.AppEventsLogger.activateApp(this, "272047936334195");

	}

	@Override
	public boolean onTouch(View v, MotionEvent touchevent) {
		app = v;

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

			// if left to right sweep event on screen
			/*
			 * if (x1 < x2) { System.out.println("fffffffffffffffffffffffff");
			 * Toast.makeText(this, "Left to Right Swap Performed",
			 * Toast.LENGTH_LONG).show(); }
			 */
			// if right to left sweep event on screen
			if (x1 > x2) {
				Context context = menu.getContext();

				int menuWidth = menu.getMeasuredWidth();

				// Ensure menu is visible
				menu.setVisibility(View.GONE);

				if (!menuOut) {
					Log.d("===slide==", "Scroll to right");
					Log.d("===clicked==", "clicked");
					int left = 20;
				} else {
					// Scroll to menuWidth so menu isn't on screen.
					Log.d("===slide==", "Scroll to left");
					Log.d("===clicked==", "clicked");
					int left = menuWidth;
					scrollView.smoothScrollTo(left, 0);
				}
				menuOut = !menuOut;
			}

			break;
		}
		}
		return true;
	}

	@Override
	public void onBackPressed() {

		finish();
		super.onBackPressed();
	}

	public class parent_pic_update extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(Home.this);

		InputStream is;
		String result;
		JSONObject jArray = null;
		String data;
		private String urlreturned;

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading.......please wait");
			dialog.setCancelable(false);
			dialog.show();
			GlobalVariable.parent_picute_update = 0;
			url_picupdate = Constants.GUARDIAN_PIC_UPDATE;//"http://54.191.67.152/playdate/guardian_edit.php";// ?profile_pic=pic&name=deepak&dob=1989/1/12&set_fixed_freetime=1989/2/4&school=DPS&conditions=TRUE&allergies=test&hobbies=demo&siblings=mother&youth_club=abc&g_id=10"
		}

		@Override
		protected String doInBackground(String... arg0) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url_picupdate);
			String image_str = null;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
				byte[] byte_arr = stream.toByteArray();
				 image_str = com.iapp.playdate.Base64.encodeBytes(byte_arr);
				System.out.println("image compressed");
			} catch (Exception e) {
				// TODO: handle exception
			}
			

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("profile_image", "12.jpg"));
			nameValuePairs.add(new BasicNameValuePair("g_id", user_guardian_id));
			nameValuePairs.add(new BasicNameValuePair("img", image_str));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			HttpResponse response = null;
			try {
				response = httpClient.execute(httpPost, localContext);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String sResponse = null;

			try {
				sResponse = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				JSONObject json = new JSONObject(sResponse);
				data = json.getString("success");
				urlreturned = json.getString("url");

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String resultt) {

			url = urlreturned;
			GlobalVariable.url = urlreturned;
			dialog.dismiss();
			if (data.equalsIgnoreCase("1")) {
				
				editor.putString("URLPROFILE", urlreturned);
				editor.commit();
				Toast.makeText(Home.this, "Update Successful", 1000).show();
		//		Bundle bundle = new Bundle();
		//		bundle.putString("name", name);
		//		bundle.putString("url", url);
				//bundle.putString("freetime", freetime);
		//		bundle.putString("dob", dob);
		//		bundle.putString("guardiantype", guardiantype);
		//		bundle.putString("location", location);
		//		bundle.putString("firstname", firstname);
		//		bundle.putString("facebook_id", facebook_id);
		//		bundle.putString("user_guardian_id", user_guardian_id);
		//		bundle.putString("phone", phone_number);
				android.support.v4.app.Fragment fragment = new Parent_profile();
		//		fragment.setArguments(bundle);
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			} else {
				Toast.makeText(Home.this,"Update not successful, please try again", 1000).show();
		//		Bundle bundle = new Bundle();
		//		bundle.putString("name", name);
		//		bundle.putString("url", url);
			//	bundle.putString("freetime", freetime);
		//		bundle.putString("dob", dob);
		//		bundle.putString("guardiantype", guardiantype);
		//		bundle.putString("location", location);
		//		bundle.putString("firstname", firstname);
		//		bundle.putString("facebook_id", facebook_id);
		//		bundle.putString("user_guardian_id", user_guardian_id);
		//		bundle.putString("phone", phone_number);
				android.support.v4.app.Fragment fragment = new Parent_profile();
		//		fragment.setArguments(bundle);
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
			}
		}
	}

	public class child_pic_update extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(Home.this);
		String url_child;
		InputStream is;
		String result;
		JSONObject jArray = null;
		String data;

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading.......please wait");
			dialog.setCancelable(false);
			dialog.show();
			GlobalVariable.parent_picute_update = 0;
			url_child = Constants.CHILD_PIC_UPDATE;//"http://54.191.67.152/playdate/childprofile_pic_edit.php";// ?childid=4&profile_image=1.jpg";//"http://54.191.67.152/playdate/guardian_edit.php";//?profile_pic=pic&name=deepak&dob=1989/1/12&set_fixed_freetime=1989/2/4&school=DPS&conditions=TRUE&allergies=test&hobbies=demo&siblings=mother&youth_club=abc&g_id=10"
		}

		@Override
		protected String doInBackground(String... arg0) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url_child);
			String image_str=null;
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream); 
				byte[] byte_arr = stream.toByteArray();
			 image_str = com.iapp.playdate.Base64.encodeBytes(byte_arr);
			} catch (Exception e) {
			}
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("g_id", user_guardian_id));
			nameValuePairs.add(new BasicNameValuePair("childid", child_ID));
			nameValuePairs.add(new BasicNameValuePair("img", image_str));
			StringBuilder sbb = new StringBuilder();
			sbb.append("http://54.191.67.152/playdate/guardian_child.php?");
			sbb.append(nameValuePairs.get(2));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			HttpResponse response = null;
			try {
				response = httpClient.execute(httpPost, localContext);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String sResponse = null;

			try {
				sResponse = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				JSONObject json = new JSONObject(sResponse);
				data = json.getString("success");
				url_child_image = json.getString("url");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String resultt) {

			dialog.dismiss();
			if (data.equalsIgnoreCase("1")) {
				Toast.makeText(Home.this, "Update Successful", 1000).show();
				Bundle bundle = new Bundle();
				bundle.putString("child_name", child_name);
				bundle.putString("Child_id", child_ID);
				bundle.putString("Child_profilepic", url_child_image);
				bundle.putString("dob", child_dob);
				//bundle.putString("conditions", child_conditions);
				//bundle.putString("hobbies", child_hobbies);
				bundle.putString("Child_guardianId", user_guardian_id);
				bundle.putString("allergies", child_allergies);
				//bundle.putString("school", child_school);
				//bundle.putString("youthclub", child_youthclub);
				//bundle.putString("free_time", free_time_child);
		//		bundle.putString("parent_name", name);
		//		bundle.putString("parent_profilepic", url);
		//		bundle.putString("parent_freetime", freetime);
		//		bundle.putString("phone", phone_number);
		//		bundle.putString("parent_dob", dob);
		//		bundle.putString("parent_type", guardiantype);
		//		bundle.putString("parent_location", location);
		//		bundle.putString("parent_fbid", facebook_id);
		//		bundle.putString("facebook_friends", facebook_friends);
				android.support.v4.app.Fragment fragment = new Child_profile();
				fragment.setArguments(bundle);
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();

			} else {
				Toast.makeText(Home.this,"Update not successful, please try again", 1000).show();
				Bundle bundle = new Bundle();
				bundle.putString("child_name", child_name);
				bundle.putString("Child_id", child_ID);
				bundle.putString("Child_profilepic", child_profile_pic);
				bundle.putString("dob", child_dob);
				//bundle.putString("conditions", child_conditions);
				//bundle.putString("hobbies", child_hobbies);
				bundle.putString("Child_guardianId", user_guardian_id);
				bundle.putString("allergies", child_allergies);
				//bundle.putString("school", child_school);
				//bundle.putString("youthclub", child_youthclub);
				//bundle.putString("free_time", free_time_child);
		//		bundle.putString("parent_name", name);
		//		bundle.putString("parent_profilepic", url);
		//		bundle.putString("parent_freetime", freetime);
		//		bundle.putString("phone", phone_number);
		//		bundle.putString("parent_dob", dob);
		//		bundle.putString("parent_type", guardiantype);
		//		bundle.putString("parent_location", location);
		//		bundle.putString("parent_fbid", facebook_id);
	//			bundle.putString("facebook_friends", facebook_friends);
				android.support.v4.app.Fragment fragment = new Child_profile();
				fragment.setArguments(bundle);
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
			}
		}
	}

	public class child_pic_update1 extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(Home.this);
		String url_child;
		InputStream is;
		String result;
		JSONObject jArray = null;
		String data;

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading.......please wait");
			dialog.setCancelable(false);
			dialog.show();
		//	url_picupdate = "http://54.191.67.152/playdate/guardian_child.php";
		}

		@Override
		protected String doInBackground(String... arg0) {
			
			return null;
		}

		@SuppressLint({ "ShowToast" })
		protected void onPostExecute(String resultt) {
			dialog.dismiss();
			Toast.makeText(Home.this, "Image Captured", 1000).show();
			Bundle bundle = new Bundle();
		//	bundle.putString("user_guardian_id", user_guardian_id);
		//	bundle.putString("phone", phone_number);
		//	bundle.putString("name", name);
		//	bundle.putString("url", url);
			//bundle.putString("freetime", freetime);
		//	bundle.putString("dob", dob);
		//	bundle.putString("guardiantype", guardiantype);
		//	bundle.putString("location", location);
		//	bundle.putString("firstname", firstname);
		//	bundle.putString("facebook_id", facebook_id);
			bundle.putParcelable("bitmap", bitmap);
			android.support.v4.app.Fragment fragment = new Add_Child();
			fragment.setArguments(bundle);
			android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "3R9DSMW64DCK3236DHR9");
		GoogleAnalytics.getInstance(Home.this).reportActivityStart(this);
		    EasyTracker.getInstance(this).activityStart(this); 
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
		GoogleAnalytics.getInstance(Home.this).reportActivityStop(this);

		 EasyTracker.getInstance(this).activityStop(this); 
	}
	
	@Override
	 public boolean onKeyDown(int keyCode, KeyEvent event)
	 {
	 if (keyCode == KeyEvent.KEYCODE_BACK)
	 {
	    if (getSupportFragmentManager().getBackStackEntryCount() == 0)
	    {
	        this.finish();
	        return false;
	    }
	    else
	    {
	        getSupportFragmentManager().popBackStack();
	        removeCurrentFragment();

	        return false;
	    }



	 }

	  return super.onKeyDown(keyCode, event);
	 }
	public void removeCurrentFragment()
	{
	FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

	Fragment currentFrag =  getSupportFragmentManager().findFragmentById(R.id.content_frame);
	}
	
	
}
