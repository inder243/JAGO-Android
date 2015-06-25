package com.iapp.playdate;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iapp.playdate.R;
import com.playdate.inapppurchase.BillingService;
import com.playdate.inapppurchase.BillingService.RequestPurchase;
import com.playdate.inapppurchase.BillingService.RestoreTransactions;
import com.playdate.inapppurchase.Consts;
import com.playdate.inapppurchase.Consts.PurchaseState;
import com.playdate.inapppurchase.Consts.ResponseCode;
import com.playdate.inapppurchase.PurchaseDatabase;
import com.playdate.inapppurchase.PurchaseObserver;
import com.playdate.inapppurchase.ResponseHandler;

public class in_App_purchase extends android.support.v4.app.Fragment implements OnClickListener{
	int x=0;
	Button btn;
	String user_guardian_id,facebook_friends;
	private static final String DB_INITIALIZED = "db_initialized";
	String  statusUpGradeResponse;
	  private PurchaseDatabase mPurchaseDatabase;
	    private DungeonsPurchaseObserver mDungeonsPurchaseObserver;
	String categoryIdStr,categoryNameStr,statusUpGradeFailResponse;
	RelativeLayout profileSettings;
	ImageView temsOfService;
	ImageView privacyPolicy,sendUsFeedBack;
	 public static final String PREFS_NAME = "MyPrefsFile";
	 ProgressDialog dialog=null;
	 String userId="",statusVendordashBoard="";
	 String idRes,fullNameRes,emailRes,companyRes,phoneRes,addressRes,cityRes,stateRes,countryRes;
	 Button freeVersionUodatedAccount=null;
	 String accountType="",accountTypeProfileVendorOrProfile="",website,id,category,status,statusResponse,lastNameRes;
	 ArrayList<String>categoryIdList,categoryNameList;
	 TextView accountTypeSettings;
	 String toggleStatus,toggleId,zipStr;
	 String mainService="",msgProfileStatus="";
	 TextView profileDetail;
	 String description;
	 Button paidVersionBtn;
	 ImageView back;
	 private Handler mHandler;
	 private BillingService mBillingService;
	
	 private static final int DIALOG_CANNOT_CONNECT_ID = 1;
	    private static final int DIALOG_BILLING_NOT_SUPPORTED_ID = 2;
	   
	public in_App_purchase(){
		
	}
	
	
	 private static final char[] symbols = new char[36];

