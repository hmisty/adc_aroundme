package info.liuqy.adc.aroundme;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class StarOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> stars = new ArrayList<OverlayItem>();
	private Context context;
	CouchDbAdapter couchdb;

	public StarOverlay(Context context, Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker)); // adjust (0,0) to center bottom
		this.context = context;

		/*
		 * populate the empty overlay.
		 * http://code.google.com/p/android/issues/detail?id=11666
		 */
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return stars.get(i);
	}

	@Override
	public int size() {
		return stars.size();
	}

	// run this in UI thread
	public void renewCouchDbAdapter() {
		couchdb = new CouchDbAdapter(handler);
	}
	
	// run this in any thread
	public void loadStarsAroundMe(MapView mapView) {
		GeoPoint p = mapView.getMapCenter();

		int lat1 = p.getLatitudeE6() - mapView.getLatitudeSpan() / 2;
		int lat2 = p.getLatitudeE6() + mapView.getLatitudeSpan() / 2;
		int long1 = p.getLongitudeE6() - mapView.getLongitudeSpan() / 2;
		int long2 = p.getLongitudeE6() + mapView.getLongitudeSpan() / 2;
		double lat1d = (double) lat1 / 1e6;
		double long1d = (double) long1 / 1e6;
		double lat2d = (double) lat2 / 1e6;
		double long2d = (double) long2 / 1e6;

		String extraPath = "/_design/stars/_view/by_loc";
		String query = "view/by_loc?startkey=[" + long1d + "," + lat1d
				+ "]&endkey=[" + long2d + "," + lat2d + "]";
		couchdb.doGet(extraPath, query);
	}

	private Handler handler = new Handler() {
		
	};
}
