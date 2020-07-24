package com.example.staffmanagement.NonAdmin.RequestActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Admin.Const;
import com.example.staffmanagement.Database.Entity.Request;
import com.example.staffmanagement.NonAdmin.RequestCrudActivity.RequestCrudActivity;
import com.example.staffmanagement.Presenter.RequestPresenter;
import com.example.staffmanagement.R;

import java.util.ArrayList;

public class RequestListNonAdminAdapter extends RecyclerView.Adapter<RequestListNonAdminAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Request> items;
    private RequestPresenter requestPresenter;

    public RequestListNonAdminAdapter(Context mContext, ArrayList<Request> items, RequestPresenter requestPresenter) {
        this.mContext = mContext;
        this.items = items;
        this.requestPresenter = requestPresenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_user_request_non_admin,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.setTxtTitle(items.get(position).getTitle());
        String stateName = requestPresenter.getStateNameById(items.get(position).getIdState());
        holder.setTxtState(stateName);
        holder.setTxtDateTime(items.get(position).getDateTime());

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RequestCrudActivity.class);
                intent.setAction(RequestActivity.ACTION_EDIT_REQUEST);
                intent.putExtra(Const.REQUEST_DATA_INTENT,items.get(position));
                ((Activity) mContext).startActivityForResult(intent,RequestActivity.getRequestCodeEdit());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txtTitle = view.findViewById(R.id.textView_item_title_request_non_admin);
            txtState = view.findViewById(R.id.textView_item_state_request_non_admin);
            txtDateTime = view.findViewById(R.id.textView_item_dateTime_request_non_admin);
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
    }
}
