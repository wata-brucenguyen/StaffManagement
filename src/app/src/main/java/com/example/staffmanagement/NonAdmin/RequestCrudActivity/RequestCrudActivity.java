package com.example.staffmanagement.NonAdmin.RequestCrudActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.staffmanagement.Admin.Const;
import com.example.staffmanagement.Database.Data.UserSingleTon;
import com.example.staffmanagement.Database.Entity.Request;
import com.example.staffmanagement.NonAdmin.RequestActivity.RequestActivity;
import com.example.staffmanagement.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestCrudActivity extends AppCompatActivity {

    private EditText edtTitle,edtContent;
    private Toolbar toolbar;
    private String action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_crud);
        mapping();
        setupToolBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_crud_request_non_admin,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_menu_apply_request_crud_non_admin:
                Request request = getInputRequest();
                if(request != null){
                    Intent data = new Intent();
                    data.putExtra(Const.REQUEST_DATA_INTENT,request);
                    setResult(RESULT_OK,data);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mapping(){
        edtTitle = findViewById(R.id.editText_title_request_non_admin);
        edtContent = findViewById(R.id.editText_content_request_non_admin);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolBar(){
        setSupportActionBar(toolbar);
        action = getIntent().getAction();
        if( action.equals(RequestActivity.ACTION_ADD_NEW_REQUEST))
            toolbar.setTitle("Add new request");
        else{

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
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
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateString = format.format(date);
        return new Request(0, UserSingleTon.getInstance().getUser().getId(),1,title,content,dateString);
    }

    private void showMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}