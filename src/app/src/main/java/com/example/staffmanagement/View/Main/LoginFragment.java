package com.example.staffmanagement.View.Main;

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
    private ConstraintLayout loginParent;
    private Animation animation;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mInterface = (LoginInterface) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        GeneralFunc.setupUI(loginParent,getActivity());
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
        loginParent=view.findViewById(R.id.loginParent);
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
//    // hide keyboard when touch outside
//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) activity.getSystemService(
//                        Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(
//                activity.getCurrentFocus().getWindowToken(), 0);
//    }
//    public void setupUI(View view) {
//        // Set up touch listener for non-text box views to hide keyboard.
//        if (!(view instanceof EditText)) {
//            view.setOnTouchListener((v, event) -> {
//                hideSoftKeyboard(getActivity());
//                return false;
//            });
//        }
//
//        //If a layout container, iterate over children and seed recursion.
//        if (view instanceof ViewGroup) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                View innerView = ((ViewGroup) view).getChildAt(i);
//                setupUI(innerView);
//            }
//        }
//    }
}
