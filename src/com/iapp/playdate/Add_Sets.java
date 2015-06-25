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

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iapp.playdate.R;
import com.iapptechnologies.time.util.Constants;

public class Add_Sets extends android.support.v4.app.Fragment {

	EditText name_of_sets_edt;
	ListView list_friends;
	 Boolean isInternetPresent = false;
	    ConnectionDetector cd;
	Button btn_save;
	String sresponse, url_sets, facebookfriends,friends_id_for_setcreate="",user_id,name_of_set,setid,setname,check,edit_create_sets;
	
	MyCustomAdapter dataAdapter = null;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	ArrayList<String>friend_id_list=new ArrayList<String>();
	
	public Add_Sets() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.add_new_sets,container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		 Home.menu.setVisibility(View.GONE);
		name_of_sets_edt = (EditText) view.findViewById(R.id.editText_addsets_name);
		list_friends = (ListView) view.findViewById(R.id.listView_add_sets_list);
		btn_save = (Button) view.findViewById(R.id.button_save_sets);
		inflater=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
		editor = settings.edit();
		try {
			check=getArguments().getString("check");
			// user_id = getArguments().getString("user_guardian_id");
			//facebookfriends = getArguments().getString("facebook_friends");
		} catch (Exception e) {
			// TODO: handle exception
		}
		 cd=new ConnectionDetector(getActivity());
		 isInternetPresent = cd.isConnectingToInternet();
		 
			user_id=settings.getString("USERID","");
			facebookfriends=settings.getString("FBFRIEND","");
		 
