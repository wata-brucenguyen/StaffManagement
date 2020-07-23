package com.example.staffmanagement.Admin.MainAdminActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Database.Data.SeedData;
import com.example.staffmanagement.Database.Entity.User;
import com.example.staffmanagement.Presenter.RequestPresenter;
import com.example.staffmanagement.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<User> userArrayList;
    private RequestPresenter requestPresenter;

    public UserAdapter(Context mContext, ArrayList<User> userArrayList, RequestPresenter requestPresenter) {
        this.mContext = mContext;
        this.userArrayList = userArrayList;
        this.requestPresenter = requestPresenter;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView =layoutInflater.inflate(R.layout.item_user,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        holder.txtName.setText(position+1+". "+ userArrayList.get(position).getFullName());
        holder.txtRole.setText(requestPresenter.getRoleNameById(userArrayList.get(position).getIdRole()));
        int soluong = requestPresenter.getCountWaitingForRequest(userArrayList.get(position).getId());
        Log.i("aaaa",userArrayList.get(position).getId()+"  "+ soluong);

        if(soluong > 0){
            holder.txtRequestNumber.setText(String.valueOf(soluong));
        } else
            holder.txtRequestNumber.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName,txtRole, txtRequestNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.textViewName);
            txtRole=itemView.findViewById(R.id.textViewRole);
            txtRequestNumber=itemView.findViewById(R.id.textViewRequestNumber);
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
    }
}
