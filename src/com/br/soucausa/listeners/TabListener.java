package com.br.soucausa.listeners;

import com.br.logsocial.R;
import com.br.soucausa.util.Settings;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class TabListener <T extends Fragment> implements ActionBar.TabListener {
	
	private Fragment fragment;
	private final FragmentActivity mActivity;
    private final String mTag;
    private final Class<T> mClass;
    private FragmentTransaction fft;
    
    public TabListener(FragmentActivity mActivity, String mTag,Class<T> mClass) {
		this.mActivity = mActivity;
		this.mTag = mTag;
		this.mClass = mClass;
	}
	
	
	@Override
	public void onTabReselected(Tab arg0, android.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0, android.app.FragmentTransaction ft) {
		fragment = mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
		fft = mActivity.getSupportFragmentManager().beginTransaction();
		if(fragment == null) {
			fragment = Fragment.instantiate(mActivity, mClass.getName());
	        fft.add(R.id.container, fragment, mTag);
	        fft.commit();
            mActivity.getSupportFragmentManager().executePendingTransactions();
		} else {
			fft.attach(fragment);
            fft.commit();
            mActivity.getSupportFragmentManager().executePendingTransactions();
		}
	}

	@Override
	public void onTabUnselected(Tab arg0, android.app.FragmentTransaction ft) {
		fragment = mActivity.getSupportFragmentManager().findFragmentByTag(mTag);

        if (fragment != null && !fragment.isDetached())
        {
            fft = mActivity.getSupportFragmentManager().beginTransaction();
            fft.detach(fragment);
            fft.commit();
            mActivity.getSupportFragmentManager()
                    .executePendingTransactions();
        }
		
	}

}
