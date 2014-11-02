package in.ceeq.msdcs.provider;

public class BaseColumns {

	/**
	 * _Id <br/>
	 * <b>type</b> Integer PRIMARY KEY<br/>
	 * <b>default</b> NOT NULL
	 */
	public static final String _ID = "_id";

	/**
	 * Sync Id <br/>
	 * <b>type</b> Integer <br/>
	 * <b>default</b> NULL
	 */
	public static final String SYNC_ID = "sync_id";

	/**
	 * Created On <br/>
	 * <b>type</b> Integer <br/>
	 * <b>default</b> NULL
	 */
	public static final String CREATED_ON = "created_on";

	/**
	 * Created By <br/>
	 * <b>type</b> Integer <br/>
	 * <b>default</b> NULL
	 */
	public static final String CREATED_BY = "created_by";

	/**
	 * Modified On <br/>
	 * <b>type</b> Integer <br/>
	 * <b>default</b> NULL
	 */
	public static final String MODIFIED_ON = "modified_on";

	/**
	 * Modified By <br/>
	 * <b>type</b> Integer <br/>
	 * <b>default</b> NULL
	 */
	public static final String MODIFIED_BY = "modified_by";

	/**
	 * Deleted On <br/>
	 * <b>type</b> Integer <br/>
	 * <b>default</b> NULL
	 */
	public static final String DELETED_ON = "deleted_on";

	/**
	 * Deleted By <br/>
	 * <b>type</b> Integer <br/>
	 * <b>default</b> NULL
	 */
	public static final String DELETED_BY = "deleted_by";

	/**
	 * Soft Deleted <br/>
	 * <b>type</b> Integer <br/>
	 * <b>default</b> NULL
	 */
	public static final String SOFT_DELETED = "soft_deleted";

	public static final String CREATE_TABLE = "create table ";

	public static final String COMMA = ", ";

	private static final String DROP_TABLE = "drop table if exists ";

	protected static final String INTEGER = " integer ";

	protected static final String REAL = " real ";

	protected static final String TEXT = " text ";

	protected static final String NOT_NULL = "not null ";

	protected static final String PRIMARY_KEY = "primary key ";

	protected static final String AUTO_INCREMENT = "autoincrement ";

	private static final String BRACE_OPEN = " (";

	private static final String BRACE_CLOSE = " );";

	public static String create(final String mTableName, final String columns) {
		return new StringBuilder(CREATE_TABLE).append(mTableName).append(BRACE_OPEN).append(_ID).append(INTEGER)
				.append(PRIMARY_KEY).append(AUTO_INCREMENT).append(COMMA).append(SYNC_ID).append(INTEGER).append(COMMA)
				.append(columns).append(COMMA).append(CREATED_ON).append(INTEGER).append(COMMA).append(CREATED_BY)
				.append(INTEGER).append(COMMA).append(MODIFIED_ON).append(INTEGER).append(COMMA).append(MODIFIED_BY)
				.append(INTEGER).append(COMMA).append(SOFT_DELETED).append(INTEGER).append(COMMA).append(DELETED_ON)
				.append(INTEGER).append(COMMA).append(DELETED_BY).append(INTEGER).append(BRACE_CLOSE).toString();
	}

	public static String drop(final String mTableName) {
		return new StringBuilder(DROP_TABLE).append(mTableName).toString();
	}
}
