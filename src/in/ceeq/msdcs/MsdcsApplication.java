package in.ceeq.msdcs;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

public class MsdcsApplication extends Application {

	public void onCreate() {
		Crashlytics.start(this);
	}
}
