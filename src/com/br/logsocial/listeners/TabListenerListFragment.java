package com.br.logsocial.listeners;

import com.br.logsocial.util.Settings;

import android.app.ActionBar.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.app.ActionBar;

public class TabListenerListFragment <T extends ListFragment> implements ActionBar.TabListener {
	private ListFragment fragment;
	private final FragmentActivity mActivity;
    private final String mTag;
    private final Class<T> mClass;
    
    public TabListenerListFragment(FragmentActivity mActivity, String mTag,
			Class<T> mClass) {
		this.mActivity = mActivity;
		this.mTag = mTag;
		this.mClass = mClass;
	}


	@Override
	public void onTabSelected(Tab arg0, android.app.FragmentTransaction ft) {
		android.support.v4.app.FragmentTransaction ftt = mActivity.getSupportFragmentManager().beginTransaction();
		if(fragment == null){
			fragment = (ListFragment) ListFragment.instantiate(mActivity, mClass.getName());
			ftt.add(android.R.id.content, fragment, mTag);
			ftt.commit();
		}else{
			ftt.attach(fragment).commit();
		}
	}


	@Override
	public void onTabUnselected(Tab arg0,
			android.app.FragmentTransaction ft) {
		android.support.v4.app.FragmentTransaction ftt = mActivity.getSupportFragmentManager().beginTransaction();
		
		Log.d(Settings.TAG,"onTabUnselected");
		if(fragment != null){
			ftt.detach(fragment).commit();
		}
		
	}


	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}
