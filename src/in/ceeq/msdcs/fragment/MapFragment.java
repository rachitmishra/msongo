package in.ceeq.msdcs.fragment;

import in.ceeq.msdcs.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MapFragment extends Fragment implements OnMapClickListener, OnMarkerClickListener, View.OnClickListener,
		OnDateSetListener {

	public static final int SOWING_DATE_PICKER = 1;

	public static final int SURVEY_DATE_PICKER = 2;

	private GoogleMap map;

	private MapView mapView;

	private EditText sowingDate;

	private EditText surveyDate;

	private EditText diseaseName;

	private EditText diseaseSeverityScore;

	private EditText pestName;

	private EditText pestInfestationCount;

	private Spinner cropStageSpinner;

	private LinearLayout appBar;

	private LinearLayout formLayout;

	private LinearLayout appBarToggleLayout;

	private Button saveData;

	private Button cancel;

	private ImageButton appBarToggleOut;

	private ImageButton appBarToggleIn;

	private int mCurrentDatePicker;

	public static MapFragment getInstance() {
		return new MapFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_map, container, false);

		appBar = (LinearLayout) view.findViewById(R.id.appBar);
		appBarToggleOut = (ImageButton) view.findViewById(R.id.appBarToggleOut);
		appBarToggleIn = (ImageButton) view.findViewById(R.id.appBarToggleIn);
		formLayout = (LinearLayout) view.findViewById(R.id.formLayout);
		appBarToggleLayout = (LinearLayout) view.findViewById(R.id.appBarToggleLayout);
		cropStageSpinner = (Spinner) view.findViewById(R.id.cropStage);
		sowingDate = (EditText) view.findViewById(R.id.sowingDate);
		surveyDate = (EditText) view.findViewById(R.id.surveyDate);
		diseaseName = (EditText) view.findViewById(R.id.diseaseName);
		diseaseSeverityScore = (EditText) view.findViewById(R.id.diseaseSeverity);
		pestName = (EditText) view.findViewById(R.id.pestName);
		pestInfestationCount = (EditText) view.findViewById(R.id.pestInfestationCount);

		saveData = (Button) view.findViewById(R.id.save);
		cancel = (Button) view.findViewById(R.id.cancel);

		saveData.setOnClickListener(this);
		cancel.setOnClickListener(this);
		appBarToggleOut.setOnClickListener(this);
		appBarToggleIn.setOnClickListener(this);
		sowingDate.setOnClickListener(this);
		surveyDate.setOnClickListener(this);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.crop_stages,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cropStageSpinner.setAdapter(adapter);
		cropStageSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				parent.setTag(R.string.spinner_value, parent.getItemAtPosition(pos).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		mapView = (MapView) view.findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);
		mapView.onResume();
		try {
			MapsInitializer.initialize(getActivity().getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		map = mapView.getMap();
		setupMap();

		return view;
	}

	private void setupMap() {
		map.getUiSettings().setCompassEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);
		map.setOnMapClickListener(this);
		map.setOnMarkerClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override
	public void onMapClick(LatLng point) {
		appBar.setVisibility(View.GONE);
		appBarToggleLayout.setVisibility(View.GONE);
		formLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return false;
	}

	@Override
	public void onClick(View v) {
		Calendar c = new GregorianCalendar();

		switch (v.getId()) {
			case R.id.cancel:
				formLayout.setVisibility(View.GONE);
				appBarToggleLayout.setVisibility(View.VISIBLE);
				break;

			case R.id.save:
				formLayout.setVisibility(View.GONE);
				appBarToggleLayout.setVisibility(View.VISIBLE);
				break;
			case R.id.appBarToggleIn:
				appBar.setVisibility(View.GONE);
				appBarToggleLayout.setVisibility(View.VISIBLE);
				break;
			case R.id.appBarToggleOut:
				appBarToggleLayout.setVisibility(View.GONE);
				appBar.setVisibility(View.VISIBLE);
				break;
			case R.id.sowingDate:
				mCurrentDatePicker = SOWING_DATE_PICKER;
				new DatePickerDialog(getActivity(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH)).show();
				break;
			case R.id.surveyDate:
				mCurrentDatePicker = SURVEY_DATE_PICKER;
				new DatePickerDialog(getActivity(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH)).show();
				break;
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		switch (mCurrentDatePicker) {
			case SOWING_DATE_PICKER:
				sowingDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
				break;
			case SURVEY_DATE_PICKER:
				surveyDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
				break;
		}
	}
}