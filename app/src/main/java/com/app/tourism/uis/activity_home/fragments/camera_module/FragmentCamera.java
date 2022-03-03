package com.app.tourism.uis.activity_home.fragments.camera_module;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.tourism.R;
import com.app.tourism.databinding.FragmentCameraBinding;
import com.app.tourism.databinding.FragmentHomeBinding;
import com.app.tourism.uis.activity_home.HomeActivity;
import com.app.tourism.uis.common_ui.activity_base.FragmentBase;

public class FragmentCamera extends FragmentBase {
    private HomeActivity activity;
    private FragmentCameraBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_camera,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

    }
}