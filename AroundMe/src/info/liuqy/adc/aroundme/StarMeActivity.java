package info.liuqy.adc.aroundme;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
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

        // 增加用户昵称直接从主视图读取
        EditText nick = (EditText) this.findViewById(R.id.nickname);
        nick.setText(AroundMeActivity.myNickname);
	}

	public void starme(View v) {
		EditText nick = (EditText) this.findViewById(R.id.nickname);
		Spinner stay = (Spinner) this.findViewById(R.id.stay);

		nickname = nick.getText().toString();
		int stayfor = stay.getSelectedItemPosition();
		double lat0 = location.getLatitude();
		double long0 = location.getLongitude();

		long now = System.currentTimeMillis();
		switch (stayfor) {
		case 0: //30 min
			until = now + 30 * 60 * 1000;
			break;
		case 1: //1 hour
			until = now + 60 * 60 * 1000;
			break;
		case 2: //2 hour
			until = now + 2 * 60 * 60 * 1000;
			break;
		case 3: //half a day
			until = now + 4 * 60 * 60 * 1000;
			break;
		case 4: //1 day
			until = now + 8 * 60 * 60 * 1000;
			break;
		default: //30 min
			until = now + 30 * 60 * 1000;
		}

        // 修改JSON对象创建方式，增加userid,phone
        try {
            JSONObject object = new JSONObject();
            object.put("type", "star");
            object.put("name", nickname);
            object.put("until", until);
            object.put("long", long0);
            object.put("lat", lat0);
            object.put("userid", AroundMeActivity.myId);
            object.put("phone", AroundMeActivity.phone);
            String json = object.toString();
            couchdb.doPost(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle data;
			switch (msg.what) {
			case CouchDbAdapter.RESULT:
				progressDialog.dismiss();
                // 直接结束视图，不再注册ID和昵称
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
