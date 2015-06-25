package com.iapp.playdate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.iapp.playdate.R;
import com.iapptechnologies.time.models.ChildInfo;
import com.iapptechnologies.time.models.FriendsInfo;
import com.iapptechnologies.time.util.Constants;


public class authentication extends android.support.v4.app.Fragment {
	ArrayList<String>friend_id_list=new ArrayList<String>();//child_id_list
	ArrayList<String>child_id_list=new ArrayList<String>();
	ArrayList<String>selectedList=new ArrayList<String>();
	TextView friends_list;
	//MyCustomAdapter dataAdapter = null;
	//MyCustomAdapter_friend data_friendadapter=null;
	ListView list_child,list_friend;
	String url,user_id,facebook_friends,friends_id_for_setcreate="",child_id_for_setcreate="";
	Button btn_add;
	Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ArrayList<arrayList>al;
    ArrayList<arrayList>al_friend;//=new ArrayList<authentication.arrayList>();
    ProgressDialog dialog;
    ArrayList<Boolean>check;
    ArrayList<FriendsInfo> friendIfoList;
    ImageLoader imageLoader;
    String Childname,child_id,profile_image;
    ArrayList<ChildInfo> childInfoListLIst;
    SharedPreferences settings;
	SharedPreferences.Editor editor;
    @Override
		public View onCreateView(final LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ViewGroup view = (ViewGroup) inflater.inflate(R.layout.authentication, container,false);
			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			check=new ArrayList<Boolean>();
			settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
			friends_list=(TextView)view.findViewById(R.id.friends_list);
			editor = settings.edit();
			friendIfoList=new ArrayList<FriendsInfo>();
			childInfoListLIst=new ArrayList<ChildInfo>();
			 imageLoader=new ImageLoader(getActivity());
			list_child=(ListView)view.findViewById(R.id.listView_child_authenticate);
			list_friend=(ListView)view.findViewById(R.id.listView_friend_authenticate);
			btn_add=(Button)view.findViewById(R.id.button_authentication);
			
			user_id=settings.getString("USERID","");
			facebook_friends=settings.getString("FBFRIEND","");
			
			 cd=new ConnectionDetector(getActivity());
			 isInternetPresent = cd.isConnectingToInternet();
			 
			 if (isInternetPresent) {
					
				 new Get_Childinfo().execute();
				// new Get_friendinfo().execute();
						
				 }
				 else{
					 Toast.makeText(getActivity(),"Please check internet connection",2000).show();
					
				 }
			 btn_add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					
//					for(int i=0;i<child_id_list.size();i++){
//						   String id=child_id_list.get(i);
//						   System.out.println("iddddddd...."+id);
//						   if(child_id_for_setcreate.equals("")){
//							   child_id_for_setcreate=id;
//						   }else{
//							   child_id_for_setcreate=child_id_for_setcreate+","+id;
//						   }
//					   }
//					
//					for(int i=0;i<friend_id_list.size();i++){
//						   String id=friend_id_list.get(i);
//						   System.out.println("iddddddd...."+id);
//						   if(friends_id_for_setcreate.equals("")){
//							   friends_id_for_setcreate=id;
//						   }else{
//						   friends_id_for_setcreate=friends_id_for_setcreate+","+id;
//						   }
//					   }
					
					
				
					
					
					 if (isInternetPresent) {
//						if(child_id_for_setcreate.length()>0 && friends_id_for_setcreate.length()>0){
							
						 String child_id = settings.getString("ChildInfoShared", "");
		                  if(child_id.equalsIgnoreCase(""))
		                  {
		                 Toast.makeText(getActivity(), "Please select atleast one child",Toast.LENGTH_LONG).show();
		                  }else
		                  {
		                	  new assignparent().execute();
		                	 
		                  }
		                  
						 
							
						//}
						// new get_sets_friends().execute();
							
							
					 }
					 else{
						 Toast.makeText(getActivity(),"Please check internet connection",2000).show();
						
					 }
					
				}
			});
			 list_child.requestLayout();
			 list_friend.requestLayout();
			 
			 Display display = getActivity().getWindowManager().getDefaultDisplay();
				
				int height = display.getHeight();
				int width = display.getWidth();
				
				System.out.println(height  +  "   "+width);
				
				float mReqPercentages = 32;
				float mCalculatedWidth = (mReqPercentages / 100) * height;
				
				 list_child.getLayoutParams().height = (int)mCalculatedWidth;
				  list_friend.getLayoutParams().height = (int)mCalculatedWidth;
				
				//child_list.getLayoutParams().height = (int)mCalculatedWidth;
				//child_list.getLayoutParams().width = (int)mCalculatedWidth1;
			/* int density = getActivity().getResources().getDisplayMetrics().densityDpi;
			  switch (density) {
			  case DisplayMetrics.DENSITY_LOW:
				  list_child.getLayoutParams().height = 130;
				  list_friend.getLayoutParams().height = 130;
				 
				  break;
			  case DisplayMetrics.DENSITY_MEDIUM:
				  list_child.getLayoutParams().height = 195;
				  list_friend.getLayoutParams().height = 195;
				  break;
			  case DisplayMetrics.DENSITY_HIGH:
				  list_child.getLayoutParams().height = 280;
				  list_friend.getLayoutParams().height = 280;
				  break;
			  case DisplayMetrics.DENSITY_XHIGH:
				  list_child.getLayoutParams().height = 380;
				  list_friend.getLayoutParams().height = 380;
				  break;
			  case DisplayMetrics.DENSITY_XXHIGH:
				  list_child.getLayoutParams().height = 380;
				  list_friend.getLayoutParams().height = 380;
				  break;
			  }
			  */
			 
	 return view;
	 }
	 
	 public class Get_friendinfo extends AsyncTask<String, Integer, String> {
			ProgressDialog dialog = new ProgressDialog(getActivity());
	String friend_name,friend_id,friend_imageurl;
	
String url_sets;
			@Override
			protected void onPreExecute() {
				
				dialog.show();
				dialog.setMessage("Loading.......please wait");
				//url_sets = "http://54.191.67.152/playdate/facebook_friend_detail.php";
				// http://54.191.67.152/playdate/facebook_friend_detail.php?friend_fbid=%27100004938971287%27,%27100001678200547%27
				url_sets = Constants.ASSIGNED_FRIENDS;//"http://54.191.67.152/playdate/guardian_assigned_unsigned_friends.php?";
			}

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				friend_id_list.clear();
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(url_sets);

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("friend_fbids",facebook_friends));
				nameValuePairs.add(new BasicNameValuePair("g_id",user_id));
				
				 //SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
			       String child_id = settings.getString("ChildInfoShared", "");
				
				nameValuePairs.add(new BasicNameValuePair("child_id",child_id));
				Log.e("FaceBooFriends==",""+facebook_friends);
				Log.e("GID==",""+user_id);
				Log.e("CID==",""+child_id_for_setcreate);
				Log.e("UserId",""+user_id);
