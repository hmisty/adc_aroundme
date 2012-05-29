package info.liuqy.adc.aroundme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: dean
 * Date: 12-5-29
 * Time: 上午10:45
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }




    public void OnLogin(View view) {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, AroundMeActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    public void OnRegist(View view){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, RegistActivty.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }
}