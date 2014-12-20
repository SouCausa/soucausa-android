package com.br.logsocial.fragments;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.br.background.postCupomInfo;
import com.br.logsocial.R;
import com.br.logsocial.MainScreen;
import com.br.logsocial.dialogs.DatePickerFragment;
import com.br.logsocial.util.Pontuacao;
import com.br.logsocial.util.Settings;
import com.br.logsocial.util.Validation;
import com.br.model.Cupom;
import com.br.model.Ong;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link TypeCupomFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link TypeCupomFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class TypeCupomFragment extends Fragment{

	
	public Long ONG_ID;
	public String ONG_RAZAO_SOCIAL;

	EditText dataEt;
	EditText cnpjEt;
	EditText cooEt;
	EditText valorEt;
	Button bt2;
	

	
	String mCurrentPhotoPath;

	private OnCupomFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment SendCupomFragment.
	 */
	
	public static TypeCupomFragment newInstance(String param1, String param2) {
		TypeCupomFragment fragment = new TypeCupomFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public TypeCupomFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View cupomView = inflater.inflate(R.layout.fragment_send_cupom, container, false); 
		
		displayPontuacaoOnActionBar();

		bt2 = (Button) cupomView.findViewById(R.id.buttonDoar);
		dataEt = (EditText) cupomView.findViewById(R.id.dataForm);
		cnpjEt = (EditText) cupomView.findViewById(R.id.cnpjForm);
		cooEt = (EditText) cupomView.findViewById(R.id.cooForm);
		valorEt = (EditText) cupomView.findViewById(R.id.valorForm);

		dataEt.setFocusable(true);
		dataEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if (hasFocus)
					showDatePickerDialog(v);
			}
		});

		valorEt.addTextChangedListener(new TextWatcher() {

			private boolean isUpdating = false;
			private NumberFormat nf = NumberFormat.getCurrencyInstance();

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (isUpdating) {
					isUpdating = false;
					return;
				}

				isUpdating = true;
				String str = s.toString();

				boolean hasMask = ((str.indexOf("R$") > -1 || str.indexOf("$") > -1) && (str
						.indexOf(".") > -1 || str.indexOf(",") > -1));

				if (hasMask) {
					str = str.replaceAll("[R$]", "").replaceAll("[,]", "")
							.replaceAll("[.]", "");
				}

				try {
					Currency currency = Currency.getInstance(new Locale("pt",
							"BR"));
					nf.setCurrency(currency);
					str = nf.format(Double.parseDouble(str) / 100);
					valorEt.setText(str);
					valorEt.setSelection(valorEt.getText().length());
				} catch (NumberFormatException e) {
					s = "";
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				

			}

			@Override
			public void afterTextChanged(Editable s) {
				

			}
		});

		/* Type Cupom */
		bt2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			    String Data;
			    String CNPJ;
				String COO;
				Float Valor = null;
				
				CNPJ = cnpjEt.getText().toString(); 
				Data = dataEt.getText().toString();
				COO = cooEt.getText().toString(); 
				
				String tmp = valorEt.getText().toString();				
				//format on server side
				tmp = tmp.replaceAll("[R$]", "").replaceAll("[,]", "").replaceAll("[.]", "");
				if(tmp.equals("")){
					Valor = (float) 0.00;
				}else{
					Valor = Float.parseFloat( tmp );
				}
				
				
				
				
				Cupom cp = new Cupom();
				cp.setCNPJ(CNPJ);
				cp.setData(Data);
				cp.setCOO(COO);
				cp.setTotal(Valor);
				
				Ong ong = new Ong(v.getContext());
				ong.setONGId(ONG_ID);				
				cp.setOng(ong);				

				Log.d(Settings.TAG,cp.getCNPJ());
				Log.d(Settings.TAG,cp.getData());
				Log.d(Settings.TAG,cp.getCOO());
				Log.d(Settings.TAG, Float.toString(cp.getTotal()));
				
				
				if("".equals(cp.getCNPJ()) || "".equals(cp.getCOO()) || "".equals(cp.getTotal())){
					Toast.makeText(getActivity(), "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if ( Validation.isCnpjValido(cp.getCNPJ()) ) {
					MainScreen activity = (MainScreen) getActivity();
					cp.setCausaId( activity.getCausaId() );
					new postCupomInfo(getActivity()).execute(cp);
				}
				else{
					Log.d(Settings.TAG,"nao validado!");
					Toast.makeText(getActivity(), "CNPJ invalido!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		
		
		return cupomView;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onCupomFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnCupomFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnCupomFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onCupomFragmentInteraction(Uri uri);
	}

	public void displayPontuacaoOnActionBar() {
		Pontuacao pontuacao = new Pontuacao(getActivity());
		int pontos = pontuacao.getPontuacao();

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setCustomView(R.layout.custom_actionbar);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		TextView acTvPontuacao = (TextView) getActivity().findViewById(
				R.id.AcTvPontuacao);
		String msg = null;
		if (pontos > 1)
			msg = Integer.toString(pontos) + " Pontos";
		else
			msg = Integer.toString(pontos) + " Ponto";

		acTvPontuacao.setText(msg);
		acTvPontuacao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(v.getContext(), "Click na pontuação",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getActivity().getFragmentManager(), "datePicker");
	}

	public void takeThePicture() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		if (takePictureIntent
				.resolveActivity(getActivity().getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, Settings.REQUEST_IMAGE_CAPTURE);
		}
	}




}
