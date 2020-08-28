package com.example.staffmanagement.View.Admin.Home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.staffmanagement.Model.Entity.Rule;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.MainAdminActivity.MainAdminActivity;
import com.example.staffmanagement.View.Admin.SendNotificationActivity.SendNotificationActivity;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestActivity;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Main.LoginActivity;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Admin.AdminHomeViewModel;
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
    private TextView txtName, txtMail, txtEditRule,
            txtQuantityStaff, txtQuantityAdmin, txtName_Admin, txtCurrentDate,
            txtMostSending, txtLeastSending, txtLimitQuantityRequest, txtMonthRequest;
    private TextView txtRecentRequestQuantity, txtWaitingRequestQuantity, txtResponseRequestQuantity, txtAllRequestQuantity;
    private ImageView imgAvatar, imgClose, imgMenu;
    private AdminHomeViewModel mViewModel;
    private EditText edtNumRequest, edtPeriod, edtTypeOfPeriod;
    private Dialog mDialog;
    private ProgressDialog mProgressDialog;
    private ValueEventListener valueEventListener;
    private DatabaseReference ref;
    private Animation animScale;
    private CardView cardViewRecent, cardViewWaiting, cardViewResponse, cardViewTotal, cardViewAdmin, cardViewStaff;
    private int f = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        mViewModel = ViewModelProviders.of(this).get(AdminHomeViewModel.class);
        generateToken();
        mapping();
        statistic();
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
        drawerLayout = findViewById(R.id.drawer_layout_in_staff);

        cardViewRecent = findViewById(R.id.cardViewRecent);
        cardViewResponse = findViewById(R.id.cardViewResponse);
        cardViewWaiting = findViewById(R.id.cardViewWaiting);
        cardViewTotal = findViewById(R.id.cardViewTotal);
        cardViewAdmin = findViewById(R.id.cardViewAdmin);
        cardViewStaff = findViewById(R.id.cardViewStaff);

        txtQuantityAdmin = findViewById(R.id.txtQuantityAdmin);
        txtQuantityStaff = findViewById(R.id.txtQuantityStaff);
        txtName_Admin = findViewById(R.id.txtName_Admin);
        txtCurrentDate = findViewById(R.id.txtCurrentDate);
        txtRecentRequestQuantity = findViewById(R.id.txtRecentRequestQuantity);
        txtWaitingRequestQuantity = findViewById(R.id.txtWaitingRequestQuantity);
        txtResponseRequestQuantity = findViewById(R.id.txtAcceptRequestQuantity);
        txtAllRequestQuantity = findViewById(R.id.txtTotalRequestQuantity);

        txtName = navigationView.getHeaderView(0).findViewById(R.id.textViewName);
        txtMail = navigationView.getHeaderView(0).findViewById(R.id.textViewEmail);
        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.imageViewAvatar);
        imgClose = navigationView.getHeaderView(0).findViewById(R.id.imageViewClose);

        txtMostSending = findViewById(R.id.txtMostSendingUser);
        txtLeastSending = findViewById(R.id.txtLeastSendingUser);
        txtLimitQuantityRequest = findViewById(R.id.txtLimitQuantityRequest);

        txtEditRule = findViewById(R.id.textView_EditRule);
    }

    private void eventRegister() {
        animScale= AnimationUtils.loadAnimation(this,R.anim.anim_scale);
        imgMenu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        txtName_Admin.setText("Hi, "+ UserSingleTon.getInstance().getUser().getFullName());
        txtCurrentDate.setText(GeneralFunc.getCurrentDateTime());
        setOnItemDrawerClickListener();

        mViewModel.getStateRequestLD().observe(this, integer -> {
            cardViewWaiting.setAnimation(animScale);
            txtWaitingRequestQuantity.setTextColor(Color.RED);
            txtWaitingRequestQuantity.setText(String.valueOf(integer));
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(() -> txtWaitingRequestQuantity.setTextColor(getColor(R.color.colorLeft)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        mViewModel.getResponseRequestLD().observe(this, integer -> {
            cardViewResponse.setAnimation(animScale);
            txtResponseRequestQuantity.setTextColor(Color.RED);
            txtResponseRequestQuantity.setText(String.valueOf(integer));
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(() -> txtResponseRequestQuantity.setTextColor(getColor(R.color.colorLeft)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        mViewModel.getRecentRequestLD().observe(this, integer -> {
            cardViewRecent.setAnimation(animScale);
            txtRecentRequestQuantity.setTextColor(Color.RED);
            txtRecentRequestQuantity.setText(String.valueOf(integer));
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(() -> txtRecentRequestQuantity.setTextColor(getColor(R.color.colorLeft)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        mViewModel.getAllRequestLD().observe(this, integer -> {
            cardViewTotal.setAnimation(animScale);
            txtAllRequestQuantity.setTextColor(Color.RED);
            txtAllRequestQuantity.setText(String.valueOf(integer));
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(() -> txtAllRequestQuantity.setTextColor(getColor(R.color.colorLeft)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        mViewModel.getStaffLD().observe(this, integer -> {
            cardViewStaff.setAnimation(animScale);
            txtQuantityStaff.setTextColor(Color.RED);
            txtQuantityStaff.setText(String.valueOf(integer));
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(() -> txtQuantityStaff.setTextColor(getColor(R.color.colorLeft)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        mViewModel.getAdminLD().observe(this, integer -> {
            cardViewAdmin.setAnimation(animScale);
            txtQuantityAdmin.setTextColor(Color.RED);
            txtQuantityAdmin.setText(String.valueOf(integer));
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(() -> txtQuantityAdmin.setTextColor(getColor(R.color.colorLeft)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        mViewModel.getMostSendingLD().observe(this, s -> {
            txtMostSending.setTextColor(Color.RED);
            txtMostSending.setText(s);
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(() -> txtMostSending.setTextColor(getColor(R.color.colorRight)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        mViewModel.getLeastSendingLD().observe(this,s -> {
            txtLeastSending.setTextColor(Color.RED);
            txtLeastSending.setText(s);
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(() -> txtLeastSending.setTextColor(getColor(R.color.colorRight)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        txtEditRule.setOnClickListener(view -> {
            showDialogEditRule();
        });

        mViewModel.getNumRequestOfRule().observe(this, rule -> {

            if (mDialog != null && mDialog.isShowing() && rule != null) {
                setDataRuleToDialog(mViewModel.getNumRequestOfRule().getValue());
                Toast.makeText(AdminHomeActivity.this,"Success get/update rule",Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(AdminHomeActivity.this,"Get/update rule failed",Toast.LENGTH_SHORT).show();

            if(mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        });


    }

    private void showDialogEditRule() {
        mDialog = new Dialog(AdminHomeActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_edit_rule);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        edtNumRequest = mDialog.findViewById(R.id.editText_num_of_request);
        edtPeriod = mDialog.findViewById(R.id.editText_period);
        edtTypeOfPeriod = mDialog.findViewById(R.id.editText_type_period);
        TextView btnAccept = mDialog.findViewById(R.id.textView_accept);
        TextView btnClose = mDialog.findViewById(R.id.textView_closeDialog);

        btnClose.setOnClickListener(v -> mDialog.dismiss());

        btnAccept.setOnClickListener(v -> {
            if (!GeneralFunc.checkInternetConnection(AdminHomeActivity.this))
                return;
            if (TextUtils.isEmpty(edtNumRequest.getText().toString())) {
                Toast.makeText(AdminHomeActivity.this, "Field num of request is empty", Toast.LENGTH_SHORT).show();
                edtNumRequest.requestFocus();
                return;
            }

            mProgressDialog = new ProgressDialog(AdminHomeActivity.this);
            mProgressDialog.setMessage("Updating...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            int num = Integer.parseInt(edtNumRequest.getText().toString());
            mViewModel.updateRule(num);

        });
        mDialog.show();
        if (mViewModel.getNumRequestOfRule().getValue() != null) {
            setDataRuleToDialog(mViewModel.getNumRequestOfRule().getValue());
        } else if (GeneralFunc.checkInternetConnection(AdminHomeActivity.this))
            mViewModel.getRuleFromNetwork();
    }

    private void setDataRuleToDialog(Rule rule){
        edtNumRequest.setText(String.valueOf(rule.getMaxNumberRequestOfRule()));
        edtPeriod.setText(String.valueOf(rule.getPeriod()));
        edtTypeOfPeriod.setText(String.valueOf(rule.getTypePeriod()));
    }
    private void statistic() {
        ref = FirebaseDatabase.getInstance().getReference("database").child("Request");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mViewModel.countRequestWaiting();
                mViewModel.countRequestResponse();
                mViewModel.countRecentRequest();
                mViewModel.countAllRequest();
                mViewModel.countMostUserSendingRequest();
                mViewModel.countLeastUserSendingRequest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addValueEventListener(valueEventListener);

        ref = FirebaseDatabase.getInstance().getReference("database").child("User");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mViewModel.countStaff();
                mViewModel.countAdmin();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addValueEventListener(valueEventListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ref.removeEventListener(valueEventListener);
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
            if (UserSingleTon.getInstance().getUser().getAvatar() != null) {
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