package com.example.staffmanagement.View.Admin.DetailRequestUser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.SendNotificationActivity.Service.Broadcast;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Admin.DetailRequestViewModel;

public class DetailRequestUserActivity extends AppCompatActivity {
    private CheckNetwork mCheckNetwork;
    private Toolbar toolbar;
    private TextView txtTitle, txtContent, txtState, txtTime;
    private Button btnDecline, btnAccept;
    private Request mRequest;
    private DetailRequestViewModel mViewModel;
    private Broadcast mBroadcast;
    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CheckNetwork.checkInternetConnection(DetailRequestUserActivity.this)) {
                runOnUiThread(() -> {
                    int id = getIntent().getIntExtra("IdRequest", 0);
                    if (id != 0 && CheckNetwork.checkInternetConnection(DetailRequestUserActivity.this))
                        mViewModel.getRequestById(id);
                });
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request_detail);
        mViewModel = ViewModelProviders.of(this).get(DetailRequestViewModel.class);
        mapping();
        setView();
        eventRegister();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCheckNetwork = new CheckNetwork(this);
        mCheckNetwork.registerCheckingNetwork();
        mBroadcast = new Broadcast();
        IntentFilter filter = new IntentFilter("Notification");
        registerReceiver(mBroadcast, filter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mWifiReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCheckNetwork.unRegisterCheckingNetwork();
        unregisterReceiver(mBroadcast);
        unregisterReceiver(mWifiReceiver);
    }

    private void setView() {
        int id = getIntent().getIntExtra("IdRequest", 0);
        if (id != 0) {
            mViewModel.getRequestById(id);
        } else {
            Intent intent = getIntent();
            mRequest = (Request) intent.getSerializableExtra(Constant.REQUEST_DATA_INTENT);
            String time = GeneralFunc.convertMilliSecToDateString(mRequest.getDateTime());
            String title = mRequest.getTitle();
            String content = mRequest.getContent();
            String state = mRequest.getStateRequest().getName();
            String fullName = mRequest.getNameOfUser();
            switch (state) {
                case "Waiting":
                    txtState.setTextColor(getResources().getColor(R.color.colorWaiting));
                    break;
                case "Accept":
                    txtState.setTextColor(getResources().getColor(R.color.colorAccept));
                    break;
                case "Decline":
                    txtState.setTextColor(getResources().getColor(R.color.colorDecline));
                    break;
            }
            txtTitle.setText(title);
            txtContent.setText(content);
            txtTime.setText(time);
            txtState.setText(state);

            toolbar.setTitle(fullName);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorInput));
        }

    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar);
        txtTitle = findViewById(R.id.textViewTitle);
        txtContent = findViewById(R.id.textViewContent);
        txtState = findViewById(R.id.textViewState);
        txtTime = findViewById(R.id.textViewTimeCreate);
        btnAccept = findViewById(R.id.buttonAccept);
        btnDecline = findViewById(R.id.buttonDecline);
    }

    private void eventRegister() {
        toolbar.setNavigationOnClickListener(view -> finish());
        mViewModel.getRequest().observe(this, request -> {
            if (request == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailRequestUserActivity.this);
                builder.setMessage("Request was deleted");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", (dialogInterface, i) -> finish());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return;
            }
            mRequest = request;
            String time = GeneralFunc.convertMilliSecToDateString(mRequest.getDateTime());
            String title = mRequest.getTitle();
            String content = mRequest.getContent();
            String state = mRequest.getStateRequest().getName();
            String fullName = mRequest.getNameOfUser();
            switch (state) {
                case "Waiting":
                    txtState.setTextColor(getResources().getColor(R.color.colorWaiting));
                    break;
                case "Accept":
                    txtState.setTextColor(getResources().getColor(R.color.colorAccept));
                    break;
                case "Decline":
                    txtState.setTextColor(getResources().getColor(R.color.colorDecline));
                    break;
            }
            txtTitle.setText(title);
            txtContent.setText(content);
            txtTime.setText(time);
            txtState.setText(state);

            toolbar.setTitle(fullName);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorInput));
        });
        btnDecline.setOnClickListener(view -> {
            if (mRequest == null) {
                Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
                return;
            }
            if (CheckNetwork.checkInternetConnection(DetailRequestUserActivity.this)) {
                mRequest.setStateRequest(new StateRequest(3, "Decline"));
                String type = getIntent().getStringExtra("ViewRequestByNotification");
                if (!TextUtils.isEmpty(type) && type.equals("ViewRequestByNotification")) {
                    mViewModel.updateRequest(mRequest);
                    Log.i("TEST_", mRequest.getTitle());
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.REQUEST_DATA_INTENT, mRequest);
                    setResult(RESULT_OK, intent);
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }).start();
                }
                txtState.setText("Decline");
                txtState.setTextColor(getResources().getColor(R.color.colorDecline));
            }
        });

        btnAccept.setOnClickListener(view -> {

            if (mRequest == null) {
                Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
                return;
            }
            if (CheckNetwork.checkInternetConnection(DetailRequestUserActivity.this)) {
                mRequest.setStateRequest(new StateRequest(2, "Accept"));
                String type = getIntent().getStringExtra("ViewRequestByNotification");
                if (!TextUtils.isEmpty(type) && type.equals("ViewRequestByNotification")) {
                    mViewModel.updateRequest(mRequest);
                    Log.i("TEST_", mRequest.getTitle());
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.REQUEST_DATA_INTENT, mRequest);
                    setResult(RESULT_OK, intent);
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }).start();
                }
                txtState.setText("Accept");
                txtState.setTextColor(getResources().getColor(R.color.colorAccept));
            }
        });
        GeneralFunc.setHideKeyboardOnTouch(this, findViewById(R.id.DetailRequest));
    }
}