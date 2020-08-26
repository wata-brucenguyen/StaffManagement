package com.example.staffmanagement.View.Admin.MainAdminActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestActivity;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Admin.UserListViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private UserListViewModel mViewModel;
    private MainAdminInterface mInterface;
    private final int ITEM_VIEW_TYPE = 1;
    private final int LOADING_VIEW_TYPE = 2;


    public UserAdapter(Context mContext, UserListViewModel userViewModel, MainAdminInterface mInterface) {
        this.mContext = mContext;
        this.mViewModel = userViewModel;
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
            v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.item_user, parent, false);
            return new ViewHolder(v);
        } else {
            v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.view_load_more, parent, false);
            return new LoadingViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof LoadingViewHolder) {
            return;
        }

        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.getTxtName().setText(mViewModel.getUserList().get(position).getFullName());


        if (mViewModel.getUserList().get(position).getIdRole() == 1) {
            viewHolder.getTxtRole().setTextColor(Color.RED);

        }
        viewHolder.getTxtRole().setText(getRoleNameById(mViewModel.getUserList().get(position).getIdRole()));
        viewHolder.getTxtRequestNumber().setText(mViewModel.getQuantityWaitingRequest().get(position).toString());
        if (mViewModel.getQuantityWaitingRequest().get(position) > 0)
            viewHolder.getTxtRequestNumber().setVisibility(View.VISIBLE);
        else
            viewHolder.getTxtRequestNumber().setVisibility(View.INVISIBLE);
        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserRequestActivity.class);
                intent.putExtra(Constant.USER_INFO_INTENT, mViewModel.getUserList().get(position));
                mContext.startActivity(intent);
            }
        });
        viewHolder.getImgMore().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(viewHolder, mViewModel.getUserList().get(position));

            }
        });

        setSwitch(viewHolder, position);
        viewHolder.getaSwitch().setOnClickListener(view -> {
            if(GeneralFunc.checkInternetConnection(mContext)){
                if (((SwitchCompat) view).isChecked()) {
                    viewHolder.getTxtState().setText("Lock");
                    mInterface.onChangeUserState(mViewModel.getUserList().get(position).getId(), 2);
                } else {
                    viewHolder.getTxtState().setText("Active");
                    mInterface.onChangeUserState(mViewModel.getUserList().get(position).getId(), 1);
                }
            }else{
                if(((SwitchCompat) view).isChecked())
                    ((SwitchCompat) view).setChecked(false);
                else
                    ((SwitchCompat) view).setChecked(true);
            }

        });
    }

    private void setSwitch(ViewHolder viewHolder, int position) {
        if (mViewModel.getUserList().get(position).getIdUserState() == 1) {
            viewHolder.getTxtState().setText("Active");
            viewHolder.getaSwitch().setChecked(false);
        } else if (mViewModel.getUserList().get(position).getIdUserState() == 2) {
            viewHolder.getTxtState().setText("Lock");
            viewHolder.getaSwitch().setChecked(true);
        }
    }

    private void showPopupMenu(ViewHolder holder, final User user) {
        PopupMenu popupMenu = new PopupMenu(mContext, holder.imgMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_item_user, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menuViewProfile: {
                        Intent intent = new Intent(mContext, AdminInformationActivity.class);
                        intent.setAction(AdminInformationActivity.STAFF_PROFILE);
                        intent.putExtra(Constant.USER_INFO_INTENT, user);
                        mContext.startActivity(intent);
                        break;
                    }
                    case R.id.menuViewRequest: {
                        Intent intent = new Intent(mContext, UserRequestActivity.class);
                        intent.putExtra(Constant.USER_INFO_INTENT, user);
                        mContext.startActivity(intent);
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
        return mViewModel.getUserList().size();
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtRole, txtRequestNumber, txtState;
        private ImageView imgMore;
        private View view;
        private SwitchCompat aSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txtName = itemView.findViewById(R.id.textViewName);
            txtRole = itemView.findViewById(R.id.textViewRole);
            txtRequestNumber = itemView.findViewById(R.id.textViewRequestNumber);
            txtState = itemView.findViewById(R.id.txtState);
            imgMore = itemView.findViewById(R.id.imageViewMore);
            aSwitch = itemView.findViewById(R.id.switchState);
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

        public TextView getTxtRequestNumber() {
            return txtRequestNumber;
        }

        public TextView getTxtState() {
            return txtState;
        }

        public ImageView getImgMore() {
            return imgMore;
        }

        public SwitchCompat getaSwitch() {
            return aSwitch;
        }

    }

    public void setData(List<User> listLoadMore, List<Integer> quantities){
        List<User> newListUser = new ArrayList<>(mViewModel.getUserList());
        List<Integer> newListQuantities = new ArrayList<>(mViewModel.getQuantityWaitingRequest());

        newListUser.addAll(listLoadMore);
        newListQuantities.addAll(quantities);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MainAdminDiffUtilCallBack(newListUser,
                mViewModel.getUserList()));
        diffResult.dispatchUpdatesTo(this);

        mViewModel.getUserList().clear();
        mViewModel.getQuantityWaitingRequest().clear();

        mViewModel.getUserList().addAll(newListUser);
        mViewModel.getQuantityWaitingRequest().addAll(newListQuantities);
    }
}
