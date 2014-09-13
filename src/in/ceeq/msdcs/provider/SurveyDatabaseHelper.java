package in.ceeq.msdcs.provider;

import in.ceeq.msdcs.provider.utils.TrackingCursorFactory;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SurveyDatabaseHelper extends SQLiteOpenHelper {

	private static final String LOG_TAG = SurveyDatabaseHelper.class.getCanonicalName();

	private static final String DB_NAME = "mobilesurveydata";

	private static final int DB_VERSION = 1;

	public SurveyDatabaseHelper(Context context) {
		super(context, DB_NAME, TrackingCursorFactory.newInstance(), DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SurveyContract.Surveys.create() + SurveyContract.Details.create()
				+ SurveyContract.Locations.create());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LOG_TAG, "Upgrading database. Existing contents will be lost. [" + oldVersion + "]->[" + newVersion + "]");
		db.execSQL(SurveyContract.Surveys.drop() + SurveyContract.Details.drop() + SurveyContract.Locations.drop());
		onCreate(db);
	}
}
