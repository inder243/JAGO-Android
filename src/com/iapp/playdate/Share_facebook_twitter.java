package com.iapp.playdate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.iapp.playdate.R;


public class Share_facebook_twitter extends android.support.v4.app.Fragment {
	Float x1, y1, x2, y2;
	Button facebook_share, twittershare, email_share, sms_share;
	 Boolean isInternetPresent = false;
	    ConnectionDetector cd;
		
		String videoLogIn="";
		public static final String PREFS_NAME = "MyPrefsFile";
	    String url;
	    
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	
	
    private static SharedPreferences mSharedPreferences;
	    
	public Share_facebook_twitter() {
		// TODO Auto-generated constructor stub

	}

	@SuppressWarnings("unused")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.share_facebook_twitter, container, false);

		
		mSharedPreferences = getActivity().getSharedPreferences(
                "MyPref", 0);
		facebook_share = (Button) view.findViewById(R.id.button_facebook_share);
		twittershare = (Button) view.findViewById(R.id.button_twitter_share);
		email_share = (Button) view.findViewById(R.id.button_email_share);
		sms_share = (Button) view.findViewById(R.id.button_sms_share);

		  cd = new ConnectionDetector(getActivity());
			 isInternetPresent = cd.isConnectingToInternet();
			 
	         
	         if (isInternetPresent) {

		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(getActivity(), null, statusCallback,
						savedInstanceState);
			}
			if (session == null) {
				session = new Session(getActivity());
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
			System.out.println(session);
				/*session.openForRead(new Session.OpenRequest(this)
						.setPermissions(Arrays.asList("email")).setCallback(
								statusCallback));*/
				/*session.openForRead(new Session.OpenRequest(this).setPermissions(
						Arrays.asList("id", "email", "firstname", "lastname"))
						.setCallback(statusCallback));*/
				 session.openForRead(new Session.OpenRequest(getActivity()).setCallback(statusCallback));
			}
		}
	       
		 
		
		facebook_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 if (isInternetPresent) {
				onClickLogin();
				final Session session = Session.getActiveSession();
				if (session.isOpened()) {/*
				Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
				
				System.out.println(session);

				Bundle parameters = new Bundle();
				parameters.putString("message", "Send Request");
				System.out.println(parameters);

				WebDialog.Builder builder = new Builder(Share_facebook_twitter.this, Session.getActiveSession(),
				                                "apprequests", parameters);
				WebDialog.Builder builder = new Builder(getActivity(), Session.getActiveSession(), "apprequest", parameters);

				builder.setOnCompleteListener(new OnCompleteListener() {

				 
					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						// TODO Auto-generated method stub
						if (error != null){
				            if (error instanceof FacebookOperationCanceledException){
				               // Toast.makeText(Share_facebook_twitter.this,"Request cancelled",Toast.LENGTH_SHORT).show();
				            	System.out.println("request cancled");
				            }
				            else{
				              //  Toast.makeText(Share_facebook_twitter.this,"Network Error",Toast.LENGTH_SHORT).show();
				            	System.out.println("network error");
				            }
				        }
				        else{

				            final String requestId = values.getString("request");
				            if (requestId != null) {
				               // Toast.makeText(Share_facebook_twitter.this,"Request sent",Toast.LENGTH_SHORT).show();
				            	System.out.println("Request send");
				            } 
				            else {
				              //  Toast.makeText(Share_facebook_twitter.this,"Request cancelled",Toast.LENGTH_SHORT).show();
				            	System.out.println("request cancelled");
				            }
				        }                       
					}
				});

				WebDialog webDialog = builder.build();
				webDialog.show();
			*/
					publishFeedDialog();
					}
			}else {
		       	 showAlertDialog(getActivity(), "No Internet Connection",
		                    "You don't have internet connection.", false);
		           
				}
}
		});
		twittershare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
