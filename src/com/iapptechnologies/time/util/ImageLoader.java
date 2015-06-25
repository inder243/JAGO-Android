package com.iapptechnologies.time.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.iapp.playdate.FileCache;
import com.iapp.playdate.MemoryCache;
import com.iapp.playdate.R;
import com.iapp.playdate.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.widget.ImageView;


public class ImageLoader {
    
    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
     
    public ImageLoader(Context context){
        fileCache=new FileCache(context);
        executorService=Executors.newFixedThreadPool(5);
    }
     
   

	final int stub_id=R.drawable.place_holder;
    public  Bitmap DisplayImage(String url, ImageView imageView)
    {
    	imageViews.put(imageView, url);
    Bitmap bitmap=memoryCache.get(url);
    if(bitmap!=null){
  	 /* bitmap= Bitmap.createScaledBitmap(bitmap, 70, 70,
                true);*/
      imageView.setImageBitmap(getCircularBitmapWithWhiteBorder(bitmap,4));
  	 
  }
  else
  {
  	 /* bitmap= Bitmap.createScaledBitmap(bitmap, 70, 70,
                true);*/
      queuePhoto(url, imageView);
      imageView.setImageResource(stub_id);
  }
    	
    	
		return bitmap;
    }
        
    public static Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap,
	        int borderWidth) {
	    if (bitmap == null || bitmap.isRecycled()) {
	        return null;
	    }
	    Bitmap dstBmp;
		 if (bitmap.getWidth() >= bitmap.getHeight()){

			  dstBmp = Bitmap.createBitmap(
					 bitmap, 
					 bitmap.getWidth()/2 - bitmap.getHeight()/2,
			     0,
			     bitmap.getHeight(), 
			     bitmap.getHeight()
			     );

			}else{

			  dstBmp = Bitmap.createBitmap(
					  bitmap,
			     0, 
			     bitmap.getHeight()/2 - bitmap.getWidth()/2,
			     bitmap.getWidth(),
			     bitmap.getWidth() 
			     );
			}
		 bitmap=dstBmp;
	    final int width = bitmap.getWidth() + borderWidth;
	    final int height = bitmap.getHeight() + borderWidth;

	    Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	    BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
	    Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setShader(shader);

	    Canvas canvas = new Canvas(canvasBitmap);
	    float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
	    canvas.drawCircle(width / 2, height / 2, radius, paint);
	    paint.setShader(null);
	    paint.setStyle(Paint.Style.STROKE);
	    paint.setColor(Color.WHITE);
	    paint.setStrokeWidth(borderWidth);
	    canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
	    return canvasBitmap;
	}
    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }
     
    private Bitmap getBitmap(String url)
    {
        File f=fileCache.getFile(url);
         
        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;
         
        //from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Throwable ex){
           ex.printStackTrace();
           if(ex instanceof OutOfMemoryError)
               memoryCache.clear();
           return null;
        }
    }
 
    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
             
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=100;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
             
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
     
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u;
            imageView=i;
        }
    }
     
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
         
        @Override
        public void run() {
            if(imageViewReused(photoToLoad))
                return;
            Bitmap bmp=getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, bmp);
            if(imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
            Activity a=(Activity)photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }
     
    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
     
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(getCircularBitmapWithWhiteBorder(bitmap,4));
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }
 
    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
 
}
