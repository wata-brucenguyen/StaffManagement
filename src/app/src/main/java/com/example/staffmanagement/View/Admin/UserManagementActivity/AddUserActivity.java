package com.example.staffmanagement.View.Admin.UserManagementActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.staffmanagement.Model.Entity.UserState;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Notification.Service.Broadcast;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Admin.AddUserViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AddUserActivity extends AppCompatActivity {

    private CheckNetwork mCheckNetwork;
    private EditText editText_Email, editText_PhoneNumber, editText_Address, editText_NameAdmin, editText_UserName;
    private TextInputLayout textInputLayoutFullName, textInputLayoutUserName, textInputLayoutEmail, textInputLayoutPhoneNumber;
    private Spinner spinnerRole;
    private Toolbar mToolbar;
    private List<Role> role;
    private List<String> string;
    private ArrayAdapter arrayAdapter;
    private AddUserViewModel mViewModel;
    private Broadcast mBroadcast;

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

        mBroadcast = new Broadcast();
        IntentFilter filter = new IntentFilter("Notification");
        registerReceiver(mBroadcast, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mWifiReceiver);
        mCheckNetwork.unRegisterCheckingNetwork();
        unregisterReceiver(mBroadcast);
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

        textInputLayoutFullName = findViewById(R.id.textInputLayoutFullName);
        textInputLayoutUserName = findViewById(R.id.textInputLayoutUserName);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPhoneNumber = findViewById(R.id.textInputLayoutPhoneNumber);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> finish());
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
            textInputLayoutFullName.setError("Full name is empty");
            textInputLayoutFullName.requestFocus();
            return null;
        }

        if (TextUtils.isEmpty(userName)) {
            textInputLayoutUserName.setError("User name is empty");
            textInputLayoutUserName.requestFocus();
            return null;
        }

        String userNamePattern = "^[a-zA-Z0-9_]{6,}$";
        if (!Pattern.matches(userNamePattern, userName)) {
            textInputLayoutUserName.setError("User name must more than 5 character and not be contained special character");
            textInputLayoutUserName.requestFocus();
            return null;
        }

        //check phone number
        if ((phoneNumber.length() < 10 || phoneNumber.length() > 12) && phoneNumber.length() > 0) {
            textInputLayoutPhoneNumber.setError("Phone number must be from 10 to 12");
            textInputLayoutPhoneNumber.requestFocus();
            return null;
        }

        //check email
        String emailPattern = "^[a-z][a-z0-9_.]{1,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";
        if (email.length() > 0 && !Pattern.matches(emailPattern, email)) {
            textInputLayoutEmail.setError("Email format is wrong");
            textInputLayoutEmail.requestFocus();
            return null;
        }

        User user = new UserBuilder()
                .buildId(0)
                .buildRole(getRoleById(idRole))
                .buildFullName(nameAdmin)
                .buildUserName(userName)
                .buildPassword(Constant.DEFAULT_PASSWORD)
                .buildPhoneNumber(phoneNumber)
                .buildEmail(email)
                .buildAddress(address)
                .buildAvatar(Constant.DEFAULT_AVATAR)
                .buildUserState(new UserState(1, "Active"))
                .build();
        return user;
    }

    private Role getRoleById(int id) {
        for (int i = 0; i < role.size(); i++) {
            if (id == role.get(i).getId())
                return role.get(i);
        }
        return null;
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