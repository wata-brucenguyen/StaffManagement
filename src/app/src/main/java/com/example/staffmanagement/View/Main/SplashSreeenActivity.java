package com.example.staffmanagement.View.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.staffmanagement.R;

public class SplashSreeenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sreeen);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Intent intent=new Intent(SplashSreeenActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}