package com.iapp.playdate;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iapp.playdate.R;
import com.iapp.playdate.Child_profile.unlink_child;
public class UserListAdapter extends BaseAdapter {

	private static final String TAG = UserListAdapter.class.getName();
	private Activity activity;
	private Vector<Model_contactlist> items;
	private ArrayList<Model_friend_list> friend_list;
	private ListView mListView;
	Context context;
	String phonenumber_sms;
	public UserListAdapter(Activity activity,
			Vector<Model_contactlist> items,ListView mListView,Context context,ArrayList<Model_friend_list> friend_list) {
	    Log.i(TAG, TAG);
		this.activity = activity;
		this.items = items;
		this.mListView=mListView;
		this.context=context;
		this.friend_list=friend_list;
	}


	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		//if (convertView == null)
		{

			LayoutInflater inflater = activity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.listrow_user, null);
			holder = new ViewHolder();
			holder.sms=(Button)convertView.findViewById(R.id.sms_button);
			holder.name = (TextView) convertView.findViewById(R.id.nameTV);
			holder.headingLL = (LinearLayout)convertView.findViewById(R.id.headingLL);
			holder.headingTV = (TextView)convertView.findViewById(R.id.headingTV);
			holder.nameLL = (RelativeLayout)convertView.findViewById(R.id.nameLL);
			holder.sms.setOnClickListener(clicklistener);
			convertView.setTag(holder);
		}
		/*else {
			holder = (ViewHolder) convertView.getTag();
		}*/
		if (position < items.size()) {
			final Model_contactlist subsidies = items.get(position);
			if (subsidies != null && (subsidies.getName().length() == 1)) 
			{
				global.arrayforTitle.add(position);
				holder.nameLL.setVisibility(View.GONE);
				holder.headingLL.setBackgroundColor(Color.parseColor("#f3f3f3"));
				holder.headingLL.setVisibility(View.VISIBLE);
				
				holder.headingTV.setText(subsidies.getName());
				
			}
			else
			{
				String phonenumber=subsidies.getphonenumber();
				if(phonenumber.contains("-")){
					phonenumber=phonenumber.replace("-","");
				}
               if(phonenumber.contains(" ")){
            	   phonenumber=phonenumber.replace(" ", "");
               }
				//phonenumber=phonenumber.trim();
				System.out.println("................"+phonenumber);
				String email=subsidies.getemail();
                boolean check_background=false;
			    int i = 0;
			    for (Model_friend_list temp : friend_list) {
			    if(temp.friend_phone_number.equals(phonenumber)){
			    	 holder.sms.setBackgroundResource(R.drawable.playdate_button);
			    	 check_background=true;
			    	 
			    }
			    i++;
			    }
			    if(!check_background){
			    	int j=0;
			    	for (Model_friend_list temp : friend_list) {
					    if(temp.friend_email.equals(email)){
					    	 holder.sms.setBackgroundResource(R.drawable.playdate_button);
					    	 check_background=true;
					    }
					    j++;
					    }
			    }
				holder.nameLL.setVisibility(View.VISIBLE);
				holder.headingLL.setVisibility(View.GONE);
				holder.name.setText(subsidies.getName());	
				
				
				
			}
		}
		

		return convertView;
	}
	private OnClickListener clicklistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			
			
			
			
			final int position_1 = mListView.getPositionForView((View) v.getParent());
			 phonenumber_sms=items.get(position_1).getphonenumber();
			String email=items.get(position_1).getemail();
			if(phonenumber_sms.contains("-")){
				phonenumber_sms=phonenumber_sms.replace("-","");
			}
           if(phonenumber_sms.contains(" ")){
        	   phonenumber_sms=phonenumber_sms.replace(" ", "");
           }
           boolean check_button=false;
           int i1 = 0;
		    for (Model_friend_list temp : friend_list) {
		    if(temp.friend_phone_number.equals(phonenumber_sms)){
              check_button=true;		    	 
		    }
		    i1++;
		    }
		    if(!check_button){
		    	int j1=0;
		    	for (Model_friend_list temp : friend_list) {
				    if(temp.friend_email.equals(email)){
				    check_button=true;	
				    }
				    j1++;
				    }
		    }
			if(!check_button){
				
			if(phonenumber_sms.equals(null)||phonenumber_sms.equals("")){
				
				if(items.get(position_1).getemail().equals(null)||items.get(position_1).getemail().equals("")){
					Toast.makeText(context,"Phone number or email id does not exists", 2000).show();
				}else{
					Intent email1 = new Intent(Intent.ACTION_SEND);
	    			  email1.putExtra(Intent.EXTRA_EMAIL, new String[]{ items.get(position_1).getemail()});
	    			 
	    			  email1.putExtra(Intent.EXTRA_SUBJECT, "Fast Playdate Planning from JAGO.");
	    			  email1.putExtra(Intent.EXTRA_TEXT, "I'm using a new app 'Playdate' to plan the kids schedule, join me by downloading at http://www.playdateapp.co it makes it easier & fun !!");
	     
	    			  //need this to prompts email client only
	    			  email1.setType("message/rfc822");
	     
	    			  activity.startActivity(Intent.createChooser(email1, "Choose an Email client :"));
	    			  
				}
				
				
			}else{
				
				
              if(items.get(position_1).getemail().equals(null)||items.get(position_1).getemail().equals("")){
            	  Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                  i.putExtra("address", phonenumber_sms);
                  i.putExtra("sms_body", "I'm using a new app 'Playdate' to plan the kids schedule, join me by downloading at http://www.playdateapp.co it makes it easier & fun !!");
                  i.setType("vnd.android-dir/mms-sms");
                  activity.startActivity(i);
				
				}else{
					 
					
					new AlertDialog.Builder(activity)
					.setTitle("INVITE")
					.setMessage("Invitation send via ")
					.setPositiveButton("SMS",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									 Intent i = new Intent(android.content.Intent.ACTION_VIEW);
				                     i.putExtra("address", phonenumber_sms);
				                     i.putExtra("sms_body", "I'm using a new app 'Playdate' to plan the kids schedule, join me by downloading at http://www.playdateapp.co it makes it easier & fun !!");
				                     i.setType("vnd.android-dir/mms-sms");
				                     activity.startActivity(i);
								}
							})
					.setNegativeButton("E MAIL",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									  Intent email1 = new Intent(Intent.ACTION_SEND);
					    			  email1.putExtra(Intent.EXTRA_EMAIL, new String[]{ items.get(position_1).getemail()});
					    			  email1.putExtra(Intent.EXTRA_SUBJECT, "Fast Playdate Planning from JAGO.");
					    			  email1.putExtra(Intent.EXTRA_TEXT, "I'm using a new app 'Playdate' to plan the kids schedule, join me by downloading at http://www.playdateapp.co it makes it easier & fun !!");
					    			  email1.setType("message/rfc822");
					     
					    			  activity.startActivity(Intent.createChooser(email1, "Choose an Email client :"));
									
								}
							})
					.show();

					
					
				}
				
                	
                    
				
				
			
			}
			
		}else{
		}
		}
	};

	private static class ViewHolder {
		TextView name,headingTV;
		RelativeLayout nameLL;LinearLayout headingLL;
		Button sms;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
