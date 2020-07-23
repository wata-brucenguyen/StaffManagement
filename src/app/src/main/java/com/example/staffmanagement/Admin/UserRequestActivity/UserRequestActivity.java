package com.example.staffmanagement.Admin.UserRequestActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.staffmanagement.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.Presenter.UserPresenter;
import com.example.staffmanagement.R;

public class UserRequestActivity extends AppCompatActivity implements UserRequestInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);
        UserPresenter presenter = new UserPresenter(this,this);
    }
}