package com.iapp.playdate;

import com.iapp.playdate.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class Send_mail extends android.support.v4.app.Fragment {
	
	
	Button buttonSend;
	//EditText textTo;
	
	EditText textMessage;
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//FragmentManager fragmentManager;
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.sendmail,
				container, false);
		

		buttonSend = (Button) view.findViewById(R.id.buttonSend);
		//textTo = (EditText)view. findViewById(R.id.editTextTo);
		
		textMessage = (EditText) view.findViewById(R.id.editTextMessage);
		


		
		
		buttonSend.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
 
			 // String to = textTo.getText().toString();
			  String subject = "Playdate Feedback";
			  String message = textMessage.getText().toString();
              String to="jagoapps@gmail.com";
              
              if(message.equals("")||message.equals(null)){
            	  
              }else{
            	  Intent email = new Intent(Intent.ACTION_SEND);
    			  email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
    			  //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
    			  //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
    			  email.putExtra(Intent.EXTRA_SUBJECT, subject);
    			  email.putExtra(Intent.EXTRA_TEXT, message);
     
    			  //need this to prompts email client only
    			  email.setType("message/rfc822");
     
    			  startActivity(Intent.createChooser(email, "Choose an Email client :"));
    			  
    			  
    			 
    			  textMessage.setText(""); 
              }
			 
			  
			  
 
			}
		});
 
		
		
		
		return view;
	}
}
