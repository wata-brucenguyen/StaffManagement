package com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity;

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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.example.staffmanagement.View.Admin.SendNotificationActivity.Service.Broadcast;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestCrudActivity.StaffRequestCrudActivity;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Staff.RequestViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StaffRequestActivity extends AppCompatActivity implements StaffRequestInterface, CallBackItemTouch {
    private CheckNetwork mCheckNetwork;
    private Toolbar toolbar;
    private RecyclerView mRecyclerViewRequestList;
    private EditText edtSearch;
    private StaffRequestListAdapter mAdapter;
    private ImageView btnNavigateToAddNewRequest;
    private ImageButton imageBtnFilter;
    private StaffRequestFilterDialog mDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final int mNumRow = Constant.NUM_ROW_ITEM_REQUEST_IN_STAFF;
    private static final int REQUEST_CODE_CREATE_REQUEST = 1;
    private static final int REQUEST_CODE_EDIT_REQUEST = 2;
    public static final String ACTION_ADD_NEW_REQUEST = "ACTION_ADD_NEW_REQUEST";
    public static final String ACTION_EDIT_REQUEST = "ACTION_EDIT_REQUEST";
    public static final String ACTION_VIEW_REQUEST = "ACTION_VIEW_REQUEST";
    private boolean isLoading = false, isShowMessageEndData = false, isSearching = false;
    private StaffRequestFilter mFilter;
    private RequestViewModel mViewModel;
    private ItemTouchHelper.Callback mCallBackItemTouch;
    private ItemTouchHelper mItemTouchHelper;
    private Broadcast mBroadcast;
    private Thread mSearchThread;
    private ChildEventListener mListener;
    private DatabaseReference mDbRef;

    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CheckNetwork.checkInternetConnection(StaffRequestActivity.this)) {
                runOnUiThread(() -> getAllStateRequest());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        mFilter = new StaffRequestFilter();
        mViewModel = ViewModelProviders.of(this).get(RequestViewModel.class);
        Intent intent = getIntent();
        String state = intent.getStringExtra("state");
        if (!TextUtils.isEmpty(state) && state.equals(StaffRequestFilter.STATE.Waiting.toString())) {
            mFilter.getStateList().add(StaffRequestFilter.STATE.Waiting);
        }
        if (!TextUtils.isEmpty(state) && state.equals(StaffRequestFilter.STATE.Accept.toString())) {
            mFilter.getStateList().add(StaffRequestFilter.STATE.Accept);
        }
        if (!TextUtils.isEmpty(state) && state.equals(StaffRequestFilter.STATE.Decline.toString())) {
            mFilter.getStateList().add(StaffRequestFilter.STATE.Decline);
        }
        mapping();
        eventRegister();
        setupToolbar();
        registerObserveDb();
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

        mDbRef.addChildEventListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcast);
        unregisterReceiver(mWifiReceiver);
        mCheckNetwork.unRegisterCheckingNetwork();
        mDbRef.removeEventListener(mListener);
        mDialog = null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CREATE_REQUEST && resultCode == RESULT_OK && data != null) {
            Request request = (Request) data.getSerializableExtra(Constant.REQUEST_DATA_INTENT);
            mViewModel.addNewRequest(request, UserSingleTon.getInstance().getUser().getId(), mFilter);
            mViewModel.getListRequest().add(0, request);
            mAdapter.notifyItemInserted(0);
            mRecyclerViewRequestList.smoothScrollToPosition(0);
        } else if (requestCode == REQUEST_CODE_EDIT_REQUEST && resultCode == RESULT_OK && data != null) {
            Request request = (Request) data.getSerializableExtra(Constant.REQUEST_DATA_INTENT);
            int pos = mViewModel.update(request);
            mAdapter.notifyItemChanged(pos);
        }
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarRequest);
        mRecyclerViewRequestList = findViewById(R.id.recyclerView_RequestList_NonAdmin);
        mRecyclerViewRequestList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        edtSearch = findViewById(R.id.editText_searchRequest_NonAdmin);
        btnNavigateToAddNewRequest = findViewById(R.id.imageView_navigate_to_add_new_request);
        imageBtnFilter = findViewById(R.id.imageButtonFilter);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    private void eventRegister() {
        GeneralFunc.setHideKeyboardOnTouch(this, findViewById(R.id.requestListStaffParent));
        onSearchChangeListener();
        btnNavigateToAddNewRequest.setOnClickListener(view -> navigateToAddRequestActivity());
        imageBtnFilter.setOnClickListener(view -> showFilterDialog());
        onScrollRecyclerView();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            if (CheckNetwork.checkInternetConnection(this)) {
                setStartForSearch();
                mViewModel.getLimitListRequestForUser(UserSingleTon.getInstance().getUser().getId(), 0, Constant.NUM_ROW_ITEM_REQUEST_IN_STAFF, mFilter);
            }
        });

        mCallBackItemTouch = new StaffRequestItemTouchHelper(this);
        mItemTouchHelper = new ItemTouchHelper(mCallBackItemTouch);
        mItemTouchHelper.attachToRecyclerView(mRecyclerViewRequestList);

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
                if (CheckNetwork.checkInternetConnection(StaffRequestActivity.this)) {
                    searchDelay();
                }
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
                        if (mAdapter != null)
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

    private void onScrollRecyclerView() {
        mRecyclerViewRequestList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        if (!isLoading && lastPosition == mViewModel.getListRequest().size() - 1 && dy > 0) {
            isLoading = true;
            mViewModel.insert(null);
            mAdapter.notifyItemInserted(mViewModel.getListRequest().size() - 1);
            if (CheckNetwork.checkInternetConnection(StaffRequestActivity.this)) {
                mViewModel.getLimitListRequestForUser(UserSingleTon.getInstance().getUser().getId(), mViewModel.getListRequest().size() - 1, mNumRow, mFilter);
            } else {
                if (mViewModel.getListRequest().size() > 0 && mViewModel.getListRequest().get(mViewModel.getListRequest().size() - 1) == null) {
                    mViewModel.delete(mViewModel.getListRequest().size() - 1);
                    mAdapter.notifyItemRemoved(mViewModel.getListRequest().size());
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

    private void navigateToAddRequestActivity() {
        Intent intent1 = new Intent(StaffRequestActivity.this, StaffRequestCrudActivity.class);
        intent1.setAction(ACTION_ADD_NEW_REQUEST);
//        Pair[] pair = new Pair[1];
//        pair[0] = new Pair<View,String>(btnNavigateToAddNewRequest,"btnToToolbar");
//        ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(this,pair);
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
        mRecyclerViewRequestList.setAdapter(mAdapter);

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
        mAdapter.setData(list);
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
        if (mViewModel.getStateRequestList().size() == 0)
            mViewModel.getAllStateRequest();
        else if (mViewModel.getListRequest() == null || mViewModel.getListRequest().size() == 0)
            setUpListRequest();
    }

    public void deleteRequest(RecyclerView.ViewHolder viewHolder, int position) {
        final Request deletedItem = mViewModel.getListRequest().get(position);
        mAdapter.deleteItem(position);
        if (deletedItem.getStateRequest().getId() != 1) {
            showMessage("Cannot delete this item, only item with waiting state can be deleted");
            mAdapter.restoreItem(deletedItem, position);
        } else {
            mViewModel.deleteRequest(deletedItem);
            String msg = "Restore item " + deletedItem.getTitle();
            Snackbar.make(findViewById(android.R.id.content), msg, BaseTransientBottomBar.LENGTH_LONG)
                    .setAction("UNDO", view -> {
                        mViewModel.restoreRequest(deletedItem);
                        mAdapter.restoreItem(deletedItem, position);
                        mRecyclerViewRequestList.smoothScrollToPosition(position);
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
        int state = mViewModel.getListRequest().get(pos).getStateRequest().getId();
        if (state != 1)
            return false;
        return true;
    }

    public void registerObserveDb() {
        mDbRef = FirebaseDatabase.getInstance()
                .getReference("database")
                .child("Request")
                .child("uid_" + String.valueOf(UserSingleTon.getInstance().getUser().getId()));
        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Request request = snapshot.getValue(Request.class);
                if (mViewModel.getListRequest().size() > 0) {
                    int flag = 0, pos = 0;
                    for (int i = 0; i < mViewModel.getListRequest().size(); i++) {
                        if (mViewModel.getListRequest().get(i).getId() == request.getId()) {
                            mViewModel.getListRequest().set(i, request);
                            mAdapter.notifyItemChanged(i);
                            mRecyclerViewRequestList.smoothScrollToPosition(i);
                            pos = i;
                            flag = 1;
                            break;
                        }
                    }

                    int finalFlag = flag;
                    int finalPos = pos;
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (finalFlag == 1) {
                            RecyclerView.ViewHolder viewHolder = mRecyclerViewRequestList.findViewHolderForAdapterPosition(finalPos);
                            View v = ((StaffRequestListAdapter.ViewHolder) viewHolder).getView();
                            Animation anim = AnimationUtils.loadAnimation(StaffRequestActivity.this, R.anim.anim_item_list_change);
                            v.startAnimation(anim);
                        } else {
                            runOnUiThread(() -> {
                                AlertDialog.Builder builder = new AlertDialog.Builder(StaffRequestActivity.this);
                                builder.setMessage("Your request : " + request.getTitle() + " was changed");
                                builder.setNegativeButton("OK", (dialogInterface, i) -> {

                                });

                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            });
                        }
                    }).start();

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }


}