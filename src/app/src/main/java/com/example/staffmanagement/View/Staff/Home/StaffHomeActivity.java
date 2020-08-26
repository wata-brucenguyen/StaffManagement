package com.example.staffmanagement.View.Staff.Home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Main.LoginActivity;
import com.example.staffmanagement.View.Notification.Service.Broadcast;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestActivity;
import com.example.staffmanagement.View.Staff.UserProfile.StaffUserProfileActivity;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.View.Ultils.ImageHandler;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import com.tuann.floatingactionbuttonexpandable.FloatingActionButtonExpandable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StaffHomeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TextView txtNameUser, txtEmailInDrawer, txtHoTen, txtRequestTotal, txtRequestWaiting, txtRequestAccept, txtRequestDecline, txtNowDay;
    private ImageView imvAvatar, imgClose, imgDrawer, imgNoti;
    private Broadcast mBroadcast;
    private int f = 0;
    private PieChart pieChart;
    private FloatingActionButtonExpandable fabAddRequest;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.StaffAppTheme);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_staff_home);
        mapping();
        loadHeaderDrawerNavigation(this, imvAvatar, txtNameUser, txtEmailInDrawer);
        eventRegister();
        generateToken();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBroadcast = new Broadcast();
        IntentFilter filter = new IntentFilter("Notification");
        registerReceiver(mBroadcast, filter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkProfileStateChange();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcast);
    }


    @Override
    public void onBackPressed() {
        if (GeneralFunc.isTheLastActivity(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Do you want to exit ?");
            builder.setPositiveButton("Ok", (dialogInterface, i) -> finish());
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                return;
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else
            super.onBackPressed();
    }

    private void PieChart(){
        ArrayList<PieEntry> RequestTotal=new ArrayList<>();
        RequestTotal.add(new PieEntry(10,"Waiting"));
        RequestTotal.add((new PieEntry(5,"Accept")));
        RequestTotal.add((new PieEntry(5,"Decline")));
        PieDataSet pieDataSet=new PieDataSet(RequestTotal,"Request State");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Request Total");
        pieChart.animate();
    }
    private boolean checkProfileStateChange() {
        boolean b = GeneralFunc.checkChangeProfile(this);
        if (b) {
            loadHeaderDrawerNavigation(this, imvAvatar, txtNameUser, txtEmailInDrawer);
        }
        return false;
    }

    private void mapping() {
        fabAddRequest=findViewById(R.id.floatingAddRequest);
        scrollView=findViewById(R.id.scrollView);
        pieChart=findViewById(R.id.pieChart);
        txtHoTen = findViewById(R.id.textViewHoTen);
        txtRequestTotal = findViewById(R.id.textViewRequestTotal);
        txtRequestAccept = findViewById(R.id.textViewRequestAccept);
        txtRequestDecline = findViewById(R.id.textViewRequestDecline);
        txtRequestWaiting = findViewById(R.id.textViewRequestWaiting);
        txtNowDay = findViewById(R.id.textViewNowDay);
        imgDrawer = findViewById(R.id.imgageViewDrawerMenu);
        imgNoti = findViewById(R.id.imgageViewDrawerNoti);

        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.main_layout);
        mNavigationView = findViewById(R.id.navigation_drawer);
        imvAvatar = mNavigationView.getHeaderView(0).findViewById(R.id.imageView_avatar_header_drawer_navigation);
        txtNameUser = mNavigationView.getHeaderView(0).findViewById(R.id.textView_name_header_drawer_navigation);
        txtEmailInDrawer = mNavigationView.getHeaderView(0).findViewById(R.id.textView_email_header_drawer_navigation);
        imgClose = mNavigationView.getHeaderView(0).findViewById(R.id.imageViewClose);
    }

    private void eventRegister() {
//        setupToolBar();
        SimpleDateFormat format = new SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss");
        new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> txtNowDay.setText(format.format(new Date())));
            }
        }).start();

        PieChart();
        setOnItemDrawerClickListener();
        imgDrawer.setOnClickListener(view -> mDrawerLayout.openDrawer(GravityCompat.START));

    }
//    private void setupToolBar() {
//        setSupportActionBar(mToolbar);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mDrawerLayout.openDrawer(GravityCompat.START);
//            }
//        });
//    }

    private void setOnItemDrawerClickListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
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
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        GeneralFunc.logout(StaffHomeActivity.this, LoginActivity.class);
                        break;
                }
                return false;
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
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

    public void loadHeaderDrawerNavigation(final Context context, final ImageView avatar, final TextView txtName, final TextView txtEmail) {
        new Thread(() -> ((Activity) context).runOnUiThread(() -> {
            if (UserSingleTon.getInstance().getUser().getAvatar() != null && UserSingleTon.getInstance().getUser().getAvatar().length > 0)
                ImageHandler.loadImageFromBytes(this, UserSingleTon.getInstance().getUser().getAvatar(), avatar);
            txtName.setText(UserSingleTon.getInstance().getUser().getFullName());
            txtEmail.setText(UserSingleTon.getInstance().getUser().getEmail());
        })).start();
    }
}