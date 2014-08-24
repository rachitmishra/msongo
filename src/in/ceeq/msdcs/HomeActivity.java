package in.ceeq.msdcs;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class HomeActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		getSupportFragmentManager().beginTransaction().replace(R.id.container, MapFragment.getInstance(), "Home")
		.commit();
	}
}
