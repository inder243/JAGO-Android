package com.playdate.contactlist;

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

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iapp.playdate.ConnectionDetector;
import com.iapp.playdate.R;

public class IndexTableActivity extends CustomListIndex {
	ListView booksLV;
	TextView tv;
	LinearLayout sideIndex;
	private UserListAdapter userListAdapter;
	Vector<Model_contactlist> subsidiesList;
	ConnectionDetector cd;
	boolean isInternetPresent = false;
	String user_guardian_id,facebook_friends;

	/** Called when the activity is first created. */

	@Override
	public View onCreateView(final LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.sample,container, false);
		booksLV = (ListView) view.findViewById(R.id.booksLV);
		sideIndex = (LinearLayout) view.findViewById(R.id.sideIndex);
		tv = (TextView) view.findViewById(R.id.tv);
		cd = new ConnectionDetector(getActivity());
		isInternetPresent = cd.isConnectingToInternet();

		try {
			user_guardian_id = getArguments().getString("user_guardian_id");
			facebook_friends = getArguments().getString("facebook_friends");
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (isInternetPresent) {
			new Get_contacts().execute();
		} else {
			Toast.makeText(getActivity(), "Please check internet connection",
					2000).show();
		}
		// userVector = getUserList1();

		/*
		 * final Vector<Book> subsidiesList = getIndexedBooks(userVector);
		 * totalListSize = subsidiesList.size();
		 * 
		 * userListAdapter = new UserListAdapter(getActivity(), subsidiesList);
		 * booksLV.setAdapter(userListAdapter);
		 */
		/*
		 * booksLV.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
		 * arg2, long arg3) { // TODO Auto-generated method stub
		 * System.err.println("Clicked...."+arg1);
		 * 
		 * } });
		 */booksLV.setOnItemClickListener(new OnItemClickListener() {

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
		// LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);

		/*
		 * sideIndex.setOnClickListener(onClicked); sideIndexHeight =
		 * sideIndex.getHeight(); mGestureDetector = new
		 * GestureDetector(getActivity(), new ListIndexGestureListener()); //
		 * LinearLayout sideIndex1= (LinearLayout)
		 * view.findViewById(R.id.sideIndex); //ListView
		 * listview=(ListView)view.findViewById(R.id.booksLV); // = null;
		 * handler =getActivity().getWindow().getDecorView().getHandler();
		 * getDisplayListOnChange(sideIndex,booksLV);
		 */
		/*
		 * getView().post(new Runnable() {
		 * 
		 * @Override public void run() {
		 * 
		 * handler.post(new Runnable() { // <-- NULL POINTER EXCEPTION THIS LINE
		 * 
		 * @Override public void run() {
		 * 
		 * getDisplayListOnChange(); } }); // Handler handler=new Handler(); //
		 * code you want to run when view is visible for the first time
		 * 
		 * } } );
		 */
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
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				try {
					if (name.length() > 1) {
						name = name.substring(0, 1).toUpperCase()
								+ name.substring(1).toLowerCase();
						if (name.length() > 30) {
							name = name.substring(0, 29) + "....";
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

					Model_contactlist model = new Model_contactlist(name, phone, email);
					bookList.add(model);

					// Get note.......
					/*
					 * String noteWhere = ContactsContract.Data.CONTACT_ID +
					 * " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
					 * String[] noteWhereParams = new String[]{id,
					 * ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
					 * Cursor noteCur =
					 * cr.query(ContactsContract.Data.CONTENT_URI, null,
					 * noteWhere, noteWhereParams, null); if
					 * (noteCur.moveToFirst()) { String note =
					 * noteCur.getString(
					 * noteCur.getColumnIndex(ContactsContract.
					 * CommonDataKinds.Note.NOTE)); System.out.println("Note " +
					 * note); } noteCur.close();
					 */

					// Get Postal Address....

					/*
					 * String addrWhere = ContactsContract.Data.CONTACT_ID +
					 * " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
					 * String[] addrWhereParams = new String[]{id,
					 * ContactsContract
					 * .CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
					 * Cursor addrCur =
					 * cr.query(ContactsContract.Data.CONTENT_URI, null, null,
					 * null, null); while(addrCur.moveToNext()) { String poBox =
					 * addrCur.getString(
					 * addrCur.getColumnIndex(ContactsContract
					 * .CommonDataKinds.StructuredPostal.POBOX)); String street
					 * = addrCur.getString(
					 * addrCur.getColumnIndex(ContactsContract
					 * .CommonDataKinds.StructuredPostal.STREET)); String city =
					 * addrCur.getString(
					 * addrCur.getColumnIndex(ContactsContract
					 * .CommonDataKinds.StructuredPostal.CITY)); String state =
					 * addrCur.getString(
					 * addrCur.getColumnIndex(ContactsContract
					 * .CommonDataKinds.StructuredPostal.REGION)); String
					 * postalCode = addrCur.getString(
					 * addrCur.getColumnIndex(ContactsContract
					 * .CommonDataKinds.StructuredPostal.POSTCODE)); String
					 * country = addrCur.getString(
					 * addrCur.getColumnIndex(ContactsContract
					 * .CommonDataKinds.StructuredPostal.COUNTRY)); String type
					 * = addrCur.getString(
					 * addrCur.getColumnIndex(ContactsContract
					 * .CommonDataKinds.StructuredPostal.TYPE));
					 * 
					 * // Do something with these....
					 * 
					 * } addrCur.close();
					 */

					// Get Instant Messenger.........
					/*
					 * String imWhere = ContactsContract.Data.CONTACT_ID +
					 * " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
					 * String[] imWhereParams = new String[]{id,
					 * ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
					 * Cursor imCur =
					 * cr.query(ContactsContract.Data.CONTENT_URI, null,
					 * imWhere, imWhereParams, null); if (imCur.moveToFirst()) {
					 * String imName = imCur.getString(
					 * imCur.getColumnIndex(ContactsContract
					 * .CommonDataKinds.Im.DATA)); String imType; imType =
					 * imCur.getString(
					 * imCur.getColumnIndex(ContactsContract.CommonDataKinds
					 * .Im.TYPE)); } imCur.close();
					 */

					// Get Organizations.........

					/*
					 * String orgWhere = ContactsContract.Data.CONTACT_ID +
					 * " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
					 * String[] orgWhereParams = new String[]{id,
					 * ContactsContract
					 * .CommonDataKinds.Organization.CONTENT_ITEM_TYPE}; Cursor
					 * orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
					 * null, orgWhere, orgWhereParams, null); if
					 * (orgCur.moveToFirst()) { String orgName =
					 * orgCur.getString
					 * (orgCur.getColumnIndex(ContactsContract.CommonDataKinds
					 * .Organization.DATA)); String title =
					 * orgCur.getString(orgCur
					 * .getColumnIndex(ContactsContract.CommonDataKinds
					 * .Organization.TITLE)); } orgCur.close();
					 */
				}

			}
		}

		// ContentResolver cr = getActivity().getContentResolver();
		// Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
		// null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
		// if (cur.getCount() > 0)
		// {
		// while (cur.moveToNext())
		// {
		// //ContactsModels contacts=new ContactsModels();
		// String id =
		// cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
		// // idList.add(id);
		// // contacts.setId(id);
		//
		//
		//
		//
		// //
		// http://wptrafficanalyzer.in/blog/android-contacts-content-provider-retrieving-and-listing-contacts-in-listview-example/
		// //
		// if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)){
		// // companyName =
		// dataCursor.getString(dataCursor.getColumnIndex("data1"));
		// // title = dataCursor.getString(dataCursor.getColumnIndex("data4"));
		// // }
		//
		// String name = cur.getString(cur
		// .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		// //Cursor ppCur =
		// cr.query(ContactsContract.CommonDataKinds.Organization.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+
		// " = ?", new String[] { id }, null);
		//
		//
		//
		//
		//
		// // if(cur!=null && cur.getCount()>0)
		// // {
		// // String companyName =
		// cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA1));
		// // Log.e("Company==",""+companyName);
		// // }
		//
		// if
		// (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))
		// > 0)
		// {
		// Cursor pCur =
		// cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+
		// " = ?", new String[] { id }, null);
		//
		// while (pCur.moveToNext())
		//
		// {
		//
		// String phoneNo =
		// pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		// // String companyName =
		// pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
		// // Log.e("Company==",""+companyName);
		// // contacts.setCompanyName(companyName);
		//
		// // nameList.add(name); // Here you can list of contact.
		// //contacts.setName(name);
		//
		//
		// // Log.e("ContactsName==",""+nameList);
		// // phoneList.add(phoneNo); // Here you will get list of phone number.
		// //contacts.setNumber(phoneNo);
		//
		// // Log.e("ContactPhone==",""+phoneList);
		// Cursor emailCur = cr.query(
		// ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
		// ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new
		// String[]{id}, null);
		// while (emailCur.moveToNext())
		// {
		// String email =
		// emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
		// // emailList.add(email); // Here you will get list of email
		// //contacts.setEmail(email);
		//
		//
		// // Log.e("contactsModels==",""+contactListModels);
		// // Log.e("EmailPhone==",""+emailList);
		// //
		// // Global appState = ((Global)getApplicationContext());
		// // appState.setContact(contactListModels);
		// // Log.e("EmailPhone==",""+emailList);
		// // Lo
		// }
		//
		// String orgWhere = Contacts.ContactMethods.PERSON_ID + " = ?";
		// String[] orgWhereParams = new String[]{id};
		// Cursor orgCur = cr.query(Contacts.Organizations.CONTENT_URI,
		// null, orgWhere, orgWhereParams, null);
		// while (orgCur.moveToFirst()) {
		// String orgName = orgCur.getString(
		// orgCur.getColumnIndex(Contacts.Organizations.COMPANY));
		// //Log.e("CompanyName==",""+nameList);
		// String title = orgCur.getString(
		// orgCur.getColumnIndex(Contacts.Organizations.TITLE));
		// }
		// orgCur.close();
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		// // Cursor cursor =
		// getContentResolver().query(Contacts.Organizations.CONTENT_URI, null,
		// null, null, null);
		// //
		// // if(cursor!=null && cursor.getCount()>0)
		// // {
		// //
		// //System.out.println(cursor.getString(cursor.getColumnIndex(Contacts.Organizations.COMPANY)));
		// // String companyName =
		// cur.getString(cursor.getColumnIndex(Contacts.Organizations.COMPANY));
		// // Log.e("Company==",""+companyName);
		// // }
		//
		// //contactListModels.add(contacts);
		// emailCur.close();
		// }
		// pCur.close();
		// }
		// }
		// }

		// Cursor cursor = null;
		// // ContentResolver cr = getActivity().getContentResolver();
		// try {
		// cursor = getActivity().getContentResolver().query(Phone.CONTENT_URI,
		// null, null, null, null);
		// int contactIdIdx = cursor.getColumnIndex(Phone._ID);
		// int nameIdx = cursor.getColumnIndex(Phone.DISPLAY_NAME);
		// int phoneNumberIdx = cursor.getColumnIndex(Phone.NUMBER);
		// // int photoIdIdx = cursor.getColumnIndex(Phone.PHOTO_ID);
		// int emailID=cursor.getColumnIndex(Phone.DISPLAY_NAME_SOURCE);
		// cursor.moveToFirst();
		// do {
		// String idContact = cursor.getString(contactIdIdx);
		// String name = cursor.getString(nameIdx);
		// Log.d("name","    "+name);
		// String phoneNumber = cursor.getString(phoneNumberIdx);
		// Log.d("phoneNumber","    "+phoneNumber);
		// // String email=cursor.getString(emailID);
		// String email = null;
		// /*
		// Cursor emailCur = cr.query(
		// ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
		// ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new
		// String[]{idContact}, null);
		// while (emailCur.moveToNext())
		// {
		// email =
		// emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
		// // emailList.add(email); // Here you will get list of email
		// // contacts.setEmail(email);
		//
		//
		// // Log.e("contactsModels==",""+contactListModels);
		// Log.e("EmailPhone==",""+email);
		// }*/
		// Book book = new Book(name,idContact,phoneNumber,email,email);
		// bookList.add(book);
		// //...
		// } while (cursor.moveToNext());
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// if (cursor != null) {
		// cursor.close();
		// }
		// }

		/*
		 * ContentResolver cr = getActivity().getContentResolver(); Cursor
		 * cursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
		 * PROJECTION, null, null, null); if (cursor != null) { try { final int
		 * contactIdIndex =
		 * cursor.getColumnIndex(ContactsContract.CommonDataKinds
		 * .Email.CONTACT_ID); final int displayNameIndex =
		 * cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME); final
		 * int displayNameIndex =
		 * cursor.getColumnIndex(ContactsContract.Contacts.); final int
		 * emailIndex =
		 * cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
		 * long contactId; String displayName, address; while
		 * (cursor.moveToNext()) { contactId = cursor.getLong(contactIdIndex);
		 * Log.d("contactId","    "+contactId); displayName =
		 * cursor.getString(displayNameIndex);
		 * Log.d("displayName","    "+displayName); address =
		 * cursor.getString(emailIndex); Log.d("address","    "+address); Book
		 * book = new Book(displayName,address,address,address,address);
		 * bookList.add(book); } } finally { cursor.close(); } }
		 */

		/*
		 * StringBuffer sb = new StringBuffer();
		 * sb.append("......Contact Details....."); ContentResolver cr =
		 * getActivity().getContentResolver(); Cursor cur =
		 * cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
		 * null); String phone = null; String emailContact = null; String
		 * emailType = null; String image_uri = ""; Bitmap bitmap = null; if
		 * (cur.getCount() > 0) { while (cur.moveToNext()) { String id =
		 * cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
		 * String name =
		 * cur.getString(cur.getColumnIndex(ContactsContract.Contacts
		 * .DISPLAY_NAME)); image_uri =
		 * cur.getString(cur.getColumnIndex(ContactsContract
		 * .CommonDataKinds.Phone.PHOTO_URI)); if
		 * (Integer.parseInt(cur.getString
		 * (cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) >
		 * 0) { System.out.println("name : " + name + ", ID : " + id);
		 * sb.append("\n Contact Name:" + name); Cursor pCur = cr.query(
		 * ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
		 * ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = ?", new
		 * String[] { id }, null); while (pCur.moveToNext()) { phone =
		 * pCur.getString
		 * (pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		 * sb.append("\n Phone number:" + phone); System.out.println("phone" +
		 * phone); } pCur.close();
		 * 
		 * Cursor emailCur = cr.query(
		 * ContactsContract.CommonDataKinds.Email.CONTENT_URI,
		 * null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new
		 * String[] { id }, null); while (emailCur.moveToNext()) { emailContact
		 * = emailCur
		 * .getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds
		 * .Email.DATA)); emailType =
		 * emailCur.getString(emailCur.getColumnIndex(
		 * ContactsContract.CommonDataKinds.Email.TYPE)); sb.append("\nEmail:" +
		 * emailContact + "Email type:" + emailType);
		 * System.out.println("Email " + emailContact + " Email Type : " +
		 * emailType);
		 * 
		 * }
		 * 
		 * emailCur.close(); } /* if (image_uri != null) {
		 * System.out.println(Uri.parse(image_uri)); try { bitmap =
		 * MediaStore.Images.Media
		 * .getBitmap(getActivity().getContentResolver(), Uri.parse(image_uri));
		 * sb.append("\n Image in Bitmap:" + bitmap);
		 * System.out.println(bitmap);
		 * 
		 * } catch (FileNotFoundException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * }
		 */

		/*
		 * ContentResolver cr = getActivity().getContentResolver(); Cursor cur =
		 * cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
		 * ContactsContract.Contacts.DISPLAY_NAME + " ASC"); if (cur.getCount()
		 * > 0) { while (cur.moveToNext()) { String id =
		 * cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)); //
		 * idList.add(id); // contacts.setId(id);
		 * 
		 * 
		 * 
		 * 
		 * //
		 * http://wptrafficanalyzer.in/blog/android-contacts-content-provider-
		 * retrieving-and-listing-contacts-in-listview-example/ //
		 * if(dataCursor.
		 * getString(dataCursor.getColumnIndex("mimetype")).equals(
		 * ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)){ //
		 * companyName =
		 * dataCursor.getString(dataCursor.getColumnIndex("data1")); // title =
		 * dataCursor.getString(dataCursor.getColumnIndex("data4")); // }
		 * 
		 * String name = cur.getString(cur
		 * .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); //Cursor
		 * ppCur =
		 * cr.query(ContactsContract.CommonDataKinds.Organization.CONTENT_URI
		 * ,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = ?", new
		 * String[] { id }, null);
		 * 
		 * 
		 * Log.d("name   ",name);
		 * 
		 * 
		 * // if(cur!=null && cur.getCount()>0) // { // String companyName =
		 * cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.
		 * Organization.DATA1)); // Log.e("Company==",""+companyName); // }
		 * 
		 * if
		 * (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract
		 * .Contacts.HAS_PHONE_NUMBER))) > 0) { Cursor pCur =
		 * cr.query(ContactsContract
		 * .CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract
		 * .CommonDataKinds.Phone.CONTACT_ID+ " = ?", new String[] { id },
		 * null);
		 * 
		 * while (pCur.moveToNext())
		 * 
		 * {
		 * 
		 * String phoneNo =
		 * pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds
		 * .Phone.NUMBER)); // String companyName =
		 * pCur.getString(pCur.getColumnIndex
		 * (ContactsContract.CommonDataKinds.Organization.COMPANY)); //
		 * Log.e("Company==",""+companyName); //
		 * contacts.setCompanyName(companyName);
		 * 
		 * // nameList.add(name); // Here you can list of contact. //
		 * contacts.setName(name);
		 * 
		 * 
		 * // Log.e("ContactsName==",""+nameList); // phoneList.add(phoneNo); //
		 * Here you will get list of phone number. //
		 * contacts.setNumber(phoneNo);
		 * 
		 * Log.e("ContactPhone==",""+phoneNo); } Cursor emailCur = cr.query(
		 * ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
		 * ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new
		 * String[]{id}, null); while (emailCur.moveToNext()) { String email =
		 * emailCur
		 * .getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds
		 * .Email.DATA)); // emailList.add(email); // Here you will get list of
		 * email // contacts.setEmail(email);
		 * 
		 * 
		 * // Log.e("contactsModels==",""+contactListModels);
		 * Log.e("EmailPhone==",""+email); } } }
		 * 
		 * }
		 */
		Collections.sort(bookList);
		return bookList;
	}

	private static final String[] PROJECTION = new String[] {
			ContactsContract.CommonDataKinds.Email.CONTACT_ID,
			ContactsContract.Contacts.DISPLAY_NAME,
			ContactsContract.CommonDataKinds.Email.DATA };

	/*
	 * @Override public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); setContentView(R.layout.sample);
	 * 
	 * }
	 */

	/*
	 * @Override public void onWindowFocusChanged(boolean hasFocus) {
	 * super.onWindowFocusChanged(hasFocus);
	 * 
	 * getDisplayListOnChange(); }
	 */

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
		Vector<Model_contactlist> model_list;
		ArrayList<Model_friend_list>friend_list;
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

			model_list = new Vector<Model_contactlist>();
			friend_list=new ArrayList<Model_friend_list>();

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost("http://54.191.67.152/playdate/guardian_fb_friends_details.php");//?friend_fbids=%27100004938971287%27,%27100001678200547%27");

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			//nameValuePairs.add(new BasicNameValuePair("usr_id",user_guardian_id));
			nameValuePairs.add(new BasicNameValuePair("friend_fbids",facebook_friends));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
			String Response="";
			try {
				 Response = reader.readLine();

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
						friend_list.add(model_friendlist);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ContentResolver cr = getActivity().getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
					null, null, null);

			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					String id = cur.getString(cur
							.getColumnIndex(ContactsContract.Contacts._ID));
					String name = cur
							.getString(cur
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					try {
						if (name.length() > 1) {
							name = name.substring(0, 1).toUpperCase()
									+ name.substring(1).toLowerCase();
							if (name.length() > 25) {
								name = name.substring(0, 24) + "....";
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
						System.out.println("name : " + name + ", ID : " + id);

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

							System.out.println("phone" + phone);
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
							String emailType = emailCur
									.getString(emailCur
											.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

							
							break;
						}
						emailCur.close();

					}
					Model_contactlist model = new Model_contactlist(name, phone, email);
					model_list.add(model);
				}
			}

			Collections.sort(model_list);
			return null;
		}

		protected void onPostExecute(String resultt) {
			userVector = model_list;
			subsidiesList = getIndexedBooks(userVector);
			totalListSize = subsidiesList.size();

			userListAdapter = new UserListAdapter(getActivity(), subsidiesList,booksLV, getActivity(),friend_list);
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

}