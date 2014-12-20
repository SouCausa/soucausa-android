package com.br.soucausa.fragments;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.br.logsocial.R;
import com.br.soucausa.background.PostPhoto;
import com.br.soucausa.callbacks.CallbackPostPhoto;
import com.br.soucausa.model.Cupom;
import com.br.soucausa.model.Ong;
import com.br.soucausa.util.Settings;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link GamificationFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link GamificationFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class GamificationFragment extends Fragment implements CallbackPostPhoto {

	Button shareButton;
	Button newDonationButton;
	
	String mCurrentPhotoPath;
	

	public Long ong_id;
	
	

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment GamificationFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static GamificationFragment newInstance(String param1, String param2) {
		GamificationFragment fragment = new GamificationFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public GamificationFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
			}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View gameficationView = inflater.inflate(R.layout.fragment_gamification, container,false); 
		
		newDonationButton = (Button) gameficationView.findViewById(
				R.id.newDonationButton);
		shareButton = (Button) gameficationView.findViewById(R.id.shareButton);

		
		//new PostPhoto(getActivity().getApplicationContext(), this).execute(cp);

		
		return gameficationView;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onGameficationFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
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
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onGameficationFragmentInteraction(Uri uri);
	}

	@Override
	public void onTaskComplete() {
		Log.d(Settings.TAG, "[" + this.getClass().toString() + "] onTaskComplete");

		Button bt = (Button) getActivity().findViewById(R.id.newDonationButton);
		bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent();
			}
		});
	}

	private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
            	Log.d(Settings.TAG, "dispatch take picture..");
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, Settings.REQUEST_TAKE_PHOTO);
            }
        }
    }
	
	private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

}
