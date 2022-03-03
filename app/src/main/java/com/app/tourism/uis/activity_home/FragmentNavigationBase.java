package com.app.tourism.uis.activity_home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.tourism.R;
import com.app.tourism.uis.common_ui.activity_base.FragmentBase;

import java.util.HashSet;
import java.util.Set;

public class FragmentNavigationBase extends FragmentBase {
    private int layoutRecourseId = -1;
    private int navHostId = -1;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private Set<Integer> rootDestination = new HashSet<>();
    private View root;
    private HomeActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentNavigationBase newInstance(int layoutRecourseId, int navHostId) {
        Bundle bundle = new Bundle();
        bundle.putInt("layoutRecourseId", layoutRecourseId);
        bundle.putInt("navHostId", navHostId);
        FragmentNavigationBase fragment = new FragmentNavigationBase();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            layoutRecourseId = bundle.getInt("layoutRecourseId");
            navHostId = bundle.getInt("navHostId");

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (layoutRecourseId != -1 && navHostId != -1) {
            root = inflater.inflate(layoutRecourseId, container, false);
            return root;
        } else {
            return null;
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        if (navHostId != -1 && layoutRecourseId != -1) {

            navController = Navigation.findNavController(activity, navHostId);
            Toolbar toolbar = root.findViewById(R.id.toolbar);
            TextView tvTitle = root.findViewById(R.id.tvTitle);
            NavigationUI.setupWithNavController(toolbar, navController);
            navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
                int id = navDestination.getId();
                if (id==R.id.fragmentProfile){
                    tvTitle.setText(R.string.profile);
                }else if (id==R.id.fragmentOffers){
                    tvTitle.setText(R.string.offers);
                }
            });
        }

    }

    public boolean onBackPressed() {
        return navController.navigateUp();
    }
}

