package com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity;

import android.app.Dialog;
import android.os.Build;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.StaffRequestFilter;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Notification.Service.Broadcast;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestCrudActivity.StaffRequestCrudActivity;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Staff.RequestViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class StaffRequestActivity extends AppCompatActivity implements StaffRequestInterface, CallBackItemTouch {
    private Toolbar toolbar;
    private RecyclerView rvRequestList;
    private EditText edtSearch;
    private StaffRequestListAdapter mAdapter;
    private ImageView btnNavigateToAddNewRequest;
    private StaffRequestFilterDialog mDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final int mNumRow = Constant.NUM_ROW_ITEM_REQUEST_IN_STAFF;
    private static final int REQUEST_CODE_CREATE_REQUEST = 1;
    private static final int REQUEST_CODE_EDIT_REQUEST = 2;
    public static final String ACTION_ADD_NEW_REQUEST = "ACTION_ADD_NEW_REQUEST";
    public static final String ACTION_EDIT_REQUEST = "ACTION_EDIT_REQUEST";
    private boolean isLoading = false, isShowMessageEndData = false, isSearching = false;
    private StaffRequestFilter mFilter;
    private RequestViewModel mViewModel;
    private ItemTouchHelper.Callback mCallBackItemTouch;
    private ItemTouchHelper mItemTouchHelper;
    private Broadcast mBroadcast;
    private Thread mSearchThread;

    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            if (WifiManager.WIFI_STATE_ENABLED == wifiState) {
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       int time = 0;
                       while(!GeneralFunc.checkInternetConnectionNoToast(StaffRequestActivity.this)){
                           try {
                               Thread.sleep(1000);
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                           time = time + 1;
                           if(time == 15){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(StaffRequestActivity.this,"No network to fetch data, please reconnect internet again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                               return;
                           }

                       }

                       runOnUiThread(() -> getAllStateRequest());
                   }
               }).start();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        mFilter = new StaffRequestFilter();
        mViewModel = ViewModelProviders.of(this).get(RequestViewModel.class);
        Intent intent=getIntent();
        String state=intent.getStringExtra("state");
        if(!TextUtils.isEmpty(state) && state.equals(StaffRequestFilter.STATE.Waiting.toString())){
            mFilter.getStateList().add(StaffRequestFilter.STATE.Waiting);
        }
        if(!TextUtils.isEmpty(state) && state.equals(StaffRequestFilter.STATE.Accept.toString())){
            mFilter.getStateList().add(StaffRequestFilter.STATE.Accept);
        }
        if(!TextUtils.isEmpty(state) && state.equals(StaffRequestFilter.STATE.Decline.toString())){
            mFilter.getStateList().add(StaffRequestFilter.STATE.Decline);
        }
        mapping();
        if (GeneralFunc.checkInternetConnection(this))
            getAllStateRequest();
        eventRegister();
        setupToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBroadcast = new Broadcast();
        IntentFilter filter = new IntentFilter("Notification");
        registerReceiver(mBroadcast, filter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(mWifiReceiver, intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcast);
        unregisterReceiver(mWifiReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialog = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_staff_request_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_menu_item_staff_request_filter:
                showFilterDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CREATE_REQUEST && resultCode == RESULT_OK && data != null) {
            Request request = (Request) data.getSerializableExtra(Constant.REQUEST_DATA_INTENT);
            mViewModel.addNewRequest(request, UserSingleTon.getInstance().getUser().getId(), mFilter);
        } else if (requestCode == REQUEST_CODE_EDIT_REQUEST && resultCode == RESULT_OK && data != null) {
            Request request = (Request) data.getSerializableExtra(Constant.REQUEST_DATA_INTENT);
            int pos = mViewModel.update(request);
            mAdapter.notifyItemChanged(pos);
        }
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarRequest);
        rvRequestList = findViewById(R.id.recyclerView_RequestList_NonAdmin);
        rvRequestList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        edtSearch = findViewById(R.id.editText_searchRequest_NonAdmin);
        btnNavigateToAddNewRequest = findViewById(R.id.imageView_navigate_to_add_new_request);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    private void eventRegister() {
        onSearchChangeListener();
        btnNavigateToAddNewRequest.setOnClickListener(view -> navigateToAddRequestActivity());

        onScrollRecyclerView();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            if(GeneralFunc.checkInternetConnection(StaffRequestActivity.this)){
                setStartForSearch();
                mViewModel.getLimitListRequestForUser(UserSingleTon.getInstance().getUser().getId(), 0, Constant.NUM_ROW_ITEM_REQUEST_IN_STAFF, mFilter);
            }
        });

        mCallBackItemTouch = new StaffRequestItemTouchHelper(this);
        mItemTouchHelper = new ItemTouchHelper(mCallBackItemTouch);
        mItemTouchHelper.attachToRecyclerView(rvRequestList);

        mViewModel.getRequestListLD().observe(this, requests ->
                onLoadMoreListSuccess((ArrayList<Request>) requests)

        );

        mViewModel.getStateRequestListLD().observe(this, stateRequests -> {
                    if (stateRequests != null && stateRequests.size() > 0) {
                        mViewModel.insertNewStateRequestList(stateRequests);
                        setUpListRequest();
                    }
                }
        );

    }

    private void onSearchChangeListener() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mFilter.setSearchString(String.valueOf(charSequence));
                if(GeneralFunc.checkInternetConnection(StaffRequestActivity.this))
                    searchDelay();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
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
                        if(mAdapter != null)
                        setStartForSearch();
                        mViewModel.getLimitListRequestForUser(UserSingleTon.getInstance().getUser().getId(), 0, Constant.NUM_ROW_ITEM_REQUEST_IN_STAFF, mFilter);
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
        mAdapter.notifyDataSetChanged();
        mViewModel.insert(null);
        mAdapter.notifyItemInserted(0);
    }

    private void checkSearchChangeToSearchAgain() {
        if (!edtSearch.getText().toString().equals(mFilter.getSearchString()) && !isSearching) {
            setStartForSearch();
            mViewModel.getLimitListRequestForUser(UserSingleTon.getInstance().getUser().getId(), 0, Constant.NUM_ROW_ITEM_REQUEST_IN_STAFF, mFilter);
        }
    }

    private void onScrollRecyclerView() {
        rvRequestList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(GeneralFunc.checkInternetConnection(StaffRequestActivity.this))
                    loadMore(recyclerView, dy);
            }
        });
    }

    private void loadMore(RecyclerView recyclerView, int dy) {
        LinearLayoutManager ll = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastPosition = ll.findLastVisibleItemPosition();
        if (!isLoading && lastPosition == mViewModel.getListRequest().size() - 1 && dy > 0) {
            isLoading = true;
            mViewModel.insert(null);
            mAdapter.notifyItemInserted(mViewModel.getListRequest().size() - 1);
            mViewModel.getLimitListRequestForUser(UserSingleTon.getInstance().getUser().getId(), mViewModel.getListRequest().size() - 1, mNumRow, mFilter);
        }
    }

    private void navigateToAddRequestActivity() {
        Intent intent1 = new Intent(StaffRequestActivity.this, StaffRequestCrudActivity.class);
        intent1.setAction(ACTION_ADD_NEW_REQUEST);
        startActivityForResult(intent1, REQUEST_CODE_CREATE_REQUEST);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Request List");
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void setUpListRequest() {
        isLoading = true;

        mViewModel.clearList();
        mViewModel.insert(null);
        mAdapter = new StaffRequestListAdapter(this, mViewModel);
        rvRequestList.setAdapter(mAdapter);

        // add loading
        mAdapter.notifyItemInserted(0);
        mViewModel.getLimitListRequestForUser(UserSingleTon.getInstance().getUser().getId(), 0, mNumRow, mFilter);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void onLoadMoreListSuccess(ArrayList<Request> list) {
        if (mViewModel.getListRequest().size() > 0 && mViewModel.getListRequest().get(mViewModel.getListRequest().size() - 1) == null) {
            mViewModel.delete(mViewModel.getListRequest().size() - 1);
            mAdapter.notifyItemRemoved(mViewModel.getListRequest().size());
        }
        isLoading = false;
        isSearching = false;
        if (list == null || list.size() == 0) {
            if (!isShowMessageEndData)
                showMessageEndData();
            return;
        }
        //mViewModel.addRange(list);
        mAdapter.setData(list);
        checkSearchChangeToSearchAgain();
    }

    @Override
    public void onApplyFilter(StaffRequestFilter filter) {
        mFilter = filter;
        this.mDialog = null;
        setStartForSearch();
        mViewModel.getLimitListRequestForUser(UserSingleTon.getInstance().getUser().getId(), 0, Constant.NUM_ROW_ITEM_REQUEST_IN_STAFF, mFilter);
    }

    @Override
    public void onCancelFilter() {
        this.mDialog = null;
    }

    public void getAllStateRequest() {
        if (mViewModel.getStateRequestList().size()==0)
            mViewModel.getAllStateRequest();
        else
            setUpListRequest();
    }

    public void deleteRequest(RecyclerView.ViewHolder viewHolder, int position) {
        final Request deletedItem = mViewModel.getListRequest().get(position);
        mAdapter.deleteItem(position);
        if (deletedItem.getIdState() != 1) {
            showMessage("Cannot delete this item, only item with waiting state can be deleted");
            mAdapter.restoreItem(deletedItem, position);
        } else {
            mViewModel.deleteRequest(deletedItem);
            String msg = "Restore item " + deletedItem.getTitle();
            Snackbar.make(findViewById(android.R.id.content), msg, BaseTransientBottomBar.LENGTH_LONG)
                    .setAction("UNDO", view -> {
                        mViewModel.restoreRequest(deletedItem);
                        mAdapter.restoreItem(deletedItem, position);
                        rvRequestList.smoothScrollToPosition(position);
                    })
                    .setActionTextColor(Color.GREEN)
                    .show();
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

    public static int getRequestCodeEdit() {
        return REQUEST_CODE_EDIT_REQUEST;
    }

    private void showFilterDialog() {
        mDialog = new StaffRequestFilterDialog(mFilter, this);
        mDialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void itemTouchOnMove(int oldPosition, int newPosition) {
        mAdapter.swapItem(oldPosition, newPosition);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, final int position) {
        deleteRequest(viewHolder, position);
    }

    @Override
    public boolean checkStateRequest(RecyclerView.ViewHolder viewHolder) {
        int pos = viewHolder.getAdapterPosition();
        int state = mViewModel.getListRequest().get(pos).getIdState();
        if (state != 1)
            return false;
        return true;
    }

}