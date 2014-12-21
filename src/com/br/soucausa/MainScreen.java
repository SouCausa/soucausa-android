package com.br.soucausa;


import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.br.logsocial.R;
import com.br.soucausa.fragments.CausasGridFragment;
import com.br.soucausa.fragments.TakePictureFragment;
import com.br.soucausa.listeners.TabListener;
import com.br.soucausa.util.Pontuacao;
import com.br.soucausa.util.UserPreference;

public class MainScreen extends FragmentActivity implements CausasGridFragment.OnFragmentInteractionListener, TakePictureFragment.OnPictureFragmentInteractionListener{

	ActionBar.Tab takePictureTab, sendCupomTab, gameficationTab, causasTab, causasGridTab;
//	TypeCupomFragment sendCupomFragment;
	TakePictureFragment takePictureFragment;
	CausasGridFragment causasGridFragment;
	String causaId = null;
	
	UserPreference userPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_manager);
		
		userPref = new UserPreference(this);
		takePictureFragment = new TakePictureFragment();
		causasGridFragment = new CausasGridFragment();
//		sendCupomFragment = new SendCupomFragment();
		
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		takePictureTab = actionBar.newTab().setText(R.string.chooseCameraButton);
		causasGridTab = actionBar.newTab().setText("Escolher Causa");
//		sendCupomTab = actionBar.newTab().setText(R.string.chooseTypeButton);
		
		takePictureTab.setTabListener(new TabListener<TakePictureFragment>(this, "picture", TakePictureFragment.class));
		causasGridTab.setTabListener(new TabListener<CausasGridFragment>(this, "causas grid", CausasGridFragment.class));
//		sendCupomTab.setTabListener(new TabListener<SendCupomFragment>(this, "cupom", SendCupomFragment.class));
		
		if ( userPref.isDeviceVirgem() || userPref.getCausaId() == null)
		{
			actionBar.addTab(causasGridTab);
			actionBar.addTab(takePictureTab);
//			actionBar.addTab(sendCupomTab);
		}
		else
		{
			actionBar.addTab(takePictureTab);
			actionBar.addTab(causasGridTab);
//			actionBar.addTab(sendCupomTab);
		}
		
		if ( userPref.getCausaId() != null )
			displayCausaOnActionBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.screen_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPictureFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFragmentInteraction(String id) {
		getActionBar().setSelectedNavigationItem(takePictureTab.getPosition()); 
		String msg = "Causa: "+CausasGridFragment.causasImg.get(Integer.parseInt(id)).getDescription() + ", selecionada";
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		causaId = id;
		userPref.setCausaId(id);
		displayCausaOnActionBar();
	}
	
	public void displayCausaOnActionBar() {

		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(R.layout.custom_actionbar);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		
		TextView acTvPontuacao = (TextView)findViewById(R.id.AcTvPontuacao);
		
		UserPreference userPref = new UserPreference(this);
		int causaId = Integer.parseInt(userPref.getCausaId());
		if ( CausasGridFragment.causasImg != null && CausasGridFragment.causasImg.size() > 0 )
		{
			String msg = CausasGridFragment.causasImg.get(causaId).getDescription();
			acTvPontuacao.setText(msg);
		}
	}
	
	public String getCausaId() {
		return causaId;
	}
}
