/*package com.example.pdfsconverter.contactscreen;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pdfsconverter.DashBoardScreen;
import com.example.pdfsconverter.R;

import com.example.pdfsconverter.constants.MyDate;
import com.example.pdfsconverter.database.DataBaseSqlliteHelper;
import com.example.pdfsconverter.models.ContactModels;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

   public class ContactScreen  extends Activity  implements OnClickListener
   {
	   File newFolder;
	   TextView noContactFound;
	   ListView contactList;
	   ProgressDialog dialog;
	   ArrayList<ContactModels>ContactList;
	   File file=null;
	   PdfWriter docWriter = null;
	   String imagePath,mImageName;
	   Button pdfButton;
	   SharedPreferences settings;
	   SharedPreferences.Editor editor;
       @Override
       public void onCreate(Bundle savedInstanceState) {
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       super.onCreate(savedInstanceState);
       setContentView(R.layout.contactlist);
       pdfButton=(Button)findViewById(R.id.pdfBtn);
       pdfButton.setOnClickListener(this);
       
       noContactFound=(TextView)findViewById(R.id.noContactsFound);
       ContactList=new ArrayList<ContactModels>();
       contactList=(ListView)findViewById(R.id.contactsList);
      
       new PerformanceBackGround().execute();
       

  
       }
       
       public void insert (ContentValues mValue){
    	DataBaseSqlliteHelper mDataBaseSqlLiteHelper = new DataBaseSqlliteHelper(this);
   		SQLiteDatabase mSqLiteDatabase = mDataBaseSqlLiteHelper.getWritableDatabase();
   		long mCount = mSqLiteDatabase.insert("contactPath", null, mValue);
    	Log.e("Count==",""+mCount);
   		Log.i("****************","__________ROW AFFECTED ______  : "+mCount);
   	}
       class PerformanceBackGround extends AsyncTask<String, String, String>
       {
    	@Override
    	protected void onPreExecute() {
        dialog=new ProgressDialog(ContactScreen.this);
        dialog.setMessage("Please Wait");
        dialog.setCancelable(true);
        dialog.show();
    	super.onPreExecute();
    	}

		@Override
		protected String doInBackground(String... params) {
			
			ContentResolver cr =getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
			if (cur.getCount() > 0) {
			    while (cur.moveToNext()) {
			    	  ContactModels ctModels=new ContactModels();	  
			    	
			        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
			        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			        ctModels.setContactName(name);
			        
			        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
			             Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{id}, null);
			             while (pCur.moveToNext()) {
			                  int phoneType = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
			                  String phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			                  switch (phoneType) {
			                        case Phone.TYPE_MOBILE:
			                            Log.e(name + "(mobile number)", phoneNumber);
			                            if(phoneNumber!=null && phoneNumber.length()>0)
			                            {
			                             ctModels.setMobileNumber(phoneNumber);	
			                            }else
			                            {
			                            ctModels.setMobileNumber("No Mobile Number");	
			                            }
			                            
			                            
			                            break;
			                        case Phone.TYPE_HOME:
			                            Log.e(name + "(home number)", phoneNumber);
			                            if(phoneNumber!=null && phoneNumber.length()>0)
			                            {
			                             ctModels.setHomeNumber(phoneNumber);	
			                            }else
			                            {
			                             ctModels.setHomeNumber("No Home Number");	
			                            }
			                            
			                            break;
			                            case Phone.TYPE_WORK:
			                            Log.e(name + "(work number)", phoneNumber);
			                            if(phoneNumber!=null && phoneNumber.length()>0)
			                            {
			                             ctModels.setWorkNumber(name);	
			                            }else
			                            {
			                             ctModels.setWorkNumber("No Work Number");	
			                            }
			                            break;
//			                        case Phone.TYPE_OTHER:
//			                            Log.e(name + "(other number)", phoneNumber);
//			                            break;                                  
			                        default:
			                            break;
			                  }
			              } 
			             pCur.close();
			             ContactList.add(ctModels);
			             Log.e("ContactSize==",""+ContactList.size());
			   
			        }
			    }
			}	
			
		
//		  Cursor cursor = null;
//		  try {
//		      cursor =getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
//		      int contactIdIdx = cursor.getColumnIndex(Phone._ID);
//		      int nameIdx = cursor.getColumnIndex(Phone.DISPLAY_NAME);
//		      int phoneNumberIdx = cursor.getColumnIndex(Phone.NUMBER);
//		      int photoIdIdx = cursor.getColumnIndex(Phone.PHOTO_ID);
//		      cursor.moveToFirst();
//		      do {
//		    	  ContactModels ctModels=new ContactModels();	  
//		    	  
//		          String idContact = cursor.getString(contactIdIdx);
//		          String name = cursor.getString(nameIdx);
//		          Log.e("Name==",""+name);
//		          ctModels.setContactName(name);
//		          
//		          String phoneNumber = cursor.getString(phoneNumberIdx);
//		          Log.e("Name==",""+phoneNumber);
//		          ctModels.setContactNumber(phoneNumber);
//		          ContactList.add(ctModels);
//		          
//		       
//		      } while (cursor.moveToNext());  
//		  } catch (Exception e) {
//		      e.printStackTrace();
//		  } finally {
//		      if (cursor != null) {
//		          cursor.close(); 
//		      }
//		  }
			return null;
		}
    	   @Override
    	protected void onPostExecute(String result) {
    	if(dialog!=null && dialog.isShowing())
    	{
    		if(ContactList!=null && ContactList.size()>0)
    		{
    		contactList.setAdapter(new ContactList());
    		}else
    		{
    		noContactFound.setVisibility(View.VISIBLE);	
    		}
    		
    
    	}
    	dialog.dismiss();
    	super.onPostExecute(result);
    	}
       }
       
       		class ContactList extends BaseAdapter
       		{
			@Override
			public int getCount() {
			return ContactList.size();
			}

			@Override
			public Object getItem(int position) {
				
			return position;
			}

			@Override
			public long getItemId(int position) {
			
			return position;
				}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
			    ViewHolder holder = null;
		        LayoutInflater mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		        if (convertView == null) {
		            convertView = mInflater.inflate(R.layout.contactlistinflater, null);
		            holder = new ViewHolder();
			        holder.contactName = (TextView) convertView.findViewById(R.id.contactName);
		            convertView.setTag(holder);
		        }
		         else {
		            holder = (ViewHolder) convertView.getTag();
		        }
	    	 holder.contactName.setText(ContactList.get(position).getContactName().toString().trim());
		     return convertView;
			}
       			
       		}
       	 private class ViewHolder {
      	 TextView contactName; 
      	 }
       	 
       	 private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){
       	   
       	  //create a new cell with the specified Text and Font
       	  PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
       	  //set the cell alignment
       	  cell.setHorizontalAlignment(align);
       	  //set the cell column span in case you want to merge two or more cells
       	  cell.setColspan(colspan);
       	  //in case there is no text and you wan to create an empty row
       	  if(text.trim().equalsIgnoreCase("")){
       	   cell.setMinimumHeight(10f);
       	  }
       	  //add the call to the table
       	  table.addCell(cell);
       	   
       	 }
       	 

 		@Override
 		public void onClick(View v) {
 		switch(v.getId())
 		{
 		case R.id.pdfBtn:
 			
 	    final Dialog dialog = new Dialog(ContactScreen.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.pdfname);
		
		Button btn_search=(Button)dialog.findViewById(R.id.button1_popup);
		Button btn_cancel=(Button)dialog.findViewById(R.id.button2_popup);
		final EditText edt=(EditText)dialog.findViewById(R.id.editText_friend_search_name);	
		
		btn_search.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		if(edt.getEditableText().toString().length()==0)
		{
		Toast.makeText(ContactScreen.this,"Please enter ", 1).show();
		}else
		{
		if(edt.getEditableText().toString()!=null && edt.getEditableText().toString().length()>0)
		{
		String pdfnameStr=edt.getEditableText().toString();
		String appendNameStrPdf=pdfnameStr+"_p.pdf";
			
		pdfCreator(appendNameStrPdf);
//			
//	        ContentValues mValues = new ContentValues();
//	        if(imagePath!=null && imagePath.length()>0)
//	        {
//	        mValues.put("pathname",imagePath);
//	        }
//			if(appendNameStrPdf!=null && appendNameStrPdf.length()>0)
//			{
//			mValues.put("filename", appendNameStrPdf);
//			}
//			insert(mValues);   
			
//			Intent in=new Intent(ContactScreen.this,DashBoardScreen.class);
//			in.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(in);
//			finish();
		}
		}
				
			}

	
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {	
		dialog.dismiss();	
		}
		});
		dialog.show();
 		break;
 		}
 			
 		} 	 
    

     	private void pdfCreator(String appendNameStrPdf) {
		
     	     
            try {
              //  File newFolder = new File(Environment.getExternalStorageDirectory(), "NiteshKhosla");
         	    mImageName = MyDate.getCurrentDate()+"_"+MyDate.getCurrentTime()+"_p.pdf";
                 imagePath = Environment.getExternalStorageDirectory() + File.separator+ mImageName;
                 newFolder = new File(imagePath);
                 newFolder.createNewFile();
                if (!newFolder.exists()) {
                newFolder.mkdir();
                }
                
                

            } catch (Exception e) {
             e.printStackTrace();
            }
            
        
//    		String pdfnameStr=edt.getEditableText().toString();
//    		String appendNameStrPdf=pdfnameStr+"_p.pdf";
    			
//    		pdfCreator();
    			
    	        ContentValues mValues = new ContentValues();
    	        if(imagePath!=null && imagePath.length()>0)
    	        {
    	        mValues.put("pathname",imagePath);
    	        }
    			if(appendNameStrPdf!=null && appendNameStrPdf.length()>0)
    			{
    			mValues.put("filename", appendNameStrPdf);
    			}
    			insert(mValues);   
            
            Document doc = new Document();
            DecimalFormat df = new DecimalFormat("0.00");
            try {
                
                //special font sizes
                Font bfBold12 = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0)); 
                Font bf12 = new Font(FontFamily.TIMES_ROMAN, 12); 
              
                docWriter = PdfWriter.getInstance(doc , new FileOutputStream(newFolder));
              
                doc.open();
              
                //create a paragraph
                Paragraph paragraph = new Paragraph("Contacts");
                paragraph.setFont(bfBold12);
             
                
                
                //specify column widths
                float[] columnWidths = {5f, 5f,5f};
                //create PDF table with the given widths
                PdfPTable table = new PdfPTable(columnWidths);
                // set table width a percentage of the page width
                table.setWidthPercentage(90f);
              
                //insert column headings
                insertCell(table, "Name", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "Mobile", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "Home", Element.ALIGN_CENTER, 1, bfBold12);
                
                
               // insertCell(table, "Email", Element.ALIGN_CENTER, 1, bfBold12);
                table.setHeaderRows(1);
              
             
                 
                //just some random data to fill 
                for(int x=0; x<ContactList.size(); x++){
              
                 if(ContactList!=null && ContactList.size()>x)
                 {
                insertCell(table, ContactList.get(x).getContactName(), Element.ALIGN_CENTER, 1, bf12);
                 }
                 
                 if(ContactList.get(x).getMobileNumber()!=null && ContactList.get(x).getMobileNumber().length()>0)
                 {
                 	   if(ContactList.size()>0 && ContactList.size()>x)
                        {
                        insertCell(table,ContactList.get(x).getMobileNumber(), Element.ALIGN_CENTER, 1, bf12);
                        }	
                 }else
                 {
                 insertCell(table,"No Mobile Number", Element.ALIGN_CENTER, 1, bf12);		
                 }
                 
                 
              
                 
                 if(ContactList.get(x).getHomeNumber()!=null && ContactList.get(x).getHomeNumber().length()>0)
                 {
                     if(ContactList.size()>0 && ContactList.size()>x)
                     {
                     insertCell(table,ContactList.get(x).getHomeNumber(), Element.ALIGN_CENTER, 1, bf12);
                     }	
                 }else
                 {
                 insertCell(table,"No Home Number", Element.ALIGN_CENTER, 1, bf12);	
                 }
                }

                 
                //add the PDF table to the paragraph 
                paragraph.add(table);
                // add the paragraph to the document
                doc.add(paragraph);
              
               }
               catch (DocumentException dex)
               {
                dex.printStackTrace();
               }
               catch (Exception ex)
               {
                ex.printStackTrace();
               }
            finally
            {
             if (doc != null){
              //close the document
              doc.close();
             }
             if (docWriter != null){
              //close the writer
              docWriter.close();
             }
            }
		}

      	 @Override
      	public void onBackPressed() {
      	Intent in=new Intent(ContactScreen.this,DashBoardScreen.class);
      	in.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
    	in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      	startActivity(in);
      	finish();
      	super.onBackPressed();
      	 }
 }
*/