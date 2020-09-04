package com.example.staffmanagement.View.Notification.Sender;

public class NotificationSenderWithRequest {
    public DataStaffRequest data;
    public String to;

    public NotificationSenderWithRequest(DataStaffRequest data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationSenderWithRequest() {
    }
}
