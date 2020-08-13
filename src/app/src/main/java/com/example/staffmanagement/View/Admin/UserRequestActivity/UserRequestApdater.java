package com.example.staffmanagement.View.Admin.UserRequestActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Model.LocalDb.Database.Entity.Request;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.DetailRequestUser.DetailRequestUserActivity;
import com.example.staffmanagement.View.Admin.ViewModel.UserRequestViewModel;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class UserRequestApdater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private UserRequestViewModel mViewModel;
    //    private List<Request> requestList;
//    private List<String> arrayListRequestState = new ArrayList<>();
//    private List<StateRequest> stateRequestArrayList = new ArrayList<>();
    private final int ITEM_VIEW_TYPE = 1;
    private UserRequestInterface mInterface;

    public UserRequestApdater(Context context, UserRequestViewModel mViewModel, UserRequestInterface userRequestInterface) {
        WeakReference<Context> weak = new WeakReference<>(context);
        this.mContext = weak.get();
        this.mViewModel = mViewModel;
        this.mInterface = userRequestInterface;
    }


    @Override
    public int getItemViewType(int position) {
        int LOADING_VIEW_TYPE = 2;
        return mViewModel.getRequestList().get(position) == null ? LOADING_VIEW_TYPE : ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_VIEW_TYPE) {
            view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.item_user_request, parent, false);
            return new ViewHolder(view);
        } else {
            view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.view_load_more, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof LoadingViewHolder) {
            return;
        }

        final ViewHolder viewHolder = (ViewHolder) holder;

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, mViewModel.getStateRequestNameList()) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                else return true;
            }

            @Override
            public int getItemViewType(int position) {

                return super.getItemViewType(position);
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.parseColor("#bcbcbb"));
                }
                return view;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (textView.getText().equals("Waiting")) {
                    textView.setTextColor(mContext.getResources().getColor(R.color.colorWaiting));
                } else {
                    if (textView.getText().equals("Accept")) {
                        textView.setTextColor(mContext.getResources().getColor(R.color.colorAccept));
                    } else {
                        textView.setTextColor(mContext.getResources().getColor(R.color.colorDecline));
                    }
                }
                return view;
            }
        };

        viewHolder.getSpnRequestState().setAdapter(adapter);
        final int idState = mViewModel.getRequestList().get(position).getIdState();
        viewHolder.getSpnRequestState().setSelection(getPositionById(idState));

       // mInterface.getFullNameById(mViewModel.getRequestList().get(position).getIdUser(), (ViewHolder) holder);
        viewHolder.setTxtName(mViewModel.getNameUserList().get(position));
        viewHolder.setTxtTitle(mViewModel.getRequestList().get(position).getTitle());
        viewHolder.setTxtDateTime(GeneralFunc.convertMilliSecToDateString(mViewModel.getRequestList().get(position).getDateTime()));
        viewHolder.getSpnRequestState().setEnabled(false);

        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailRequestUserActivity.class);
                intent.putExtra(Constant.REQUEST_DATA_INTENT, mViewModel.getRequestList().get(position));
                intent.putExtra(Constant.STATE_NAME_INTENT, String.valueOf(viewHolder.getSpnRequestState().getSelectedItem()));
                intent.putExtra(Constant.FULL_NAME, "Detail");
                ((Activity) mContext).startActivityForResult(intent, 123);
            }
        });
    }
    public void setData(List<Request> listLoadMore, List<String> listName){
        List<Request> newList = new ArrayList<>();
        List<String> newListName=new ArrayList<>();
        newListName.addAll(mViewModel.getNameUserList());
        newListName.addAll(listName);
        newList.addAll(mViewModel.getRequestList());
        newList.addAll(listLoadMore);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new UserRequestDiffUtilCallback(newList,mViewModel.getRequestList()));
        diffResult.dispatchUpdatesTo(this);
        mViewModel.getNameUserList().clear();
        mViewModel.getNameUserList().addAll(newListName);
        mViewModel.getRequestList().clear();
        mViewModel.getRequestList().addAll(newList);
    }
    private int getPositionById(int idState) {
        for (int i = 0; i < mViewModel.getStateRequestList().size(); i++) {
            if (mViewModel.getStateRequestList().get(i).getId() == idState)
                return i;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mViewModel.getRequestList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtTitle, txtDateTime;
        private Spinner spnRequestState;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txtName = itemView.findViewById(R.id.textViewStaffName);
            txtTitle = itemView.findViewById(R.id.textViewRequestName);
            txtDateTime = itemView.findViewById(R.id.textViewRequestDateTime);
            spnRequestState = itemView.findViewById(R.id.spinnerRequestState);
        }

        public TextView getTxtName() {
            return txtName;
        }

        public void setTxtName(String txtName) {
            this.txtName.setText(txtName);
        }

        public TextView getTxtTitle() {
            return txtTitle;
        }

        public void setTxtTitle(String txtTitle) {
            this.txtTitle.setText(txtTitle);
        }

        public TextView getTxtDateTime() {
            return txtDateTime;
        }

        public void setTxtDateTime(String txtDateTime) {
            this.txtDateTime.setText(txtDateTime);
        }

        public void setSpnRequestState(Spinner spnRequestState) {
            this.spnRequestState = spnRequestState;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public Spinner getSpnRequestState() {
            return spnRequestState;
        }

    }

}
