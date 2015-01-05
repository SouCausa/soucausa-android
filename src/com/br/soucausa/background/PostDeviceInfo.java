package com.br.soucausa.background;

import java.io.BufferedReader;
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

import com.br.soucausa.util.Constants;
import com.br.soucausa.util.Pontuacao;
import com.br.soucausa.util.Settings;
import com.br.soucausa.util.UserPreference;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class PostDeviceInfo extends AsyncTask<Bundle, Void, Void> {

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
	    
		try{
			if ( bundle[0].containsKey("deviceId") )
			{
				jsonObject.accumulate("deviceId", bundle[0].getString("deviceId"));
				
				ByteArrayEntity btArray = new ByteArrayEntity( jsonObject.toString().getBytes("UTF8") );
		    	btArray.setContentType("application/json");
				httppost.setEntity( btArray );

				HttpResponse response = httpclient.execute(httppost);
				int responseStatus = response.getStatusLine().getStatusCode();
				
				if ( responseStatus == HttpStatus.SC_OK )
				{
					BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
					String json = reader.readLine();
					JSONTokener tokener = new JSONTokener(json);
					JSONObject finalResult = new JSONObject(tokener);
					if ( finalResult != null )
					{
						UserPreference userPref = new UserPreference(context);
						if ( finalResult.getBoolean("implicit") )
						{
							Log.d(Constants.TAG,"["+ this.getClass().toString() +"] SET AS USER IMPLICIT");
							userPref.setUserAsImplicit();
						}
						else
						{
							Log.d(Constants.TAG,"["+ this.getClass().toString() +"] SET AS USER EXPLICIT");
							userPref.setUserAsExplicit();
						}
						
						userPref.setUserId( Integer.parseInt( finalResult.get("userId").toString() )  );
					}
				}
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
		
		return null;
	}

}