//				System.out.println(facebook_friends);
				//741557995900933','1452969094987636'
				//100001122280010
				
				
				//741557995900933','1452969094987636'
				//100001122280010
//				//350
//				08-21 18:26:47.826: E/FaceBooFriends==(30184): '741557995900933','1452969094987636'
//						08-21 18:26:47.866: E/UserId(30184): 100001122280010

			//	http://54.191.67.152/playdate/guardian_assigned_unsigned_friends.php?friend_fbid='741557995900933','1452969094987636'&g_id=100001122280010&child_id=342
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
String sresponse=null;
				try {
					sresponse = reader.readLine();

//					System.out.println("response.................................."
//							+ sresponse);
					Log.e("Response==",""+sresponse);
		// {"friendinfo":[{"g_id":"100001122280017","firstname":"Jago","checked":"false","profile_image":"http:\/\/54.191.67.152\/playdate\/uploads\/53efca335d002.jpg"},{"g_id":"100001122280019","firstname":"Mohit","checked":"true","profile_image":"http:\/\/54.191.67.152\/https:\/\/fbcdn-profile-a.akamaihd.net\/hprofile-ak-xap1\/t1.0-1\/s200x200\/59405_113115308745208_3001142_n.jpg"}],"success":1}

					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject json = null;
				try {
					json = new JSONObject(sresponse);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					
					JSONObject jsonObj = new JSONObject(sresponse);
					JSONArray contacts = jsonObj.getJSONArray("friendinfo");
					for (int i = 0; i < contacts.length(); i++) 
					{
//					al_friend=new ArrayList<authentication.arrayList>();
					 FriendsInfo friendInfo=new FriendsInfo();
	                 JSONObject c = contacts.getJSONObject(i);
	                 String GargonId =c.getString("g_id");
	                 friendInfo.setGargonId(GargonId);
	                 
	                 Log.e("GargonId==",""+GargonId);
	                 String firstNameStr=c.getString("firstname");
	                 friendInfo.setFirstName(firstNameStr);
	                 
	                 Log.e("FirstName==",""+firstNameStr);
	                 String checKedStr=c.getString("checked");
	                 friendInfo.setChecked(checKedStr);
	                 
	                 if(checKedStr.equals("true")){
	                	 friend_id_list.add(GargonId);
	                 }
	                 
	                 Log.e("Checked==",""+checKedStr);
	                 String profileImageStr=c.getString("profile_image");
	                 friendInfo.setProfileImage(profileImageStr);
	                 
	                 friendIfoList.add(friendInfo);
	                 Log.e("FriendInfoList==",""+friendIfoList.size());
	                 
//						arrayList arrlist =new arrayList(friend_imageurl,friend_name,friend_id, false);
//						al_friend.add(arrlist);
	                 
					 }
					
//					al_friend=new ArrayList<authentication.arrayList>();
//					JSONArray jArray = json.getJSONArray("data");
//					for (int i = 0; i < jArray.length(); i++) {
//						JSONObject getdetail = jArray.getJSONObject(i);
//						JSONObject friendinfo = getdetail
//								.getJSONObject("friendinfo");
//						
//						friend_name=friendinfo.getString("name");
//						System.out.println("friendinfo"+friend_name);
//						
//						friend_id=friendinfo.getString("g_id");
//						
//						friend_imageurl=friendinfo.getString("profile_image");
//					
//						
//						arrayList arrlist =new arrayList(friend_imageurl,friend_name,friend_id, false);
//						al_friend.add(arrlist);
//					
//					}
					
					 
				} catch (Exception e) {

				}
				return null;
			}

			protected void onPostExecute(String resultt) {
				dialog.dismiss();
				list_friend.setVisibility(View.VISIBLE);
				friends_list.setVisibility(View.VISIBLE);
				list_friend.setAdapter(new FriendList());
//				data_friendadapter = new MyCustomAdapter_friend(getActivity(),R.layout.list_add_sets,al_friend);
//				list_friend.setAdapter(data_friendadapter);
//				list_friend.setVisibility(View.VISIBLE);
			}
		}
	 public class Get_Childinfo extends AsyncTask<String, Integer, String> {
			ProgressDialog dialog = new ProgressDialog(getActivity());
	
	

			@Override
			protected void onPreExecute() {
				
				dialog.show();
				dialog.setMessage("Loading.......please wait");
				url = Constants.GUARDIAN_CHILD;//"http://54.191.67.152/playdate/guard_child.php";//?g_id=46" ///not change
						}

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(url);

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("g_id", user_id));
				

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
				String sresponse=null;
				try {
					sresponse = reader.readLine();

					System.out.println("response.................................."
							+ sresponse);
					
					
					Log.e("Start==",""+sresponse);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject json = null;
				try {
					System.out.println("dgskjsah");
					json = new JSONObject(sresponse);
				
					al=new ArrayList<authentication.arrayList>();
					JSONArray jArray = json.getJSONArray("data");
					System.out.println("1111111111111    "+jArray);
					for (int i = 0; i < jArray.length(); i++) {
						System.out.println("inside array");
						ChildInfo childInfo=new ChildInfo();	
						
						JSONObject getdetail = jArray.getJSONObject(i);
						
						
						Childname=getdetail.getString("Childname");
						childInfo.setChildName(Childname);
						
						child_id=getdetail.getString("child_id");
						childInfo.setChildId(child_id);
						
						profile_image=getdetail.getString("profile_image");
						childInfo.setProfileImage(profile_image);
						
						
						
//						arrayList arrlist =new arrayList(profile_image,Childname,child_id, false);
//				          al.add(arrlist);
						childInfoListLIst.add(childInfo);
					}
					
					 
				} catch (Exception e) {

				}
				return null;
			}
      
			protected void onPostExecute(String resultt) {

				dialog.dismiss();
				list_child.setAdapter(new ChildInfoList());
				
//				dataAdapter = new MyCustomAdapter(getActivity(),R.layout.list_add_sets,al);
//			    
//				list_child.setAdapter(dataAdapter);
			}
		}

	 
	 //new web service
	 
	 
	 public class Get_friendinfo_new extends AsyncTask<String, Integer, String> {
			ProgressDialog dialog = new ProgressDialog(getActivity());
	String friend_name,friend_id,friend_imageurl;
	
String url_sets;
			@Override
			protected void onPreExecute() {
				
				dialog.show();
				dialog.setMessage("Loading.......please wait");
				url_sets =Constants.ASSIGNED_FRIENDS; //"http://54.191.67.152/playdate/guardian_assigned_unsigned_friends.php";
				// http://54.191.67.152/playdate/guardian_assigned_unsigned_friends.php?g_id=100001122280003&child_id=//325&friend_fbids='100001402194206','100001496842161',%27100008236784143%27
			}

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(url_sets);

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("friend_fbid",
						facebook_friends));
				System.out.println(facebook_friends);

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
				String sresponse=null;
				try {
					sresponse = reader.readLine();

					System.out.println("response.................................."
							+ sresponse);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject json = null;
				try {
					json = new JSONObject(sresponse);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				try {
					
//					JSONArray jArray = json.getJSONArray("friendinfo");
//					for (int i = 0; i < jArray.length(); i++) {
//						JSONObject getdetail = jArray.getJSONObject(i);
					
					
					al_friend=new ArrayList<authentication.arrayList>();
					JSONArray jArray = json.getJSONArray("friendinfo");
					for (int i = 0; i < jArray.length(); i++) 
					{	

						arrayList arrlist =new arrayList(friend_imageurl,friend_name,friend_id, false);
						al_friend.add(arrlist);
					
					}
					friend_id_list.clear();
					 
				} catch (Exception e) {

				}
				return null;
			}

			protected void onPostExecute(String resultt) {

//				dialog.dismiss();
//				
//				data_friendadapter = new MyCustomAdapter_friend(getActivity(),R.layout.list_add_sets,al_friend);
//			    
//				list_friend.setAdapter(data_friendadapter);
			}
		}
	 
	 //end of new web service
	 
	 
	 
	 
	 
	 
