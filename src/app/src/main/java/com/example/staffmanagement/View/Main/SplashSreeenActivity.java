package com.example.staffmanagement.View.Main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Presenter.Main.LogInPresenter;
import com.example.staffmanagement.R;

public class SplashSreeenActivity extends AppCompatActivity implements LogInInterface{
    private Animation animation;
    private ImageView img;
    private LogInPresenter mInterface;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sreeen);
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
        mInterface = new LogInPresenter(this,this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mapping();
        img.setAnimation(animation);
        prepareData();
    }

    private void mapping() {
        img = findViewById(R.id.imageViewLogo);
        animation = AnimationUtils.loadAnimation(this,R.anim.anim_splash);
    }

    @Override
    public void createNewProgressDialog(String message) {

    }

    @Override
    public void setMessageProgressDialog(String message) {

    }

    @Override
    public void dismissProgressDialog() {

    }

    @Override
    public void prepareData() {
        mInterface.prepareData();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSuccess(User user) {

    }
  
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradient(Activity activity){
        Window window = activity.getWindow();
        Drawable background = activity.getResources().getDrawable(R.drawable.bg_gradient_admin);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);
    }
}
