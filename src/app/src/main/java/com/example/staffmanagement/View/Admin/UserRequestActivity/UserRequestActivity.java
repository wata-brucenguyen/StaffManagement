package com.example.staffmanagement.View.Admin.UserRequestActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Presenter.Admin.UserRequestPresenter;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestActivity;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestFilterDialog;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestListAdapter;
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
    private User user;
    private ProgressDialog mProgressDialog;
    private final int mNumRow = Constant.NUM_ROW_ITEM_REQUEST_IN_STAFF;
    private String searchString = "";
    private AdminUserRequestDialog mDialog;
    private boolean isLoading = false, isShowMessageEndData = false;
    private AdminRequestFilter mCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);
       // overridePendingTransition(R.anim.anim_slide_out_left, R.anim.anim_slide_out_left);
        mCriteria = new AdminRequestFilter();
        Mapping();
        eventRegister();
        setupToolbar();
        mPresenter = new UserRequestPresenter(this, this);
        setView();
        setupList();
    }

    private void setupList() {
        isLoading = true;
        arrayListRequest = new ArrayList<>();
        adapter = new UserRequestApdater(this, arrayListRequest, mPresenter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);


        rvRequestList.setLayoutManager(linearLayoutManager);
        rvRequestList.setAdapter(adapter);
        arrayListRequest.add(null);
        adapter.notifyItemInserted(0);
        if (user == null)
            mPresenter.getLimitListRequestForUser(0, 0, mNumRow, mCriteria);
        else
            mPresenter.getLimitListRequestForUser(user.getId(), 0, mNumRow, mCriteria);
        //  mPresenter.getLimitListRequestForUser(UserSingleTon.getInstance().getUser().getId(), 0, mNumRow, mCriteria);
    }

    private void setView() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(Constant.USER_INFO_INTENT);
        if (user != null)
            edtSearchRequest.setText(user.getFullName());
    }

    private void showFilterDialog() {
        mDialog = new AdminUserRequestDialog(mCriteria, this);
        mDialog.show(getSupportFragmentManager(), null);
    }


    private void Mapping() {
        toolbar = findViewById(R.id.toolbarRequest);
        rvRequestList = findViewById(R.id.recyclerViewRequestList);
        imgBtnFilter = findViewById(R.id.imageButtonFilter);
        pullToRefresh = findViewById(R.id.swipeRefreshUserRequest);
        edtSearchRequest = findViewById(R.id.editTextSearchRequest);
        pullToRefresh = findViewById(R.id.swipeRefreshUserRequest);
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
            if (user != null) {
                mPresenter.getLimitListRequestForUser(user.getId(), arrayListRequest.size() - 1, mNumRow, mCriteria);
            } else {
                mPresenter.getLimitListRequestForUser(0, arrayListRequest.size() - 1, mNumRow, mCriteria);

            }
        }
    }

    private void eventRegister() {
        imgBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });

        edtSearchRequest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isLoading = true;
                mCriteria.setSearchString(String.valueOf(charSequence));
                arrayListRequest = new ArrayList<>();
                adapter = new UserRequestApdater(UserRequestActivity.this, arrayListRequest, mPresenter);
                rvRequestList.setAdapter(adapter);
                arrayListRequest.add(null);
                adapter.notifyItemInserted(arrayListRequest.size()-1);

                if (user == null)
                    mPresenter.getLimitListRequestForUser(0,0,mNumRow, mCriteria);
                else {
                    mPresenter.getLimitListRequestForUser(user.getId(),0,mNumRow, mCriteria);
                    user = null;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setupList();
                pullToRefresh.setRefreshing(false);
            }
        });

        onScrollRecyclerView();
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

    private void showMessageEndData() {
        isShowMessageEndData = true;
        showMessage("End data");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    isShowMessageEndData = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onLoadMoreListSuccess(ArrayList<Request> arrayList) {
        if(arrayListRequest != null && arrayListRequest.size() > 0) {
            arrayListRequest.remove(arrayListRequest.size() - 1);
            adapter.notifyItemRemoved(arrayListRequest.size());
        }
        isLoading = false;
        if (arrayList == null || arrayList.size() == 0) {
            if (isShowMessageEndData == false)
                showMessageEndData();
            return;
        }
        if (arrayListRequest == null)
            arrayListRequest = new ArrayList<>();
        arrayListRequest.addAll(arrayList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void newProgressDialog(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(message);
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog.show();
    }

    @Override
    public void setMessageProgressDialog(String message) {
        mProgressDialog.setMessage(message);
    }

    @Override
    public void dismissProgressDialog() {
        mProgressDialog.dismiss();
    }

//    @Override
//    public void onGetListSuccessfully(ArrayList<Request> list) {
//        arrayListRequest = new ArrayList<>();
//        arrayListRequest.addAll(list);
//        adapter = new UserRequestApdater(this, arrayListRequest, mPresenter);
//        rvRequestList.setAdapter(adapter);
//    }

    @Override
    public void onAddNewRequestSuccessfully(Request newItem) {
        arrayListRequest.add(newItem);
        adapter.notifyDataSetChanged();
        showMessage("Add successfully");
    }

    @Override
    public void onUpdateRequestSuccessfully(Request item) {
        showMessage("Update successfully");
    }

    @Override
    public void onApplyFilter(AdminRequestFilter filter) {
        mCriteria = filter;
        isLoading = true;
        arrayListRequest = new ArrayList<>();
        adapter = new UserRequestApdater(UserRequestActivity.this, arrayListRequest, mPresenter);
        rvRequestList.setAdapter(adapter);
        arrayListRequest.add(null);
        adapter.notifyItemInserted(arrayListRequest.size() - 1);
        if (user == null)
            mPresenter.getLimitListRequestForUser(0,0,mNumRow, mCriteria);
        else {
            mPresenter.getLimitListRequestForUser(user.getId(),0,mNumRow, mCriteria);
            user = null;
        }
        //mPresenter.getLimitListRequestForUser(UserSingleTon.getInstance().getUser().getId(), 0, Constant.NUM_ROW_ITEM_REQUEST_IN_STAFF, mCriteria);
    }


}