package com.app.tourism.uis.activity_home.fragments.profile_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.app.tourism.R;
import com.app.tourism.databinding.FragmentProfileBinding;
import com.app.tourism.models.UserModel;
import com.app.tourism.tags.Common;
import com.app.tourism.tags.Tags;
import com.app.tourism.uis.common_ui.activity_base.FragmentBase;
import com.app.tourism.uis.activity_home.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentProfile extends FragmentBase {
    private FragmentProfileBinding binding;
    private HomeActivity activity;
    private DatabaseReference dRef;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        dRef = FirebaseDatabase.getInstance().getReference();
        ;
        binding.setLang(getLang());
        binding.setModel(getUserModel());
        binding.userLayout.setLang(getLang());
        binding.guideLayout.setLang(getLang());
        binding.guideLayout.setModel(getUserModel());
        binding.userLayout.setCounter("0");
        binding.guideLayout.setCounter("0");
        binding.setRate("0");
        binding.userLayout.llOffer.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.fragmentOffers);
        });
        binding.cardLogout.setOnClickListener(view -> {
            activity.logout();
        });

        if (getUserModel().getUser_type().equals(Tags.user_normal)) {
            getOfferCount();
        } else {
            getNewOrderCount();
            getRate();
        }

        binding.guideLayout.switchBtn.setOnClickListener(view -> {
            boolean checked = binding.guideLayout.switchBtn.isChecked();
            updateGuideStatus(checked);
        });
    }



    private void getOfferCount() {
        dRef.child(Tags.USERS_TABLE)
                .child(getUserModel().getUser_id())
                .child("offer_count")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            binding.userLayout.setCounter(snapshot.getValue().toString());

                        } else {
                            binding.userLayout.setCounter("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getNewOrderCount() {
        dRef.child(Tags.USERS_TABLE)
                .child(getUserModel().getUser_id())
                .child("new_order_count")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            binding.guideLayout.setCounter(snapshot.getValue().toString());

                        } else {
                            binding.guideLayout.setCounter("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getRate() {
        dRef.child(Tags.USERS_TABLE)
                .child(getUserModel().getUser_id())
                .child("rate")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            binding.setRate(snapshot.getValue().toString());

                        } else {
                            binding.setRate("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void updateGuideStatus(boolean checked) {
        ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        dRef.child(Tags.USERS_TABLE)
                .child(getUserModel().getUser_id())
                .child("canReceiveOrders")
                .setValue(checked)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    UserModel model = getUserModel();
                    model.setCanReceiveOrders(checked);
                    setUserModel(model);
                    binding.guideLayout.setModel(model);

                }).addOnFailureListener(e -> {
            dialog.dismiss();
            UserModel model = getUserModel();
            binding.guideLayout.setModel(model);
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();


        });
    }

}