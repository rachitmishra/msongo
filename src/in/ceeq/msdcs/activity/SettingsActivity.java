package in.ceeq.msdcs.activity;

import in.ceeq.msdcs.R;
import in.ceeq.msdcs.fragment.SettingsFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class SettingsActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_settings);
		getFragmentManager().beginTransaction().add(R.id.content, new SettingsFragment()).commit();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (!getFragmentManager().popBackStackImmediate()) {
			finish();
		}

	}
}