package com.playdate.contactlist;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
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
			
			
			
			
			
			int position_1 = mListView.getPositionForView((View) v.getParent());
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
				Toast.makeText(context,"Phone number does not exists", 2000).show();
			}else{
				
				
				// Create custom dialog object
                final Dialog dialog = new Dialog(context);
                // Include dialog.xml file
                dialog.setContentView(R.layout.sms_popup);
                // Set dialog title
                dialog.setTitle("Send Message");
 
                final EditText text = (EditText) dialog.findViewById(R.id.editTextMessage_typed);
                text.setText("I've started using Playdate by JAGO, an awesome app to arrange & manage my kids lives.  Check it out here www.jago.nu");
                Button send = (Button) dialog.findViewById(R.id.buttonSend_SMS);
 
                dialog.show();
                 
                send.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                    	String message_trim="";
                        String message=text.getText().toString();
                        try {
                        	message_trim=message.replace(" ","" );
						} catch (Exception e) {
							// TODO: handle exception
						}
                         
                        
                        if(message_trim.equals(null)||message_trim.equals("")){
                        	
                        }else{
                        	
                        
        		        String SENT = "SMS_SENT";
        		        String DELIVERED = "SMS_DELIVERED";
        		 
        		        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
        		            new Intent(SENT), 0);
        		 
        		        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
        		            new Intent(DELIVERED), 0);
        		
        		        //---when the SMS has been sent---
        		        context.registerReceiver(new BroadcastReceiver(){
        		            @Override
        		            public void onReceive(Context arg0, Intent arg1) {
        		                switch (getResultCode())
        		                {
        		                    case Activity.RESULT_OK:
        		                        Toast.makeText(context, "SMS sent", 
        		                                Toast.LENGTH_SHORT).show();
        		                        break;
        		                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
        		                        Toast.makeText(context, "Generic failure", 
        		                                Toast.LENGTH_SHORT).show();
        		                        break;
        		                    case SmsManager.RESULT_ERROR_NO_SERVICE:
        		                        Toast.makeText(context, "No service", 
        		                                Toast.LENGTH_SHORT).show();
        		                        break;
        		                    case SmsManager.RESULT_ERROR_NULL_PDU:
        		                        Toast.makeText(context, "Null PDU", 
        		                                Toast.LENGTH_SHORT).show();
        		                        break;
        		                    case SmsManager.RESULT_ERROR_RADIO_OFF:
        		                        Toast.makeText(context, "Radio off", 
        		                                Toast.LENGTH_SHORT).show();
        		                        break;
        		                }
        		            }
        		        }, new IntentFilter(SENT));
        		 
        		        //---when the SMS has been delivered---
        		        context.registerReceiver(new BroadcastReceiver(){
        		            @Override
        		            public void onReceive(Context arg0, Intent arg1) {
        		                switch (getResultCode())
        		                {
        		                    case Activity.RESULT_OK:
        		                        Toast.makeText(context, "SMS delivered", 
        		                                Toast.LENGTH_SHORT).show();
        		                        break;
        		                    case Activity.RESULT_CANCELED:
        		                        Toast.makeText(context, "SMS not delivered", 
        		                                Toast.LENGTH_SHORT).show();
        		                        break;                        
        		                }
        		            }
        		        }, new IntentFilter(DELIVERED));        
        		 
        		        SmsManager sms = SmsManager.getDefault();
        		        sms.sendTextMessage(phonenumber_sms, null, text.getText().toString(), sentPI, deliveredPI); 
                    	
                        
                        dialog.dismiss();
                        }
                    }
                });
 
                 
            
 
				
				
				
				 	
		      //SmsManager smsManager = SmsManager.getDefault();
	      		//smsManager.sendTextMessage(phonenumber, null, "play date", null, null);
			/*	Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			      smsIntent.setData(Uri.parse("smsto:"));
			      smsIntent.setType("vnd.android-dir/mms-sms");

			      smsIntent.putExtra("address"  , phonenumber);
			      smsIntent.putExtra("sms_body"  , "Test SMS to Angilla");*/
			     /* try {
			    	  SmsManager smsManager = SmsManager.getDefault();
			      		smsManager.sendTextMessage(phonenumber, null, "play date", null, null);
			    	 // context.startActivity(smsIntent);
			       
			         Log.i("Finished sending SMS...", "");
			      } catch (Exception ex) {
			         Toast.makeText(context, 
			         "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
			      }
*/		
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
