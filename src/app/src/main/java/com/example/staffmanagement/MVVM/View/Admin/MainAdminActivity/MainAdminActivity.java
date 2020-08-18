package com.example.staffmanagement.MVVM.View.Admin.MainAdminActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
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
import android.widget.Toast;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.MVVM.Model.Entity.UserState;
import com.example.staffmanagement.MVVM.View.Admin.UserManagementActivity.AddUserActivity;
import com.example.staffmanagement.MVVM.View.Admin.UserRequestActivity.UserRequestActivity;
import com.example.staffmanagement.MVVM.View.Admin.ViewModel.UserViewModel;

import com.example.staffmanagement.MVVM.View.Data.UserSingleTon;

import com.example.staffmanagement.MVVM.ViewModel.Admin.UserListViewModel;
import com.example.staffmanagement.R;
import com.example.staffmanagement.MVVM.View.Ultils.Constant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainAdminActivity extends AppCompatActivity implements MainAdminInterface {
    private Toolbar toolbar;
    private RecyclerView rvUserList;
    private UserAdapter mAdapter;
    private SwipeRefreshLayout pullToRefresh;
    private ProgressDialog mProgressDialog;
    private FloatingActionButton floatingActionButton_AddUser;
    private EditText edtSearch;
    private static final int ADD_USER_CODE = 1;

    private String searchString = "";
    private Map<String, Object> mCriteria;
    private int mNumRow = Constant.NUM_ROW_ITEM_USER_LIST_ADMIN;
    private boolean isLoading = false, isShowMessageEndData = false;
    private UserListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AdminAppTheme);
        setContentView(R.layout.activity_main_admin);
        mapping();
        setupToolbar();
        setUpLinearLayout();

        mViewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
        getAllRoleAndUserState();
        eventRegister();
    }

    @Override
    public void setupList() {
        packetDataFilter();

        isLoading = true;
        mViewModel.clearList();
        mViewModel.getQuantityWaitingRequest().clear();
        mAdapter = new UserAdapter(this, mViewModel, this);
        rvUserList.setAdapter(mAdapter);

        mViewModel.insert(null);
        mAdapter.notifyItemInserted(mViewModel.getUserList().size() - 1);
        mViewModel.getLimitListUser(UserSingleTon.getInstance().getUser().getId(), 0, mNumRow, mCriteria);
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
    public void onLoadMoreListSuccess(List<User> list, List<Integer> quantities) {
        if (mViewModel.getUserList() != null && mViewModel.getUserList().size() > 0) {
            mViewModel.delete(mViewModel.getUserList().size() - 1);
            mAdapter.notifyItemRemoved(mViewModel.getUserList().size());
        }
        isLoading = false;
        if (list == null || list.size() == 0) {
            if (isShowMessageEndData == false)
                showMessageEndData();
            return;
        }

        mAdapter.setData(list, quantities);
//        mViewModel.addRangeUserList(list);
//        mViewModel.addRangeQuantityWaitingRequest(quantities);
//        mAdapter.notifyDataSetChanged();
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
                    linearLayoutManager.findLastVisibleItemPosition() == mViewModel.getUserList().size() - 1 && dy > 0) {
                isLoading = true;
                mViewModel.insert(null);
                mAdapter.notifyItemInserted(mViewModel.getUserList().size() - 1);
                mViewModel.getLimitListUser(UserSingleTon.getInstance().getUser().getId(), mViewModel.getUserList().size() - 1, mNumRow, mCriteria);
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
        //mViewModel.insert(newItem);
        isLoading = true;
        mViewModel.insert(null);
        mAdapter.notifyItemInserted(mViewModel.getUserList().size() - 1);
        mViewModel.getLimitListUser(UserSingleTon.getInstance().getUser().getId(), mViewModel.getUserList().size() - 1, 1, mCriteria);
        showMessage("Add user successfully");
    }

    @Override
    public void onChangeUserState(int idUser, int idUserState) {
        mViewModel.changeIdUserState(idUser, idUserState);
        int pos = mViewModel.updateState(idUser, idUserState);
        mAdapter.notifyItemChanged(pos);
        showMessage("Change user state successfully");
    }

    @Override
    public void getAllRoleAndUserState() {
        if (mViewModel.getRoleList().isEmpty() && mViewModel.getUserStateList().isEmpty())
            mViewModel.getAllRoleAndUserState();
        else
            setupList();
    }

    @Override
    public void onSuccessGetAllRoleAndUserState(List<Role> roles, List<UserState> userStates) {
        mViewModel.addNewRoleList(roles);
        mViewModel.addNewUserStateList(userStates);
        setupList();
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
        toolbar = findViewById(R.id.toolbarSendNotification);
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
                mViewModel.getQuantityWaitingRequest().clear();
                mAdapter.notifyDataSetChanged();
                mViewModel.insert(null);
                mAdapter.notifyItemInserted(mViewModel.getUserList().size() - 1);
                mViewModel.getLimitListUser(UserSingleTon.getInstance().getUser().getId(), 0, mNumRow, mCriteria);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initScrollListener();
        mViewModel.getListUserStateLD().observe(this,userStates -> {
            if(userStates != null && userStates.size() > 0){
                mViewModel.getUserStateList().addAll(userStates);
            }
        });

        mViewModel.getListRoleLD().observe(this, roles -> {
            if (roles != null && roles.size() > 0) {
                mViewModel.getRoleList().addAll(roles);
                setupList();
            }
        });

        mViewModel.getUserListLD().observe(this,users -> {
            onLoadMoreListSuccess(users,mViewModel.getQuantityWaitingRequest());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_USER_CODE && resultCode == RESULT_OK && data != null) {
            User user = (User) data.getSerializableExtra(Constant.USER_INFO_INTENT);
            mViewModel.insertUser(user);
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void packetDataFilter() {
        mCriteria = new HashMap<>();
        mCriteria.put(Constant.SEARCH_NAME_IN_ADMIN, searchString);
    }
    //Rx android java, weak reference, anrdroid component, leak canary, room DB, glide, android contract permission
}