package com.example.staffmanagement.NonAdmin.RequestActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.staffmanagement.Admin.Const;
import com.example.staffmanagement.Database.Data.UserSingleTon;
import com.example.staffmanagement.Database.Entity.Request;
import com.example.staffmanagement.LogInActivity;
import com.example.staffmanagement.NonAdmin.RequestCrudActivity.RequestCrudActivity;
import com.example.staffmanagement.NonAdmin.UserProfileActivity.UserProfileActivity;
import com.example.staffmanagement.NonAdmin.UserProfileActivity.UserProfileNonAdminInterface;
import com.example.staffmanagement.Presenter.RequestPresenter;
import com.example.staffmanagement.R;

import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity implements RequestAcInterface{
    private Toolbar toolbar;
    private RecyclerView rvRequestList;
    private EditText edtSearch;
    private RequestPresenter requestPresenter;
    private RequestListNonAdminAdapter mAdapter;
    private ArrayList<Request> requestList;
    private static final int REQUEST_CODE_CREATE_REQUEST = 1;
    private static final int REQUEST_CODE_EDIT_REQUEST = 2;
    public static final String ACTION_ADD_NEW_REQUEST = "ACTION_ADD_NEW_REQUEST";
    public static final String ACTION_EDIT_REQUEST = "ACTION_EDIT_REQUEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        requestPresenter = new RequestPresenter(this,this);
        mapping();
        eventRegister();
        setupToolbar();
        setUpListRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_non_admin,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_menu_log_out_non_admin:
                Intent intent = new Intent(RequestActivity.this, LogInActivity.class);
                startActivity(intent);
                UserSingleTon.getInstance().setUser(null);
                finish();
                break;

            case R.id.option_menu_view_profile_non_admin:
                Intent intent2 = new Intent(RequestActivity.this, UserProfileActivity.class);
                startActivity(intent2);
                break;
            case R.id.option_menu_add_new_request_non_admin:
                Intent intent1 = new Intent(RequestActivity.this, RequestCrudActivity.class);
                intent1.setAction(ACTION_ADD_NEW_REQUEST);
                startActivityForResult(intent1,REQUEST_CODE_CREATE_REQUEST);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CREATE_REQUEST && resultCode == RESULT_OK && data != null ){
            Request request = (Request) data.getSerializableExtra(Const.REQUEST_DATA_INTENT);
            requestPresenter.addNewRequest(request);
            setUpListRequest();
        }
        else if(requestCode == REQUEST_CODE_EDIT_REQUEST && resultCode == RESULT_OK && data != null ){
            Request request = (Request) data.getSerializableExtra(Const.REQUEST_DATA_INTENT);
            requestPresenter.updateRequest(request);
            showMessage("Updated");
            setUpListRequest();
        }

    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarRequest);
        rvRequestList = findViewById(R.id.recyclerView_RequestList_NonAdmin);
        edtSearch = findViewById(R.id.editText_searchRequest_NonAdmin);
    }

    private void eventRegister(){
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Request> list = requestPresenter
                        .findRequest(UserSingleTon.getInstance().getUser().getId(),
                        String.valueOf(charSequence));
                mAdapter = new RequestListNonAdminAdapter(RequestActivity.this,list,requestPresenter);
                rvRequestList.setAdapter(mAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setTitle("Request List");
    }

    private void setUpListRequest(){
        rvRequestList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        requestList = new ArrayList<>();
        requestList.addAll(requestPresenter.getAllRequestForUser(UserSingleTon.getInstance().getUser().getId()));
        mAdapter = new RequestListNonAdminAdapter(this, requestList,requestPresenter);
        rvRequestList.setAdapter(mAdapter);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static int getRequestCodeEdit(){
        return REQUEST_CODE_EDIT_REQUEST;
    }
}