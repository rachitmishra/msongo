package in.ceeq.msdcs.fragment;

import in.ceeq.msdcs.R;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SplashFragment extends Fragment implements View.OnClickListener {

	private TextView mLabel;

	private TextView mExcelLabel;

	private TextView mPdfLabel;

	// private TextView mSheetLabel;

	public static SplashFragment newInstance() {
		return new SplashFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_splash, container, false);

		setupUi(rootView);

		return rootView;
	}

	private void setupUi(View rootView) {
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");

		mLabel = (TextView) rootView.findViewById(R.id.exportLabel);
		mLabel.setTypeface(typeFace);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		}
	}
}