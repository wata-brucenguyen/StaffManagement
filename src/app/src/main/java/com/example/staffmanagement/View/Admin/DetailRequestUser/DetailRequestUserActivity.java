package com.example.staffmanagement.View.Admin.DetailRequestUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;

public class DetailRequestUserActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtTitle, txtContent, txtState, txtTime;
    private Button btnDecline, btnAccept;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
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

        btnDecline.setOnClickListener(view -> {
            if(GeneralFunc.checkInternetConnection(DetailRequestUserActivity.this)){
                txtState.setText("Decline");
                txtState.setTextColor(getResources().getColor(R.color.colorDecline));
                request.setIdState(3);
                Intent intent = new Intent();
                intent.putExtra(Constant.REQUEST_DATA_INTENT, request);
                setResult(RESULT_OK,intent);
                finish();
            }

        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GeneralFunc.checkInternetConnection(DetailRequestUserActivity.this)){
                    txtState.setText("Accept");
                    txtState.setTextColor(getResources().getColor(R.color.colorAccept));
                    request.setIdState(2);
                    Intent intent = new Intent();
                    intent.putExtra(Constant.REQUEST_DATA_INTENT, request);
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }
        });
    }
}