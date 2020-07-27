package com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Model.Database.Entity.Request;

import com.example.staffmanagement.Presenter.Staff.StaffRequestPresenter;
import com.example.staffmanagement.View.Ultils.Const;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestCrudActivity.StaffRequestCrudActivity;

import com.example.staffmanagement.R;

import java.util.ArrayList;

public class StaffRequestListAdapter extends RecyclerView.Adapter<StaffRequestListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Request> items;
    private StaffRequestPresenter mPresenter;

    public StaffRequestListAdapter(Context mContext, ArrayList<Request> items, StaffRequestPresenter mPresenter) {
        this.mContext = mContext;
        this.items = items;
        this.mPresenter = mPresenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_user_request_for_staff,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.setTxtTitle(items.get(position).getTitle());
        String stateName = mPresenter.getStateNameById(items.get(position).getIdState());
        holder.setTxtState(stateName);
        switch (items.get(position).getIdState()){
            case 1:
                holder.getTxtState().setTextColor(mContext.getResources().getColor(R.color.colorWaiting));
                holder.getLila().setBackgroundColor(mContext.getResources().getColor(R.color.colorWaiting));
                break;
            case 2:
                holder.getTxtState().setTextColor(mContext.getResources().getColor(R.color.colorAccept));
                holder.getLila().setBackgroundColor(mContext.getResources().getColor(R.color.colorAccept));
                break;
            case 3:
                holder.getTxtState().setTextColor(mContext.getResources().getColor(R.color.colorDecline));
                holder.getLila().setBackgroundColor(mContext.getResources().getColor(R.color.colorDecline));
                break;
        }
        holder.setTxtDateTime(items.get(position).getDateTime());

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StaffRequestCrudActivity.class);
                intent.setAction(StaffRequestActivity.ACTION_EDIT_REQUEST);
                intent.putExtra(Const.REQUEST_DATA_INTENT,items.get(position));
                ((Activity) mContext).startActivityForResult(intent, StaffRequestActivity.getRequestCodeEdit());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView txtTitle,txtDateTime,txtState;
        private LinearLayout lila;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txtTitle = view.findViewById(R.id.textView_item_title_request_non_admin);
            txtState = view.findViewById(R.id.textView_item_state_request_non_admin);
            txtDateTime = view.findViewById(R.id.textView_item_dateTime_request_non_admin);
            lila = view.findViewById(R.id.linearLayout_color_vertical);
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public TextView getTxtTitle() {
            return txtTitle;
        }

        public void setTxtTitle(String title) {
            this.txtTitle.setText(title);
        }

        public TextView getTxtDateTime() {
            return txtDateTime;
        }

        public void setTxtDateTime(String dateTime) {
            this.txtDateTime.setText(dateTime);
        }

        public TextView getTxtState() {
            return txtState;
        }

        public void setTxtState(String state) {
            this.txtState.setText(state);
        }

        public LinearLayout getLila() {
            return lila;
        }

        public void setLila(LinearLayout lila) {
            this.lila = lila;
        }
    }
}
