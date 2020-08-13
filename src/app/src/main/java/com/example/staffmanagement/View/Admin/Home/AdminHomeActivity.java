package com.example.staffmanagement.View.Admin.Home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Presenter.Admin.AdminHomePresenter;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.MainAdminActivity.MainAdminActivity;
import com.example.staffmanagement.View.Admin.SendNotificationActivity.SendNotificationActivity;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestActivity;
import com.example.staffmanagement.View.Main.LogInActivity;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity implements AdminHomeInterface {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView txtName, txtMail;
    private AdminHomePresenter mPresenter;
    private ImageView imgAvatar, imgClose, ivClear;
    private CardView mClear;
    private ArrayList<Weather> weatherArrayList;
    private RecyclerView rvWeather;
    private WeatherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        // WeakReference<Context> weakReference = new WeakReference<>(getApplicationContext());
        mPresenter = new AdminHomePresenter(this, this);
        mapping();
        eventRegister();
        mPresenter.loadHeaderDrawerNavigation(this, imgAvatar, txtName, txtMail);
//        cardEventRegister();
        setUpList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkProfileStateChange();
    }

    private boolean checkProfileStateChange() {
        boolean b = GeneralFunc.checkChangeProfile(this);
        if (b) {
            mPresenter.loadHeaderDrawerNavigation(this, imgAvatar, txtName, txtMail);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (GeneralFunc.isTheLastActivity(this) == true) {
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
        } else
            super.onBackPressed();
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigation_drawer_admin);
        drawerLayout = findViewById(R.id.main_layout);
        txtName = navigationView.getHeaderView(0).findViewById(R.id.textViewName);
        txtMail = navigationView.getHeaderView(0).findViewById(R.id.textViewEmail);
        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.imageViewAvatar);
        imgClose = navigationView.getHeaderView(0).findViewById(R.id.imageViewClose);
        rvWeather = findViewById(R.id.recyclerViewWeather);

    }

    private void setUpList() {
        weatherArrayList = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(this,2 , GridLayoutManager.VERTICAL, false);
        weatherArrayList.add(new Weather("1", "https://firebasestorage.googleapis.com/v0/b/staffmanagement-a0116.appspot.com/o/HinhAnh%2Fweather%2Fclear.jpg?alt=media&token=5edb33cf-9b0b-47e9-b15a-e07ae153f82d", "Clear"));
        weatherArrayList.add(new Weather("2", "https://firebasestorage.googleapis.com/v0/b/staffmanagement-a0116.appspot.com/o/HinhAnh%2Fweather%2Fheavy_rain.jpg?alt=media&token=769ca650-4279-451a-beac-37734e7b6c1b", "Heavy Rain"));
        weatherArrayList.add(new Weather("3", "https://firebasestorage.googleapis.com/v0/b/staffmanagement-a0116.appspot.com/o/HinhAnh%2Fweather%2Fpartly_cloudy.png?alt=media&token=de4d97e3-b5d9-4445-9bcd-65715d465f78", "Partly Cloudy"));
        weatherArrayList.add(new Weather("4", "https://firebasestorage.googleapis.com/v0/b/staffmanagement-a0116.appspot.com/o/HinhAnh%2Fweather%2Fsnow.jpg?alt=media&token=f20f1306-3536-47aa-8442-14a7d88488ba", "Snow"));
        weatherArrayList.add(new Weather("5", "https://firebasestorage.googleapis.com/v0/b/staffmanagement-a0116.appspot.com/o/HinhAnh%2Fweather%2Fthunderstorms.jpg?alt=media&token=37135967-2d29-4216-ba82-256be7e4f008", "Thunder Storm"));
        weatherArrayList.add(new Weather("6", "https://firebasestorage.googleapis.com/v0/b/staffmanagement-a0116.appspot.com/o/HinhAnh%2Fweather%2Fwindy.jpg?alt=media&token=9ec25337-e142-4345-ad38-d62b98d8ff63", "Windy"));
        adapter = new WeatherAdapter(this, weatherArrayList);
        DividerItemDecoration dividerHorizontal =
                new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        rvWeather.setAdapter(adapter);
        rvWeather.setLayoutManager(layoutManager);

    }

    private void eventRegister() {
        setupToolBar();
        setOnItemDrawerClickListener();
    }

    private void setupToolBar() {
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
        getMenuInflater().inflate(R.menu.option_menu_home_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setOnItemDrawerClickListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
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

                    case R.id.item_menu_navigation_drawer_admin_notification:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(AdminHomeActivity.this, SendNotificationActivity.class);
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

    private void cardEventRegister() {
        mClear.setTag("front");
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(mClear, "scaleX", 1f,0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(mClear, "scaleX", 0f, 1f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.setDuration(5000);
////               oa2.setDuration(1000);

                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        oa1.setFloatValues(1f,0f);

                    }
                });
                oa1.start();
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationPause(Animator animation) {
                        super.onAnimationPause(animation);
                        ivClear.setImageResource(R.drawable.thunderstorms);
                    }
                });
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationResume(Animator animation) {
                        super.onAnimationResume(animation);
                        oa1.setFloatValues(0f,1f);
                    }
                });
                oa1.start();
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //oa1.setFloatValues(0f,1f);
                        //ivClear.setImageResource(R.drawable.clear);
//                        if (mClear.getTag().equals("front")){
//                            //oa1.setFloatValues(1f,0.5f, 0f,0.5f,1f);
//
//                            mClear.setTag("back");
//                        }
//                        else {
//                           // oa1.setFloatValues(0f,0.5f, 1f,0.5f,0f);
//                            oa1.setFloatValues(0f,-1f);
//                            ivClear.setImageResource(R.drawable.clear);
//                            mClear.setTag("front");
//                        }
                    }
                });


                oa1.start();
            }
        });
    }


}