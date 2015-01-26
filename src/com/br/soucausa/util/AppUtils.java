package com.br.soucausa.util;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppUtils {

	public static boolean isNetworkAvailable(Context context)
	{
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();	    
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static boolean isThisTimestampLunchTime(long timestamp) {
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(timestamp);
		
		int hour_of_day = time.get( Calendar.HOUR_OF_DAY );
		
		if ( ( hour_of_day >= Constants.INITIAL_LUNCH_TIME ) && (hour_of_day <= Constants.EMD_LUNCH_TIME) )		
			return true;
		else
			return false;
	}
	
	public static boolean isNowLunchTime() {
		Calendar now = Calendar.getInstance();
		
		int hour_of_day = now.get( Calendar.HOUR_OF_DAY );
		
		if ( ( hour_of_day >= Constants.INITIAL_LUNCH_TIME ) && (hour_of_day <= Constants.EMD_LUNCH_TIME) )		
			return true;
		else
			return false;
	}
	
	public static boolean isThisTimestampToday(long timestamp) {
		
		Calendar now = Calendar.getInstance();
		int currentDayOfMonth = now.get( Calendar.DAY_OF_MONTH );
		int currentMonth = now.get( Calendar.MONTH );
		int currentYear = now.get( Calendar.YEAR );
		
		now.setTimeInMillis(timestamp);
		int paramDayOfMonth = now.get( Calendar.DAY_OF_MONTH );
		int paramMonth = now.get( Calendar.MONTH );
		int paramYear = now.get( Calendar.YEAR );
		
		if ( ( paramDayOfMonth == currentDayOfMonth ) && ( paramMonth == currentMonth )  && ( paramYear == currentYear ) )
			return true;
		else
			return false;
	}
	
	public static boolean isCnpjValido(String cnpj)
	{  
		if("".equals(cnpj)){
			return false;
		}
		
        if (!cnpj.substring(0, 1).equals("")) {  
            try {
                int soma = 0, dig;  
                String cnpj_calc = cnpj.substring(0, 12);  
  
                if (cnpj.length() != 14) {  
                    return false;  
                }  
                char[] chr_cnpj = cnpj.toCharArray();  
                /* Primeira parte */  
                for (int i = 0; i < 4; i++) {  
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {  
                        soma += (chr_cnpj[i] - 48) * (6 - (i + 1));  
                    }  
                }  
                for (int i = 0; i < 8; i++) {  
                    if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {  
                        soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));  
                    }  
                }  
                dig = 11 - (soma % 11);  
                cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(  
                        dig);  
                /* Segunda parte */  
                soma = 0;  
                for (int i = 0; i < 5; i++) {  
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {  
                        soma += (chr_cnpj[i] - 48) * (7 - (i + 1));  
                    }  
                }  
                for (int i = 0; i < 8; i++) {  
                    if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {  
                        soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));  
                    }  
                }  
                dig = 11 - (soma % 11);  
                cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(  
                        dig);  
                return cnpj.equals(cnpj_calc);  
            }  
            catch (Exception e) {  
                return false;  
            }  
        }  
        else
        {  
            return false;  
        }  
    }  

}
