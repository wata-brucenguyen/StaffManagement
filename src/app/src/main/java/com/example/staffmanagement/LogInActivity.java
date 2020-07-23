package com.example.staffmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.staffmanagement.Admin.MainAdminActivity.MainAdminActivity;
import com.example.staffmanagement.Presenter.LogInPresenter;
import com.google.android.material.textfield.TextInputEditText;

public class LogInActivity extends AppCompatActivity implements LogInInterface{

    private ProgressDialog mProgressDialog;
    private  LogInPresenter mPresenter;
    private Button btnLogin;
    private TextInputEditText txtEdtUsername, txtEdtPassword;
    private CheckBox cbRemember;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Mapping();
        sharedPreferences=getSharedPreferences("infoLogin",MODE_PRIVATE);
        txtEdtUsername.setText(sharedPreferences.getString("username",""));
        txtEdtPassword.setText(sharedPreferences.getString("password",""));
        cbRemember.setChecked(sharedPreferences.getBoolean("remember",false));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        mPresenter = new LogInPresenter(this,this);
        prepareData();
    }

    private void login(){
        String username=txtEdtUsername.getText().toString().trim();
        String password=txtEdtPassword.getText().toString().trim();

        if(username.equals("vuong") && password.equals("12345")){
            saveLogin(username,password);

            Intent intent = new Intent(LogInActivity.this, MainAdminActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(LogInActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLogin(String username, String password){
        if(cbRemember.isChecked()){
            editor=sharedPreferences.edit();
            editor.putString("username",username);
            editor.putString("password",password);
            editor.putBoolean("remember",true);
            editor.commit();
        }else{
            editor=sharedPreferences.edit();
            editor.remove("username");
            editor.remove("password");
            editor.remove("remember");
            editor.commit();
        }
    }
    private void Mapping() {
        btnLogin = findViewById(R.id.buttonLogin);
        txtEdtUsername = findViewById(R.id.textInputEditTextUserName);
        txtEdtPassword = findViewById(R.id.textInputEditTextPassword);
        cbRemember = findViewById(R.id.checkBox);
    }
    @Override
    public void createNewProgressDialog(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(message);
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
    public void prepareData() {
        mPresenter.prepareData();
    }
}