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

public class Friend_profile extends android.support.v4.app.Fragment{
	 EditText parentdob,parentlocation,parenttype,phoneNo;
	    EditText txt_name;
	    Button btn_update,addmore_freetime;
	    ListView child_list;
	    ArrayList<Getcategory>child_list_detail;
	    String name,dob,location,freetime,url,guardiantype,firstname,lastname,userguardiantype,userfreetime,userphone_no,strURL;
	    ImageView img;
	    com.iapptechnologies.time.util.ImageLoader imgLoader;
	 @Override
		public View onCreateView(final LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			ViewGroup view = (ViewGroup) inflater.inflate(R.layout.parent_profile, container,
	                false);
			
			    parentdob=(EditText)view.findViewById(R.id.textView_dob_parent);
		        child_list_detail=new ArrayList<Getcategory>();
		        parentlocation=(EditText)view.findViewById(R.id.textView_location_parent);
		       // parentfreetime=(EditText)view.findViewById(R.id.textView_freetime_parent);
		        parenttype=(EditText)view.findViewById(R.id.textView_user_parent);
		        txt_name=(EditText)view.findViewById(R.id.parent_parentname);
		        child_list=(ListView)view.findViewById(R.id.listView_parent_profile_child);
		        btn_update=(Button)view.findViewById(R.id.button_save_parentupdate);
		      //  addmore_freetime=(Button)view.findViewById(R.id.button1_add_moredays_parent);
		        phoneNo=(EditText)view.findViewById(R.id.textView_parent_phone_no);
		        child_list.setDivider(null);
		        child_list.setDividerHeight(0);
		      //  addmore_freetime.setVisibility(View.GONE);
		        btn_update.setVisibility(View.GONE);
			 name = getArguments().getString("name"); 
			 firstname = getArguments().getString("firstname"); 
			
			 dob = getArguments().getString("dob"); 
			 location = getArguments().getString("location"); 
			// freetime = getArguments().getString("freetime"); 
			 strURL = getArguments().getString("url"); 
			 guardiantype = getArguments().getString("guardiantype"); 
			
			 LazyAdapter adapter;
			
			 parentdob.setFocusable(false);
			 parentlocation.setFocusable(false);
			// parentfreetime.setFocusable(false);
			 parenttype.setFocusable(false);
			 txt_name.setFocusable(false);
			 phoneNo.setFocusable(false);
			
			 DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			 Date date_of_birth=null;
			 try {
				 date_of_birth=sdf.parse(dob);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//String reportDate = df.format(today);
			// birthDay=sdf.format(date_of_birth);
			 DateFormat destDf = new SimpleDateFormat("dd/MM/yy");
			
			 String dob_formated = null;
		
			   try {
				    dob_formated = destDf.format(date_of_birth);
			} catch (Exception e) {
				// TODO: handle exception
			}          // format the date into another format
			
			
			 
			 
			 
			 try {
				 txt_name.setText(name.toUpperCase());
			} catch (Exception e) {
				// TODO: handle exception
			}
			 try {
				  parentlocation.setText(location.toUpperCase());
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		      
		        parentdob.setText(dob_formated);
		      //  parentfreetime.setText(freetime.toUpperCase());
		
	       
	        
		        try {
		        	if (guardiantype.equalsIgnoreCase("f")||guardiantype.equalsIgnoreCase("male")) {
		        		guardiantype = "FATHER";
		        	}else if(guardiantype.equalsIgnoreCase("m")||guardiantype.equalsIgnoreCase("female")) {
		        		guardiantype = "MOTHER";
		        	}else if(guardiantype.equalsIgnoreCase("b")) {
		        		guardiantype = "BROTHER";
		        	}
		        	else if(guardiantype.equalsIgnoreCase("s")) {
		        		guardiantype = "SISTER";
		        	}
		        	else if(guardiantype.equalsIgnoreCase("n")) {
		        		guardiantype = "NANNY";
		        	}
		        	else if(guardiantype.equalsIgnoreCase("gm")) {
		        		guardiantype = "GRAND MOTHER";
		        	}
		        	else if(guardiantype.equalsIgnoreCase("GF")) {
		        		guardiantype = "GRAND FATHER";
		        	}
		        	else if(guardiantype.equalsIgnoreCase("T")) {
		        		guardiantype = "TEACHER";
		        	}
		        	else if(guardiantype.equalsIgnoreCase("o")) {
		        		guardiantype = "OTHER";
		        	}
		        } catch (Exception e) {
		        	// TODO: handle exception
		        }
	       
	        parenttype.setText(guardiantype.toUpperCase());
	        parentdob.setEnabled(false);
	        parentlocation.setEnabled(false);
	      //  parentfreetime.setEnabled(false);
	        txt_name.setEnabled(false);
	        phoneNo.setEnabled(false);
	        
	        
	        img=(ImageView)view.findViewById(R.id.imageView_parent);
			 imgLoader = new com.iapptechnologies.time.util.ImageLoader(getActivity());
			
			 imgLoader.DisplayImage(strURL, img);
			 img.requestLayout();
			 child_list.requestLayout();
			 
			 Display display = getActivity().getWindowManager().getDefaultDisplay();
				
				int height = display.getHeight();
				int width = display.getWidth();
				
				System.out.println(height  +  "   "+width);
				
				float mReqPercentages = 35;
				float mCalculatedWidth = (mReqPercentages / 100) * width;

				float mReqPercentages1 = 40;
				float mCalculatedWidth1 = (mReqPercentages1 / 100) * width;

				
				img.getLayoutParams().height = (int) (mCalculatedWidth);
				img.getLayoutParams().width = (int) mCalculatedWidth;
				child_list.getLayoutParams().height = (int)mCalculatedWidth;
				child_list.getLayoutParams().width = (int)mCalculatedWidth1;
			/* int density = getResources().getDisplayMetrics().densityDpi;
			  switch (density) {
			  case DisplayMetrics.DENSITY_LOW:
				  img.getLayoutParams().height = 100;
				  img.getLayoutParams().width = 100;
				  //child_list.setLayoutParams(new LayoutParams(70,70));
				  child_list.getLayoutParams().height = 100;
				  //child_list.getLayoutParams().width = 70;
				  break;
			  case DisplayMetrics.DENSITY_MEDIUM:
				  img.getLayoutParams().height = 180;
				  img.getLayoutParams().width = 180;
				  child_list.getLayoutParams().height = 180;
				 // child_list.getLayoutParams().width = 90;
				 // child_list.setLayoutParams(new LayoutParams(90,90));
				  break;
			  case DisplayMetrics.DENSITY_HIGH:
				  img.getLayoutParams().height = 180;
				  img.getLayoutParams().width = 180;
				  child_list.getLayoutParams().height = 180;
				 // child_list.getLayoutParams().width = 110;
				  //child_list.setLayoutParams(new LayoutParams(110,110));
				  break;
			  case DisplayMetrics.DENSITY_XHIGH:
				  img.getLayoutParams().height = 250;
				  img.getLayoutParams().width = 250;
				  child_list.getLayoutParams().height = 250;
				  //child_list.getLayoutParams().width = 150;
				 // child_list.setLayoutParams(new LayoutParams(150,150));
				  break;
			  case DisplayMetrics.DENSITY_XXHIGH:
				  img.getLayoutParams().height = 250;
				  img.getLayoutParams().width = 250;
				  child_list.getLayoutParams().height = 250;
				  //child_list.getLayoutParams().width = 150;
				 // child_list.setLayoutParams(new LayoutParams(150,150));
				  break;
			  }*/
	        
			  child_list_detail=GlobalVariable.global_list_child;
			  adapter = new LazyAdapter(getActivity(), child_list_detail);
			  child_list.setAdapter(adapter);
	        
			  
			  child_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					 ArrayList<Getcategory> _items;
					 //parentdob,parentlocation,parentfreetime,parenttype,phoneNo
				        _items=child_list_detail;
				      String parent_name=txt_name.getText().toString();  
				      String date_of_birth_of_parent=parentdob.getText().toString();  
				      String parent_type=parenttype.getText().toString();  
				      String parent_location=parentlocation.getText().toString();  
				    //  String parent_freetime=parentfreetime.getText().toString();  
				      if (parent_type.equals("") || parent_type.equals(null)) {
							parent_type = "";
						}else if(parent_type.equalsIgnoreCase("FATHER")){
							guardiantype="f";
						}else if(parent_type.equalsIgnoreCase("MOTHER")){
							guardiantype="m";
						}else if(parent_type.equalsIgnoreCase("BROTHER")){
							parent_type="b";
						}else if(guardiantype.equalsIgnoreCase("SISTER")){
							parent_type="s";
						}else if(guardiantype.equalsIgnoreCase("NANNY")){
							parent_type="n";
						}else if(guardiantype.equalsIgnoreCase("GRAND FATHER")){
							parent_type="gf";
						}else if(guardiantype.equalsIgnoreCase("GRAND MOTHER")){
							parent_type="gm";
						}
						else if(guardiantype.equalsIgnoreCase("TEACHER")){
							parent_type="t";
						}else if(guardiantype.equalsIgnoreCase("OTHER")){
							parent_type="o";
						}
				      
				      
				      
					String child_name=_items.get(arg2).child_name;
					String Child_id=_items.get(arg2).child_id;
					String Child_profilepic=_items.get(arg2).profile_image;
					String dob=_items.get(arg2).date_of_birth;
					//String conditions=_items.get(arg2).condition_child;
					//String hobbies=_items.get(arg2).hobbies_child;
					String allergies=_items.get(arg2).allergies_child;
					//String school=_items.get(arg2).school_child;
					//String youthclub=_items.get(arg2).youthclub_child;
					//String free_time=_items.get(arg2).free_time;
					
					//String guardian_id=_items.get(arg2).guardian_child;
					
					 
					
					Bundle bundle = new Bundle();
					bundle.putString("child_name", child_name);
					bundle.putString("Child_id", Child_id);
					bundle.putString("Child_profilepic", Child_profilepic);
					bundle.putString("dob", dob);
					//bundle.putString("conditions", conditions);
					//bundle.putString("hobbies", hobbies);
					
					bundle.putString("allergies", allergies);
				//	bundle.putString("school", school);
				//	bundle.putString("youthclub", youthclub);
				//	bundle.putString("free_time", free_time);
					bundle.putString("parent_name", parent_name);
					bundle.putString("parent_profilepic", strURL);
					//bundle.putString("parent_freetime","");
					
					bundle.putString("parent_dob", date_of_birth_of_parent);
					bundle.putString("parent_type", parent_type);
					bundle.putString("parent_location",parent_location);
				
					
					
					
					
					
					
					
					
					
					  android.support.v4.app.Fragment  fragment=new Child_Friend_profile();
	                  fragment.setArguments(bundle);
	                  android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
	  				android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
	  				fragmentTransaction.replace(R.id.content_frame, fragment);
	  				fragmentTransaction.addToBackStack("first6");
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
