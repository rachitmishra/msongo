package in.ceeq.msdcs.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.ceeq.msdcs.R;
import in.ceeq.msdcs.fragment.ExportFragment;
import in.ceeq.msdcs.fragment.LoginFragment;
import in.ceeq.msdcs.fragment.MapFragment;
import in.ceeq.msdcs.fragment.TimeLineFragment;
import in.ceeq.msdcs.utils.Utils;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {

	public static final int LOGIN_FRAGMENT = 1;

	public static final int MAP_FRAGMENT = 2;

	public static final int TIMELINE_FRAGMENT_ID = 3;

	public static final int EXPORT_FRAGMENT_ID = 4;

    public static final String CURRENT_FRAGMENT_ID = "current_fragment_id";

	public int mCurrentFragmentId = MAP_FRAGMENT;

	private FragmentManager mFragmentManager;

	private ImageButton mAppBarToggleOut;

	private ImageButton mAppBarToggleIn;

	private ImageButton mSettingsButton;

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
		mSettingsButton = (ImageButton) findViewById(R.id.settings);

		mAppBarToggleOut.setOnClickListener(this);
		mAppBarToggleIn.setOnClickListener(this);
		mSettingsButton.setOnClickListener(this);

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
			replaceFragment(mCurrentFragmentId);
		} else {
			replaceFragment(LOGIN_FRAGMENT);
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

    @Override
    public void onSaveInstanceState (Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(CURRENT_FRAGMENT_ID, mCurrentFragmentId);
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentFragmentId = savedInstanceState.getInt(CURRENT_FRAGMENT_ID);
    }

    public void replaceFragment(final int fragmentId) {
		switch (fragmentId) {
		case MAP_FRAGMENT:
			mCurrentFragmentId = MAP_FRAGMENT;
			mFragmentManager.beginTransaction().replace(R.id.container, MapFragment.newInstance())
					.commitAllowingStateLoss();
			break;
		case LOGIN_FRAGMENT:
			toggleAppbar(false);
			mFragmentManager.beginTransaction().replace(R.id.container, LoginFragment.newInstance())
					.commitAllowingStateLoss();
			break;
		case TIMELINE_FRAGMENT_ID:
			mCurrentFragmentId = TIMELINE_FRAGMENT_ID;
			mFragmentManager.beginTransaction().replace(R.id.container, TimeLineFragment.newInstance())
					.commitAllowingStateLoss();
			break;
		case EXPORT_FRAGMENT_ID:
			mCurrentFragmentId = EXPORT_FRAGMENT_ID;
			mFragmentManager.beginTransaction().replace(R.id.container, ExportFragment.newInstance())
					.commitAllowingStateLoss();
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
		case R.id.settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		case R.id.mapLabel:
			if (mCurrentFragmentId != MAP_FRAGMENT) {
				replaceFragment(MAP_FRAGMENT);
			}
			break;
		case R.id.timeLineLabel:
			if (mCurrentFragmentId != TIMELINE_FRAGMENT_ID) {
				replaceFragment(TIMELINE_FRAGMENT_ID);
			}
			break;
		case R.id.exportLabel:
			if (mCurrentFragmentId != EXPORT_FRAGMENT_ID) {
				replaceFragment(EXPORT_FRAGMENT_ID);
			}
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

	public int getCurrentFragmentId() {
		return this.mCurrentFragmentId;
	}
}
