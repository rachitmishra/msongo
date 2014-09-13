package in.ceeq.msdcs.provider;

import in.ceeq.msdcs.utils.Utils;
import android.net.Uri;

public final class SurveyContract {

	public static final String AUTHORITY = "com.ceeq.msdcs.surveyprovider";

	public static final class Surveys extends BaseColumns {

		public static final String PATH = "surveys";

		public static final String JOIN_PATH = "surveydetails";

		/**
		 * Location Id <br/>
		 * <b>type</b> Integer <br/>
		 * <b>default</b> NOT NULL
		 */
		public static final String LOCATION_ID = "location_id";

		/**
		 * User Id <br/>
		 * <b>type</b> Integer <br/>
		 * <b>default</b> NOT NULL
		 */
		public static final String USER_ID = "user_id";

		/**
		 * Detail Id <br/>
		 * <b>type</b> Integer <br/>
		 * <b>default</b> NOT NULL
		 */
		public static final String DETAILS_ID = "details_id";

		public static final String CONTENT_ITEM_TYPE = Utils.getTableContentItemType(PATH);

		public static final String CONTENT_TYPE = Utils.getTableContentType(PATH);

		public static final Uri CONTENT_URI = Utils.getTableContentUri(PATH);

		public static final Uri JOIN_CONTENT_URI = Utils.getTableContentUri(JOIN_PATH);

		public static final String[] DETAILED_PROJECTION = { LOCATION_ID, DETAILS_ID,
				Utils.getJoinColumnName(PATH, _ID), Utils.getJoinColumnName(Details.PATH, _ID),
				Utils.getJoinColumnName(Locations.PATH, _ID), Details.DATE_SOWING, Details.DATE_SURVEY,
				Details.DISEASE_NAME, Details.DISEASE_SCORE, Details.PEST_INFESTATION_COUNT, Details.PEST_NAME,
				Locations.LATITUDE, Locations.LONGITUDE, Locations.PROVIDER, Locations.ACCURACY, Locations.ALTITUDE,
				Locations.TIME };

		public static String create() {
			return create(PATH,
					new StringBuilder(LOCATION_ID).append(INTEGER).append(NOT_NULL).append(COMMA).append(DETAILS_ID)
							.append(INTEGER).append(NOT_NULL).toString());
		}

		public static String drop() {
			return drop(PATH);
		}
	}

	public static final class Users extends BaseColumns {

		public static final String PATH = "users";

		/**
		 * Name <br/>
		 * <b>type</b> String <br/>
		 * <b>default</b> NOT NULL
		 */
		public static final String NAME = "name";

		/**
		 * Password <br/>
		 * <b>type</b> String <br/>
		 * <b>default</b> NOT NULL
		 */
		public static final String PASSWORD = "password";

		/**
		 * Email <br/>
		 * <b>type</b> String <br/>
		 * <b>default</b> NULL
		 */
		public static final String EMAIL = "email";

		public static final String CONTENT_ITEM_TYPE = Utils.getTableContentItemType(PATH);

		public static final String CONTENT_TYPE = Utils.getTableContentType(PATH);

		public static final Uri CONTENT_URI = Utils.getTableContentUri(PATH);

		public static final String[] DEFAULT_PROJECTION = { _ID, NAME, EMAIL };

		public static String create() {
			return create(PATH, new StringBuilder(NAME).append(TEXT).append(NOT_NULL).append(COMMA).append(PASSWORD)
					.append(TEXT).append(NOT_NULL).append(COMMA).append(EMAIL).append(TEXT).append(NOT_NULL).toString());
		}

		public static String drop() {
			return drop(PATH);
		}

	}

	public static final class Locations extends BaseColumns {

		public static final String PATH = "locations";

		/**
		 * Latitude <br/>
		 * <b>type</b> Double <br/>
		 * <b>default</b> NOT NULL
		 */
		public static final String LATITUDE = "latitude";

		/**
		 * Longitude <br/>
		 * <b>type</b> Double <br/>
		 * <b>default</b> NOT NULL
		 */
		public static final String LONGITUDE = "longitude";

