package com.app.tourism.uis.activity_home.fragments.home_guide_module;

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
import com.app.tourism.adapters.GuideOrderAdapter;
import com.app.tourism.adapters.UserOrderAdapter;
import com.app.tourism.databinding.FragmentHomeBinding;
import com.app.tourism.databinding.FragmentHomeGuideBinding;
import com.app.tourism.models.OfferModel;
import com.app.tourism.tags.Common;
import com.app.tourism.tags.Tags;
import com.app.tourism.uis.activity_home.HomeActivity;
import com.app.tourism.uis.common_ui.activity_base.FragmentBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentHomeGuide extends FragmentBase {
    private HomeActivity activity;
    private FragmentHomeGuideBinding binding;
    private GuideOrderAdapter adapter;
    private DatabaseReference dRef;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_guide, container, false);
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

        adapter = new GuideOrderAdapter(activity, this);
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(this::getOffers);
        getOffers();
    }

    private void getOffers() {
        binding.recViewLayout.swipeRefresh.setRefreshing(true);
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.ORDERS_TABLE)
                .orderByChild("guide_id")
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

    public void acceptOrder(OfferModel offerModel, int adapterPosition) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        offerModel.setOrder_status(Tags.status_accepted);
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.ORDERS_TABLE)
                .child(offerModel.getOrder_id())
                .setValue(offerModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    public void refuseOrder(OfferModel offerModel, int adapterPosition) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        offerModel.setOrder_status(Tags.status_refused);
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.ORDERS_TABLE)
                .child(offerModel.getOrder_id())
                .setValue(offerModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public void endOrder(OfferModel offerModel, int adapterPosition) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        offerModel.setOrder_status(Tags.status_completed);
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.ORDERS_TABLE)
                .child(offerModel.getOrder_id())
                .setValue(offerModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        startActivity(intent);
    }
}