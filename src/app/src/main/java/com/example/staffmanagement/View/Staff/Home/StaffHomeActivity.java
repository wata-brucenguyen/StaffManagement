package com.example.staffmanagement.View.Staff.Home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.StaffRequestFilter;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Main.LoginActivity;
import com.example.staffmanagement.View.Notification.Service.Broadcast;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestActivity;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestCrudActivity.StaffRequestCrudActivity;
import com.example.staffmanagement.View.Staff.UserProfile.StaffUserProfileActivity;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Staff.HomeViewModel;
import com.example.staffmanagement.ViewModel.Staff.RequestViewModel;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class StaffHomeActivity extends AppCompatActivity {
    public static final String ACTION_ADD_NEW_REQUEST = "ACTION_ADD_NEW_REQUEST";
    private static final int REQUEST_CODE_CREATE_REQUEST = 1;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TextView txtNameUser, txtEmailInDrawer, txtHoTen, txtRequestTotal, txtRequestWaiting, txtRequestAccept, txtRequestDecline, txtNowDay;
    private ImageView imvAvatar, imgClose, imgDrawer, imgNoti, imgAddRequest;
    private Broadcast mBroadcast;
    private int f = 0;
    private PieChart pieChart;
    private HomeViewModel homeViewModel;
    private RequestViewModel requestViewModel;
    private StaffRequestFilter mFilter;
    private ValueEventListener listener;
    private DatabaseReference ref;
    private float waiting=0.0f, accept=0.0f, decline=0.0f;
    private ArrayList<PieEntry> RequestTotal;
    private Animation animScale;
    private CardView cvTotal, cvWaiting,cvAccept,cvDecline;
    private Thread  threadTime;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.StaffAppTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_staff_home);
        mFilter = new StaffRequestFilter();
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        requestViewModel=ViewModelProviders.of(this).get(RequestViewModel.class);
        mapping();
        loadHeaderDrawerNavigation(this, imvAvatar, txtNameUser, txtEmailInDrawer);
        eventRegister();
        generateToken();
        ref =  FirebaseDatabase.getInstance().getReference("database").child("Request");
        listener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                homeViewModel.TotalRequestForUser(UserSingleTon.getInstance().getUser().getId());
                homeViewModel.StateRequestForUser(UserSingleTon.getInstance().getUser().getId(), 1);
                homeViewModel.StateRequestForUser(UserSingleTon.getInstance().getUser().getId(), 2);
                homeViewModel.StateRequestForUser(UserSingleTon.getInstance().getUser().getId(), 3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addValueEventListener(listener);
        threadTime.start();

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
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        ref.removeEventListener(listener);
    }

    @Override
    public void onBackPressed() {
        if (GeneralFunc.isTheLastActivity(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Do you want to exit ?");
            builder.setPositiveButton("Ok", (dialogInterface, i) -> finish());
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else
            super.onBackPressed();
    }

    private void checkProfileStateChange() {
        boolean b = GeneralFunc.checkChangeProfile(this);
        if (b) {
            loadHeaderDrawerNavigation(this, imvAvatar, txtNameUser, txtEmailInDrawer);
        }
    }

    private void mapping() {
        cvTotal =findViewById(R.id.cardViewTotal);
        cvWaiting =findViewById(R.id.cardViewWaiting);
        cvAccept =findViewById(R.id.cardViewAccept);
        cvDecline =findViewById(R.id.cardViewDecline);
        pieChart = findViewById(R.id.pieChart);
        txtHoTen = findViewById(R.id.textViewHoTen);
        txtRequestTotal = findViewById(R.id.textViewRequestTotal);
        txtRequestAccept = findViewById(R.id.textViewRequestAccept);
        txtRequestDecline = findViewById(R.id.textViewRequestDecline);
        txtRequestWaiting = findViewById(R.id.textViewRequestWaiting);
        txtNowDay = findViewById(R.id.textViewNowDay);
        imgDrawer = findViewById(R.id.imgageViewDrawerMenu);
        imgNoti = findViewById(R.id.imgageViewDrawerNoti);
        imgAddRequest = findViewById(R.id.imageViewAddRequest);
        mDrawerLayout = findViewById(R.id.drawer_layout_staff);
        mNavigationView = findViewById(R.id.navigation_drawer_staff);
        imvAvatar = mNavigationView.getHeaderView(0).findViewById(R.id.imageView_avatar_header_drawer_navigation);
        txtNameUser = mNavigationView.getHeaderView(0).findViewById(R.id.textView_name_header_drawer_navigation);
        txtEmailInDrawer = mNavigationView.getHeaderView(0).findViewById(R.id.textView_email_header_drawer_navigation);
        imgClose = mNavigationView.getHeaderView(0).findViewById(R.id.imageViewClose);
    }

    private void PieChart(){
        RequestTotal=new ArrayList<>();
        Log.d("piechart", waiting+" "+ accept+" "+ decline);
        RequestTotal.add(new PieEntry(waiting,"Waiting"));
        RequestTotal.add((new PieEntry(accept,"Accept")));
        RequestTotal.add((new PieEntry(decline,"Decline")));
        PieDataSet pieDataSet=new PieDataSet(RequestTotal,"Request State");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Request Total");
        pieChart.setCenterTextColor(getColor(R.color.colorStart));
        pieChart.animate();
    }

    private void eventRegister() {
        animScale= AnimationUtils.loadAnimation(this,R.anim.anim_scale);
        txtHoTen.setText("Hi, "+UserSingleTon.getInstance().getUser().getFullName());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss");
        threadTime = new Thread(() -> {
            isRunning = true;
            while (isRunning) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> homeViewModel.getTime().postValue(format.format(new Date())));
            }
        });

        imgAddRequest.setOnClickListener(view -> {
            Intent intent = new Intent(StaffHomeActivity.this, StaffRequestCrudActivity.class);
            intent.setAction(ACTION_ADD_NEW_REQUEST);
            startActivityForResult(intent, REQUEST_CODE_CREATE_REQUEST);
        });
        setOnItemDrawerClickListener();
        imgDrawer.setOnClickListener(view -> mDrawerLayout.openDrawer(GravityCompat.START));

        homeViewModel.getTotalRequestLD().observe(this, integer -> {
            cvTotal.setAnimation(animScale);
            txtRequestTotal.setText(String.valueOf(integer));
            txtRequestTotal.setTextColor(Color.RED);
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(() -> txtRequestTotal.setTextColor(getColor(R.color.colorStart)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            if(waiting!= -1f && accept!=-1f && decline !=-1f)
                PieChart();
        });
        homeViewModel.getWaitingRequestLD().observe(this, integer ->
        {
            txtRequestWaiting.setText(String.valueOf(integer));
            waiting=Float.parseFloat(String.valueOf(integer));
            Log.d("piechart-waiting", waiting+" "+ accept+" "+ decline);
            cvWaiting.setAnimation(animScale);
            txtRequestWaiting.setTextColor(Color.RED);
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(() -> txtRequestWaiting.setTextColor(getColor(R.color.colorStart)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            if(waiting!= -1f && accept!=-1f && decline !=-1f)
                PieChart();
        });
        homeViewModel.getAcceptRequestLD().observe(this, integer -> {
            txtRequestAccept.setText(String.valueOf(integer));
            accept=Float.parseFloat(String.valueOf(integer));
            cvAccept.setAnimation(animScale);
            Log.d("piechart-accept", waiting+" "+ accept+" "+ decline);
            txtRequestAccept.setTextColor(Color.RED);
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(() -> txtRequestAccept.setTextColor(getColor(R.color.colorStart)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            if(waiting!= -1f && accept!=-1f && decline !=-1f)
                PieChart();
        });
        homeViewModel.getDeclineRequestLD().observe(this, integer -> {
            txtRequestDecline.setText(String.valueOf(integer));
            decline=Float.parseFloat(String.valueOf(integer));
            cvDecline.setAnimation(animScale);
            Log.d("piechart-decline", waiting+" "+ accept+" "+ decline);
            txtRequestDecline.setTextColor(Color.RED);
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(() -> txtRequestDecline.setTextColor(getColor(R.color.colorStart)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            if(waiting!= -1f && accept!=-1f && decline !=-1f)
                PieChart();
        });

        homeViewModel.getTime().observe(this, s -> txtNowDay.setText(s));
        CardViewOnClick();
    }

    private void CardViewOnClick(){
        cvTotal.setOnClickListener(view -> {
            Intent intent=new Intent(this, StaffRequestActivity.class);
            startActivity(intent);
        });
        cvWaiting.setOnClickListener(view -> {
            Intent intent=new Intent(this, StaffRequestActivity.class);
            intent.putExtra("state","Waiting");
            startActivity(intent);
        });
        cvAccept.setOnClickListener(view -> {
            Intent intent=new Intent(this, StaffRequestActivity.class);
            intent.putExtra("state","Accepte");
            startActivity(intent);
        });
        cvDecline.setOnClickListener(view -> {
            Intent intent=new Intent(this, StaffRequestActivity.class);
            intent.putExtra("state","Decline");
            startActivity(intent);
        });
    }
    private void setOnItemDrawerClickListener() {
        mNavigationView.setNavigationItemSelectedListener(item -> {
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
        });
        imgClose.setOnClickListener(view -> mDrawerLayout.closeDrawer(GravityCompat.START));
    }



    private void generateToken() {
        FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    final String token = Objects.requireNonNull(task.getResult()).getToken();
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
            if (UserSingleTon.getInstance().getUser().getAvatar() != null){
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round);
                Glide.with(this).load(UserSingleTon.getInstance().getUser().getAvatar()).apply(options).into(avatar);
            }
            txtName.setText(UserSingleTon.getInstance().getUser().getFullName());
            txtEmail.setText(UserSingleTon.getInstance().getUser().getEmail());
        })).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CREATE_REQUEST && resultCode == RESULT_OK && data != null) {
            Request request = (Request) data.getSerializableExtra(Constant.REQUEST_DATA_INTENT);
            requestViewModel.addNewRequest(request, UserSingleTon.getInstance().getUser().getId(), mFilter);
        }
    }
}