if(isInternetPresent){
	
	/* loginToTwitter();*/
	
	 Intent shareIntent = new Intent();
     shareIntent.setAction(Intent.ACTION_SEND);
     shareIntent.setType("text/plain");
     shareIntent.putExtra(Intent.EXTRA_TEXT, " �I�ve started using Playdate by JAGO, an awesome app to arrange & manage my kids social lives.  Check it out and get your download here www.jago.nu�");
     startActivity(Intent.createChooser(shareIntent, "Share your thoughts"));

   /*  mTwitter = new TwitterApp(getActivity(), CONSUMER_KEY, CONSUMER_SECRET);
     SharedPreferences pref = getActivity().getSharedPreferences(PREFS_NAME, 0);
   	videoLogIn=pref.getString("VideoLogIn","");
   	Log.e("VideoLogIn=========",""+videoLogIn);
     
   	 
  	mTwitter.setListener(mTwLoginDialogListener);
     mTwitter.resetAccessToken();
    
	        if (mTwitter.hasAccessToken() == true)
	        {
	            try
	            {
	                mTwitter.updateStatus("PlayDate Application");
	                postAsToast(FROM.TWITTER_POST, MESSAGE.SUCCESS);
	            } 
	            catch (Exception e)
	            {
	                if (e.getMessage().toString().contains("duplicate"))
	                {
	                    postAsToast(FROM.TWITTER_POST, MESSAGE.DUPLICATE);
	                }
	                e.printStackTrace();
	            }
	            mTwitter.resetAccessToken();
	        } else 
	        {
	        mTwitter.authorize();
	        }*/
	
	/* try {
         Twitter twitter = new TwitterFactory().getInstance();
         try {
             // get request token.
             // this will throw IllegalStateException if access token is already available
             RequestToken requestToken = twitter.getOAuthRequestToken();
             System.out.println("Got request token.");
             System.out.println("Request token: " + requestToken.getToken());
             System.out.println("Request token secret: " + requestToken.getTokenSecret());
             AccessToken accessToken = null;

             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             while (null == accessToken) {
                 System.out.println("Open the following URL and grant access to your account:");
                 System.out.println(requestToken.getAuthorizationURL());
                 System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                 String pin = br.readLine();
                 try {
                     if (pin.length() > 0) {
                         accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                     } else {
                         accessToken = twitter.getOAuthAccessToken(requestToken);
                     }
                 } catch (TwitterException te) {
                     if (401 == te.getStatusCode()) {
                         System.out.println("Unable to get the access token.");
                     } else {
                         te.printStackTrace();
                     }
                 }
             }
             System.out.println("Got access token.");
             System.out.println("Access token: " + accessToken.getToken());
             System.out.println("Access token secret: " + accessToken.getTokenSecret());
         } catch (IllegalStateException ie) {
             // access token is already available, or consumer key/secret is not set.
             if (!twitter.getAuthorization().isEnabled()) {
                 System.out.println("OAuth consumer key/secret is not set.");
                 System.exit(-1);
             }
         }
         Status status = twitter.updateStatus("PLAYDATE APPLICATION");
         System.out.println("Successfully updated the status to [" + status.getText() + "].");
         System.exit(0);
     } catch (TwitterException te) {
         te.printStackTrace();
         System.out.println("Failed to get timeline: " + te.getMessage());
         System.exit(-1);
     } catch (IOException ioe) {
         ioe.printStackTrace();
         System.out.println("Failed to read the system input.");
         System.exit(-1);
     }*/
	
}else {
  	 showAlertDialog(getActivity(), "No Internet Connection",
             "You don't have internet connection.", false);
    
	}

			}
			
		});
		email_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isInternetPresent){
					 /*Intent email = new Intent(Intent.ACTION_SEND);
					
					  email.setType("message/rfc822");*/
					
		             // String to="jsingh@iapptechnologies.com";
					  Intent email = new Intent(Intent.ACTION_SEND);
					 // email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
					  //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
					  //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
					  email.putExtra(Intent.EXTRA_TEXT, "I�ve started using Playdate by JAGO, an awesome app to arrange & manage my kids social lives.  Check it out and get your download here www.jago.nu");
					  email.putExtra(Intent.EXTRA_SUBJECT, "App Request");
		 
					  //need this to prompts email client only
					  email.setType("message/rfc822");
		 
					  startActivity(Intent.createChooser(email, "Choose an Email client :"));
					
				}else {
				  	 showAlertDialog(getActivity(), "No Internet Connection",
				             "You don't have internet connection.", false);
				    
					}
			}
		});
		sms_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				android.support.v4.app.Fragment  fragment=new Send_SMS();
		       
				android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
				        .replace(R.id.content_frame, fragment).commit();
			}
		});
	        

		final Home home = new Home();

		LinearLayout layout = (LinearLayout) view
				.findViewById(R.id.share_layout);// get your root layout
		layout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent touchevent) {
				Log.v(null, "TOUCH EVENT"); // handle your fragment number here
				switch (touchevent.getAction()) {
				// when user first touches the screen we get x and y coordinate
				case MotionEvent.ACTION_DOWN: {
					System.out.println("fffffffffffffffffffffffff");
					x1 = touchevent.getX();
					y1 = touchevent.getY();
					break;
				}
				case MotionEvent.ACTION_UP: {
					System.out.println("fffffffffffffffffffffffff");
					x2 = touchevent.getX();
					y2 = touchevent.getY();

					if (x1 > x2) {
						System.out.println("right to left swipe");
						home.clickeventimplementfragment();
					}

					break;
				}
				}
				return true;
			}
		});

		
	         }return view;
	}
	
	 /*public boolean isTwitterLoggedInAlready() {
	        // return twitter login status from Shared Preferences
	        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	    }
	private void loginToTwitter() {
		// TODO Auto-generated method stub
		if (!isTwitterLoggedInAlready()) {
         ConfigurationBuilder builder = new ConfigurationBuilder();
         builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
         builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
         Configuration configuration = builder.build();
          
         TwitterFactory factory = new TwitterFactory(configuration);
         twitter = factory.getInstance();

         try {
             requestToken = twitter
                     .getOAuthRequestToken(TWITTER_CALLBACK_URL);
             getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri
                     .parse(requestToken.getAuthenticationURL())));
         } catch (TwitterException e) {
             e.printStackTrace();
         }
     } else {
         // user already logged into twitter
         Toast.makeText(getActivity(),
                 "Already Logged into twitter", Toast.LENGTH_LONG).show();
     }
	}*/
	 private void publishFeedDialog() {

		    Bundle params = new Bundle();
		    params.putString("name", "Playdate App");
		    params.putString("caption", "Playdate App");
		    params.putString("description", "I�ve started using Playdate by JAGO, an awesome app to arrange & manage my kids social lives.  Check it out and get your download here www.jago.nu");
		    params.putString("link", "https://developers.facebook.com/android");
		    params.putString("picture", "http://112.196.34.179/playdate.png");


		      /*  WebDialog feedDialog = (
		                new WebDialog.FeedDialogBuilder(getActivity(),
		                        Session.getActiveSession(),
		                        params))
		                        .setOnCompleteListener(new OnCompleteListener() {

		                            @Override
		                            public void onComplete(Bundle values,FacebookException error) {

		                               
		                            }
		
		       @Override
		       public void onComplete(Bundle values,
		         FacebookException error) {
		        // TODO Auto-generated method stub
		         if (error == null) {
		                                     // When the story is posted, echo the success
		                                     // and the post Id.
		                                     final String postId = values.getString("post_id");
		                                     if (postId != null) {
		                                         //Toast.makeText(PhotoViewer.this,"Posted story, id: "+postId,Toast.LENGTH_SHORT).show();
		                                         Toast.makeText(getActivity(), "Publish Successfully!", Toast.LENGTH_SHORT).show();
		                                     } else {
		                                         // User clicked the Cancel button
		                                         Toast.makeText(getActivity(), "Publish cancelled", Toast.LENGTH_SHORT).show();
		                                     }
		                                 } else if (error instanceof FacebookOperationCanceledException) {
		                                     // User clicked the "x" button
		                                     Toast.makeText(getActivity(), "Publish cancelled", Toast.LENGTH_SHORT).show();
		                                 } else {
		                                     // Generic, ex: network error
		                                     Toast.makeText(getActivity(),"Error posting story",Toast.LENGTH_SHORT).show();
		                                 }
		       }

		                        })
		                        .build();
		        feedDialog.show();*/
		    }
	
	private void onClickLogin()
	{
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
System.out.println("onClick login");
/*session.openForRead(new Session.OpenRequest(this).setPermissions(
					Arrays.asList("id", "email", "firstname", "lastname"))
					.setCallback(statusCallback));*/

			  session.openForRead(new Session.OpenRequest(getActivity()).setCallback(statusCallback));
		} else {
			System.out.println("else block of onclick login");
			Session.openActiveSession(getActivity(), true, statusCallback);

		}
	}
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
       
        alertDialog.setTitle(title);
 
        
        alertDialog.setMessage(message);
       
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
 
       
        alertDialog.show();
    }
	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// updateView();
		}
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}
	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*
		 * super.onActivityResult(requestCode, resultCode, data);
		 * Session.getActiveSession().onActivityResult(this, requestCode,
		 * resultCode, data);
		 */
		super.onActivityResult(requestCode, resultCode, data);
		if (Session.getActiveSession() != null)
			Session.getActiveSession().onActivityResult(getActivity(), requestCode,
					resultCode, data);

		Session currentSession = Session.getActiveSession();
		if (currentSession == null || currentSession.getState().isClosed()) {
			Session session = new Session.Builder(getActivity())
					.build();
			Session.setActiveSession(session);
			currentSession = session;
		}
	}
	
	}
