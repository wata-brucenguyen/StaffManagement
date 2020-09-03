package com.example.staffmanagement.View.Staff.RequestManagement.RequestCrudActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Notification.Sender.APIService;
import com.example.staffmanagement.View.Notification.Sender.Data;
import com.example.staffmanagement.View.Notification.Sender.DataStaffRequest;
import com.example.staffmanagement.View.Notification.Sender.MyResponse;
import com.example.staffmanagement.View.Notification.Sender.NotificationSender;
import com.example.staffmanagement.View.Notification.Service.Broadcast;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestActivity;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Staff.ScreenAddRequestViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffRequestCrudActivity extends AppCompatActivity {

    private CheckNetwork mCheckNetwork;
    private EditText edtContent, edtTitle;
    private Toolbar toolbar;
    private String action;
    private TextView txtTime;
    private Request mRequest;
    private Broadcast mBroadcast;
    private ScreenAddRequestViewModel mViewModel;
    private ProgressDialog mProgressDialog;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setTheme(R.style.StaffAppTheme);
        mViewModel = ViewModelProviders.of(this).get(ScreenAddRequestViewModel.class);
        getDataIntentEdit();
        setContentView(R.layout.activity_request_crud);
        mapping();
        setupToolBar();
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcast);
        mCheckNetwork.unRegisterCheckingNetwork();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_crud_request_non_admin, menu);
        checkEditAction(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_menu_apply_request_crud_non_admin:
                if (CheckNetwork.checkInternetConnection(StaffRequestCrudActivity.this)) {
                    if (action.equals(StaffRequestActivity.ACTION_ADD_NEW_REQUEST)) {
                        mProgressDialog = new ProgressDialog(StaffRequestCrudActivity.this);
                        mProgressDialog.setMessage("Checking...");
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        mProgressDialog.show();
                        mViewModel.checkRuleForAddRequest(UserSingleTon.getInstance().getUser().getId());
                    } else {
                        mViewModel.getError().postValue(ScreenAddRequestViewModel.ERROR_ADD_REQUEST.PASS);
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mapping() {
        edtTitle = findViewById(R.id.editText_title_request_non_admin);
        edtContent = findViewById(R.id.editText_content_request_non_admin);
        toolbar = findViewById(R.id.toolbar);
        txtTime = findViewById(R.id.textView_timeCreate);
    }

    private void eventRegister() {
        GeneralFunc.setHideKeyboardOnTouch(this, findViewById(R.id.AddRequest));
        mViewModel.getError().observe(this, error_add_request -> {
            switch (error_add_request) {
                case OVER_LIMIT:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(StaffRequestCrudActivity.this);
                    builder.setTitle("Warning");
                    builder.setMessage("Your number of request over limit, are you sure to add new request, it can be declined");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Request request = getInputRequest();
                            request.setStateRequest(new StateRequest(3, "Decline"));
                            if (request != null) {
                                Intent data = new Intent();
                                data.putExtra(Constant.REQUEST_DATA_INTENT, request);
                                setResult(RESULT_OK, data);
                                finish();
                            }
                        }
                    });

                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    break;

                case PASS:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    Request request = getInputRequest();
                    if (request != null) {
                        Intent data = new Intent();
                        data.putExtra(Constant.REQUEST_DATA_INTENT, request);
                        setResult(RESULT_OK, data);
                        sendMessageToAdmin(request.getId());
                        finish();
                    }
                    break;
                case NETWORK_ERROR:
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(StaffRequestCrudActivity.this);
                    builder1.setTitle("NETWORK ERROR");
                    builder1.setMessage("Cannot add new request");
                    builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog1 = builder1.create();
                    alertDialog1.show();
                    break;

            }
        });
    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);
        if (action.equals(StaffRequestActivity.ACTION_ADD_NEW_REQUEST))
            toolbar.setTitle("Add new request");
        else {
            toolbar.setTitle("Edit request");
            setDataOnView();
        }

        toolbar.setNavigationOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void checkEditAction(Menu menu) {
        menu.findItem(R.id.option_menu_apply_request_crud_non_admin).setEnabled(true);
        menu.findItem(R.id.option_menu_apply_request_crud_non_admin).getIcon().setAlpha(200);
        if (action.equals(StaffRequestActivity.ACTION_EDIT_REQUEST) && mRequest != null && mRequest.getStateRequest().getId() != 1) {
            menu.findItem(R.id.option_menu_apply_request_crud_non_admin).setEnabled(false);
            menu.findItem(R.id.option_menu_apply_request_crud_non_admin).getIcon().setAlpha(0);
        }
    }

    private Request getInputRequest() {
        String title = edtTitle.getText().toString();
        String content = edtContent.getText().toString();
        if (TextUtils.isEmpty(title)) {
            showMessage("Title is empty");
            edtTitle.requestFocus();
            return null;
        }
        if (TextUtils.isEmpty(content)) {
            showMessage("Content is empty");
            edtContent.requestFocus();
            return null;
        }

        Date date = new Date();
        Request request = new Request(0, UserSingleTon.getInstance().getUser().getId(), title, content, date.getTime(), new StateRequest(1, "Waiting"), UserSingleTon.getInstance().getUser().getFullName());
        if (action.equals(StaffRequestActivity.ACTION_EDIT_REQUEST))
            request.setId(mRequest.getId());

        return request;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setDataOnView() {
        edtTitle.setText(mRequest.getTitle());
        edtContent.setText(mRequest.getContent());
        txtTime.setText(GeneralFunc.convertMilliSecToDateString(mRequest.getDateTime()));
        checkStateRequest();
    }

    private void checkStateRequest() {
        if (mRequest.getStateRequest().getId() != 1) {
            edtContent.setFocusable(false);
            edtTitle.setFocusable(false);
        }
    }

    private void getDataIntentEdit() {
        action = getIntent().getAction();
        if (action.equals(StaffRequestActivity.ACTION_EDIT_REQUEST))
            mRequest = (Request) getIntent().getSerializableExtra(Constant.REQUEST_DATA_INTENT);
    }

    private void sendNotificationStaffRequest(String userToken, String title, String message, String type, int idRequest) {
        Data data = new DataStaffRequest(title, message, type, idRequest);
        NotificationSender sender = new NotificationSender(data, userToken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    private void sendMessageToAdmin(int idRequest) {
        final List<String> listUserToken = new ArrayList<>();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tokens");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    for (DataSnapshot data : d.getChildren()) {
                        String userToken = data.getValue(String.class);
                        listUserToken.add(userToken);
                    }
                }
                for (String s : listUserToken)
                    sendNotificationStaffRequest(s, "New request"
                            , "You have new request from " + UserSingleTon.getInstance().getUser().getFullName()
                            , "request"
                            , idRequest);

                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}