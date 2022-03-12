package com.app.tourism.uis.activity_home.fragments.home_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.tourism.R;
import com.app.tourism.adapters.GuideAdapter;
import com.app.tourism.databinding.FragmentHomeBinding;
import com.app.tourism.models.UserModel;
import com.app.tourism.tags.Tags;
import com.app.tourism.uis.activity_send_order.SendOrderActivity;
import com.app.tourism.uis.common_ui.activity_base.FragmentBase;
import com.app.tourism.uis.activity_home.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentHome extends FragmentBase {
    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private GuideAdapter adapter;
    private DatabaseReference dRef;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        dRef = FirebaseDatabase.getInstance().getReference();
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new GuideAdapter(activity,getLang(),this);
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(this::getGuides);
        binding.recViewLayout.tvNoData.setText(R.string.no_guide);
        getGuides();
    }

    private void getGuides() {
        binding.recViewLayout.swipeRefresh.setRefreshing(true);
        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        Query query = dRef.child(Tags.USERS_TABLE);
        query.orderByChild("user_type")
                .equalTo(Tags.user_guide)
                .limitToFirst(20)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        binding.recViewLayout.swipeRefresh.setRefreshing(false);
                        List<UserModel> guidesList = new ArrayList<>();
                        if (snapshot.getValue()!=null){
                            for (DataSnapshot ds :snapshot.getChildren()){
                                UserModel userModel = ds.getValue(UserModel.class);
                                if (userModel!=null){
                                    guidesList.add(userModel);
                                }
                            }

                            if (guidesList.size()>0){
                                adapter.updateList(sortedList(guidesList));
                                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
                            }else {
                                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private List<UserModel> sortedList(List<UserModel> list) {
        Collections.sort(list, (m1, m2) -> Double.compare(m2.getRate(),m1.getRate()));
        return list;
    }

    public void setItemData(UserModel guideModel) {
        Intent intent = new Intent(activity, SendOrderActivity.class);
        intent.putExtra("data",guideModel);
        startActivity(intent);
    }
}