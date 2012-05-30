package info.liuqy.adc.aroundme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: dean
 * Date: 12-5-29
 * Time: 下午1:46
 * To change this template use File | Settings | File Templates.
 */
public class RegistActivty extends Activity {

    CouchDbAdapter couchdb;
    ProgressDialog progressDialog;
    String username;
    String password;
    String sex;
    String phone;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);

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
                        JSONObject json = new JSONObject(jsonText);
                        String id = json.getString("id");

                        // claim my id to the chat server
                        Intent in = new Intent(ChatAgent.SEND_ACTION);
                        in.putExtra(ChatAgent.EXTRA_MESSAGE, "id " + AroundMeActivity.myId);
                        sendBroadcast(in);

                        // Sex、手机号等信息的输入或获取可以在用户注册时就完成并用SharedPreferences保存下来供后续使用
                        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("username", username);
                        editor.putString("sex", sex);
                        editor.putString("phone", phone);
                        editor.commit();

                        Intent intent = new Intent();
                        intent.setClass(RegistActivty.this, AroundMeActivity.class);
                        intent.putExtra("myNickname", username);
                        intent.putExtra("myId", id);
                        startActivity(intent);
                        RegistActivty.this.finish();

                        Log.d("XXX", "set myId to " + AroundMeActivity.myId + " and nickname to " + AroundMeActivity.myNickname);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    //TODO a better way to do
                    Toast.makeText(RegistActivty.this, "result!" + page,
                            Toast.LENGTH_SHORT).show();

                    finish();
                    break;
                case CouchDbAdapter.START:
                    progressDialog = ProgressDialog.show(RegistActivty.this, "",
                            "Loading. Please wait...", true);
                    break;
                default:
                    //TODO
            }
        }
    };

    public void OnRegist(View view){
        try {
            username = ((EditText)findViewById(R.id.regist_username_text)).getText().toString().trim();
            password = ((EditText)findViewById(R.id.regist_password_text)).getText().toString().trim();
            sex      = ""+((Spinner) this.findViewById(R.id.regist_sex)).getSelectedItemPosition();
            phone    = ((EditText)findViewById(R.id.regist_phone_text)).getText().toString().trim();
            password = AeSimpleSHA1.SHA1(password);

            JSONObject object = new JSONObject();
            object.put("type","user");
            object.put("username", username);
            object.put("password", password);
            object.put("sex", sex);
            object.put("phone", phone);
            String json = object.toString();

            // 注册时向CouchDB POST user doc
            couchdb.doPost(json);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}