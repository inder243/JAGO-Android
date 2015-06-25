package com.iapp.playdate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iapp.playdate.R;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

public class IntentChoserBuilder {
	

	    public static final String TAG = IntentChoserBuilder.class.getSimpleName();
	    public static final Map<String, Integer> PRIORITY = new HashMap<String, Integer>();

	    public static final String ANDROID_EMAIL = "com.google.android.email";
	    public static final String FACEBOOK = "com.facebook.katana";
	    public static final String MMS = "com.android.mms";
	    public static final String ANDROID_GM = "com.google.android.gm";
	    public static final String APPS_PLUS = "com.google.android.apps.plus";
	    public static final String TWITTER = "com.twitter.android";
	    public static final String CLIPBOARD = "com.google.android.apps.docs";
	    public static final String WHATSAPP = "com.whatsapp";
	    public static final String LINKEDIN = "com.linkedin.android"; 
	    public static final String HANGOUT = "com.google.android.talk";
	    public static final String FB = "com.facebook.orca";
	    public static final String SKYPE="com.skype.android";
	    //static fields for custom sorting
	    static {
	        PRIORITY.put(CLIPBOARD, 0);//
	        PRIORITY.put(FACEBOOK, 1);
	        PRIORITY.put(TWITTER, 2);
	        PRIORITY.put(APPS_PLUS, 3);
	        PRIORITY.put(ANDROID_EMAIL, 4);
	        PRIORITY.put(ANDROID_GM, 5);
	        PRIORITY.put(MMS, 6);//
	        PRIORITY.put(WHATSAPP, 7);
	        PRIORITY.put(LINKEDIN, 8);
	        PRIORITY.put(HANGOUT, 9);
	        PRIORITY.put(FB, 10);
	        PRIORITY.put(SKYPE, 11);
	    }

	    public static void createChoserIntent(Home fragment, Intent prototype, final PackageManager pm, String EXTRA_TEXT, String EXTRA_SUBJECT) {

	        String[] forbiddenChoices = new String[]{CLIPBOARD, FACEBOOK, TWITTER, APPS_PLUS, ANDROID_EMAIL, ANDROID_GM, MMS, WHATSAPP,LINKEDIN,HANGOUT,FB,SKYPE};

	        List<Intent> targetedShareIntents = new ArrayList<Intent>();
	        List<HashMap<String, String>> intentMetaInfo = new ArrayList<HashMap<String, String>>();
	        Intent chooserIntent = null;

	        Intent dummy = new Intent(prototype.getAction());
	        dummy.setType(prototype.getType());
	        List<ResolveInfo> resInfo = pm.queryIntentActivities(dummy, 0);

	        if (!resInfo.isEmpty()) {
	            for (ResolveInfo resolveInfo : resInfo) {
	                if (!Arrays.asList(forbiddenChoices).contains(resolveInfo.activityInfo.packageName))
	                    continue;

	                //todo hack to ignore word DYSK (remove duplicated option `copy to clipboard`, working for PL language)
	                if (String.valueOf(resolveInfo.activityInfo.loadLabel(pm)).equals("Dysk"))
	                    continue;

	                HashMap<String, String> info = new HashMap<String, String>();
	                info.put("packageName", resolveInfo.activityInfo.packageName);
	                info.put("className", resolveInfo.activityInfo.name);
	                info.put("simpleName", String.valueOf(resolveInfo.activityInfo.loadLabel(pm)));

	                intentMetaInfo.add(info);
	            }

	            if (!intentMetaInfo.isEmpty()) {
	                Collections.sort(intentMetaInfo, new Comparator<HashMap<String, String>>() {
	                    @Override
	                    public int compare(HashMap<String, String> map1, HashMap<String, String> map2) {
	                        int m1 = getLabeledIntentPriority(map1.get("packageName"));
	                        int m2 = getLabeledIntentPriority(map2.get("packageName"));

	                        if (m1 < m2)
	                            return -1;
	                        else if (m1 > m2)
	                            return 1;
	                        else
	                            return 0;
	                    }
	                });
	                boolean already=false;
	                for (HashMap<String, String> metaInfo : intentMetaInfo) {
	                	
	                    Intent targetedShareIntent = (Intent) prototype.clone();
	                    if (metaInfo.get("packageName").equals(CLIPBOARD)) {
	                    	if(already){
	                    		
	                    	}else{
	                    		already=true;
	                    		 targetedShareIntent.setPackage(metaInfo.get("packageName"));
	 	                        targetedShareIntent.setClassName(metaInfo.get("packageName"), metaInfo.get("className"));
	 	                        LabeledIntent labeledIntent = new LabeledIntent(targetedShareIntent, fragment.getPackageName(), "ClipBoard", R.drawable.clipboard);
	 	                        targetedShareIntents.add(labeledIntent);
	                    	}
	                       
	                    } else {
	                        targetedShareIntent.setPackage(metaInfo.get("packageName"));
	                        targetedShareIntent.setClassName(metaInfo.get("packageName"), metaInfo.get("className"));
	                        targetedShareIntents.add(targetedShareIntent);
	                    }

	                }

	                chooserIntent = Intent.createChooser(targetedShareIntents.remove(targetedShareIntents.size() - 1),"Sharing");
	                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
	            }
	        }
	        fragment.startActivity(Intent.createChooser(chooserIntent, ""));

	    }

	    private static int getLabeledIntentPriority(String packageName) {
	        if (packageName.equalsIgnoreCase(ANDROID_EMAIL))
	            return PRIORITY.get(ANDROID_EMAIL);
	        else if (packageName.equalsIgnoreCase(FACEBOOK))
	            return PRIORITY.get(FACEBOOK);
	        else if (packageName.equalsIgnoreCase(MMS))
	            return PRIORITY.get(MMS);
	        else if (packageName.equalsIgnoreCase(ANDROID_GM))
	            return PRIORITY.get(ANDROID_GM);
	        else if (packageName.equalsIgnoreCase(APPS_PLUS))
	            return PRIORITY.get(APPS_PLUS);
	        else if (packageName.equalsIgnoreCase(TWITTER))
	            return PRIORITY.get(TWITTER);
	        else if (packageName.equalsIgnoreCase(WHATSAPP))
	            return PRIORITY.get(WHATSAPP);
	        else if (packageName.equalsIgnoreCase(CLIPBOARD))
	            return PRIORITY.get(CLIPBOARD);
	        else if (packageName.equalsIgnoreCase(LINKEDIN))
	            return PRIORITY.get(LINKEDIN);
	        else if (packageName.equalsIgnoreCase(HANGOUT))
	            return PRIORITY.get(HANGOUT);
	        else if (packageName.equalsIgnoreCase(FB))
	            return PRIORITY.get(FB);
	        else if (packageName.equalsIgnoreCase(SKYPE))
	            return PRIORITY.get(SKYPE);
	        else
	            return 1000;//none
	    }

		/*public static void createChoserIntent(Home home, Intent shareIntent,
				PackageManager pm, String eXTRA_TEXT, String eXTRA_SUBJECT) {
			// TODO Auto-generated method stub
			
		}*/
	}

