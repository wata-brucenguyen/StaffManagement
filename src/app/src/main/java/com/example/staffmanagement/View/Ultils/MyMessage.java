package com.example.staffmanagement.MVVM.View.Ultils;

import android.os.Message;

public class MyMessage {
    public static Message getMessage(int what) {
        Message m = new Message();
        m.what = what;
        return m;
    }

    public static Message getMessage(int what,Object obj) {
        Message m = new Message();
        m.what = what;
        m.obj = obj;
        return m;
    }
}
