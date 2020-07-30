package com.example.staffmanagement.View.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.staffmanagement.View.Admin.Home.AdminHomeActivity;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Staff.Home.StaffHomeActivity;
import com.example.staffmanagement.Presenter.Main.LogInPresenter;
import com.example.staffmanagement.View.Ultils.Constant;

public class LogInActivity extends AppCompatActivity implements LogInInterface, LoginTransData {

    private ProgressDialog mProgressDialog;
    private LogInPresenter mPresenter;
    private Button btnLogin;
    private EditText txtEdtUsername, txtEdtPassword;
    private CheckBox cbRemember;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String mUsername, mPassword;
    private boolean mRemember;
    private LoginFragment loginFragment;
    private LoadingFragment loadingFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LoginAppTheme);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        mPresenter = new LogInPresenter(this, this);

        //checkIsLogin();
        getSavedLogin();
        loadingFragment = new LoadingFragment();
        setLoginFragment();

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.frameLayout, loadingFragment);
        ft.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIsLogin();
    }

    private void setLoginFragment() {
        loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", mUsername);
        bundle.putString("password", mPassword);
        bundle.putBoolean("remember", mRemember);
        loginFragment.setArguments(bundle);
    }

    private void checkIsLogin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
                    boolean b = sharedPreferences.getBoolean(Constant.SHARED_PREFERENCE_IS_LOGIN, false);
                    if (b) {
                        int idUser = sharedPreferences.getInt(Constant.SHARED_PREFERENCE_ID_USER, -1);
                        mPresenter.getUserForLogin(idUser);
                    } else {
                        showFragment(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void getSavedLogin() {
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        mUsername = sharedPreferences.getString(Constant.SHARED_PREFERENCE_USERNAME, "");
        mPassword = sharedPreferences.getString(Constant.SHARED_PREFERENCE_PASSWORD, "");
        mRemember = sharedPreferences.getBoolean(Constant.SHARED_PREFERENCE_REMEMBER, false);
    }

    private void saveLogin(String username, String password) {
        if (mRemember) {
            editor = sharedPreferences.edit();
            editor.putString(Constant.SHARED_PREFERENCE_USERNAME, username);
            editor.putString(Constant.SHARED_PREFERENCE_PASSWORD, password);
            editor.putBoolean(Constant.SHARED_PREFERENCE_REMEMBER, true);
            editor.apply();
        } else {
            editor = sharedPreferences.edit();
            editor.remove(Constant.SHARED_PREFERENCE_USERNAME);
            editor.remove(Constant.SHARED_PREFERENCE_PASSWORD);
            editor.remove(Constant.SHARED_PREFERENCE_REMEMBER);
            editor.commit();
        }
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
    public void onLoginSuccess(User user) {
        UserSingleTon.getInstance().setUser(user);
        saveLogin(user.getUserName(), user.getPassword());
        editor = sharedPreferences.edit();
        editor.putBoolean(Constant.SHARED_PREFERENCE_IS_LOGIN, true);
        editor.putInt(Constant.SHARED_PREFERENCE_ID_USER, user.getId());
        editor.apply();
        Intent intent;
        if (user.getIdRole() == 1) {
            intent = new Intent(LogInActivity.this, AdminHomeActivity.class);
        } else
            intent = new Intent(LogInActivity.this, StaffHomeActivity.class);
        intent.putExtra("fullname", user.getFullName());
        startActivity(intent);
        finish();
    }


    @Override
    public void passData(String username, String password, boolean remember) {
        mPresenter.checkLoginInformation(username, password);
        mRemember = remember;
    }

    @Override
    public void showFragment(int i) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (i) {
            case 0:
                ft.replace(R.id.frameLayout, loadingFragment);
                break;
            case 1:
                ft.replace(R.id.frameLayout, loginFragment);
        }
        ft.commit();
    }
}