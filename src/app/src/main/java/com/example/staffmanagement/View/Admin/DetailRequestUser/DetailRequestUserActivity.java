package com.example.staffmanagement.View.Admin.DetailRequestUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Presenter.Admin.DetailRequestPresenter;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestApdater;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;

public class DetailRequestUserActivity extends AppCompatActivity implements DetailRequestUserInterface {
    private Toolbar toolbar;
    private TextView txtTitle, txtContent, txtState, txtTime;
    private Button btnDecline, btnAccept;
    private Request request;
    private DetailRequestPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        mPresenter = new DetailRequestPresenter(this, this);
        mapping();
        eventRegister();
        setView();
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = mPresenter.getIdStateByName("Decline");
                request.setIdState(id);
                mPresenter.update(request);
                txtState.setText("Decline");
                txtState.setTextColor(getResources().getColor(R.color.colorDecline));
                Constant.FLAG_INTENT = 1;
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = mPresenter.getIdStateByName("Accept");
                request.setIdState(id);
                mPresenter.update(request);
                txtState.setText("Accept");
                txtState.setTextColor(getResources().getColor(R.color.colorAccept));
                Constant.FLAG_INTENT = 1;
            }
        });
    }
}