package com.br.soucausa.receivers;

import com.br.soucausa.services.SendPhotoService;
import com.br.soucausa.util.Settings;

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
	            }
			}
			else
			{
				firstConnect= true;
			}
	    }

	}

}
