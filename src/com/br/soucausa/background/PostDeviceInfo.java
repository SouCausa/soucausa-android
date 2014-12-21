package com.br.soucausa.background;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.br.soucausa.callbacks.CallbackDadoInicial;
import com.br.soucausa.util.Pontuacao;
import com.br.soucausa.util.Settings;
import com.br.soucausa.util.UserPreference;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class PostDeviceInfo extends AsyncTask<Bundle, Void, Void> {
	
	static final String TAG = "SouCausa";
	public Context context;

	public PostDeviceInfo(Context context)
	{
		this.context = context;
	}
	
	@Override
	protected Void doInBackground(Bundle... bundle) {
		
		JSONObject jsonObject = new JSONObject();
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(Settings.postDeviceInfoUrl);
		
		try {
			if ( bundle[0].containsKey("deviceId") )
				jsonObject.accumulate("deviceId", bundle[0].getString("deviceId"));
			
			ByteArrayEntity btArray = new ByteArrayEntity( jsonObject.toString().getBytes("UTF8") );
	    	btArray.setContentType("application/json");
			httppost.setEntity( btArray );
			
			HttpResponse response = httpclient.execute(httppost);
			String responseStatus = response.getStatusLine().toString();
			Log.d(TAG,"["+ this.getClass().toString() +"]"+responseStatus);
			
			if ( response.getStatusLine().getStatusCode() == 200 )
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
				String json = reader.readLine();
				JSONTokener tokener = new JSONTokener(json);
				JSONObject finalResult = new JSONObject(tokener);
				
				UserPreference userPref = new UserPreference(context);
				if ( finalResult.getBoolean("implicit") )
				{
					Log.d(TAG,"["+ this.getClass().toString() +"] SET AS USER IMPLICIT");
					userPref.setUserAsImplicit();
				}
				else
				{
					Log.d(TAG,"["+ this.getClass().toString() +"] SET AS USER EXPLICIT");
					userPref.setUserAsExplicit();
				}
					
				
				
				userPref.setUserId( Integer.parseInt( finalResult.get("userId").toString() )  );
				
				Log.d(TAG," User ID => " + finalResult.get("userId").toString());
				
				Pontuacao pontuacao = new Pontuacao(context);
				pontuacao.syncPontuacao();
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
