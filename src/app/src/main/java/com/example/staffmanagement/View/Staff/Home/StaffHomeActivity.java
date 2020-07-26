package com.example.staffmanagement.View.Staff.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.staffmanagement.Presenter.Staff.StaffHomePresenter;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Main.LogInActivity;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestActivity;
import com.example.staffmanagement.View.Staff.UserProfile.StaffUserProfileActivity;
import com.example.staffmanagement.View.Ultils.Const;
import com.google.android.material.navigation.NavigationView;

public class StaffHomeActivity extends AppCompatActivity implements StaffHomeInterface{

    private StaffHomePresenter mPresenter;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.StaffAppTheme);
        setContentView(R.layout.activity_staff_home);
        mPresenter = new StaffHomePresenter(this,this);
        mapping();
        eventRegister();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_home_staff,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_option_menu_notification_home_staff:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mapping(){
        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_drawer);
    }

    private void eventRegister(){
        setupToolBar();
        setOnItemDrawerClickListener();
    }

    private void setupToolBar(){
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void logout(){
        mDrawerLayout.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(StaffHomeActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        SharedPreferences sharedPreferences = getSharedPreferences(Const.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Const.SHARED_PREFERENCE_IS_LOGIN);
        editor.remove(Const.SHARED_PREFERENCE_ID_USER);
        editor.commit();

        startActivity(intent);
        finish();
    }

    private void setOnItemDrawerClickListener(){
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId()){
                    case R.id.item_menu_navigation_drawer_staff_home:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.item_menu_navigation_drawer_staff_request:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(StaffHomeActivity.this, StaffRequestActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_menu_navigation_drawer_staff_profile:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(StaffHomeActivity.this, StaffUserProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_menu_navigation_drawer_staff_log_out:
                        logout();
                        break;
                }
                return false;
            }
        });
    }
}