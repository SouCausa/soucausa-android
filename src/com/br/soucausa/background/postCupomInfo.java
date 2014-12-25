package com.br.soucausa.background;

import java.io.IOException;
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
import com.br.soucausa.model.Cupom;
import com.br.soucausa.model.Ong;
import com.br.soucausa.util.Constants;
import com.br.soucausa.util.Pontuacao;
import com.br.soucausa.util.Settings;
import com.br.soucausa.util.UserPreference;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class postCupomInfo extends AsyncTask<Object, Void, Void> {
	
	Context context;
	private ProgressDialog pDialog;
	
	
	public postCupomInfo(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(Object... cupom) {
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(Settings.postCupomInfoUrl);
	    UserPreference userPref = new UserPreference(this.context);
	    
	    Cupom cp = (Cupom) cupom[0];
	    String Data = cp.getData();
	    String CNPJ = cp.getCNPJ();
		String COO = cp.getCOO();
		float Valor = cp.getTotal();

		Ong ong = cp.getOng();
		Long ongId = ong.getONGId();
			
		JSONObject jsonObject = new JSONObject();
		try {
			
			jsonObject.put("device_id" , userPref.getDeviceId());
			jsonObject.put("data", Data);
			jsonObject.put("cnpj", CNPJ);
			jsonObject.put("coo", COO);
			jsonObject.put("valor", Valor);
			jsonObject.put("ong_id", ongId);
			
			if (cp.getCausaId() != null)
			{
				jsonObject.put("causa_fk" , cp.getCausaId());
			}
			else
			{
				if ( userPref.getCausaId() != null )
				{
					jsonObject.put("causa_fk" , userPref.getCausaId());
				}
			}
			
			httppost.setEntity( new ByteArrayEntity( jsonObject.toString().getBytes("UTF8")) );
			
			HttpResponse response = httpclient.execute(httppost);
			int postResponse = response.getStatusLine().getStatusCode();

			if (postResponse == HttpStatus.SC_OK)
			{
				Pontuacao pontuacao = new Pontuacao(context);
				pontuacao.incrementar(Constants.PONTOS_POR_FOTO);
				pontuacao.syncPontuacao();
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
	
	@Override
	protected void onPreExecute() {
		// show the progress bar
		pDialog = new ProgressDialog(context);
		pDialog.setMessage(Constants.sDialog);
		pDialog.show();
	}

	@Override
	protected void onPostExecute(Void param){
		super.onPostExecute(param);
		pDialog.dismiss();
		Toast.makeText(context, "Obrigado por doar!", Toast.LENGTH_LONG).show();
	}

}
