package com.example.staffmanagement.View.Admin.UserManagementActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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


import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Presenter.Admin.AdminInformationPresenter;
import com.example.staffmanagement.View.Main.LogInActivity;
import com.example.staffmanagement.View.Ultils.Constant;

import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.Model.Database.Entity.User;

import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.View.Ultils.ImageHandler;


import java.lang.ref.WeakReference;
import java.util.regex.Pattern;


public class AdminInformationActivity extends AppCompatActivity implements AdminInformationInterface {
    private EditText editText_Email, editText_Phonenumber, editText_Address, editText_Role;
    private EditText tv_eup_name, tv_eup_phone, tv_eup_email, tv_eup_address, editTextPassword, editTextNewPassword, editTextConfirmPassword;
    private TextView txt_NameAdmin, txtCloseDialog, txtAccept;
    private ImageView back_icon, edit_icon, imvAvatar, imvChangeAvatarDialog;
    private String action;
    private User mUser;
    private Dialog mDialog;
    private ProgressDialog mProgressDialog;
    private Bitmap mBitmap;
    public static final String ADMIN_PROFILE = "ADMIN_PROFILE";
    public static final String STAFF_PROFILE = "STAFF_PROFILE";
    private AdminInformationPresenter mPresenter;
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;
    private boolean isChooseAvatar = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AdminAppTheme);
        setContentView(R.layout.activity_admin_information);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        WeakReference<Context> weakReference = new WeakReference<>(getApplicationContext());
        mPresenter = new AdminInformationPresenter(weakReference, this);
        mapping();
        checkAction();
        setDataToLayout();
        eventRegister();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_GALLERY && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            if(intent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }else{
                showMessage("You don't have gallery");
            }
        } else if (requestCode == REQUEST_CODE_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            else{
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
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpPopUpMenu();
            }
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

                break;
            case STAFF_PROFILE:

                mUser = (User) intent.getSerializableExtra(Constant.USER_INFO_INTENT);
                break;
        }
    }

    @Override
    public void loadAdminProfile(String roleName) {
        editText_Role.setText(roleName);
        txt_NameAdmin.setText(UserSingleTon.getInstance().getUser().getFullName());
        editText_Address.setText(UserSingleTon.getInstance().getUser().getAddress());
        editText_Email.setText(UserSingleTon.getInstance().getUser().getEmail());
        editText_Phonenumber.setText(UserSingleTon.getInstance().getUser().getPhoneNumber());
        ImageHandler.loadImageFromBytes(this, UserSingleTon.getInstance().getUser().getAvatar(), imvAvatar);
    }

    @Override
    public void loadStaffProfile(String roleName) {
        editText_Role.setText(roleName);
        txt_NameAdmin.setText(mUser.getFullName());
        editText_Address.setText(mUser.getAddress());
        editText_Email.setText(mUser.getEmail());
        editText_Phonenumber.setText(mUser.getPhoneNumber());
        ImageHandler.loadImageFromBytes(this, mUser.getAvatar(), imvAvatar);
    }

    private void setDataToLayout() {
        switch (action) {
            case ADMIN_PROFILE:
                mPresenter.getRoleNameById(UserSingleTon.getInstance().getUser().getIdRole());
                break;
            case STAFF_PROFILE:
                mPresenter.getRoleNameById(mUser.getIdRole());
                break;
        }
    }

    private void popupMenuAdminProfile(PopupMenu popupMenu) {
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
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
            }
        });
    }

    private void popupMenuStaffProfile(PopupMenu popupMenu) {
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.popup_menu_item_reset_password) {

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

    private void showChangePasswordDialog() {

        mDialog = new Dialog(AdminInformationActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_change_admin_password);
        mDialog.setCanceledOnTouchOutside(false);

        Window window = mDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        mappingChangePassword();


        // close dialog

        txtCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        txtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
                    showMessage("Password is empty");
                    editTextPassword.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(editTextNewPassword.getText().toString())) {
                    showMessage("New password is empty");
                    editTextNewPassword.requestFocus();
                    return;
                } else if(editTextNewPassword.getText().toString().length() <6 || editTextNewPassword.getText().toString().length() >16){
                    showMessage("Password must be 9-16 characters");
                    editTextNewPassword.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(editTextConfirmPassword.getText().toString())) {
                    showMessage("Confirm password is empty");
                    editTextConfirmPassword.requestFocus();
                    return;
                } else if (!editTextPassword.getText().toString().equals(UserSingleTon.getInstance().getUser().getPassword())) {
                    showMessage("Old password is incorrect");
                    editTextPassword.requestFocus();
                    return;
                }
                else if (!editTextNewPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
                    showMessage("New password is different from confirm password");
                    editTextConfirmPassword.requestFocus();
                    return;
                }

                mPresenter.changePassword(UserSingleTon.getInstance().getUser().getId(), editTextNewPassword.getText().toString());
                GeneralFunc.logout(AdminInformationActivity.this, LogInActivity.class);
            }
        });
        mDialog.show();
    }

    private void mappingChangePassword() {
        editTextPassword = mDialog.findViewById(R.id.editText_Password);
        editTextNewPassword = mDialog.findViewById(R.id.editText_New_Password);
        editTextConfirmPassword = mDialog.findViewById(R.id.editText_Confirm_Password);
        txtAccept = mDialog.findViewById(R.id.textView_acceptChangePassword_admin);
        txtCloseDialog = mDialog.findViewById(R.id.textView_CloseChangePassword);
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

        txtCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        //accept edit profile

        txtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(tv_eup_name.getText().toString())) {
                    showMessage("Full name is empty");
                    tv_eup_name.requestFocus();
                    return;
                }
                //check phone number
                if (tv_eup_phone.getText().toString().length() < 10 || tv_eup_phone.getText().toString().length() > 12) {
                    showMessage("Phone number must be from 10 to 12");
                    tv_eup_phone.requestFocus();
                    return;
                }

                //check email
                String emailPattern = "^[a-z][a-z0-9_.]{1,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";
                if (tv_eup_email.length() > 0 && !Pattern.matches(emailPattern, tv_eup_email.getText().toString())) {
                    showMessage("Email format is wrong");
                    tv_eup_email.requestFocus();
                    return;
                }

                UserSingleTon.getInstance().getUser().setFullName(tv_eup_name.getText().toString());
                UserSingleTon.getInstance().getUser().setEmail(tv_eup_email.getText().toString());
                UserSingleTon.getInstance().getUser().setPhoneNumber(tv_eup_phone.getText().toString());
                UserSingleTon.getInstance().getUser().setAddress(tv_eup_address.getText().toString());
                mPresenter.update(UserSingleTon.getInstance().getUser());
                mDialog.dismiss();
            }
        });

        mDialog.show();


    }

    private void mappingEditProfile() {
        txtCloseDialog = mDialog.findViewById(R.id.textView_CloseEditAdminProfile);
        tv_eup_name = mDialog.findViewById(R.id.tv_eup_Name);
        tv_eup_phone = mDialog.findViewById(R.id.tv_eup_Phone);
        tv_eup_address = mDialog.findViewById(R.id.tv_eup_Address);
        tv_eup_email = mDialog.findViewById(R.id.tv_eup_Email);
        txtAccept = mDialog.findViewById(R.id.textView_accept);

        tv_eup_name.setText(UserSingleTon.getInstance().getUser().getFullName());
        tv_eup_phone.setText(UserSingleTon.getInstance().getUser().getPhoneNumber());
        tv_eup_email.setText(UserSingleTon.getInstance().getUser().getEmail());
        tv_eup_address.setText(UserSingleTon.getInstance().getUser().getAddress());
    }

    @Override
    public void showChangePassword(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dismissProgressDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog.show();
    }

    @Override
    public void createNewProgressDialog(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(message);
    }

    @Override
    public void onSuccessChangeAvatar() {
        ImageHandler.loadImageFromBytes(this, UserSingleTon.getInstance().getUser().getAvatar(), imvAvatar);
        isChooseAvatar = false;
        mDialog.dismiss();
        GeneralFunc.setStateChangeProfile(this, true);
    }

    @Override
    public void onSuccessUpdateProfile() {
        txt_NameAdmin.setText(UserSingleTon.getInstance().getUser().getFullName());
        editText_Email.setText(UserSingleTon.getInstance().getUser().getEmail());
        editText_Phonenumber.setText(UserSingleTon.getInstance().getUser().getPhoneNumber());
        editText_Address.setText(UserSingleTon.getInstance().getUser().getAddress());
        GeneralFunc.setStateChangeProfile(this, true);
    }

    private void openDialogOptionChangeAvatar() {
        mDialog = new Dialog(AdminInformationActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_change_avatar_staff);
        mDialog.setCanceledOnTouchOutside(false);

        imvChangeAvatarDialog = mDialog.findViewById(R.id.imageView_change_avatar_dialog);
        ImageHandler.loadImageFromBytes(this, UserSingleTon.getInstance().getUser().getAvatar(), imvChangeAvatarDialog);

        Window window = mDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        //close dialog
        TextView txtCloseDialog = mDialog.findViewById(R.id.textView_CloseDialog);
        txtCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                isChooseAvatar = false;
            }
        });

        //accept change avatar
        TextView txtApply = mDialog.findViewById(R.id.textView_ApplyDialog);
        txtApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChooseAvatar) {
                    mPresenter.changeAvatar(mBitmap);
                } else {
                    showMessage("You don't choose image or captured image from camera");
                }
            }
        });

        //choose from gallery
        LinearLayout llGallery = mDialog.findViewById(R.id.linearLayout_choose_gallery);
        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
                }
            }
        });

        //choose from captured image
        LinearLayout llCamera = mDialog.findViewById(R.id.linearLayout_choose_camera);
        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                }
            }
        });
        mDialog.show();
    }
}