package com.example.staffmanagement.View.Admin.DetailRequestUser;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestActivity;
import com.example.staffmanagement.View.Staff.UserProfile.StaffUserProfileActivity;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.View.Ultils.NetworkState;

public class DetailRequestUserActivity extends AppCompatActivity {
    private CheckNetwork mCheckNetwork;
    private Toolbar toolbar;
    private TextView txtTitle, txtContent, txtState, txtTime;
    private Button btnDecline, btnAccept;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request_detail);
        mapping();
        eventRegister();
        setView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCheckNetwork = new CheckNetwork(this);
        mCheckNetwork.registerCheckingNetwork();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCheckNetwork.unRegisterCheckingNetwork();
    }

    private void setView() {
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

        btnDecline.setOnClickListener(view -> {
            if (CheckNetwork.checkInternetConnection(DetailRequestUserActivity.this)) {
                txtState.setText("Decline");
                txtState.setTextColor(getResources().getColor(R.color.colorDecline));
                request.setStateRequest(new StateRequest(3,"Decline"));
                Intent intent = new Intent();
                intent.putExtra(Constant.REQUEST_DATA_INTENT, request);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        btnAccept.setOnClickListener(view -> {

            if (CheckNetwork.checkInternetConnection(DetailRequestUserActivity.this)) {
                txtState.setText("Accept");
                txtState.setTextColor(getResources().getColor(R.color.colorAccept));
                request.setStateRequest(new StateRequest(2,"Accept"));
                Intent intent = new Intent();
                intent.putExtra(Constant.REQUEST_DATA_INTENT, request);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        GeneralFunc.setHideKeyboardOnTouch(this,findViewById(R.id.DetailRequest));
    }
}