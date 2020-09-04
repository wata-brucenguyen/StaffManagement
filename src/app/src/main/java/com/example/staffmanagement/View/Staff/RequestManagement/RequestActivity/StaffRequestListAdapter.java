package com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestCrudActivity.StaffRequestCrudActivity;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.ViewModel.Staff.RequestViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class StaffRequestListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private final int ITEM_VIEW_TYPE = 1;
    private final int LOADING_VIEW_TYPE = 2;
    private RequestViewModel mViewModel;

    public StaffRequestListAdapter(Context context, RequestViewModel mViewModel) {
        WeakReference<Context> weakContext = new WeakReference<>(context);
        this.mContext = weakContext.get();
        this.mViewModel = mViewModel;
    }

    @Override
    public int getItemViewType(int position) {
        return mViewModel.getListRequest().get(position) == null ? LOADING_VIEW_TYPE : ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        if (viewType == ITEM_VIEW_TYPE) {
             v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.item_user_request_for_staff, parent, false);
            //ItemUserRequestForStaffBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_user_request_for_staff,parent,false);
            return new ViewHolder(v);
        } else {
            v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.view_load_more, parent, false);
            return new LoadingViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof LoadingViewHolder) {
            return;
        }

        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setTxtTitle(mViewModel.getListRequest().get(position).getTitle());
        viewHolder.getTxtContent().setText(mViewModel.getListRequest().get(position).getContent());
        viewHolder.setTxtState(mViewModel.getListRequest().get(position).getStateRequest().getName());

        switch (mViewModel.getListRequest().get(position).getStateRequest().getId()) {
            case 1:
                viewHolder.getTxtState().setTextColor(mContext.getResources().getColor(R.color.colorWaiting));
                viewHolder.getLila().setBackgroundColor(mContext.getResources().getColor(R.color.colorWaiting));
                break;
            case 2:
                viewHolder.getTxtState().setTextColor(mContext.getResources().getColor(R.color.colorAccept));
                viewHolder.getLila().setBackgroundColor(mContext.getResources().getColor(R.color.colorAccept));
                break;
            case 3:
                viewHolder.getTxtState().setTextColor(mContext.getResources().getColor(R.color.colorDecline));
                viewHolder.getLila().setBackgroundColor(mContext.getResources().getColor(R.color.colorDecline));
                break;
        }

        viewHolder.setTxtDateTime(GeneralFunc.convertMilliSecToDateString(mViewModel.getListRequest().get(position).getDateTime()));

        viewHolder.getView().setOnClickListener(view -> {
            Intent intent = new Intent(mContext, StaffRequestCrudActivity.class);
            intent.setAction(StaffRequestActivity.ACTION_EDIT_REQUEST);
            intent.putExtra(Constant.REQUEST_DATA_INTENT, mViewModel.getListRequest().get(position));
            ((Activity) mContext).startActivityForResult(intent, StaffRequestActivity.getRequestCodeEdit());
        });
    }

    @Override
    public int getItemCount() {
        return mViewModel.getListRequest().size();
    }

    public void setData(List<Request> listLoadMore) {
        final List<Request> newList = new ArrayList<>();
        newList.addAll(mViewModel.getListRequest());
        newList.addAll(listLoadMore);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new StaffRequestDiffUtilCallBack(newList, mViewModel.getListRequest()));
        diffResult.dispatchUpdatesTo(this);
        mViewModel.getListRequest().clear();
        mViewModel.getListRequest().addAll(newList);
    }

    public void swapItem(int oldPosition, int newPosition){
        Request r = mViewModel.getListRequest().get(oldPosition);
        mViewModel.getListRequest().remove(oldPosition);
        mViewModel.getListRequest().add(newPosition,r);
        notifyItemMoved(oldPosition,newPosition);
    }

    public void deleteItem(int position) {
        mViewModel.delete(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Request request, int position) {
        mViewModel.getListRequest().add(position, request);
        notifyItemInserted(position);
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //private ItemUserRequestForStaffBinding binding;
        private View view;
        private TextView txtTitle, txtDateTime, txtState, txtContent;
        private LinearLayout lila, viewBackground;
        private ConstraintLayout viewForeground;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           // this.binding = binding;
            view = itemView;
            txtTitle = view.findViewById(R.id.textView_item_title_request_non_admin);
            txtState = view.findViewById(R.id.textView_item_state_request_non_admin);
            txtContent = view.findViewById(R.id.textView_item_content_request_non_admin);
            txtDateTime = view.findViewById(R.id.textView_item_dateTime_request_non_admin);
            lila = view.findViewById(R.id.linearLayout_color_vertical);
            viewBackground = view.findViewById(R.id.linearLayout_item_background);
            viewForeground = view.findViewById(R.id.constraintLayout_item_foreground);
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public void setTxtTitle(String title) {
            this.txtTitle.setText(title);
        }

        public TextView getTxtContent() {
            return txtContent;
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

        public LinearLayout getViewBackground() {
            return viewBackground;
        }

        public ConstraintLayout getViewForeground() {
            return viewForeground;
        }

    }
}
