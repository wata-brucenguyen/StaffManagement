package com.example.staffmanagement.View.Admin.SendNotificationActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.staffmanagement.R;
import com.example.staffmanagement.Model.FirebaseDb.Notification.Sender.Data;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
import com.example.staffmanagement.ViewModel.Admin.UserListViewModel;

public class SendNotificationDialog extends DialogFragment {
    private SendNotificationInterface mInterface;
    private EditText editText_Title, editText_Content;
    private Button btnSendNotification, btnCancel;
    private UserListViewModel mViewModel;

    public SendNotificationDialog(SendNotificationInterface Interface, UserListViewModel ViewModel) {
        this.mInterface = Interface;
        this.mViewModel = ViewModel;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_admin_send_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        eventRegister();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void mapping(View view) {
        editText_Title = view.findViewById(R.id.editText_Title);
        editText_Content = view.findViewById(R.id.editText_Content);
        btnSendNotification = view.findViewById(R.id.buttonSendNotification);
        btnCancel = view.findViewById(R.id.buttonCancel);
    }

    private void eventRegister() {
        btnSendNotification.setOnClickListener(view -> {
            //createGroup();
            if (CheckNetwork.checkInternetConnection(getActivity())) {
                if(TextUtils.isEmpty(editText_Title.getText().toString()) && TextUtils.isEmpty(editText_Content.getText().toString())){
                    mInterface.showMessage("Please fill in the title or the content");
                    return;
                }
                sendMessageToOneUser();
                mInterface.showMessage("Sent");
                getDialog().dismiss();
                mInterface.onCancelDialog();
            }
        });
        btnCancel.setOnClickListener(view -> {
            getDialog().dismiss();
            mInterface.onCancelDialog();
        });
    }

    public void sendMessageToOneUser() {
        Data data = new Data(editText_Title.getText().toString().trim(),editText_Content.getText().toString().trim());
        mViewModel.sendNotification(data);
    }

}