		/**
		 * Accuracy <br/>
		 * <b>type</b> Double <br/>
		 * <b>default</b> NULL
		 */
		public static final String ACCURACY = "accuracy";

		/**
		 * Altitude <br/>
		 * <b>type</b> Double <br/>
		 * <b>default</b> NULL
		 */
		public static final String ALTITUDE = "altitude";

		/**
		 * Location Provider Examples. Network or Precise <br/>
		 * <b>type</b> String <br/>
		 * <b>default</b> NULL
		 */
		public static final String PROVIDER = "provider";

		/**
		 * Location Time <br/>
		 * <b>type</b> Integer <br/>
		 * <b>default</b> NULL
		 */
		public static final String TIME = "location_time";

		public static final String CONTENT_ITEM_TYPE = Utils.getTableContentItemType(PATH);

		public static final String CONTENT_TYPE = Utils.getTableContentType(PATH);

		public static final Uri CONTENT_URI = Utils.getTableContentUri(PATH);

		public static String create() {
			return create(
					PATH,
					new StringBuilder(LATITUDE).append(REAL).append(NOT_NULL).append(COMMA).append(LONGITUDE)
							.append(REAL).append(NOT_NULL).append(COMMA).append(ACCURACY).append(REAL).append(COMMA)
							.append(ALTITUDE).append(REAL).append(COMMA).append(PROVIDER).append(TEXT).append(COMMA)
							.append(TIME).append(INTEGER).toString());
		}

		public static String drop() {
			return drop(PATH);
		}

	}

	public static final class Details extends BaseColumns {

		public static final String PATH = "details";

		/**
		 * Date of sowing <br/>
		 * <b>type</b> Integer <br/>
		 * <b>default</b> NOT NULL
		 */
		public static final String DATE_SOWING = "date_sowing";

		/**
		 * Date of survey <br/>
		 * <b>type</b> Integer <br/>
		 * <b>default</b> NOT NULL
		 */
		public static final String DATE_SURVEY = "date_survey";

		/**
		 * Crop Stage <br/>
		 * <b>type</b> String <br/>
		 * <b>default</b> NULL
		 */
		public static final String CROP_STAGE = "crop_stage";

		/**
		 * Disease Name <br/>
		 * <b>type</b> String <br/>
		 * <b>default</b> NULL
		 */
		public static final String DISEASE_NAME = "disease_name";

		/**
		 * Disease Score <br/>
		 * <b>type</b> String <br/>
		 * <b>default</b> NULL
		 */
		public static final String DISEASE_SCORE = "disease_score";

		/**
		 * Pest Name <br/>
		 * <b>type</b> String <br/>
		 * <b>default</b> NULL
		 */
		public static final String PEST_NAME = "pest_name";

		/**
		 * Pest Infestation Count <br/>
		 * <b>type</b> String <br/>
		 * <b>default</b> NULL
		 */
		public static final String PEST_INFESTATION_COUNT = "pest_infestation_count";

		public static final String CONTENT_ITEM_TYPE = Utils.getTableContentItemType(PATH);

		public static final String CONTENT_TYPE = Utils.getTableContentType(PATH);

		public static final Uri CONTENT_URI = Utils.getTableContentUri(PATH);

		public static String create() {
			String columns = new StringBuilder(DATE_SOWING).append(INTEGER).append(NOT_NULL).append(COMMA)
					.append(DATE_SURVEY).append(INTEGER).append(NOT_NULL).append(COMMA).append(CROP_STAGE).append(TEXT)
					.append(COMMA).append(DISEASE_NAME).append(TEXT).append(COMMA).append(DISEASE_SCORE).append(TEXT)
					.append(COMMA).append(PEST_NAME).append(TEXT).append(COMMA).append(PEST_INFESTATION_COUNT)
					.append(TEXT).toString();
			return create(PATH, columns);
		}

		public static String drop() {
			return drop(PATH);
		}
	}

}
