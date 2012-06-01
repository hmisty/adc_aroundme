package info.liuqy.adc.aroundme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: dean
 * Date: 12-5-29
 * Time: 上午10:45
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends Activity {
    CouchDbAdapter couchdb;
    ProgressDialog progressDialog;
    String username;
    String password;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        couchdb = new CouchDbAdapter(handler);
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
                        checkLogin(jsonText);
                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this,
                                "Login failed. Wrong json format: " + jsonText,
                                Toast.LENGTH_LONG).show();
                        couchdb = new CouchDbAdapter(handler);
                        e.printStackTrace();
                        return;
                    }
                    break;
                case CouchDbAdapter.START:
                    progressDialog = ProgressDialog.show(LoginActivity.this, "",
                            "Login. Please wait...", true);
                    break;
                default:
                    //TODO
            }
        }

        //登录时，获取对应_id == USER_ID的user doc，若取到且SHA1(USER_PASSWORD)符合则登录成功
        //此后，向chat server注册时以及发送消息时均使用USER_ID而不再使用随机UUID或start doc的doc id
        private void checkLogin(String jsonText) throws JSONException {
            Boolean bFindUser = false;
            String id = null, pass = null;
            JSONObject json = new JSONObject(jsonText);
            JSONArray rows = json.getJSONArray("rows");
            for (int i = 0; i < rows.length(); i++) {
                JSONObject user = rows.getJSONObject(i).getJSONObject("value");
                id = user.getString("_id");
                pass = user.getString("password");
                if (password.equals(pass)){
                    bFindUser = true;
                    break;
                }
            }
            if (!bFindUser){
                Toast.makeText(LoginActivity.this, "密码错误!", Toast.LENGTH_SHORT).show();
                couchdb = new CouchDbAdapter(handler);
                return;
            }

            // claim my id to the chat server
            Intent in = new Intent(ChatAgent.SEND_ACTION);
            in.putExtra(ChatAgent.EXTRA_MESSAGE, "id " + AroundMeActivity.myId);
            sendBroadcast(in);
            Log.d("XXX", "set myId to " + AroundMeActivity.myId + " and nickname to " + AroundMeActivity.myNickname);

            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, AroundMeActivity.class);
            intent.putExtra("myNickname", username);
            intent.putExtra("myId", id);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    };


    public void OnLogin(View view) {
         try {
             this.username = ((EditText)findViewById(R.id.login_username_text)).getText().toString().trim();
             String password = ((EditText)findViewById(R.id.login_password_text)).getText().toString().trim();
             this.password = AeSimpleSHA1.SHA1(password);
             if (username.length() == 0) {
                 Toast.makeText(LoginActivity.this, "用户名为空", Toast.LENGTH_SHORT).show();
                 return;
             }
             if (password.length() == 0) {
                 Toast.makeText(LoginActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
                 return;
             }

             String extraPath = "/_design/users/_view/by_id";
             String query = "id=" + username;
             couchdb.doGet(extraPath, query);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void OnRegist(View view){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, RegistActivty.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }
}