package com.example.staffmanagement.View.Main;

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

import com.example.staffmanagement.View.Admin.MainAdminActivity.MainAdminActivity;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Staff.Home.StaffHomeActivity;
import com.example.staffmanagement.Presenter.Main.LogInPresenter;
import com.example.staffmanagement.View.Ultils.Const;

public class LogInActivity extends AppCompatActivity implements LogInInterface {

    private ProgressDialog mProgressDialog;
    private LogInPresenter mPresenter;
    private Button btnLogin;
    private EditText txtEdtUsername, txtEdtPassword;
    private CheckBox cbRemember;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPresenter = new LogInPresenter(this, this);
        try {
            prepareData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapping();
        eventRegister();
        checkIsLogin();
        getSavedLogin();
    }

    private void eventRegister() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String userName = txtEdtUsername.getText().toString().trim();
        String password = txtEdtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            showMessage("Username is empty");
            txtEdtUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showMessage("Password is empty");
            txtEdtPassword.requestFocus();
            return;
        }

        mPresenter.checkLoginInformation(userName, password);

    }

    private void getSavedLogin() {
        sharedPreferences = getSharedPreferences(Const.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        txtEdtUsername.setText(sharedPreferences.getString(Const.SHARED_PREFERENCE_USERNAME, ""));
        txtEdtPassword.setText(sharedPreferences.getString(Const.SHARED_PREFERENCE_PASSWORD, ""));
        cbRemember.setChecked(sharedPreferences.getBoolean(Const.SHARED_PREFERENCE_REMEMBER, false));
    }

    private void checkIsLogin(){
        sharedPreferences = getSharedPreferences(Const.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean(Const.SHARED_PREFERENCE_IS_LOGIN,false);
        if(b){
            int idUser = sharedPreferences.getInt(Const.SHARED_PREFERENCE_ID_USER,-1);
            mPresenter.getUserForLogin(idUser);
        }
    }

    private void saveLogin(String username, String password) {
        if (cbRemember.isChecked()) {
            editor = sharedPreferences.edit();
            editor.putString(Const.SHARED_PREFERENCE_USERNAME, username);
            editor.putString(Const.SHARED_PREFERENCE_PASSWORD, password);
            editor.putBoolean(Const.SHARED_PREFERENCE_REMEMBER, true);
            editor.apply();
        } else {
            editor = sharedPreferences.edit();
            editor.remove(Const.SHARED_PREFERENCE_USERNAME);
            editor.remove(Const.SHARED_PREFERENCE_PASSWORD);
            editor.remove(Const.SHARED_PREFERENCE_REMEMBER);
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSuccess(User user) {
        UserSingleTon.getInstance().setUser(user);
        saveLogin(user.getUserName(), user.getPassword());
        editor = sharedPreferences.edit();
        editor.putBoolean(Const.SHARED_PREFERENCE_IS_LOGIN,true);
        editor.putInt(Const.SHARED_PREFERENCE_ID_USER,user.getId());
        editor.apply();
        Intent intent;
        if (user.getIdRole() == 1) {
            intent = new Intent(LogInActivity.this, MainAdminActivity.class);
        } else
            intent = new Intent(LogInActivity.this, StaffHomeActivity.class);
        intent.putExtra("fullname", user.getFullName());
        startActivity(intent);
        finish();
    }
}