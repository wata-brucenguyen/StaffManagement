package com.example.staffmanagement.View.Admin.UserRequestActivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Presenter.Admin.UserRequestPresenter;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Ultils.GeneralFunc;

import java.util.ArrayList;

public class UserRequestApdater extends RecyclerView.Adapter<UserRequestApdater.ViewHolder> {
    private Context mContext;
    private ArrayList<Request> requestArrayList;
    private UserRequestPresenter mPresenter;
    private ArrayList<String> arrayListRequestState;
    private ArrayList<StateRequest> stateRequestArrayList;
    private ArrayAdapter adapter;

    public UserRequestApdater(Context mContext, ArrayList<Request> requestArrayList, UserRequestPresenter mPresenter) {
        this.mContext = mContext;
        this.requestArrayList = requestArrayList;
        this.mPresenter = mPresenter;
    }


    @NonNull
    @Override
    public UserRequestApdater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_user_request,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserRequestApdater.ViewHolder holder, final int position) {
        holder.txtName.setText(mPresenter.getFullNameById(requestArrayList.get(position).getIdUser()));
        holder.txtTitle.setText(mPresenter.getTitleById(requestArrayList.get(position).getId()));

        holder.txtDateTime.setText(GeneralFunc.convertMilliSecToDateString(mPresenter.getDateTimeById(requestArrayList.get(position).getId())));
        addRequestState();
        adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1,arrayListRequestState);
        holder.getSpnRequestState().setAdapter(adapter);
        final int idState=mPresenter.getIdStateById(requestArrayList.get(position).getId());

        holder.getSpnRequestState().setSelection(getIdStateById(idState));
        holder.getSpnRequestState().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameState = holder.spnRequestState.getSelectedItem().toString();
                requestArrayList.get(position).setIdState(getIdStateByName(nameState));
                mPresenter.update(requestArrayList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int getIdStateById(int idRequest){
        for(int i=0;i<stateRequestArrayList.size();i++){
            if(stateRequestArrayList.get(i).getId()==(idRequest))
                return i;
        }
        return -1;
    }
    private int getIdStateByName(String name){
        for(int i=0;i<stateRequestArrayList.size();i++){
            if(stateRequestArrayList.get(i).getName().equals(name))
                return stateRequestArrayList.get(i).getId();
        }
        return -1;
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

    }

    private void addRequestState(){
        // StateRequestDbHandler db = new StateRequestDbHandler(mContext);
        arrayListRequestState = new ArrayList<>();
        stateRequestArrayList = new ArrayList<>();
        stateRequestArrayList.addAll(mPresenter.getAllStateRequest());
        for(int i = 0; i < stateRequestArrayList.size(); i++){
            arrayListRequestState.add(stateRequestArrayList.get(i).getName());
        }
    }


}
