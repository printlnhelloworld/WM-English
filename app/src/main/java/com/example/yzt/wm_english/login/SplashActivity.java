package com.example.yzt.wm_english.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.yzt.wm_english.R;

public class SplashActivity extends AppCompatActivity {
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(goToMainActivity, 2000);
    }
    Runnable goToMainActivity = new Runnable() {

        @Override
        public void run() {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this,
                    Login.class));
            finish();
        }
    };
}
