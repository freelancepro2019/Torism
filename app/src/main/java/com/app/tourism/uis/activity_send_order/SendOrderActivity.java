package com.app.tourism.uis.activity_send_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.tourism.R;
import com.app.tourism.databinding.ActivitySendOrderBinding;
import com.app.tourism.models.OfferModel;
import com.app.tourism.models.UserModel;
import com.app.tourism.tags.Common;
import com.app.tourism.tags.Tags;
import com.app.tourism.uis.common_ui.activity_base.ActivityBase;
import com.app.tourism.uis.common_ui.activity_login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SendOrderActivity extends ActivityBase implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private ActivitySendOrderBinding binding;
    private OfferModel offerModel;
    private UserModel guideModel;
    private DatabaseReference dRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_order);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        guideModel = (UserModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        offerModel = new OfferModel();
        binding.setLang(getLang());
        binding.setModel(guideModel);
        binding.setOfferModel(offerModel);
        binding.llBack.setOnClickListener(view -> finish());
        binding.cardDate.setOnClickListener(view -> createDateDialog());
        binding.cardTime.setOnClickListener(view -> createTimeDialog());

        binding.btnBook.setOnClickListener(view -> {
            sendOrder();
        });

        getGuideData();
    }

    private void getGuideData() {
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.USERS_TABLE)
                .orderByChild("user_id")
                .equalTo(guideModel.getUser_id());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    UserModel userModel = snapshot.child(guideModel.getUser_id()).getValue(UserModel.class);
                    if (userModel != null) {
                        guideModel = userModel;
                        Log.e("status", userModel.isCanReceiveOrders() + "");
                    } else {
                        guideModel.setCanReceiveOrders(false);
                        Common.createAlertDialog(SendOrderActivity.this, getString(R.string.error_msg));
                    }
                } else {
                    guideModel.setCanReceiveOrders(false);
                    Toast.makeText(SendOrderActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("error_login", error.getMessage());

            }
        });

    }

    private void sendOrder() {
        if (guideModel.isCanReceiveOrders()) {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            dRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference mRef = dRef.child(Tags.ORDERS_TABLE);
            String order_id = mRef.push().getKey();
            offerModel.setGuide_id(guideModel.getUser_id());
            offerModel.setGuide_name(guideModel.getFirst_name() + " " + guideModel.getLast_name());
            offerModel.setGuide_phone(guideModel.getPhone_code() + guideModel.getPhone());
            offerModel.setUser_id(getUserModel().getUser_id());
            offerModel.setUser_name(getUserModel().getFirst_name() + " " + getUserModel().getLast_name());
            offerModel.setUser_phone(getUserModel().getPhone_code() + getUserModel().getPhone());
            offerModel.setDate(getNowDate());
            offerModel.setOrder_id(order_id);
            mRef.child(order_id).setValue(offerModel)
                    .addOnSuccessListener(unused -> {
                        dialog.dismiss();
                        Toast.makeText(this, R.string.suc, Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            String msg = guideModel.getFirst_name() + " " + guideModel.getLast_name() + " " + getString(R.string.cnt_receive_order);
            Common.createAlertDialog(this, msg);
        }


    }

    private void createDateDialog() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, 1);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        datePickerDialog.setTitle(getString(R.string.booking_date));
        datePickerDialog.setMinDate(now);
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setCancelColor(ContextCompat.getColor(this, R.color.gray4));
        datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setOkColor(ContextCompat.getColor(this, R.color.colorPrimary));
        datePickerDialog.setOkText(R.string.select);
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        try {
            datePickerDialog.show(getFragmentManager(), "");

        } catch (Exception e) {
        }
    }

    private void createTimeDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
        timePickerDialog.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        timePickerDialog.setTitle(getString(R.string.booking_date));
        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setCancelColor(ContextCompat.getColor(this, R.color.gray4));
        timePickerDialog.setCancelText(getString(R.string.cancel));
        timePickerDialog.setOkColor(ContextCompat.getColor(this, R.color.colorPrimary));
        timePickerDialog.setOkText(R.string.select);
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_1);
        try {
            timePickerDialog.show(getFragmentManager(), "");

        } catch (Exception e) {
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String date = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH).format(calendar.getTime());
        offerModel.setBooking_date(date);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        String time = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(calendar.getTime());
        offerModel.setBooking_time(time);
    }

    private String getNowDate() {
        Calendar now = Calendar.getInstance();
        return new SimpleDateFormat("dd/MMM/yyyy hh:mm a", Locale.ENGLISH).format(now.getTime());
    }
}