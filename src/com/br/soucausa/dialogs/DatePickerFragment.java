package com.br.soucausa.dialogs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.br.logsocial.R;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

public class DatePickerFragment extends DialogFragment implements
		OnDateSetListener {
	
	  EditText dataEt;
	
	  public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the current time as the default values for the picker
		  // Use the current date as the default date in the picker
	        final Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR);
	        int month = c.get(Calendar.MONTH);
	        int day = c.get(Calendar.DAY_OF_MONTH);
	        
	       

	        // Create a new instance of DatePickerDialog and return it
	        return new DatePickerDialog(getActivity(), this, year, month, day);
	 }

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		final Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		Date date = c.getTime();
		String strDate = new SimpleDateFormat("d/M/y").format(date);
		dataEt = (EditText) getActivity().findViewById(R.id.dataForm);
		dataEt.setText(strDate);
		
	}

}
