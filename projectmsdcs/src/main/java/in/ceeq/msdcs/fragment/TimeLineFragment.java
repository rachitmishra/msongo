package in.ceeq.msdcs.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import in.ceeq.msdcs.R;
import in.ceeq.msdcs.provider.SurveyContract;
import in.ceeq.msdcs.utils.BaseListFragment;
import in.ceeq.msdcs.utils.Utils;

public class TimeLineFragment extends BaseListFragment implements LoaderCallbacks<Cursor> {

    public static final String LOGTAG = TimeLineFragment.class.getCanonicalName();

    private static final int LOADER_ID = 1;

    private TimeLineAdapter mTimeLineAdapter;

    public TimeLineFragment () {
    }

    public static TimeLineFragment newInstance () {
        TimeLineFragment fragment = new TimeLineFragment();
        return fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTimeLineAdapter = new TimeLineAdapter(getActivity(), null, false);
        setListAdapter(mTimeLineAdapter);
        setupLoader(false);
    }

    public void setupLoader (boolean mReset) {
        if (mReset) {
            getLoaderManager().restartLoader(LOADER_ID, null, this);
            return;
        }
        getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Override
    public void onResume () {
        super.onResume();
        setupLoader(true);
    }

    @Override
    public void onListItemClick (ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        // Cursor c = (Cursor) listView.getAdapter().getItem(position);
    }

    @Override
    public Loader<Cursor> onCreateLoader (int arg0, Bundle arg1) {
        return new CursorLoader(getActivity(), SurveyContract.Surveys.JOIN_CONTENT_URI,
                SurveyContract.Surveys.DETAILED_PROJECTION, null, null, Utils.getJoinColumnName(SurveyContract.Details
                        .PATH,
                SurveyContract.Details
                        ._ID)
                + " DESC");
    }

    @Override
    public void onLoadFinished (Loader<Cursor> arg0, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mTimeLineAdapter.swapCursor(cursor);
            mTimeLineAdapter.notifyDataSetChanged();
            setListShown(true);
        } else {
            setListShown(true);
            setEmptyText("No data available.");
        }
    }

    @Override
    public void onLoaderReset (Loader<Cursor> arg0) {
    }

    public static class ViewHolder {
        public TextView userView;
        public TextView sowingDate;
        public TextView surveyDate;
        public TextView cropStage;
        public TextView diseaseSeverity;
        public TextView pestCount;
    }

    private class TimeLineAdapter extends CursorAdapter {

        public TimeLineAdapter (Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.base_list_row_time_line, parent, false);
                holder.userView = (TextView) convertView.findViewById(R.id.userView);
                holder.sowingDate = (TextView) convertView.findViewById(R.id.sowing_date);
                holder.surveyDate = (TextView) convertView.findViewById(R.id.survey_date);
                holder.cropStage = (TextView) convertView.findViewById(R.id.crop_stage);
                holder.diseaseSeverity = (TextView) convertView.findViewById(R.id.disease_severity_score);
                holder.pestCount = (TextView) convertView.findViewById(R.id.pest_count);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Cursor item = (Cursor) getItem(position);

            holder.sowingDate.setText(Utils.getFormattedDate(
                    item.getLong(item.getColumnIndex(SurveyContract.Details.DATE_SOWING)), "dd-MMMM-yyyy"));
            holder.surveyDate.setText(Utils.getFormattedDate(
                    item.getLong(item.getColumnIndex(SurveyContract.Details.DATE_SURVEY)), "dd-MMMM-yyyy"));
            holder.userView.setText(item.getString(item.getColumnIndex(SurveyContract.Users.NAME)).substring(0, 1));
            holder.cropStage.setText(Utils.getCropStageString(getActivity(),
                    item.getInt(item.getColumnIndex(SurveyContract.Details.CROP_STAGE))));
            holder.diseaseSeverity.setText(item.getString(item
                    .getColumnIndex(SurveyContract.Details.DISEASE_SEVERITY_SCORE)));
            holder.pestCount
                    .setText(item.getString(item.getColumnIndex(SurveyContract.Details.PEST_INFESTATION_COUNT)));

            return convertView;
        }

        @Override
        public View newView (Context arg0, Cursor arg1, ViewGroup arg2) {
            return null;
        }

        @Override
        public void bindView (View arg0, Context arg1, Cursor arg2) {

        }
    }

}
