package in.ceeq.msongo.provider;

import android.net.Uri;

import in.ceeq.msongo.utils.Utils;

public final class SurveyContract {

	public static final String AUTHORITY = "com.ceeq.msongo.surveyprovider";

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
				Utils.getJoinColumnName(Details.PATH, Details.DATE_SURVEY),
				Utils.getJoinColumnName(Details.PATH, Details.TYPE_OF_PLACE),
                Utils.getJoinColumnName(Details.PATH, Details.OTHER_PLACE_TYPE),
                Utils.getJoinColumnName(Details.PATH, Details.NOTES),
				Utils.getJoinColumnName(Details.PATH, Details.NAME_OF_PROJECT),
				Utils.getJoinColumnName(Details.PATH, Details.STUDY_AREA_NAME),
				Utils.getJoinColumnName(Details.PATH, Details.SURVEY_PLACE_NAME),
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
		 * Date of survey <br/>
		 * <b>type</b> Integer <br/>
		 * <b>default</b> NOT NULL
		 */
		public static final String DATE_SURVEY = "date_survey";

		/**
		 * Type Of Place <br/>
		 * <b>type</b> Integer <br/>
		 * <b>default</b> NULL
		 */
		public static final String TYPE_OF_PLACE = "type_of_place";

        /**
         * Other Place Type <br/>
         * <b>type</b> String <br/>
         * <b>default</b> NULL
         */
        public static final String OTHER_PLACE_TYPE = "other_place_type";

		/**
		 * Name Of Project <br/>
		 * <b>type</b> String <br/>
		 * <b>default</b> NULL
		 */
		public static final String NAME_OF_PROJECT = "name_of_project";

		/**
		 * Study Area Name <br/>
		 * <b>type</b> String <br/>
		 * <b>default</b> NULL
		 */
		public static final String STUDY_AREA_NAME = "study_area_name";

		/**
		 * Survey Place Name <br/>
		 * <b>type</b> String <br/>
		 * <b>default</b> NULL
		 */
		public static final String SURVEY_PLACE_NAME = "survey_place_name";

        /**
         * Notes <br/>
         * <b>type</b> String <br/>
         * <b>default</b> NULL
         */
        public static final String NOTES = "notes";

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
			String columns = new StringBuilder(DATE_SURVEY).append(INTEGER).append(NOT_NULL).append(COMMA).append(TYPE_OF_PLACE)
					.append(INTEGER).append(COMMA).append(OTHER_PLACE_TYPE).append(TEXT).append(COMMA)
                    .append(NOTES).append(TEXT).append(COMMA).append(NAME_OF_PROJECT).append(TEXT).append(COMMA).append
                            (STUDY_AREA_NAME).append(TEXT)
					.append(COMMA).append(SURVEY_PLACE_NAME).append(TEXT).append(COMMA).append(LATITUDE)
					.append(REAL).append(NOT_NULL).append(COMMA).append(LONGITUDE).append(REAL).append(NOT_NULL)
					.toString();
			return create(PATH, columns);
		}

		public static String drop() {
			return drop(PATH);
		}
	}

}
