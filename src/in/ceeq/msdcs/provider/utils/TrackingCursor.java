package in.ceeq.msdcs.provider.utils;

import java.util.LinkedList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteQuery;

public class TrackingCursor extends SQLiteCursor {

	private static List<Cursor> openCursors = new LinkedList<Cursor>();

	public TrackingCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
		super(driver, editTable, query);
		openCursors.add(this);
	}

	@Override
	public void close() {
		super.close();
		openCursors.remove(this);
	}

	public static List<Cursor> getOpenCursors() {
		return openCursors;
	}

}