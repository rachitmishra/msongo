package in.ceeq.msdcs.provider;

import hirondelle.date4j.DateTime;
import in.ceeq.msdcs.provider.SurveyContract.Details;
import in.ceeq.msdcs.provider.SurveyContract.Locations;
import in.ceeq.msdcs.utils.Utils;

import java.util.TimeZone;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class SurveyProvider extends ContentProvider {

	private SurveyDatabaseHelper mDatabaseHelper;

	private SQLiteDatabase mDatabase;

	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	private static final int LOCATIONS_MATCH = 7001;

	private static final int SURVEYS_MATCH = 7002;

	private static final int DETAILS_MATCH = 7003;

	private static final int USERS_MATCH = 7004;

	private static final int SURVEY_DETAILS_MATCH = 7005;

	private static final String SELECT = " SELECT ";

	private static final String AND = " AND ";

	private static final String OR = " OR ";

	private static final String FROM = " FROM ";

	private static final String WHERE = " WHERE ";

	private static final String LEFT_JOIN = " LEFT JOIN ";

	private static final String ON = " ON ";

	private static final String ORDER_BY = " ORDER BY ";

	private static final String HAVING = " HAVING ";

	private static final String COMMA_SEPARATOR = " , ";

	private static final String EQUAL_TO = " = ";

	private static final String GREATER_THAN = " > ";

	private static final String LESS_THAN = " < ";

	private static final String GREATER_THAN_EQUAL_TO = " >= ";

	private static final String LESS_THAN_EQUAL_TO = " =< ";

	private static final String IS_NULL = " IS NULL ";

	private static final String IS_NOT_NULL = " IS NOT NULL ";

	private static final String NOT_EQUAL_TO = " != ";

	private static final String LIKE = " LIKE ";

	public SurveyProvider() {
	}

	@Override
	public boolean onCreate() {
		mDatabaseHelper = new SurveyDatabaseHelper(getContext());

		sUriMatcher.addURI(SurveyContract.AUTHORITY, SurveyContract.Surveys.PATH, SURVEYS_MATCH);
		sUriMatcher.addURI(SurveyContract.AUTHORITY, SurveyContract.Locations.PATH, LOCATIONS_MATCH);
		sUriMatcher.addURI(SurveyContract.AUTHORITY, SurveyContract.Details.PATH, DETAILS_MATCH);
		sUriMatcher.addURI(SurveyContract.AUTHORITY, SurveyContract.Surveys.JOIN_PATH, SURVEY_DETAILS_MATCH);
		sUriMatcher.addURI(SurveyContract.AUTHORITY, SurveyContract.Users.PATH, USERS_MATCH);

		return (mDatabaseHelper == null) ? false : true;
	}

	@Override
	public String getType(Uri uri) {

		final int match = sUriMatcher.match(uri);
		switch (match) {
			case SURVEYS_MATCH:
				return SurveyContract.Surveys.CONTENT_TYPE;
			case LOCATIONS_MATCH:
				return SurveyContract.Locations.CONTENT_TYPE;
			case USERS_MATCH:
				return SurveyContract.Users.CONTENT_TYPE;
			case DETAILS_MATCH:
				return SurveyContract.Details.CONTENT_TYPE;
			case SURVEY_DETAILS_MATCH:
				return SurveyContract.Details.CONTENT_TYPE;
			default:
				throw new IllegalArgumentException("NO FOUND: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder mQueryBuilder = new SQLiteQueryBuilder();
		mDatabase = mDatabaseHelper.getReadableDatabase();

		StringBuilder mQuery;
		Cursor cursor;

		int uriType = sUriMatcher.match(uri);
		switch (uriType) {
			case SURVEY_DETAILS_MATCH:
				mQuery = new StringBuilder(SELECT);
				mQuery.append(Utils.getStringFromArray(projection, COMMA_SEPARATOR));
				mQuery.append(FROM);
				mQuery.append(SurveyContract.Surveys.PATH);
				mQuery.append(LEFT_JOIN);
				mQuery.append(Locations.PATH);
				mQuery.append(ON);
				mQuery.append(SurveyContract.Surveys.LOCATION_ID + EQUAL_TO + SurveyContract.Locations._ID);
				mQuery.append(LEFT_JOIN);
				mQuery.append(Details.PATH);
				mQuery.append(ON);
				mQuery.append(SurveyContract.Surveys.DETAILS_ID + EQUAL_TO + SurveyContract.Details._ID);

				if (!TextUtils.isEmpty(selection)) {
					mQuery.append(WHERE);
					mQuery.append(selection);
				}
				cursor = mDatabase.rawQuery(mQuery.toString(), selectionArgs);
				cursor.setNotificationUri(getContext().getContentResolver(), uri);
				return cursor;
			case USERS_MATCH:
				mQueryBuilder.setTables(SurveyContract.Users.PATH);
				cursor = mQueryBuilder.query(mDatabase, projection, selection, selectionArgs, sortOrder, null, null);
				cursor.setNotificationUri(getContext().getContentResolver(), uri);
				return cursor;
			default:
				throw new IllegalArgumentException("NOT FOUND: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		mDatabase = mDatabaseHelper.getWritableDatabase();

		Uri result = null;

		int uriType = sUriMatcher.match(uri);
		switch (uriType) {
			case SURVEYS_MATCH:

				ContentValues locationValues = new ContentValues();

				locationValues.put(SurveyContract.Locations.LATITUDE,
						values.getAsDouble(SurveyContract.Locations.LATITUDE));
				locationValues.put(SurveyContract.Locations.LONGITUDE,
						values.getAsDouble(SurveyContract.Locations.LONGITUDE));
				locationValues.put(SurveyContract.Locations.PROVIDER,
						values.getAsString(SurveyContract.Locations.PROVIDER));
				locationValues.put(SurveyContract.Locations.ACCURACY,
						values.getAsDouble(SurveyContract.Locations.ACCURACY));
				locationValues.put(SurveyContract.Locations.ALTITUDE,
						values.getAsDouble(SurveyContract.Locations.ALTITUDE));
				locationValues.put(SurveyContract.Locations.TIME, values.getAsInteger(SurveyContract.Locations.TIME));

				long locationRowId = mDatabase.insert(SurveyContract.Locations.PATH, null, locationValues);

				ContentValues detailValues = new ContentValues();

				detailValues.put(SurveyContract.Details.DATE_SOWING,
						values.getAsInteger(SurveyContract.Details.DATE_SOWING));
				detailValues.put(SurveyContract.Details.DATE_SURVEY,
						values.getAsInteger(SurveyContract.Details.DATE_SURVEY));
				detailValues.put(SurveyContract.Details.CROP_STAGE,
						values.getAsString(SurveyContract.Details.CROP_STAGE));
				detailValues.put(SurveyContract.Details.DISEASE_NAME,
						values.getAsString(SurveyContract.Details.DISEASE_NAME));
				detailValues.put(SurveyContract.Details.DISEASE_SCORE,
						values.getAsString(SurveyContract.Details.DISEASE_SCORE));
				detailValues
						.put(SurveyContract.Details.PEST_NAME, values.getAsString(SurveyContract.Details.PEST_NAME));
				detailValues.put(SurveyContract.Details.PEST_INFESTATION_COUNT,
						values.getAsString(SurveyContract.Details.PEST_INFESTATION_COUNT));
				detailValues.put(SurveyContract.Surveys.CREATED_ON, DateTime.now(TimeZone.getDefault())
						.getMilliseconds(TimeZone.getDefault()));
				detailValues.put(SurveyContract.Surveys.CREATED_BY, 0);

				long detailRowId = mDatabase.insert(SurveyContract.Details.PATH, null, detailValues);

				ContentValues surveyContentValues = new ContentValues();

				surveyContentValues.put(SurveyContract.Surveys.USER_ID, Utils.getCurrentUser(getContext()).mId);
				surveyContentValues.put(SurveyContract.Surveys.LOCATION_ID, locationRowId);
				surveyContentValues.put(SurveyContract.Surveys.DETAILS_ID, detailRowId);
				surveyContentValues.put(SurveyContract.Surveys.CREATED_ON, DateTime.now(TimeZone.getDefault())
						.getMilliseconds(TimeZone.getDefault()));
				surveyContentValues.put(SurveyContract.Surveys.CREATED_BY,
						values.getAsString(SurveyContract.Details.PEST_NAME));

				long surveyRowId = mDatabase.insert(SurveyContract.Details.PATH, null, surveyContentValues);

				if (surveyRowId > 0) {
					result = ContentUris.withAppendedId(SurveyContract.Surveys.CONTENT_URI, surveyRowId);
					getContext().getContentResolver().notifyChange(result, null);
				}
			case USERS_MATCH:
				long userRowId = mDatabase.insert(SurveyContract.Details.PATH, null, values);

				if (userRowId > 0) {
					result = ContentUris.withAppendedId(SurveyContract.Users.CONTENT_URI, userRowId);
					getContext().getContentResolver().notifyChange(result, null);
				}
		}

		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
