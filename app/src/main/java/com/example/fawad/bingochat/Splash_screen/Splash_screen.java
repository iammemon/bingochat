package com.example.fawad.bingochat.Splash_screen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.fawad.bingochat.MainActivity;
import com.example.fawad.bingochat.R;

public class Splash_screen extends AppCompatActivity {

    private static int  SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_screen.this,MainActivity.class);
                startActivity(intent);
                finish();
                ProgressBar.getDefaultSize(50,5000);

            }
        },SPLASH_TIME_OUT);
    }
}
