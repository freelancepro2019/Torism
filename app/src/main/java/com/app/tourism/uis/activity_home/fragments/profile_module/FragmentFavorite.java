package com.app.tourism.uis.activity_home.fragments.profile_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.tourism.R;
import com.app.tourism.adapters.FavouriteAdapter;
import com.app.tourism.databinding.BottomSheetRateBinding;
import com.app.tourism.databinding.FragmentFavoriteBinding;
import com.app.tourism.models.FavoriteModel;
import com.app.tourism.models.OfferModel;
import com.app.tourism.models.RateModel;
import com.app.tourism.tags.Common;
import com.app.tourism.tags.Tags;
import com.app.tourism.uis.activity_home.HomeActivity;
import com.app.tourism.uis.common_ui.activity_base.FragmentBase;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FragmentFavorite extends FragmentBase {
    private HomeActivity activity;
    private FragmentFavoriteBinding binding;
    private FavouriteAdapter adapter;
    private DatabaseReference dRef;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.recViewLayout.tvNoData.setText(R.string.no_fav);
        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        adapter = new FavouriteAdapter(activity, this);
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(this::getFavorites);
        getFavorites();
    }

    private void getFavorites() {
        binding.recViewLayout.swipeRefresh.setRefreshing(true);
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.FAVORITE_TABLE)
                .orderByChild("user_id")
                .equalTo(getUserModel().getUser_id());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.recViewLayout.swipeRefresh.setRefreshing(false);
                List<FavoriteModel> list = new ArrayList<>();
                if (snapshot.getValue() != null) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        FavoriteModel favoriteModel = ds.getValue(FavoriteModel.class);
                        if (favoriteModel != null) {
                            list.add(favoriteModel);
                        }
                    }

                    if (list.size() > 0) {
                        Collections.reverse(list);
                        adapter.updateList(list);
                        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
                    } else {
                        binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

                    }
                } else {
                    adapter.updateList(new ArrayList<>());
                    binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void unFavorite(FavoriteModel model) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.FAVORITE_TABLE).child(model.getId())
                .removeValue()
                .addOnSuccessListener(unused -> {

                    dialog.dismiss();
                    Toast.makeText(activity, R.string.unfav, Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        startActivity(intent);
    }
}