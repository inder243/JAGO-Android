package com.iapp.playdate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iapp.playdate.R;
import com.iapptechnologies.time.models.NotificationModels;
import com.iapptechnologies.time.util.Constants;
import com.iapptechnologies.time.util.DataBaseSqlliteHelper;

public class notification_fragement extends android.support.v4.app.Fragment {
	DataBaseSqlliteHelper eventsData;
	String user_guardian_id, facebook_friends;
	ListView list_home;
	ConnectionDetector cd;
	Boolean isInternetPresent = false;
	String[] stringarray;
	Button data_btn, notification_btn;
	ArrayList<Getcatagory_forlist> params = new ArrayList<Getcatagory_forlist>();
	ArrayList<NotificationModels> modelsDataList;
	BroadcastReceiver receiver;
	SQLiteDatabase eventDataRead, eventDataWrite;
	public static String BROADCAST_ACTION = "com.iapptechnologies.time.BroadCast";
	String notificationMessage = "";
	Element_notification adapter;
	DB_Helper db;
	Database_Handler dbb;

	public notification_fragement() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.notification_layout, container, false);
		final LinearLayout linearLayout = (LinearLayout) view
				.findViewById(R.id.btnMain);
		dbb = new Database_Handler(getActivity());
		data_btn = (Button) view.findViewById(R.id.data);
		notification_btn = (Button) view.findViewById(R.id.NotifacationData);
		db = new DB_Helper(getActivity());
		modelsDataList = new ArrayList<NotificationModels>();
		// data_btn.setTextColor(Color.parseColor("#0097dc"));
		// notification_btn.setTextColor(Color.parseColor("#ffffff"));
		// TODO Auto-generated method stub
		// Drawable d =
		// getActivity().getResources().getDrawable(R.drawable.left_active);
		// linearLayout.setBackgroundDrawable(d);

		// eventsData = new DataBaseSqlliteHelper(getActivity());
		user_guardian_id = getArguments().getString("user_guardian_id");
		facebook_friends = getArguments().getString("facebook_friends");
		list_home = (ListView) view
				.findViewById(R.id.listView1_home_notification);
		cd = new ConnectionDetector(getActivity());
		isInternetPresent = cd.isConnectingToInternet();
		modelsDataList.clear();
		// modelsDataList=dbb.get_notification();
		// adapter = new Element_notification(getActivity(), modelsDataList);

		// list_home.setAdapter(adapter);

		list_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String text = modelsDataList.get(position)
						.getNotificationData();
				if (text.contains("Playdate created")
						|| text.contains("Playdate Updated")
						|| text.contains("Playdate Rejected")
						|| text.contains("Playdate Accepted")) {
					Bundle bundle = new Bundle();
					bundle.putString("user_guardian_id", user_guardian_id);

					android.support.v4.app.Fragment fragment = new Home_fragment();
					fragment.setArguments(bundle);
					android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);

					fragmentTransaction.commit();
				} else if (text.contains("Set Created")
						|| text.contains("Set updated")) {
					/*Bundle bundle = new Bundle();
					bundle.putString("user_guardian_id", user_guardian_id);
					bundle.putString("facebook_friends", facebook_friends);
					android.support.v4.app.Fragment fragment = new Sets();
					fragment.setArguments(bundle);
					android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);*/

					Bundle bundle = new Bundle();
					bundle.putString("user_guardian_id", user_guardian_id);
					bundle.putString("facebook_friends", facebook_friends);
					android.support.v4.app.Fragment fragment = new Sets();
					fragment.setArguments(bundle);
					android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);

					fragmentTransaction.commit();
				
				}

			}
		});

		data_btn.setTextColor(Color.parseColor("#ffffff"));
		notification_btn.setTextColor(Color.parseColor("#0097dc"));

		// TODO Auto-generated method stub
		Drawable d = getActivity().getResources().getDrawable(
				R.drawable.right_active);
		linearLayout.setBackgroundDrawable(d);
		if (isInternetPresent) {
			new getEvents_detail().execute();
		} else {
			Toast.makeText(getActivity(), "Please check internet connection",
					2000).show();

		}

		data_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				data_btn.setTextColor(Color.parseColor("#ffffff"));
				notification_btn.setTextColor(Color.parseColor("#0097dc"));

				// TODO Auto-generated method stub
				Drawable d = getActivity().getResources().getDrawable(
						R.drawable.right_active);
				linearLayout.setBackgroundDrawable(d);
				if (isInternetPresent) {
					new getEvents_detail().execute();
				} else {
					Toast.makeText(getActivity(),
							"Please check internet connection", 2000).show();

				}

			}
		});
		notification_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Drawable d = getActivity().getResources().getDrawable(
						R.drawable.left_active);
				linearLayout.setBackgroundDrawable(d);
				data_btn.setTextColor(Color.parseColor("#0097dc"));
				notification_btn.setTextColor(Color.parseColor("#ffffff"));

				modelsDataList.clear();
				modelsDataList = dbb.get_notification();
				adapter = new Element_notification(getActivity(),
						modelsDataList);

				list_home.setAdapter(adapter);

			}

		});

		/*
		 * receiver = new BroadcastReceiver() {
		 * 
		 * @Override public void onReceive(Context context, Intent intent) {
		 * 
		 * if(intent != null) { String action = intent.getAction();
		 * notificationMessage=intent.getStringExtra("Message");
		 * Toast.makeText(getActivity(), "received"+notificationMessage,
		 * Toast.LENGTH_SHORT).show(); if (action != null &&
		 * action.equalsIgnoreCase(BROADCAST_ACTION)) { ContentValues cv = new
		 * ContentValues(); eventDataWrite=eventsData.getWritableDatabase();
		 * if(notificationMessage!=null && notificationMessage.length()>0) {
		 * cv.put("NotificationMessage",notificationMessage);
		 * eventDataWrite.insert("NotificationMessage",null, cv); }
		 * 
		 * } }
		 * 
		 * } };
		 */

		return view;
	}

	public class getEvents_detail extends AsyncTask<String, Integer, String> {
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
			dialog.show(); // event
		}

		@Override
		protected String doInBackground(String... arg0) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(Constants.GET_PUSH_MESSAGE);
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("g_id", user_guardian_id));
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
				modelsDataList.clear();
				JSONObject json = new JSONObject(sResponse);

				JSONArray jarray = json.getJSONArray("data");
				// String[] stringarray = new String[jarray.length()];

				for (int i = 0; i < jarray.length(); i++) {
					NotificationModels nn = new NotificationModels();
					String aa = jarray.getString(i);

					nn.setNotificationData(aa);
					modelsDataList.add(nn);
					Log.e(aa, "" + modelsDataList.size());

				}

			}

			catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@SuppressLint("CommitPrefEdits")
		protected void onPostExecute(String resultt) {
			adapter = new Element_notification(getActivity(), modelsDataList);

			list_home.setAdapter(adapter);
			// ArrayAdapter<String> ad=new ArrayAdapter<String>(this,
			// android.R.layout.simple_list_item_1, stringarray);
			dialog.dismiss();

		}
	}

	class Element_notification extends BaseAdapter {
		private Activity activity;
		private ArrayList<NotificationModels> _items;
		private LayoutInflater inflater = null;

		public Element_notification(Activity activity,
				ArrayList<NotificationModels> parentItems) {

			// this.imageLoader.clearCache();
			this.activity = activity;
			this._items = parentItems;
			inflater = (LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return modelsDataList.size();
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

		class ViewHolder {
			public TextView Notification;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View vi = convertView;
			ViewHolder _holder;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.notification_list, null);
				_holder = new ViewHolder();

				_holder.Notification = (TextView) convertView
						.findViewById(R.id.txtView1);

				// _holder.event_date = (TextView) convertView
				// .findViewById(R.id.event_date);
				convertView.setTag(_holder);
			} else {
				_holder = (ViewHolder) convertView.getTag();
			}
			String notification_value = _items.get(position)
					.getNotificationData();
			Log.e(notification_value, "" + notification_value);
			_holder.Notification.setText(notification_value);
			// _holder.Notification.setBackgroundColor(getResources().getColor(R.color.bg_color));
			convertView.setBackgroundColor(Color.parseColor("#ffffff"));

			return convertView;
		}

	}

}
