package info.liuqy.adc.aroundme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MyMyLocationOverlay extends MyLocationOverlay {
	private Activity context;

	public MyMyLocationOverlay(Activity context, MapView mapView) {
		super(context, mapView);

		this.context = context;
	}

	@Override
	protected boolean dispatchTap() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("Star you?")
				.setMessage("Mark you as a star here, OK?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Location loc = MyMyLocationOverlay.this.getLastFix();
								if (loc != null) {
									Intent i = new Intent(context,
											StarMeActivity.class);
									i.putExtra(StarMeActivity.EXTRA_LOCATION, loc);
									context.startActivity(i);
								} else {
									Toast.makeText(context, "Cannot get your location! Please try again!", Toast.LENGTH_SHORT).show();
								}
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		dialog.show();
		return true;
	}
}
