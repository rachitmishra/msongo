package in.ceeq.msdcs.provider;

import in.ceeq.msdcs.provider.utils.TrackingCursorFactory;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SurveyDatabaseHelper extends SQLiteOpenHelper {

	public static final String SEMI_COLON = ";";

	private static final String LOG_TAG = SurveyDatabaseHelper.class.getCanonicalName();

	private static final String DB_NAME = "mobilesurveydata";

	private static final int DB_VERSION = 1;

	public SurveyDatabaseHelper(Context context) {
		super(context, DB_NAME, TrackingCursorFactory.newInstance(), DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SurveyContract.Users.create());
		db.execSQL(SurveyContract.Details.create());
		db.execSQL(SurveyContract.Surveys.create());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LOG_TAG, "Upgrading database. Existing contents will be lost. [" + oldVersion + "]->[" + newVersion + "]");
		db.execSQL(SurveyContract.Surveys.drop());
		db.execSQL(SurveyContract.Details.drop());
		db.execSQL(SurveyContract.Users.drop());
		onCreate(db);
	}
}
