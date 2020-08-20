package com.example.staffmanagement.MVVM.View.Main;

import android.view.View;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.R;

public class LoadingFragment extends BaseFragment {

    @Override
    public ViewModel getViewModel() {
        return null;
    }

    @Override
    public void initView() {
        this.idResLayout = R.layout.fragment_loading_login;
    }

    @Override
    public void mapping(View view) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void setDataOnView() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
