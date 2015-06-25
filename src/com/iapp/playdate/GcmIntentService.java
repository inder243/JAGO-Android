package com.iapp.playdate;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.iapp.playdate.R;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static String BROADCAST_ACTION = "com.iapptechnologies.time.BroadCast";
    Database_Handler db;
    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	
    	
    	 db=new Database_Handler(getApplicationContext());
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
               /* for (int i=0; i<5; i++) {
                    Log.i("", "Working... " + (i+1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }}*/
                
                Log.i("", "Completed work @ " + SystemClock.elapsedRealtime());
                String message=null;
                message = intent.getExtras().getString("message");
                Log.e("Message==",""+message);
                Intent broadcast = new Intent(BROADCAST_ACTION);
        	    if(message!=null && message.length()>0)
        	    {
        	    	if(message.equalsIgnoreCase("Playdate Requested")||message.equalsIgnoreCase("Playdate Accepted")||message.equalsIgnoreCase("Playdate Rejected")||message.equalsIgnoreCase("Playdate Updated")||message.equalsIgnoreCase("Set Created")||message.equalsIgnoreCase("Child added to your profile")||message.equalsIgnoreCase("Set updated"))
        	    	{ db.insert_notification(message);
        	    	}
        	  //  broadcast.putExtra("Message", message);	
        	    Log.e("Response==",""+message);
        	    }
               // sendBroadcast(broadcast);
                // Post notification of received message.
                sendNotification( message/*extras.toString()*/);
                Log.i("", "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    
    }
    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
       /* mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, FacebookLogin.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("GCM Notification")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
       // mBuilder.flags |= Notification.FLAG_AUTO_CANCEL;
        
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());*/
    	/* if (Session.getActiveSession() != null){
    	        Session.getActiveSession().closeAndClearTokenInformation();
    	 }*/
    	  int icon = R.drawable.app_icon;
          long when = System.currentTimeMillis();
          NotificationManager notificationManager = (NotificationManager)
                  this.getSystemService(Context.NOTIFICATION_SERVICE);
          Notification notification = new Notification(icon, msg, when);
          String title = this.getString(R.string.app_name);
          Intent notificationIntent = null;
         
        	  notificationIntent = new Intent(this, Splash_screen.class);
        	  notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                      Intent.FLAG_ACTIVITY_SINGLE_TOP);
        	  PendingIntent intent =
                      PendingIntent.getActivity(this, 0, notificationIntent, 0);
              notification.setLatestEventInfo(this, title, msg, intent);
              notification.defaults |= Notification.DEFAULT_SOUND;
              notification.defaults |= Notification.DEFAULT_VIBRATE;
              notification.flags |= Notification.FLAG_AUTO_CANCEL;
              notificationManager.notify(0, notification);
         
          // set intent so it does not start a new activity
         
          
    }
}