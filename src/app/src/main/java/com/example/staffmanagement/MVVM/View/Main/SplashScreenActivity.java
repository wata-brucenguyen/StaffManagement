package com.example.staffmanagement.MVVM.View.Main;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.staffmanagement.MVVM.ViewModel.Main.SplashScreenVM;
import com.example.staffmanagement.R;

public class SplashScreenActivity extends AppCompatActivity {
    private Animation animation;
    private ImageView img;
    private SplashScreenVM mViewModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sreeen);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mapping();
        mViewModel = ViewModelProviders.of(this).get(SplashScreenVM.class);
        img.setAnimation(animation);
        mViewModel.prepareData(this);
    }

    private void mapping() {
        img = findViewById(R.id.imageViewLogo);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_splash);
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
