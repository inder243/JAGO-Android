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

import com.iapp.playdate.R;
import com.iapp.playdate.Parent_profile.LazyAdapter;
import com.iapptechnologies.time.util.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class Sets_friendlist extends android.support.v4.app.Fragment {
	ListView list_friend;
	String response,check,url_sets,sresponse,facebookfriends,setid;
	ArrayList<Getcatagory_for_sets>getcat_for_sets;
	ArrayList<Getcategory>child_info_list;
	LazyAdapter adapter;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	public Sets_friendlist(){
		
	}
	 @Override
		public View onCreateView(final LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ViewGroup view = (ViewGroup) inflater.inflate(R.layout.sets_fragment_list_1, container,
	                false);
			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			cd = new ConnectionDetector(getActivity());
			
			settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
			editor = settings.edit();

			isInternetPresent = cd.isConnectingToInternet();

			list_friend=(ListView)view.findViewById(R.id.listView_sets);
			list_friend.setDivider(null);
			list_friend.setDividerHeight(0);
			try {
				response=getArguments().getString("response_data");
				check=getArguments().getString("check");
				facebookfriends=settings.getString("FBFRIEND","");
				//facebookfriends=getArguments().getString("facebook_friends");
			} catch (Exception e) {
				// TODO: handle exception
			}
		
			if(check.equals("0")){
			jsonparsing(response);
			}
			else if(check.equals("1")){
				if(isInternetPresent){
					new Get_Sets().execute();
				}else{
					Toast.makeText(getActivity(), "Please check internet connection", 2000).show();
				}
				
			}
			else if(check.equals("2")){
				try {
					if(isInternetPresent){
						setid=getArguments().getString("set_id");
						new get_set_by_id().execute();
					}else{
						Toast.makeText(getActivity(), "Please check internet connection", 2000).show();
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			
			list_friend.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					ArrayList<Getcatagory_for_sets>getcat_for_sets_listclick;
					getcat_for_sets_listclick=getcat_for_sets;
					ArrayList<Getcategory>childinfo;
					
					
					
					String parentname=getcat_for_sets_listclick.get(arg2).parent_name;
					String parentid=getcat_for_sets_listclick.get(arg2).parent_id;
					//String parentfreetime=getcat_for_sets_listclick.get(arg2).parent_freetime;
					String parentdob=getcat_for_sets_listclick.get(arg2).parent_date_ofbirth;
					String parentpic=getcat_for_sets_listclick.get(arg2).parent_profilepic;
					String parent_type=getcat_for_sets_listclick.get(arg2).parent_type;
					String location=getcat_for_sets_listclick.get(arg2).parent_location;
					
					childinfo=getcat_for_sets_listclick.get(arg2).childinfo;
					GlobalVariable.global_list_child=childinfo;
					Bundle bundle = new Bundle();
					bundle.putString("name", parentname);
					bundle.putString("url", parentpic);
					//bundle.putString("freetime", parentfreetime);
					bundle.putString("dob", parentdob);
					bundle.putString("guardiantype", parent_type);
					bundle.putString("location", location);
					/*bundle.putString("location", parent_location);
					bundle.putString("firstname", parent_name);
					bundle.putString("facebook_id", fb_id);
					bundle.putString("user_guardian_id", guardian_id);*/
					//bundle.putStringArrayList("childinfo", childinfo);
					
					android.support.v4.app.Fragment  fragment=new Friend_profile();
			        fragment.setArguments(bundle);
					/*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
					        .replace(R.id.content_frame, fragment).commit();*/
			        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);
					fragmentTransaction.addToBackStack("first18");
					fragmentTransaction.commit();
				
					
					
				
					

				}
			});
	 
			return view;
			 
	 	   
	 }
	 public void jsonparsing(String response1) {
		 System.out.println(response1);
		 ProgressDialog dialog = null;
		 try {
			 dialog=new ProgressDialog(getActivity());
			 dialog.setMessage("Loading....Please wait");
				dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		Getcatagory_for_sets getcatagory_for_sets=null;
		Getcategory childdetaillist=null;
		getcat_for_sets=new ArrayList<Getcatagory_for_sets>();
		
		try {
			JSONObject json=new JSONObject(response1);
			JSONArray jArray=json.getJSONArray("data");
			if(jArray.length()>0){
				for(int i=0;i<jArray.length();i++){
					child_info_list=new ArrayList<Getcategory>();
					JSONObject getdetail=jArray.getJSONObject(i);
					JSONObject friendinfo=getdetail.getJSONObject("friendinfo");
					getcatagory_for_sets= new Getcatagory_for_sets();
					getcatagory_for_sets.parent_name=friendinfo.getString("name");
					getcatagory_for_sets.parent_id=friendinfo.getString("g_id");
					String id_parent=friendinfo.getString("g_id");
					getcatagory_for_sets.parent_profilepic=friendinfo.getString("profile_image");
					getcatagory_for_sets.parent_date_ofbirth=friendinfo.getString("dob");
					//getcatagory_for_sets.parent_freetime=friendinfo.getString("set_fixed_freetime");
					getcatagory_for_sets.parent_type=friendinfo.getString("guardian_type");
					getcatagory_for_sets.parent_location=friendinfo.getString("location");
					try {
						JSONArray child=getdetail.getJSONArray("childinfo");
						for(int j=0;j<child.length();j++){
							JSONObject child_detail= child.getJSONObject(j);
							childdetaillist=new Getcategory();
							childdetaillist.guardian_child=id_parent;
							childdetaillist.child_name=child_detail.getString("name");
							childdetaillist.child_id=child_detail.getString("child_id");
							//childdetaillist.profile_image=child_detail.getString("profile_image");
							childdetaillist.date_of_birth=child_detail.getString("dob");
							childdetaillist.free_time=child_detail.getString("set_fixed_freetime");
							childdetaillist.allergies_child=child_detail.getString("allergies");
							
							//childdetaillist.hobbies_child=child_detail.getString("hobbies");
							//childdetaillist.condition_child=child_detail.getString("dob");
							//childdetaillist.school_child=child_detail.getString("school");
							//childdetaillist.youthclub_child=child_detail.getString("youth_club");
							if(child_detail.getString("child_id").equals("")||child_detail.getString("child_id").equals(null)){
								break;
							}else{
								child_info_list.add(childdetaillist);
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
						
					
						
					
					getcatagory_for_sets.childinfo=child_info_list;
					getcat_for_sets.add(getcatagory_for_sets);
					
					
				}
				try {
					if(dialog!=null || dialog.isShowing()){
						dialog.dismiss();
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				adapter = new LazyAdapter(getActivity(), getcat_for_sets);
				list_friend.setAdapter(adapter);
			}
			
			else{
				dialog.dismiss();
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				dialog.dismiss();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
		}
		
		
	}
	public class LazyAdapter extends BaseAdapter {
			String _imgurl = "";
			private Activity activity;
			private ArrayList<Getcatagory_for_sets> _items;
			private LayoutInflater inflater = null;

			

			public ImageLoader imageLoader;

			

			public LazyAdapter(Activity activity, ArrayList<Getcatagory_for_sets> getcat_for_sets) {

				
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
				String name_of_child=_items.get(position).parent_name;
				name_of_child=name_of_child.toUpperCase();
				_holder.event_title.setText(name_of_child);
				
				_imgurl = _items.get(position).parent_profilepic;
				
				Log.d("", "_imgurl" + _imgurl);
				Log.d("", "_imgurl" + _imgurl);
				Log.d("", "_imgurl" + _imgurl);
				Log.d("", "_imgurl" + _imgurl);
				Log.d("", "_imgurl" + _imgurl);
				_holder._image.setTag(_imgurl);
				imageLoader.DisplayImage(_imgurl,_holder._image);
				
				_holder._image.requestLayout();
                Display display = getActivity().getWindowManager().getDefaultDisplay();
				
				int height = display.getHeight();
				int width = display.getWidth();
				
				System.out.println(height  +  "   "+width);
				
				float mReqPercentages = 17;
				float mCalculatedWidth = (mReqPercentages / 100) * width;

				
				_holder._image.getLayoutParams().height = (int) (mCalculatedWidth);
				_holder._image.getLayoutParams().width = (int) mCalculatedWidth;
				/* int density = getResources().getDisplayMetrics().densityDpi;
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
	public class Get_Sets extends AsyncTask<String, Integer, String>{
		ProgressDialog dialog= new ProgressDialog(getActivity());
				
				@Override
				protected void onPreExecute() {
						// Toast.makeText(Login.this,"asynch task",Toast.LENGTH_LONG).show();
						
						dialog.show();
						dialog.setMessage("Loading.......please wait");
						url_sets=Constants.FACEBOOK_FRIEND_DETAIL;//"http://54.191.67.152/playdate/facebook_friend_detail.php";
						//http://112.196.34.179/playdate/facebook_friend_detail.php?friend_fbid=%27100004938971287%27,%27100001678200547%27
				}

				
				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub
					HttpClient httpClient = new DefaultHttpClient();
			        HttpContext localContext = new BasicHttpContext();
			        HttpPost httpPost = new HttpPost(url_sets);
			      
			    

			        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			        nameValuePairs.add(new BasicNameValuePair("friend_fbid",facebookfriends));
			       System.out.println("facebook issue......................"+facebookfriends);

					
					try {
						httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

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
				

			        try {
			        	sresponse = reader.readLine();
						
						System.out.println("response.................................."+sresponse);
			        } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        JSONObject json = null;
					try {
						 json=new JSONObject(sresponse);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					/*try {
						JSONArray jArray=json.getJSONArray("data");
						Log.d("fdgn", "dsgfbkj"+jArray);
						
						
						for(int i=0;i<jArray.length();i++){
							JSONObject getdetail=jArray.getJSONObject(i);
							JSONObject friendinfo=getdetail.getJSONObject("friendinfo");
							String name=friendinfo.getString("name");
							System.out.println(name);
						
						}
						
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					
					return null;
				}
				protected void onPostExecute(String resultt) {
					

					dialog.dismiss();
					jsonparsing(sresponse);
					
					}
			}
	
	public class get_set_by_id extends AsyncTask<String, Integer, String>{
		ProgressDialog dialog= new ProgressDialog(getActivity());
				
				@Override
				protected void onPreExecute() {
						// Toast.makeText(Login.this,"asynch task",Toast.LENGTH_LONG).show();
						
						dialog.show();
						dialog.setMessage("Loading.......please wait");
						url_sets=Constants.SETS_BY_ID;//"http://54.191.67.152/playdate/set_friend_child.php";
						//http://112.196.34.179/playdate/set_friend_child.php?&set_id=95&friend_fbid=%27123%27,%2724%27
				}

				
				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub
					HttpClient httpClient = new DefaultHttpClient();
			        HttpContext localContext = new BasicHttpContext();
			        HttpPost httpPost = new HttpPost(url_sets);
			      
			    
                    String guardian_id=GlobalVariable.guardian_Id;
			        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			        nameValuePairs.add(new BasicNameValuePair("guardian_id",guardian_id));
			        nameValuePairs.add(new BasicNameValuePair("set_id",setid));
			        nameValuePairs.add(new BasicNameValuePair("friend_fbid",facebookfriends));
			        
			        StringBuilder sbb = new StringBuilder();

					sbb.append("http://54.191.67.152/playdate/set_friend_child.php?");
					sbb.append(nameValuePairs.get(0)+"&");
					sbb.append(nameValuePairs.get(1)+"&");
					sbb.append(nameValuePairs.get(2));
					System.out.println(sbb);
			        System.out.println(setid);
			       System.out.println(facebookfriends);

					
					try {
						httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

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
				

			        try {
			        	sresponse = reader.readLine();
						
						System.out.println("response................11111.................."+sresponse);
			        } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        JSONObject json = null;
					try {
						 json=new JSONObject(sresponse);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					/*try {
						JSONArray jArray=json.getJSONArray("data");
						Log.d("fdgn", "dsgfbkj"+jArray);
						
						
						for(int i=0;i<jArray.length();i++){
							JSONObject getdetail=jArray.getJSONObject(i);
							JSONObject friendinfo=getdetail.getJSONObject("friendinfo");
							String name=friendinfo.getString("name");
							System.out.println(name);
						
						}
						
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					
					return null;
				}
				protected void onPostExecute(String resultt) {
					

					dialog.dismiss();
					jsonparsing(sresponse);
					
					}
			}
}

