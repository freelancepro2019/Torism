package com.app.tourism.uis.activity_home.fragments.profile_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.tourism.R;
import com.app.tourism.adapters.UserOrderAdapter;
import com.app.tourism.databinding.BottomSheetRateBinding;
import com.app.tourism.databinding.FragmentHomeBinding;
import com.app.tourism.databinding.FragmentOffersBinding;
import com.app.tourism.models.FavoriteModel;
import com.app.tourism.models.OfferModel;
import com.app.tourism.models.RateModel;
import com.app.tourism.tags.Common;
import com.app.tourism.tags.Tags;
import com.app.tourism.uis.activity_home.HomeActivity;
import com.app.tourism.uis.common_ui.activity_base.FragmentBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class FragmentOffers extends FragmentBase {
    private HomeActivity activity;
    private FragmentOffersBinding binding;
    private UserOrderAdapter adapter;
    private DatabaseReference dRef;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.recViewLayout.tvNoData.setText(R.string.no_orders);
        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        adapter = new UserOrderAdapter(activity, this);
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(this::getOffers);
        getOffers();
    }

    private void getOffers() {
        binding.recViewLayout.swipeRefresh.setRefreshing(true);
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.ORDERS_TABLE)
                .orderByChild("user_id")
                .equalTo(getUserModel().getUser_id());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.recViewLayout.swipeRefresh.setRefreshing(false);
                List<OfferModel> list = new ArrayList<>();
                if (snapshot.getValue() != null) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        OfferModel offerModel = ds.getValue(OfferModel.class);
                        if (offerModel != null) {
                            list.add(offerModel);
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
                    binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void createRateSheetDialog(OfferModel model, int adapterPosition) {
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        BottomSheetRateBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.bottom_sheet_rate, null, false);
        dialog.setContentView(binding.getRoot());
        binding.rateBar.setOnRatingBarChangeListener((simpleRatingBar, rating, fromUser) -> {
            int rate = (int) rating;
            binding.tvRate.setText(rate + "");
        });
        binding.imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

        binding.btnRate.setOnClickListener(view -> {
            int rate = (int) binding.rateBar.getRating();
            addRate(model, rate, adapterPosition);
            dialog.dismiss();

        });

        binding.imageFavorite.setOnClickListener(view -> {
            checkIsGuideInMyFavorite(model);
        });
        dialog.show();

    }

    private void checkIsGuideInMyFavorite(OfferModel model) {
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.FAVORITE_TABLE)
                .orderByChild("user_id")
                .equalTo(getUserModel().getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isInMyFavorite = false;
                if (snapshot.getValue() == null) {
                    addToFavorite(model);
                } else {

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        FavoriteModel favoriteModel = ds.getValue(FavoriteModel.class);
                        if (favoriteModel != null) {
                            if (favoriteModel.getGuide_id().equals(model.getGuide_id())) {
                                isInMyFavorite = true;
                                break;
                            }
                        }
                    }
                    if (isInMyFavorite) {
                        Toast.makeText(activity, R.string.already_fav, Toast.LENGTH_SHORT).show();

                    } else {
                        addToFavorite(model);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addToFavorite(OfferModel model) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dRef = FirebaseDatabase.getInstance().getReference();
        String id = dRef.child(Tags.FAVORITE_TABLE).push().getKey();
        FavoriteModel favoriteModel = new FavoriteModel(id, getUserModel().getUser_id(), model.getGuide_id(), model.getGuide_name(), model.getGuide_phone());
        dRef.child(Tags.FAVORITE_TABLE).child(id)
                .setValue(favoriteModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    Toast.makeText(activity, R.string.fav, Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    private void addRate(OfferModel model, int rate, int adapterPosition) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dRef = FirebaseDatabase.getInstance().getReference();
        String rate_id = dRef.child(Tags.RATES_TABLE).push().getKey();
        RateModel rateModel = new RateModel(rate_id, model.getGuide_id(), model.getOrder_id(), rate);
        dRef.child(Tags.RATES_TABLE).child(rate_id)
                .setValue(rateModel)
                .addOnSuccessListener(unused -> {
                    model.setOrder_status(Tags.status_rated);
                    adapter.updateItem(model, adapterPosition);
                    updateGuideRate(model, dialog);
                }).addOnFailureListener(e -> {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

    }

    private void updateGuideRate(OfferModel model, ProgressDialog dialog) {
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.RATES_TABLE)
                .orderByChild("guide_id")
                .equalTo(model.getGuide_id());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    int count = 0;
                    long total = snapshot.getChildrenCount();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        RateModel rateModel = ds.getValue(RateModel.class);
                        count += rateModel.getRate();
                    }

                    int rate = (int) (count / total);
                    updateUserRate(rate, model, dialog);

                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUserRate(int rate, OfferModel model, ProgressDialog dialog) {
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.USERS_TABLE)
                .child(model.getGuide_id())
                .child("rate")
                .setValue(rate)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        });


    }

    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }
}