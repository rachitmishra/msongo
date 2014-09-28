package in.ceeq.msdcs.fragment;

import hirondelle.date4j.DateTime;
import in.ceeq.msdcs.R;
import in.ceeq.msdcs.activity.HomeActivity;
import in.ceeq.msdcs.provider.SurveyContract;
import in.ceeq.msdcs.utils.FloatLabeledEditText;
import in.ceeq.msdcs.utils.Utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapClickListener, OnMarkerClickListener, View.OnClickListener,
		OnDateSetListener, GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, LoaderCallbacks<Cursor> {

	public static final int SOWING_DATE_PICKER = 1;

	public static final int SURVEY_DATE_PICKER = 2;

	private static final int LOADER_ID = 1;

	private GoogleMap mMap;

	private MapView mMapView;

	private FloatLabeledEditText mSowingDate;

	private FloatLabeledEditText mSurveyDate;

	private FloatLabeledEditText mDiseaseName;

	private FloatLabeledEditText mDiseaseSeverityScore;

	private FloatLabeledEditText mPestName;

	private FloatLabeledEditText mPestInfestationCount;

	private Spinner mCropStageSpinner;

	private LinearLayout mFormLayout;

	private Button mSave;

	private Button mCancel;

	private int mCurrentDatePicker;

	private LinearLayout mMapLayout;

	private ImageButton mMapToggle;

	private LinearLayout mLocationLayout;

	private ImageButton mLocationToggle;

	private LocationRequest mLocationRequest;

	private LocationClient mLocationClient;

	private Location mCurrentLocation;

	private CameraPosition mCameraPosition;

	public static MapFragment newInstance() {
		return new MapFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		mLocationClient = new LocationClient(getActivity(), this, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_map, container, false);

		setupUi(rootView);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.crop_stages,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCropStageSpinner.setAdapter(adapter);
		mCropStageSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				parent.setTag(R.string.spinner_value, parent.getItemAtPosition(pos).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		mMapView = (MapView) rootView.findViewById(R.id.mapView);
		mMapView.onCreate(savedInstanceState);
		mMapView.onResume();
		try {
			MapsInitializer.initialize(getActivity().getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		mMap = mMapView.getMap();
		setupMap();

		mLocationClient.connect();
		setupLoader(false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}

	private void setupUi(View rootView) {
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");

		mFormLayout = (LinearLayout) rootView.findViewById(R.id.formLayout);
		mCropStageSpinner = (Spinner) rootView.findViewById(R.id.cropStage);

		mSowingDate = (FloatLabeledEditText) rootView.findViewById(R.id.sowingDate);
		mSowingDate.getEditText().setTypeface(typeFace);
		mSurveyDate = (FloatLabeledEditText) rootView.findViewById(R.id.surveyDate);
		mSurveyDate.getEditText().setTypeface(typeFace);
		mDiseaseName = (FloatLabeledEditText) rootView.findViewById(R.id.diseaseName);
		mDiseaseName.getEditText().setTypeface(typeFace);
		mDiseaseSeverityScore = (FloatLabeledEditText) rootView.findViewById(R.id.diseaseSeverity);
		mDiseaseSeverityScore.getEditText().setTypeface(typeFace);
		mPestName = (FloatLabeledEditText) rootView.findViewById(R.id.pestName);
		mPestName.getEditText().setTypeface(typeFace);
		mPestInfestationCount = (FloatLabeledEditText) rootView.findViewById(R.id.pestInfestationCount);
		mPestInfestationCount.getEditText().setTypeface(typeFace);

		((TextView) rootView.findViewById(R.id.addLabel)).setTypeface(typeFace);
		mMapLayout = (LinearLayout) rootView.findViewById(R.id.toggleMapLayout);
		mMapToggle = (ImageButton) rootView.findViewById(R.id.toggleMap);
		mLocationLayout = (LinearLayout) rootView.findViewById(R.id.currentLocationLayout);
		mLocationToggle = (ImageButton) rootView.findViewById(R.id.currentLocation);

		mSave = (Button) rootView.findViewById(R.id.save);
		mSave.setTypeface(typeFace);
		mCancel = (Button) rootView.findViewById(R.id.cancel);
		mCancel.setTypeface(typeFace);
		mSave.setOnClickListener(this);
		mCancel.setOnClickListener(this);
		mSowingDate.setOnClickListener(this);
		mSurveyDate.setOnClickListener(this);
		mMapToggle.setOnClickListener(this);
		mLocationToggle.setOnClickListener(this);
	}

	private void setupMap() {
		if (mCameraPosition != null) {
			// mMap.animateCamera(new Ca);
		}
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(false);
		mMap.getUiSettings().setMyLocationButtonEnabled(false);
		mMap.setMyLocationEnabled(true);
		mMap.setOnMapClickListener(this);
		mMap.setOnMarkerClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
		setupLoader(true);
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
		mMap.getCameraPosition();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		if (mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(this);
		}
		mLocationClient.disconnect();
		mCameraPosition = mMap.getCameraPosition();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mMapView.onLowMemory();
	}

	@Override
	public void onMapClick(LatLng point) {
		((HomeActivity) getActivity()).toggleAppbar(false);
		mMapLayout.setVisibility(View.GONE);
		mLocationLayout.setVisibility(View.GONE);
		mFormLayout.setVisibility(View.VISIBLE);

		mFormLayout.setTag(R.string.tag_latitude, point.latitude);
		mFormLayout.setTag(R.string.tag_longitude, point.longitude);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return false;
	}

	@Override
	public void onClick(View v) {
		Calendar c = new GregorianCalendar();

		switch (v.getId()) {
			case R.id.toggleMap:
				toggleMapState();
				break;
			case R.id.currentLocation:
				setMapToCurrentLocation();
				break;
			case R.id.cancel:
				mFormLayout.setVisibility(View.GONE);
				mMapLayout.setVisibility(View.VISIBLE);
				mLocationLayout.setVisibility(View.VISIBLE);
				((HomeActivity) getActivity()).toggleAppbar(true);
				Utils.hideKeyboard(getActivity());
				break;

			case R.id.save:
				mFormLayout.setVisibility(View.GONE);
				mMapLayout.setVisibility(View.VISIBLE);
				mLocationLayout.setVisibility(View.VISIBLE);
				((HomeActivity) getActivity()).toggleAppbar(true);
				Utils.hideKeyboard(getActivity());
				saveSurveyData();
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

	private void saveSurveyData() {
		ContentValues surveyValues = new ContentValues();

		if (!validate()) {
			Toast.makeText(getActivity(), "Entered values are not correct.", Toast.LENGTH_SHORT).show();
			return;
		}

		surveyValues.put(
				SurveyContract.Details.DATE_SOWING,
				DateTime.forDateOnly((int) mSowingDate.getTag(R.string.tag_year),
						(int) mSowingDate.getTag(R.string.tag_month), (int) mSowingDate.getTag(R.string.tag_day))
						.getMilliseconds(TimeZone.getDefault()));

		DateTime surveyDate = DateTime.forDateOnly((int) mSurveyDate.getTag(R.string.tag_year),
				(int) mSurveyDate.getTag(R.string.tag_month), (int) mSurveyDate.getTag(R.string.tag_day));
		surveyValues.put(SurveyContract.Details.DATE_SURVEY, surveyDate.getMilliseconds(TimeZone.getDefault()));
		surveyValues.put(SurveyContract.Details.DISEASE_NAME, mDiseaseName.getText().toString());
		surveyValues.put(SurveyContract.Details.DISEASE_SEVERITY_SCORE, mDiseaseSeverityScore.getText().toString());
		surveyValues.put(SurveyContract.Details.PEST_NAME, mDiseaseName.getText().toString());
		surveyValues.put(SurveyContract.Details.PEST_INFESTATION_COUNT, mDiseaseSeverityScore.getText().toString());
		surveyValues.put(SurveyContract.Details.CROP_STAGE, mCropStageSpinner.getSelectedItemPosition());
		double latitude = (double) mFormLayout.getTag(R.string.tag_latitude);
		double longitude = (double) mFormLayout.getTag(R.string.tag_longitude);
		surveyValues.put(SurveyContract.Details.LATITUDE, latitude);
		surveyValues.put(SurveyContract.Details.LONGITUDE, longitude);
		surveyValues.put(SurveyContract.Surveys.USER_ID, Utils.getCurrentUser(getActivity()).mId + "");
		mMap.addMarker(new MarkerOptions().draggable(true).position(new LatLng(latitude, longitude))
				.title(surveyDate.format("DD MM YY")));
		NewSurveyQuery.newInstance(getActivity().getContentResolver(), getActivity()).startInsert(0, null,
				SurveyContract.Surveys.CONTENT_URI, surveyValues);
	}

	private boolean validate() {
		boolean valid = true;

		if (mSowingDate.getText().length() == 0 || TextUtils.isEmpty(mSowingDate.getText().toString())) {
			valid = false;
			mSowingDate.setError("Sowing date cannot be empty.");
		}

		if (mSurveyDate.getText().length() == 0 || TextUtils.isEmpty(mSurveyDate.getText().toString())) {
			valid = false;
			mSowingDate.setError("Sowing date cannot be empty.");
		}

		return valid;

	}

	private static class NewSurveyQuery extends AsyncQueryHandler {

		private Context mContext;

		public static NewSurveyQuery newInstance(ContentResolver contentResolver, Context context) {
			return new NewSurveyQuery(contentResolver, context);
		}

		public NewSurveyQuery(ContentResolver contentResolver, Context context) {
			super(contentResolver);
			this.mContext = context;
		}

		@Override
		protected void onInsertComplete(int token, Object cookie, Uri uri) {
			Toast.makeText(mContext, "Survey saved successfully.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		switch (mCurrentDatePicker) {
			case SOWING_DATE_PICKER:
				mSowingDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
				mSowingDate.setTag(R.string.tag_year, year);
				mSowingDate.setTag(R.string.tag_month, monthOfYear);
				mSowingDate.setTag(R.string.tag_day, dayOfMonth);
				break;
			case SURVEY_DATE_PICKER:
				mSurveyDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
				mSurveyDate.setTag(R.string.tag_year, year);
				mSurveyDate.setTag(R.string.tag_month, monthOfYear);
				mSurveyDate.setTag(R.string.tag_day, dayOfMonth);
				break;
		}
	}

	@Override
	public void onLocationChanged(Location newLocation) {
		mCurrentLocation = newLocation;
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {

	}

	@Override
	public void onConnected(Bundle arg0) {
		mCurrentLocation = mLocationClient.getLastLocation();
		setMapToCurrentLocation();
	}

	@Override
	public void onDisconnected() {
	}

	private void toggleMapState() {
		switch (mMap.getMapType()) {
			case GoogleMap.MAP_TYPE_NORMAL:
				mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				break;
			case GoogleMap.MAP_TYPE_TERRAIN:
				mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
			case GoogleMap.MAP_TYPE_SATELLITE:
				mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			case GoogleMap.MAP_TYPE_HYBRID:
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
		}
	}

	private void setMapToCurrentLocation() {
		if (mCurrentLocation != null) {
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(),
					mCurrentLocation.getLongitude()), 15));
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new CursorLoader(getActivity(), SurveyContract.Surveys.JOIN_CONTENT_URI,
				SurveyContract.Surveys.DETAILED_PROJECTION, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (cursor.moveToNext()) {
				mMap.addMarker(new MarkerOptions().position(new LatLng(cursor.getDouble(cursor
						.getColumnIndex(SurveyContract.Details.LATITUDE)), cursor.getDouble(cursor
						.getColumnIndex(SurveyContract.Details.LONGITUDE)))));
			}
		}
	}

	public void setupLoader(boolean mReset) {
		if (mReset) {
			getLoaderManager().restartLoader(LOADER_ID, null, this);
			return;
		}
		getLoaderManager().initLoader(LOADER_ID, null, this);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	}

	private class SpinnerAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		private String[] values;

		public SpinnerAdapter(Context context, String[] values) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return values.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ListContent holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.spinner_item, null);
				holder = new ListContent();
				holder.name = (TextView) convertView.findViewById(R.id.textView1);
				convertView.setTag(holder);
			} else {
				holder = (ListContent) convertView.getTag();
			}

			Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
			holder.name.setTypeface(typeFace);
			holder.name.setText("" + values[position]);
			return convertView;
		}
	}

	private static class ListContent {

		TextView name;

	}
}