package com.example.staffmanagement.View.Admin.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.MainAdminActivity.MainAdminActivity;
import com.example.staffmanagement.View.Admin.SendNotificationActivity.SendNotificationActivity;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestActivity;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Main.LoginActivity;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.View.Ultils.ImageHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView txtName, txtMail;
    private ImageView imgAvatar, imgClose, ivClear;
    private CardView mClear;
    private ArrayList<Weather> weatherArrayList;
    private RecyclerView rvWeather;
    private WeatherAdapter adapter;
    private int f = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        generateToken();
        mapping();
        eventRegister();
        loadHeaderDrawerNavigation(imgAvatar, txtName, txtMail);
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
            loadHeaderDrawerNavigation(imgAvatar, txtName, txtMail);
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

        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        weatherArrayList.add(new Weather("1", "https://firebasestorage.googleapis.com/v0/b/staffmanagement-a0116.appspot.com/o/HinhAnh%2Fweather%2Fclear.jpg?alt=media&token=5edb33cf-9b0b-47e9-b15a-e07ae153f82d", "Clear"));
        weatherArrayList.add(new Weather("2", "https://firebasestorage.googleapis.com/v0/b/staffmanagement-a0116.appspot.com/o/HinhAnh%2Fweather%2Fheavy_rain.jpg?alt=media&token=769ca650-4279-451a-beac-37734e7b6c1b", "Heavy Rain"));
        weatherArrayList.add(new Weather("3", "https://firebasestorage.googleapis.com/v0/b/staffmanagement-a0116.appspot.com/o/HinhAnh%2Fweather%2Fpartly_cloudy.png?alt=media&token=de4d97e3-b5d9-4445-9bcd-65715d465f78", "Partly Cloudy"));
        weatherArrayList.add(new Weather("4", "https://firebasestorage.googleapis.com/v0/b/staffmanagement-a0116.appspot.com/o/HinhAnh%2Fweather%2Fsnow.jpg?alt=media&token=f20f1306-3536-47aa-8442-14a7d88488ba", "Snow"));
        weatherArrayList.add(new Weather("5", "https://firebasestorage.googleapis.com/v0/b/staffmanagement-a0116.appspot.com/o/HinhAnh%2Fweather%2Fthunderstorms.jpg?alt=media&token=37135967-2d29-4216-ba82-256be7e4f008", "Thunder Storm"));
        weatherArrayList.add(new Weather("6", "https://firebasestorage.googleapis.com/v0/b/staffmanagement-a0116.appspot.com/o/HinhAnh%2Fweather%2Fwindy.jpg?alt=media&token=9ec25337-e142-4345-ad38-d62b98d8ff63", "Windy"));
        adapter = new WeatherAdapter(this, weatherArrayList);
//        DividerItemDecoration dividerHorizontal =
//                new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);

        rvWeather.setHasFixedSize(true);
        rvWeather.setAdapter(adapter);
        rvWeather.setLayoutManager(manager);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_option_menu_notification_home_staff) {
            Intent intent = new Intent(AdminHomeActivity.this, SendNotificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadHeaderDrawerNavigation(final ImageView imgAvatar, final TextView txtName, final TextView txtMail) {
        new Thread(() -> runOnUiThread(() -> {
            if (UserSingleTon.getInstance().getUser().getAvatar() != null && UserSingleTon.getInstance().getUser().getAvatar().length > 0)
                ImageHandler.loadImageFromBytes(this, UserSingleTon.getInstance().getUser().getAvatar(), imgAvatar);
            txtName.setText(UserSingleTon.getInstance().getUser().getFullName());
            txtMail.setText(UserSingleTon.getInstance().getUser().getEmail());
        })).start();
    }

    private void setOnItemDrawerClickListener() {
        navigationView.setNavigationItemSelectedListener(item -> {
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
                    GeneralFunc.logout(AdminHomeActivity.this, LoginActivity.class);
                    break;
            }
            return false;
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }


    private void generateToken() {
        FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    final String token = task.getResult().getToken();
                    Log.d("Token", " " + token);

                    final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tokens")
                            .child(String.valueOf(UserSingleTon.getInstance().getUser().getId()));

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot d : snapshot.getChildren()) {
                                if (token.equals(d.getValue())) {
                                    f = 1;
                                    return;
                                }
                            }
                            if (f == 0) {
                                myRef.push().setValue(token);
                            }
                            myRef.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    saveToken(token);

                }
            }
        });

    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.SHARED_PREFERENCE_TOKEN, token);
        editor.apply();
    }

}