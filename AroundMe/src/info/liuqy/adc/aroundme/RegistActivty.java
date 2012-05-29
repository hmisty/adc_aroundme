package info.liuqy.adc.aroundme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: dean
 * Date: 12-5-29
 * Time: 下午1:46
 * To change this template use File | Settings | File Templates.
 */
public class RegistActivty extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);
    }

    public void OnRegist(View view){
        Intent intent = new Intent();
        intent.setClass(RegistActivty.this, AroundMeActivity.class);
        startActivity(intent);
        RegistActivty.this.finish();
    }
}