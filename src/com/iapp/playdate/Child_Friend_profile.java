package com.iapp.playdate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iapp.playdate.R;


public class Child_Friend_profile extends android.support.v4.app.Fragment {
	

	
	EditText dob,allergies,name,editallergies;
	ImageView child_image;
	com.iapptechnologies.time.util.ImageLoader imageLoader;
	String parent_freetime,date_of_birth_of_parent,parent_type,parent_location,url,child_name,child_ID,child_profile_pic,child_hobbies,child_allergies,child_conditions,child_school,child_youthclub,guardian_id,child_dob,free_time_child,urlreturned;
String parent_name,parent_profilepic;
ListView list_parent;
Button update_info;
ArrayList<Getcategory> parentItems;
LazyAdapter adapter;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.child_profile_show, container,
                false);
		imageLoader=new com.iapptechnologies.time.util.ImageLoader(getActivity());	
		dob=(EditText)view.findViewById(R.id.textView_dob_child_profile);
	//	setfixedfree_time=(EditText)view.findViewById(R.id.textView_freetime_child_profile);
		//conditions=(EditText)view.findViewById(R.id.textView_condition_childprofile);
		allergies=(EditText)view.findViewById(R.id.textView_child_profile_allergies);
	//	hobbies=(EditText)view.findViewById(R.id.textView_child_profile_hobbies);
	//	school=(EditText)view.findViewById(R.id.textView_child_profile_school);
		//youthclub=(EditText)view.findViewById(R.id.textView_child_profile_youthclub);
		name=(EditText)view.findViewById(R.id.child_profile_name);
		child_image=(ImageView)view.findViewById(R.id.imageView_child_profile);
		list_parent=(ListView)view.findViewById(R.id.listView_child_profile_child);
	//	addmore_freetime=(Button)view.findViewById(R.id.button1_add_moredays_child_profile);
		update_info=(Button)view.findViewById(R.id.button_save_child_update);
		//addmore_freetime.setVisibility(View.GONE);
		update_info.setVisibility(View.GONE);
		
		
		//parent_freetime = getArguments().getString("parent_freetime"); 
		date_of_birth_of_parent = getArguments().getString("parent_dob"); 
		parent_type = getArguments().getString("parent_type"); 
		parent_location=getArguments().getString("parent_location");
		
		parent_name=getArguments().getString("parent_name");
		parent_profilepic=getArguments().getString("parent_profilepic");
		
		child_dob = getArguments().getString("dob"); 
		child_name = getArguments().getString("child_name"); 
		child_ID = getArguments().getString("Child_id"); 
		child_profile_pic=getArguments().getString("Child_profilepic");
		//child_hobbies = getArguments().getString("hobbies"); 
		child_allergies = getArguments().getString("allergies"); 
		//child_conditions = getArguments().getString("conditions"); 
		//child_school = getArguments().getString("school"); 
	//	child_youthclub = getArguments().getString("youthclub"); 
		guardian_id = getArguments().getString("Child_guardianId");
		//free_time_child = getArguments().getString("free_time");
		
		System.out.println("-------------------"+parent_name);
		System.out.println("-------------------"+parent_profilepic);
		
		
		
		 DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 Date date_of_birth=null;
		 try {
			 date_of_birth=sdf.parse(child_dob);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//String reportDate = df.format(today);
		// birthDay=sdf.format(date_of_birth);
		 DateFormat destDf = new SimpleDateFormat("dd/MM/yy");
		
		               
		 String dob_formated=null;
		     try {
		    	 dob_formated = destDf.format(date_of_birth);
			} catch (Exception e) {
				// TODO: handle exception
			}        // format the date into another format
		
		 
		
		dob.setText(dob_formated);
		/*try {
			setfixedfree_time.setText(free_time_child.toUpperCase());
		} catch (Exception e) {
			// TODO: handle exception
		}*/
try {
	allergies.setText(child_allergies.toUpperCase());
	//hobbies.setText(child_hobbies.toUpperCase());
	//school.setText(child_school.toUpperCase());
	//youthclub.setText(child_youthclub.toUpperCase());
		} catch (Exception e) {
			// TODO: handle exception
		}
try {
	name.setText(child_name.toUpperCase());
} catch (Exception e) {
	// TODO: handle exception
}
		
		//conditions.setText(child_conditions.toUpperCase());
		
		
		
		
		dob.setFocusable(false);
	   
	   //  setfixedfree_time.setFocusable(false);
	    
	   //  conditions.setFocusable(false);
	    
	     allergies.setFocusable(false);
	 //    hobbies.setFocusable(false);
	    
	//     school.setFocusable(false);
	   
	     
	   //  youthclub.setFocusable(false);
	    
	     name.setFocusable(false);
	     dob.setEnabled(false);
		   
	  //   setfixedfree_time.setEnabled(false);
	    
	    // conditions.setEnabled(false);
	    
	     allergies.setEnabled(false);
	   //  hobbies.setEnabled(false);
	    
	 //    school.setEnabled(false);
	   
	     
	//     youthclub.setEnabled(false);
	    
	     name.setEnabled(false);
	    
		
		list_parent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		imageLoader.DisplayImage(child_profile_pic, child_image);
		
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
		
		/*child_image.requestLayout();
		list_parent.requestLayout();
		 int density = getResources().getDisplayMetrics().densityDpi;
		  switch (density) {
		  case DisplayMetrics.DENSITY_LOW:
			  child_image.getLayoutParams().height = 100;
			  child_image.getLayoutParams().width = 100;
			 
			  list_parent.getLayoutParams().height = 100;
			
			  break;
		  case DisplayMetrics.DENSITY_MEDIUM:
			  child_image.getLayoutParams().height = 180;
			  child_image.getLayoutParams().width = 180;
			  list_parent.getLayoutParams().height = 180;
			
			  break;
		  case DisplayMetrics.DENSITY_HIGH:
			  child_image.getLayoutParams().height = 180;
			  child_image.getLayoutParams().width = 180;
			  list_parent.getLayoutParams().height = 180;
			
			  break;
		  case DisplayMetrics.DENSITY_XHIGH:
			  child_image.getLayoutParams().height = 250;
			  child_image.getLayoutParams().width = 250;
			  list_parent.getLayoutParams().height = 250;
			 
			  break;
		  case DisplayMetrics.DENSITY_XXHIGH:
			  child_image.getLayoutParams().height = 250;
			  child_image.getLayoutParams().width = 250;
			  list_parent.getLayoutParams().height = 250;
			 
			  break;
		  }*/
		
		  parentItems = new ArrayList<Getcategory>();
			Getcategory getcategory = null;
		  getcategory = new Getcategory();
			getcategory.child_name = parent_name;
			getcategory.profile_image = parent_profilepic;
			parentItems.add(getcategory);
		  
		    adapter = new LazyAdapter(getActivity(), parentItems);

		    list_parent.setAdapter(adapter);
		  
		  
		  
		  list_parent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				
				Bundle bundle = new Bundle();
				bundle.putString("name", parent_name);
				bundle.putString("url", parent_profilepic);
				//bundle.putString("freetime", parent_freetime);
				bundle.putString("dob", date_of_birth_of_parent);
				bundle.putString("guardiantype", parent_type);
				bundle.putString("location", parent_location);
				bundle.putString("firstname", parent_name);
				
			
				
				android.support.v4.app.Fragment  fragment=new Friend_profile();
		        fragment.setArguments(bundle);
		        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, fragment);
				fragmentTransaction.addToBackStack("first3");
				fragmentTransaction.commit();
				/*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
				        .replace(R.id.content_frame, fragment).commit();*/
				
			}
		});
		
		return view;
	}
	public class LazyAdapter extends BaseAdapter {
		String _imgurl = "";
		private Activity activity;
		private ArrayList<Getcategory> _items;
		private LayoutInflater inflater = null;

		// public ImageLoader imageLoader;

		public ImageLoader imageLoader;

		// int count = 10;

		public LazyAdapter(Activity activity, ArrayList<Getcategory> parentItems) {

			// this.imageLoader.clearCache();
			this.activity = activity;
			this._items = parentItems;
			inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

		// .............View holder use to hold the view comes in the form of
		// array list............//
		// ........................ Here we link the all the objects with the
		// xml class ...............//
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
						.inflate(R.layout.child_parent_list, null);
				_holder = new ViewHolder();

				_holder.event_title = (TextView) convertView
						.findViewById(R.id.textView1);
				_holder._image = (ImageView) convertView
						.findViewById(R.id.imageView1);
				// _holder.event_date = (TextView) convertView
				// .findViewById(R.id.event_date);
				convertView.setTag(_holder);
			} else {
				_holder = (ViewHolder) convertView.getTag();
			}
			String name_of_child=_items.get(position).child_name;
			name_of_child=name_of_child.toUpperCase();
			_holder.event_title.setText(name_of_child);
			// _holder.event_date.setText(_items.get(position).event_date);
			_imgurl = _items.get(position).profile_image;
			// Log.d("", "_items.get(position).event_title"
			// + _items.get(position).event_title);
			// Log.d("", "_items.get(position).event_title"
			// + _items.get(position).event_title);
			// Log.d("", "_items.get(position).event_title"
			// + _items.get(position).event_title);
			// Log.d("", "_items.get(position).event_title"
			// + _items.get(position).event_title);
			Log.d("", "_imgurl" + _imgurl);
			Log.d("", "_imgurl" + _imgurl);
			Log.d("", "_imgurl" + _imgurl);
			Log.d("", "_imgurl" + _imgurl);
			Log.d("", "_imgurl" + _imgurl);
			_holder._image.setTag(_imgurl);
			// imageLoader.DisplayImage(_imgurl, activity, _holder._image);
			// // getImagesSize();
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
				 // child_list.setLayoutParams(new LayoutParams(90,90));
				  break;
			  case DisplayMetrics.DENSITY_HIGH:
				  _holder._image.getLayoutParams().height = 85;
				  _holder._image.getLayoutParams().width = 85;
				//  child_list.setLayoutParams(new LayoutParams(110,110));
				  break;
			  case DisplayMetrics.DENSITY_XHIGH:
				  _holder._image.getLayoutParams().height = 120;
				  _holder._image.getLayoutParams().width = 120;
				 // child_list.setLayoutParams(new LayoutParams(150,150));
				  break;
			  case DisplayMetrics.DENSITY_XXHIGH:
				  _holder._image.getLayoutParams().height = 120;
				  _holder._image.getLayoutParams().width = 120;
				 // child_list.setLayoutParams(new LayoutParams(150,150));
				  break;
			  }*/
			
			return convertView;
		}
	}
}
