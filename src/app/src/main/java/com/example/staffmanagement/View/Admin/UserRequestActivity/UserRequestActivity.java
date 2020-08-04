package com.example.staffmanagement.View.Admin.UserRequestActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Presenter.Admin.UserRequestPresenter;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.ViewModel.UserRequestViewModel;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Ultils.Constant;


import java.util.ArrayList;
import java.util.List;


public class UserRequestActivity extends AppCompatActivity implements UserRequestInterface {
    private Toolbar toolbar;
    private RecyclerView rvRequestList;
    private ImageButton imgBtnFilter;
    //private ArrayList<Request> arrayListRequest;
    private UserRequestApdater adapter;
    private UserRequestPresenter mPresenter;
    private SwipeRefreshLayout pullToRefresh;
    private EditText edtSearchRequest;
    private User user;
    private ProgressDialog mProgressDialog;
    private final int mNumRow = Constant.NUM_ROW_ITEM_REQUEST_IN_STAFF;
    private String searchString = "";
    private AdminUserRequestDialog mDialog;
    private boolean isLoading = false, isShowMessageEndData = false, isSearching = false;
    private AdminRequestFilter mFilter;
    private UserRequestInterface userRequestInterface;
    private UserRequestViewModel userRequestVM;
    private List<String> arrayListRequestState;
    private List<StateRequest> stateRequestArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);
        // overridePendingTransition(R.anim.anim_slide_out_left, R.anim.anim_slide_out_left);
        userRequestVM = ViewModelProviders.of(this).get(UserRequestViewModel.class);
        mPresenter = new UserRequestPresenter(this, this);
        mFilter = new AdminRequestFilter();
        Mapping();
        eventRegister();
        setupToolbar();
        // WeakReference<Context> m = new WeakReference<>(getApplicationContext());
        setView();
        readListStateRequest();
    }


    private void setObserveFilter() {
        userRequestVM.getRequestListObserver().observe(this, new Observer<List<Request>>() {
            @Override
            public void onChanged(List<Request> requests) {
                adapter.notifyDataSetChanged();
            }
        });
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
        rvRequestList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
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
        if (isLoading == false && lastPosition == userRequestVM.getRequestList().size() - 1 && dy > 0) {
            isLoading = true;
            userRequestVM.insert(null);
            showMessage("scroll");
            if (user != null) {
                mPresenter.getLimitListRequestForUser(user.getId(), userRequestVM.getRequestList().size() - 1, mNumRow, mFilter);
            } else {
                mPresenter.getLimitListRequestForUser(0, userRequestVM.getRequestList().size() - 1, mNumRow, mFilter);
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
                if (!isSearching) {
                    mFilter.setSearchString(String.valueOf(charSequence));
                    setStartForSearch();
                    if (user == null)
                        mPresenter.getLimitListRequestForUser(0, 0, mNumRow, mFilter);
                    else {
                        mPresenter.getLimitListRequestForUser(user.getId(), 0, mNumRow, mFilter);
                        user = null;
                    }
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
        setObserveFilter();
    }
    private void setupList() {
        isLoading = true;
        adapter = new UserRequestApdater(this, userRequestVM.getRequestList(), arrayListRequestState, stateRequestArrayList, this);
        rvRequestList.setAdapter(adapter);
        userRequestVM.clearList();
        userRequestVM.getRequestList().add(null);

        if (user == null)
            mPresenter.getLimitListRequestForUser(0, 0, mNumRow, mFilter);
        else
            mPresenter.getLimitListRequestForUser(user.getId(), 0, mNumRow, mFilter);
    }


    private void setStartForSearch() {
        isLoading = true;
        isSearching = true;
        userRequestVM.clearList();
        userRequestVM.insert(null);
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
    public void onLoadMoreListSuccess(List<Request> arrayList) {
        if (userRequestVM.getRequestList().size() > 0 ) {
           userRequestVM.delete(userRequestVM.getRequestList().size() - 1);
        }
        isLoading = false;
        isSearching = false;

        if (arrayList == null || arrayList.size() == 0) {
            if (isShowMessageEndData == false)
                showMessageEndData();
            return;
        }
        userRequestVM.addRange(arrayList);
        checkSearchChangeToSearchAgain();
        mPresenter.destroyBus();
    }

    private void checkSearchChangeToSearchAgain() {
        if (!edtSearchRequest.getText().toString().equals(mFilter.getSearchString()) && !isSearching) {
            setStartForSearch();
            mPresenter.getLimitListRequestForUser(UserSingleTon.getInstance().getUser().getId(), 0, mNumRow, mFilter);
        }
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

    @Override
    public void onAddNewRequestSuccessfully(Request newItem) {
        userRequestVM.insert(newItem);
        showMessage("Add successfully");
    }

    @Override
    public void onUpdateRequestSuccessfully(Request item) {
        showMessage("Update successfully");
    }

    @Override
    public void onApplyFilter(AdminRequestFilter filter) {
        mFilter = filter;
        isLoading = true;
        userRequestVM.clearList();
        userRequestVM.insert(null);

        if (user == null)
            mPresenter.getLimitListRequestForUser(0, 0, mNumRow, mFilter);
        else {
            mPresenter.getLimitListRequestForUser(user.getId(), 0, mNumRow, mFilter);
            user = null;
        }
    }


    @Override
    public void readListStateRequest() {
        if(arrayListRequestState == null ){
            arrayListRequestState = new ArrayList<>();
            stateRequestArrayList = new ArrayList<>();
            mPresenter.getAllStateRequest();
        } else
            setupList();
    }

    @Override
    public void onSuccessGetAllStateRequest(List<StateRequest> list) {
        stateRequestArrayList.addAll(list);
        for (int i = 0; i < stateRequestArrayList.size(); i++) {
            arrayListRequestState.add(stateRequestArrayList.get(i).getName());
        }
        setupList();
    }

    @Override
    public void getFullNameById(int idUser, UserRequestApdater.ViewHolder holder) {
        mPresenter.getFullNameById(idUser,holder);
    }

    @Override
    public void onSuccessFullNameById(int idUser, String fullName, UserRequestApdater.ViewHolder holder) {
        holder.getTxtName().setText(fullName);
    }

    @Override
    public void update(Request request) {
        //userRequestVM.update(request);
        userRequestVM.updateState(request);
        mPresenter.update(request);
    }
}