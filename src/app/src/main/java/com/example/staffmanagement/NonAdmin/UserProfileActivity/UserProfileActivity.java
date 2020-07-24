package com.example.staffmanagement.NonAdmin.UserProfileActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.staffmanagement.Database.Data.UserSingleTon;
import com.example.staffmanagement.Database.Entity.User;
import com.example.staffmanagement.Presenter.RequestPresenter;
import com.example.staffmanagement.Presenter.UserPresenter;
import com.example.staffmanagement.R;

public class UserProfileActivity extends AppCompatActivity implements UserProfileNonAdminInterface{

    private TextView txtName,txtRole,txtEmail,txtPhoneNumber,txtAddress;
    private EditText tv_eup_name,tv_eup_phone,tv_eup_email,tv_eup_address;
    private ImageView imvBack,imvEdit,imvCloseDialog;
    private Button btn_eup_accept;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mapping();
        eventRegister();
        setDataOnView();
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

    private void setDataOnView(){
        txtName.setText(UserSingleTon.getInstance().getUser().getFullName());
        RequestPresenter re = new RequestPresenter(this,this);
        String roleName = re.getRoleNameById(UserSingleTon.getInstance().getUser().getIdRole());
        txtRole.setText(roleName);
        txtEmail.setText(UserSingleTon.getInstance().getUser().getEmail());
        txtPhoneNumber.setText(UserSingleTon.getInstance().getUser().getPhoneNumber());
        txtAddress.setText(UserSingleTon.getInstance().getUser().getAddress());
    }

    private void eventRegister(){
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

    private void setUpBtnEditProfile(){
        PopupMenu popupMenu =new PopupMenu(UserProfileActivity.this,imvEdit);
        popupMenu.getMenuInflater().inflate(R.menu.menu_edit_user_profile,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.item_menu_editUserProfile_nonAdmin:
                        activateEditUserProfile();
                        break;
                    case R.id.item_menu_changePassword__nonAdmin:
                        activateChangePassword();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public void activateEditUserProfile(){
        newDialog(R.layout.dialog_edit_user_profile_non_admin);
        mappingEditUserProfile();
        registerEventEditUserProfile();
    }

    public void newDialog(int layoutRes){
        dialog =new Dialog(UserProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutRes);
        dialog.setCanceledOnTouchOutside(false);
        Window window =dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void mappingEditUserProfile() {
        imvCloseDialog=dialog.findViewById(R.id.imgCloseEditUserProfile);
        tv_eup_name=dialog.findViewById(R.id.tv_eup_Name);
        tv_eup_phone=dialog.findViewById(R.id.tv_eup_Phone);
        tv_eup_address=dialog.findViewById(R.id.tv_eup_Address);
        tv_eup_email=dialog.findViewById(R.id.tv_eup_Email);
        btn_eup_accept=dialog.findViewById(R.id.btn_eup_accept);

        tv_eup_name.setText(UserSingleTon.getInstance().getUser().getFullName());
        tv_eup_phone.setText(UserSingleTon.getInstance().getUser().getPhoneNumber());
        tv_eup_email.setText(UserSingleTon.getInstance().getUser().getEmail());
        tv_eup_address.setText(UserSingleTon.getInstance().getUser().getAddress());
    }

    private void registerEventEditUserProfile() {

        imvCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_eup_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPresenter userPresenter = new UserPresenter(UserProfileActivity.this,UserProfileActivity.this);
                UserSingleTon.getInstance().getUser().setFullName(tv_eup_name.getText().toString());
                UserSingleTon.getInstance().getUser().setPhoneNumber( tv_eup_phone.getText().toString());
                UserSingleTon.getInstance().getUser().setEmail(tv_eup_email.getText().toString());
                UserSingleTon.getInstance().getUser().setAddress(tv_eup_address.getText().toString());
                userPresenter.updateUserProfile( UserSingleTon.getInstance().getUser() );
                showMessage("Profile is updated");
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void activateChangePassword(){
        newDialog(R.layout.change_user_password_dialog);
        final EditText edtOldPass=dialog.findViewById(R.id.edt_oldPassword_non_admin),
                edtNewPass=dialog.findViewById(R.id.edt_newPassword_non_admin),
                edtReNewPass=dialog.findViewById(R.id.edt_reNewPassword_non_admin);

        Button btnAccept =dialog.findViewById(R.id.btn_acceptChangePassword_non_admin);
        ImageView imvClose=dialog.findViewById(R.id.imgCloseChangePassword);

        imvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtOldPass.getText().toString()) || TextUtils.isEmpty( edtNewPass.getText().toString()) || TextUtils.isEmpty( edtReNewPass.getText().toString())) {
                    Toast.makeText(UserProfileActivity.this,"Some field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                UserPresenter userPresenter = new UserPresenter(UserProfileActivity.this,UserProfileActivity.this);
                UserSingleTon.getInstance().getUser().setPassword(edtNewPass.getText().toString());
                userPresenter.updateUserProfile(UserSingleTon.getInstance().getUser());
                showMessage("Password is changed");
            }
        });
        dialog.show();
    }

    public void showMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}