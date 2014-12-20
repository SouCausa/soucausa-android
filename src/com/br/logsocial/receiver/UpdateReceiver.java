package com.br.logsocial.receiver;

import com.br.logsocial.services.SendPhotoService;
import com.br.logsocial.util.Settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.sax.StartElementListener;
import android.util.Log;

public class UpdateReceiver extends BroadcastReceiver {

    private static boolean firstConnect = true;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(Settings.TAG,intent.getAction());
		
	    if ( intent.getAction() != null &&  intent.getAction() ==  "android.net.conn.CONNECTIVITY_CHANGE")
	    {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE | ConnectivityManager.TYPE_WIFI);
	
			boolean isConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
			
			if (isConnected)
			{
				if (firstConnect) { 
	                // do subroutines here
					Intent i = SendPhotoService.makeIntent(context);
					context.startService(i);
					
	                firstConnect = false;
	                Log.i(Settings.TAG, "ESTA CONNECTADO");
	            }
			}
			else
			{
				firstConnect= true;
				Log.i(Settings.TAG, "NÌO ESTA CONNECTADO");
			}
	    }

	}

}
