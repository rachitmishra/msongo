package in.ceeq.msdcs.fragment;

import in.ceeq.msdcs.R;
import in.ceeq.msdcs.activity.HomeActivity;
import in.ceeq.msdcs.provider.SurveyContract;
import in.ceeq.msdcs.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment implements LoaderCallbacks<Cursor> {

	private EditText mNameView;

	private AutoCompleteTextView mEmailView;

	private EditText mPasswordView;

	private View mProgressView;

	private String mName;

	public static LoginFragment newInstance() {
		return new LoginFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View loginView = inflater.inflate(R.layout.fragment_login, container, false);
		setupLoginUi(loginView);
		return loginView;
	}

	private void setupLoginUi(View loginView) {

		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");

		((TextView) loginView.findViewById(R.id.logo)).setTypeface(typeFace);
		((TextView) loginView.findViewById(R.id.header)).setTypeface(typeFace);
		((TextView) loginView.findViewById(R.id.logo_full)).setTypeface(typeFace);

		mNameView = (EditText) loginView.findViewById(R.id.name);
		mNameView.setTypeface(typeFace);

		mEmailView = (AutoCompleteTextView) loginView.findViewById(R.id.email);
		mEmailView.setTypeface(typeFace);
		populateAutoComplete();

		mPasswordView = (EditText) loginView.findViewById(R.id.password);
		mPasswordView.setTypeface(typeFace);

		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					registerUser();
					return true;
				}
				return false;
			}
		});

		Button mRegisterButton = (Button) loginView.findViewById(R.id.email_sign_in_button);
		mRegisterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				registerUser();
			}
		});

		mProgressView = loginView.findViewById(R.id.login_progress);
	}

	private void populateAutoComplete() {
		getLoaderManager().initLoader(0, null, this);
	}

	public void registerUser() {

		mEmailView.setError(null);
		mPasswordView.setError(null);

		mName = mNameView.getText().toString();
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (!TextUtils.isEmpty(mName) && !isNameValid(mName)) {
			mNameView.setError(getString(R.string.error_invalid_name));
			focusView = mNameView;
			cancel = true;
		}

		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		if (TextUtils.isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!Utils.validateEmail(email)) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			showProgress(true);
			ContentValues newUserValues = new ContentValues();
			mEmailView.setEnabled(false);
			mNameView.setEnabled(false);
			mPasswordView.setEnabled(false);
			newUserValues.put(SurveyContract.Users.NAME, mName);
			newUserValues.put(SurveyContract.Users.EMAIL, email);
			newUserValues.put(SurveyContract.Users.PASSWORD, password);
			Utils.hideKeyboard(getActivity());
			NewUserQuery.newInstance(getActivity().getContentResolver(), getActivity()).startInsert(0, mName,
					SurveyContract.Users.CONTENT_URI, newUserValues);
		}
	}

	private void showProgress(boolean show) {
		if (show) {
			mProgressView.setVisibility(View.VISIBLE);
		} else {
			mProgressView.setVisibility(View.GONE);
		}
	}

	private static class NewUserQuery extends AsyncQueryHandler {

		private Context mContext;

		public static NewUserQuery newInstance(ContentResolver contentResolver, Context context) {
			return new NewUserQuery(contentResolver, context);
		}

		public NewUserQuery(ContentResolver contentResolver, Context context) {
			super(contentResolver);
			this.mContext = context;
		}

		@Override
		protected void onInsertComplete(int token, Object cookie, Uri uri) {
			Utils.setBooleanPrefs(mContext, Utils.IS_LOGGED_IN, true);
			Utils.setLongPrefs(mContext, Utils.CURRENT_USER_ID, ContentUris.parseId(uri));
			Toast.makeText(mContext, "Welcome ! " + (String) cookie, Toast.LENGTH_SHORT).show();
			((HomeActivity) mContext).replaceFragment(HomeActivity.MAP_FRAGMENT);
			((HomeActivity) mContext).toggleAppbar(true);
		}
	}

	private boolean isPasswordValid(String password) {
		return password.length() > 4;
	}

	private boolean isNameValid(String name) {
		return name.length() > 3;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

		return new CursorLoader(getActivity(), Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
				ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

		ContactsContract.Contacts.Data.MIMETYPE + " = ?",
				new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE },

				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		List<String> emails = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			emails.add(cursor.getString(ProfileQuery.ADDRESS));
			cursor.moveToNext();
		}

		addEmailsToAutoComplete(emails);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {

	}

	private interface ProfileQuery {

		String[] PROJECTION = { ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY, };

		int ADDRESS = 0;

	}

	private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
		mEmailView.setAdapter(adapter);
	}
}
