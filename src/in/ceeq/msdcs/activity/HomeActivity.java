package in.ceeq.msdcs.activity;

import in.ceeq.msdcs.R;
import in.ceeq.msdcs.fragment.LoginFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class HomeActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		getSupportFragmentManager().beginTransaction().replace(R.id.container, LoginFragment.getInstance(), "Login")
				.commit();
	}
}
