package com.br.soucausa.factories;

import android.content.Context;
import android.widget.Toast;

public class ScToastNotification extends ScNotification {
	public int duration = 0;
	public String message = "";
	Context mContext;

	public void doNotifty() {
		super.doNotifty();
		
		if ( ! this.message.isEmpty() && this.hasContext() ) {
			if ( this.duration > 0 ) {
				Toast.makeText(this.mContext, this.message , this.duration).show();
			} else {
				this.duration = Toast.LENGTH_LONG; 
				Toast.makeText(this.mContext, this.message , this.duration).show();
			}
		} else {
			//throw exception telling what's wrong
		}
	}
	
	public void doNotify(String message) {
		super.doNotifty();
		this.message = message;
		
		if ( ! this.message.isEmpty() && this.hasContext() ) {
			if ( this.duration > 0 ) {
				Toast.makeText(this.mContext, this.message , this.duration).show();
			} else {
				this.duration = Toast.LENGTH_LONG; 
				Toast.makeText(this.mContext, this.message , this.duration).show();
			}
		} else {
			//throw exception telling what's wrong
		}
	}
	
	public void doNotify(String message,Context context) {
		super.doNotifty();
		this.message = message;
		this.mContext = context;
		
		if ( ! this.message.isEmpty() && this.hasContext() ) {
			if ( this.duration > 0 ) {
				Toast.makeText(this.mContext, this.message , this.duration).show();
			} else {
				this.duration = Toast.LENGTH_LONG; 
				Toast.makeText(this.mContext, this.message , this.duration).show();
			}
		} else {
			//throw exception telling what's wrong
		}
	}
	
	public void doNotify(String message,int duration,Context context) {
		super.doNotifty();
		this.message = message;
		this.mContext = context;
		this.duration = duration;
		
		if ( ! this.message.isEmpty() && this.hasContext() ) {
			if ( this.duration > 0 ) {
				Toast.makeText(this.mContext, this.message , this.duration).show();
			} else {
				this.duration = Toast.LENGTH_LONG; 
				Toast.makeText(this.mContext, this.message , this.duration).show();
			}
		} else {
			//throw exception telling what's wrong
		}
	}
	
	public boolean hasContext() {
		if ( this.mContext != null ) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
}
