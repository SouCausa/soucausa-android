package com.br.soucausa.services;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.br.soucausa.data.DataContract;
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

    static final int MAX_THREADS = 4;
    final Context context = this;
    ExecutorService mExecutor;
	
	public static Intent makeIntent(Context context) {

		Intent intent = new Intent(context, SendPhotoService.class);

		// Create and pass a Messenger as an "extra" so the
		// DownloadService can send back the pathname.
		//intent.putExtra("MESSENGER", new Messenger(downloadHandler));
		
		return intent;
	}
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mExecutor = Executors.newFixedThreadPool(MAX_THREADS);
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//return super.onStartCommand(intent, flags, startId);
		Runnable downloadRunnable;
		DataContract dbHelper = new DataContract(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query(true, "DOACAO", null ,"status = 0",null,null,null,"_id",null);
		
		if ( cursor.getCount() == 0 )
		{
			stopSelf();
		}
		else
		{
			final CountDownLatch countThreads = new CountDownLatch( cursor.getCount() );

			while (cursor.moveToNext()) {
			    final String path = cursor.getString( cursor.getColumnIndex("path") );
			    final Integer causa_id = cursor.getInt( cursor.getColumnIndex("causa_id") );
			    final int _id = cursor.getInt( cursor.getColumnIndex("_id") );
			    
			    downloadRunnable = new Runnable() {
			    	
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.d(Settings.TAG,path);
						UserPreference userPref = new UserPreference(context);
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(Settings.postPhotoUrl);
						
						BitmapFactory.Options bmOptions = new BitmapFactory.Options();
					    bmOptions.inJustDecodeBounds = true;
						BitmapFactory.decodeFile(path,bmOptions);
						int photoW = bmOptions.outWidth;
					    int photoH = bmOptions.outHeight;
					    int targetW = 550;
					    int targetH = 620;
					    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
					    bmOptions.inJustDecodeBounds = false;
					    bmOptions.inSampleSize = scaleFactor;
					    bmOptions.inPurgeable = true;
						
						Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
						
						byte[] b = baos.toByteArray(); // b is my ByteArray
						String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
						JSONObject jsonObject = new JSONObject();
						
						try {
							
							jsonObject.accumulate("image_base64", encodedImage);
							jsonObject.put("device_id" , userPref.getDeviceId());
							
							if (causa_id != null)
								jsonObject.put("causa_fk" , causa_id);
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
							String responseStatus = response.getStatusLine().toString();
							Log.d(Settings.TAG,"["+ this.getClass().toString() +"]"+responseStatus);
							int postResponse = response.getStatusLine().getStatusCode();
							
							if (postResponse == 200)
							{
								countThreads.countDown();
								Log.d(Settings.TAG,"postResponse == 200");
								
								if ( !userPref.hasUserId() )
								{
									BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
									String json = reader.readLine();
									JSONTokener tokener = new JSONTokener(json);
									JSONObject finalResult = new JSONObject(tokener);
									userPref.setUserId( Integer.parseInt( finalResult.get("userId").toString() )  );
								}
	
								Pontuacao pontuacao = new Pontuacao(context);
								pontuacao.incrementar(Settings.PONTOS_POR_FOTO);
								
								DataContract dbHelper = new DataContract(context);
								SQLiteDatabase db = dbHelper.getWritableDatabase();
								ContentValues values = new ContentValues();
								values.put("status",1);
								String[] whereValues = new String[1];
								whereValues[0] = new Integer(_id).toString();
								db.update("DOACAO", values, "_id = ?", whereValues);
							}
							else
							{
								countThreads.countDown();
								Log.d(Settings.TAG,"postResponse != 200");
							}
	
						} catch (JSONException e) {
							Log.d(Settings.TAG,"Deu bosta ao adicionar no JSON");
							e.printStackTrace();
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				};
			
				mExecutor.execute(downloadRunnable);
			}
			
			try {
				countThreads.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				stopSelf();
			}
		}
		
		return startId;
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
