package com.br.background;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.br.callback.CallbackDadoInicial;
import com.br.logsocial.util.Settings;
import com.br.model.Ong;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DadoInicial extends AsyncTask<Void, Void, Boolean> {

	private Context context;
	private CallbackDadoInicial callback;
	private String MyJson;
	static final String TAG = "SalveCupom";
	
	public DadoInicial(Context context,CallbackDadoInicial callback)
	{
		this.context = context;
		this.callback = callback;
	}
	
	protected String downloadJson() throws IOException
	{
		InputStream is = null;
		
		try {
			URL url = new URL(Settings.dadoInicialUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(50000 /* milliseconds */);
	        conn.setConnectTimeout(55000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        // Starts the query
	        conn.connect();
	        
	        Log.d(TAG,"["+ this.getClass().toString() +"] Entrou em ddownloadJson");
	        int response = conn.getResponseCode();
	        Log.d(TAG,"["+ this.getClass().toString() +"]"+response);
	        
	        if ( response == 200 )
	        {
		        is = conn.getInputStream();	        
		        String contentAsString = readIt(is);
		        return contentAsString;
	        }
	        else
	        {
	        	return "";
	        }
		}
		catch ( IOException e)
		{
			e.printStackTrace();
			return "Deu Pau aqui";
		}			
		finally {
			if (is != null)
			{
	            is.close();
	        } 
		}
	}
	
	protected String readIt(InputStream input) throws IOException, UnsupportedEncodingException
	{			
	    BufferedReader r = new BufferedReader(new InputStreamReader(input,Charset.forName("UTF-8")));
	    StringBuilder total = new StringBuilder();
	    String line;
	    while ((line = r.readLine()) != null)	    	
	        total.append(line);

	    return total.toString(); 	
	}
	
	private void parseMyJson() throws JSONException, UnsupportedEncodingException
	{
		JSONObject json = new JSONObject(MyJson);	
		JSONObject jsonOng;
		Iterator<?> keys = json.keys();
		String ongId;
		Ong ong = new Ong(context);
		
		while( keys.hasNext() )
		{
			ongId = (String) keys.next();
            jsonOng = (JSONObject) json.get(ongId);
            ong.setONGId( Long.parseLong( ongId ));
            ong.setRazaoSocial( jsonOng.get("razao_social").toString() );
            ong.setBairro( jsonOng.get("bairro").toString() );
            ong.setNomeFantasia(jsonOng.get("nome_fantasia").toString());
            ong.setCNPJ(jsonOng.get("cnpj").toString());
            ong.setRua(jsonOng.get("rua").toString());
            ong.setNumero(jsonOng.get("numero").toString());
            ong.setComplemento( jsonOng.get("complemento").toString() );
            ong.setCidade( jsonOng.get("cidade").toString() );
            ong.setUF(jsonOng.get("uf").toString());
            ong.setBairro(jsonOng.get("bairro").toString());
            ong.setCep(jsonOng.get("cep").toString());
            ong.setExpediente(jsonOng.get("expediente").toString());
            ong.setTelefone(jsonOng.get("telefone").toString());
            ong.setEmail(jsonOng.get("email").toString());
            ong.setFax(jsonOng.get("fax").toString());
            ong.setSite(jsonOng.get("site").toString());
            ong.setData_fundacao(jsonOng.get("data_fundacao").toString());
            ong.setCRCE(jsonOng.get("crce").toString());
            ong.setPublico_alvo(jsonOng.get("publico_alvo").toString());
            ong.setArea_atuacao(jsonOng.get("area_atuacao").toString());
            ong.setEquipe(jsonOng.get("equipe").toString());
            ong.setHabilitada(Integer.parseInt(jsonOng.get("habilitada").toString()));
            
            ong.save();            
            ong = new Ong(context); //clear            
            
            //Log.d(TAG, jsonOng.get("razao_social").toString() );
		}
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub		
		try
		{
			Log.d(TAG,"["+ this.getClass().toString() +"] Entrou em doInBackground");
			
			MyJson = this.downloadJson();
			if ( ! MyJson.equals("") )
			{
				parseMyJson();
			}
			else
			{
				Log.d(TAG,"["+ this.getClass().toString() +"] MyJson = ''");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"["+ this.getClass().toString() +"] IOException deu errado exception");
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"["+ this.getClass().toString() +"] JSONException deu errado exception");
			e.printStackTrace();
		}
		
		
		return null;
	}

}
