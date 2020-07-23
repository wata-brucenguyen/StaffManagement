package com.example.staffmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.staffmanagement.Admin.MainAdminActivity.MainAdminActivity;
import com.example.staffmanagement.Database.Data.UserSingleTon;
import com.example.staffmanagement.Database.Entity.User;
import com.example.staffmanagement.NonAdmin.RequestActivity.RequestActivity;
import com.example.staffmanagement.Presenter.LogInPresenter;

public class LogInActivity extends AppCompatActivity implements LogInInterface{

    private ProgressDialog mProgressDialog;
    private  LogInPresenter mPresenter;
    private Button btnLogin;
    private EditText txtEdtUsername, txtEdtPassword;
    private CheckBox cbRemember;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();
        getSavedLogin();
        eventRegister();
        mPresenter = new LogInPresenter(this,this);
        prepareData();
    }

    private void eventRegister(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login(){
        String userName = txtEdtUsername.getText().toString().trim();
        String password = txtEdtPassword.getText().toString().trim();

        if(TextUtils.isEmpty(userName)){
            showMessage("Username is empty");
            txtEdtUsername.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)){
            showMessage("Password is empty");
            txtEdtPassword.requestFocus();
            return;
        }

        mPresenter.checkLoginInformation(userName,password);

    }

    private void getSavedLogin(){
        sharedPreferences = getSharedPreferences("infoLogin",MODE_PRIVATE);
        txtEdtUsername.setText(sharedPreferences.getString("username",""));
        txtEdtPassword.setText(sharedPreferences.getString("password",""));
        cbRemember.setChecked(sharedPreferences.getBoolean("remember",false));
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

    private void mapping() {
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

    @Override
    public void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSuccess(User user) {
        UserSingleTon.getInstance().setUser(user);
        saveLogin(user.getUserName(),user.getPassword());
        Intent intent;
        if(user.getIdRole() == 1 ) {
            intent = new Intent(LogInActivity.this, MainAdminActivity.class);
        }
        else
            intent = new Intent(LogInActivity.this, RequestActivity.class);

        startActivity(intent);
        finish();
    }
}