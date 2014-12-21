package com.br.soucausa.fragments;

import java.util.ArrayList;

import com.br.logsocial.R;
import com.br.soucausa.model.Causa;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class CausasGridFragment extends Fragment {
	private GridView myGrid;
	public Context mContext;
	private OnFragmentInteractionListener mListener;
	static public ArrayList<Causa> causasImg;
	
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return super.getView();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity().getApplicationContext();
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View causasGridView = inflater.inflate(R.layout.causas_gridview, container, false);
		myGrid = (GridView) causasGridView.findViewById(R.id.gridView1);
		myGrid.setAdapter(new CausasGridAdapter(mContext));
		myGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				mListener.onFragmentInteraction(Integer.toString(position));
			}
			
		});
		
		
		return causasGridView;
	}
	
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(String id);
	}
	
	private class CausasGridAdapter extends BaseAdapter {
		Context mContext;
		
		CausasGridAdapter(Context context) {
			causasImg = new ArrayList<Causa>();
			this.mContext = context;
			causasImg.add(new Causa(1,R.drawable.idoso_saude,"Idoso - Saúde"));
			causasImg.add(new Causa(2,R.drawable.idoso_protecao,"Idoso - Proteção"));
			causasImg.add(new Causa(3,R.drawable.saude_crianca,"Criança - Saúde"));
			causasImg.add(new Causa(4,R.drawable.social_crianca,"Criança - Social"));
			causasImg.add(new Causa(5,R.drawable.saude_mulher,"Mulher - Saúde"));
			causasImg.add(new Causa(6,R.drawable.protecao_soc_mulher,"Mulher - Proteção Social"));
			causasImg.add(new Causa(7,R.drawable.educacao,"Educação"));
			causasImg.add(new Causa(8,R.drawable.animal,"Animal"));
			causasImg.add(new Causa(8,R.drawable.meio_ambiente,"Meio Ambiente"));
			causasImg.add(new Causa(9,R.drawable.adolescente_saude,"Adolescente - Saúde"));
			causasImg.add(new Causa(10,R.drawable.adolescente_prot_social,"Adolescente - Proteção Social"));
		}
		
		class ViewHolder {
			ImageView imageView;
			ViewHolder(View v) {
				imageView = (ImageView) v.findViewById(R.id.imageView1); //Expensive
			}
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return causasImg.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return causasImg.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View item = convertView;
			ViewHolder viewholder = null;
			
			if ( item == null )
			{
				 //first time getView get called
				LayoutInflater lf = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				item = lf.inflate(R.layout.causa_gridview_item, parent,false); //Expensive Operation... need to be cached
				viewholder = new ViewHolder(item);
				item.setTag(viewholder);
			}
			else
			{
				viewholder = (ViewHolder) item.getTag();
			}
			
			viewholder.imageView.setImageResource( causasImg.get(position).getResource() );
			
			return item;
		}
		
	}

}
