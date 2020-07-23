package com.example.staffmanagement.Admin.UserManagementActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.staffmanagement.Admin.Const;
import com.example.staffmanagement.Admin.MainAdminActivity.User;
import com.example.staffmanagement.Database.DAL.ConstString;
import com.example.staffmanagement.Database.Data.UserSingleTon;
import com.example.staffmanagement.Presenter.LogInPresenter;
import com.example.staffmanagement.Presenter.UserPresenter;
import com.example.staffmanagement.R;

public class AdminInformationActivity extends AppCompatActivity implements AdminInformationInterface{

    private TextView txt_NameAdmin,txt_Role,txt_Email,txt_Phonenumber,txt_Address;
    private EditText editText_Role, editText_Email, editText_Phonenumber, editText_Address;
    private Toolbar mToolbar;
    private ImageView imageView_profileImage, imageView_editIcon;
    private String action;
    private User mUser;
    public static final String ADMIN_PROFIILE = "ADMIN_PROFILE";
    public static final String STAFF_PROFIILE = "STAFF_PROFILE";
    private UserPresenter presenter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_information);
        presenter = new UserPresenter(this,this);
        Intent intent = getIntent();
        action = intent.getAction();
        mUser = (User) intent.getSerializableExtra(Const.USER_INFO_INTENT);
        mapping();
        setupToolbar();
        setUpPopUpMenu();
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
        switch (action) {
            case ADMIN_PROFIILE:
                popupMenu.inflate(R.menu.menu_item_edit_admin);
                popupMenuAdminProfile(popupMenu);
                break;
            case STAFF_PROFIILE:
                popupMenu.inflate(R.menu.menu_item_edit_staff);
                popupMenuStaffProfile(popupMenu);
                break;
        }


    }

    private void popupMenuAdminProfile(PopupMenu popupMenu){
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.popup_menu_item_change_password:
                        showChangePasswordDialog();
                        break;
                    case R.id.popup_menu_item_edit_profile:

                        break;


                }

                return false;
        }
    });
    }
    private void popupMenuStaffProfile(PopupMenu popupMenu){
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() ==  R.id.popup_menu_item_change_password) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminInformationActivity.this);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button

                            presenter.resetPassword(UserSingleTon.getInstance().getUser().getId());


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                }


                return false;
            }
        });
    }

    private void showChangePasswordDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.change_password_dialog,null,false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}