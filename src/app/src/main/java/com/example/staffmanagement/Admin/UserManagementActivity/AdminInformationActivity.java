package com.example.staffmanagement.Admin.UserManagementActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.staffmanagement.Admin.Const;

import com.example.staffmanagement.Database.Data.UserSingleTon;
import com.example.staffmanagement.Database.Entity.Role;
import com.example.staffmanagement.Database.Entity.User;

import com.example.staffmanagement.Presenter.UserPresenter;
import com.example.staffmanagement.R;

import java.util.ArrayList;


public class AdminInformationActivity extends AppCompatActivity implements AdminInformationInterface{


    private EditText editText_Email, editText_Phonenumber, editText_Address, editText_NameAdmin;
    private Spinner spinnerRole;
    private Toolbar mToolbar;
    private ImageView imageView_profileImage, imageView_editIcon;
    private Boolean isEdit = false;
    private String action;
    private User mUser;
    private ArrayAdapter arrayAdapter;
    public static final String ADMIN_PROFILE = "ADMIN_PROFILE";
    public static final String STAFF_PROFILE = "STAFF_PROFILE";
    private UserPresenter userPresenter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_information);
        userPresenter = new UserPresenter(this,this);
        checkAction();
        mapping();
        setupToolbar();
        setUpPopUpMenu();
        setDataToLayout();
        setUpSpinner();
    }

    private void mapping()
    {
        editText_NameAdmin = findViewById(R.id.editText_NameAdmin);
        editText_Email = findViewById(R.id.editText_Email);
        editText_Phonenumber = findViewById(R.id.editText_PhoneNumber);
        editText_Address = findViewById(R.id.editText_Address);

        spinnerRole = findViewById(R.id.spinnerRole);

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
            case ADMIN_PROFILE:
                popupMenu.inflate(R.menu.menu_item_edit_admin);
                popupMenuAdminProfile(popupMenu);
                break;
            case STAFF_PROFILE:
                popupMenu.inflate(R.menu.menu_item_edit_staff);
                popupMenuStaffProfile(popupMenu);
                break;
        }


    }

    private void checkAction(){
        Intent intent = getIntent();
        action = intent.getAction();
        switch (action) {
            case ADMIN_PROFILE:

                break;
            case STAFF_PROFILE:
                mUser = (User) intent.getSerializableExtra(Const.USER_INFO_INTENT);
                break;
        }
    }

    private void loadAdminProfile(){
        editText_NameAdmin.setText(UserSingleTon.getInstance().getUser().getFullName());
        editText_Address.setText(UserSingleTon.getInstance().getUser().getAddress());
        editText_Email.setText(UserSingleTon.getInstance().getUser().getEmail());
        editText_Phonenumber.setText(UserSingleTon.getInstance().getUser().getPhoneNumber());
    }

    private void loadStaffProfile(){
        editText_NameAdmin.setText(mUser.getFullName());
        editText_Address.setText(mUser.getAddress());
        editText_Email.setText(mUser.getEmail());
        editText_Phonenumber.setText(mUser.getPhoneNumber());
    }
    private void setDataToLayout(){
        switch (action) {
            case ADMIN_PROFILE:
               loadAdminProfile();
                break;
            case STAFF_PROFILE:
                loadStaffProfile();
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
                        editProfile();
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
                            userPresenter.resetPassword(UserSingleTon.getInstance().getUser().getId());
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
        final EditText editTextUsername = dialogView.findViewById(R.id.editText_Username);
        final EditText editTextPassword = dialogView.findViewById(R.id.editText_Password);
        final EditText editTextConfirmPassword = dialogView.findViewById(R.id.editText_Confirm_Password);

        builder.setView(dialogView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // code for matching password
                String user = editTextUsername.getText().toString();
                String pass = editTextPassword.getText().toString();
                String confirmPass= editTextConfirmPassword.getText().toString();
                if (pass == confirmPass)
                {
                    userPresenter = new UserPresenter(AdminInformationActivity.this,AdminInformationActivity.this);
                    userPresenter.changePassword(UserSingleTon.getInstance().getUser().getId(),pass);

                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void editProfile(){

        if( !isEdit ) {
            setFocusEdittext(true);
            imageView_editIcon.setImageResource(R.drawable.ic_baseline_green_check_24);
            isEdit=true;
        } else {
            setFocusEdittext(false);
            imageView_editIcon.setImageResource(R.drawable.ic_baseline_black_create_24);
            isEdit=false;
        }
    }

    private void setFocusEdittext(Boolean b){
        editText_Address.setFocusable(b);
        editText_Email.setFocusable(b);
        editText_Phonenumber.setFocusable(b);
    }

    @Override
    public void showChangePassword(String message) {
        Toast.makeText(getBaseContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void setUpSpinner(){
        ArrayList<Role> role = new ArrayList<Role>();
        userPresenter.getAllRole();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,role);
        spinnerRole.setAdapter(arrayAdapter);
        switch (action) {
            case ADMIN_PROFILE:
                int id = UserSingleTon.getInstance().getUser().getId();
                int idRole = userPresenter.getIdRole(id);
                spinnerRole.setSelection(idRole-1);
                break;
            case STAFF_PROFILE:

                break;
        }

    }

}