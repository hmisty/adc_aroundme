package info.liuqy.adc.aroundme;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class StarOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> stars = new ArrayList<OverlayItem>();
	private Context context;

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

}
