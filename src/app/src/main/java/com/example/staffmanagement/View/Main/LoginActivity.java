package com.example.staffmanagement.View.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.Home.AdminHomeActivity;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Staff.Home.StaffHomeActivity;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Main.LoginViewModel;

public class LoginActivity extends AppCompatActivity implements LoginInterface {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LoginFragment loginFragment;
    private LoadingFragment loadingFragment;
    private LoginViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LoginAppTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        getSavedLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences = null;
        editor = null;
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
        showDialogExit();
    }

    private void showDialogExit() {
        if (GeneralFunc.isTheLastActivity(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Do you want to exit ?");
            builder.setPositiveButton("Ok", (dialogInterface, i) -> finish());
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
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
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onLoginSuccess(final User user) {
        UserSingleTon.getInstance().setUser(user);
        saveLogin(user.getUserName(), user.getPassword());
        editor = sharedPreferences.edit();
        editor.putBoolean(Constant.SHARED_PREFERENCE_IS_LOGIN, true);
        editor.putInt(Constant.SHARED_PREFERENCE_ID_USER, user.getId());
        editor.apply();
        Intent intent;
        if (user.getIdRole() == 1) {
            intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, StaffHomeActivity.class);
        }
        intent.putExtra("fullname", user.getFullName());
        startActivity(intent);
        finish();
    }

    @Override
    public void executeLogin() {
        showFragment(0);
        new Thread(() -> {
            final User user = mViewModel.getByLoginInformation();
            runOnUiThread(() -> {
                if (user == null) {
                    showMessage("Login failed");
                    showFragment(1);
                } else if (user.getIdUserState() != 1) {
                    showMessage("Account is locked");
                    showFragment(1);
                } else
                    onLoginSuccess(user);
            });
        }).start();
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

    public void checkIsLogin() {
        if (!mViewModel.isCheckLogin())
            new Thread(() -> {
                try {
                    mViewModel.setCheckLogin(true);
                    showFragment(0);
                    Thread.sleep(1000);
                    SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
                    boolean b = sharedPreferences.getBoolean(Constant.SHARED_PREFERENCE_IS_LOGIN, false);
                    if (b) {
                        int idUser = sharedPreferences.getInt(Constant.SHARED_PREFERENCE_ID_USER, -1);
                        User user = mViewModel.getUserForLogin(idUser);
                        onLoginSuccess(user);
                    } else {
                        showFragment(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

    }

}