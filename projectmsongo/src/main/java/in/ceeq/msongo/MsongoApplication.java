package in.ceeq.msongo;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

public class MsongoApplication extends Application {

	public void onCreate() {
		Crashlytics.start(this);
	}
}
