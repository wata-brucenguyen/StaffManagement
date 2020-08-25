package com.example.staffmanagement.View.Admin.UserRequestActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.ViewModel.Admin.UserRequestViewModel;

import java.util.List;


public class UserRequestActivity extends AppCompatActivity implements UserRequestInterface {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);
        //overridePendingTransition(R.anim.anim_slide_out_left, R.anim.anim_slide_out_left);
        mViewModel = ViewModelProviders.of(this).get(UserRequestViewModel.class);
        mFilter = new AdminRequestFilter();
        Mapping();
        setupToolbar();
        setView();
        eventRegister();
        readListStateRequest();
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
        pullToRefresh = findViewById(R.id.swipeRefreshUserRequest);
        rvRequestList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void setupToolbar() {
        toolbar.setTitle("Request List");
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
            if (user != null) {
                mViewModel.getLimitRequestForUser(user.getId(), mViewModel.getRequestList().size() - 1, mNumRow, mFilter);
            } else {
                mViewModel.getLimitRequestForUser(0, mViewModel.getRequestList().size() - 1, mNumRow, mFilter);
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
            adapter.notifyDataSetChanged();
        });

        onScrollRecyclerView();

        mViewModel.getStateRequestListLD().observe(this,stateRequests -> {
            if(stateRequests != null && stateRequests.size() > 0){
                mViewModel.getStateRequestList().addAll(stateRequests);
                for(int i=0;i<stateRequests.size();i++){
                    mViewModel.getListRequestState().add(stateRequests.get(i).getName());
                }
                setupList();
            }
        });

        mViewModel.getRequestListLD().observe(this,requests -> {
            onLoadMoreListSuccess(requests,mViewModel.getListFullNameLD().getValue());
        });

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
        mViewModel.getListFullName().clear();
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
        mViewModel.getListFullName().clear();
        adapter.notifyDataSetChanged();
        mViewModel.insert(null);
        adapter.notifyItemInserted(mViewModel.getRequestList().size() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void setRefresh(Boolean b) {
        pullToRefresh.setRefreshing(b);
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

    public void onLoadMoreListSuccess(List<Request> requestList, List<String> userNameList) {
        if (mViewModel.getRequestList().size() > 0) {
            mViewModel.delete(mViewModel.getRequestList().size() - 1);
            adapter.notifyItemRemoved(mViewModel.getRequestList().size());
        }
        isLoading = false;
        isSearching = false;

        if ( (requestList == null || requestList.size() == 0) ) {
            if (isShowMessageEndData == false)
                showMessageEndData();
            return;
        }
        adapter.setData(requestList,userNameList);
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onApplyFilter(AdminRequestFilter filter) {
        mFilter = filter;
        isLoading = true;
        mViewModel.clearList();
        mViewModel.getListFullName().clear();
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

    @Override
    public void onCancelDialog() {
        mDialog = null;
    }


    public void readListStateRequest() {
        if (mViewModel.getStateRequestNameList() == null || mViewModel.getStateRequestNameList().size() == 0) {
            mViewModel.getAllStateRequest();
        } else
            setupList();
    }


}