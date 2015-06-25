package com.iapp.playdate;

import com.iapp.playdate.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Send_SMS extends android.support.v4.app.Fragment {
	
		
		
		Button buttonSend;
		//EditText textTo;
		EditText phone_number;
		EditText textMessage;
		@Override
		public View onCreateView(final LayoutInflater inflater,
				final ViewGroup container, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			//FragmentManager fragmentManager;
			ViewGroup view = (ViewGroup) inflater.inflate(R.layout.send_sms,
					container, false);
			

			buttonSend = (Button) view.findViewById(R.id.buttonSend_SMS);
			//textTo = (EditText)view. findViewById(R.id.editTextTo);
			phone_number = (EditText) view.findViewById(R.id.editTextSubject_phone_number);
			textMessage = (EditText) view.findViewById(R.id.editTextMessage_typed);
			textMessage.setText("I�ve started using Playdate by JAGO, an awesome app to arrange & manage my kids lives.  Check it out here www.jago.nu");


			
			
			buttonSend.setOnClickListener(new OnClickListener() {
				 
				@Override
				public void onClick(View v) {
	 
				 // String to = textTo.getText().toString();
				  String phonenumber = phone_number.getText().toString();
				  String message = textMessage.getText().toString();
				  String trim="";
				  try {
					   trim=message.replace(" ", "");
				} catch (Exception e) {
					// TODO: handle exception
				}
	             
				  
				  if(trim.equals("")||trim.equals(null)){
					  
				  }else{
					  try {
						  
						  String SENT = "SMS_SENT";
	        		        String DELIVERED = "SMS_DELIVERED";
	        		 
	        		        PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0,
	        		            new Intent(SENT), 0);
	        		 
	        		        PendingIntent deliveredPI = PendingIntent.getBroadcast(getActivity(), 0,
	        		            new Intent(DELIVERED), 0);
	        		
	        		        //---when the SMS has been sent---
	        		        getActivity().registerReceiver(new BroadcastReceiver(){
	        		            @Override
	        		            public void onReceive(Context arg0, Intent arg1) {
	        		                switch (getResultCode())
	        		                {
	        		                    case Activity.RESULT_OK:
	        		                        Toast.makeText(getActivity(), "SMS sent", 
	        		                                Toast.LENGTH_SHORT).show();
	        		                        break;
	        		                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	        		                        Toast.makeText(getActivity(), "Generic failure", 
	        		                                Toast.LENGTH_SHORT).show();
	        		                        break;
	        		                    case SmsManager.RESULT_ERROR_NO_SERVICE:
	        		                        Toast.makeText(getActivity(), "No service", 
	        		                                Toast.LENGTH_SHORT).show();
	        		                        break;
	        		                    case SmsManager.RESULT_ERROR_NULL_PDU:
	        		                        Toast.makeText(getActivity(), "Null PDU", 
	        		                                Toast.LENGTH_SHORT).show();
	        		                        break;
	        		                    case SmsManager.RESULT_ERROR_RADIO_OFF:
	        		                        Toast.makeText(getActivity(), "Radio off", 
	        		                                Toast.LENGTH_SHORT).show();
	        		                        break;
	        		                }
	        		            }
	        		        }, new IntentFilter(SENT));
	        		 
	        		        //---when the SMS has been delivered---
	        		        getActivity().registerReceiver(new BroadcastReceiver(){
	        		            @Override
	        		            public void onReceive(Context arg0, Intent arg1) {
	        		                switch (getResultCode())
	        		                {
	        		                    case Activity.RESULT_OK:
	        		                        Toast.makeText(getActivity(), "SMS delivered", 
	        		                                Toast.LENGTH_SHORT).show();
	        		                        break;
	        		                    case Activity.RESULT_CANCELED:
	        		                        Toast.makeText(getActivity(), "SMS not delivered", 
	        		                                Toast.LENGTH_SHORT).show();
	        		                        break;                        
	        		                }
	        		            }
	        		        }, new IntentFilter(DELIVERED));        
	        		 
	        		        SmsManager sms = SmsManager.getDefault();
	        		        sms.sendTextMessage(phonenumber, null, textMessage.getText().toString(), sentPI, deliveredPI); 
	                    	
						 /* SmsManager smsManager = SmsManager.getDefault();
				      		smsManager.sendTextMessage(phonenumber, null, message, null, null);*/
				      		phone_number.setText("");
				      		
					} catch (Exception e) {
						// TODO: handle exception
					}
					  
				  }
	              
	      		
	      			 
				}
			});
	 
			
			
			
			return view;
		}
	

}
