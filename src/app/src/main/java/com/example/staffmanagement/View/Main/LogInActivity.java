package com.example.staffmanagement.View.Main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.staffmanagement.View.Admin.Home.AdminHomeActivity;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Staff.Home.StaffHomeActivity;
import com.example.staffmanagement.Presenter.Main.LogInPresenter;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.View.Staff.ViewModel.LoginViewModel;

public class LogInActivity extends AppCompatActivity implements LogInInterface, LoginTransData {

    private ProgressDialog mProgressDialog;
    private LogInPresenter mPresenter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LoginFragment loginFragment;
    private LoadingFragment loadingFragment;
    private LoginViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LoginAppTheme);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mPresenter = new LogInPresenter(this, this);

        //checkIsLogin();
        getSavedLogin();
    }

    public void newLoadingFragment() {
        loadingFragment = new LoadingFragment();
        loginFragment = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIsLogin();
    }

    @Override
    public void onBackPressed() {
        if (GeneralFunc.isTheLastActivity(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Do you want to exit ?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else
            super.onBackPressed();
    }

    private void newLoginFragment() {
        loginFragment = new LoginFragment();
        loadingFragment = null;
    }

    private void checkIsLogin() {
        if (!mViewModel.isCheckLogin())
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mViewModel.setCheckLogin(true);
                        showFragment(0);
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
        mViewModel.setUsername(sharedPreferences.getString(Constant.SHARED_PREFERENCE_USERNAME, ""));
        mViewModel.setPassword(sharedPreferences.getString(Constant.SHARED_PREFERENCE_PASSWORD, ""));
        mViewModel.setIsRemember(sharedPreferences.getBoolean(Constant.SHARED_PREFERENCE_REMEMBER, false));
    }

    private void saveLogin(String username, String password) {
        if (mViewModel.getIsRemember()) {
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
    public void onLoginSuccess(final User user) {
        UserSingleTon.getInstance().setUser(user);
        saveLogin(user.getUserName(), user.getPassword());
        editor = sharedPreferences.edit();
        editor.putBoolean(Constant.SHARED_PREFERENCE_IS_LOGIN, true);
        editor.putInt(Constant.SHARED_PREFERENCE_ID_USER, user.getId());
        editor.apply();
        Intent intent;
        if (user.getIdRole() == 1) {
            intent = new Intent(LogInActivity.this, AdminHomeActivity.class);
        } else {
            intent = new Intent(LogInActivity.this, StaffHomeActivity.class);
        }
        intent.putExtra("fullname", user.getFullName());
        startActivity(intent);
        finish();
    }

    @Override
    public void executeLogin() {
        mPresenter.checkLoginInformation(mViewModel.getUsername(), mViewModel.getPassword());
    }

    @Override
    public void showFragment(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (i) {
            case 0:
                newLoadingFragment();
                ft.replace(R.id.frameLayout, loadingFragment);
                break;
            case 1:
                newLoginFragment();
                ft.replace(R.id.frameLayout, loginFragment);
        }
        ft.commit();
    }
}