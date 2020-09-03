package com.example.staffmanagement.View.Admin.DetailRequestUser;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Notification.Service.Broadcast;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Admin.DetailRequestViewModel;

public class DetailRequestUserActivity extends AppCompatActivity {
    private CheckNetwork mCheckNetwork;
    private Toolbar toolbar;
    private TextView txtTitle, txtContent, txtState, txtTime;
    private Button btnDecline, btnAccept;
    private Request request;
    private DetailRequestViewModel mViewModel;
    private Broadcast mBroadcast;

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
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCheckNetwork.unRegisterCheckingNetwork();
        unregisterReceiver(mBroadcast);
    }

    private void setView() {
        int id = 0;
        if (getIntent().getSerializableExtra("Request") != null)
            id = (int) getIntent().getSerializableExtra("Request");
        if (id == 0) {
            Intent intent = getIntent();
            request = (Request) intent.getSerializableExtra(Constant.REQUEST_DATA_INTENT);
            String time = GeneralFunc.convertMilliSecToDateString(request.getDateTime());
            String title = request.getTitle();
            String content = request.getContent();
            String state = intent.getStringExtra(Constant.STATE_NAME_INTENT);
            String fullName = intent.getStringExtra(Constant.FULL_NAME);
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
        } else {
            mViewModel.getRequestById(id);
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
        mViewModel.getRequest().observe(this, request -> setView());
        btnDecline.setOnClickListener(view -> {
            if (CheckNetwork.checkInternetConnection(DetailRequestUserActivity.this)) {
                txtState.setText("Decline");
                txtState.setTextColor(getResources().getColor(R.color.colorDecline));
                request.setStateRequest(new StateRequest(3, "Decline"));
                Intent intent = new Intent();
                intent.putExtra(Constant.REQUEST_DATA_INTENT, request);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnAccept.setOnClickListener(view -> {

            if (CheckNetwork.checkInternetConnection(DetailRequestUserActivity.this)) {
                txtState.setText("Accept");
                txtState.setTextColor(getResources().getColor(R.color.colorAccept));
                request.setStateRequest(new StateRequest(2, "Accept"));
                Intent intent = new Intent();
                intent.putExtra(Constant.REQUEST_DATA_INTENT, request);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        GeneralFunc.setHideKeyboardOnTouch(this, findViewById(R.id.DetailRequest));
    }
}