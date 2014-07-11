package com.atleusdigital.android.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment(Class<?> c) throws InstantiationException, IllegalAccessException {
		return (Fragment)c.newInstance();
	}

}
