package com.example.staffmanagement.View.Main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Main.LoginViewModel;

import java.util.Objects;

public class LoginFragment extends BaseFragment {

    protected LoginInterface mInterface;
    private TextView btnLogin;
    private EditText txtEdtUsername, txtEdtPassword;
    private CheckBox cbRemember;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mInterface = (LoginInterface) context;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.loginParent).setOnTouchListener((view1, motionEvent) -> {
           GeneralFunc.hideKeyboard(view1, Objects.requireNonNull(getActivity()));
           return false;
        });
    }

    @Override
    public ViewModel getViewModel() {
        return ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(LoginViewModel.class);
    }

    @Override
    public void initView() {
        this.idResLayout = R.layout.fragment_login;
    }

    @Override
    public void mapping(View view) {
        btnLogin = view.findViewById(R.id.textViewLogin);
        txtEdtUsername = view.findViewById(R.id.textInputEditTextUserName);
        txtEdtPassword = view.findViewById(R.id.textInputEditTextPassword);
        cbRemember = view.findViewById(R.id.checkBox);
    }

    @Override
    public void initEvent() {
        btnLogin.setOnClickListener(view -> {
            if(GeneralFunc.checkInternetConnection(getActivity()))
                login();
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
