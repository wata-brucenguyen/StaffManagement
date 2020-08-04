package com.example.staffmanagement.View.Main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.staffmanagement.View.Staff.ViewModel.LoginViewModel;

public abstract class BaseFragment extends Fragment {

    protected LogInInterface mInterface;
    protected LoginViewModel mViewModel;
    protected int idResLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mInterface = (LogInInterface) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        initView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(idResLayout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        eventRegister();
        setDataOnView();
    }

    public abstract void initView();

    public abstract void mapping(View view);

    public abstract void eventRegister();

    public abstract void setDataOnView();

}
