package com.atleusdigital.android.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public abstract class SingleFragmentActivity extends FragmentActivity {
	
	final static String TAG = "SingleFragmentActivity";
	
	protected abstract Fragment createFragment(Class<?> c) throws InstantiationException, IllegalAccessException;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		Fragment fragmentContent = fm.findFragmentById(R.id.contentContainer);
		
		if ( fragment == null ) {
			try {
				fragment = createFragment(CrimeListFragment.class);
			} catch (InstantiationException e) {
				Log.i(TAG, "CAUGHT INSTANTIATION EXCEPTION");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				Log.i(TAG, "CAUGHT ILLEGAL ACCESS");
				e.printStackTrace();
			}
			/* for fragments to get arguments, fragments must be 
				created before adding to fragmentmanager */
			fm.beginTransaction()
				.add(R.id.fragmentContainer, fragment)
				.commit();
		} 
		
		if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ) {
			if ( fragmentContent == null ) {
				/* create content fragment if nothing is placed here yet */
				try {
					fragmentContent = createFragment(CrimeFragment.class);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				fm.beginTransaction()
					.add(R.id.contentContainer, fragmentContent)
					.commit();
			}
		}
	}
	
}
