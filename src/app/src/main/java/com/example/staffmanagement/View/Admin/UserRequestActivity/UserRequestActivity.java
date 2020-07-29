package com.example.staffmanagement.View.Admin.UserRequestActivity;

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
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Presenter.Admin.UserRequestPresenter;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Ultils.Constant;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UserRequestActivity extends AppCompatActivity implements UserRequestInterface {
    private Toolbar toolbar;
    private RecyclerView rvRequestList;
    private ImageButton imgBtnFilter;
    private ArrayList<Request> arrayListRequest;
    private UserRequestApdater adapter;
    private UserRequestPresenter mPresenter;
    private SwipeRefreshLayout pullToRefresh;
    private EditText edtSearchRequest;

    private final int mNumRow = Constant.NUM_ROW_ITEM_REQUEST_IN_STAFF;
    private boolean isLoading = false, isEndData = false, isShowMessageEndData = false;
    private String searchString ="";
    private Map<String, Object> mCriteria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);
        overridePendingTransition(R.anim.anim_slide_out_left, R.anim.anim_slide_out_left);
        Mapping();
        setupToolbar();
        mPresenter = new UserRequestPresenter(this, this);
        pullToRefresh = findViewById(R.id.swipeRefreshUserRequest);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setupList();
            }
        });
        setupList();
        setView();
        eventRegister();
    }

    private void setupList() {
        arrayListRequest = new ArrayList<>();
        adapter = new UserRequestApdater(this, arrayListRequest, mPresenter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        arrayListRequest.addAll(mPresenter.getAllRequest());
        rvRequestList.setLayoutManager(linearLayoutManager);
        rvRequestList.setAdapter(adapter);
    }

    private void setView(){
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra(Constant.USER_INFO_INTENT);
        edtSearchRequest.setText(user.getFullName());
    }

    private void showPopupMenu() {
        final PopupMenu popupMenu = new PopupMenu(this, imgBtnFilter);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_request_filter, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menuWaitingFilter: {

                    }
                    case R.id.menuAcceptFilter: {

                    }
                    case R.id.menuDeclineFilter: {
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

    private void setupToolbar() {
        toolbar.setTitle("Request List");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void packageDataFilter() {
        mCriteria = new HashMap<>();
        mCriteria.put(Constant.SEARCH_NAME_REQUEST_IN_STAFF, searchString);
    }

    private void onScrollRecyclerView() {
        rvRequestList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                loadMore(recyclerView, dy);
            }
        });
    }

    private void loadMore(RecyclerView recyclerView, int dy) {

        LinearLayoutManager ll = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastPosition = ll.findLastVisibleItemPosition();
        if (isLoading == false && lastPosition == arrayListRequest.size() - 1) {
            isLoading = true;
            arrayListRequest.add(null);
            adapter.notifyItemInserted(arrayListRequest.size() - 1);
            mPresenter.getLimitListRequestForUser(UserSingleTon.getInstance().getUser().getId(), arrayListRequest.size() - 1, mNumRow, mCriteria);
        }
    }

    private void eventRegister() {
        imgBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu();
            }
        });

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
    protected void onResume() {
        super.onResume();
        if (Constant.FLAG_INTENT == 1) {
            rvRequestList.setAdapter(adapter);
        }
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