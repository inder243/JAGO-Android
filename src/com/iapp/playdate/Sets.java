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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iapp.playdate.R;
import com.iapptechnologies.time.util.Constants;

public class Sets extends android.support.v4.app.Fragment {

	String user_guardian_id, facebook_friends, url_sets, setid;
	int count_friend = 0;
	String sResponse = null, sResponse1 = null;
	Button btn_View;
	TextView txt_no_of_friend;
	ListView list_sets;
	int size;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	Button add_sets_btn;
	ArrayList<Getcategory> sets_detail;
	LazyAdapter adapter;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	public Sets() {

	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
	
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.sets, container,
				false);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
		editor = settings.edit();

		
		btn_View = (Button) view.findViewById(R.id.button_view_sets);
		txt_no_of_friend = (TextView) view
				.findViewById(R.id.textView_members_sets);
		add_sets_btn = (Button) view.findViewById(R.id.button_add_sets);
		list_sets = (ListView) view.findViewById(R.id.listView_sets_display);
		
		user_guardian_id=settings.getString("USERID","");
		
		facebook_friends=settings.getString("FBFRIEND","");
		
		/*try {
			user_guardian_id = getArguments().getString("user_guardian_id");
			facebook_friends = getArguments().getString("facebook_friends");
		} catch (Exception e) {
			// TODO: handle exception
		}*/

