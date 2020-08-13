package com.example.staffmanagement.View.Admin.SendNotificationActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Model.LocalDb.Database.Entity.Role;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.User;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.MainAdminActivity.MainAdminDiffUtilCallBack;
import com.example.staffmanagement.View.Admin.ViewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class SendNotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private UserViewModel mViewModel;
    private SendNotificationInterface mInterface;
    private final int ITEM_VIEW_TYPE = 1;
    private final int LOADING_VIEW_TYPE = 2;

    public SendNotificationAdapter(Context mContext, UserViewModel mViewModel, SendNotificationInterface mInterface) {
        this.mContext = mContext;
        this.mViewModel = mViewModel;
        this.mInterface = mInterface;
    }

    @Override
    public int getItemViewType(int position) {
        return mViewModel.getUserList().get(position) == null ? LOADING_VIEW_TYPE : ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        if (viewType == ITEM_VIEW_TYPE) {
            v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.item_user_send_notification, parent, false);
            return new ViewHolder(v);
        } else {
            v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.view_load_more, parent, false);
            return new LoadingViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SendNotificationAdapter.LoadingViewHolder) {
            return;
        }

        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.getTxtName().setText(mViewModel.getUserList().get(position).getFullName());


        if (mViewModel.getUserList().get(position).getIdRole() == 1) {
            viewHolder.getTxtRole().setTextColor(Color.RED);

        }
        viewHolder.getTxtRole().setText(getRoleNameById(mViewModel.getUserList().get(position).getIdRole()));


        viewHolder.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mViewModel.getUserCheckList().add(mViewModel.getUserList().get(position));
            }
        });





    }

    @Override
    public int getItemCount() {
        return mViewModel.getUserList().size();
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }

    private String getRoleNameById(int idRole) {
        for (Role r : mViewModel.getRoleList()) {
            if (r.getId() == idRole)
                return r.getName();
        }
        return "Unknown";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtRole;
        private CheckBox checkBox;
        private View view;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txtName = itemView.findViewById(R.id.textViewName);
            txtRole = itemView.findViewById(R.id.textViewRole);
            checkBox = itemView.findViewById(R.id.checkBox);

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

        public TextView getTxtRole() {
            return txtRole;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
    }

    public void setData(List<User> listLoadMore){
        List<User> newListUser = new ArrayList<>(mViewModel.getUserList());
        newListUser.addAll(listLoadMore);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MainAdminDiffUtilCallBack(newListUser,mViewModel.getUserList()));
        diffResult.dispatchUpdatesTo(this);

        mViewModel.getUserList().clear();
        mViewModel.getUserList().addAll(newListUser);

    }
}
