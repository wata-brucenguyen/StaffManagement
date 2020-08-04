package com.example.staffmanagement.Presenter.Staff;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Staff.Home.StaffHomeInterface;
import com.example.staffmanagement.View.Ultils.ImageHandler;

import java.lang.ref.WeakReference;

public class StaffHomePresenter {
    private StaffHomeInterface mInterface;
    private Context mContext;

    public StaffHomePresenter(StaffHomeInterface mInterface, Context context) {
        WeakReference<Context> weak = new WeakReference<>(context);
        this.mInterface = mInterface;
        this.mContext = weak.get();

    }

    public void loadHeaderDrawerNavigation(final Context context, final ImageView avatar, final TextView txtName, final TextView txtEmail) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageHandler.loadImageFromBytes(mContext, UserSingleTon.getInstance().getUser().getAvatar(), avatar);
                        txtName.setText(UserSingleTon.getInstance().getUser().getFullName());
                        txtEmail.setText(UserSingleTon.getInstance().getUser().getEmail());
                    }
                });
            }
        }).start();
    }
}
