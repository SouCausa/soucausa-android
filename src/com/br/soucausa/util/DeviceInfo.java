package com.br.soucausa.util;

import android.content.Context;
import android.provider.Settings.Secure;

public class DeviceInfo {
	
	private String deviceID;
	private Context mContext;
	
	public DeviceInfo(Context context) {
		mContext = context;
	}
	
	public String getDeviceId()
	{
		deviceID = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
		return deviceID;
	}
	
	public String getAccount()
	{
		return null;
	}

}
