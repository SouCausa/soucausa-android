package com.br.soucausa;

import java.util.concurrent.CountDownLatch;

import com.br.logsocial.R;
import com.br.soucausa.background.DadoInicial;
import com.br.soucausa.background.PostDeviceInfo;
import com.br.soucausa.callbacks.CallbackDadoInicial;
import com.br.soucausa.data.DataContract;
import com.br.soucausa.util.DeviceInfo;
import com.br.soucausa.util.Pontuacao;
import com.br.soucausa.util.Settings;
import com.br.soucausa.util.SystemUiHider;
import com.br.soucausa.util.UserPreference;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class SplashScreen extends Activity implements CallbackDadoInicial {
	
	public SQLiteDatabase db;
	static final int PONTOS_INICIAIS = 20;
	UserPreference userPref;
	final CountDownLatch countSleep = new CountDownLatch( 1 );
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		userPref = new UserPreference(this);
		DeviceInfo deviceInfo = new DeviceInfo(this);
		DataContract dbHelper = new DataContract(this);
	
		if ( !this.isNetworkAvailable() )
		{
			String deviceId;
			if ( !userPref.hasDeviceId() )
			{
				deviceId = deviceInfo.getDeviceId();
				userPref.setDeviceId(deviceId);
			}
			else
				deviceId = userPref.getDeviceId();

			Log.d(Settings.TAG,"No connection available");
		}
		else
		{   
			String deviceId;
			if ( !userPref.hasDeviceId() )
			{
				deviceId = deviceInfo.getDeviceId();
				userPref.setDeviceId(deviceId);
			}
			else
				deviceId = userPref.getDeviceId();
			
			if ( !userPref.hasUserId() )
			{
				Pontuacao pontuacao = new Pontuacao(this);
				pontuacao.incrementar(PONTOS_INICIAIS);
				this.db = dbHelper.getWritableDatabase();
				
				Bundle infos = new Bundle();
				infos.putString("deviceId", deviceId );
				new PostDeviceInfo(this).execute(infos);
			}
			
			dbHelper.close();
		}
		
		 
       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	goMainActivity();
            }
        }, 2500);
       
	}
	
	private void goMainActivity()
	{
		Log.d(Settings.TAG,userPref.getDeviceId());
		Intent i = new Intent(this, MainScreen.class);
		this.startActivity(i);
		this.finish();
		if ( this.db != null )
			this.db.close();
	}
	
	private boolean isNetworkAvailable()
	{
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();	    
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	public void onTaskComplete()
	{
		Log.d(Settings.TAG,"Task has Compled");
		goMainActivity();
	}

}
