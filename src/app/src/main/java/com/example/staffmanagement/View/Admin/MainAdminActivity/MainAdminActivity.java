package com.example.staffmanagement.View.Admin.MainAdminActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.staffmanagement.Presenter.Admin.UserListPresenter;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AddUserActivity;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestActivity;
import com.example.staffmanagement.Model.Database.Entity.User;

import com.example.staffmanagement.View.Admin.ViewModel.UserViewModel;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Main.LogInActivity;

import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainAdminActivity extends AppCompatActivity implements MainAdminInterface {
    private Toolbar toolbar;
    private RecyclerView rvUserList;
    private UserAdapter mAdapter;
    private UserListPresenter mPresenter;
    private SwipeRefreshLayout pullToRefresh;
    private ProgressDialog mProgressDialog;
    private FloatingActionButton floatingActionButton_AddUser;
    private EditText edtSearch;
    private static final int ADD_USER_CODE = 1;

    private String searchString = "";
    private Map<String, Object> mCriteria;
    private int mNumRow = Constant.NUM_ROW_ITEM_USER_LIST_ADMIN;
    private boolean isLoading = false, isShowMessageEndData = false;
    private UserViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AdminAppTheme);
        setContentView(R.layout.activity_main_admin);
        mapping();
        setupToolbar();
        setUpLinearLayout();

        mPresenter = new UserListPresenter(this, this);
        mViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        setupList();
        eventRegister();
    }

    @Override
    public void setupList() {
        packetDataFilter();

        isLoading = true;
        mAdapter = new UserAdapter(this, mViewModel.getListUser(), this);
        rvUserList.setAdapter(mAdapter);

        mViewModel.clearList();
        mViewModel.insert(null);

        mPresenter.getLimitListUser(UserSingleTon.getInstance().getUser().getId(), 0, mNumRow, mCriteria);
    }

    @Override
    public void newProgressDialog(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(message);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dismissProgressDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void onLoadMoreListSuccess(ArrayList<User> list) {
        if (mViewModel.getListUser() != null && mViewModel.getListUser().size() > 0) {
            mViewModel.delete(mViewModel.getListUser().size() - 1);
        }
        isLoading = false;
        if (list == null || list.size() == 0) {
            if (isShowMessageEndData == false)
                showMessageEndData();
            return;
        }

        mViewModel.addRange(list);
    }

    private void initScrollListener() {
        rvUserList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                loadMore(recyclerView, dy);
            }
        });
    }

    private void loadMore(RecyclerView recyclerView, int dy) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (!isLoading) {
            if (linearLayoutManager != null &&
                    linearLayoutManager.findLastVisibleItemPosition() == mViewModel.getListUser().size() - 1 && dy > 0) {
                isLoading = true;
                mViewModel.insert(null);
                mPresenter.getLimitListUser(UserSingleTon.getInstance().getUser().getId(), mViewModel.getListUser().size() - 1, mNumRow, mCriteria);
            }

        }
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
    public void onAddNewUserSuccessfully(User newItem) {
        mViewModel.insert(newItem);
        showMessage("Add user successfully");
    }

    @Override
    public void onChangeUserState(int idUser, int idUserState) {
        mPresenter.changeIdUserState(idUser, idUserState);
        mViewModel.updateState(idUser, idUserState);
        showMessage("Change user state successfully");
    }

    @Override
    public void getRoleNameById(int idRole, UserAdapter.ViewHolder holder) {
        mPresenter.getRoleNameById(idRole, holder);
    }

    @Override
    public void getAmountOfUserRequestHasWaitingState(int idUser, UserAdapter.ViewHolder holder) {
        mPresenter.getCountWaitingForRequest(idUser, holder);
    }

    private void setupToolbar() {
        toolbar.setTitle("User List");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvUserList.setLayoutManager(linearLayoutManager);
    }

    private void mapping() {
        pullToRefresh = findViewById(R.id.swipeRefeshMainAdmin);
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
        setOnClickFloatingActionButton();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(false);
                setupList();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isLoading = true;
                searchString = String.valueOf(charSequence);
                packetDataFilter();

                mViewModel.clearList();
                mViewModel.insert(null);

                mPresenter.getLimitListUser(UserSingleTon.getInstance().getUser().getId(), 0, mNumRow, mCriteria);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initScrollListener();
        setObserve();
    }

    private void setObserve() {
        mViewModel.getListUserObserver().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_USER_CODE && resultCode == RESULT_OK && data != null) {
            User user = (User) data.getSerializableExtra(Constant.USER_INFO_INTENT);
            mViewModel.insert(user);
            mPresenter.insertUser(user);
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
                GeneralFunc.logout(this, LogInActivity.class);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setRefresh(Boolean b) {
        pullToRefresh.setRefreshing(b);
    }

    private void packetDataFilter() {
        mCriteria = new HashMap<>();
        mCriteria.put(Constant.SEARCH_NAME_IN_ADMIN, searchString);
    }
    //Rx android java, weak reference, anrdroid component, leak canary, room DB, glide, android contract permission
}