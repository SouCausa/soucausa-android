package com.br.soucausa.receivers;

import com.br.soucausa.factories.NotificationFactory;
import com.br.soucausa.factories.ScNotification;
import com.br.soucausa.factories.ScStatusBarNotification;
import com.br.soucausa.services.SendPhotoService;
import com.br.soucausa.util.AppUtils;
import com.br.soucausa.util.Connectivity;
import com.br.soucausa.util.Constants;
import com.br.soucausa.util.Settings;
import com.br.soucausa.util.UserPreference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.sax.StartElementListener;
import android.util.Log;

public class UpdateReceiver extends BroadcastReceiver {

	private Context mContext;
    private static boolean firstConnect = true;
    private static int lastConnectionType = -1; //none
	
	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
	    if ( intent.getAction() != null &&  intent.getAction() ==  "android.net.conn.CONNECTIVITY_CHANGE")
	    {
	    	
			if (Connectivity.isConnected(context))
			{
				if (firstConnect)
				{ 
					sendPendingPhotosViaService();
	                firstConnect = false;
	            }
			}
			else
			{
				firstConnect = true;
			}
			
			if ( shouldRemind() ) {
				Log.d(Constants.TAG,"shouldRemind - true");
				makeDonationRemind();
			} else
				Log.d(Constants.TAG,"shouldRemind - false");
			
			lastConnectionType = Connectivity.getConnectionType(context);
	    }
	    
//		treating other broadcast actions	    
//	    if ( ... ) {
//	    	
//	    }
	}
	
	public void sendPendingPhotosViaService() {
		Intent i = SendPhotoService.makeIntent(mContext);
		mContext.startService(i);
	}
	
	public boolean shouldRemind() {
		
		// TODO - check if user choose not to be remind by status notification button //
		
		if ( hasAlreadyRemindedUserToday() )
		{
			Log.d(Constants.TAG,"hasAlreadyRemindedUserToday - true");
			return false;
		}
		else
		{
			if ( AppUtils.isNowLunchTime() )
			{
				Log.d(Constants.TAG,"isNowLunchTime - true");
				return true;
			}
			else
			{
				Log.d(Constants.TAG,"isNowLunchTime - false");
				return false;
			}
		}
		
	}
	
	public boolean hasAlreadyRemindedUserToday() {
		UserPreference userPref = new UserPreference(mContext);
		long lastRemindTimestamp = userPref.getReminderTime();
		
		if ( AppUtils.isThisTimestampToday(lastRemindTimestamp) )
		{
			return false;
		}
		else
		{
			return false;
		}
	}
	
	public void makeDonationRemind() {
		
		ScStatusBarNotification statusNotification = (ScStatusBarNotification) NotificationFactory.createNotification(ScNotification.ScType.STATUS_BAR);
		statusNotification.doNotify("Sou Causa","Lembre-se de doar sua nota fiscal sem CPF",mContext);
		
		updateRemindTime();
	}
	
	public void updateRemindTime() {
		UserPreference userPref = new UserPreference(mContext);
		long timestamp = System.currentTimeMillis();
		userPref.setReminderTime(timestamp);
	}

}
