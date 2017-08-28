package com.app.mathquiz;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    private Intent intent;
    private static int interval = 3000;
    private boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onPause() {
        super.onPause();
        status = false;
    }

    @Override
    protected void onResume(){
        super.onResume();
        status = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status) {
                    intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                }
            }
        },interval);
    }
}
