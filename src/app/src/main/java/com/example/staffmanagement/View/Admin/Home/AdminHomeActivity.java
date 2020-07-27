package com.example.staffmanagement.View.Admin.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.UserList.UserListActivity;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestActivity;
import com.example.staffmanagement.View.Main.LogInActivity;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.google.android.material.navigation.NavigationView;

public class AdminHomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        mapping();
        eventRegister();
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_drawer);
        drawerLayout = findViewById(R.id.drawer_layout);
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
                        intent = new Intent(AdminHomeActivity.this, UserListActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.item_menu_navigation_drawer_admin_profile:
                        drawerLayout.closeDrawer(GravityCompat.START);
//                        intent = new Intent(AdminHomeActivity.this, .class);
//                        startActivity(intent);
                        break;
                    case R.id.item_menu_navigation_drawer_admin_log_out:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        GeneralFunc.logout(AdminHomeActivity.this, LogInActivity.class);
                        break;
                }
                return false;
            }
        });
    }
}