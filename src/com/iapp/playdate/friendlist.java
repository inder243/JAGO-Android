package com.iapp.playdate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

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
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iapp.playdate.R;
import com.iapptechnologies.time.util.Constants;


public class friendlist extends CustomListIndex {
	
	String response,check,url_sets,sresponse,facebookfriends,setid;
	
	ArrayList<Getcategory>child_info_list;
	LazyAdapter adapter;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	Button contact_button, Playdate_button;
	ListView listView;
	///////
	
	ListView booksLV;
	
	TextView tv;
	LinearLayout sideIndex;
	private UserListAdapter userListAdapter;
	Vector<Model_contactlist> subsidiesList;
	
	String user_guardian_id,facebook_friends;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	TextView txt;
	public friendlist(){
		
	}
	 @Override
		public View onCreateView(final LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ViewGroup view = (ViewGroup) inflater.inflate(R.layout.sets_fragment_list, container, false);
			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			cd = new ConnectionDetector(getActivity());
			isInternetPresent = cd.isConnectingToInternet();
			contact_button = (Button) view.findViewById(R.id.contacts);
			Playdate_button = (Button) view.findViewById(R.id.playdate_friends);
			txt=(TextView)view.findViewById(R.id.tv);
			booksLV = (ListView) view.findViewById(R.id.booksLV);
			sideIndex = (LinearLayout) view.findViewById(R.id.sideIndex);
			tv = (TextView) view.findViewById(R.id.tv);
			listView=(ListView)view.findViewById(R.id.list_friends);
			settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
			editor = settings.edit();
			facebook_friends=settings.getString("FBFRIEND","");
		//	getcat_for_sets=new ArrayList<Getcatagory_for_sets>();
			//model_list = new Vector<Model_contactlist>();
			
			try {
				user_guardian_id = getArguments().getString("user_guardian_id");
				response=getArguments().getString("response_data");
				check=getArguments().getString("check");
				//facebook_friends=getArguments().getString("facebook_friends");
			} catch (Exception e) {
				// TODO: handle exception
			}
			listView.setVisibility(View.VISIBLE);
			listView.setDivider(null);
			listView.setDividerHeight(0);
			final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.btnMain);
			
			contact_button.setTextColor(Color.parseColor("#0097dc"));
			Playdate_button.setTextColor(Color.parseColor("#ffffff"));

			
			Drawable d = getActivity().getResources().getDrawable(R.drawable.left_active);
			linearLayout.setBackgroundDrawable(d);
			if(check.equals("0")){
				jsonparsing(response);
				}
				else if(check.equals("1")){
					if(isInternetPresent){
						
						//new Get_Sets().execute();
						
						if(GlobalVariable.getcat_for_sets.size()>0){
							adapter = new LazyAdapter(getActivity(), GlobalVariable.getcat_for_sets);
							listView.setAdapter(adapter);
						}else{
							new Get_Sets().execute();
						}
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
			
			contact_button.setOnClickListener(new OnClickListener() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					txt.setVisibility(View.VISIBLE);
					booksLV.setVisibility(View.VISIBLE);
					sideIndex.setVisibility(View.VISIBLE);
					listView.setVisibility(View.INVISIBLE);
					contact_button.setTextColor(Color.parseColor("#ffffff"));
					Playdate_button.setTextColor(Color.parseColor("#0097dc"));

					// TODO Auto-generated method stub
					Drawable d = getActivity().getResources().getDrawable(R.drawable.right_active);
					linearLayout.setBackgroundDrawable(d);
				
					if (isInternetPresent) {
						
						if(GlobalVariable.model_list.size()>0){
							
							userVector = GlobalVariable.model_list;
							subsidiesList = getIndexedBooks(userVector);
							totalListSize = subsidiesList.size();

							userListAdapter = new UserListAdapter(getActivity(), subsidiesList,booksLV, getActivity(),GlobalVariable.friend_list);
							booksLV.setAdapter(userListAdapter);

							sideIndex.setOnClickListener(onClicked);
							sideIndexHeight = sideIndex.getHeight();
							mGestureDetector = new GestureDetector(getActivity(),new ListIndexGestureListener());
							getDisplayListOnChange(sideIndex, booksLV);
							
						}else{
							new Get_contacts().execute();
						}
						
						
					} else {
						Toast.makeText(getActivity(), "Please check internet connection",
								2000).show();
					}
					
				}
			});
			
