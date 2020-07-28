package com.example.staffmanagement.View.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.staffmanagement.R;

public class SplashSreeenActivity extends AppCompatActivity {
    private Animation animation;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sreeen);
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mapping();
        img.setAnimation(animation);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                    Intent intent=new Intent(SplashSreeenActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void mapping() {
        img = findViewById(R.id.imageViewLogo);
        animation = AnimationUtils.loadAnimation(this,R.anim.anim_splash);

    }
}