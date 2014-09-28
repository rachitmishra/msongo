package in.ceeq.msdcs.utils;

import hirondelle.date4j.DateTime;
import in.ceeq.msdcs.provider.SurveyContract;
import in.ceeq.msdcs.provider.entity.User;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TimeZone;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class Utils {

	public static void hideKeyboard(Activity activity) {
		try {

			InputMethodManager inputManager = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**************************************
	 ***************** User ****************
	 **************************************/

	public static User getCurrentUser(Context context) {
		User user = new User();
		user.mId = Utils.getLongPrefs(context, CURRENT_USER_ID);
		user.mName = Utils.getStringPrefs(context, CURRENT_USER_NAME);
		user.mEmail = Utils.getStringPrefs(context, CURRENT_USER_EMAIL);

		return user;
	}

	/**************************************
	 ************** Provider ***************
	 **************************************/

	public static String getStringFromArray(String[] strings, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (String string : strings) {
			sb.append(string).append(delimiter);
		}
		if (strings.length != 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static Uri getTableContentUri(String mTableName) {
		return Uri.parse("content://" + SurveyContract.AUTHORITY + "/" + mTableName);
	}

	public static String getTableContentItemType(String mTableName) {
		return new StringBuilder(ContentResolver.CURSOR_ITEM_BASE_TYPE).append("/").append(mTableName).toString();
	}

	public static String getTableContentType(String mTableName) {
		return new StringBuilder(ContentResolver.CURSOR_DIR_BASE_TYPE).append("/").append(mTableName).toString();
	}

	public static String getJoinColumnName(String mTableName, String mColumnName) {
		return new StringBuilder(mTableName).append(".").append(mColumnName).toString();
	}

	public static String getColumnName(String mTableName, String mColumnName, String mAliasName) {
		return new StringBuilder(mTableName).append(".").append(mColumnName).append(" AS ").append(mAliasName)
				.toString();
	}

	/**************************************
	 ***************** Logs ***************
	 **************************************/

	/**
	 * Log debug message.
	 * 
	 * @param message
	 */
	public static void d(String message) {
		Log.d("@ceeq", message);
	}

	/**
	 * Log warning message.
	 * 
	 * @param message
	 */
	public static void w(String message) {
		Log.w("@ceeq", message);
	}

	/**
	 * Log informative message.
	 * 
	 * @param message
	 */
	public static void i(String message) {
		Log.i("@ceeq", message);
	}

	/**************************************
	 ***************** Phone ***************
	 **************************************/

	public static final int GPS = 1;

	public static final int INTERNET = 2;

	public static final int PLAY_SERVICES = 3;

	public static final int EXTERNAL_STORAGE = 4;

	/**
	 * 
	 * Check phone feature enabled
	 * 
	 * @param featureType
	 * @param context
	 * @return
	 */
	public static boolean enabled(int featureType, Context context) {
		boolean data = false;
		switch (featureType) {
			case GPS:
				data = isGpsEnabled(context);
				break;
			case INTERNET:
				data = isInternetEnabled(context);
				break;
			case PLAY_SERVICES:
				data = isPlayServiceInstalled(context);
				break;
			case EXTERNAL_STORAGE:
				data = isExternalStorageEnabled(context);
				break;
			default:
				data = false;
				break;

		}
		return data;
	}

	/**
	 * 
	 * Is GPS enabled
	 * 
	 * @param context
	 * 
	 * @return status
	 */
	private static boolean isGpsEnabled(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return true;
		return false;
	}

	private static boolean isPlayServiceInstalled(Context context) {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if (ConnectionResult.SUCCESS == resultCode)
			return true;
		return false;
	}

	private static boolean isExternalStorageEnabled(Context context) {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return mExternalStorageAvailable && mExternalStorageWriteable;
	}

	private static boolean isInternetEnabled(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**************************************
	 ************** Preferences ***********
	 **************************************/

	public static final String CURRENT_USER_ID = "current_user_id";

	public static final String CURRENT_USER_NAME = "current_user_name";

	public static final String CURRENT_USER_EMAIL = "current_user_email";

	public static final String IS_LOGGED_IN = "is_logged_in";

	public static Boolean getBooleanPrefs(Context ctx, String key) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(key, false);
	}

	public static void setBooleanPrefs(Context ctx, String key, Boolean value) {
		PreferenceManager.getDefaultSharedPreferences(ctx).edit().putBoolean(key, value).commit();
	}

	public static String getStringPrefs(Context ctx, String key) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(key, "");
	}

	public static void setStringPrefs(Context ctx, String key, String value) {
		PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(key, value).commit();
	}

	public static int getIntPrefs(Context ctx, String key) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getInt(key, 0);
	}

	public static void setIntPrefs(Context ctx, String key, int value) {
		PreferenceManager.getDefaultSharedPreferences(ctx).edit().putInt(key, value).commit();
	}

	public static long getLongPrefs(Context ctx, String key) {
		return PreferenceManager.getDefaultSharedPreferences(ctx).getLong(key, 0);
	}

	public static void setLongPrefs(Context ctx, String key, long value) {
		PreferenceManager.getDefaultSharedPreferences(ctx).edit().putLong(key, value).commit();
	}

	public static void clearPrefs(Context ctx) {
		PreferenceManager.getDefaultSharedPreferences(ctx).edit().clear().commit();
	}

	/**************************************
	 ***************** Files ***************
	 **************************************/

	public static final String APP_PATH = "/msdcs";

	public static File createFile(String path, String type, Context ctx) throws IOException,
			ExternalStorageNotFoundException {
		if (!enabled(EXTERNAL_STORAGE, ctx)) {
			throw new ExternalStorageNotFoundException();
		}

		File storageLocation = new File(Environment.getExternalStorageDirectory(), path);

		if (!storageLocation.exists()) {
			storageLocation.mkdirs();
		}

		File file = new File(storageLocation, getFileName(type));
		file.createNewFile();
		return file;
	}

	public static String writeFile(String text, String type, Context ctx) throws IOException,
			ExternalStorageNotFoundException {
		FileOutputStream fos = new FileOutputStream(createFile(APP_PATH, type, ctx));
		DataOutputStream out = new DataOutputStream(fos);
		out.writeBytes(text);
		out.close();
		return getFileName(type);
	}

	public static String getFileName(String type) {
		if (type.equals("cam"))
			return type + "_" + getDate() + ".jpg";
		else
			return type + "_" + getDate() + ".xml";
	}

	public static String getDate() {
		return DateTime.now(TimeZone.getDefault()).format("DD-MM-YY-hh-mm-ss").toString();
	}

}