			Playdate_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					txt.setVisibility(View.INVISIBLE);
					booksLV.setVisibility(View.INVISIBLE);
					sideIndex.setVisibility(View.INVISIBLE);
					listView.setVisibility(View.VISIBLE);
					Drawable d = getActivity().getResources().getDrawable(R.drawable.left_active);
					linearLayout.setBackgroundDrawable(d);
					contact_button.setTextColor(Color.parseColor("#0097dc"));
					Playdate_button.setTextColor(Color.parseColor("#ffffff"));
					if(check.equals("0")){
						jsonparsing(response);
						}
						else if(check.equals("1")){
							if(isInternetPresent){
								if(GlobalVariable.getcat_for_sets.size()>0){
									adapter = new LazyAdapter(getActivity(), GlobalVariable.getcat_for_sets);
									listView.setAdapter(adapter);
								}else{
									new Get_Sets().execute();
								}
								
								
								
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
					
				}
			});
			

			
		
			
			
			
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					ArrayList<Getcatagory_for_sets>getcat_for_sets_listclick;
					getcat_for_sets_listclick=GlobalVariable.getcat_for_sets;
					ArrayList<Getcategory>childinfo;
					
					
					
					String parentname=getcat_for_sets_listclick.get(arg2).parent_name;
					String parentid=getcat_for_sets_listclick.get(arg2).parent_id;
					String parentfreetime=getcat_for_sets_listclick.get(arg2).parent_freetime;
					String parentdob=getcat_for_sets_listclick.get(arg2).parent_date_ofbirth;
					String parentpic=getcat_for_sets_listclick.get(arg2).parent_profilepic;
					String parent_type=getcat_for_sets_listclick.get(arg2).parent_type;
					String location=getcat_for_sets_listclick.get(arg2).parent_location;
					
					childinfo=getcat_for_sets_listclick.get(arg2).childinfo;
					GlobalVariable.global_list_child=childinfo;
					Bundle bundle = new Bundle();
					bundle.putString("name", parentname);
					bundle.putString("url", parentpic);
					bundle.putString("freetime", parentfreetime);
					bundle.putString("dob", parentdob);
					bundle.putString("guardiantype", parent_type);
					bundle.putString("location", location);
					
					
					android.support.v4.app.Fragment  fragment=new Friend_profile();
			        fragment.setArguments(bundle);
				
			        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);
					fragmentTransaction.addToBackStack("first18");
					fragmentTransaction.commit();
				
					
					
				
					

				}
			});
			
			
			booksLV.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					if (!global.arrayforTitle.contains(position)) {
						Toast.makeText(getActivity(),
								subsidiesList.get(position).getName(), 1000)
								.show();
					}
					System.err.println("Clicked...." + position);
				}
			});
			booksLV.setOnScrollListener(new OnScrollListener() {
				public void onScrollStateChanged(AbsListView paramAbsListView,
						int paramInt) {
					// TODO Auto-generated method stub

				}

				public void onScroll(AbsListView paramAbsListView, int paramInt1,
						int paramInt2, int paramInt3) {
					// TODO Auto-generated method stub
					if (global.arrayforTitle.contains(paramInt1)) {
						System.out.println("Matched..." + paramInt1);
						try {
							tv.setText(subsidiesList.get(paramInt1).getName());
						} catch (Exception e) {
							// TODO: handle exception
						}

					}
					// System.out.println("firstVisibleItem="+paramInt1
					// +" visibleItemCount..="+paramInt2+"--- totalItemCount---"+paramInt3);
				}
			});
	 
			return view;
			 
	 	   
	 }
	 
	 
	 
	 private Vector<Model_contactlist> getUserList1() {
			// TODO Auto-generated method stub
			Vector<Model_contactlist> bookList = new Vector<Model_contactlist>();

			ContentResolver cr = getActivity().getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
					null, null, null);

			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					String name="";
					String id = cur.getString(cur
							.getColumnIndex(ContactsContract.Contacts._ID));
					 name = cur
							.getString(cur
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					try {
						
						
						
						
						
						
						if (name.length() > 1) {
							name = name.substring(0, 1).toUpperCase()+ name.substring(1).toLowerCase();
							if (name.length() > 30) {
								name = name.substring(0, 29) + "...";
							}

						} else if (name.length() == 1) {
							name = name.toUpperCase() + ".";
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

					String phone = "", email = "";
					Log.e("Name==", "" + name);
					if (Integer
							.parseInt(cur.getString(cur
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						System.out.println("name : " + name + ", ID : " + id);

						// get the phone number
						Cursor pCur = cr.query(
								ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
								null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID
										+ " = ?", new String[] { id }, null);
						while (pCur.moveToNext()) {
							phone = pCur
									.getString(pCur
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

							System.out.println("phone" + phone);
							Log.e("Phone==", "" + phone);
							break;
						}
						pCur.close();

						// get email and type

						Cursor emailCur = cr.query(
								ContactsContract.CommonDataKinds.Email.CONTENT_URI,
								null,
								ContactsContract.CommonDataKinds.Email.CONTACT_ID
										+ " = ?", new String[] { id }, null);
						while (emailCur.moveToNext()) {
							// This would allow you get several email addresses
							// if the email addresses were stored in an array
							email = emailCur
									.getString(emailCur
											.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
							String emailType = emailCur
									.getString(emailCur
											.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

							System.out.println("Email " + email + " Email Type : "
									+ emailType);
							Log.e("Email==", "" + email);
							Log.e("TYpe==", "" + emailType);
							break;
						}
						emailCur.close();
						
						try {
							Model_contactlist model = new Model_contactlist(name, phone, email);
							bookList.add(model);
						} catch (Exception e) {
							// TODO: handle exception
						}

						

									}

				}
			}

			
			
			Collections.sort(bookList);
			return bookList;
		}

		private static final String[] PROJECTION = new String[] {
				ContactsContract.CommonDataKinds.Email.CONTACT_ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Email.DATA };

		
		private Vector<Model_contactlist> getIndexedBooks(Vector<Model_contactlist> booksVector) {

			// Retrieve it from DB in shorting order
			Vector<Model_contactlist> v = new Vector<Model_contactlist>();
			// Add default item
			String idx1 = null;
			String idx2 = null;
			for (int i = 0; i < booksVector.size(); i++) {
				Model_contactlist temp = booksVector.elementAt(i);
				// Insert the alphabets
				idx1 = (temp.getName().substring(0, 1)).toLowerCase();
				if (!idx1.equalsIgnoreCase(idx2)) {
					v.add(new Model_contactlist(idx1.toUpperCase(), "", ""));
					idx2 = idx1;
					dealList.add(i);
				}
				v.add(temp);
			}

			return v;
		}

		/**
		 * ListIndexGestureListener method gets the list on scroll.
		 */
		private class ListIndexGestureListener extends
				GestureDetector.SimpleOnGestureListener {
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {

				/**
				 * we know already coordinates of first touch we know as well a
				 * scroll distance
				 */
				sideIndexX = sideIndexX - distanceX;
				sideIndexY = sideIndexY - distanceY;

				/**
				 * when the user scrolls within our side index, we can show for
				 * every position in it a proper item in the list
				 */
				if (sideIndexX >= 0 && sideIndexY >= 0) {
					displayListItem();
				}

				return super.onScroll(e1, e2, distanceX, distanceY);
			}
		}

		public class Get_contacts extends AsyncTask<String, Integer, String> {
			ProgressDialog dialog = new ProgressDialog(getActivity());
		
			
			@Override
			protected void onPreExecute() {
				try {
					dialog.show();
					dialog.setCancelable(false);
					dialog.setMessage("Loading.......please wait");
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			@Override
			protected String doInBackground(String... params) {

				
			

				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(Constants.FBFRIENDS);//?friend_fbids=%27100004938971287%27,%27100001678200547%27");

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				//nameValuePairs.add(new BasicNameValuePair("usr_id",user_guardian_id));
				nameValuePairs.add(new BasicNameValuePair("friend_fbids",facebook_friends));
				try {
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				StringBuilder sbb=new StringBuilder();
				sbb.append("http://54.191.67.152/playdate/guardian_fb_friends_details.php?");
				sbb.append(nameValuePairs.get(0));
				System.out.println(sbb);
				
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
				String Response="";
				try {
					 Response = reader.readLine();
					 System.out.println(Response);

				} catch (IOException e) {
					e.printStackTrace();
				}
				if (Response.equals(null) || Response.equals("")) {

				} else {
					JSONObject json = null;
					try {
						json = new JSONObject(Response);
					} catch (JSONException e) {
						e.printStackTrace();
					}
	                
					try {
						JSONArray jArray = json.getJSONArray("data");
						for (int i = 0; i < jArray.length(); i++) {
							Model_friend_list model_friendlist=new Model_friend_list();
							JSONObject getdetail = jArray.getJSONObject(i);
							model_friendlist.friend_name=getdetail.getString("name");
							model_friendlist.friend_id=getdetail.getString("guardian_id");
							model_friendlist.friend_email=getdetail.getString("email");
							model_friendlist.friend_phone_number=getdetail.getString("phone");
							model_friendlist.friend_pic=getdetail.getString("profile_image");
							GlobalVariable.friend_list.add(model_friendlist);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("...........Done........");
				ContentResolver cr = getActivity().getContentResolver();
				Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
						null, null, null);

				if (cur.getCount() > 0) {
					while (cur.moveToNext()) {
						String name="";
						String id = cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts._ID));
						 name = cur
								.getString(cur
										.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						try {
							if (name.length() > 1) {
								name = name.substring(0, 1).toUpperCase()
										+ name.substring(1).toLowerCase();
								
									
									 int density = getResources().getDisplayMetrics().densityDpi;
									  switch (density) {
									 
									  case DisplayMetrics.DENSITY_LOW:
										  if (name.length() > 18) {
											  name = name.substring(0, 17) + "...";
									  }
										
										  break;
									  case DisplayMetrics.DENSITY_MEDIUM:
									   if (name.length() > 18) {
										   name = name.substring(0, 17) + "...";
									  }
										
										  break;
									  case DisplayMetrics.DENSITY_HIGH:
									   if (name.length() > 18) {
										   name = name.substring(0, 17) + "...";
									  }
										
										  break;
									  case DisplayMetrics.DENSITY_XHIGH:
									   if (name.length() > 25) {
										   name = name.substring(0, 24) + "...";
									  }
										
										  break;
									  case DisplayMetrics.DENSITY_XXHIGH:
									   if (name.length() > 25) {
										   name = name.substring(0, 24) + "...";
									  }
										
										  break;
									  }
									
									
								
							} else if (name.length() == 1) {
								name = name.toUpperCase() + "  ";
							}
						} catch (Exception e) {
							// TODO: handle exception
						}

						String phone = "", email = "";
						if (Integer
								.parseInt(cur.getString(cur
										.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						

							// get the phone number
							Cursor pCur = cr
									.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
											null,
											ContactsContract.CommonDataKinds.Phone.CONTACT_ID
													+ " = ?", new String[] { id },
											null);
							while (pCur.moveToNext()) {
								phone = pCur
										.getString(pCur
												.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

								
								break;
							}
							pCur.close();

							// get email and type

							Cursor emailCur = cr
									.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
											null,
											ContactsContract.CommonDataKinds.Email.CONTACT_ID
													+ " = ?", new String[] { id },
											null);
							while (emailCur.moveToNext()) {

								email = emailCur
										.getString(emailCur
												.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
								/*String emailType = emailCur
										.getString(emailCur
												.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
*/
								
								break;
							}
							emailCur.close();

						}else{
							Cursor emailCur = cr
									.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
											null,
											ContactsContract.CommonDataKinds.Email.CONTACT_ID
													+ " = ?", new String[] { id },
											null);
							while (emailCur.moveToNext()) {

								email = emailCur
										.getString(emailCur
												.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
							
								
								break;
							}
							emailCur.close();
						}
						try {
							
							if(name.equals(null)||name.equals("")){
							
							}else{
								Model_contactlist model = new Model_contactlist(name, phone, email);
								GlobalVariable.model_list.add(model);
							}
							
							
						} catch (Exception e) {
							
						}
						
					}
				}

				Collections.sort(GlobalVariable.model_list);
				return null;
			}

			protected void onPostExecute(String resultt) {
				userVector = GlobalVariable.model_list;
				subsidiesList = getIndexedBooks(userVector);
				totalListSize = subsidiesList.size();

				userListAdapter = new UserListAdapter(getActivity(), subsidiesList,booksLV, getActivity(),GlobalVariable.friend_list);
				booksLV.setAdapter(userListAdapter);

				sideIndex.setOnClickListener(onClicked);
				sideIndexHeight = sideIndex.getHeight();
				mGestureDetector = new GestureDetector(getActivity(),new ListIndexGestureListener());
				getDisplayListOnChange(sideIndex, booksLV);
				try {
					dialog.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
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
					getcatagory_for_sets.parent_freetime=friendinfo.getString("set_fixed_freetime");
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
							childdetaillist.date_of_birth=child_detail.getString("dob");
							childdetaillist.free_time=child_detail.getString("set_fixed_freetime");
							childdetaillist.allergies_child=child_detail.getString("allergies");
							childdetaillist.hobbies_child=child_detail.getString("hobbies");
							childdetaillist.condition_child=child_detail.getString("dob");
							childdetaillist.school_child=child_detail.getString("school");
							childdetaillist.youthclub_child=child_detail.getString("youth_club");
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
					GlobalVariable.getcat_for_sets.add(getcatagory_for_sets);
					
					
				}
				try {
					if(dialog!=null || dialog.isShowing()){
						dialog.dismiss();
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				adapter = new LazyAdapter(getActivity(), GlobalVariable.getcat_for_sets);
				listView.setAdapter(adapter);
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
			        nameValuePairs.add(new BasicNameValuePair("friend_fbid",facebook_friends));
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
					try {
						dialog.setMessage("Loading.......please wait");
						dialog.setCancelable(false);
						dialog.show();
						
					} catch (Exception e) {
						// TODO: handle exception
					}	
						
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
					
try {
	dialog.dismiss();
} catch (Exception e) {
	// TODO: handle exception
}
					
					jsonparsing(sresponse);
					
					}
			}
}

