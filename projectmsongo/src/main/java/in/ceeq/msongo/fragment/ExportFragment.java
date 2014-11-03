package in.ceeq.msongo.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.ceeq.msongo.R;
import in.ceeq.msongo.service.ExportService;

public class ExportFragment extends Fragment implements View.OnClickListener {

	private TextView mExportLabel;

	private TextView mExcelLabel;

	private TextView mPdfLabel;

	private TextView mTextLabel;

	private int mExportFileType;

	public static final int EXPORT_PDF = 101;

	public static final int EXPORT_EXCEL = 102;

	public static final int EXPORT_TEXT = 103;

	public static final String EXPORT_FILE_NAME = "export_file_name";

	public static final String EXPORT_FILE_TYPE = "export_file_type";

	// private TextView mSheetLabel;

	private EditText mFileName;

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
		mTextLabel = (TextView) rootView.findViewById(R.id.exportText);
		mTextLabel.setTypeface(typeFace);
		// mSheetLabel = (TextView) rootView.findViewById(R.id.exportSheet);
		// mSheetLabel.setTypeface(typeFace);

		mExcelLabel.setOnClickListener(this);
		mPdfLabel.setOnClickListener(this);
		mTextLabel.setOnClickListener(this);
		// mSheetLabel.setOnClickListener(this);
	}

	private AlertDialog mAlertDialog;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.exportPdf:
			mExportFileType = EXPORT_PDF;
			showExportDialog();
			break;
		case R.id.exportExcel:
			mExportFileType = EXPORT_EXCEL;
			showExportDialog();
			break;
		case R.id.exportText:
			mExportFileType = EXPORT_TEXT;
			showExportDialog();
			break;
		case R.id.cancel:
			mAlertDialog.dismiss();
			break;
		case R.id.save:
			mAlertDialog.dismiss();
			Intent exportIntent = new Intent(getActivity(), ExportService.class);
			exportIntent.putExtra(EXPORT_FILE_NAME, mFileName.getText().toString());
			exportIntent.putExtra(EXPORT_FILE_TYPE, mExportFileType);
			getActivity().startService(exportIntent);
			break;
		}
	}

	public void showExportDialog() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
		View exportView = getActivity().getLayoutInflater().inflate(R.layout.dialog_file_name, null);
		mFileName = (EditText) exportView.findViewById(R.id.fileName);
		LinearLayout mCancel = (LinearLayout) exportView.findViewById(R.id.cancel);
		mCancel.setOnClickListener(this);
		LinearLayout mSave = (LinearLayout) exportView.findViewById(R.id.save);
		mSave.setOnClickListener(this);
		mAlertDialog = alertBuilder.setView(exportView).create();
		mAlertDialog.show();
	}
}