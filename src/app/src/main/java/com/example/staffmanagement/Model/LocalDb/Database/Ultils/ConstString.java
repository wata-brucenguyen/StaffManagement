package com.example.staffmanagement.Model.LocalDb.Database.Ultils;

public class ConstString {

    // table Role
    public static final String ROLE_TABLE_NAME = "Role";
    public static final String ROLE_COL_ID = "Id";
    public static final String ROLE_COL_NAME = "Name";

    // table State request
    public static final String STATE_REQUEST_TABLE_NAME = "StateRequest";
    public static final String STATE_REQUEST_COL_ID = "Id";
    public static final String STATE_REQUEST_COL_NAME = "Name";

    // table User
    public static final String USER_TABLE_NAME = "User";
    public static final String USER_COL_ID = "Id";
    public static final String USER_COL_FULL_NAME = "FullName";
    public static final String USER_COL_USERNAME = "UserName";
    public static final String USER_COL_PASSWORD = "PassWord";
    public static final String USER_COL_ID_ROLE = "IdRole";
    public static final String USER_COL_PHONE_NUMBER = "PhoneNumber";
    public static final String USER_COL_EMAIL = "Email";
    public static final String USER_COL_ADDRESS = "Address";
    public static final String USER_COL_AVATAR = "Avatar";
    public static final String USER_COL_ID_USER_STATE = "IdUserState";

    public static final String DEFAULT_PASSWORD = "123456";
    // table Request
    public static final String REQUEST_TABLE_NAME = "Request";
    public static final String REQUEST_COL_ID = "Id";
    public static final String REQUEST_COL_ID_USER = "IdUser";
    public static final String REQUEST_COL_ID_STATE = "IdState";
    public static final String REQUEST_COL_TITLE = "Title";
    public static final String REQUEST_COL_CONTENT = "Content";
    public static final String REQUEST_COL_DATETIME = "DateTime";

    public static final String DATABASE_NAME = "StaffManagement";
    public static final int DATABASE_VERSION = 1;

    // table user state
    public static final String USER_STATE_TABLE_NAME = "UserState";
    public static final String USER_STATE_COL_ID = "Id";
    public static final String USER_STATE_COL_NAME = "Name";
}
