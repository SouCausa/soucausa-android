package com.br.soucausa.background;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
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
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.br.soucausa.callbacks.CallbackPostPhoto;
import com.br.soucausa.data.DataContract;
import com.br.soucausa.model.Cupom;
import com.br.soucausa.util.AppUtils;
import com.br.soucausa.util.Pontuacao;
import com.br.soucausa.util.Settings;
import com.br.soucausa.util.UserPreference;

public class PostPhoto extends AsyncTask<Object, Void, Void> {
	
	
	ProgressDialog pDialog;
	int postResponse;
	Context context;
	public SQLiteDatabase db;
	private CallbackPostPhoto callback;


	public PostPhoto(Context context,CallbackPostPhoto callback) {
		this.context = context;
		this.callback = callback;
	}


	@Override
	protected Void doInBackground(Object... params) {
	
		DataContract dbHelper = new DataContract(context);
		this.db = dbHelper.getWritableDatabase(); //just called once.
		ContentValues cv = new ContentValues();
		Cupom cp = (Cupom) params[0];
		UserPreference userPref = new UserPreference(this.context);
		
		if ( AppUtils.isNetworkAvailable(this.context) )
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Settings.postPhotoUrl);
			
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		    bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(cp.getFile().getPath(),bmOptions);
			int photoW = bmOptions.outWidth;
		    int photoH = bmOptions.outHeight;
		    int targetW = 550;
		    int targetH = 620;
		    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
		    bmOptions.inJustDecodeBounds = false;
		    bmOptions.inSampleSize = scaleFactor;
		    bmOptions.inPurgeable = true;
			
			Bitmap bitmap = BitmapFactory.decodeFile(cp.getFile().getPath(), bmOptions);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			
			byte[] b = baos.toByteArray(); // b is my ByteArray
			String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
			JSONObject jsonObject = new JSONObject();
	
			try {
				jsonObject.accumulate("image_base64", encodedImage);
				jsonObject.put("ong_id" , cp.getOng().getONGId());
				jsonObject.put("device_id" , userPref.getDeviceId());
				
				if (cp.getCausaId() != null)
					jsonObject.put("causa_fk" , cp.getCausaId());
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
				
			} catch (JSONException e) {
				Log.d(Settings.TAG,"Deu bosta ao adicionar no JSON");
				e.printStackTrace();
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	
			try {
				HttpResponse response = httpclient.execute(httppost);
				String responseStatus = response.getStatusLine().toString();
				Log.d(Settings.TAG,"["+ this.getClass().toString() +"]"+responseStatus);
				postResponse = response.getStatusLine().getStatusCode();
				
				if (postResponse == 200)
				{
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
					pontuacao.syncPontuacao();
					//create the row as status 1 (SENT)
					Log.d(Settings.TAG,"postResponse == 200");
					cv.put("path",cp.getFile().getPath());
					cv.put("causa_id",userPref.getCausaId());
					cv.put("status",1);
				}
				else
				{
					Log.d(Settings.TAG,"postResponse != 200");
					cv.put("path",cp.getFile().getPath());
					cv.put("causa_id",userPref.getCausaId());
					cv.put("status",0);
				}
	
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		else
		{
			cv.put("path",cp.getFile().getPath());
			cv.put("causa_id",userPref.getCausaId());
			cv.put("status",0);
			Log.d(Settings.TAG,"HASN'T NETWORK AVAILABLE");
		}
		
		this.db.insert("DOACAO",null, cv);
		return null;
	}


	@Override

	protected void onPreExecute() {
		if ( AppUtils.isNetworkAvailable(this.context) )
			Toast.makeText(context, "Obrigado por doar.", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(context, "Obrigado por doar. Enviaremos quando houver conex�o com a internet", Toast.LENGTH_LONG).show();
		
		this.callback.onTaskComplete();
	}


	@Override
	protected void onPostExecute(Void param){
		super.onPostExecute(param);
	}
}