//	 private class MyCustomAdapter extends ArrayAdapter<arrayList> {
//
//		  private ArrayList<arrayList> al;
//           ImageLoader imageLoader=new ImageLoader(getActivity());
//		  public MyCustomAdapter(Context context, int textViewResourceId,ArrayList<arrayList> al) {
//		   super(context, textViewResourceId, al);
//		   this.al = new ArrayList<arrayList>();
//		   this.al.addAll(al);
//		  }
//
//		  private class ViewHolder {
//		   TextView code;
//		   CheckBox name;
//		   ImageView image;
//		  }
//
//		  @Override
//		  public View getView(final int position, View convertView, ViewGroup parent) {
//
//		   ViewHolder holder = null;
//		   Log.v("ConvertView", String.valueOf(position));
//
//		   if (convertView == null) {
//		    LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		    convertView = vi.inflate(R.layout.list_add_sets, null);
//
//		    holder = new ViewHolder();
//		    holder.code = (TextView) convertView.findViewById(R.id.textView_list_addsets_name);
//		    holder.name = (CheckBox) convertView.findViewById(R.id.checkBox_addsets);
//		    holder.image = (ImageView) convertView.findViewById(R.id.imageView1_addsets);
//		    convertView.setTag(holder);
//
//		    
//		    
//		    holder.name.setOnClickListener(new View.OnClickListener() {
//		     public void onClick(View v) {
//		      
//		      System.out.println("running......................");
//		      
//		      CheckBox cb = (CheckBox) v;
//		      arrayList al1 = (arrayList) cb.getTag();
//		     
//		     
//		      if (cb.isChecked()) {
//		      
//		       String search=cb.getText().toString();
//		       System.out.println("search..."+search);
//		       child_id_list.add(search);
//		       Log.e("ChildID==",""+child_id_list);
//		       
//		       cb.setChecked(true);
//		       String childId=child_id_list.get(position).trim();
//		       Log.e("ChildId==",""+childId);
//		       new Get_friendinfo().execute();
//		      } else {
//		    	   cb.setChecked(false);
//		       String search=cb.getText().toString();
//		       child_id_list.remove(search);
//		      }
//		    
//		     
//		      al1.setSelected(cb.isChecked());
//		     }
//		    });
//		   } else {
//		    holder = (ViewHolder) convertView.getTag();
//		   }
//
//		   arrayList al1 = al.get(position);
//		   holder.code.setText(al1.getCode());
//		   holder.name.setText(al1.getName());
//		   holder.name.setChecked(al1.isSelected());
//		   
//		  
//		   holder.name.setTag(al1);
//          String _imgurl=al1.getimage();
//		   imageLoader.DisplayImage(_imgurl,holder.image);
//			
//			holder.image.requestLayout();
//			 int density = getResources().getDisplayMetrics().densityDpi;
//			  switch (density) {
//			  case DisplayMetrics.DENSITY_LOW:
//				  holder.image.getLayoutParams().height = 40;
//				  holder.image.getLayoutParams().width = 40;
//				 
//				  break;
//			  case DisplayMetrics.DENSITY_MEDIUM:
//				  holder.image.getLayoutParams().height = 65;
//				  holder.image.getLayoutParams().width = 65;
//				  break;
//			  case DisplayMetrics.DENSITY_HIGH:
//				  holder.image.getLayoutParams().height = 85;
//				  holder.image.getLayoutParams().width = 85;
//				  break;
//			  case DisplayMetrics.DENSITY_XHIGH:
//				  holder.image.getLayoutParams().height = 120;
//				  holder.image.getLayoutParams().width = 120;
//				  break;
//			  case DisplayMetrics.DENSITY_XXHIGH:
//				  holder.image.getLayoutParams().height = 120;
//				  holder.image.getLayoutParams().width = 120;
//				  break;
//			  }
//		   return convertView;
//
//		  }
//
//		 }

	 

	 
	 
