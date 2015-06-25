package com.iapp.playdate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
 
public class ConnectionDetector {
     
    private Context _context;
     
    public ConnectionDetector(Context context){
        this._context = context;
    }
 
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null) 
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null) 
                  for (int i = 0; i < info.length; i++) 
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    	
    	/*ConnectivityManager connectivityManager = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || 
    	            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
    	        //we are connected to a network
    	       // connected = true;
    	    	return true;
    	    }
    	   
    	       return false;
    	       */
    	       
    	       
    	  /*     boolean haveConnectedWifi = false;
    	       boolean haveConnectedMobile = false;

    	       ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	       NetworkInfo[] netInfo = cm.getAllNetworkInfo();
    	       for (NetworkInfo ni : netInfo) {
    	           if (ni.getTypeName().equalsIgnoreCase("WIFI"))
    	               if (ni.isConnected()){
    	                   haveConnectedWifi = true;
    	           return true;
    	               }
    	           if (ni.getTypeName().equalsIgnoreCase("MOBILE")){
    	               if (ni.isConnected())
    	                   haveConnectedMobile = true;
    	           return true;
    	           }
    	       }
    	       return false;
    	       
    	       */
    }
}