package info.liuqy.adc.aroundme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ChatAgent implements Runnable {
	public static final String EXTRA_MESSAGE = "message";
	public static final String SEND_ACTION = "chat_send";

	//FIXME hard coded configurations
	public static final String HOST = "liuqy.info";
	public static final int PORT = 4000;

	Handler handler; //the incoming message handler
	Socket conn;
	BufferedReader in;
	PrintWriter out;

	public ChatAgent(Handler handler) {
		this.handler = handler;
	}
	
	public void run() {
		try {
			while (true) {
				if (conn == null || !conn.isConnected()) {
					conn = new Socket(HOST, PORT);
					in = new BufferedReader(new InputStreamReader(
							conn.getInputStream()));
					out = new PrintWriter(new OutputStreamWriter(
							conn.getOutputStream()));
				}

				if (conn.isConnected() && !conn.isInputShutdown()) {
					String content;
					while ((content = in.readLine()) != null) {
						Message msg = handler.obtainMessage();
						Bundle data = new Bundle();
						data.putString(EXTRA_MESSAGE, content);
						msg.setData(data);
						handler.sendMessage(msg);
					}
				} else
					// TODO handling error better
					Log.e("XXX",
							"cannot read socket. disconnected or input shutdown.");

			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BroadcastReceiver sendAgent = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle data = intent.getExtras();
			String text = data.getString(EXTRA_MESSAGE);
			Log.d("XXX", "received broadcast of SEND_ACTION: " + text);
			
			if (conn != null && conn.isConnected() && !conn.isOutputShutdown()) {
				out.print(text + "\n");
				out.flush();
			} else
				//TODO handling error better
				Log.e("XXX", "cannot write socket. disconnected or output shutdown.");
		}
		
	};
	
}