     static {
         for (int idx = 0; idx < 10; ++idx)
             symbols[idx] = (char) ('0' + idx);
         for (int idx = 10; idx < 36; ++idx)
             symbols[idx] = (char) ('a' + idx - 10);
     }
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.in_app_purchase,
				container, false);
		
		 
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		btn=(Button)view.findViewById(R.id.button_purchase);
		btn.setOnClickListener(this);
		user_guardian_id = getArguments().getString("user_guardian_id");
		facebook_friends = getArguments().getString("facebook_friends");
		SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
  	    boolean purchased_product = prefs.getBoolean("purchased", false);
     	
     	if(purchased_product){
     		Bundle bundle = new Bundle();
         	bundle.putString("user_guardian_id", user_guardian_id);
         	bundle.putString("facebook_friends",facebook_friends);
         	android.support.v4.app.Fragment  fragment=new authentication();
             fragment.setArguments(bundle);
     		android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
     		fragmentManager.beginTransaction()
     		        .replace(R.id.content_frame, fragment).commit();
     	}
		
		
		
		
		 mHandler = new Handler();
	     mDungeonsPurchaseObserver = new DungeonsPurchaseObserver(mHandler);
	     mBillingService = new BillingService();
	     mBillingService.setContext(getActivity());
	     
	     ResponseHandler.register(mDungeonsPurchaseObserver);
	     if (!mBillingService.checkBillingSupported())
	     {
	        // showDialog(DIALOG_CANNOT_CONNECT_ID);
	     }
	     
		return view;
	}
	@Override
    public void onClick(View v) {
            switch (v.getId()) {
            case R.id.button_purchase:
            	/*BillingHelper.setCompletedHandler(mTransactionHandler);
            		
                    if(BillingHelper.isBillingSupported()){
                            BillingHelper.requestPurchase(getActivity(), "android.test.purchased");
                            // android.test.purchased or android.test.canceled or android.test.refunded
            } else {
                    Log.i("TAG","Can't purchase on this device");
            }
                */   
            	
            	 SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
         	    boolean purchased_product = prefs.getBoolean("purchased", false);
         	    
         	/*   Bundle bundle = new Bundle();
           	bundle.putString("user_guardian_id", user_guardian_id);
           	bundle.putString("facebook_friends",facebook_friends);
           	android.support.v4.app.Fragment  fragment=new authentication();
               fragment.setArguments(bundle);
       		android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
       		fragmentManager.beginTransaction()
       		        .replace(R.id.content_frame, fragment).commit();*/
         	    
            	
            	if(purchased_product){
            		Bundle bundle1 = new Bundle();
	            	bundle1.putString("user_guardian_id", user_guardian_id);
	            	bundle1.putString("facebook_friends",facebook_friends);
	            	android.support.v4.app.Fragment  fragment1=new authentication();
	                fragment1.setArguments(bundle1);
	        		android.support.v4.app.FragmentManager fragmentManager1 = getFragmentManager();
	        		fragmentManager1.beginTransaction()
	        		        .replace(R.id.content_frame, fragment1).commit();
            	}
            	else
            	{
         	    
            	 RandomString randomString = new RandomString(36);
                 System.out.println("RandomString>>>>" + randomString.nextString());
                 // bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ
                 String payload = randomString.nextString();
            	
                /* if (!mBillingService.requestPurchase("android.test.purchased", ""))*/
            	
            	 if (!mBillingService.requestPurchase("upgrade_app", payload)) {
 		            
 	            	
            		 
            		//Log.e("aa", "billing service called");
 	                getActivity().showDialog(DIALOG_BILLING_NOT_SUPPORTED_ID);
 	            }
 		
            	}
				
            	
                    break;
            default:
                    // nada
                    Log.i("TAG","default. ID: "+v.getId());
                    break;
            }
            
    }
	private class DungeonsPurchaseObserver extends PurchaseObserver {
	    public DungeonsPurchaseObserver(Handler handler) {
	        super(getActivity(), handler);
	    }
	    @Override
	    public void onBillingSupported(boolean supported) {
	        if (Consts.DEBUG) {
	        	Log.e("bILLINGsUPPORTED==",""+supported);
	        }
	        if (supported) {
	          //  restoreDatabase();
	          //  mBuyButton.setEnabled(true);
	           // mEditPayloadButton.setEnabled(true);
	        } else {
	           // showDialog(DIALOG_BILLING_NOT_SUPPORTED_ID);
	        }
	    }

	    @Override
	    public void onPurchaseStateChange(PurchaseState purchaseState, String itemId,
	            int quantity, long purchaseTime, String developerPayload) {
	        if (Consts.DEBUG) {
	           // Log.i(TAG, "onPurchaseStateChange() itemId: " + itemId + " " + purchaseState);
	        }

	        if (developerPayload == null) {
	            logProductActivity(itemId, purchaseState.toString());
	        } else {
	            logProductActivity(itemId, purchaseState + "\n\t" + developerPayload);
	        }

	        if (purchaseState == PurchaseState.PURCHASED) {
	           // mOwnedItems.add(itemId);
	        }
	      //  mCatalogAdapter.setOwnedItems(mOwnedItems);
	       // mOwnedItemsCursor.requery();
	    }

	    @Override
	    public void onRequestPurchaseResponse(RequestPurchase request,
	            ResponseCode responseCode) {
	        if (Consts.DEBUG) {
	           // Log.d(TAG, request.mProductId + ": " + responseCode);
	        	 Log.e("ResponseCode==",""+responseCode);
	        	
	        }
	        if (responseCode == ResponseCode.RESULT_OK) {
	            if (Consts.DEBUG) {
	              //  Log.i(TAG, "purchase was successfully sent to server");
	            	 Log.e("ResultOk==","purchase was successfully sent to server");
	            	
	            	Log.e("ResponsSucessfully===","ResponseSucessfully");
	            	logProductActivity(request.mProductId, "sending purchase request");
	            	
	            	 SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
	 	            SharedPreferences.Editor edit = prefs.edit();
	 	            edit.putBoolean("purchased", true);
	 	            edit.commit();
	            	
	            	
	            	Bundle bundle = new Bundle();
	            	bundle.putString("user_guardian_id", user_guardian_id);
	            	bundle.putString("facebook_friends",facebook_friends);
	            	android.support.v4.app.Fragment  fragment=new authentication();
	                fragment.setArguments(bundle);
	        		android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
	        		fragmentManager.beginTransaction()
	        		        .replace(R.id.content_frame, fragment).commit();
	            	
	            }
	            
	           
	        } else if (responseCode == ResponseCode.RESULT_USER_CANCELED) {
	            if (Consts.DEBUG) {
	               // Log.i(TAG, "user canceled purchase");
	            }
	            logProductActivity(request.mProductId, "dismissed purchase dialog");
	        } 
	        
	        
	        else {
	            if (Consts.DEBUG) {
	               // Log.i(TAG, "purchase failed");
	            }
	            logProductActivity(request.mProductId, "request purchase returned " + responseCode);
	        }
	        
	        
	         if(responseCode == ResponseCode.RESULT_DEVELOPER_ERROR)
	        {
	        	//Toast.makeText(SettingsActivity.this, "Developer Error", 1).show();
	        	Log.e("DeveloperError==","DeveloperErrir");
	        	// new PerformanceBackgroundInAPPPurchase().execute();
	        }
	    }

	    @Override
	    public void onRestoreTransactionsResponse(RestoreTransactions request,
	            ResponseCode responseCode) {
	        if (responseCode == ResponseCode.RESULT_OK) {
	            if (Consts.DEBUG) {
	            //	new PerformanceBackgroundInAPPPurchase().execute(); 
	               // Log.d(TAG, "completed RestoreTransactions request");
	            	// Log.e(TAG, "completed RestoreTransactions request");
	            }
	            // Update the shared preferences so that we don't perform
	            // a RestoreTransactions again.
	            SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
	            SharedPreferences.Editor edit = prefs.edit();
	            edit.putBoolean(DB_INITIALIZED, true);
	            edit.commit();
	        } else {
	            if (Consts.DEBUG) {
	               // Log.d(TAG, "RestoreTransactions error: " + responseCode);
	            }
	        }
	    }
	}
	private void logProductActivity(String product, String activity) {
	    SpannableStringBuilder contents = new SpannableStringBuilder();
	    contents.append(Html.fromHtml("<b>" + product + "</b>: "));
	    contents.append(activity);
	    prependLogEntry(contents);
	}
	private void prependLogEntry(CharSequence cs) {
	    SpannableStringBuilder contents = new SpannableStringBuilder(cs);
	    contents.append('\n');
	  //  contents.append(mLogTextView.getText());
	   // mLogTextView.setText(contents);
	}
	private void restoreDatabase() {
	    SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
	    boolean initialized = prefs.getBoolean(DB_INITIALIZED, false);
	    if (!initialized) {
	        mBillingService.restoreTransactions();
	       // Toast.makeText(this, R.string.restoring_transactions, Toast.LENGTH_LONG).show();
	    }
	}
	private void doInitializeOwnedItems() {
	    Cursor cursor = mPurchaseDatabase.queryAllPurchasedItems();
	    if (cursor == null) {
	        return;
	    }

//	    final Set<String> ownedItems = new HashSet<String>();
//	    try {
//	        int productIdCol = cursor.getColumnIndexOrThrow(
//	                PurchaseDatabase.PURCHASED_PRODUCT_ID_COL);
//	        while (cursor.moveToNext()) {
//	            String productId = cursor.getString(productIdCol);
//	            ownedItems.add(productId);
//	        }
//	    } finally {
//	        cursor.close();
//	    }

	    // We will add the set of owned items in a new Runnable that runs on
	    // the UI thread so that we don't need to synchronize access to
	    // mOwnedItems.
	    mHandler.post(new Runnable() {
	        public void run() {
//	            mOwnedItems.addAll(ownedItems);
//	            mCatalogAdapter.setOwnedItems(mOwnedItems);
	        }
	    });
	}
	@Override
	public void onStart() {
	    super.onStart();
	    ResponseHandler.register(mDungeonsPurchaseObserver);
	   // initializeOwnedItems();
	}



	//@Override
	//protected void onStart() {
