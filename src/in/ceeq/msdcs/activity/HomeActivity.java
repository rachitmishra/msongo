package in.ceeq.msdcs.activity;

import in.ceeq.msdcs.R;
import in.ceeq.msdcs.fragment.ExportFragment;
import in.ceeq.msdcs.fragment.LoginFragment;
import in.ceeq.msdcs.fragment.MapFragment;
import in.ceeq.msdcs.fragment.TimeLineFragment;
import in.ceeq.msdcs.utils.Utils;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends ActionBarActivity implements View.OnClickListener {

	private FragmentManager mFragmentManager;

	private ImageButton mAppBarToggleOut;

	private ImageButton mAppBarToggleIn;

	private LinearLayout mAppBar;

	private TextView mMapLabel;

	private TextView mTimelineLabel;

	private TextView mExportLabel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		mFragmentManager = getSupportFragmentManager();

		Typeface typeFace = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

		mAppBar = (LinearLayout) findViewById(R.id.tab_container);
		mAppBarToggleOut = (ImageButton) findViewById(R.id.toggle_out);
		mAppBarToggleIn = (ImageButton) findViewById(R.id.toggle_in);

		mAppBarToggleOut.setOnClickListener(this);
		mAppBarToggleIn.setOnClickListener(this);

		mMapLabel = (TextView) findViewById(R.id.mapLabel);
		mMapLabel.setTypeface(typeFace);
		mTimelineLabel = (TextView) findViewById(R.id.timeLineLabel);
		mTimelineLabel.setTypeface(typeFace);
		mExportLabel = (TextView) findViewById(R.id.exportLabel);
		mExportLabel.setTypeface(typeFace);

		mMapLabel.setOnClickListener(this);
		mTimelineLabel.setOnClickListener(this);
		mExportLabel.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Utils.getBooleanPrefs(this, Utils.IS_LOGGED_IN)) {
			replaceFragment(0);
		} else {
			replaceFragment(1);
		}
	}

	private boolean mExit;

	@Override
	public void onBackPressed() {
		if (mExit)
			HomeActivity.this.finish();
		else {
			Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
			mExit = true;
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					mExit = false;
				}
			}, 3 * 1000);
		}
	}

	public void replaceFragment(int id) {
		switch (id) {
			case 0:
				mFragmentManager.beginTransaction().replace(R.id.container, MapFragment.newInstance()).commit();
				break;
			case 1:
				toggleAppbar(false);
				mFragmentManager.beginTransaction().replace(R.id.container, LoginFragment.newInstance()).commit();
				break;
			case 2:
				mFragmentManager.beginTransaction().replace(R.id.container, TimeLineFragment.newInstance()).commit();
				break;
			case 3:
				mFragmentManager.beginTransaction().replace(R.id.container, ExportFragment.newInstance()).commit();
				break;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.toggle_in:
				mAppBar.setVisibility(View.GONE);
				mAppBarToggleOut.setVisibility(View.VISIBLE);
				break;
			case R.id.toggle_out:
				mAppBarToggleOut.setVisibility(View.GONE);
				mAppBar.setVisibility(View.VISIBLE);
				break;
			case R.id.mapLabel:
				replaceFragment(0);
				break;
			case R.id.timeLineLabel:
				replaceFragment(2);
				break;
			case R.id.exportLabel:
				replaceFragment(3);
				break;
		}
	}

	public void toggleAppbar(boolean show) {
		if (show) {
			mAppBar.setVisibility(View.VISIBLE);
			mAppBarToggleOut.setVisibility(View.GONE);
			return;
		}
		mAppBar.setVisibility(View.GONE);
		mAppBarToggleOut.setVisibility(View.GONE);
	}

}
