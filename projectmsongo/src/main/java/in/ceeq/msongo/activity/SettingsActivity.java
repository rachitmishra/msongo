package in.ceeq.msongo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import in.ceeq.msongo.R;
import in.ceeq.msongo.fragment.SettingsFragment;

public class SettingsActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_settings);
		getFragmentManager().beginTransaction().add(R.id.content, new SettingsFragment()).commit();
	}

	@Override
	public void onBackPressed() {
		if (!getFragmentManager().popBackStackImmediate()) {
			finish();
		}

	}
}