package com.example.staffmanagement.Presenter.Admin;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.staffmanagement.View.Admin.Home.AdminHomeInterface;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Ultils.ImageHandler;

import java.lang.ref.WeakReference;

public class AdminHomePresenter {

    private Context mContext;
    private AdminHomeInterface mInterface;

    public AdminHomePresenter(Context context, AdminHomeInterface mInterface) {
        WeakReference<Context> weakReference = new WeakReference<>(context);
        this.mContext = weakReference.get();
        this.mInterface = mInterface;
    }

    public void loadHeaderDrawerNavigation(final Context context, final ImageView imgAvatar, final TextView txtName, final TextView txtMail){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageHandler.loadImageFromBytes(mContext, UserSingleTon.getInstance().getUser().getAvatar(),imgAvatar);
                        txtName.setText(UserSingleTon.getInstance().getUser().getFullName());
                        txtMail.setText(UserSingleTon.getInstance().getUser().getEmail());
                    }
                });

            }
        }).start();
    }
}
