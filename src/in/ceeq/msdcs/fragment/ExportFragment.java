package in.ceeq.msdcs.fragment;

import in.ceeq.msdcs.R;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExportFragment extends Fragment implements View.OnClickListener {

	private TextView mExportLabel;

	private TextView mExcelLabel;

	private TextView mPdfLabel;

	// private TextView mSheetLabel;

	public static ExportFragment newInstance() {
		return new ExportFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_export, container, false);

		setupUi(rootView);

		// instructions to create the pdf file content

		return rootView;
	}

	private void setupUi(View rootView) {
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");

		mExportLabel = (TextView) rootView.findViewById(R.id.exportLabel);
		mExportLabel.setTypeface(typeFace);
		mExcelLabel = (TextView) rootView.findViewById(R.id.exportExcel);
		mExcelLabel.setTypeface(typeFace);
		mPdfLabel = (TextView) rootView.findViewById(R.id.exportPdf);
		mPdfLabel.setTypeface(typeFace);
		// mSheetLabel = (TextView) rootView.findViewById(R.id.exportSheet);
		// mSheetLabel.setTypeface(typeFace);

		mExcelLabel.setOnClickListener(this);
		mPdfLabel.setOnClickListener(this);
		// mSheetLabel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		}
	}
}