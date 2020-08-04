package com.example.staffmanagement.View.Admin.MainAdminActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Model.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Presenter.Admin.UserListPresenter;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AdminInformationActivity;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestActivity;
import com.example.staffmanagement.View.Ultils.Constant;

import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<User> userList;
    private UserListPresenter mPresenter;
    private MainAdminInterface mInterface;
    private final int ITEM_VIEW_TYPE = 1;
    private final int LOADING_VIEW_TYPE = 2;


    public UserAdapter(Context mContext, List<User> userList, UserListPresenter mPresenter, MainAdminInterface mInterface) {
        this.mContext = mContext;
        this.userList = userList;
        this.mPresenter = mPresenter;
        this.mInterface = mInterface;
    }


    public void deleteUser(User item) {
        for (int i = 0; i < userList.size(); i++) {
            if (item.getId() == userList.get(i).getId()) {
                UserDbHandler db = new UserDbHandler(mContext);
                db.update(item);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return userList.get(position) == null ? LOADING_VIEW_TYPE : ITEM_VIEW_TYPE;
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

        viewHolder.getTxtName().setText(userList.get(position).getFullName());


        if (userList.get(position).getIdRole() == 1) {
            viewHolder.getTxtRole().setTextColor(Color.RED);

        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String role = mPresenter.getRoleNameById(userList.get(position).getIdRole());
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.getTxtRole().setText(role);
                    }
                });

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final int soluong = mPresenter.getCountWaitingForRequest(userList.get(position).getId());
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (soluong > 0) {
                            viewHolder.getTxtRequestNumber().setText(String.valueOf(soluong));
                        } else
                            viewHolder.getTxtRequestNumber().setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();

        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserRequestActivity.class);
                intent.putExtra(Constant.USER_INFO_INTENT, userList.get(position));
                mContext.startActivity(intent);
            }
        });
        viewHolder.getImgMore().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(viewHolder, userList.get(position));

            }
        });

        setSwitch(viewHolder,position);
        viewHolder.getaSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    viewHolder.getTxtState().setText("Lock");
                    mInterface.onChangeUserState(userList.get(position).getId(),2);
                }
                else {
                    viewHolder.getTxtState().setText("Active");
                    mInterface.onChangeUserState(userList.get(position).getId(),1);
                }
            }
        });
    }

    private void setSwitch(ViewHolder viewHolder, int position){
        if(userList.get(position).getIdUserState() == 1){
            viewHolder.getTxtState().setText("Active");
            viewHolder.getaSwitch().setChecked(false);
        }
        else if(userList.get(position).getIdUserState() == 2){
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
                        intent.putExtra("name", user.getFullName());
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
        return userList.size();
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtRole, txtRequestNumber, txtState;
        private ImageView imgMore;
        private View view;
        private Switch aSwitch;

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

        public TextView getTxtState() {
            return txtState;
        }

        public void setTxtState(TextView txtState) {
            this.txtState = txtState;
        }

        public ImageView getImgMore() {
            return imgMore;
        }

        public void setImgMore(ImageView imgMore) {
            this.imgMore = imgMore;
        }

        public Switch getaSwitch() {
            return aSwitch;
        }

        public void setaSwitch(Switch aSwitch) {
            this.aSwitch = aSwitch;
        }
    }
}
