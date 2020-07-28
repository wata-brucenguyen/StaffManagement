package com.example.staffmanagement.View.Admin.Home;

import androidx.annotation.NonNull;

import androidx.annotation.UiThread;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Presenter.Admin.AdminHomePresenter;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.MainAdminActivity.MainAdminActivity;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestActivity;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Main.LogInActivity;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestActivity;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.View.Ultils.ImageHandler;
import com.google.android.material.navigation.NavigationView;

public class AdminHomeActivity extends AppCompatActivity implements AdminHomeInterface {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView txtName,txtMail;

    private AdminHomePresenter mPresenter;

    private ImageView imgAvatar, imgClose;
    private Animation animationInRight, animationOutLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        mPresenter = new AdminHomePresenter(this,this);

        

        mapping();
        eventRegister();
        mPresenter.loadHeaderDrawerNavigation(this,imgAvatar,txtName,txtMail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkProfileStateChange();
    }
    private boolean checkProfileStateChange(){
        boolean b = GeneralFunc.checkChangeProfile(this);
        if(b){
            mPresenter.loadHeaderDrawerNavigation(this,imgAvatar,txtName,txtMail);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(GeneralFunc.isTheLastActivity(this) == true){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Do you want to exit ?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
            super.onBackPressed();
    }
    private void mapping() {
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_drawer_admin);
        drawerLayout = findViewById(R.id.drawer_layout);
        txtName = navigationView.getHeaderView(0).findViewById(R.id.textViewName);
        txtMail = navigationView.getHeaderView(0).findViewById(R.id.textViewEmail);
        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.imageViewAvatar);
        imgClose = navigationView.getHeaderView(0).findViewById(R.id.imageViewClose);
        animationInRight = AnimationUtils.loadAnimation(this,R.anim.anim_slide_in_right);
        animationOutLeft = AnimationUtils.loadAnimation(this,R.anim.anim_slide_out_left);
    }

    private void eventRegister(){
        setupToolBar();
        setOnItemDrawerClickListener();
    }

    private void setupToolBar(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_home_admin,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setOnItemDrawerClickListener(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId()){
                    case R.id.item_menu_navigation_drawer_admin_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.item_menu_navigation_drawer_admin_request:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(AdminHomeActivity.this, UserRequestActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.item_menu_navigation_drawer_admin_user_list:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(AdminHomeActivity.this, MainAdminActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.item_menu_navigation_drawer_admin_profile:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(AdminHomeActivity.this, AdminInformationActivity.class);
                        intent.setAction(AdminInformationActivity.ADMIN_PROFILE);
                        startActivity(intent);
                        break;
                    case R.id.item_menu_navigation_drawer_admin_log_out:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        GeneralFunc.logout(AdminHomeActivity.this, LogInActivity.class);
                        break;
                }
                return false;
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }
}