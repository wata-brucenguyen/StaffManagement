package com.example.staffmanagement.View.Admin.MainAdminActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.Home.AdminHomeActivity;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AddUserActivity;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestActivity;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestActivity;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.View.Ultils.NetworkState;
import com.example.staffmanagement.ViewModel.Admin.UserListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainAdminActivity extends AppCompatActivity implements MainAdminInterface {
    private Toolbar toolbar;
    private RecyclerView rvUserList;
    private UserAdapter mAdapter;
    private SwipeRefreshLayout pullToRefresh;
    private EditText edtSearch;
    private static final int ADD_USER_CODE = 1;
    private ImageView imgAddUser;
    private String searchString = "";
    private Map<String, Object> mCriteria;
    private int mNumRow = Constant.NUM_ROW_ITEM_USER_LIST_ADMIN;
    private boolean isLoading = false, isShowMessageEndData = false, isSearching = false;
    private UserListViewModel mViewModel;
    private Thread mSearchThread;

    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (CheckNetwork.checkInternetConnection(MainAdminActivity.this)) {
                runOnUiThread(() -> getAllRoleAndUserState());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AdminAppTheme);
        setContentView(R.layout.activity_main_admin);
        mapping();
        setupToolbar();
        setUpLinearLayout();

        mViewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
        eventRegister();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mWifiReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWifiReceiver);
    }

    private void setupList() {
        packetDataFilter();
        isLoading = true;
        mViewModel.clearList();
        mViewModel.getQuantityWaitingRequest().clear();
        mAdapter = new UserAdapter(this, mViewModel, this);
        rvUserList.setAdapter(mAdapter);

        mViewModel.insert(null);
        mAdapter.notifyItemInserted(mViewModel.getUserList().size() - 1);
        mViewModel.getLimitListUser(0, mNumRow, mCriteria);
    }


    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void onLoadMoreListSuccess(List<User> list, List<Integer> quantities) {
        if (mViewModel.getUserList() != null && mViewModel.getUserList().size() > 0
                && mViewModel.getUserList().get(mViewModel.getUserList().size() - 1) == null) {
            mViewModel.delete(mViewModel.getUserList().size() - 1);
            mAdapter.notifyItemRemoved(mViewModel.getUserList().size());
        }
        isLoading = false;
        isSearching = false;
        if (list == null || list.size() == 0) {
            if (isShowMessageEndData == false)
                showMessageEndData();
            return;
        }
        mAdapter.setData(list, quantities);
    }

    private void initScrollListener() {
        rvUserList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (CheckNetwork.checkInternetConnection(MainAdminActivity.this)) {
                    loadMore(recyclerView, dy);
                }
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
                mViewModel.getLimitListUser(mViewModel.getUserList().size() - 1, mNumRow, mCriteria);
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


    public void setStartForSearch() {
        isLoading = true;
        isSearching = true;
        mViewModel.clearList();
        mViewModel.getQuantityWaitingRequest().clear();
        mAdapter.notifyDataSetChanged();
        mViewModel.insert(null);
        mAdapter.notifyItemInserted(mViewModel.getUserList().size() - 1);
    }

    private void searchDelay() {
        if (mViewModel.getRoleList().size() > 0 && mViewModel.getUserStateList().size() > 0) {
            if (mSearchThread != null && mSearchThread.isAlive()) {
                mSearchThread.interrupt();
            }

            mSearchThread = new Thread(() -> {
                try {
                    Thread.sleep(500);
                    if (!isSearching) {
                        runOnUiThread(() -> {
                            if (CheckNetwork.checkInternetConnection(MainAdminActivity.this)) {
                                setStartForSearch();
                                mViewModel.getLimitListUser(0, mNumRow, mCriteria);
                            }
                        });
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            mSearchThread.start();
        }
    }

    @Override
    public void onChangeUserState(int idUser, int idUserState) {
        mViewModel.changeIdUserState(idUser, idUserState);
        showMessage("Change user state successfully");
    }

    private void getAllRoleAndUserState() {
        if (mViewModel.getRoleList().isEmpty() && mViewModel.getUserStateList().isEmpty())
            mViewModel.getAllRoleAndUserState();
        else if(mViewModel.getUserList() == null || mViewModel.getUserList().size() == 0)
            setupList();
    }


    private void setupToolbar() {
        toolbar.setTitle("User List");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void setUpLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvUserList.setLayoutManager(linearLayoutManager);
    }

    private void mapping() {
        pullToRefresh = findViewById(R.id.swipeRefeshMainAdmin);
        toolbar = findViewById(R.id.toolbarSendNotification);
        rvUserList = findViewById(R.id.recyclerViewUserList);
        imgAddUser = findViewById(R.id.imageViewAddUser);
        edtSearch = findViewById(R.id.searchView);
    }

    private void setOnClickFloatingActionButton() {
        imgAddUser.setOnClickListener(view -> {
            Intent intent = new Intent(MainAdminActivity.this, AddUserActivity.class);
            startActivityForResult(intent, ADD_USER_CODE);
        });
    }

    private void eventRegister() {
        setOnClickFloatingActionButton();
        pullToRefresh.setOnRefreshListener(() -> {
            pullToRefresh.setRefreshing(false);
            if (CheckNetwork.checkInternetConnection(MainAdminActivity.this)) {
                setStartForSearch();
                mViewModel.getLimitListUser(0, mNumRow, mCriteria);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchString = String.valueOf(charSequence);
                packetDataFilter();
                searchDelay();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initScrollListener();
        mViewModel.getListUserStateLD().observe(this, userStates -> {
            if (userStates != null && userStates.size() > 0) {
                mViewModel.getUserStateList().addAll(userStates);
            }
        });

        mViewModel.getListRoleLD().observe(this, roles -> {
            if (roles != null && roles.size() > 0) {
                mViewModel.getRoleList().addAll(roles);
                setupList();
            }
        });

        mViewModel.getUserListLD().observe(this, users -> {
            onLoadMoreListSuccess(users, mViewModel.getListQuantitiesLD().getValue());
        });
        GeneralFunc.setHideKeyboardOnTouch(this, findViewById(R.id.ListUser));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_USER_CODE && resultCode == RESULT_OK && data != null) {
            User user = (User) data.getSerializableExtra(Constant.USER_INFO_INTENT);
            mViewModel.insertUser(user, UserSingleTon.getInstance().getUser().getId(), mCriteria);
            if (user.getIdRole() == 1) {
                Toast.makeText(this, "Add successfully, add user with admin role won't show on list user", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Add successfully", Toast.LENGTH_SHORT).show();
                mViewModel.getUserList().add(0, user);
                mAdapter.notifyItemInserted(0);
                rvUserList.smoothScrollToPosition(0);
            }
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