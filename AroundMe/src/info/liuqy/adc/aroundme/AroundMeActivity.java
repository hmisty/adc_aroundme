package info.liuqy.adc.aroundme;

import java.util.List;
import java.util.UUID;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class AroundMeActivity extends MapActivity {
	public static String myId, myNickname, phone;

    MapView mapView;
    MapController mapCtrl;
    List<Overlay> mapOverlays;
    MyLocationOverlay myLocationOverlay;
    StarOverlay starOverlay;
    ChatAgent chatAgent;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 读取登录、注册得到的myId和myNickname
        Intent i = getIntent();
        Bundle data = i.getExtras();
        myId = data.getString("myId");
        myNickname = data.getString("myNickname");

        // 读取SharedPreferences中的手机号
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        phone = settings.getString("phone", null);

        chatAgent = new ChatAgent(handler);
        new Thread(chatAgent).start();
        
		registerReceiver(chatAgent.sendAgent, new IntentFilter(ChatAgent.SEND_ACTION));

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        mapOverlays = mapView.getOverlays();

        Drawable defaultMarker = this.getResources().getDrawable(R.drawable.star);
        starOverlay = new StarOverlay(this, defaultMarker);
        mapOverlays.add(starOverlay);
        
        myLocationOverlay = new MyMyLocationOverlay(this, mapView);
        mapOverlays.add(myLocationOverlay);
  
        mapCtrl = mapView.getController();
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unregisterReceiver(chatAgent.sendAgent);
	}
    
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mapChangeCheckerHandler.removeCallbacks(mapChangeChecker);
        myLocationOverlay.disableMyLocation();
        myLocationOverlay.disableCompass();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableCompass();
        
        starOverlay.renewCouchDbAdapter();
        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapCtrl.animateTo(myLocationOverlay.getMyLocation());
                mapCtrl.setZoom(15); //FIXME magic number
                starOverlay.loadStarsAroundMe(mapView);
    	        mapChangeCheckerHandler.postDelayed(mapChangeChecker, mapChangeCheckingDelay);
    	    }
        });
	}
	
	//the incoming message handler
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String text = msg.getData().getString(ChatAgent.EXTRA_MESSAGE);

			Log.d("XXX", "got msg from the chat server: " + text);

			//the message should be: m FROM TO CONTENT
			//or ok, error
			String[] parts = text.split(" ", 4);
			String cmd = parts[0];

			if (cmd.equals("m")) {
				String from = parts[1];
//				String to = parts[2];
				String content = parts[3];
				
				Intent i = new Intent(AroundMeActivity.this, ChatActivity.class);
				i.putExtra(ChatActivity.EXTRA_ID, from);
				i.putExtra(ChatActivity.EXTRA_MESSAGE, content);

				i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //bring it up or create it
				AroundMeActivity.this.startActivity(i);
			}
		}
	};

	//the map position/zoom change monitor
	private Handler mapChangeCheckerHandler = new Handler();
	public static final int mapChangeCheckingDelay = 3000; // in ms
	GeoPoint lastMapCenter = null;

	private Runnable mapChangeChecker = new Runnable()
	{		
	    public void run()
	    {
	    	GeoPoint newMapCenter = mapView.getMapCenter();
	    	if (lastMapCenter == null)
	    		lastMapCenter = newMapCenter;
	    	else if (!newMapCenter.equals(lastMapCenter)){
	    		lastMapCenter = newMapCenter;
		    	starOverlay.renewCouchDbAdapter();
		    	starOverlay.loadStarsAroundMe(mapView);	    		
	    	}
	    	
	        mapChangeCheckerHandler.removeCallbacks(mapChangeChecker); // remove the old callback
	        mapChangeCheckerHandler.postDelayed(mapChangeChecker, mapChangeCheckingDelay); // register a new one
	    }
	};
}