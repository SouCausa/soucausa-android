package com.br.soucausa;

import com.br.logsocial.R;
import com.br.soucausa.background.PostDeviceInfo;
import com.br.soucausa.callbacks.CallbackDadoInicial;
import com.br.soucausa.factories.NotificationFactory;
import com.br.soucausa.factories.ScNotification;
import com.br.soucausa.factories.ScNotification.ScType;
import com.br.soucausa.factories.ScToastNotification;
import com.br.soucausa.util.AppUtils;
import com.br.soucausa.util.DeviceInfo;
import com.br.soucausa.util.UserPreference;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity implements CallbackDadoInicial {
	
	UserPreference userPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		//CODE TEST BLOCK
			ScToastNotification toastNot = (ScToastNotification) NotificationFactory.createNotification(ScNotification.ScType.TOAST);
			toastNot.doNotify("testando", this.getApplicationContext());
		//CODE TEST BLOCK
		
		userPref = new UserPreference(this);
		DeviceInfo deviceInfo = new DeviceInfo(this);
	
		if ( !AppUtils.isNetworkAvailable(this) )
		{
			if ( !userPref.hasDeviceId() )
			{
				userPref.setDeviceId(deviceInfo.getDeviceId());
			}
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
				
				Bundle infos = new Bundle();
				infos.putString("deviceId", deviceId );
				new PostDeviceInfo(this).execute(infos);
			}
			
		}
		 
	   //simulating our future syncs to the server
       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	goMainActivity();
            }
        }, 2500);
       
	}
	
	private void goMainActivity()
	{
		Intent i = new Intent(this, MainScreen.class);
		this.startActivity(i);
		this.finish();
	}

	@Override
	public void onTaskComplete()
	{
		goMainActivity();
	}

}
