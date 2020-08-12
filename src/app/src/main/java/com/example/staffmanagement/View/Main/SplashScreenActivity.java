package com.example.staffmanagement.View.Main;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Model.Database.Firebase.CreateDatabaseFirebase;
import com.example.staffmanagement.Presenter.Main.LogInPresenter;
import com.example.staffmanagement.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreenActivity extends AppCompatActivity implements LogInInterface {
    private Animation animation;
    private ImageView img;
    private LogInPresenter mPresenter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sreeen);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        mPresenter = new LogInPresenter(this, this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mapping();
        img.setAnimation(animation);
        prepareData();

//        CreateDatabaseFirebase createDatabaseFirebase = new CreateDatabaseFirebase();
//        createDatabaseFirebase.createDatabase();

    }

    private void mapping() {
        img = findViewById(R.id.imageViewLogo);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_splash);
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
        mPresenter.prepareData();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginSuccess(User user) {

    }

    @Override
    public void showFragment(int i) {

    }

    @Override
    public void executeLogin() {

    }
}
