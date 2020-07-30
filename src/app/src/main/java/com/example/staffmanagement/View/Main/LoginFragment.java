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

import com.example.staffmanagement.R;


public class LoginFragment extends Fragment {

    private Button btnLogin;
    private EditText txtEdtUsername, txtEdtPassword;
    private CheckBox cbRemember;
    private String mUsername, mPassword;
    private boolean mRemember;
    private LoginTransData mInterface;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mInterface = (LoginTransData) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            mUsername = bundle.getString("username");
            mPassword = bundle.getString("password");
            mRemember = bundle.getBoolean("remember");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        eventRegister();
        txtEdtUsername.setText(mUsername);
        txtEdtPassword.setText(mPassword);
        cbRemember.setChecked(mRemember);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void mapping(View view) {
        btnLogin = view.findViewById(R.id.buttonLogin);
        txtEdtUsername = view.findViewById(R.id.textInputEditTextUserName);
        txtEdtPassword = view.findViewById(R.id.textInputEditTextPassword);
        cbRemember = view.findViewById(R.id.checkBox);
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

        mInterface.passData(userName,password,cbRemember.isChecked());

    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
