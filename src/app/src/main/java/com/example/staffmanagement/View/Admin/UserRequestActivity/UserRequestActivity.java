package com.example.staffmanagement.View.Admin.UserRequestActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Presenter.Admin.UserRequestPresenter;
import com.example.staffmanagement.R;


import java.util.ArrayList;


public class UserRequestActivity extends AppCompatActivity implements UserRequestInterface {
    private Toolbar toolbar;
    private RecyclerView rvRequestList;
    private ImageButton imgBtnFilter;
    private ArrayList<Request> arrayListRequest;
    private UserRequestApdater adapter;
    private UserRequestPresenter mPresenter;
    private SwipeRefreshLayout pullToRefresh;
    private EditText edtSearchRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);
        Mapping();
        setupToolbar();
        mPresenter=new UserRequestPresenter(this, this);
        pullToRefresh = findViewById(R.id.swipeRefreshUserRequest);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setupList();
            }
        });
        setupList();
        imgBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu();
            }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        edtSearchRequest.setText(name);
        eventRegister();
    }
    private void setupList(){
        arrayListRequest=new ArrayList<>();

        adapter=new UserRequestApdater(this,arrayListRequest,mPresenter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        arrayListRequest.addAll(mPresenter.getAllRequest());
        rvRequestList.setLayoutManager(linearLayoutManager);
        rvRequestList.setAdapter(adapter);
    }
    private void showPopupMenu(){
        final PopupMenu popupMenu=new PopupMenu(this,imgBtnFilter);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_request_filter,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menuWaitingFilter:{

                    }
                    case R.id.menuAcceptFilter:{

                    }
                    case R.id.menuDeclineFilter
                            :{
                    }
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void Mapping() {
        toolbar = findViewById(R.id.toolbarRequest);
        rvRequestList = findViewById(R.id.recyclerViewRequestList);
        imgBtnFilter = findViewById(R.id.imageButtonFilter);
        pullToRefresh = findViewById(R.id.swipeRefreshUserRequest);
        edtSearchRequest = findViewById(R.id.editTextSearchRequest);
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

    private void eventRegister(){
        edtSearchRequest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setRefresh(Boolean b) {
        pullToRefresh.setRefreshing(b);
    }
}