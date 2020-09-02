package com.example.staffmanagement.View.Admin.SendNotificationActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.MainAdminActivity.MainAdminActivity;
import com.example.staffmanagement.View.Notification.Service.Broadcast;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Admin.UserListViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendNotificationActivity extends AppCompatActivity implements SendNotificationInterface {
    private CheckNetwork mCheckNetwork;
    private Toolbar toolbar;
    private RecyclerView rvUserList;
    private SendNotificationAdapter mAdapter;
    private SwipeRefreshLayout pullToRefresh;
    private EditText edtSearch, edtQuantity;
    private CheckBox mCheckBoxAll;
    private Button mButtonSend;
    private SendNotificationDialog mDialog;

    private Map<String, Object> mCriteria;
    private int mNumRow = Constant.NUM_ROW_ITEM_USER_LIST_ADMIN;
    private boolean isLoading = false, isShowMessageEndData = false, isSearching = false;
    private UserListViewModel mViewModel;
    private Broadcast mBroadcast;
    private Thread mSearchThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AdminAppTheme);
        setContentView(R.layout.activity_admin_send_notification);

        mapping();
        setupToolbar();
        setUpLinearLayout();

        mViewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
        eventRegister();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBroadcast = new Broadcast();
        IntentFilter filter = new IntentFilter("Notification");
        registerReceiver(mBroadcast, filter);

        mCheckNetwork = new CheckNetwork(this);
        mCheckNetwork.registerCheckingNetwork();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mWifiReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcast);
        mCheckNetwork.unRegisterCheckingNetwork();
        unregisterReceiver(mWifiReceiver);
        mDialog = null;
    }

    private void setupList() {
        packetDataFilter("");

        isLoading = true;
        mViewModel.clearList();
        mViewModel.getUserCheckList().clear();
        mAdapter = new SendNotificationAdapter(this, mViewModel, this);
        rvUserList.setAdapter(mAdapter);

        mViewModel.insert(null);
        mAdapter.notifyItemInserted(mViewModel.getUserList().size() - 1);
        mViewModel.getLimitListUser(0, mNumRow, mCriteria);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void onLoadMoreListSuccess(List<User> list) {
        if (mViewModel.getUserList() != null && mViewModel.getUserList().size() > 0) {
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

        mAdapter.setData(list);
        mViewModel.countStaff();

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
                if (CheckNetwork.checkInternetConnection(SendNotificationActivity.this)) {
                    mViewModel.getLimitListUser(mViewModel.getUserList().size() - 1, mNumRow, mCriteria);
                } else {
                    if (mViewModel.getUserList() != null && mViewModel.getUserList().size() > 0
                            && mViewModel.getUserList().get(mViewModel.getUserList().size() - 1) == null) {
                        mViewModel.delete(mViewModel.getUserList().size() - 1);
                        mAdapter.notifyItemRemoved(mViewModel.getUserList().size());
                    }
                    new Thread(() -> {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isLoading = false;
                    }).start();

                }
            }
            mViewModel.countStaff();
        }
    }

    private void showMessageEndData() {
        isShowMessageEndData = true;
        showMessage("End data");

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                isShowMessageEndData = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

//    public void getAllRole() {
//        if (mViewModel.getRoleList().isEmpty())
//            mViewModel.getAllRole();
//        else if (mViewModel.getUserList() == null || mViewModel.getUserList().size() == 0)
//            setupList();
//    }

    @Override
    public void setCheckAll(boolean b) {
        mCheckBoxAll.setChecked(b);
    }

    private void setupToolbar() {
        toolbar.setTitle("User List");
        toolbar.setTitleTextColor(Color.WHITE);
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
        edtSearch = findViewById(R.id.searchView);
        edtQuantity = findViewById(R.id.editText_Quantity);
        mCheckBoxAll = findViewById(R.id.checkBoxAll);
        mButtonSend = findViewById(R.id.btnSend);
        mCheckBoxAll = findViewById(R.id.checkBoxAll);
    }


    private void eventRegister() {
        pullToRefresh.setOnRefreshListener(() -> {
            pullToRefresh.setRefreshing(false);
            mCheckBoxAll.setChecked(false);
            setupList();
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                packetDataFilter(String.valueOf(charSequence));
                searchDelay();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initScrollListener();
        sendToAll();
        mButtonSend.setOnClickListener(view -> {
            if (mViewModel.getUserCheckList() == null || mViewModel.getUserCheckList().size() == 0) {
                showMessage("Please choose the person you want to send");
            } else
                showSendNotificationDialog();
        });

//        mViewModel.getListRoleLD().observe(this, roles -> {
//            if (roles != null && roles.size() > 0) {
//                mViewModel.getRoleList().addAll(roles);
//                setupList();
//            }
//        });

        mViewModel.getUserListLD().observe(this, this::onLoadMoreListSuccess);

        mViewModel.getCountStaffLD().observe(this, integer -> edtQuantity.setText(mViewModel.getUserCheckList().size() + "/" + integer));

        mViewModel.getUserCheckListLD().observe(this, users -> {
            if (users.size() != mViewModel.getUserCheckList().size() && mViewModel.getUserCheckList().size() == 0) {
                mViewModel.getUserCheckList().addAll(users);
                mViewModel.getCountStaffLD().postValue(users.size());
            }
        });
        GeneralFunc.setHideKeyboardOnTouch(this, findViewById(R.id.Notification));
    }

    private void searchDelay() {
        if (mSearchThread != null && mSearchThread.isAlive()) {
            mSearchThread.interrupt();
        }

        mSearchThread = new Thread(() -> {
            try {
                Thread.sleep(500);
                if (!isSearching) {
                    runOnUiThread(() -> {
                        setStartForSearch();
                        mViewModel.getLimitListUser(0, mNumRow, mCriteria);
                    });
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        mSearchThread.start();
    }

    private void setStartForSearch() {
        isLoading = true;
        isSearching = true;
        mViewModel.clearList();
        mViewModel.getQuantityWaitingRequest().clear();
        mAdapter.notifyDataSetChanged();
        mViewModel.insert(null);
        mAdapter.notifyItemInserted(mViewModel.getUserList().size() - 1);
    }

    private void showSendNotificationDialog() {
        mDialog = new SendNotificationDialog(this, mViewModel);
        mDialog.show(getSupportFragmentManager(), null);
    }

    private void packetDataFilter(String edt) {
        mCriteria = new HashMap<>();
        mCriteria.put(Constant.SEARCH_NAME_IN_ADMIN, edt);
    }


    private void sendToAll() {
        mCheckBoxAll.setOnClickListener(view -> {
            if (mCheckBoxAll.isChecked()) {
                mAdapter.selectAll();
                mViewModel.countStaff();
            } else {
                mAdapter.unSelectedAll();
                mViewModel.countStaff();
            }
        });

    }

    @Override
    public void loadBottomSheetDialog(User user) {
        EditText editTextEmail, editTextPhone, editTextAddress;
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        editTextPhone = dialogView.findViewById(R.id.editTextPhone);
        editTextEmail = dialogView.findViewById(R.id.editTextEmail);
        editTextAddress = dialogView.findViewById(R.id.editTextAddress);

        editTextPhone.setText(user.getPhoneNumber());
        editTextEmail.setText(user.getEmail());
        editTextAddress.setText(user.getAddress());
        dialog.setContentView(dialogView);
        dialog.show();
    }

    @Override
    public void changeQuantity() {
        mViewModel.countStaff();
//        edtQuantity.setText(mViewModel.getUserCheckList().size() + "/" + mViewModel.getCountStaffLD().getValue());
    }

    @Override
    public void onCancelDialog() {
        mDialog = null;
    }

    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CheckNetwork.checkInternetConnection(SendNotificationActivity.this)) {
                runOnUiThread(() ->{
                    if (mViewModel.getUserList() == null || mViewModel.getUserList().size() == 0)
                        setupList();
                });
            }
        }
    };
}