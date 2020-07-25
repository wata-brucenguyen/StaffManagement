package com.example.staffmanagement.Admin.MainAdminActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.staffmanagement.Admin.Const;
import com.example.staffmanagement.Admin.UserManagementActivity.AddUserActivity;
import com.example.staffmanagement.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.Admin.UserManagementActivity.AdminInformationInterface;
import com.example.staffmanagement.Admin.UserRequestActivity.UserRequestActivity;
import com.example.staffmanagement.Database.Data.SeedData;
import com.example.staffmanagement.Database.Data.UserSingleTon;
import com.example.staffmanagement.Database.Entity.Request;
import com.example.staffmanagement.Database.Entity.User;
import com.example.staffmanagement.LogInActivity;
import com.example.staffmanagement.NonAdmin.RequestActivity.RequestActivity;
import com.example.staffmanagement.Presenter.RequestPresenter;
import com.example.staffmanagement.Presenter.UserPresenter;
import com.example.staffmanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainAdminActivity extends AppCompatActivity implements MainAdminInterface{
    private Toolbar toolbar;
    private RecyclerView rvUserList;
    private ArrayList<User> arrayListUser;
    private UserAdapter adapter;
    private RequestPresenter requestPresenter;
    private UserPresenter userPresenter;
    private  SwipeRefreshLayout pullToRefresh;
    private FloatingActionButton floatingActionButton_AddUser;
    private static final int ADD_USER_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        Mapping();
        setupToolbar();
        userPresenter = new UserPresenter(this, this);
        pullToRefresh = findViewById(R.id.swipeRefeshMainAdmin);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setupList();
            }
        });
        setOnClickFloatingActionButton();
        setupList();
    }
    @Override
    public void setupList(){
        arrayListUser=new ArrayList<>();
        requestPresenter=new RequestPresenter(this,this);
        adapter=new UserAdapter(this,arrayListUser,requestPresenter,userPresenter,this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, RecyclerView.VERTICAL,false);

        ArrayList<User> userArrayList  = userPresenter.getUserList();
        for(int i = 0 ; i < userArrayList.size() ; i++){
            if(userArrayList.get(i).getId()==UserSingleTon.getInstance().getUser().getId())
                userArrayList.remove(i);
        }

        arrayListUser.addAll(userArrayList);

        rvUserList.setLayoutManager(linearLayoutManager);
        rvUserList.setAdapter(adapter);
    }

    private void setupToolbar(){
        Intent intent=getIntent();
        String name= intent.getStringExtra("fullname");
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
    }

    private void Mapping() {
        toolbar = findViewById(R.id.toolbarMainAdmin);
        rvUserList = findViewById(R.id.recyclerViewUserList);
        floatingActionButton_AddUser = findViewById(R.id.floatingActionButton_AddUser);
    }
    private void setOnClickFloatingActionButton(){
        floatingActionButton_AddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainAdminActivity.this, AddUserActivity.class);
                startActivityForResult(intent,ADD_USER_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_USER_CODE && resultCode == RESULT_OK && data != null ){
            User user = (User) data.getSerializableExtra(Const.USER_DATA_INTENT);
            userPresenter.insertUser(user);
            setupList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_admin,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuViewAllRequest:{
                Intent intent=new Intent(MainAdminActivity.this, UserRequestActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.menuViewProfile:{
                Intent intent=new Intent(MainAdminActivity.this, AdminInformationActivity.class);
                intent.setAction(AdminInformationActivity.ADMIN_PROFILE);
                startActivity(intent);
                return true;
            }
            case R.id.menuLogout:{
                Intent intent=new Intent(MainAdminActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setRefresh(Boolean b) {
        pullToRefresh.setRefreshing(b);
    }
}