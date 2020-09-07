package com.example.staffmanagement.View.Admin.Home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.staffmanagement.View.Admin.SendNotificationActivity.Service.Broadcast;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
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

    private CheckNetwork mCheckNetwork;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView txtName, txtMail, txtEditRule,
            txtQuantityStaff, txtQuantityAdmin, txtName_Admin, txtCurrentDate,
            txtMostSending, txtLeastSending, txtLimitQuantityRequest, txtMonthRequest;
    private TextView txtRecentRequestQuantity, txtWaitingRequestQuantity, txtResponseRequestQuantity, txtAllRequestQuantity;
    private ImageView imgAvatar, imgClose, imgMenu, imvNotification;
    private AdminHomeViewModel mViewModel;
    private EditText edtNumRequest, edtPeriod, edtTypeOfPeriod;
    private Dialog mDialog;
    private ProgressDialog mProgressDialog;
    private ValueEventListener valueEventListenerRequest, valueEventListenerUser, valueEventListenerRule;
    private DatabaseReference refRequest, refUser, refRule;
    private Animation animScale;
    private CardView cardViewRecent, cardViewWaiting, cardViewResponse, cardViewTotal, cardViewAdmin, cardViewStaff;
    private int f = 0;
    private Broadcast mBroadcast;

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
    protected void onStart() {
        super.onStart();
        mCheckNetwork = new CheckNetwork(this);
        mCheckNetwork.registerCheckingNetwork();
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
        mCheckNetwork.unRegisterCheckingNetwork();
        unregisterReceiver(mBroadcast);
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
        imvNotification = findViewById(R.id.imgageViewDrawerNotiAdmin);

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
        animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        onClickCardView();
        imgMenu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        txtName_Admin.setText("Hi, " + UserSingleTon.getInstance().getUser().getFullName());
        txtCurrentDate.setText(GeneralFunc.getCurrentDateTime());
        setOnItemDrawerClickListener();

        imvNotification.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHomeActivity.this,SendNotificationActivity.class);
            startActivity(intent);
        });
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

        mViewModel.getLeastSendingLD().observe(this, s -> {
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
                Toast.makeText(AdminHomeActivity.this, "Success get/update rule", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            } else if (rule == null)
                Toast.makeText(AdminHomeActivity.this, "Get/update rule failed", Toast.LENGTH_SHORT).show();

            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            txtLimitQuantityRequest.setText(rule.getMaxNumberRequestOfRule() + " request in " + rule.getPeriod() + " " + rule.getTypePeriod());
        });


    }

    private void onClickCardView(){
        cardViewTotal.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserRequestActivity.class);
            startActivity(intent);
        });

        cardViewRecent.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserRequestActivity.class);
            intent.putExtra("state","recent");
            startActivity(intent);
        });

        cardViewWaiting.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserRequestActivity.class);
            intent.putExtra("state","Waiting");
            startActivity(intent);
        });
        cardViewResponse.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserRequestActivity.class);
            intent.putExtra("state","response");
            startActivity(intent);
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
        GeneralFunc.setHideKeyboardOnTouch(this,mDialog.findViewById(R.id.EditRule));
        edtNumRequest = mDialog.findViewById(R.id.editText_num_of_request);
        edtPeriod = mDialog.findViewById(R.id.editText_period);
        edtTypeOfPeriod = mDialog.findViewById(R.id.editText_type_period);
        TextView btnAccept = mDialog.findViewById(R.id.textView_accept);
        TextView btnClose = mDialog.findViewById(R.id.textView_closeDialog);

        btnClose.setOnClickListener(v -> mDialog.dismiss());

        btnAccept.setOnClickListener(v -> {
            if (!CheckNetwork.checkInternetConnection(AdminHomeActivity.this)) {
                return;
            }
            if (TextUtils.isEmpty(edtNumRequest.getText().toString())) {
                Toast.makeText(AdminHomeActivity.this, "Field num of request is empty", Toast.LENGTH_SHORT).show();
                edtNumRequest.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(edtPeriod.getText().toString())) {
                Toast.makeText(AdminHomeActivity.this, "Field period is empty", Toast.LENGTH_SHORT).show();
                edtPeriod.requestFocus();
                return;
            }

            int num = Integer.parseInt(edtNumRequest.getText().toString());
            int period = Integer.parseInt(edtPeriod.getText().toString());

            if(num > 1000){
                Toast.makeText(AdminHomeActivity.this, "Field num of request must be less than 1000", Toast.LENGTH_SHORT).show();
                edtNumRequest.requestFocus();
                return;
            }

            if (period > 1000) {
                Toast.makeText(AdminHomeActivity.this, "Field period must be less than 1000", Toast.LENGTH_SHORT).show();
                edtPeriod.requestFocus();
                return;
            }

            mProgressDialog = new ProgressDialog(AdminHomeActivity.this);
            mProgressDialog.setMessage("Updating...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();


            mViewModel.updateRule(num,period);

        });
        mDialog.show();
        if (mViewModel.getNumRequestOfRule().getValue() != null) {
            setDataRuleToDialog(mViewModel.getNumRequestOfRule().getValue());
        } else{
            if (CheckNetwork.checkInternetConnection(AdminHomeActivity.this)) {
                mViewModel.getRuleFromNetwork();
            }
        }
    }

    private void setDataRuleToDialog(Rule rule) {
        edtNumRequest.setText(String.valueOf(rule.getMaxNumberRequestOfRule()));
        edtPeriod.setText(String.valueOf(rule.getPeriod()));
        edtTypeOfPeriod.setText(String.valueOf(rule.getTypePeriod()));
    }

    private void statistic() {
        refRequest = FirebaseDatabase.getInstance().getReference("database").child("Request");
        valueEventListenerRequest = new ValueEventListener() {
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
        refRequest.addValueEventListener(valueEventListenerRequest);

        refUser = FirebaseDatabase.getInstance().getReference("database").child("User");
        valueEventListenerUser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mViewModel.countStaff();
                mViewModel.countAdmin();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refUser.addValueEventListener(valueEventListenerUser);
        mViewModel.getRuleFromNetwork();

        refRule = FirebaseDatabase.getInstance().getReference("database").child("Rule").child("rule_id_1");
        valueEventListenerRule = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mDialog == null || !mDialog.isShowing())
                    mViewModel.getRuleFromNetwork();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refRule.addValueEventListener(valueEventListenerRule);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refRequest.removeEventListener(valueEventListenerRequest);
        refUser.removeEventListener(valueEventListenerUser);
        refRule.removeEventListener(valueEventListenerRule);
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
            txtName_Admin.setText("Hi, "+UserSingleTon.getInstance().getUser().getFullName());
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
                    GeneralFunc.logout(AdminHomeActivity.this);
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
                    mViewModel.saveToken(token);
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