package com.br.soucausa.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Pontuacao {

	//static final String URL_TO_SYNC =  "http://ec2-54-186-67-108.us-west-2.compute.amazonaws.com/index.php/user/pontos";
	static final String URL_TO_SYNC =  "http://10.0.3.2:8080/index.php/user/pontos";
		
	static final String PREFS_NAME = "UserInfo";
	static final String TAG = "SalveCupom";
	private Context context;
	private UserPreference userPref;
	
	public Pontuacao(Context context) {
		this.context = context;
		userPref = new UserPreference(context);
	}
	
	public void incrementar(int pontos) {
		int pontuacao_antiga = userPref.getPontuacao();
		int pontuacao_nova = pontuacao_antiga + pontos;
		userPref.setPontuacao(pontuacao_nova);
	}
	
	public int getPontuacao() {
		return userPref.getPontuacao();
	}
	
	public void syncPontuacaoOnBackground() {
		Log.d(TAG,"SYNC PONTUACAO");
		new Thread(new RunnableSync() ).start();
	}
	
	/**
	 * Enviar pontuação presente no userPreferences e então manter a maior pontuação entre o server e o cliente.
	 * Para identificar o mobile corrente, devemos enviar o deviceId junto com o userId atual.
	 * caso o request dê errado, retornar pontuacao atual contida no userPreference
	 */
	public int syncPontuacao()
	{
		Log.d(TAG,Integer.toString(userPref.getUserId()));
		//Log.d(TAG,userPref.getDeviceId());
		
		if ( userPref.getUserId() == -1 || userPref.getDeviceId() == null )
			return getPontuacao();
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
        StringBuilder result = new StringBuilder();
        
		HttpPut putRequest = new HttpPut(URL_TO_SYNC);
		putRequest.addHeader("Content-Type", "application/json");
        putRequest.addHeader("Accept", "application/json");
        
        JSONObject keyArg = new JSONObject();
        // Aqui eu posso verificar se a pontuacao é Syncable ???? Utilizando um método interno da classe UserPreferences
        try {
			keyArg.put("pontuacao", userPref.getPontuacao());
			keyArg.put("deviceId", userPref.getDeviceId());
	        keyArg.put("userId", userPref.getUserId());
	        
	        StringEntity input;
	        input = new StringEntity(keyArg.toString());
	        putRequest.setEntity(input);
	        HttpResponse response = httpClient.execute(putRequest);
	        
	        Log.d(TAG,"["+ this.getClass().toString() +"]"+response.getStatusLine());
	        
	        if (response.getStatusLine().getStatusCode() == 200)
	        {
	        	BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
	            String output;
	            while ((output = br.readLine()) != null)
	                result.append(output);
	            
	            JSONTokener tokener = new JSONTokener(result.toString());
				JSONObject finalResult = new JSONObject(tokener);
				userPref.setPontuacao( Integer.parseInt( finalResult.get("pontuacao").toString() )  );
				return userPref.getPontuacao();
	        }
	        else
	        {
	        	return getPontuacao();
	        }
	        
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return getPontuacao();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getPontuacao();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getPontuacao();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getPontuacao();
		}
	}
	
	public void decrementar() {
		
	}
	
	private class RunnableSync implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			syncPontuacao();
		}
		
	}

	
}