//	 private class MyCustomAdapter_friend extends ArrayAdapter<arrayList> {
//
//		  private ArrayList<arrayList> al;
//          ImageLoader imageLoader=new ImageLoader(getActivity());
//		  public MyCustomAdapter_friend(Context context, int textViewResourceId,ArrayList<arrayList> al) {
//		   super(context, textViewResourceId, al);
//		   this.al = new ArrayList<arrayList>();
//		   this.al.addAll(al);
//		  }
//
		  private class ViewHolder {
		   TextView code;
		   CheckBox name;
		   ImageView image;
		  }
//
//		  @Override
//		  public View getView(int position, View convertView, ViewGroup parent) {
//
//		   ViewHolder holder = null;
//		   Log.v("ConvertView", String.valueOf(position));
//
//		   if (convertView == null) {
//		    LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		    convertView = vi.inflate(R.layout.list_add_sets, null);
//
//		    holder = new ViewHolder();
//		    holder.code = (TextView) convertView.findViewById(R.id.textView_list_addsets_name);
//		    holder.name = (CheckBox) convertView.findViewById(R.id.checkBox_addsets);
//		    holder.image = (ImageView) convertView.findViewById(R.id.imageView1_addsets);
//		    convertView.setTag(holder);
//
//		    holder.name.setOnClickListener(new View.OnClickListener() {
//		     public void onClick(View v) {
//		      
//		      System.out.println("running......................");
//		      
//		      CheckBox cb = (CheckBox) v;
//		      arrayList al1 = (arrayList) cb.getTag();
//		     
//		     
//		      if (cb.isChecked()) {
//		      
//		       String search=cb.getText().toString();
//		       System.out.println("search..."+search);
//		       friend_id_list.add(search);
//
//		      } else {
//		     
//		       String search=cb.getText().toString();
//		       friend_id_list.remove(search);
//		      }
//		     /* Toast.makeText(getActivity(),
//				        "Checkbox " + cb.getText() ,
//				        Toast.LENGTH_LONG).show();*/
//		     
//		      al1.setSelected(cb.isChecked());
//		     }
//		    });
//		   } else {
//		    holder = (ViewHolder) convertView.getTag();
//		   }
//
//		   
//		   arrayList al1 = al.get(position);
//		   holder.code.setText(al1.getCode());
//		   holder.name.setText(al1.getName());
//		   holder.name.setChecked(al1.isSelected());
//		   
//		  
//		   holder.name.setTag(al1);
//         String _imgurl=al1.getimage();
//		   imageLoader.DisplayImage(_imgurl,holder.image);
//			
//			holder.image.requestLayout();
//			 int density = getResources().getDisplayMetrics().densityDpi;
//			  switch (density) {
//			  case DisplayMetrics.DENSITY_LOW:
//				  holder.image.getLayoutParams().height = 40;
//				  holder.image.getLayoutParams().width = 40;
//				 
//				  break;
//			  case DisplayMetrics.DENSITY_MEDIUM:
//				  holder.image.getLayoutParams().height = 85;
//				  holder.image.getLayoutParams().width = 85;
//				  break;
//			  case DisplayMetrics.DENSITY_HIGH:
//				  holder.image.getLayoutParams().height = 85;
//				  holder.image.getLayoutParams().width = 85;
//				  break;
//			  case DisplayMetrics.DENSITY_XHIGH:
//				  holder.image.getLayoutParams().height = 120;
//				  holder.image.getLayoutParams().width = 120;
//				  break;
//			  case DisplayMetrics.DENSITY_XXHIGH:
//				  holder.image.getLayoutParams().height = 120;
//				  holder.image.getLayoutParams().width = 120;
//				  break;
//			  }
//			
//		   
//		   return convertView;
//
//		  }
//
//		 }

		 // arrayList class
		 public class arrayList {

		  String code = null;
		  String name = null;
		  String image = null;
		  boolean selected = false;

		  public arrayList(String image,String code, String name, boolean selected) {
		   super();
		   this.code = code;
		this.name=name;
		this.image=image;
		   this.selected = selected;
		  }

		  public String getCode() {
		   return code;
		  }

		  public void setCode(String code) {
		   this.code = code;
		  }

		  public String getimage() {
			   return image;
			  }

			  public void setimage(String image) {
			   this.image = image;
			  }
			  
		  public String getName() {
		   return name;
		  }

		  public void setName(String name) {
		   this.name = name;
		  }

		  public boolean isSelected() {
		   return selected;
		  }

		  public void setSelected(boolean selected) {
		   this.selected = selected;
		  }

		 }
		 
		 public class assignparent extends AsyncTask<String, Integer, String> {
				ProgressDialog dialog = new ProgressDialog(getActivity());
		String success;
		String url_for_authentication;
				@Override
				protected void onPreExecute() {
					// Toast.makeText(Login.this,"asynch task",Toast.LENGTH_LONG).show();

					dialog.show();
					dialog.setMessage("Loading.......please wait");
					url_for_authentication=Constants.ASSIGN_GUARDIAN;//"http://54.191.67.152/playdate/edit_auth_child.php?";
					
					//url_for_authentication="http://54.191.67.152/playdate/auth_child.php";//?child_id=68&assign_gid=47&org_gid=114;
					}//http://54.191.67.152/playdate/auth_child.php?child_id=205&friend_id=108&g_id=112

				//http://54.191.67.152/playdate/edit_auth_child.php
				
////SaveBUtton'556'
				
				//http://54.191.67.152/playdate/edit_auth_child.php?child_id=342&friend_id='100001122280019','100001122280017'&g_id=&user_id=100001122280010
				@Override
				protected String doInBackground(String... params) {
					
					HttpClient httpClient = new DefaultHttpClient();
					HttpContext localContext = new BasicHttpContext();
					HttpPost httpPost = new HttpPost(url_for_authentication);
					//342
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				//checbox
                //  SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
                  String child_id = settings.getString("ChildInfoShared", "");
                  if(child_id.equalsIgnoreCase(""))
                  {
                  nameValuePairs.add(new BasicNameValuePair("child_id","")); 
                  }else
                  {
                   nameValuePairs.add(new BasicNameValuePair("child_id",child_id));   
                  }
                  
                  
                 /*String friendGidss="";
                  for(int z=0;z<friendIfoList.size();z++)
				  {
                 String   friendGid=friendIfoList.get(z).getGargonId().toString();
				  String   friendGids=""+friendGid+"";
				 friendGidss=friendGidss+""+friendGids+",";
				 
				
				
					Log.e("FriendGID====",""+friendGidss);
					}
                  String str="";
                  if (friendGidss.length() > 0 && friendGidss.charAt(friendGidss.length()-1)==',') {
     				  str = friendGidss.substring(0, friendGidss.length()-1);
     					Log.e("FriendGID====",""+str);
     				    }*/
                  String str="";
                  if(friend_id_list.size()>0){
                	  for(int length=0;length<friend_id_list.size();length++){
                		 
                		  if(str.equals("")||str.equals(null)){
                			  str=friend_id_list.get(length);
                		  }else{
                			  str=str+","+friend_id_list.get(length);
                		  }
                	  }
                  }
                  System.out.println("str.........."+str);
                  if(str.equalsIgnoreCase(""))
                  {
                	nameValuePairs.add(new BasicNameValuePair("friend_id",""));
                  }else
                  {
                	  nameValuePairs.add(new BasicNameValuePair("friend_id",str));
                  }

				
				 nameValuePairs.add(new BasicNameValuePair("g_id",user_id));
					
					System.out.println(".....child...."+child_id_for_setcreate);
					System.out.println("...friend....."+friends_id_for_setcreate);
					
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
						String sresponse=null;
					try {
						sresponse = reader.readLine();

						Log.e("Response===",""+sresponse);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JSONObject json = null;
					try {
						json = new JSONObject(sresponse);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				try {
					success=json.getString("success");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					
					return null;
				}

				protected void onPostExecute(String resultt) {

					dialog.dismiss();
					if(success.equals("1")){
//						child_id_list.clear();
//						friend_id_list.clear();
//						child_id_for_setcreate="";
//						friends_id_for_setcreate="";
//						 new Get_Childinfo().execute();
//						 new Get_friendinfo().execute();
						
//						Intent in=new Intent(getActivity(),Home.class);
//						startActivity(in);
						
						Toast.makeText(getActivity(),"Update Sucessfully",1).show();
						android.support.v4.app.Fragment fragment = new Home_fragment();
						//fragment.setArguments(bundle);
						android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
						android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
						fragmentTransaction.replace(R.id.content_frame, fragment);
					
						fragmentTransaction.commit();
						
					}else{
//						 new Get_Childinfo().execute();
//						 new Get_friendinfo().execute();
//						Toast.makeText(getActivity(), "Please try again later", 2000).show();
					}
					
				}
			}
		 
		 class FriendList extends BaseAdapter
		 {

			@Override
			public int getCount() {
			
				return friendIfoList.size();
			}

			@Override
			public Object getItem(int position) {
			
				return position;
			}

			@Override
			public long getItemId(int position) {
		
				return position;
			}

			  @Override
			  public View getView(final int position, View convertView, ViewGroup parent) {
	
			   ViewHolder holder = null;
			   Log.v("ConvertView", String.valueOf(position));
	
			   if (convertView == null) {
			    LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    convertView = vi.inflate(R.layout.list_add_sets, null);
	
			    holder = new ViewHolder();
			    holder.code = (TextView) convertView.findViewById(R.id.textView_list_addsets_name);
			    holder.name = (CheckBox) convertView.findViewById(R.id.checkBox_addsets);
			    holder.image = (ImageView) convertView.findViewById(R.id.imageView1_addsets);
			    convertView.setTag(holder);
	
			    holder.name.setOnClickListener(new View.OnClickListener() {
			     public void onClick(View v) {
			      
			      System.out.println("running......................");
			      
			      CheckBox cb = (CheckBox) v;
			      arrayList al1 = (arrayList) cb.getTag();
			     
			     
			      if (cb.isChecked()) {
			      
			       String search=friendIfoList.get(position).getGargonId();//cb.getText().toString();
			       System.out.println("search..."+search);
			       friend_id_list.add(search);
	
			      } else {
			     
//			       String search=cb.getText().toString();
			    	  String search=friendIfoList.get(position).getGargonId();
			       friend_id_list.remove(search);
			      }
			     /* Toast.makeText(getActivity(),
					        "Checkbox " + cb.getText() ,
					        Toast.LENGTH_LONG).show();*/
			     
			    //...  al1.setSelected(cb.isChecked());
			     }
			    });
			   } else {
			    holder = (ViewHolder) convertView.getTag();
			   }
	
			   
//			   arrayList al1 = al.get(position);
//			   holder.code.setText(al1.getCode());
//			   holder.name.setText(al1.getName());
//			   holder.name.setChecked(al1.isSelected());
//			   
//			  
//			   holder.name.setTag(al1);
//	           String _imgurl=al1.getimage();
			   imageLoader.DisplayImage(friendIfoList.get(position).getProfileImage(),holder.image);
			   holder.code.setText(friendIfoList.get(position).getFirstName());
			   holder.name.setText(friendIfoList.get(position).getGargonId());
			   if(friendIfoList.get(position).getChecked().contains("false"))
			   {
			   holder.name.setChecked(false);
			   }else
			   {
				holder.name.setChecked(true);   
			   }	
				holder.image.requestLayout();
				Display display = getActivity().getWindowManager().getDefaultDisplay();
				
				int height = display.getHeight();
				int width = display.getWidth();
				
				System.out.println(height  +  "   "+width);
				
				float mReqPercentages = 14;
				float mCalculatedWidth = (mReqPercentages / 100) * width;

				
				holder.image.getLayoutParams().height = (int) (mCalculatedWidth);
				holder.image.getLayoutParams().width = (int) mCalculatedWidth;
				/* int density = getResources().getDisplayMetrics().densityDpi;
				  switch (density) {
				  case DisplayMetrics.DENSITY_LOW:
					  holder.image.getLayoutParams().height = 40;
					  holder.image.getLayoutParams().width = 40;
					 
					  break;
				  case DisplayMetrics.DENSITY_MEDIUM:
					  holder.image.getLayoutParams().height = 85;
					  holder.image.getLayoutParams().width = 85;
					  break;
				  case DisplayMetrics.DENSITY_HIGH:
					  holder.image.getLayoutParams().height = 85;
					  holder.image.getLayoutParams().width = 85;
					  break;
				  case DisplayMetrics.DENSITY_XHIGH:
					  holder.image.getLayoutParams().height = 120;
					  holder.image.getLayoutParams().width = 120;
					  break;
				  case DisplayMetrics.DENSITY_XXHIGH:
					  holder.image.getLayoutParams().height = 120;
					  holder.image.getLayoutParams().width = 120;
					  break;
				  }*/
				
			   
			   return convertView;
	
			  }
				 
		 }
		 
		 class ChildInfoList extends BaseAdapter
		 {
			    private boolean userSelected = false;
			    private CheckBox mCurrentlyCheckedRB;
			    boolean[]   checkBoxState = new boolean[childInfoListLIst.size()];

			@Override
			public int getCount() {
		
				return childInfoListLIst.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			  @Override
			  public View getView(final int position, View convertView, ViewGroup parent) {
	
			   ViewHolders holder = null;
			  // convertView=null;
	
			   if (convertView == null) {
			    LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    convertView = vi.inflate(R.layout.chilinfoinflater, null);
	
			    holder = new ViewHolders();
			    holder.code = (TextView) convertView.findViewById(R.id.textView_list_addsets_name);
			    holder.radio = (CheckBox) convertView.findViewById(R.id.checkBox_addsets);
			    holder.image = (ImageView) convertView.findViewById(R.id.imageView1_addsets);
			    convertView.setTag(holder);
	

			   } else {
			    holder = (ViewHolders) convertView.getTag();
			   }
	
			   imageLoader.DisplayImage(childInfoListLIst.get(position).getProfileImage(),holder.image);
			   holder.code.setText(childInfoListLIst.get(position).getChildName());
			   
			  /* if (position == getCount() - 1 && userSelected == false) {
		            holder.radio.setChecked(false);
		            mCurrentlyCheckedRB = holder.radio;
		        } else {
		            holder.radio.setChecked(false);
		        }
			   */
			   if(selectedList.contains(String.valueOf(position))){
				   holder.radio.setChecked(true);
			   }else{
				   holder.radio.setChecked(false);
			   }
			   
			   holder.radio.setOnClickListener(new OnClickListener() {

		            @Override
		            public void onClick(View v) {
		            	
		            	 CheckBox cb = (CheckBox) v;
					      arrayList al1 = (arrayList) cb.getTag();
					      
					      if (cb.isChecked()) {
					    	  
					    	  
					    	  if(selectedList.contains(String.valueOf(position))){
								  cb.setChecked(false);
								  selectedList.remove(String.valueOf(position));
							   }else{
								   String search = cb.getText().toString();
								      Log.e("Search==",""+search);
								      //System.out.println("search..." + search);
								      selectedList.clear();
								      selectedList.add(String.valueOf(position));
								      cb.setChecked(true);
								      checkBoxState[position] = true;
								      System.out.println("checked");
								      String childId=childInfoListLIst.get(position).getChildId();
					                   Log.e("ChildId==",""+childId);
					                   // SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
					                   // SharedPreferences.Editor editor = settings.edit();
					                    editor.putString("ChildInfoShared", childId);
					                    editor.commit();
					                    
					                    friendIfoList.clear();
					                   
					                   list_child.setAdapter(new ChildInfoList());
					                   if(position==0){
					                	   list_child.setSelection(position);
					                   }else{
					                	   list_child.setSelection(position-1);
					                   }
					                   
					                   
								      new Get_friendinfo().execute();
							   }

						      

						     } else {
						    	cb.setChecked(false);
						      String search = cb.getText().toString();
						      selectedList.remove(String.valueOf(position));
						      String childId=childInfoListLIst.get(position).getChildId();
			                   Log.e("ChildId==",""+childId);
			                   // SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
			                   // SharedPreferences.Editor editor = settings.edit();
			                    editor.putString("ChildInfoShared", "");
			                    editor.commit();
						      checkBoxState[position] = false;
						      System.out.println("unchecked");
						     }
					      

		               /* if (mCurrentlyCheckedRB != null) {
		                    if (mCurrentlyCheckedRB == null)
		                        mCurrentlyCheckedRB = (CheckBox) v;
		                    mCurrentlyCheckedRB.setChecked(true);
		                    Log.e("CheckedTrue==","true");
		                    String childId=childInfoListLIst.get(position).getChildId();
		                    Log.e("ChildId==",""+childId);
		                   // SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
		                   // SharedPreferences.Editor editor = settings.edit();
		                    editor.putString("ChildInfoShared", childId);
		                    editor.commit();
		                    
		                    friendIfoList.clear();
		                    new Get_friendinfo().execute();
		                   
		                }
		                Log.e("CheckedFalse==","false");
		                if (mCurrentlyCheckedRB == v)
		                    return;

		               // mCurrentlyCheckedRB.setChecked(false);
		                ((CheckBox) v).setChecked(true);
		                mCurrentlyCheckedRB = (CheckBox) v;*/
		            }
		        });
			   
			  
			   
			   
	
				holder.image.requestLayout();
				Display display = getActivity().getWindowManager().getDefaultDisplay();
				
				int height = display.getHeight();
				int width = display.getWidth();
				
				System.out.println(height  +  "   "+width);
				
				float mReqPercentages = 14;
				float mCalculatedWidth = (mReqPercentages / 100) * width;

				
				holder.image.getLayoutParams().height = (int) (mCalculatedWidth);
				holder.image.getLayoutParams().width = (int) mCalculatedWidth;
				/* int density = getResources().getDisplayMetrics().densityDpi;
				  switch (density) {
				  case DisplayMetrics.DENSITY_LOW:
					  holder.image.getLayoutParams().height = 40;
					  holder.image.getLayoutParams().width = 40;
					 
					  break;
				  case DisplayMetrics.DENSITY_MEDIUM:
					  holder.image.getLayoutParams().height = 85;
					  holder.image.getLayoutParams().width = 85;
					  break;
				  case DisplayMetrics.DENSITY_HIGH:
					  holder.image.getLayoutParams().height = 85;
					  holder.image.getLayoutParams().width = 85;
					  break;
				  case DisplayMetrics.DENSITY_XHIGH:
					  holder.image.getLayoutParams().height = 120;
					  holder.image.getLayoutParams().width = 120;
					  break;
				  case DisplayMetrics.DENSITY_XXHIGH:
					  holder.image.getLayoutParams().height = 120;
					  holder.image.getLayoutParams().width = 120;
					  break;
				  }*/
				
			   
			   return convertView;
	
			  }
			 
		 }
		  private class ViewHolders {
			   TextView code;
			   CheckBox radio;
			   ImageView image;
			  }
		 
}
