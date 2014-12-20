package com.br.soucausa.fragments;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.br.logsocial.R;
import com.br.soucausa.MainScreen;
import com.br.soucausa.background.PostPhoto;
import com.br.soucausa.callbacks.CallbackPostPhoto;
import com.br.soucausa.model.Cupom;
import com.br.soucausa.model.Ong;
import com.br.soucausa.util.Settings;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link TakePictureFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link TakePictureFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class TakePictureFragment extends Fragment implements CallbackPostPhoto{

	public Long ONG_ID;
	public String ONG_RAZAO_SOCIAL;
	String mCurrentPhotoPath;
	TextView tv;
	Button bt;
	private OnPictureFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment TakePictureFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static TakePictureFragment newInstance(String param1, String param2) {
		TakePictureFragment fragment = new TakePictureFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public TakePictureFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Take Picture */

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View pictureFragmentView = inflater.inflate(
				R.layout.fragment_take_picture, container, false);

		bt = (Button) pictureFragmentView.findViewById(R.id.takePictureButton);
		tv = (TextView) pictureFragmentView.findViewById(R.id.choosen_ong);
		bt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dispatchTakePictureIntent();
			}
		});

		return pictureFragmentView;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onPictureFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnPictureFragmentInteractionListener) activity;
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
	public interface OnPictureFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onPictureFragmentInteraction(Uri uri);
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent
				.resolveActivity(getActivity().getPackageManager()) != null) {
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
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent,
						Settings.REQUEST_TAKE_PHOTO);
			}
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		Log.d(Settings.TAG, "photo created 1");
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		Log.d(Settings.TAG, "photo created 2");
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		Log.d(Settings.TAG, "photo created");
		return image;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Settings.REQUEST_TAKE_PHOTO
				&& resultCode == Activity.RESULT_OK) {
			if (data == null)
				Log.d(Settings.TAG, "data == null");
			/*
			 * Bundle extras = data.getExtras(); Bitmap imageBitmap = (Bitmap)
			 * extras.get("data"); ImageView mImageView = (ImageView)
			 * findViewById(R.id.taken_photo);
			 * mImageView.setImageBitmap(imageBitmap);
			 */
			this.galleryAddPic();
		}

		if (requestCode == Settings.REQUEST_CHOOSEN_ONG
				&& resultCode == Activity.RESULT_OK) {
			ONG_ID = data.getLongExtra("ongId", (long) -1);
			ONG_RAZAO_SOCIAL = data.getStringExtra("razao_social");

			if (ONG_ID != -1) {

				tv.setText(ONG_RAZAO_SOCIAL);

			}
		}
	}

	private void galleryAddPic() {

	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.parse(mCurrentPhotoPath);
	    
        Ong ong = new Ong(getActivity());
        Cupom cp = new Cupom();

        File f = new File( uri.getPath() );
        mediaScanIntent.setData( Uri.fromFile(f) );
        cp.setOng(ong);
        cp.setFile(f);
        
        MainScreen activity = (MainScreen) getActivity();
        cp.setCausaId(activity.getCausaId());
        
        getActivity().sendBroadcast(mediaScanIntent);
        
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        new PostPhoto(getActivity(),this,connectivityManager).execute(cp);
	}

	@Override
	public void onTaskComplete() {	
		Log.d(Settings.TAG,"["+ this.getClass().toString() +"] onTaskComplete");
//		Intent i = new Intent(getActivity().getApplicationContext(),ThankYouActivity.class);
//		startActivity(i);
	}
}
