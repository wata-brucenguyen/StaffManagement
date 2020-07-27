package com.example.staffmanagement.View.Admin.UserManagementActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Presenter.Admin.AddUserPresenter;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Ultils.Constant;

import java.util.ArrayList;

public class AddUserActivity extends AppCompatActivity implements AddUserInterface {

    private EditText editText_Email, editText_Phonenumber, editText_Address, editText_NameAdmin, editText_UserName;
    private Spinner spinnerRole;
    private Toolbar mToolbar;
    private ImageView imageView_saveIcon;
    private ArrayList<Role> role;
    private ArrayList<String> string;
    private AddUserPresenter mPresenter;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AdminAppTheme);
        setContentView(R.layout.activity_add_user);
        mPresenter = new AddUserPresenter(this, this);
        mapping();
        setupToolbar();
        setUpSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_add_user_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        User user = getInputUser();
        if (user != null) {
            Intent data = new Intent();
            data.putExtra(Constant.USER_INFO_INTENT, user);
            setResult(RESULT_OK, data);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void mapping() {
        editText_NameAdmin = findViewById(R.id.txt_NameAdmin);
        editText_Email = findViewById(R.id.editText_Email);
        editText_Phonenumber = findViewById(R.id.editText_PhoneNumber);
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


    private void setUpSpinner() {
        setUpRole();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, string);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(arrayAdapter);
    }


    private User getInputUser() {
        int idRole = findIdByName(spinnerRole.getSelectedItem().toString());
        String nameAdmin = editText_NameAdmin.getText().toString();
        String userName = editText_UserName.getText().toString();
        String phoneNumber = editText_Phonenumber.getText().toString();
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

        if (TextUtils.isEmpty(phoneNumber)) {
            showMessage("Phone number is empty");
            editText_Phonenumber.requestFocus();
            return null;
        }

        if (TextUtils.isEmpty(address)) {
            showMessage("Address is empty");
            editText_Address.requestFocus();
            return null;
        }

        User user = new User(0, idRole, nameAdmin, userName
                , "123456", phoneNumber, email
                , address, new byte[]{});
        return user;
    }


    private void setUpRole() {
        role = new ArrayList<>();
        string = new ArrayList<>();
        role.addAll(mPresenter.getAllRole());
        for (int i = 0; i < role.size(); i++) {
            string.add(role.get(i).getName());
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
}