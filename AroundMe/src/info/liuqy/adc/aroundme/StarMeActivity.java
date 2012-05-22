package info.liuqy.adc.aroundme;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class StarMeActivity extends Activity {
	public static final String EXTRA_LOCATION = "location";

	Location location;

	CouchDbAdapter couchdb;
	ProgressDialog progressDialog;
	String nickname;
	long until;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.starme);

		couchdb = new CouchDbAdapter(handler);

		Bundle data = this.getIntent().getExtras();
		location = (Location) data.getParcelable(StarMeActivity.EXTRA_LOCATION);
	}

	public void starme(View v) {
		//TODO
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle data;
			switch (msg.what) {
			case CouchDbAdapter.RESULT:
				progressDialog.dismiss();
				data = msg.getData();
				String page = data.getString("result");
				
				String jsonText = page.substring(4); // 200:{jsonText}
				//{"ok":true,"id":"e4fe0d7643f7969678868de459b3723b","rev":"1-a89de4cc9c0b5aed75c699ab77014431"}
				try {
					JSONObject json = new JSONObject(jsonText);
					String id = json.getString("id");
					
					//TODO
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Toast.makeText(StarMeActivity.this, "result!" + page,
						Toast.LENGTH_SHORT).show();
				
				finish();
				break;
			case CouchDbAdapter.START:
				progressDialog = ProgressDialog.show(StarMeActivity.this, "", 
                        "Loading. Please wait...", true);
				break;
			default:
				//TODO	
			}
		}
	};

}
