package com.example.staffmanagement.Admin.UserRequestActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.staffmanagement.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.Presenter.UserPresenter;
import com.example.staffmanagement.R;


import java.util.ArrayList;


public class UserRequestActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Spinner spnRequestState;
    private ArrayList<String> arrayListRequestState;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);


        Mapping();
        setupToolbar();
        addRequestState();
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayListRequestState);
        spnRequestState.setAdapter(adapter);
        spnRequestState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(UserRequestActivity.this, i+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void Mapping() {
        toolbar=findViewById(R.id.toolbarRequest);
        spnRequestState=findViewById(R.id.spinnerRequestState);

    }
    private void addRequestState(){
        arrayListRequestState=new ArrayList<>();
        arrayListRequestState.add("Waiting");
        arrayListRequestState.add("Accept");
        arrayListRequestState.add("Decline");
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