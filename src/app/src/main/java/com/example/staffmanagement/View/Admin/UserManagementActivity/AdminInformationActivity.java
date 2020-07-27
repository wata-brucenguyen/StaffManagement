package com.example.staffmanagement.View.Admin.UserManagementActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.staffmanagement.Presenter.Admin.AdminInformationPresenter;
import com.example.staffmanagement.View.Ultils.Const;

import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.User;

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
    private ArrayList<Role> role;
    private ArrayList<String> string ;
    private AdminInformationPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_information);
        mPresenter = new AdminInformationPresenter(this,this);
        mapping();
        checkAction();
        setupToolbar();
        setDataToLayout();
        setUpSpinner();

        imageView_editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEdit) {
                    setUpPopUpMenu();
                }
                else {
                    applyEditProfile();
                    Toast.makeText(getBaseContext(), "Apply", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void mapping()
    {
        editText_NameAdmin = findViewById(R.id.editText_NameAdmin);
        editText_Email = findViewById(R.id.editText_Email);
        editText_Phonenumber = findViewById(R.id.editText_PhoneNumber);
        editText_Address = findViewById(R.id.editText_Address);

        spinnerRole = findViewById(R.id.spinnerRole);

        mToolbar = findViewById(R.id.toolbar);
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
        PopupMenu popupMenu  = new PopupMenu(AdminInformationActivity.this,imageView_editIcon);

        switch (action) {
            case ADMIN_PROFILE:
                popupMenu.getMenuInflater().inflate(R.menu.menu_item_edit_admin,popupMenu.getMenu());
                popupMenuAdminProfile(popupMenu);
                break;
            case STAFF_PROFILE:
                popupMenu.getMenuInflater().inflate(R.menu.menu_item_edit_staff,popupMenu.getMenu());
                popupMenuStaffProfile(popupMenu);
                break;
        }
        popupMenu.show();

    }

    private void checkAction(){
        Intent intent = getIntent();
        action = intent.getAction();
        switch (action) {
            case ADMIN_PROFILE:
                mToolbar.setTitle("Admin Profile");
                break;
            case STAFF_PROFILE:
                mToolbar.setTitle("Staff Profile");
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
                if(menuItem.getItemId() ==  R.id.popup_menu_item_reset_password) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminInformationActivity.this);
                    builder.setTitle("Do you want to reset password ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            mPresenter.resetPassword(UserSingleTon.getInstance().getUser().getId());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }


                return false;
            }
        });


    }

    private void showChangePasswordDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.change_password_dialog,null,false);
        final EditText editTextPassword = dialogView.findViewById(R.id.editText_Password);
        final EditText editTextNewPassword = dialogView.findViewById(R.id.editText_New_Password);
        final EditText editTextConfirmPassword = dialogView.findViewById(R.id.editText_Confirm_Password);

        builder.setView(dialogView);
        builder.setTitle("Change password");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // code for checking password
                String pass = editTextPassword.getText().toString();
                String newPass = editTextNewPassword.getText().toString();
                String confirmPass= editTextConfirmPassword.getText().toString();
                if(TextUtils.isEmpty(pass) )
                    showMessage("Password is empty");
                else if(TextUtils.isEmpty(newPass))
                    showMessage("New password is empty");
                else if(TextUtils.isEmpty(confirmPass))
                    showMessage("Confirm password is empty");
                else if(pass.equals(UserSingleTon.getInstance().getUser().getPassword())) {
                    if (newPass.equals(confirmPass)) {

                        mPresenter.changePassword(UserSingleTon.getInstance().getUser().getId(), newPass);
                    }
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void editProfile(){
            setFocusEdittext(true);
            imageView_editIcon.setImageResource(R.drawable.ic_baseline_green_check_24);
            isEdit=true;
    }

    private void setFocusEdittext(boolean b){
        spinnerRole.setEnabled(b);
        editText_NameAdmin.setEnabled(b);
        editText_Address.setEnabled(b);
        editText_Email.setEnabled(b);
        editText_Phonenumber.setEnabled(b);
    }

    @Override
    public void showChangePassword(String message) {
        Toast.makeText(getBaseContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getBaseContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void setUpSpinner(){
       setUpRole();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,string);
        spinnerRole.setAdapter(arrayAdapter);
        switch (action) {
            case ADMIN_PROFILE:
                int id = UserSingleTon.getInstance().getUser().getIdRole();

                spinnerRole.setSelection(getPositionRoleById(id)-1);
                break;
            case STAFF_PROFILE:
                int idStaff = mUser.getIdRole();
                spinnerRole.setSelection(idStaff-1);
                break;
        }

    }

    private void setUpRole(){
        role = new ArrayList<>();
        string = new ArrayList<>();
        role.addAll(mPresenter.getAllRole());
        for(int i=0;i<role.size();i++){
            string.add(role.get(i).getName());
        }
    }

    private int getPositionRoleById(int id){
        int vitri=0;
        for(int i=0;i<=role.size();i++){
            if(role.get(i).getId() == id) {
                vitri=i;
                break;
            }
        }
        return vitri;
    }
    private void applyEditProfile(){
        setFocusEdittext(false);
        imageView_editIcon.setImageResource(R.drawable.ic_baseline_black_create_24);
        isEdit=false;
        String roleName = (String) spinnerRole.getSelectedItem();
        switch (action) {
            case ADMIN_PROFILE:
                UserSingleTon.getInstance().getUser().setIdRole(findIdByName(spinnerRole.getSelectedItem().toString()));
                UserSingleTon.getInstance().getUser().setEmail(editText_Email.getText().toString());
                UserSingleTon.getInstance().getUser().setPhoneNumber(editText_Phonenumber.getText().toString());
                UserSingleTon.getInstance().getUser().setAddress(editText_Address.getText().toString());
                mPresenter.update(UserSingleTon.getInstance().getUser());
                break;
            case STAFF_PROFILE:
                mUser.setIdRole(findIdByName(spinnerRole.getSelectedItem().toString()));
                mUser.setAddress(editText_Address.getText().toString());
                mUser.setEmail(editText_Email.getText().toString());
                mUser.setPhoneNumber(editText_Phonenumber.getText().toString());
                break;
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