		if(check.equals("1")){
			 setid=getArguments().getString("setid");
			 setname = getArguments().getString("setname");
			 name_of_sets_edt.setText(setname.toUpperCase());
			 name_of_sets_edt.setEnabled(false);
			 if (isInternetPresent) {
				 edit_create_sets="1";
				 new get_sets_friends().execute();
			 }
			 else{
				 Toast.makeText(getActivity(),"Please check internet connection",2000).show();
				
			 }
			
		}else if(check.equals("0"))
		{
			if (isInternetPresent) {
				edit_create_sets="0";
				 new Get_Sets().execute();
					
					
			 }
			 else{
				 Toast.makeText(getActivity(),"Please check internet connection",2000).show();
				
			 }
		}
		 
		
		 btn_save.setOnClickListener(new OnClickListener() {
			 
			   @Override
			   public void onClick(View v) {
			 
			   for(int i=0;i<friend_id_list.size();i++){
				   String id=friend_id_list.get(i);
				   System.out.println("iddddddd...."+id);
				   if(friends_id_for_setcreate.equals("")){
					   friends_id_for_setcreate=id;
				   }else{
				   friends_id_for_setcreate=friends_id_for_setcreate+","+id;
				   }
			   }
			   cd=new ConnectionDetector(getActivity());
				 isInternetPresent = cd.isConnectingToInternet();
				 if (isInternetPresent) {
				
					 if(edit_create_sets.equals("0")){
						 if(name_of_sets_edt.getText().toString().length()==0)
						 {
						 Toast.makeText(getActivity(),"Please enter text",2000).show();  
						 }else if(friends_id_for_setcreate.length()==0)
						 {
						 Toast.makeText(getActivity(),"Please select checkbox",2000).show();  
						 }else
						 {
							 if(name_of_sets_edt.getText().toString().length()>0 && friends_id_for_setcreate.length()>0)
							 {
								 name_of_set=name_of_sets_edt.getText().toString();
								 url_sets = Constants.SETS_FRIENDS_LIST;//"http://54.191.67.152/playdate/set_friend.php";
								 new create_sets().execute();
							 }else
							 {
								 Toast.makeText(getActivity(),"Please enter name of set",2000).show(); 
							 }
						 }
					 	 }
					 else
					 {
						 if(name_of_sets_edt.getText().toString().length()==0)
						 {
						 Toast.makeText(getActivity(),"Please enter text",1).show();  
						 }else if(friends_id_for_setcreate.length()==0)
						 {
						 Toast.makeText(getActivity(),"Please select checkbox",1).show();  
						 }else
						 {
							 if(name_of_sets_edt.getText().toString().length()>0 && friends_id_for_setcreate.length()>0)
							 {
								 url_sets = Constants.SETS_EDIT;//"http://54.191.67.152/playdate/edit_set.php";//?set_id=106&friend_id=107";
								 new create_sets().execute();
							 }else
							 {
							 Toast.makeText(getActivity(),"Please select child",2000).show();  
							 }
						 }
					 }
					 
//					 else if(edit_create_sets.equals("1")){
//					 		 
//					 		 if(name_of_sets_edt.getText().toString().length()>0 && friends_id_for_setcreate.length()>0)
//					 		 {
//					 			 url_sets = "http://54.191.67.152/playdate/edit_set.php";//?set_id=106&friend_id=107";
//								 new create_sets().execute();
//					 		 }
//					 		else
//							 {
//								 Toast.makeText(getActivity(),"Please select child",2000).show();  
//							 }
//					 }	
				 }
				 else{
				 Toast.makeText(getActivity(),"Please check internet connection",2000).show();
				 }
			   }
			  });
		 
		 
		return view;
	}

	    public class Get_Sets extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());
        String friend_name,friend_id,friend_imageurl;
        ArrayList<arrayList>al=new ArrayList<Add_Sets.arrayList>();
		@Override
		protected void onPreExecute() {
			
			dialog.show();
			dialog.setMessage("Loading.......please wait");
			url_sets = Constants.FACEBOOK_FRIEND_DETAIL;//"http://54.191.67.152/playdate/facebook_friend_detail.php";
			// http://112.196.34.179/playdate/facebook_friend_detail.php?friend_fbid=%27100004938971287%27,%27100001678200547%27
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url_sets);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("friend_fbid",facebookfriends));
			System.out.println(facebookfriends);

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

				JSONArray jArray = json.getJSONArray("data");
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject getdetail = jArray.getJSONObject(i);
					JSONObject friendinfo = getdetail
							.getJSONObject("friendinfo");
					
					friend_name=friendinfo.getString("name");
					System.out.println("friendinfo"+friend_name);
					
					friend_id=friendinfo.getString("g_id");
					
					friend_imageurl=friendinfo.getString("profile_image");
				
					
					arrayList arrlist =new arrayList(friend_imageurl,friend_name,friend_id, false);
			          al.add(arrlist);
				
				}
				
				 
			} catch (Exception e) {

			}
			return null;
		}

		protected void onPostExecute(String resultt) {

			dialog.dismiss();
			
			dataAdapter = new MyCustomAdapter(getActivity(), al);/*(getActivity(),R.layout.list_add_sets,al);*/
		    
		    list_friends.setAdapter(dataAdapter);
		}
	}
	public class get_sets_friends extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());
String friend_name,friend_id,friend_imageurl;
ArrayList<arrayList>al=new ArrayList<Add_Sets.arrayList>();

		@Override
		protected void onPreExecute() {
			
			dialog.show();
			dialog.setMessage("Loading.......please wait");
			url_sets = Constants.SETS_FRIEND;//"http://54.191.67.152/playdate/get_friend_ids_by_set.php";//?set_id=105&friend_id=106,113,107
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url_sets);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("friend_id",
					facebookfriends));
			nameValuePairs.add(new BasicNameValuePair("set_id",
					setid));
			System.out.println(setid);
			System.out.println(facebookfriends);

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

				JSONArray jArray = json.getJSONArray("data");
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject getdetail = jArray.getJSONObject(i);
					JSONObject friendinfo = getdetail
							.getJSONObject("friendinfo");
					
					friend_name=friendinfo.getString("name");
					System.out.println("friendinfo"+friend_name);
					
					friend_id=friendinfo.getString("g_id");
					
					friend_imageurl=friendinfo.getString("profile_image");
				boolean checked=friendinfo.getBoolean("checked");
					
					arrayList arrlist =new arrayList(friend_imageurl,friend_name,friend_id, checked);
			          al.add(arrlist);
				
				}
				
				 
			} catch (Exception e) {

			}
			return null;
		}

		protected void onPostExecute(String resultt) {

			dialog.dismiss();
			
			dataAdapter = new MyCustomAdapter(getActivity(), al);/*(getActivity(),R.layout.list_add_sets,al);*/
		    
		    list_friends.setAdapter(dataAdapter);
		}
	}
	
	public class create_sets extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());
