package info.liuqy.adc.aroundme;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ChatActivity extends Activity {
	public static final String EXTRA_ID = "id";
	public static final String EXTRA_ATTRIBUTES = "attributes";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
	}


	public void send(View v) {
		
	}
}
