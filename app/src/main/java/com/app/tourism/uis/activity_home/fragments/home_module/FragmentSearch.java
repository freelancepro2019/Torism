package com.app.tourism.uis.activity_home.fragments.home_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.tourism.R;
import com.app.tourism.adapters.GuideAdapter;
import com.app.tourism.databinding.FragmentSearchBinding;
import com.app.tourism.models.UserModel;
import com.app.tourism.tags.Tags;
import com.app.tourism.uis.activity_home.HomeActivity;
import com.app.tourism.uis.activity_send_order.SendOrderActivity;
import com.app.tourism.uis.common_ui.activity_base.FragmentBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentSearch extends FragmentBase {
    private HomeActivity activity;
    private FragmentSearchBinding binding;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.recViewLayout.tvNoData.setText(R.string.no_search_result);
        dRef = FirebaseDatabase.getInstance().getReference();
        binding.setSearchResultCount(0);
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new GuideAdapter(activity, getLang(),this);
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(() -> filter(binding.edtSearch.getText().toString()));
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString();
                filter(name);
            }
        });
    }


    private void filter(String name) {
        if (name.isEmpty()) {
            List<UserModel> guideList = new ArrayList<>();
            binding.recViewLayout.swipeRefresh.setRefreshing(false);
            adapter.updateList(guideList);
            binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
            binding.setSearchResultCount(0);
            return;
        }
        binding.recViewLayout.swipeRefresh.setRefreshing(true);
        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        Query query = dRef.child(Tags.USERS_TABLE);
        query.orderByChild("full_name")
                .startAt(name)
                .endAt(name + "\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        binding.recViewLayout.swipeRefresh.setRefreshing(false);
                        List<UserModel> guideList = new ArrayList<>();
                        if (snapshot.getValue() != null) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                UserModel userModel = ds.getValue(UserModel.class);
                                if (userModel != null && userModel.getUser_type().equals(Tags.user_guide)) {
                                    guideList.add(userModel);
                                }
                            }

                            binding.setSearchResultCount(guideList.size());

                            if (guideList.size() > 0) {
                                adapter.updateList(guideList);
                                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
                            } else {
                                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

                            }
                        } else {
                            binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setItemData(UserModel guideModel) {
        Intent intent = new Intent(activity, SendOrderActivity.class);
        intent.putExtra("data",guideModel);
        startActivity(intent);
    }


}