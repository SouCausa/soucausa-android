package com.br.logsocial.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Validation {

	public static boolean isCnpjValido(String cnpj)
	{  
		Log.d(Settings.TAG,cnpj);
		
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