		cd = new ConnectionDetector(getActivity());
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
			new Get_Sets().execute();
			new get_total_sets().execute();
		} else {
			Toast.makeText(getActivity(), "Please check internet connection",
					2000).show();

		}
		sets_detail = new ArrayList<Getcategory>();
		add_sets_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("check", "0");
			//	bundle.putString("user_guardian_id", user_guardian_id);
			//	bundle.putString("facebook_friends", facebook_friends);
				android.support.v4.app.Fragment fragment = new Add_Sets();
				fragment.setArguments(bundle);
				/*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();*/
				  android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);
					fragmentTransaction.addToBackStack("first19");
					fragmentTransaction.commit();
			}
		});

		btn_View.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (count_friend > 0) {
					Bundle bundle = new Bundle();
					bundle.putString("response_data", sResponse);
					bundle.putString("check", "0");
				//	bundle.putString("facebook_friends", facebook_friends);
					android.support.v4.app.Fragment fragment = new Sets_friendlist();
					fragment.setArguments(bundle);
					/*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();*/
					  android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
						android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
						fragmentTransaction.replace(R.id.content_frame, fragment);
						fragmentTransaction.addToBackStack("first20");
						fragmentTransaction.commit();
				}
			}
		});
		return view;

	}

	public class Get_Sets extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());

		@Override
		protected void onPreExecute() {
			// Toast.makeText(Login.this,"asynch task",Toast.LENGTH_LONG).show();

			dialog.show();
			dialog.setMessage("Loading.......please wait");
			url_sets = Constants.FACEBOOK_FRIEND_DETAIL;//"http://54.191.67.152/playdate/facebook_friend_detail.php";
			// http://54.191.67.152/playdate/facebook_friend_detail.php?friend_fbid=%27100004938971287%27,%27100001678200547%27
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

			try {
				sResponse = reader.readLine();

				System.out.println("response.................................."
						+ sResponse);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (sResponse.equals(null) || sResponse.equals("")) {

			} else {
				JSONObject json = null;
				try {
					json = new JSONObject(sResponse);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					count_friend=0;
					JSONArray jArray = json.getJSONArray("data");
					Log.d("fdgn", "dsgfbkj" + jArray);
					size = jArray.length();
					System.out.println("0000000000000+++++++" + size);
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject getdetail = jArray.getJSONObject(i);
						JSONObject friendinfo = getdetail
								.getJSONObject("friendinfo");
						String name = friendinfo.getString("name");
						System.out.println(name);
						count_friend = count_friend + 1;
						System.out
								.println("dfsgkfj       ....." + count_friend);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		protected void onPostExecute(String resultt) {
			String friendnumber = String.valueOf(count_friend);

			txt_no_of_friend.setText(friendnumber);

			dialog.dismiss();

		}
	}

	public class get_total_sets extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());

		@Override
		protected void onPreExecute() {
			// Toast.makeText(Login.this,"asynch task",Toast.LENGTH_LONG).show();

			dialog.show();
			dialog.setMessage("Loading.......please wait");
			url_sets = Constants.GET_TOTAL_SETS;//"http://54.191.67.152/playdate/setting_set_friend.php";// ?g_id=47
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url_sets);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("g_id", user_guardian_id));

			StringBuilder sbb = new StringBuilder();

			sbb.append("http://54.191.67.152/playdate/setting_set_friend.php?");
			sbb.append(nameValuePairs.get(0));
			System.out.println(sbb);

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
				sResponse1 = reader.readLine();

				System.out
						.println("response11111111111111.................................."
								+ sResponse1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject json = null;
			try {
				json = new JSONObject(sResponse1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				JSONArray jArray = json.getJSONArray("data");
				Log.d("fdgn", "dsgfbkj" + jArray);
				size = jArray.length();
				System.out.println("0000000000000+++++++" + size);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject getdetail = jArray.getJSONObject(i);
					if(getdetail.getString("total_count").equals("0")){
						
					}else{
					
					Getcategory grtcat = new Getcategory();
					grtcat.set_id = getdetail.getString("set_id");
					grtcat.set_number = getdetail.getString("total_count");
					grtcat.child_id = getdetail.getString("g_id");
					grtcat.set_name = getdetail.getString("set_name");
					sets_detail.add(grtcat);
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String resultt) {

			dialog.dismiss();
			adapter = new LazyAdapter(getActivity(), sets_detail);
			list_sets.setAdapter(adapter);

		}
	}

	public class LazyAdapter extends BaseAdapter {
		String _imgurl = "";
		private Activity activity;
		private ArrayList<Getcategory> _items;
		private LayoutInflater inflater = null;
		// public TextView event_title, total_number;
		// public Button view,edit;

		protected ListView mListView;
		public ImageLoader imageLoader;

		public LazyAdapter(Activity activity,
				ArrayList<Getcategory> getcat_for_sets) {

			this.activity = activity;
			this._items = getcat_for_sets;
			try {
				inflater = (LayoutInflater) getActivity().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			imageLoader = new ImageLoader(getActivity());
			this.mListView = list_sets;
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
			public TextView event_title, total_number;
			public Button view,edit;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
		/*	View row = convertView;
			if (row == null) {
				// LayoutInflater inflater = (LayoutInflater)
				// this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.show_sets_list, parent, false);
			} else {
				row = convertView;
			}
			TextView event_title = (TextView) row
					.findViewById(R.id.textView_freiends_sets_list);
			TextView total_number = (TextView) row
					.findViewById(R.id.textView_members_sets_list);
			Button view = (Button) row.findViewById(R.id.button_view_sets_list);
			Button edit = (Button) row.findViewById(R.id.button_edit_sets_list);
			view.setOnClickListener(sendlistener);
			edit.setOnClickListener(editlistener);
			String admin_id = this._items.get(position).child_id;
			if (admin_id.equals(GlobalVariable.guardian_Id)) {

			} else {
				// this.edit.setVisibility(View.INVISIBLE);
				edit.setClickable(false);
				// Toast.makeText(getActivity(),
				// "You do not have permission to edit", 2000).show();
			}
			String name_of_event = this._items.get(position).set_name;

			event_title.setText(name_of_event.toUpperCase());

			String number = this._items.get(position).set_number;
			total_number.setText(number);*/
			
			 
			  ViewHolder _holder=null; 
			  convertView = null;
			  if (convertView == null) { 
				  convertView = inflater .inflate(R.layout.show_sets_list, null); 
				  _holder = new ViewHolder();
			  
				  _holder.event_title = (TextView) convertView.findViewById(R.id.textView_freiends_sets_list);
			  
			  _holder.total_number = (TextView) convertView .findViewById(R.id.textView_members_sets_list);
			  _holder.view = (Button) convertView .findViewById(R.id.button_view_sets_list);
			  _holder.edit = (Button) convertView .findViewById(R.id.button_edit_sets_list);
			 
			  _holder.view.setOnClickListener(sendlistener);
			  _holder.edit.setOnClickListener(editlistener);
			  convertView.setTag(_holder);
			  }
			 /* else
			  {
				  _holder = (ViewHolder)convertView.getTag();
				  }*/
			  String admin_id=_items.get(position).child_id;
			  if(admin_id.equals(GlobalVariable.guardian_Id)){
			  
			  }else
			  {
				  _holder.edit.setClickable(false);
				  _holder.edit.setVisibility(View.INVISIBLE);
				  }
			  String name_of_event=_items.get(position).set_name;
			  
			  _holder.event_title.setText(name_of_event.toUpperCase());
			  
			  String number=_items.get(position).set_number;
			  _holder.total_number.setText(number);
			 
			return convertView;
		}

		private OnClickListener sendlistener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				int position_1 = mListView.getPositionForView((View) v
						.getParent());
				System.out.println("position.............." + position_1);
				setid = sets_detail.get(position_1).set_id;
				Bundle bundle = new Bundle();
				bundle.putString("response_data", sResponse1);
				bundle.putString("check", "2");
			///	bundle.putString("facebook_friends", facebook_friends);
				bundle.putString("set_id", setid);
				android.support.v4.app.Fragment fragment = new Sets_friendlist();
				fragment.setArguments(bundle);
				  android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);
					fragmentTransaction.addToBackStack("first121");
					fragmentTransaction.commit();
				/*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
*/
			}
		};
		private OnClickListener editlistener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				int position_1 = mListView.getPositionForView((View) v
						.getParent());
				System.out.println("position.............." + position_1);
				setid = sets_detail.get(position_1).set_id;
				String setname = sets_detail.get(position_1).set_name;
				Bundle bundle = new Bundle();
				bundle.putString("setname", setname);
				bundle.putString("setid", setid);
				bundle.putString("check", "1");
				//bundle.putString("user_guardian_id", user_guardian_id);
			//	bundle.putString("facebook_friends", facebook_friends);
				android.support.v4.app.Fragment fragment = new Add_Sets();
				fragment.setArguments(bundle);
				  android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);
					fragmentTransaction.addToBackStack("first122");
					fragmentTransaction.commit();
				/*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();*/

			}
		};
	}
}
