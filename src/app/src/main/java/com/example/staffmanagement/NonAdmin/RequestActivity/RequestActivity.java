package com.example.staffmanagement.NonAdmin.RequestActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.example.staffmanagement.R;

public class RequestActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Mapping();
        setupToolbar();
    }

    private void Mapping() {
        toolbar=findViewById(R.id.toolbarRequest);
    }
    private void setupToolbar(){
        toolbar.setTitle("Request List");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}