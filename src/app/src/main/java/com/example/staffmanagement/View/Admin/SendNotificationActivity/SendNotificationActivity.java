package com.example.staffmanagement.View.Admin.SendNotificationActivity;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.staffmanagement.Model.LocalDb.Database.Entity.Role;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.User;

import com.example.staffmanagement.Presenter.Admin.SendNotificationPresenter;

import com.example.staffmanagement.R;

import com.example.staffmanagement.View.Admin.ViewModel.UserViewModel;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Notification.CrudGroup.APIGroup;
import com.example.staffmanagement.View.Notification.Sender.APIService;
import com.example.staffmanagement.View.Notification.Sender.Client;
import com.example.staffmanagement.View.Notification.Service.Broadcast;
import com.example.staffmanagement.View.Ultils.Constant;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendNotificationActivity extends AppCompatActivity implements SendNotificationInterface{

    private Toolbar toolbar;
    private RecyclerView rvUserList;
    private SendNotificationAdapter mAdapter;
    private SendNotificationPresenter mPresenter;
    private SwipeRefreshLayout pullToRefresh;
    private ProgressDialog mProgressDialog;
    private EditText edtSearch;
    private CheckBox mCheckBoxAll;
    private Button mButtonSend;

    private String searchString = "";
    private Map<String, Object> mCriteria;
    private int mNumRow = Constant.NUM_ROW_ITEM_USER_LIST_ADMIN;
    private boolean isLoading = false, isShowMessageEndData = false;
    private UserViewModel mViewModel;

    private Broadcast mBroadcast;
    private APIService apiService;
    private APIGroup apiGroup;

    private String notification_key_name = "GroupSend";
    private List<String> mListCheck = new ArrayList<>();
    private String []registration_ids = new String[]{};


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
    public void onLoadMoreListSuccess(ArrayList<User> list) {
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
        mCheckBoxAll = findViewById(R.id.checkBoxAll);
        mButtonSend = findViewById(R.id.btnSend);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        apiGroup = Client.getClient("https://fcm.googleapis.com/").create(APIGroup.class);
    }


    private void eventRegister() {
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
                mPresenter.getLimitListUser(UserSingleTon.getInstance().getUser().getId(), 0, mNumRow, mCriteria);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initScrollListener();
    }

    private void packetDataFilter() {
        mCriteria = new HashMap<>();
        mCriteria.put(Constant.SEARCH_NAME_IN_ADMIN, searchString);
    }

    private void loadToken(){
        for(int i=0 ; i < mListCheck.size() ; i++){
           registration_ids[i] = String.valueOf(mViewModel.getUserCheckList().get(i).getId());
        }
    }

}