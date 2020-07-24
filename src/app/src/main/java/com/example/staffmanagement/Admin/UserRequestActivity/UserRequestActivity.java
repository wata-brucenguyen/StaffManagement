package com.example.staffmanagement.Admin.UserRequestActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.staffmanagement.Admin.MainAdminActivity.MainAdminInterface;
import com.example.staffmanagement.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.Database.Data.SeedData;
import com.example.staffmanagement.Database.Entity.Request;
import com.example.staffmanagement.Presenter.RequestPresenter;
import com.example.staffmanagement.Presenter.UserPresenter;
import com.example.staffmanagement.R;


import java.util.ArrayList;


public class UserRequestActivity extends AppCompatActivity implements UserRequestInterface {
    private Toolbar toolbar;
    private RecyclerView rvRequestList;
    private ImageButton imgBtnFilter;
    private ArrayList<Request> arrayListRequest;
    private UserRequestApdater adapter;
    private RequestPresenter requestPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);
        Mapping();
        setupToolbar();
        arrayListRequest=new ArrayList<>();
        requestPresenter=new RequestPresenter(this, this);
        adapter=new UserRequestApdater(this,arrayListRequest,requestPresenter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        arrayListRequest.addAll(SeedData.getRequestList());
        rvRequestList.setLayoutManager(linearLayoutManager);
        rvRequestList.setAdapter(adapter);
        imgBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu();
            }
        });
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
        toolbar=findViewById(R.id.toolbarRequest);
        rvRequestList=findViewById(R.id.recyclerViewRequestList);
        imgBtnFilter=findViewById(R.id.imageButtonFilter);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}