String success;
		@Override
		protected void onPreExecute() {
			// Toast.makeText(Login.this,"asynch task",Toast.LENGTH_LONG).show();

			dialog.show();
			dialog.setMessage("Loading.......please wait");
			
			//http://112.196.34.179/playdate/edit_set.php?set_id=106&friend_id=107      editset
			// http://112.196.34.179/playdate/set_friend.php?set_name=setC&friend_id=100000590232251,100006707370606&g_id=47
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url_sets);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if(edit_create_sets.equals("0")){
			nameValuePairs.add(new BasicNameValuePair("set_name",name_of_set));
			nameValuePairs.add(new BasicNameValuePair("friend_id",friends_id_for_setcreate));
			nameValuePairs.add(new BasicNameValuePair("g_id",user_id));
			

			 StringBuilder sbb = new StringBuilder();

				sbb.append("http://54.191.67.152/playdate/set_friend.php?");
				sbb.append(nameValuePairs.get(0)+"&");
				sbb.append(nameValuePairs.get(1)+"&");
				
				sbb.append(nameValuePairs.get(2));
				System.out.println(sbb);
				
			}else if(edit_create_sets.equals("1")){
				nameValuePairs.add(new BasicNameValuePair("set_id",setid));
				nameValuePairs.add(new BasicNameValuePair("friend_fbid",friends_id_for_setcreate));
			}
			
			
			System.out.println("........."+friends_id_for_setcreate);

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
				if(edit_create_sets.equals("0")){
					name_of_sets_edt.setText("");
					
					Toast.makeText(getActivity(), "Set Created", 2000).show();
					}else if(edit_create_sets.equals("1")){
						friends_id_for_setcreate="";
						friend_id_list.clear();
						new get_sets_friends().execute();
						Toast.makeText(getActivity(), "Set Updated", 2000).show();
					}
			
				
			}else{
				Toast.makeText(getActivity(), "Please try again later", 2000).show();
			}
			
		}
	}
	
	private class MyCustomAdapter extends BaseAdapter{ /*ArrayAdapter<arrayList> {
*/
		  /* private ArrayList<arrayList> al;
            ImageLoader imageLoader=new ImageLoader(getActivity());
		   public MyCustomAdapter(Context context, int textViewResourceId,ArrayList<arrayList> al) {
		   super(context, textViewResourceId, al);
		   this.al = new ArrayList<arrayList>();
		   this.al.addAll(al);*/
		private Activity activity;
		private ArrayList<arrayList> al;
		private LayoutInflater inflater = null;

			public ImageLoader imageLoader=new ImageLoader(getActivity());

		/*public MyCustomAdapter(FragmentActivity activity2,
					ArrayList<arrayList> al2) {
				// TODO Auto-generated constructor stub
			}*/

		public MyCustomAdapter(FragmentActivity activity,
					ArrayList<arrayList> al) {
				// TODO Auto-generated constructor stub
			this.activity = activity;
			 this.al = new ArrayList<arrayList>();
			   this.al.addAll(al);
			try {
				inflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			} catch (Exception e) {
				// TODO: handle exception
			}
			}

		/*public void MyCustomAdapter(Activity activity, ArrayList<arrayList> parentItems) {

			// this.imageLoader.clearCache();
			
			
			//imageLoader = new ImageLoader(getActivity());
		}*/

		public int getCount() {
			return al.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}
		  

		  private class ViewHolder {
		   TextView code;
		   CheckBox name;
		   ImageView image;
		  }

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {

		   ViewHolder holder = null;
		   Log.v("ConvertView", String.valueOf(position));

		   if (convertView == null) {
		   // LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    convertView = inflater.inflate(R.layout.list_add_sets, null);

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
		      
		       String search=cb.getText().toString();
		       System.out.println("search..."+search);
		       friend_id_list.add(search);

		      } else {
		     
		       String search=cb.getText().toString();
		       friend_id_list.remove(search);
		      }
		     /* Toast.makeText(getActivity(),
				        "Checkbox " + cb.getText() ,
				        Toast.LENGTH_LONG).show();*/
		     
		      al1.setSelected(cb.isChecked());
		     }
		    });
		   } else {
		    holder = (ViewHolder) convertView.getTag();
		   }

		   
		   arrayList al1 = al.get(position);
		   holder.code.setText(al1.getCode());
		   holder.name.setText(al1.getName());
		   holder.name.setChecked(al1.isSelected());
		   if(edit_create_sets.equals("0")){
				
				}else if(edit_create_sets.equals("1")){
					 if(holder.name.isChecked()){
						   System.out.println("checked");
						   if(friend_id_list.contains(al1.getName())){
							   
						   }else{
						   friend_id_list.add(al1.getName());
						   }
					   }
				}
		
		  
		   holder.name.setTag(al1);
           String _imgurl=al1.getimage();
		   imageLoader.DisplayImage(_imgurl,holder.image);
			
			holder.image.requestLayout();
			 int density = getResources().getDisplayMetrics().densityDpi;
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
			  }
			
		   
		   return convertView;

		  }

		 }

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
	/*public class friends {

		String id = null;
		String name = null;
		String image = null;
		boolean selected = false;

		public friends( String name,String code, boolean selected) {
			super();
			this.id = code;
			this.name = name;
			this.image = image;
			this.selected = selected;
		}

		public String getid() {
			return id;
		}

		public void setid(String id) {
			this.id = id;
		}

		public String getImage() {
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
	 private class MyCustomAdapter extends ArrayAdapter<friends> {
		 
		  private ArrayList<friends> friendlist;
		 
		  public MyCustomAdapter(Context context, int textViewResourceId, 
		    ArrayList<friends> friendlist) {
		   super(context, textViewResourceId, friendlist);
		   this.friendlist = new ArrayList<friends>();
		   this.friendlist.addAll(friendlist);
		  }
		 
		  private class ViewHolder {
		   TextView name;
		   CheckBox click;
		   ImageView image_friend;
		  }
		 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		 
		   ViewHolder holder = null;
		   Log.v("ConvertView", String.valueOf(position));
		 
		   if (convertView == null) {
		   LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(
		     Context.LAYOUT_INFLATER_SERVICE);
		   convertView = vi.inflate(R.layout.list_add_sets, null);
		 
		   holder = new ViewHolder();
		   holder.name = (TextView) convertView.findViewById(R.id.textView_list_addsets_name);
		   holder.click = (CheckBox) convertView.findViewById(R.id.checkBox_addsets);
		  // holder.image_friend = (ImageView) convertView.findViewById(R.id.imageView1_addsets);
		   convertView.setTag(holder);
		 
		    holder.click.setOnClickListener( new View.OnClickListener() {  
		     public void onClick(View v) {  
		      CheckBox cb = (CheckBox) v ;  
		      friends friend = (friends) cb.getTag();  
		      Toast.makeText(getActivity(), "Clicked on Checkbox: " + cb.getText() +" is " + cb.isChecked(),Toast.LENGTH_LONG).show();
		      friend.setSelected(cb.isChecked());
		     }  
		    	 CheckBox cb = (CheckBox) v;
			      friends al1 = (friends) cb.getTag();
			      String str;
			     
			      if (cb.isChecked()) {
			       str = "Selected";
			       String search=cb.getText().toString();
			     

			      } else {
			       str = "Not selected";
			       String search=cb.getText().toString();
			      
			      }
			      Toast.makeText(getActivity(),
					        "Checkbox " + cb.getText() + " is " + str,
					        Toast.LENGTH_LONG).show();
		     }
		    });  
		   } 
		   else {
		    holder = (ViewHolder) convertView.getTag();
		   }
		 
		   friends friend = friendlist.get(position);
		   holder.name.setText(" (" +  friend.getName() + ")");
		  
		   holder.click.setChecked(friend.isSelected());
		   holder.click.setTag(friend);
		 
		   return convertView;
		 
		  }
		 
		 }*/
		 
		
		 
		

}


