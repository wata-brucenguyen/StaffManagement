package com.example.staffmanagement.Admin.UserManagementActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.staffmanagement.R;

public class AdminInformationActivity extends AppCompatActivity {

    private TextView txt_NameAdmin,txt_Role,txt_Email,txt_Phonenumber,txt_Address;
    private EditText editText_Role, editText_Email, editText_Phonenumber, editText_Address;
    private Toolbar mToolbar;
    private ImageView imageView_profileImage, imageView_editIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_information);

        mapping();
        setupToolbar();
    }

    private void mapping()
    {
        txt_NameAdmin = findViewById(R.id.txt_NameAdmin);
        txt_Role = findViewById(R.id.txt_Role);
        txt_Email = findViewById(R.id.txt_Email);
        txt_Phonenumber = findViewById(R.id.txt_PhoneNumber);
        txt_Address = findViewById(R.id.txt_Address);

        editText_Role = findViewById(R.id.editText_Role);
        editText_Email = findViewById(R.id.editText_Email);
        editText_Phonenumber = findViewById(R.id.editText_PhoneNumber);
        editText_Address = findViewById(R.id.editText_Address);

        mToolbar = findViewById(R.id.toolbar);

        imageView_profileImage = findViewById(R.id.profile_image);
        imageView_editIcon = findViewById(R.id.edit_icon);
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

    private void setUpPopUpMenu(){
        PopupMenu popupMenu  = new PopupMenu(this,imageView_editIcon);

    }
}