package com.example.staffmanagement.View.Admin.UserRequestActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Ultils.ConstString;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Admin.SendNotificationActivity.Service.Broadcast;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Admin.UserRequestViewModel;

import java.util.List;


public class UserRequestActivity extends AppCompatActivity implements UserRequestInterface {

    private CheckNetwork mCheckNetwork;
    private Toolbar toolbar;
    private RecyclerView rvRequestList;
    private ImageButton imgBtnFilter;
    private UserRequestApdater adapter;
    private SwipeRefreshLayout pullToRefresh;
    private EditText edtSearchRequest;
    private User user;
    private ProgressDialog mProgressDialog;
    private final int mNumRow = Constant.NUM_ROW_ITEM_REQUEST_IN_STAFF;
    private AdminUserRequestDialog mDialog;
    private boolean isLoading = false, isShowMessageEndData = false, isSearching = false;
    private AdminRequestFilter mFilter;
    private UserRequestViewModel mViewModel;
    private Thread mSearchThread;
    private Broadcast mBroadcast;

    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CheckNetwork.checkInternetConnection(UserRequestActivity.this)) {
                if (mViewModel.getRequestList() == null || mViewModel.getRequestList().size() == 0)
                    setupList();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AdminAppTheme);
        setContentView(R.layout.activity_user_request);
        //overridePendingTransition(R.anim.anim_slide_out_left, R.anim.anim_slide_out_left);
        mViewModel = ViewModelProviders.of(this).get(UserRequestViewModel.class);
        mFilter = new AdminRequestFilter();
        Intent intent = getIntent();
        String state = intent.getStringExtra("state");

        if(!TextUtils.isEmpty(state) && state.equals(AdminRequestFilter.STATE.Waiting.toString())){
            mFilter.getStateList().add(AdminRequestFilter.STATE.Waiting);
        }

        if(!TextUtils.isEmpty(state) && state.equals("response")){
            mFilter.getStateList().add(AdminRequestFilter.STATE.Decline);
            mFilter.getStateList().add(AdminRequestFilter.STATE.Accept);
        }

        if(!TextUtils.isEmpty(state) && state.equals("recent")){
            mFilter.setToDateTime(GeneralFunc.convertDateStringToLong(GeneralFunc.getCurrentDateTime()));
            mFilter.setFromDateTime(mFilter.getToDateTime() - ConstString.LIMIT_TIME_RECENT_REQUEST);
        }

        Mapping();
        setView();
        setupToolbar();
        eventRegister();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCheckNetwork = new CheckNetwork(this);
        mCheckNetwork.registerCheckingNetwork();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mWifiReceiver, intentFilter);

        mBroadcast = new Broadcast();
        IntentFilter filter = new IntentFilter("Notification");
        registerReceiver(mBroadcast, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCheckNetwork.unRegisterCheckingNetwork();
        unregisterReceiver(mWifiReceiver);
        unregisterReceiver(mBroadcast);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            Request req = (Request) data.getSerializableExtra(Constant.REQUEST_DATA_INTENT);
            int pos = mViewModel.updateRequest(req);
            adapter.notifyItemChanged(pos);
        }
    }

    private void setView() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(Constant.USER_INFO_INTENT);
        if (user != null)
            edtSearchRequest.setText(user.getFullName());
    }

    private void showFilterDialog() {
        mDialog = new AdminUserRequestDialog(mFilter, this);
        mDialog.show(getSupportFragmentManager(), null);
    }


    private void Mapping() {
        toolbar = findViewById(R.id.toolbarRequest);
        rvRequestList = findViewById(R.id.recyclerViewRequestList);
        imgBtnFilter = findViewById(R.id.imageButtonFilter);
        pullToRefresh = findViewById(R.id.swipeRefreshUserRequest);
        edtSearchRequest = findViewById(R.id.editTextSearchRequest);
        rvRequestList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void setupToolbar() {
        toolbar.setTitle("Request List");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
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
        if (isLoading == false && lastPosition == mViewModel.getRequestList().size() - 1 && dy > 0) {
            isLoading = true;
            mViewModel.insert(null);
            adapter.notifyItemInserted(mViewModel.getRequestList().size() - 1);
            if (CheckNetwork.checkInternetConnection(UserRequestActivity.this)) {
                if (user != null) {
                    mViewModel.getLimitRequestForUser(user.getId(), mViewModel.getRequestList().size() - 1, mNumRow, mFilter);
                } else {
                    mViewModel.getLimitRequestForUser(0, mViewModel.getRequestList().size() - 1, mNumRow, mFilter);
                }
            } else {
                if (mViewModel.getRequestList().size() > 0) {
                    mViewModel.delete(mViewModel.getRequestList().size() - 1);
                    adapter.notifyItemRemoved(mViewModel.getRequestList().size());
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
    }

    private void eventRegister() {
        imgBtnFilter.setOnClickListener(view -> showFilterDialog());
        edtSearchRequest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mFilter.setSearchString(String.valueOf(charSequence));
                searchDelay();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pullToRefresh.setOnRefreshListener(() -> {
            pullToRefresh.setRefreshing(false);
            if (CheckNetwork.checkInternetConnection(UserRequestActivity.this)) {
                setupList();
            }

        });

        onScrollRecyclerView();

        mViewModel.getRequestListLD().observe(this, requests -> {
            onLoadMoreListSuccess(requests);
        });
        GeneralFunc.setHideKeyboardOnTouch(this, findViewById(R.id.UserRequest));
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
                        if (CheckNetwork.checkInternetConnection(UserRequestActivity.this)) {
                            if (!isSearching && adapter != null) {
                                isLoading = true;
                                setStartForSearch();
                                if (user == null)
                                    mViewModel.getLimitRequestForUser(0, 0, mNumRow, mFilter);
                                else {
                                    mViewModel.getLimitRequestForUser(user.getId(), 0, mNumRow, mFilter);
                                    user = null;
                                }
                            }
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        mSearchThread.start();
    }

    private void setupList() {
        isLoading = true;
        mViewModel.clearList();
        adapter = new UserRequestApdater(this, mViewModel);
        rvRequestList.setAdapter(adapter);
        mViewModel.insert(null);
        adapter.notifyItemInserted(mViewModel.getRequestList().size() - 1);
        if (user == null)
            mViewModel.getLimitRequestForUser(0, 0, mNumRow, mFilter);
        else
            mViewModel.getLimitRequestForUser(user.getId(), 0, mNumRow, mFilter);
    }


    private void setStartForSearch() {
        isLoading = true;
        isSearching = true;
        mViewModel.clearList();
        adapter.notifyDataSetChanged();
        mViewModel.insert(null);
        adapter.notifyItemInserted(mViewModel.getRequestList().size() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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

    public void onLoadMoreListSuccess(List<Request> requestList) {
        if (mViewModel.getRequestList().size() > 0) {
            mViewModel.delete(mViewModel.getRequestList().size() - 1);
            adapter.notifyItemRemoved(mViewModel.getRequestList().size());
        }
        isLoading = false;
        isSearching = false;

        if ((requestList == null || requestList.size() == 0)) {
            if (isShowMessageEndData == false)
                showMessageEndData();
            return;
        }
        adapter.setData(requestList);
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onApplyFilter(AdminRequestFilter filter) {
        mFilter = filter;
        if (CheckNetwork.checkInternetConnection(UserRequestActivity.this)) {
            isLoading = true;
            mViewModel.clearList();
            adapter.notifyDataSetChanged();
            mViewModel.insert(null);
            adapter.notifyItemInserted(mViewModel.getRequestList().size() - 1);
            if (user == null)
                mViewModel.getLimitRequestForUser(0, 0, mNumRow, mFilter);
            else {
                mViewModel.getLimitRequestForUser(user.getId(), 0, mNumRow, mFilter);
                user = null;
            }
        }
    }

    @Override
    public void onCancelDialog() {
        mDialog = null;
    }

}