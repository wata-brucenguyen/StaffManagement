package com.example.staffmanagement.MVVM.View.Admin.SendNotificationActivity;

import android.content.IntentFilter;
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

import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.MVVM.View.Data.UserSingleTon;
import com.example.staffmanagement.MVVM.View.Notification.Service.Broadcast;
import com.example.staffmanagement.MVVM.View.Ultils.Constant;
import com.example.staffmanagement.MVVM.ViewModel.Admin.UserListViewModel;
import com.example.staffmanagement.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendNotificationActivity extends AppCompatActivity implements SendNotificationInterface {

    private Toolbar toolbar;
    private RecyclerView rvUserList;
    private SendNotificationAdapter mAdapter;
    private SwipeRefreshLayout pullToRefresh;
    private EditText edtSearch, edtQuantity;
    private CheckBox mCheckBoxAll;
    private Button mButtonSend;
    private SendNotificationDialog mDialog;

    private String searchString = "";
    private Map<String, Object> mCriteria;
    private int mNumRow = Constant.NUM_ROW_ITEM_USER_LIST_ADMIN;
    private boolean isLoading = false, isShowMessageEndData = false;
    private UserListViewModel mViewModel;
    private Broadcast mBroadcast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AdminAppTheme);
        setContentView(R.layout.activity_admin_send_notification);

        mapping();
        setupToolbar();
        setUpLinearLayout();

        mViewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
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

    private void setupList() {
        packetDataFilter();

        isLoading = true;
        mViewModel.clearList();
        mAdapter = new SendNotificationAdapter(this, mViewModel, this);
        rvUserList.setAdapter(mAdapter);

        mViewModel.insert(null);
        mAdapter.notifyItemInserted(mViewModel.getUserList().size() - 1);
        mViewModel.getLimitListUser(UserSingleTon.getInstance().getUser().getId(), 0, mNumRow, mCriteria);
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
        if (list == null || list.size() == 0) {
            if (isShowMessageEndData == false)
                showMessageEndData();
            return;
        }

        mAdapter.setData(list);
        edtQuantity.setText(mViewModel.getUserCheckList().size() + "/" + mViewModel.getCountStaff());

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
            edtQuantity.setText(mViewModel.getUserCheckList().size() + "/" + mViewModel.getCountStaff());
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

    public void getAllRole() {
        if (mViewModel.getRoleList().isEmpty())
            mViewModel.getAllRole();
        else
            setupList();
    }

    @Override
    public void setCheckAll(boolean b) {
        mCheckBoxAll.setChecked(b);
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
                mViewModel.getLimitListUser(UserSingleTon.getInstance().getUser().getId(), 0, mNumRow, mCriteria);
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

        mViewModel.getListRoleLD().observe(this, roles -> {
            if (roles != null && roles.size() > 0) {
                mViewModel.getRoleList().addAll(roles);
                setupList();
            }
        });

        mViewModel.getUserListLD().observe(this, this::onLoadMoreListSuccess);

    }

    private void showSendNotificationDialog() {
        mDialog = new SendNotificationDialog(this, mViewModel);
        mDialog.show(getSupportFragmentManager(), null);
    }

    private void packetDataFilter() {
        mCriteria = new HashMap<>();
        mCriteria.put(Constant.SEARCH_NAME_IN_ADMIN, searchString);
    }


    private void sendToAll() {
        mCheckBoxAll.setOnClickListener(view -> {
            if (mCheckBoxAll.isChecked()) {
                mAdapter.selectAll();
                edtQuantity.setText(mViewModel.getAllStaff().size() + "/" + mViewModel.getCountStaff());
            } else {
                mAdapter.unSelectedAll();
                edtQuantity.setText(mViewModel.getUserCheckList().size()+ "/" + mViewModel.getCountStaff());
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
        String s = mViewModel.getUserCheckList().size() + "/" + mViewModel.getCountStaff();
        edtQuantity.setText(s);
    }
}