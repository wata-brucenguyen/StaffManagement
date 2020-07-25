package com.example.staffmanagement.Admin.MainAdminActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Admin.Const;
import com.example.staffmanagement.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.Database.Data.SeedData;
import com.example.staffmanagement.Database.Entity.User;
import com.example.staffmanagement.Presenter.RequestPresenter;
import com.example.staffmanagement.Presenter.UserPresenter;
import com.example.staffmanagement.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<User> userArrayList;
    private RequestPresenter requestPresenter;
    private UserPresenter userPresenter;
    private MainAdminInterface adminInterface;
    public UserAdapter(Context mContext, ArrayList<User> userArrayList, RequestPresenter requestPresenter,UserPresenter userPresenter, MainAdminInterface adminInterface) {
        this.mContext = mContext;
        this.userArrayList = userArrayList;
        this.requestPresenter = requestPresenter;
        this.userPresenter = userPresenter;
        this.adminInterface = adminInterface;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView =layoutInflater.inflate(R.layout.item_user,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.ViewHolder holder, final int position) {
        holder.getTxtName().setText(position+1+". "+ userArrayList.get(position).getFullName());
        holder.getTxtRole().setText(requestPresenter.getRoleNameById(userArrayList.get(position).getIdRole()));
        int soluong = requestPresenter.getCountWaitingForRequest(userArrayList.get(position).getId());
        Log.i("NUMBER",soluong + "gg");
        if(soluong > 0){
            holder.getTxtRequestNumber().setText(String.valueOf(soluong));
        } else
            holder.getTxtRequestNumber().setVisibility(View.INVISIBLE);
        holder.getImgMore().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder, userArrayList.get(position));
            }
        });
    }

    private  void showPopupMenu(ViewHolder holder, final User user){
        PopupMenu popupMenu=new PopupMenu(mContext, holder.imgMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_item_user,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menuViewProfile:{

                        Intent intent = new Intent(mContext, AdminInformationActivity.class);
                        intent.setAction(AdminInformationActivity.STAFF_PROFILE);
                        intent.putExtra(Const.USER_INFO_INTENT,user);
                        ((Activity) mContext).startActivity(intent);
                        break;
                    }
                    case R.id.menuViewAllRequest:{

                        break;
                    }
                    case R.id.menuDelete:{
                        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                        builder.setTitle("Do you want to delete user ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                userPresenter.deleteUser(user.getId());
                                adminInterface.setupList();
//                                Log.d("aaa",user.getFullName());
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();
                        break;
                    }
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName,txtRole, txtRequestNumber;
        private ImageView imgMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.textViewName);
            txtRole=itemView.findViewById(R.id.textViewRole);
            txtRequestNumber=itemView.findViewById(R.id.textViewRequestNumber);
            imgMore = itemView.findViewById(R.id.imageViewMore);
        }

        public TextView getTxtName() {
            return txtName;
        }

        public void setTxtName(TextView txtName) {
            this.txtName = txtName;
        }

        public TextView getTxtRole() {
            return txtRole;
        }

        public void setTxtRole(TextView txtRole) {
            this.txtRole = txtRole;
        }

        public TextView getTxtRequestNumber() {
            return txtRequestNumber;
        }

        public void setTxtRequestNumber(TextView txtRequestNumber) {
            this.txtRequestNumber = txtRequestNumber;
        }

        public ImageView getImgMore() {
            return imgMore;
        }

        public void setImgMore(ImageView imgMore) {
            this.imgMore = imgMore;
        }
    }
}
