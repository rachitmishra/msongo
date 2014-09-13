package in.ceeq.msdcs.fragment;

import in.ceeq.msdcs.R;
import in.ceeq.msdcs.provider.SurveyContract;
import in.ceeq.msdcs.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
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

/**
 * A login screen that offers login via email/password.
 */
public class LoginFragment extends Fragment implements LoaderCallbacks<Cursor> {

	private static final String[] DUMMY_CREDENTIALS = new String[] { "foo@example.com:hello", "bar@example.com:world" };

	private TextView mNameView;

	private AutoCompleteTextView mEmailView;

	private EditText mPasswordView;

	private View mProgressView;

	private View mLoginFormView;

	public static LoginFragment getInstance() {
		return new LoginFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View loginView = inflater.inflate(R.layout.fragment_login, container, false);
		setupLoginUi(loginView);
		return loginView;
	}

	private void setupLoginUi(View loginView) {
		mNameView = (EditText) loginView.findViewById(R.id.name);

		mEmailView = (AutoCompleteTextView) loginView.findViewById(R.id.email);
		populateAutoComplete();

		mPasswordView = (EditText) loginView.findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		Button mEmailSignInButton = (Button) loginView.findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		mLoginFormView = loginView.findViewById(R.id.login_form);
		mProgressView = loginView.findViewById(R.id.login_progress);
	}

	private void populateAutoComplete() {
		getLoaderManager().initLoader(0, null, this);
	}

	public void attemptLogin() {

		mEmailView.setError(null);
		mPasswordView.setError(null);

		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		if (TextUtils.isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!isEmailValid(email)) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			showProgress(true);

			UserValidationQuery.newInstance(getActivity().getContentResolver(), getActivity()).startQuery(
					0,
					null,
					SurveyContract.Users.CONTENT_URI,
					SurveyContract.Users.DEFAULT_PROJECTION,
					new StringBuilder(SurveyContract.Users.EMAIL).append(" = ? AND ")
							.append(SurveyContract.Users.PASSWORD).append("password = ?").toString(),
					new String[] { mEmailView.getText().toString(), mPasswordView.getText().toString() }, null);
		}
	}

	private void showProgress(boolean show) {
		if (show) {
			mProgressView.setVisibility(View.VISIBLE);
		} else {
			mProgressView.setVisibility(View.GONE);
		}
	}

	private static class UserValidationQuery extends AsyncQueryHandler {

		private Context mContext;

		public static UserValidationQuery newInstance(ContentResolver contentResolver, Context context) {
			return new UserValidationQuery(contentResolver, context);
		}

		public UserValidationQuery(ContentResolver contentResolver, Context context) {
			super(contentResolver);
			this.mContext = context;
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				Utils.setStringPrefs(mContext, Utils.CURRENT_USER_ID,
						cursor.getString(cursor.getColumnIndex(SurveyContract.Users._ID)));
				Utils.setStringPrefs(mContext, Utils.CURRENT_USER_NAME,
						cursor.getString(cursor.getColumnIndex(SurveyContract.Users.NAME)));
				Utils.setStringPrefs(mContext, Utils.CURRENT_USER_EMAIL,
						cursor.getString(cursor.getColumnIndex(SurveyContract.Users.EMAIL)));
			} else {
				Toast.makeText(mContext, "User or password is incorrect.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private boolean isEmailValid(String email) {
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		return password.length() > 4;
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
		// Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

		mEmailView.setAdapter(adapter);

	}
}
