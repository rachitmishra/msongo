package in.ceeq.msdcs.fragment;

import in.ceeq.msdcs.R;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements OnPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		return false;
	}
}