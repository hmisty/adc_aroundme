package info.liuqy.adc.aroundme;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

public class CouchDbAdapter extends AsyncTask<String, Integer, String> {
	public static final int RESULT = 0, START = 1, PROGRESS = 2;
	Handler handler;

	// api key & secret for accessing cloudant
	// FIXME a better way to keep the secret safer?
	private static final String API_KEY = "madowstrombeesirdtherent";
	private static final String API_SECRET = "5dST3rHiFNc47jhGMAX40Ssf";
	private static final String host = "dywang.cloudant.com";
	private static final String db = "/aroundme_adc";

	public CouchDbAdapter(Handler handler) {
		this.handler = handler;
	}

	public void doGet(String extraPath, String query) {
		String path = extraPath == null ? db : (db + extraPath);
		this.execute("GET", path, query);
	}

	public void doPost(String json) {
		doPost(null, null, json);
	}
	
	public void doPost(String extraPath, String query, String json) {
		String path = extraPath == null ? db : (db + extraPath);
		this.execute("POST", path, query, json);
	}

	@Override
	protected String doInBackground(String... params) {
		HttpClient client = new DefaultHttpClient();
		
		String method = params[0];
		String path = params[1];
		String query = params[2];
		String json = params.length == 4 ? params[3] : null;
		
		try {
			// scheme://username:password@host:port/path?query#fragment
			URI url = new URI("http", host, path, query, null);

			HttpResponse response;
			if (method.equals("GET")) {
				HttpGet get = new HttpGet(url);
				get.setHeader("Authorization", "Basic " +
						Base64.encodeToString((API_KEY+":"+API_SECRET).getBytes(),
						Base64.NO_WRAP));
				response = client.execute(get);
			} else if (method.equals("POST")) {
				HttpPost post = new HttpPost(url);
				post.setHeader("Authorization", "Basic " +
						Base64.encodeToString((API_KEY+":"+API_SECRET).getBytes(),
						Base64.NO_WRAP));
				post.addHeader("Content-Type", "application/json");
				//json should not be null here
				post.setEntity(new StringEntity(json, "UTF-8"));
				response = client.execute(post);
			} else {
				// unknown method
				Log.e("XXX", "neither GET nor POST. exit.");
				return null;
			}

			int statusCode = response.getStatusLine().getStatusCode();
			String content = EntityUtils.toString(response.getEntity(), "UTF-8");
			
			//TODO better feedback using if (statusCode == 200)
			String result = statusCode + ":" + content;
			Log.d("XXX", "couchdb responded: " + result);
			
			return result;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		Message m = Message.obtain(handler, RESULT);
		Bundle data = new Bundle();
		data.putString("result", result);
		m.setData(data);
		handler.sendMessage(m);
	}

	@Override
	protected void onPreExecute() {
		Message m = Message.obtain(handler, START);
		handler.sendMessage(m);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		Message m = Message.obtain(handler, PROGRESS);
		Bundle data = new Bundle();
		data.putInt("progress", values[0]);
		m.setData(data);
		handler.sendMessage(m);
	}
}
