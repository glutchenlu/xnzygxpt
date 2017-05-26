package develop.cl.com.crsp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import develop.cl.com.crsp.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qidong);
        final long start = System.currentTimeMillis();
        new Thread() {
            public void run() {
                long time = System.currentTimeMillis() - start;
                if (time < 2000) {
                    SystemClock.sleep(2000 - time);
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }.start();
    }
}
