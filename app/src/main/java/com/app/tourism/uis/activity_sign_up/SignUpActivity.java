package com.app.tourism.uis.activity_sign_up;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.app.tourism.R;
import com.app.tourism.databinding.ActivitySignUpBinding;
import com.app.tourism.models.SignUpModel;
import com.app.tourism.models.UserModel;
import com.app.tourism.tags.Common;
import com.app.tourism.tags.Tags;
import com.app.tourism.uis.activity_base.ActivityBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends ActivityBase implements DatePickerDialog.OnDateSetListener{
    private ActivitySignUpBinding binding;
    private SignUpModel model;
    private FirebaseAuth mAuth;
    private DatabaseReference dRef;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        model = new SignUpModel();
        binding.setModel(model);

        binding.llLogin.setOnClickListener(view -> {
            navigateToLoginActivity();
        });

        binding.btnSignUp.setOnClickListener(view -> {
            if (model.isDataValid(this)) {
                createAccount();
            }
        });

        binding.rbMale.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                model.setGender(Tags.male);
            }
        });
        binding.rbFemale.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                model.setGender(Tags.female);
            }
        });

        binding.cardTourist.setOnClickListener(view -> {
            binding.tiCarNumber.setVisibility(View.GONE);

            binding.cardTourist.setCardBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
            binding.imageTourist.setColorFilter(ContextCompat.getColor(this,R.color.white));
            binding.tvTourist.setTextColor(ContextCompat.getColor(this,R.color.white));

            binding.cardTouristGuide.setCardBackgroundColor(ContextCompat.getColor(this,R.color.white));
            binding.imageGuide.setColorFilter(ContextCompat.getColor(this,R.color.black));
            binding.tvGuide.setTextColor(ContextCompat.getColor(this,R.color.black));

            model.setUserType(Tags.user_normal);

        });

        binding.cardTouristGuide.setOnClickListener(view -> {
            binding.tiCarNumber.setVisibility(View.VISIBLE);
            binding.cardTouristGuide.setCardBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
            binding.imageGuide.setColorFilter(ContextCompat.getColor(this,R.color.white));
            binding.tvGuide.setTextColor(ContextCompat.getColor(this,R.color.white));


            binding.cardTourist.setCardBackgroundColor(ContextCompat.getColor(this,R.color.white));
            binding.imageTourist.setColorFilter(ContextCompat.getColor(this,R.color.black));
            binding.tvTourist.setTextColor(ContextCompat.getColor(this,R.color.black));


            model.setUserType(Tags.user_guide);

        });

        binding.llDate.setOnClickListener(view -> {
            createDateDialog();
        });
    }

    private void createDateDialog() {
        Calendar now = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this,now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setAccentColor(ContextCompat.getColor(this,R.color.colorPrimary));
        datePickerDialog.setTitle(getString(R.string.date_of_birth));
        datePickerDialog.setMaxDate(now);
        datePickerDialog.setCancelColor(ContextCompat.getColor(this,R.color.gray4));
        datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setOkColor(ContextCompat.getColor(this,R.color.colorPrimary));
        datePickerDialog.setOkText(R.string.select);
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.show(getFragmentManager(),"");
    }

    private void createAccount() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.logging_in));
        dialog.setCancelable(false);
        dialog.show();
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(model.getEmail(), model.getPassword())
                .addOnSuccessListener(authResult -> {
                    if (authResult != null && authResult.getUser() != null) {
                        signUp(authResult.getUser().getUid(), dialog);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(e -> {
            Common.createAlertDialog(SignUpActivity.this, e.getMessage());

            dialog.dismiss();
        });
    }

    private void signUp(String user_id, ProgressDialog dialog) {
        UserModel userModel = new UserModel(user_id, model.getFirstName(), model.getLastName(), model.getPhoneCode(), model.getPhone(), model.getEmail(), model.getPassword(),model.getGender(),model.getBirthDate(),model.getCarNumber(),model.getUserType());
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.table_patients)
                .child(user_id)
                .setValue(userModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    setUserModel(userModel);
                    setResult(RESULT_OK);
                    finish();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Common.createAlertDialog(this, e.getMessage());
        });
    }

    private void navigateToLoginActivity() {
        finish();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);
        String dateOfBirth = df.format(calendar.getTime());
        model.setBirthDate(dateOfBirth);
        int age = Math.abs(now.get(Calendar.YEAR)-calendar.get(Calendar.YEAR));
        binding.tvAge.setText(age+"");
        binding.setModel(model);
    }
}