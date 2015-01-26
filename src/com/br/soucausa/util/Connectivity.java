package com.br.soucausa.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connectivity {

	 public static NetworkInfo getNetworkInfo(Context context){
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    return cm.getActiveNetworkInfo();
	 }
	
	 public static boolean isConnected(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected());
	 }
	 
	 public static boolean isConnectedWifi(Context context){
	    NetworkInfo info = Connectivity.getNetworkInfo(context);
	    return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
	 }
	 
	 public static boolean isConnectedMobile(Context context){
		NetworkInfo info = Connectivity.getNetworkInfo(context);
		return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
	 }
	 
	 public static int getConnectionType(Context context) {
		 if ( Connectivity.isConnected(context) )
		 {
			 if ( Connectivity.isConnectedWifi(context) ) {
				 return ConnectivityManager.TYPE_WIFI;
			 } else if ( Connectivity.isConnectedMobile(context) ) {
				 return ConnectivityManager.TYPE_MOBILE;
			 } else {
				 return -1;
			 }
		 }
		 else
		 {
			 return -1;
		 }
	 }
	 
}
