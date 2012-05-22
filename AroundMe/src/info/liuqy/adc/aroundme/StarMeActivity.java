package info.liuqy.adc.aroundme;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

public class StarMeActivity extends Activity {
	public static final String EXTRA_LOCATION = "location";

	Location location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.starme);

		Bundle data = this.getIntent().getExtras();
		location = (Location) data.getParcelable(StarMeActivity.EXTRA_LOCATION);
	}

	public void starme(View v) {
		//TODO
	}

}
