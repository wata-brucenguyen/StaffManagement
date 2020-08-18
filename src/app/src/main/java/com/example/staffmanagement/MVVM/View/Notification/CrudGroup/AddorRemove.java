package com.example.staffmanagement.MVVM.View.Notification.CrudGroup;

public class AddorRemove {
    public String operation;
    public String notification_key_name;
    public String notification_key;
    public String[] registration_ids;

    public AddorRemove(String operation, String notification_key_name, String[] registration_ids, String notification_key) {
        this.operation = operation;
        this.notification_key_name = notification_key_name;
        this.registration_ids = registration_ids;
        this.notification_key = notification_key;
    }

    public AddorRemove() {
    }
}
