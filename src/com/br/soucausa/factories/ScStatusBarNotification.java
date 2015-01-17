package com.br.soucausa.factories;

import com.br.logsocial.R;
import com.br.soucausa.util.Constants;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class ScStatusBarNotification extends ScNotification {

	public Context mContext;
	public String tickerText;
	public String title;
	public int when;
	public int icon;
	public Intent mIntent;
	
	public int notifId = 1;

	@Override
	public void doNotifty() {
		super.doNotifty();
	}
	
	public void doNotify(String title,String tickerText,Context context) {
		super.doNotifty();
		
		Log.d(Constants.TAG,"doNotify");
		
		mContext = context;
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(tickerText);
		mBuilder.setTicker(tickerText);
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notifId,mBuilder.build());
	}
	
	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	public String getTickerText() {
		return tickerText;
	}

	public void setTickerText(String tickerText) {
		this.tickerText = tickerText;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getWhen() {
		return when;
	}

	public void setWhen(int when) {
		this.when = when;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public Intent getmIntent() {
		return mIntent;
	}

	public void setmIntent(Intent mIntent) {
		this.mIntent = mIntent;
	}

	public int getNotifId() {
		return notifId;
	}

	public void setNotifId(int notifId) {
		this.notifId = notifId;
	}
	
}
