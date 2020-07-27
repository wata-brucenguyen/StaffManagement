package com.example.staffmanagement.View.Staff.UserProfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.staffmanagement.Presenter.Staff.StaffUserProfilePresenter;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Main.LogInActivity;
import com.example.staffmanagement.View.Ultils.Const;

public class StaffUserProfileActivity extends AppCompatActivity implements StaffUserProfileInterface {

    private TextView txtName, txtRole, txtEmail, txtPhoneNumber, txtAddress, txtCloseDialog, txt_eup_accept;
    private EditText tv_eup_name, tv_eup_phone, tv_eup_email, tv_eup_address;
    private ImageView imvBack, imvEdit;
    private Dialog mDialog;
    private StaffUserProfilePresenter userPresenter;
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.StaffAppTheme);
        setContentView(R.layout.activity_user_profile);
        userPresenter = new StaffUserProfilePresenter(this, this);
        mapping();
        eventRegister();
        setDataOnView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_GALLERY && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,REQUEST_CODE_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){

        }
    }

    private void mapping() {
        txtName = findViewById(R.id.textView_name_userProfile);
        txtRole = findViewById(R.id.textView_role_userProfile);
        txtEmail = findViewById(R.id.textView_email_userProfile);
        txtPhoneNumber = findViewById(R.id.textView_phone_userProfile);
        txtAddress = findViewById(R.id.textView_address_userProfile);
        imvBack = findViewById(R.id.imv_backUserProfile);
        imvEdit = findViewById(R.id.editOptionsUserProfile);
    }

    private void setDataOnView() {
        txtName.setText(UserSingleTon.getInstance().getUser().getFullName());
        StaffUserProfilePresenter re = new StaffUserProfilePresenter(this, this);
        String roleName = re.getRoleNameById(UserSingleTon.getInstance().getUser().getIdRole());
        txtRole.setText(roleName);
        txtEmail.setText(UserSingleTon.getInstance().getUser().getEmail());
        txtPhoneNumber.setText(UserSingleTon.getInstance().getUser().getPhoneNumber());
        txtAddress.setText(UserSingleTon.getInstance().getUser().getAddress());
    }

    private void eventRegister() {
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpBtnEditProfile();
            }
        });
    }

    private void setUpBtnEditProfile() {
        PopupMenu popupMenu = new PopupMenu(StaffUserProfileActivity.this, imvEdit);
        popupMenu.getMenuInflater().inflate(R.menu.menu_edit_user_profile, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_menu_editUserProfile_in_staff:
                        activateEditUserProfile();
                        break;
                    case R.id.item_menu_changePassword_in_staff:
                        activateChangePassword();
                        break;
                    case R.id.item_menu_change_avatar_in_staff:
                        openDialogOptionChangeAvatar();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public void activateEditUserProfile() {
        newDialog(R.layout.dialog_edit_user_profile_staff);
        mappingEditUserProfile();
        registerEventEditUserProfile();
    }

    public void newDialog(int layoutRes) {
        mDialog = new Dialog(StaffUserProfileActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(layoutRes);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void mappingEditUserProfile() {
        txtCloseDialog = mDialog.findViewById(R.id.textView_CloseEditUserProfile);
        tv_eup_name = mDialog.findViewById(R.id.tv_eup_Name);
        tv_eup_phone = mDialog.findViewById(R.id.tv_eup_Phone);
        tv_eup_address = mDialog.findViewById(R.id.tv_eup_Address);
        tv_eup_email = mDialog.findViewById(R.id.tv_eup_Email);
        txt_eup_accept = mDialog.findViewById(R.id.textView_eup_accept);

        tv_eup_name.setText(UserSingleTon.getInstance().getUser().getFullName());
        tv_eup_phone.setText(UserSingleTon.getInstance().getUser().getPhoneNumber());
        tv_eup_email.setText(UserSingleTon.getInstance().getUser().getEmail());
        tv_eup_address.setText(UserSingleTon.getInstance().getUser().getAddress());
    }

    private void registerEventEditUserProfile() {

        txtCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        txt_eup_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSingleTon.getInstance().getUser().setFullName(tv_eup_name.getText().toString());
                UserSingleTon.getInstance().getUser().setPhoneNumber(tv_eup_phone.getText().toString());
                UserSingleTon.getInstance().getUser().setEmail(tv_eup_email.getText().toString());
                UserSingleTon.getInstance().getUser().setAddress(tv_eup_address.getText().toString());
                userPresenter.updateUserProfile(UserSingleTon.getInstance().getUser());
                showMessage("Profile is updated");
                setDataOnView();
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    public void activateChangePassword() {
        newDialog(R.layout.dialog_change_user_password);
        final EditText edtOldPass = mDialog.findViewById(R.id.edt_oldPassword_non_admin),
                edtNewPass = mDialog.findViewById(R.id.edt_newPassword_non_admin),
                edtReNewPass = mDialog.findViewById(R.id.edt_reNewPassword_non_admin);

        TextView btnAccept = mDialog.findViewById(R.id.textView_acceptChangePassword_non_admin);
        TextView imvClose = mDialog.findViewById(R.id.textView_CloseChangePassword);

        imvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = edtOldPass.getText().toString();
                String newPass = edtNewPass.getText().toString();
                String confirmNewPass = edtReNewPass.getText().toString();
                userPresenter.checkInfoChangePassword(oldPass, newPass, confirmNewPass);
            }
        });
        mDialog.show();
    }

    private void openDialogOptionChangeAvatar(){
        mDialog = new Dialog(StaffUserProfileActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_change_avatar_staff);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView txtCloseDialog = mDialog.findViewById(R.id.textView_CloseDialog);
        txtCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        LinearLayout llGallery = mDialog.findViewById(R.id.linearLayout_choose_gallery);
        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
                }
            }
        });
        mDialog.show();
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void logout(){
        showMessage("Password is changed");
        Intent intent = new Intent(StaffUserProfileActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        SharedPreferences sharedPreferences = getSharedPreferences(Const.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Const.SHARED_PREFERENCE_IS_LOGIN);
        editor.remove(Const.SHARED_PREFERENCE_ID_USER);
        editor.commit();

        startActivity(intent);
        finish();
    }

    @Override
    public void onFailChangePassword(String message) {
        showMessage(message);
    }

    @Override
    public void onSuccessChangePassword() {
        logout();
    }
}