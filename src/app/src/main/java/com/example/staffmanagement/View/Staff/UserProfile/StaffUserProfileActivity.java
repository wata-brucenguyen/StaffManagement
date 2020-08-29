package com.example.staffmanagement.View.Staff.UserProfile;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Main.LoginActivity;
import com.example.staffmanagement.View.Notification.Service.Broadcast;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.View.Ultils.ImageHandler;
import com.example.staffmanagement.ViewModel.Staff.StaffUserProfileVM;

import java.util.Objects;
import java.util.regex.Pattern;

public class StaffUserProfileActivity extends AppCompatActivity {

    private TextView txtName, txtRole, txtEmail, txtPhoneNumber, txtAddress, txtCloseDialog, txt_eup_accept;
    private EditText tv_eup_name, tv_eup_phone, tv_eup_email, tv_eup_address;
    private ImageView imvBack, imvEdit, imvChangeAvatarDialog, imvAvatar;
    private Dialog mDialog;
    private Bitmap mBitmap;
    private ProgressDialog mProgressDialog;
    private Broadcast mBroadcast;
    private boolean isChooseAvatar = false;
    private StaffUserProfileVM mViewModel;
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.StaffAppTheme);
        setContentView(R.layout.activity_user_profile);
        mapping();
        mViewModel = ViewModelProviders.of(StaffUserProfileActivity.this).get(StaffUserProfileVM.class);
        eventRegister();
        mViewModel.setUpUser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialog = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBroadcast = new Broadcast();
        IntentFilter filter = new IntentFilter("Notification");
        registerReceiver(mBroadcast, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcast);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_GALLERY && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_GALLERY);
        } else if (requestCode == REQUEST_CODE_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            isChooseAvatar = true;
            Uri uri = data.getData();
            mBitmap = ImageHandler.getBitmapFromUriAndShowImage(this, uri, imvChangeAvatarDialog);
        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            isChooseAvatar = true;
            mBitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            imvChangeAvatarDialog.setImageBitmap(mBitmap);
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
        imvAvatar = findViewById(R.id.imvAvatarUserProfile);
    }

    private void setDataOnView(User user) {
        txtName.setText(user.getFullName());
        txtEmail.setText(user.getEmail());
        txtPhoneNumber.setText(user.getPhoneNumber());
        txtAddress.setText(user.getAddress());
        if (UserSingleTon.getInstance().getUser().getAvatar() != null){
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(this).load(UserSingleTon.getInstance().
                    getUser().getAvatar()).apply(options).into(imvAvatar);
        }
    }

    private void eventRegister() {
        imvBack.setOnClickListener(view -> finish());
        imvEdit.setOnClickListener(view -> setUpBtnEditProfile());
        imvAvatar.setOnClickListener(view -> openDialogOptionChangeAvatar());

        mViewModel.getUserLD().observe(this, user -> {
            if (user != null) {
                setDataOnView(user);
            }
            if (mProgressDialog != null && mProgressDialog.isShowing())
                dismissProgressDialog();
            if (mDialog != null && mDialog.isShowing())
                dismissDialog();
        });

        mViewModel.getRoleNameLD().observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                txtRole.setText(s);
            }
        });
    }

    private void setUpBtnEditProfile() {
        PopupMenu popupMenu = new PopupMenu(this, imvEdit);
        popupMenu.getMenuInflater().inflate(R.menu.menu_edit_user_profile, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
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
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void mappingEditUserProfile() {
        txtCloseDialog = mDialog.findViewById(R.id.textView_CloseEditUserProfile);
        tv_eup_name = mDialog.findViewById(R.id.tv_eup_Name);
        tv_eup_phone = mDialog.findViewById(R.id.tv_eup_Phone);
        tv_eup_address = mDialog.findViewById(R.id.tv_eup_Address);
        tv_eup_email = mDialog.findViewById(R.id.tv_eup_Email);
        txt_eup_accept = mDialog.findViewById(R.id.textView_accept_filter);

        tv_eup_name.setText(UserSingleTon.getInstance().getUser().getFullName());
        tv_eup_phone.setText(UserSingleTon.getInstance().getUser().getPhoneNumber());
        tv_eup_email.setText(UserSingleTon.getInstance().getUser().getEmail());
        tv_eup_address.setText(UserSingleTon.getInstance().getUser().getAddress());
    }

    private void registerEventEditUserProfile() {
        txtCloseDialog.setOnClickListener(v -> mDialog.dismiss());
        txt_eup_accept.setOnClickListener(v -> {

            if(!GeneralFunc.checkInternetConnection(StaffUserProfileActivity.this))
                return;

            newProgressDialog();
            showProgressDialog();
            // check user name
            String name = tv_eup_name.getText().toString();
            if (TextUtils.isEmpty(name)) {
                showMessage("Name field is empty");
                tv_eup_name.requestFocus();
                dismissProgressDialog();
                return;
            }

            // check phone number
            String phone = tv_eup_phone.getText().toString();
            if (phone.length() < 10 || phone.length() > 12) {
                showMessage("Phone number must be from 10 to 12");
                tv_eup_phone.requestFocus();
                dismissProgressDialog();
                return;
            }

            // check
            String emailPattern = "^[a-z][a-z0-9_.]{1,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";
            String email = tv_eup_email.getText().toString();
            if (email.length() > 0 && !Pattern.matches(emailPattern, email)) {
                showMessage("Email format is wrong");
                tv_eup_email.requestFocus();
                dismissProgressDialog();
                return;
            }

            mViewModel.getUser().setFullName(name);
            mViewModel.getUser().setPhoneNumber(phone);
            mViewModel.getUser().setEmail(email);
            mViewModel.getUser().setAddress(tv_eup_address.getText().toString());
            mViewModel.updateUserProfile();
            showMessage("Profile is updated");
            dismissProgressDialog();
            mDialog.dismiss();
            GeneralFunc.setStateChangeProfile(StaffUserProfileActivity.this, true);
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

        imvClose.setOnClickListener(v -> mDialog.dismiss());

        btnAccept.setOnClickListener(v -> {
            if(!GeneralFunc.checkInternetConnection(StaffUserProfileActivity.this))
                return;

            newProgressDialog();
            showProgressDialog();
            String oldPass = edtOldPass.getText().toString();
            String newPass = edtNewPass.getText().toString();
            String confirmNewPass = edtReNewPass.getText().toString();
            checkInfoChangePassword(oldPass, newPass, confirmNewPass);
        });
        mDialog.show();
    }

    private void openDialogOptionChangeAvatar() {
        mDialog = new Dialog(StaffUserProfileActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_change_avatar_staff);
        mDialog.setCanceledOnTouchOutside(false);

        imvChangeAvatarDialog = mDialog.findViewById(R.id.imageView_change_avatar_dialog);
        if (UserSingleTon.getInstance().getUser().getAvatar() != null){
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(this).load(UserSingleTon.getInstance().getUser().getAvatar()).apply(options).into(imvChangeAvatarDialog);
        }
        Window window = mDialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // close dialog
        TextView txtCloseDialog = mDialog.findViewById(R.id.textView_CloseDialog);
        txtCloseDialog.setOnClickListener(view -> {
            mDialog.dismiss();
            isChooseAvatar = false;
        });

        // accept change avatar
        TextView txtAccept = mDialog.findViewById(R.id.textView_ApplyDialog);
        txtAccept.setOnClickListener(view -> {
            if (isChooseAvatar) {
                if(!GeneralFunc.checkInternetConnection(StaffUserProfileActivity.this))
                    return;
                newProgressDialog();
                showProgressDialog();
                mViewModel.changeAvatar(mBitmap);
                isChooseAvatar = false;
                GeneralFunc.setStateChangeProfile(StaffUserProfileActivity.this, true);
            } else {
                showMessage("You don't choose image or captured image from camera");
            }
        });

        // choose from gallery
        LinearLayout llGallery = mDialog.findViewById(R.id.linearLayout_choose_gallery);
        llGallery.setOnClickListener(view -> requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY));

        //choose from camera
        LinearLayout llCamera = mDialog.findViewById(R.id.linearLayout_choose_camera);
        llCamera.setOnClickListener(view -> requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA));
        mDialog.show();
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        showMessage("Password is changed");
        Intent intent = new Intent(StaffUserProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constant.SHARED_PREFERENCE_IS_LOGIN);
        editor.remove(Constant.SHARED_PREFERENCE_ID_USER);
        editor.apply();

        startActivity(intent);
        finish();
    }

    public void checkInfoChangePassword(String oldPass, String newPass, String confirmNewPass) {
        if (TextUtils.isEmpty(oldPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confirmNewPass)) {
            showMessage("Some field is empty");
            dismissProgressDialog();
            return;
        }
        if (!UserSingleTon.getInstance().getUser().getPassword().equals(GeneralFunc.getMD5(oldPass))) {
            showMessage("Old password is wrong");
            dismissProgressDialog();
            return;
        }
        if (newPass.length() < 6) {
            showMessage("New password must more 6 characters");
            dismissProgressDialog();
            return;
        }
        if (!newPass.equals(confirmNewPass)) {
            showMessage("Confirm password is wrong");
            dismissProgressDialog();
            return;
        }

        mViewModel.getUser().setPassword(GeneralFunc.getMD5(newPass));
        mViewModel.updateUserProfile();
        logout();
    }

    private void newProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Loading...");
    }

    private void showProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.show();
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    private void dismissDialog() {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }
}