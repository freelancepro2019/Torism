package com.app.tourism.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tourism.R;
import com.app.tourism.databinding.FragmentHomeBinding;
import com.app.tourism.databinding.FragmentProfileBinding;
import com.app.tourism.uis.activity_base.FragmentBase;
import com.app.tourism.uis.activity_home.HomeActivity;

public class FragmentProfile extends FragmentBase {
    private FragmentProfileBinding binding;
    private HomeActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.setLang(getLang());
        binding.setModel(getUserModel());
        binding.cardLogout.setOnClickListener(view -> {
            activity.logout();
        });


    }
}