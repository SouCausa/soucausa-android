package com.br.soucausa.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {
	
	static final String PREFS_NAME = "UserInfo";
	
	static final String KEY_USER_NAME = "username";
	static final String KEY_PONTUACAO = "pontuacao";
	static final String KEY_USER_TIPO = "tipo";
	static final String KEY_NIVEL = "nivel";
	static final String KEY_CAUSA_ID = "causaId";
	static final String KEY_USER_ID = "userId";
	static final String KEY_DEVICE_ID = "deviceId";
	static final String KEY_STATUS = "user_status";
	
	static final String KEY_REMINDER_TIME = "reminder_time";
	
	static final int USER_IMPLICIT = 17;
	static final int USER_EXPLICIT = 18;
	
	
	static final int USER_NIVEL_LATA = 1;
	static final int USER_NIVEL_BRONZE = 2;
	
	static final String TAG = "SalveCupom";
	private SharedPreferences sharedPreferences;
	
	public UserPreference(Context context) {
		sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
	}
	
	public boolean hasDeviceId() {
		if ( sharedPreferences.contains( KEY_DEVICE_ID ) )
			return true;
		else
			return false;
	}
	
	public boolean hasUserId() {
		if ( sharedPreferences.contains( KEY_USER_ID ) )
			return true;
		else
			return false;
	}
	
	public long getReminderTime() {
		return sharedPreferences.getLong(KEY_REMINDER_TIME, -1);
	}
	
	public String getCausaId() {
		return sharedPreferences.getString(KEY_CAUSA_ID, null);
	}
	
	public String getDeviceId() {
		return sharedPreferences.getString(KEY_DEVICE_ID, null);
	}
	
	public String getUserName() {
		return sharedPreferences.getString(KEY_USER_NAME, null);
	}
	
	public int getTipo() {
		return sharedPreferences.getInt(KEY_USER_TIPO, -1);
	}
	
	public int getPontuacao() {
		return sharedPreferences.getInt(KEY_PONTUACAO, 0);
	}
	
	public int getNivel() {
		return sharedPreferences.getInt(KEY_USER_TIPO, -1);
	}
	
	public int getUserId() {
		return sharedPreferences.getInt(KEY_USER_ID, -1);
	}
	
	public void setUserName(String username) {
		sharedPreferences.edit().putString(KEY_USER_NAME, username).commit();
	}
	
	public void setReminderTime(long reminderTime) {
		sharedPreferences.edit().putLong(KEY_REMINDER_TIME, reminderTime).commit();
	}
	
	public void setTipo(int Tipo) {
		sharedPreferences.edit().putInt(KEY_USER_TIPO, Tipo).commit();
	}
	
	public void setNivel(int Nivel) {
		sharedPreferences.edit().putInt(KEY_NIVEL, Nivel).commit();
	}
	
	public void setUserId(int Id) {
		sharedPreferences.edit().putInt(KEY_USER_ID, Id).commit();
	}
	
	public void setCausaId(String Id) {
		sharedPreferences.edit().putString(KEY_CAUSA_ID, Id).commit();
	}
	
	public void setPontuacao(int Pontuacao) {
		sharedPreferences.edit().putInt(KEY_PONTUACAO, Pontuacao).commit();
	}
	
	public void setDeviceId(String deviceId) {
		sharedPreferences.edit().putString(KEY_DEVICE_ID, deviceId).commit();
	}
	
	public int getUserStatus() {
		return sharedPreferences.getInt(KEY_STATUS, -1);
	}
	
	public void setUserAsImplicit() {
		sharedPreferences.edit().putInt(KEY_STATUS, USER_IMPLICIT).commit();
	}
	
	public void setUserAsExplicit() {
		sharedPreferences.edit().putInt(KEY_STATUS, USER_EXPLICIT).commit();
	}
	
	public boolean isImplicit(){
		if ( this.getUserStatus()  == USER_IMPLICIT)
			return true;
		else
			return false;
	}
	
	public boolean isExplicit() {
		if ( this.getUserStatus()  != USER_IMPLICIT)
			return true;
		else
			return false;
	}
	
	public boolean isDeviceVirgem() {
		if ( this.getDeviceId() != null )
			return false;
		else
			return true;
	}
	
	public void cleanUserPreferences() {
		
	}

}
