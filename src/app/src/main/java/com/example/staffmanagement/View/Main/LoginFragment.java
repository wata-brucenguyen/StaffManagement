package com.example.staffmanagement.View.Main;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Staff.ViewModel.LoginViewModel;


public class LoginFragment extends BaseFragment {

    private Button btnLogin;
    private EditText txtEdtUsername, txtEdtPassword;
    private CheckBox cbRemember;

    @Override
    public void initView() {
        this.idResLayout = R.layout.fragment_login;
    }

    @Override
    public void mapping(View view) {
        btnLogin = view.findViewById(R.id.buttonLogin);
        txtEdtUsername = view.findViewById(R.id.textInputEditTextUserName);
        txtEdtPassword = view.findViewById(R.id.textInputEditTextPassword);
        cbRemember = view.findViewById(R.id.checkBox);
    }

    @Override
    public void eventRegister() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    @Override
    public void setDataOnView() {
        txtEdtUsername.setText(mViewModel.getUsername());
        txtEdtPassword.setText(mViewModel.getPassword());
        cbRemember.setChecked(mViewModel.getIsRemember());
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

        mViewModel.setAllData(userName, password, cbRemember.isChecked());
        mInterface.executeLogin();
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
