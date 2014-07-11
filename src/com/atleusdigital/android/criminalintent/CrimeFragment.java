package com.atleusdigital.android.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class CrimeFragment extends Fragment {
	
	private final static String TAG = "CrimeFragment.java";
	public static final String EXTRA_CRIME_ID = "extraCrimeID";
	public static final String KEY_UUID = "crime_key";
	private static final String DIALOG_DATE = "date";
	private static final int REQUEST_DATE = 0;
	
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		UUID crimeID;
		/* first we check to see if there are any arguments supplied, in the case of startup, there are no arguments
		 * therefore we check for this and if that's the case display the first element only
		 */
		if ( args != null ) {
			crimeID = (UUID) args.getSerializable(EXTRA_CRIME_ID);
		}  else {
			crimeID = CrimeLab.get(getActivity()).getCrimes().get(0).getmId();
		}
		
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
	}
	
	public static CrimeFragment newInstance(UUID id) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, id);
		Log.i(TAG, "id = " + id.toString());
		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	// fragments cannot return results, only activities can
	public void returnResult(){
		getActivity().setResult(Activity.RESULT_OK, null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime, container, false);
		
		mTitleField = (EditText) v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		
		// add text change listener
		mTitleField.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mCrime.setTitle(s.toString());
				returnResult();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// blank
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// blank;
			}
		});
		
		mDateButton = (Button) v.findViewById(R.id.crime_date);
		Calendar cal = Calendar.getInstance();
		Log.i("Crime.java", "Friday is: " + cal.get(Calendar.DAY_OF_WEEK));
		updateDate();
		
		mDateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});
		
		mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// Set the crime's isSolved property
				mCrime.setSolved(isChecked);
			}
			
		});
		
		
		
		return v;
	}
	
	public void updateDate() {
		Calendar cal = Calendar.getInstance();
		Date thisDate = mCrime.getDate();
		
		cal.setTime(thisDate);
		
		mDateButton.setText(mCrime.getDate().toString());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		if (requestCode == REQUEST_DATE) {
			Date date = (Date)data.getSerializableExtra(
					DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();
		}
	}	
	
	
	

}
