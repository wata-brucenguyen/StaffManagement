package com.example.staffmanagement.View.Notification.Sender;

public class NotificationSenderStaffToAdmin {
    public DataStaffRequest data;
    public String to;

    public NotificationSenderStaffToAdmin(DataStaffRequest data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationSenderStaffToAdmin() {
    }
}
