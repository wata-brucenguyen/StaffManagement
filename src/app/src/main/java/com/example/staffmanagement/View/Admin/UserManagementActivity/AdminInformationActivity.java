package com.example.staffmanagement.View.Admin.UserManagementActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Admin.SendNotificationActivity.Service.Broadcast;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.View.Ultils.ImageHandler;
import com.example.staffmanagement.ViewModel.Admin.AdminInformationViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class AdminInformationActivity extends AppCompatActivity {
    private CheckNetwork mCheckNetwork;
    private EditText editText_Email, editText_Phonenumber, editText_Address, editText_Role;
    private EditText tv_eup_name, tv_eup_phone, tv_eup_email, tv_eup_address, editTextPassword, editTextNewPassword, editTextConfirmPassword;
    private TextView txt_NameAdmin, txtCloseDialog, txtAccept;
    private ImageView back_icon, edit_icon, imvAvatar, imvChangeAvatarDialog;
    private TextInputLayout textInputLayoutNewPassword, textInputLayoutOldPassword
            , textInputLayoutConfirmPassword
            , textInputLayoutNameAdmin
            , textInputLayoutEmail
            , textInputLayoutPhoneNumber;
    private ProgressDialog mProgressDialog;
    private String action;
    private Dialog mDialog;
    private Bitmap mBitmap;
    public static final String ADMIN_PROFILE = "ADMIN_PROFILE";
    public static final String STAFF_PROFILE = "STAFF_PROFILE";
    private AdminInformationViewModel mViewModel;
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;
    private boolean isChooseAvatar = false;
    private Broadcast mBroadcast;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AdminAppTheme);
        setContentView(R.layout.activity_admin_information);
        //overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        mViewModel = ViewModelProviders.of(this).get(AdminInformationViewModel.class);
        mapping();
        eventRegister();
        checkAction();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCheckNetwork = new CheckNetwork(this);
        mCheckNetwork.registerCheckingNetwork();

        mBroadcast = new Broadcast();
        IntentFilter filter = new IntentFilter("Notification");
        registerReceiver(mBroadcast, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCheckNetwork.unRegisterCheckingNetwork();
        unregisterReceiver(mBroadcast);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialog = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_GALLERY && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                showMessage("You don't have gallery");
            }
        } else if (requestCode == REQUEST_CODE_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            else {
                showMessage("You don't have camera");
            }
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
            mBitmap = (Bitmap) data.getExtras().get("data");
            imvChangeAvatarDialog.setImageBitmap(mBitmap);
        }
    }

    private void mapping() {
        txt_NameAdmin = findViewById(R.id.txt_NameAdmin);
        editText_Email = findViewById(R.id.editText_Email);
        editText_Phonenumber = findViewById(R.id.editText_PhoneNumber);
        editText_Address = findViewById(R.id.editText_Address);
        editText_Role = findViewById(R.id.editText_Role);

        back_icon = findViewById(R.id.back_icon);
        edit_icon = findViewById(R.id.edit_icon);
        imvAvatar = findViewById(R.id.imvAvatarUserProfile);
    }

    private void eventRegister() {
        back_icon.setOnClickListener(view -> finish());
        edit_icon.setOnClickListener(view -> setUpPopUpMenu());
        imvAvatar.setOnClickListener(view -> {
            openDialogOptionChangeAvatar();
        });


        mViewModel.getUserLD().observe(this, user -> {
            if (user != null) {
                setDataToLayout();
            }

            if (mProgressDialog != null && mProgressDialog.isShowing())
                dismissProgressDialog();

            if (mDialog != null && mDialog.isShowing())
                mDialog.dismiss();
        });

    }

    private void setUpPopUpMenu() {
        PopupMenu popupMenu = new PopupMenu(AdminInformationActivity.this, edit_icon);
        switch (action) {
            case ADMIN_PROFILE:
                popupMenu.getMenuInflater().inflate(R.menu.menu_item_edit_admin, popupMenu.getMenu());
                popupMenuAdminProfile(popupMenu);
                break;
            case STAFF_PROFILE:
                popupMenu.getMenuInflater().inflate(R.menu.menu_item_edit_staff, popupMenu.getMenu());
                popupMenuStaffProfile(popupMenu);
                break;
        }
        popupMenu.show();
    }

    private void checkAction() {
        Intent intent = getIntent();
        action = intent.getAction();
        switch (action) {
            case ADMIN_PROFILE:
                mViewModel.setUpUser(UserSingleTon.getInstance().getUser());
                break;
            case STAFF_PROFILE:
                mViewModel.setUpUser((User) intent.getSerializableExtra(Constant.USER_INFO_INTENT));
                break;
        }
    }

    private void loadAdminProfile() {
        txt_NameAdmin.setText(UserSingleTon.getInstance().getUser().getFullName());
        editText_Address.setText(UserSingleTon.getInstance().getUser().getAddress());
        editText_Email.setText(UserSingleTon.getInstance().getUser().getEmail());
        editText_Phonenumber.setText(UserSingleTon.getInstance().getUser().getPhoneNumber());
        editText_Role.setText(UserSingleTon.getInstance().getUser().getRole().getName());
        if (mViewModel.getUser().getAvatar() != null) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(this).load(UserSingleTon.getInstance().getUser().getAvatar()).apply(options).into(imvAvatar);
        }
    }

    private void loadStaffProfile() {
        txt_NameAdmin.setText(mViewModel.getUser().getFullName());
        editText_Address.setText(mViewModel.getUser().getAddress());
        editText_Email.setText(mViewModel.getUser().getEmail());
        editText_Phonenumber.setText(mViewModel.getUser().getPhoneNumber());
        editText_Role.setText(mViewModel.getUser().getRole().getName());
        if (mViewModel.getUser().getAvatar() != null) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(this).load(mViewModel.getUser().getAvatar()).apply(options).into(imvAvatar);
        }
    }

    private void getRoleNameById(int idRole) {
        switch (idRole) {
            case 1:
                loadAdminProfile();
                break;
            case 2:
                loadStaffProfile();
                break;
        }
    }

    private void setDataToLayout() {
        switch (action) {
            case ADMIN_PROFILE:
                getRoleNameById(UserSingleTon.getInstance().getUser().getRole().getId());
                break;
            case STAFF_PROFILE:
                getRoleNameById(mViewModel.getUser().getRole().getId());
                break;
        }
    }

    private void popupMenuAdminProfile(PopupMenu popupMenu) {
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.popup_menu_item_change_password:
                    showChangePasswordDialog();
                    break;
                case R.id.popup_menu_item_edit_profile:
                    editProfile();
                    break;
                case R.id.popup_menu_item_change_avatar:
                    openDialogOptionChangeAvatar();
                    break;
            }
            return false;
        });
    }

    private void popupMenuStaffProfile(PopupMenu popupMenu) {
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.popup_menu_item_reset_password) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminInformationActivity.this);
                builder.setTitle("Do you want to reset password ?");
                builder.setPositiveButton("OK", (dialog, id) -> {
                    // User clicked OK button
                    if (CheckNetwork.checkInternetConnection(AdminInformationActivity.this)) {
                        mViewModel.resetPassword(mViewModel.getUser().getId());
                    }

                });
                builder.setNegativeButton("Cancel", (dialog, id) -> {
                    // User cancelled the dialog
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            return false;
        });
    }

    private void showChangePasswordDialog() {
        mDialog = new Dialog(AdminInformationActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_change_admin_password);
        mDialog.setCanceledOnTouchOutside(false);

        Window window = mDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        mappingChangePassword();
        // close dialog
        txtCloseDialog.setOnClickListener(view -> mDialog.dismiss());
        txtAccept.setOnClickListener(view -> {
            clearChangePassDialog();
            if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
                textInputLayoutOldPassword.setError("This field is empty");
                textInputLayoutOldPassword.requestFocus();
                return;
            } else if (TextUtils.isEmpty(editTextNewPassword.getText().toString())) {
                textInputLayoutNewPassword.setError("This field is empty");
                textInputLayoutNewPassword.requestFocus();
                return;
            } else if (editTextNewPassword.getText().toString().length() < 6 || editTextNewPassword.getText().toString().length() > 16) {
                textInputLayoutNewPassword.setError("Password must be 6-16 characters");
                textInputLayoutNewPassword.requestFocus();
                return;
            } else if (TextUtils.isEmpty(editTextConfirmPassword.getText().toString())) {
                textInputLayoutConfirmPassword.setError("This field is empty");
                textInputLayoutConfirmPassword.requestFocus();
                return;
            } else if (!GeneralFunc.getMD5(editTextPassword.getText().toString()).equals(UserSingleTon.getInstance().getUser().getPassword())) {
                textInputLayoutOldPassword.setError("Old password is incorrect");
                textInputLayoutOldPassword.requestFocus();
                return;
            } else if (!editTextNewPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
                textInputLayoutConfirmPassword.setError("Confirm password is different from new password");
                textInputLayoutConfirmPassword.requestFocus();
                return;
            }

            if (CheckNetwork.checkInternetConnection(AdminInformationActivity.this)) {
                newProgressDialog();
                showProgressDialog();
                UserSingleTon.getInstance().getUser().setPassword(GeneralFunc.getMD5(editTextNewPassword.getText().toString()));
                mViewModel.update();
                GeneralFunc.logout(AdminInformationActivity.this);
            }
        });
        GeneralFunc.setHideKeyboardOnTouch(this, mDialog.findViewById(R.id.changePassword));
        mDialog.show();
    }

    private void mappingChangePassword() {
        editTextPassword = mDialog.findViewById(R.id.editText_Password);
        editTextNewPassword = mDialog.findViewById(R.id.editText_New_Password);
        editTextConfirmPassword = mDialog.findViewById(R.id.editText_Confirm_Password);
        txtAccept = mDialog.findViewById(R.id.textView_acceptChangePassword_admin);
        txtCloseDialog = mDialog.findViewById(R.id.textView_CloseChangePassword);

        textInputLayoutNewPassword = mDialog.findViewById(R.id.textInputLayoutNewPassword);
        textInputLayoutOldPassword = mDialog.findViewById(R.id.textInputLayoutOldPassword);
        textInputLayoutConfirmPassword = mDialog.findViewById(R.id.textInputLayoutConfirmPassword);
    }

    private void editProfile() {
        mDialog = new Dialog(AdminInformationActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_edit_admin_profile);
        mDialog.setCanceledOnTouchOutside(false);

        Window window = mDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        mappingEditProfile();
        // close dialog
        txtCloseDialog.setOnClickListener(view -> mDialog.dismiss());

        //accept edit profile
        txtAccept.setOnClickListener(view -> {
            clearEditProfileDialog();
            if (TextUtils.isEmpty(tv_eup_name.getText().toString())) {
                textInputLayoutNameAdmin.setError("This field is empty");
                textInputLayoutNameAdmin.requestFocus();
                return;
            }
            //check phone number
            if (tv_eup_phone.getText().toString().length() < 10 || tv_eup_phone.getText().toString().length() > 12) {
                textInputLayoutPhoneNumber.setError("Phone number must be from 10 to 12");
                textInputLayoutPhoneNumber.requestFocus();
                return;
            }

            //check email
            String emailPattern = "^[a-z][a-z0-9_.]{1,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";
            if (tv_eup_email.length() > 0 && !Pattern.matches(emailPattern, tv_eup_email.getText().toString())) {
                textInputLayoutEmail.setError("Email format is wrong");
                textInputLayoutEmail.requestFocus();
                return;
            }

            if (CheckNetwork.checkInternetConnection(AdminInformationActivity.this)) {
                newProgressDialog();
                showProgressDialog();
                UserSingleTon.getInstance().getUser().setFullName(tv_eup_name.getText().toString());
                UserSingleTon.getInstance().getUser().setEmail(tv_eup_email.getText().toString());
                UserSingleTon.getInstance().getUser().setPhoneNumber(tv_eup_phone.getText().toString());
                UserSingleTon.getInstance().getUser().setAddress(tv_eup_address.getText().toString());
                mViewModel.update();
                GeneralFunc.setStateChangeProfile(AdminInformationActivity.this, true);
            }
        });
        GeneralFunc.setHideKeyboardOnTouch(this, mDialog.findViewById(R.id.dialogEditProfile));
        mDialog.show();


    }

    private void mappingEditProfile() {
        txtCloseDialog = mDialog.findViewById(R.id.textView_CloseEditAdminProfile);
        tv_eup_name = mDialog.findViewById(R.id.tv_eup_Name);
        tv_eup_phone = mDialog.findViewById(R.id.tv_eup_Phone);
        tv_eup_address = mDialog.findViewById(R.id.tv_eup_Address);
        tv_eup_email = mDialog.findViewById(R.id.tv_eup_Email);
        txtAccept = mDialog.findViewById(R.id.textView_accept);

        textInputLayoutNameAdmin = mDialog.findViewById(R.id.textInputLayoutNameAdmin);
        textInputLayoutEmail = mDialog.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPhoneNumber = mDialog.findViewById(R.id.textInputLayoutPhoneNumber);

        tv_eup_name.setText(UserSingleTon.getInstance().getUser().getFullName());
        tv_eup_phone.setText(UserSingleTon.getInstance().getUser().getPhoneNumber());
        tv_eup_email.setText(UserSingleTon.getInstance().getUser().getEmail());
        tv_eup_address.setText(UserSingleTon.getInstance().getUser().getAddress());


    }

    private void showMessage(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void openDialogOptionChangeAvatar() {
        mDialog = new Dialog(AdminInformationActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_change_avatar_staff);
        mDialog.setCanceledOnTouchOutside(false);

        imvChangeAvatarDialog = mDialog.findViewById(R.id.imageView_change_avatar_dialog);

        if (mViewModel.getUser().getAvatar() != null) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(this).load(UserSingleTon.getInstance().getUser().getAvatar()).apply(options).into(imvChangeAvatarDialog);
        }

        Window window = mDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        //close dialog
        TextView txtCloseDialog = mDialog.findViewById(R.id.textView_CloseDialog);
        txtCloseDialog.setOnClickListener(view -> {
            mDialog.dismiss();
            isChooseAvatar = false;
        });

        //accept change avatar
        TextView txtApply = mDialog.findViewById(R.id.textView_ApplyDialog);
        txtApply.setOnClickListener(view -> {
            if (isChooseAvatar) {
                if (CheckNetwork.checkInternetConnection(AdminInformationActivity.this)) {
                    newProgressDialog();
                    showProgressDialog();
                    mViewModel.changeAvatar(mBitmap);
                    isChooseAvatar = false;
                    GeneralFunc.setStateChangeProfile(AdminInformationActivity.this, true);
                }
            } else {
                showMessage("You haven't chosen image or captured image from camera");
            }
        });

        //choose from gallery
        LinearLayout llGallery = mDialog.findViewById(R.id.linearLayout_choose_gallery);
        llGallery.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });

        //choose from captured image
        LinearLayout llCamera = mDialog.findViewById(R.id.linearLayout_choose_camera);
        llCamera.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            }
        });
        mDialog.show();
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

    private void clearChangePassDialog(){
        textInputLayoutOldPassword.setError(null);
        textInputLayoutNewPassword.setError(null);
        textInputLayoutConfirmPassword.setError(null);
    }

    private void clearEditProfileDialog(){
        textInputLayoutNameAdmin.setError(null);
        textInputLayoutPhoneNumber.setError(null);
        textInputLayoutEmail.setError(null);
    }
}