package com.example.staffmanagement.View.Main;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.example.staffmanagement.R;
import com.example.staffmanagement.ViewModel.Main.SplashScreenVM;

public class SplashScreenActivity extends AppCompatActivity {
    private Animation animation;
    private ImageView img;
    private SplashScreenVM mViewModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
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
