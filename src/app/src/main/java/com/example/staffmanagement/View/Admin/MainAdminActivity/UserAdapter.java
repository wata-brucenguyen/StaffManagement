package com.example.staffmanagement.View.Admin.MainAdminActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Presenter.Admin.UserListPresenter;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestActivity;
import com.example.staffmanagement.View.Ultils.Constant;

import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<User> userArrayList;
    private UserListPresenter mPresenter;
    private MainAdminInterface mInterface;
    private final int ITEM_VIEW_TYPE = 1;
    private final int LOADING_VIEW_TYPE = 2;


    public UserAdapter(Context mContext, ArrayList<User> userArrayList, UserListPresenter mPresenter) {
        this.mContext = mContext;
        this.userArrayList = userArrayList;
        this.mPresenter = mPresenter;
    }


    public void deleteUser(User item){
        for(int i=0;i<userArrayList.size();i++){
            if(item.getId() == userArrayList.get(i).getId()){
                UserDbHandler db =new UserDbHandler(mContext);
                db.update(item);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return userArrayList.get(position) == null ? LOADING_VIEW_TYPE : ITEM_VIEW_TYPE ;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        if(viewType == ITEM_VIEW_TYPE){
            v = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_user,parent,false);
            return new ViewHolder(v);
        } else {
            v = ((Activity)mContext).getLayoutInflater().inflate(R.layout.view_load_more,parent,false);
            return new LoadingViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof LoadingViewHolder){
            return;
        }

        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.getTxtName().setText(userArrayList.get(position).getFullName());
        String role=mPresenter.getRoleNameById(userArrayList.get(position).getIdRole());

        if(userArrayList.get(position).getIdRole() == 1){
            viewHolder.getTxtRole().setTextColor(Color.RED);
        }
        viewHolder.getTxtRole().setText(role);

        int soluong = mPresenter.getCountWaitingForRequest(userArrayList.get(position).getId());
        Log.i("NUMBER",soluong + "gg");
        if(soluong > 0){
            viewHolder.getTxtRequestNumber().setText(String.valueOf(soluong));
        } else
            viewHolder.getTxtRequestNumber().setVisibility(View.INVISIBLE);

        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserRequestActivity.class);
                intent.putExtra("name",userArrayList.get(position).getFullName());
                mContext.startActivity(intent);
            }
        });
        viewHolder.getImgMore().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(viewHolder, userArrayList.get(position));

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
                        intent.putExtra(Constant.USER_INFO_INTENT,user);
                         mContext.startActivity(intent);
                        break;
                    }
                    case R.id.menuViewRequest:{
                        Intent intent = new Intent(mContext, UserRequestActivity.class);
                        intent.putExtra("name",user.getFullName());
                        mContext.startActivity(intent);
                        break;
                    }
                    case R.id.menuDelete:{
                        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                        builder.setTitle("Do you want to delete user ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mPresenter.deleteUser(user.getId());
                                mInterface.setupList();
//                                Log.d("aaa",user.getFullName());
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
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

    class LoadingViewHolder extends RecyclerView.ViewHolder{
        public View view;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName,txtRole, txtRequestNumber;
        private ImageView imgMore;
        private View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txtName=itemView.findViewById(R.id.textViewName);
            txtRole=itemView.findViewById(R.id.textViewRole);
            txtRequestNumber=itemView.findViewById(R.id.textViewRequestNumber);
            imgMore = itemView.findViewById(R.id.imageViewMore);
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
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