//	    super.onStart();
//	    ResponseHandler.register(mDungeonsPurchaseObserver);
//	    initializeOwnedItems();
	//}

	/**
	 * Called when this activity is no longer visible.
	 */
	@Override
	public void onStop() {
	    super.onStop();
	    ResponseHandler.unregister(mDungeonsPurchaseObserver);
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();

	    mBillingService.unbind();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("","I am here");
		
	}

	public void startIntentSenderForResult(IntentSender intent,
		int requestCode, Intent fillInIntent, int flagsMask,
		int flagsValues, int extraFlags) throws SendIntentException {
	// TODO Auto-generated method stub
	super.getActivity().startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask,
			flagsValues, extraFlags);
	}

	public class RandomString {

        /*
         * static { for (int idx = 0; idx < 10; ++idx) symbols[idx] = (char)
         * ('0' + idx); for (int idx = 10; idx < 36; ++idx) symbols[idx] =
         * (char) ('a' + idx - 10); }
         */

        private final Random random = new Random();

        private final char[] buf;

        public RandomString(int length) {
            if (length < 1)
                throw new IllegalArgumentException("length < 1: " + length);
            buf = new char[length];
        }

        public String nextString() {
            for (int idx = 0; idx < buf.length; ++idx)
                buf[idx] = symbols[random.nextInt(symbols.length)];
            return new String(buf);
        }

    }

    public final class SessionIdentifierGenerator {

        private SecureRandom random = new SecureRandom();

        public String nextSessionId() {
            return new BigInteger(130, random).toString(32);
        }

    }
}
