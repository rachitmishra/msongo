package in.ceeq.msdcs.provider;

import in.ceeq.msdcs.utils.Utils;
import android.net.Uri;

public final class SurveyContract {

	public static final String AUTHORITY = "com.ceeq.msdcs.surveyprovider";

	public static final class Surveys extends BaseColumns {

		public static final String PATH = "surveys";

		public static final String JOIN_PATH = "surveydetails";

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

		public static final String[] DETAILED_PROJECTION = { Utils.getJoinColumnName(Surveys.PATH, _ID), USER_ID,
				DETAILS_ID, Utils.getJoinColumnName(Details.PATH, _ID),
				Utils.getJoinColumnName(Details.PATH, Details.DATE_SOWING),
				Utils.getJoinColumnName(Details.PATH, Details.DATE_SURVEY),
				Utils.getJoinColumnName(Details.PATH, Details.CROP_STAGE),
				Utils.getJoinColumnName(Details.PATH, Details.DISEASE_NAME),
				Utils.getJoinColumnName(Details.PATH, Details.DISEASE_SEVERITY_SCORE),
				Utils.getJoinColumnName(Details.PATH, Details.PEST_INFESTATION_COUNT),
				Utils.getJoinColumnName(Details.PATH, Details.PEST_NAME),
				Utils.getJoinColumnName(Details.PATH, Details.LATITUDE),
				Utils.getJoinColumnName(Details.PATH, Details.LONGITUDE), Utils.getJoinColumnName(Users.PATH, _ID),
				Utils.getJoinColumnName(Users.PATH, Users.NAME), Utils.getJoinColumnName(Users.PATH, Users.EMAIL),
				Utils.getJoinColumnName(Surveys.PATH, CREATED_ON), Utils.getJoinColumnName(Surveys.PATH, CREATED_BY),
				Utils.getJoinColumnName(Surveys.PATH, MODIFIED_ON), Utils.getJoinColumnName(Surveys.PATH, MODIFIED_BY),
				Utils.getJoinColumnName(Surveys.PATH, SYNC_ID), Utils.getJoinColumnName(Surveys.PATH, SOFT_DELETED),
				Utils.getJoinColumnName(Surveys.PATH, DELETED_ON), Utils.getJoinColumnName(Surveys.PATH, DELETED_BY) };

		public static String create() {
			return create(PATH,
					new StringBuilder(USER_ID).append(INTEGER).append(NOT_NULL).append(COMMA).append(DETAILS_ID)
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

		/**
		 * Color <br/>
		 * <b>type</b> int <br/>
		 * <b>default</b> NOT NULL
		 */
		public static final String COLOR = "color";

		public static final String CONTENT_ITEM_TYPE = Utils.getTableContentItemType(PATH);

		public static final String CONTENT_TYPE = Utils.getTableContentType(PATH);

		public static final Uri CONTENT_URI = Utils.getTableContentUri(PATH);

		public static final String[] DEFAULT_PROJECTION = { _ID, NAME, EMAIL, COLOR };

		public static String create() {
			return create(PATH,
					new StringBuilder(NAME).append(TEXT).append(NOT_NULL).append(COMMA).append(EMAIL).append(TEXT)
							.append(NOT_NULL).append(COMMA).append(COLOR).append(TEXT).append(COMMA).append(PASSWORD)
							.append(TEXT).toString());
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
		 * <b>type</b> Integer <br/>
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
		public static final String DISEASE_SEVERITY_SCORE = "disease_severity_score";

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

		public static final String CONTENT_ITEM_TYPE = Utils.getTableContentItemType(PATH);

		public static final String CONTENT_TYPE = Utils.getTableContentType(PATH);

		public static final Uri CONTENT_URI = Utils.getTableContentUri(PATH);

		public static String create() {
			String columns = new StringBuilder(DATE_SOWING).append(INTEGER).append(NOT_NULL).append(COMMA)
					.append(DATE_SURVEY).append(INTEGER).append(NOT_NULL).append(COMMA).append(CROP_STAGE)
					.append(INTEGER).append(COMMA).append(DISEASE_NAME).append(TEXT).append(COMMA)
					.append(DISEASE_SEVERITY_SCORE).append(TEXT).append(COMMA).append(PEST_NAME).append(TEXT)
					.append(COMMA).append(PEST_INFESTATION_COUNT).append(TEXT).append(COMMA).append(LATITUDE)
					.append(REAL).append(NOT_NULL).append(COMMA).append(LONGITUDE).append(REAL).append(NOT_NULL)
					.toString();
			return create(PATH, columns);
		}

		public static String drop() {
			return drop(PATH);
		}
	}

}
