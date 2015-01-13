package com.br.soucausa.factories;

public abstract class ScNotification {
	
	public void doNotifty() {
		//save some statistical data
		//do common things to all notification
	}

	public static class ScType {
		public static String TOAST = "toast";
		public static String STATUS_BAR = "statusBar";
	}
}
