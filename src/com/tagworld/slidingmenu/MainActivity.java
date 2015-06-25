package com.tagworld.slidingmenu;

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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.iapp.playdate.Add_Child;
import com.iapp.playdate.Arrange_date_fragment;
import com.iapp.playdate.Calander_event;
import com.iapp.playdate.Child_profile;
import com.iapp.playdate.ConnectionDetector;
import com.iapp.playdate.CustomDrawerAdapter;
import com.iapp.playdate.DrawerItem;
import com.iapp.playdate.GlobalVariable;
import com.iapp.playdate.Home;
import com.iapp.playdate.Home_fragment;
import com.iapp.playdate.NavigationDrawerclass;
import com.iapp.playdate.Parent_profile;
import com.iapp.playdate.R;
import com.iapp.playdate.Home.child_pic_update;
import com.iapp.playdate.Home.child_pic_update1;
import com.iapp.playdate.Home.parent_pic_update;
import com.iapptechnologies.time.util.Constants;
import com.iapptechnologies.time.util.ImageLoader;

public class MainActivity extends BaseActivity {

	private Fragment mContent;
	Button but, close;
	
	
	
	
	static int count;
	Button btnBack;
	SharedPreferences.Editor editor;
		Button btnSlide, btn_arrangedate, btn_calander, btn_home;
	Handler handler = new Handler();
	int btnWidth;
		static TextView txt_name;
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
	protected static final String PHOTO_TAKEN = "photo_taken";
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
	
	
	
	
	

	public MainActivity() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		settings = getSharedPreferences(Constants.PREFS_NAME, 0);
		editor = settings.edit();
	
		cd = new ConnectionDetector(this);
		isInternetPresent = cd.isConnectingToInternet();
		
		
		
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
		booleancheck = settings.getBoolean("checkk", chk);
		
		// setSlidingActionBarEnabled(true);
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");

		if (mContent == null)
			mContent = new Home_fragment();

		// set the Above View
		setContentView(R.layout.content_frame);
		
		image_profile = (ImageView)findViewById(R.id.imageView1_parent_drawer);
	    txt_name = (TextView)findViewById(R.id.textView1_profilename);
		String name_global=GlobalVariable.global_name;
		txt_name.setText(name_global);
		imgLoader = new ImageLoader(this);
		
		imgLoader.DisplayImage(url,image_profile);
		image_profile.requestLayout();
		
		Display display = getWindowManager().getDefaultDisplay();
		
		 height = display.getHeight();
		 width = display.getWidth();
		 float mReqPercentages = 35;
			 mCalculatedWidth = (mReqPercentages / 100) * width;
	
		
		
		

		
		image_profile.getLayoutParams().height = (int) (mCalculatedWidth);
		image_profile.getLayoutParams().width = (int) mCalculatedWidth;
	
		
            image_profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GlobalVariable.custom_Jsonarray=null;
				count++;
			
				mContent=new  Parent_profile();
		//		fragment.setArguments(bundle);
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, mContent);
				fragmentTransaction.addToBackStack("first12");
				fragmentTransaction.commit();
				getSlidingMenu().showContent();
				
			}
		}); 

            btn_arrangedate = (Button)findViewById(R.id.button_arrange);
    		btn_arrangedate.setOnClickListener(new OnClickListener() {

    			@Override
    			public void onClick(View v) {
    				GlobalVariable.custom_Jsonarray=null;
    			
    				mContent = new Arrange_date_fragment();
    		//		fragment.setArguments(bundle);
    				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
    				fragmentTransaction.replace(R.id.content_frame, mContent);
    				fragmentTransaction.addToBackStack("first13");
    				fragmentTransaction.commit();
    				getSlidingMenu().showContent();
    			}
    		});
    		btn_calander = (Button)findViewById(R.id.button_calander);
    		btn_calander.setOnClickListener(new OnClickListener() {

    			@Override
    			public void onClick(View v) {
    				GlobalVariable.custom_Jsonarray=null;
    			
    				mContent = new Calander_event();
    		//		fragment.setArguments(bundle);
    				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
    				fragmentTransaction.replace(R.id.content_frame, mContent);
    				fragmentTransaction.addToBackStack("first14");
    				fragmentTransaction.commit();
    				getSlidingMenu().showContent();
    			}
    		});

    		
    		btn_home = (Button) findViewById(R.id.button_home);
    		btn_home.setOnClickListener(new OnClickListener() {

    			@Override
    			public void onClick(View v) {
    				GlobalVariable.custom_Jsonarray=null;
    			//	Bundle bundle = new Bundle();
    			//	bundle.putString("user_guardian_id", user_guardian_id);
    				mContent = new Home_fragment();
    			//	fragment.setArguments(bundle);
    				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
    				fragmentTransaction.replace(R.id.content_frame, mContent);
    				fragmentTransaction.addToBackStack("first15");
    				fragmentTransaction.commit();
    				
    			}
    		});
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();
		but = (Button) findViewById(R.id.button_back);
		but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toggle();
			}
		});
		

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new SampleListFragment()).commit();
	}
		
		/*close = (Button) findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toggle();
			}
		});*/
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
							mContent = new Parent_profile();
			//		fragment.setArguments(bundle);
					android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, mContent);
					fragmentTransaction.commit();
					
					
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
							mContent = new Child_profile();
							mContent.setArguments(bundle);
					android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, mContent);
					fragmentTransaction.commit();
					
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
							mContent = new Add_Child();
							
							android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
							android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
							fragmentTransaction.replace(R.id.content_frame, mContent);
							fragmentTransaction.commit();
							
						
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
						Toast.makeText(MainActivity.this,"Please check internet connection", 2000).show();
					}
				}
				if (GlobalVariable.parent_picute_update == 2) {

					if (isInternetPresent) 
					{
						new child_pic_update().execute();

					} else {
						Toast.makeText(MainActivity.this,"Please check internet connection", 2000).show();
					}
				}
				if (GlobalVariable.parent_picute_update == 3) {

					if (isInternetPresent) 
					{
						new child_pic_update1().execute();

					} else {
						Toast.makeText(MainActivity.this,"Please check internet connection", 2000).show();

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

	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}
	@Override
	protected void onResume() {
		super.onResume();
		com.facebook.AppEventsLogger.activateApp(this, "272047936334195");

	}
	
	@Override
	public void onBackPressed() {

		finish();
		super.onBackPressed();
	}

	public class parent_pic_update extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(MainActivity.this);

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
				Toast.makeText(MainActivity.this, "Update Successful", 1000).show();
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
				Toast.makeText(MainActivity.this,"Update not successful, please try again", 1000).show();
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
		ProgressDialog dialog = new ProgressDialog(MainActivity.this);
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
				Toast.makeText(MainActivity.this, "Update Successful", 1000).show();
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
				Toast.makeText(MainActivity.this,"Update not successful, please try again", 1000).show();
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
		ProgressDialog dialog = new ProgressDialog(MainActivity.this);
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
			Toast.makeText(MainActivity.this, "Image Captured", 1000).show();
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
		
		    EasyTracker.getInstance(this).activityStart(this); 
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
		 EasyTracker.getInstance(this).activityStop(this); 
	}

}
