package com.br.soucausa.services;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.br.soucausa.data.DataContract;
import com.br.soucausa.util.Constants;
import com.br.soucausa.util.Pontuacao;
import com.br.soucausa.util.Settings;
import com.br.soucausa.util.UserPreference;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

public class SendPhotoService extends Service {

    static final int MAX_THREADS = 4; //my num of threads in the pool
    final Context context = this;
    ExecutorService mExecutor;
    CountDownLatch countThreads;
	
	public static Intent makeIntent(Context context) {
		Intent intent = new Intent(context, SendPhotoService.class);
		return intent;
	}
	
	@Override
	public void onCreate() {
		mExecutor = Executors.newFixedThreadPool(MAX_THREADS);
	}
	
	private class UploadPendingPhotos implements Runnable {
		
		private String path;
		private Integer causaId;
		private Integer _id;
		CountDownLatch countThreads;
		BitmapFactory.Options bmOptions;
		
		public UploadPendingPhotos(String path,Integer causaId,Integer _id,CountDownLatch countThreads) {
			this.path = path;
			this.causaId = causaId;
			this._id  = _id;
			this.countThreads = countThreads;
			
			bmOptions = new BitmapFactory.Options();
		}
		
		private void setUpBitmapDimensions() {
			int photoW = bmOptions.outWidth;
		    int photoH = bmOptions.outHeight;
		    int scaleFactor = Math.min(photoW/Constants.TARGET_WIDTH_PHOTO, photoH/Constants.TARGET_HEIGHT_PHOTO);
		    bmOptions.inSampleSize = scaleFactor;
		}
		
		private void setPhotoAsSent() {
			DataContract dbHelper = new DataContract(context);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put("status",1);
			
			String[] whereValues = new String[1];
			whereValues[0] = new Integer(_id).toString();
			
			db.update("DOACAO", values, "_id = ?", whereValues);
			
			db.close();
		}
		
		@Override
		public void run() {

			Log.d(Constants.TAG,"running");
			
			UserPreference userPref = new UserPreference(context);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Settings.postPhotoUrl);
			
		    bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path,bmOptions);
			setUpBitmapDimensions();

			bmOptions.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
			
			byte[] b = baos.toByteArray();
			String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
			
			try{
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("image_base64", encodedImage);
				jsonObject.put("device_id" , userPref.getDeviceId());
				
				if (this.causaId != null)
				{
					jsonObject.put("causa_fk" , this.causaId);
				}
				else
				{
					if ( userPref.getCausaId() != null )
						jsonObject.put("causa_fk" , userPref.getCausaId());
				}
				
				if ( userPref.hasUserId() )
					jsonObject.put( "user_id" , userPref.getUserId() );
				
				ByteArrayEntity btArray = new ByteArrayEntity( jsonObject.toString().getBytes("UTF8") );
				btArray.setContentType("application/json");
				httppost.setEntity( btArray );
				
				HttpResponse response = httpclient.execute(httppost);
				int postResponse = response.getStatusLine().getStatusCode();
				
				if (postResponse == HttpStatus.SC_OK)
				{
					if ( !userPref.hasUserId() )
					{
						BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
						String json = reader.readLine();
						JSONTokener tokener = new JSONTokener(json);
						JSONObject finalResult = new JSONObject(tokener);
						userPref.setUserId( Integer.parseInt( finalResult.get("userId").toString() )  );
					}
					
					setPhotoAsSent();
				}

			} catch (Exception e) {
				
			} finally {
				this.countThreads.countDown();
			}
		}
	};
		
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		DataContract dbHelper = new DataContract(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		Cursor cursor = db.query(true, "DOACAO", null ,"status = " + Constants.STATUS_DONATE_NOT_SENT,null,null,null,"_id",null);
		countThreads = new CountDownLatch( cursor.getCount() );
		
		if ( cursor.getCount() == 0 )
		{
			Log.d(Constants.TAG,"Stoping Service. Photos Count == 0");
			stopSelf();
		}
		else
		{
			while (cursor.moveToNext()) {
				Log.d(Constants.TAG,"looping..");
			    String path = cursor.getString( cursor.getColumnIndex("path") );
			    Integer causaId = cursor.getInt( cursor.getColumnIndex("causa_id") );
			    Integer _id = cursor.getInt( cursor.getColumnIndex("_id") );
			    
			    mExecutor.execute(new UploadPendingPhotos(path, causaId, _id,countThreads));
			}
			
			try {
				countThreads.await(Constants.UPLOAD_PHOTO_TIMEOUT_INSECONDS,TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				stopSelf();
			}
		}
		
		return startId;
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
