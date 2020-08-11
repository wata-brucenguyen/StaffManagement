package com.example.staffmanagement.View.Notification.CrudGroup;

public class CreateGroup {
    public String operation;
    public String notification_key_name;
    public String[] registration_ids;

    public CreateGroup(String operation, String notification_key_name, String[] registration_ids) {
        this.operation = operation;
        this.notification_key_name = notification_key_name;
        this.registration_ids = registration_ids;
    }

    public CreateGroup(){

    }
}
