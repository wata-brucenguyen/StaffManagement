package com.example.staffmanagement.View.Staff.RequestManagement.RequestCrudActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.Rule;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Notification.Service.Broadcast;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestActivity;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Staff.ScreenAddRequestViewModel;

import java.util.Date;

public class StaffRequestCrudActivity extends AppCompatActivity {

    private EditText edtContent,edtTitle;
    private Toolbar toolbar;
    private String action;
    private TextView txtTime;
    private Request mRequest;
    private Broadcast mBroadcast;
    private ScreenAddRequestViewModel mViewModel;
    private ProgressDialog mProgressDialog;
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

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcast);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_crud_request_non_admin,menu);
        checkEditAction(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_menu_apply_request_crud_non_admin:
                if(GeneralFunc.checkInternetConnection(StaffRequestCrudActivity.this)){
                    mProgressDialog = new ProgressDialog(StaffRequestCrudActivity.this);
                    mProgressDialog.setMessage("Checking...");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    mViewModel.checkRuleForAddRequest();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mapping(){
        edtTitle = findViewById(R.id.editText_title_request_non_admin);
        edtContent = findViewById(R.id.editText_content_request_non_admin);
        toolbar = findViewById(R.id.toolbar);
        txtTime = findViewById(R.id.textView_timeCreate);
    }

    private void eventRegister(){
        mViewModel.getError().observe(this,error_add_request -> {
            switch (error_add_request){
                case OVER_LIMIT:
                    if(mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(StaffRequestCrudActivity.this);
                    builder.setTitle("Warning");
                    builder.setMessage("Your number of request over limit, are you sure to add new request, it can be decline");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Request request = getInputRequest();
                            request.setIdState(3);
                            if(request != null){
                                Intent data = new Intent();
                                data.putExtra(Constant.REQUEST_DATA_INTENT,request);
                                setResult(RESULT_OK,data);
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
                    if(mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    Request request = getInputRequest();
                    if(request != null){
                        Intent data = new Intent();
                        data.putExtra(Constant.REQUEST_DATA_INTENT,request);
                        setResult(RESULT_OK,data);
                        finish();
                    }
                    break;
                case NETWORK_ERROR:
                    if(mProgressDialog != null && mProgressDialog.isShowing())
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

    private void setupToolBar(){
        setSupportActionBar(toolbar);
        if( action.equals(StaffRequestActivity.ACTION_ADD_NEW_REQUEST))
            toolbar.setTitle("Add new request");
        else{
            toolbar.setTitle("Edit request");
            setDataOnView();
        }

        toolbar.setNavigationOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void checkEditAction(Menu menu){
        menu.findItem(R.id.option_menu_apply_request_crud_non_admin).setEnabled(true);
        menu.findItem(R.id.option_menu_apply_request_crud_non_admin).getIcon().setAlpha(200);
        if(action.equals(StaffRequestActivity.ACTION_EDIT_REQUEST) && mRequest != null && mRequest.getIdState() != 1 ) {
            menu.findItem(R.id.option_menu_apply_request_crud_non_admin).setEnabled(false);
            menu.findItem(R.id.option_menu_apply_request_crud_non_admin).getIcon().setAlpha(0);
        }
    }

    private Request getInputRequest(){
        String title = edtTitle.getText().toString();
        String content = edtContent.getText().toString();
        if(TextUtils.isEmpty(title)){
            showMessage("Title is empty");
            edtTitle.requestFocus();
            return null;
        }
        if(TextUtils.isEmpty(content)){
            showMessage("Content is empty");
            edtContent.requestFocus();
            return null;
        }

        Date date = new Date();
        Request request = new Request(0, UserSingleTon.getInstance().getUser().getId(),1,title,content, date.getTime());
        if( action.equals(StaffRequestActivity.ACTION_EDIT_REQUEST))
            request.setId(mRequest.getId());

        return request;
    }

    private void showMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void setDataOnView(){
        edtTitle.setText(mRequest.getTitle());
        edtContent.setText(mRequest.getContent());
        txtTime.setText(GeneralFunc.convertMilliSecToDateString(mRequest.getDateTime()));
        checkStateRequest();
    }

    private void checkStateRequest(){
        if(mRequest.getIdState() != 1){
            edtContent.setFocusable(false);
            edtTitle.setFocusable(false);
        }
    }

    private void getDataIntentEdit(){
        action = getIntent().getAction();
        if( action.equals(StaffRequestActivity.ACTION_EDIT_REQUEST))
            mRequest = (Request) getIntent().getSerializableExtra(Constant.REQUEST_DATA_INTENT);
    }
}