package com.br.soucausa.background;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.br.soucausa.callbacks.CallbackPostPhoto;
import com.br.soucausa.data.DataContract;
import com.br.soucausa.factories.NotificationFactory;
import com.br.soucausa.factories.ScNotification;
import com.br.soucausa.factories.ScToastNotification;
import com.br.soucausa.model.Cupom;
import com.br.soucausa.util.AppUtils;
import com.br.soucausa.util.Constants;
import com.br.soucausa.util.Settings;
import com.br.soucausa.util.UserPreference;

public class PostPhoto extends AsyncTask<Object, Void, Void> {
	
	private CallbackPostPhoto callback;
	private ProgressDialog pDialog;
	private int postResponse;
	private Context context;
	private SQLiteDatabase db;
	private BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	
	private int photoW;
    private int photoH;
    private int targetW = 550;
    private int targetH = 620;
    private int scaleFactor;

	public PostPhoto(Context context,CallbackPostPhoto callback) {
		this.context = context;
		this.callback = callback;
	}
	
	private void setUpBitmapDimensions() {
		photoW = bmOptions.outWidth;
	    photoH = bmOptions.outHeight;
	    scaleFactor = Math.min(photoW/Constants.TARGET_WIDTH_PHOTO, photoH/Constants.TARGET_HEIGHT_PHOTO);
	    bmOptions.inSampleSize = scaleFactor;
	}

	@Override
	protected Void doInBackground(Object... params) {
	
		DataContract dbHelper = new DataContract(context);
		this.db = dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		Cupom cp = (Cupom) params[0];
		UserPreference userPref = new UserPreference(this.context);
		
		if ( AppUtils.isNetworkAvailable(this.context) )
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Settings.postPhotoUrl);
			
			
			bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(cp.getFile().getPath(), bmOptions);
			this.setUpBitmapDimensions();
			
			bmOptions.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeFile(cp.getFile().getPath(), bmOptions);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESS_RATE, baos);
			
			byte[] b = baos.toByteArray(); // b is my ByteArray
			
			try {
				baos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
			JSONObject jsonObject = new JSONObject();
	
			try {
				jsonObject.accumulate("image_base64", encodedImage);
				jsonObject.put("ong_id" , cp.getOng().getONGId());
				jsonObject.put("device_id" , userPref.getDeviceId());
				
				if (cp.getCausaId() != null)
				{
					jsonObject.put("causa_fk" , cp.getCausaId());
				}
				else
				{
					if ( userPref.getCausaId() != null )
						jsonObject.put("causa_fk" , userPref.getCausaId());
				}
				
				if ( userPref.hasUserId() )
				{
					jsonObject.put( "user_id" , userPref.getUserId() );
				}
				
				ByteArrayEntity btArray = new ByteArrayEntity( jsonObject.toString().getBytes("UTF8") );
				btArray.setContentType("application/json");
				httppost.setEntity( btArray );
				
				HttpResponse response = httpclient.execute(httppost);
				postResponse = response.getStatusLine().getStatusCode();
				
				if (postResponse == HttpStatus.SC_OK)
				{
					if ( !userPref.hasUserId() )
					{
						BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
						String json = reader.readLine();
						JSONTokener tokener = new JSONTokener(json);
						JSONObject finalResult = new JSONObject(tokener);
						if ( finalResult != null ) {
							userPref.setUserId( Integer.parseInt( finalResult.get("userId").toString() )  );
						}
					}
					
					cv.put("path",cp.getFile().getPath());
					cv.put("causa_id",userPref.getCausaId());
					cv.put("status",1);
				}
				else
				{
					Log.d(Constants.TAG,"postResponse != 200");
					cv.put("path",cp.getFile().getPath());
					cv.put("causa_id",userPref.getCausaId());
					cv.put("status",0);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			cv.put("path",cp.getFile().getPath());
			cv.put("causa_id",userPref.getCausaId());
			cv.put("status",0);
		}
		
		this.db.insert("DOACAO",null, cv);
		return null;
	}


	@Override

	protected void onPreExecute() {
		ScToastNotification toastNot = (ScToastNotification) NotificationFactory.createNotification(ScNotification.ScType.TOAST);
		
		if ( AppUtils.isNetworkAvailable(this.context) )
			toastNot.doNotify("Obrigado por doar!", this.context);
		else
			toastNot.doNotify("Obrigado por doar! Enviaremos quando houver conex‹o com a internet", this.context);
		
		this.callback.onTaskComplete();
	}

	@Override
	protected void onPostExecute(Void param){
		super.onPostExecute(param);
		this.db.close();
	}
}
