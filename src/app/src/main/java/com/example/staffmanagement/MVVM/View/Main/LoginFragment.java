package com.example.staffmanagement.MVVM.View.Main;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.staffmanagement.R;
import com.example.staffmanagement.MVVM.ViewModel.Staff.LoginViewModel;


public class LoginFragment extends BaseFragment {

    protected LogInInterface mInterface;
    private Button btnLogin;
    private EditText txtEdtUsername, txtEdtPassword;
    private CheckBox cbRemember;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mInterface = (LogInInterface) context;
    }

    @Override
    public ViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
    }

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
    public void initEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    @Override
    public void setDataOnView() {
        txtEdtUsername.setText(((LoginViewModel)mViewModel).getUsername());
        txtEdtPassword.setText(((LoginViewModel)mViewModel).getPassword());
        cbRemember.setChecked(((LoginViewModel)mViewModel).getIsRemember());
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



        ((LoginViewModel)mViewModel).setAllData(userName, password, cbRemember.isChecked());
        mInterface.executeLogin();
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
