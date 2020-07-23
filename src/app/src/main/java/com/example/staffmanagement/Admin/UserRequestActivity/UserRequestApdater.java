package com.example.staffmanagement.Admin.UserRequestActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Database.Entity.Request;
import com.example.staffmanagement.Presenter.RequestPresenter;
import com.example.staffmanagement.R;

import java.util.ArrayList;

public class UserRequestApdater extends RecyclerView.Adapter<UserRequestApdater.ViewHolder> {
    private Context mContext;
    private ArrayList<Request> requestArrayList;
    private RequestPresenter requestPresenter;
    private ArrayList<String> arrayListRequestState;
    private ArrayAdapter adapter;

    public UserRequestApdater(Context mContext, ArrayList<Request> requestArrayList, RequestPresenter requestPresenter) {
        this.mContext = mContext;
        this.requestArrayList = requestArrayList;
        this.requestPresenter = requestPresenter;
    }

    @NonNull
    @Override
    public UserRequestApdater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_user_request,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRequestApdater.ViewHolder holder, int position) {
        holder.txtName.setText(position+1+". "+requestPresenter.getFullNameById(requestArrayList.get(position).getIdUser()));
        holder.txtTitle.setText(requestPresenter.getTitleById(requestArrayList.get(position).getId()));
        holder.txtDateTime.setText(requestPresenter.getDateTimeById(requestArrayList.get(position).getId()));
        addRequestState();
        adapter=new ArrayAdapter(mContext, android.R.layout.simple_list_item_1,arrayListRequestState);
        holder.getSpnRequestState().setAdapter(adapter);
        int idState=requestPresenter.getIdStateById(requestArrayList.get(position).getId());
        holder.getSpnRequestState().setSelection(idState-1);
    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtTitle, txtDateTime;
        private Spinner spnRequestState;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.textViewStaffName);
            txtTitle=itemView.findViewById(R.id.textViewRequestName);
            txtDateTime=itemView.findViewById(R.id.textViewRequestDateTime);
            spnRequestState=itemView.findViewById(R.id.spinnerRequestState);
        }
        public Spinner getSpnRequestState() {
            return spnRequestState;
        }

        public void setSpnRequestState(Spinner spnRequestState) {
            this.spnRequestState = spnRequestState;
        }

        public TextView getTxtName() {
            return txtName;
        }

        public void setTxtName(TextView txtName) {
            this.txtName = txtName;
        }

        public TextView getTxtTitle() {
            return txtTitle;
        }

        public void setTxtTitle(TextView txtTitle) {
            this.txtTitle = txtTitle;
        }

        public TextView getTxtDateTime() {
            return txtDateTime;
        }

        public void setTxtDateTime(TextView txtDateTime) {
            this.txtDateTime = txtDateTime;
        }
    }

    private void addRequestState(){
        arrayListRequestState=new ArrayList<>();
        arrayListRequestState.add("Waiting");
        arrayListRequestState.add("Accept");
        arrayListRequestState.add("Decline");
    }
}
