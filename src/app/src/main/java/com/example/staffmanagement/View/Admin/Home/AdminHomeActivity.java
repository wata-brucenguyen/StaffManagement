package com.example.staffmanagement.View.Admin.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

public class AdminHomeActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView txtName, txtMail, txtEditRule, txtQuantityStaff, txtQuantityAdmin, txtName_Admin;
    private TextView txtRecentRequestQuantity, txtWaitingRequestQuantity, txtResponseRequestQuantity, txAllRequestQuantity;
    private ImageView imgAvatar, imgClose, imgMenu;
    private CardView mClear;
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
        if (GeneralFunc.isTheLastActivity(this)) {
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
        navigationView = findViewById(R.id.navigation_drawer_admin);
        imgMenu = findViewById(R.id.imageViewDrawerMenu);
        drawerLayout = findViewById(R.id.drawer_layout_staff);

        txtEditRule = findViewById(R.id.txtEditRule);
        txtQuantityAdmin = findViewById(R.id.txtQuantityAdmin);
        txtQuantityStaff = findViewById(R.id.txtQuantityStaff);
        txtName_Admin = findViewById(R.id.txtName_Admin);
        txtRecentRequestQuantity = findViewById(R.id.txtRecentRequestQuantity);
        txtWaitingRequestQuantity = findViewById(R.id.txtWaitingRequestQuantity);
        txtResponseRequestQuantity = findViewById(R.id.txtResponseRequestQuantity);
        txAllRequestQuantity = findViewById(R.id.txAllRequestQuantity);

        txtName = navigationView.getHeaderView(0).findViewById(R.id.textViewName);
        txtMail = navigationView.getHeaderView(0).findViewById(R.id.textViewEmail);
        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.imageViewAvatar);
        imgClose = navigationView.getHeaderView(0).findViewById(R.id.imageViewClose);

    }

    private void eventRegister() {
        imgMenu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        txtName_Admin.setText(UserSingleTon.getInstance().getUser().getFullName());
        setOnItemDrawerClickListener();
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
            if (UserSingleTon.getInstance().getUser().getAvatar() != null){
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round);
                Glide.with(this).load(UserSingleTon.getInstance().getUser().getAvatar()).apply(options).into(imgAvatar);
            }
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