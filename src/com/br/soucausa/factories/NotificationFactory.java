package com.br.soucausa.factories;

public class NotificationFactory {
	public static ScNotification createNotification(String type) {
		
		if ( type.equals(ScNotification.ScType.STATUS_BAR ) )
		{
			return new ScStatusBarNotification();
		}
		else if ( type.equals(ScNotification.ScType.TOAST ) )
		{
			return new ScToastNotification();
		}
		else
		{
			//throwing exception ?
			return null;
		}
	}
}
