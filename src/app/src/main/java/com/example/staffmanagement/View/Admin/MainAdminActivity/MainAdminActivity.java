package com.example.staffmanagement.View.Admin.MainAdminActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.staffmanagement.Presenter.Admin.UserListPresenter;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AddUserActivity;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestActivity;
import com.example.staffmanagement.Model.Database.Entity.User;

import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Main.LogInActivity;

import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Ultils.Const;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainAdminActivity extends AppCompatActivity implements MainAdminInterface {
    private Toolbar toolbar;
    private RecyclerView rvUserList;
    private ArrayList<User> arrayListUser;
    private UserAdapter adapter;
    private UserListPresenter mPresenter;
    private SwipeRefreshLayout pullToRefresh;
    private FloatingActionButton floatingActionButton_AddUser;
    private EditText edtSearch;
    private static final int ADD_USER_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        Mapping();
        setupToolbar();
        mPresenter = new UserListPresenter(this, this);
        pullToRefresh = findViewById(R.id.swipeRefeshMainAdmin);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setupList();
            }
        });
        setOnClickFloatingActionButton();
        setupList();
        eventRegister();
    }

    @Override
    public void setupList() {
        arrayListUser = new ArrayList<>();
        adapter = new UserAdapter(this, arrayListUser,mPresenter, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        ArrayList<User> userArrayList = mPresenter.getUserList();
        for (int i = 0; i < userArrayList.size(); i++) {
            if (userArrayList.get(i).getId() == UserSingleTon.getInstance().getUser().getId())
                userArrayList.remove(i);
        }

        arrayListUser.addAll(userArrayList);

        rvUserList.setLayoutManager(linearLayoutManager);
        rvUserList.setAdapter(adapter);

    }

    private void setupToolbar() {
        Intent intent = getIntent();
        toolbar.setTitle("User List");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Mapping() {
        toolbar = findViewById(R.id.toolbarMainAdmin);
        rvUserList = findViewById(R.id.recyclerViewUserList);
        floatingActionButton_AddUser = findViewById(R.id.floatingActionButton_AddUser);
        edtSearch = findViewById(R.id.searchView);
    }

    private void setOnClickFloatingActionButton() {
        floatingActionButton_AddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainAdminActivity.this, AddUserActivity.class);
                startActivityForResult(intent, ADD_USER_CODE);
            }
        });
    }

    private void eventRegister() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<User> list = mPresenter
                        .findFullName(UserSingleTon.getInstance().getUser().getId(),
                                String.valueOf(charSequence));
                adapter = new UserAdapter(MainAdminActivity.this, list, mPresenter,MainAdminActivity.this);
                rvUserList.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_USER_CODE && resultCode == RESULT_OK && data != null) {
            User user = (User) data.getSerializableExtra(Const.USER_INFO_INTENT);
            mPresenter.insertUser(user);
            setupList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuViewAllRequest: {
                Intent intent = new Intent(MainAdminActivity.this, UserRequestActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.menuViewProfile: {
                Intent intent = new Intent(MainAdminActivity.this, AdminInformationActivity.class);
                intent.setAction(AdminInformationActivity.ADMIN_PROFILE);
                startActivity(intent);
                return true;
            }
            case R.id.menuLogout: {
                GeneralFunc.logout(this,LogInActivity.class);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setRefresh(Boolean b) {
        pullToRefresh.setRefreshing(b);
    }
}