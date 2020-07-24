package com.example.staffmanagement.Admin.UserManagementActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.staffmanagement.Database.Data.UserSingleTon;
import com.example.staffmanagement.Database.Entity.Role;
import com.example.staffmanagement.Database.Entity.User;
import com.example.staffmanagement.Presenter.UserPresenter;
import com.example.staffmanagement.R;

import java.util.ArrayList;

public class AddUserActivity extends AppCompatActivity implements AddUserInterface{

    private EditText editText_Email, editText_Phonenumber, editText_Address, editText_NameAdmin, editText_UserName;
    private Spinner spinnerRole;
    private Toolbar mToolbar;
    private ImageView  imageView_saveIcon;
    private ArrayList<Role> role;
    private ArrayList<String> string ;
    private UserPresenter userPresenter ;
    private ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        userPresenter = new UserPresenter(this,this);
        mapping();
        setupToolbar();
        setUpSpinner();


        imageView_saveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyInsertProfile();
                Toast.makeText(getBaseContext(), "Add user successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mapping()
    {
        editText_NameAdmin = findViewById(R.id.editText_NameAdmin);
        editText_Email = findViewById(R.id.editText_Email);
        editText_Phonenumber = findViewById(R.id.editText_PhoneNumber);
        editText_Address = findViewById(R.id.editText_Address);
        editText_UserName = findViewById(R.id.editText_UserName);

        spinnerRole = findViewById(R.id.spinnerRole);

        mToolbar = findViewById(R.id.toolbar);
        imageView_saveIcon = findViewById(R.id.save_icon);
    }

    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private void setUpSpinner(){
        setUpRole();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,string);
        spinnerRole.setAdapter(arrayAdapter);
        }



    private void applyInsertProfile(){
        int idRole = findIdByName(spinnerRole.getSelectedItem().toString());
        User user = new User(0,idRole,editText_NameAdmin.getText().toString(),editText_UserName.getText().toString()
                    ,"123456",editText_Phonenumber.getText().toString(),editText_Email.getText().toString()
                    ,editText_Address.getText().toString(),"5/5/1999");
        userPresenter.insertUser(user);

        }



    private void setUpRole(){
        role = new ArrayList<>();
        string = new ArrayList<>();
        role.addAll(userPresenter.getAllRole());
        for(int i=0;i<role.size();i++){
            string.add(role.get(i).getName());
        }
    }

    private int findIdByName(String name){
        for(int i=0;i<role.size();i++){
            if(role.get(i).getName().equals(name))
                return role.get(i).getId();
        }
        return -1;
    }
}