package com.example.staffmanagement.View.Admin.UserManagementActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Entity.UserBuilder.UserBuilder;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Admin.AddUserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AddUserActivity extends AppCompatActivity {

    private CheckNetwork mCheckNetwork;
    private EditText editText_Email, editText_PhoneNumber, editText_Address, editText_NameAdmin, editText_UserName;
    private Spinner spinnerRole;
    private Toolbar mToolbar;
    private List<Role> role;
    private List<String> string;
    private ArrayAdapter arrayAdapter;
    private AddUserViewModel mViewModel;
    private boolean b = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AdminAppTheme);
        setContentView(R.layout.activity_add_user);
        mViewModel = ViewModelProviders.of(this).get(AddUserViewModel.class);
        mapping();
        setupToolbar();
        eventRegister();

    }

    @Override
    protected void onStart() {
        super.onStart();

        mCheckNetwork = new CheckNetwork(this);
        mCheckNetwork.registerCheckingNetwork();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mWifiReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mWifiReceiver);
        mCheckNetwork.unRegisterCheckingNetwork();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_add_user_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (CheckNetwork.checkInternetConnection(AddUserActivity.this)) {
            User user = getInputUser();
            if (user != null) {
                mViewModel.checkUserNameIsExisted(user);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void eventRegister() {
        mViewModel.getRole().observe(this, roles -> {
            role.addAll(roles);
            for (int i = 0; i < role.size(); i++) {
                string.add(role.get(i).getName());
            }
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, string);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRole.setAdapter(arrayAdapter);
        });

        mViewModel.getFlag().observe(this, flag -> {
            switch (flag) {
                case CHECK_USER:
                    showMessage("User name is existed");
                    mViewModel.setFlag();
                    break;
                case ADD:
                    User user = getInputUser();
                    executeAddUser(user);
                    mViewModel.setFlag();
                    break;
            }
        });
        GeneralFunc.setHideKeyboardOnTouch(this, findViewById(R.id.AddUser));
    }

    public void executeAddUser(User user) {
        if (user != null) {
            Intent data = new Intent();
            data.putExtra(Constant.USER_INFO_INTENT, user);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    private void mapping() {
        editText_NameAdmin = findViewById(R.id.txt_NameAdmin);
        editText_Email = findViewById(R.id.editText_Email);
        editText_PhoneNumber = findViewById(R.id.editText_PhoneNumber);
        editText_Address = findViewById(R.id.editText_Address);
        editText_UserName = findViewById(R.id.editText_UserName);
        spinnerRole = findViewById(R.id.spinnerRole);
        mToolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.setTitle("Add user");
    }


    private User getInputUser() {
        int idRole = findIdByName(spinnerRole.getSelectedItem().toString());
        String nameAdmin = editText_NameAdmin.getText().toString();
        String userName = editText_UserName.getText().toString();
        String phoneNumber = editText_PhoneNumber.getText().toString();
        String email = editText_Email.getText().toString();
        String address = editText_Address.getText().toString();

        if (TextUtils.isEmpty(nameAdmin)) {
            showMessage("Full name is empty");
            editText_NameAdmin.requestFocus();
            return null;
        }

        if (TextUtils.isEmpty(userName)) {
            showMessage("User name is empty");
            editText_UserName.requestFocus();
            return null;
        }

        String userNamePattern = "^[a-zA-Z0-9_]{6,}$";
        if (!Pattern.matches(userNamePattern, userName)) {
            showMessage("User name must more than 5 character and not be contained special character");
            editText_UserName.requestFocus();
            return null;
        }

        //check phone number
        if ((phoneNumber.length() < 10 || phoneNumber.length() > 12) && phoneNumber.length() > 0) {
            showMessage("Phone number must be from 10 to 12");
            editText_PhoneNumber.requestFocus();
            return null;
        }

        //check email
        String emailPattern = "^[a-z][a-z0-9_.]{1,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";
        if (email.length() > 0 && !Pattern.matches(emailPattern, email)) {
            showMessage("Email format is wrong");
            editText_Email.requestFocus();
            return null;
        }

        User user = new UserBuilder()
                .buildId(0)
                .buildIdRole(idRole)
                .buildFullName(nameAdmin)
                .buildUserName(userName)
                .buildPassword(Constant.DEFAULT_PASSWORD)
                .buildPhoneNumber(phoneNumber)
                .buildEmail(email)
                .buildAddress(address)
                .buildAvatar(Constant.DEFAULT_AVATAR)
                .buildIdUserState(1)
                .build();
        return user;
    }

    private void setUpRole() {
        if (role == null || role.size() == 0) {
            role = new ArrayList<>();
            string = new ArrayList<>();
            mViewModel.getAllRole();
        }
    }

    private int findIdByName(String name) {
        for (int i = 0; i < role.size(); i++) {
            if (role.get(i).getName().equals(name))
                return role.get(i).getId();
        }
        return -1;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CheckNetwork.checkInternetConnection(AddUserActivity.this)) {
                runOnUiThread(() -> setUpRole());
            }
        }
    };
}