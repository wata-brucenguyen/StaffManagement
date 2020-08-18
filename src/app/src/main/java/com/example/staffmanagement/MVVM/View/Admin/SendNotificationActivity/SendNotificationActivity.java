package com.example.staffmanagement.MVVM.View.Admin.SendNotificationActivity;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;

import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.MVVM.View.Admin.ViewModel.UserViewModel;

import com.example.staffmanagement.Presenter.Admin.SendNotificationPresenter;

import com.example.staffmanagement.R;

import com.example.staffmanagement.MVVM.View.Data.UserSingleTon;
import com.example.staffmanagement.MVVM.View.Notification.Service.Broadcast;
import com.example.staffmanagement.MVVM.View.Ultils.Constant;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendNotificationActivity extends AppCompatActivity implements SendNotificationInterface {

    private Toolbar toolbar;
    private RecyclerView rvUserList;
    private SendNotificationAdapter mAdapter;
    private SendNotificationPresenter mPresenter;
    private SwipeRefreshLayout pullToRefresh;
    private ProgressDialog mProgressDialog;
    private EditText edtSearch, edtQuantity;
    private CheckBox mCheckBoxAll;
    private Button mButtonSend;
    private SendNotificationDialog mDialog;


    private String searchString = "";
    private Map<String, Object> mCriteria;
    private int mNumRow = Constant.NUM_ROW_ITEM_USER_LIST_ADMIN;
    private boolean isLoading = false, isShowMessageEndData = false;
    private UserViewModel mViewModel;
    private Broadcast mBroadcast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AdminAppTheme);
        setContentView(R.layout.activity_admin_send_notification);
        mapping();
        setupToolbar();
        setUpLinearLayout();

        mPresenter = new SendNotificationPresenter(this, this);
        mViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        getAllRole();
        eventRegister();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBroadcast = new Broadcast();
        IntentFilter filter = new IntentFilter("Notification");
        registerReceiver(mBroadcast, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcast);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialog = null;
    }

    @Override
    public void setupList() {
        packetDataFilter();

        isLoading = true;
        mViewModel.clearList();
        mAdapter = new SendNotificationAdapter(this, mViewModel, this);
        rvUserList.setAdapter(mAdapter);

        mViewModel.insert(null);
        mAdapter.notifyItemInserted(mViewModel.getUserList().size() - 1);
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
    public void onLoadMoreListSuccess(List<User> list) {
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

        mAdapter.setData(list);
        edtQuantity.setText("0/" + mViewModel.getUserList().size());
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
                mPresenter.getLimitListUser(UserSingleTon.getInstance().getUser().getId(), mViewModel.getUserList().size() - 1, mNumRow, mCriteria);
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
    public void getAllRole() {
        if (mViewModel.getRoleList().isEmpty())
            mPresenter.getAllRole();
        else
            setupList();
    }

    @Override
    public void onSuccessGetAllRole(List<Role> roles) {
        mViewModel.addNewRoleList(roles);
        setupList();
    }

    @Override
    public void onCancelDialog() {
        this.mDialog = null;
    }

    @Override
    public void setCheckAll(boolean b) {
        mCheckBoxAll.setChecked(b);
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
        edtSearch = findViewById(R.id.searchView);
        edtQuantity = findViewById(R.id.editText_Quantity);
        mCheckBoxAll = findViewById(R.id.checkBoxAll);
        mButtonSend = findViewById(R.id.btnSend);
        mCheckBoxAll = findViewById(R.id.checkBoxAll);
    }


    private void eventRegister() {
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(false);
                mCheckBoxAll.setChecked(false);
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
                mAdapter.notifyDataSetChanged();
                mViewModel.insert(null);
                mAdapter.notifyItemInserted(mViewModel.getUserList().size() - 1);
                mPresenter.getLimitListUser(UserSingleTon.getInstance().getUser().getId(), 0, mNumRow, mCriteria);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initScrollListener();
        sendToAll();
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mViewModel.getUserCheckList() == null || mViewModel.getUserCheckList().size() == 0){
                    showMessage("Please choose the person you want to send");
                }else
                showSendNotificationDialog();
            }
        });

    }

    private void showSendNotificationDialog() {
        mDialog = new SendNotificationDialog(this,mViewModel);
        mDialog.show(getSupportFragmentManager(), null);
    }

    private void packetDataFilter() {
        mCriteria = new HashMap<>();
        mCriteria.put(Constant.SEARCH_NAME_IN_ADMIN, searchString);
    }



    private void sendToAll() {
        mCheckBoxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCheckBoxAll.isChecked()) {
                    mAdapter.selectAll();
                    edtQuantity.setText(mViewModel.getUserCheckList().size() + "/" + mViewModel.getUserList().size());
                } else {
                    mAdapter.unSelectedAll();
                    edtQuantity.setText(mViewModel.getUserCheckList().size() + "/" + mViewModel.getUserList().size());
                }
            }
        });

    }

    @Override
    public void loadBottomSheetDialog(User user){
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
        String s = mViewModel.getUserCheckList().size()+ "/" + mViewModel.getUserList().size();
        edtQuantity.setText(s);
    }


}