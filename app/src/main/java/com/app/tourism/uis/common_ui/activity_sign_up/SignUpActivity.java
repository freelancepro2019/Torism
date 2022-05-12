package com.app.tourism.uis.common_ui.activity_sign_up;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.tourism.R;
import com.app.tourism.databinding.ActivitySignUpBinding;
import com.app.tourism.models.SignUpModel;
import com.app.tourism.models.UserModel;
import com.app.tourism.tags.Common;
import com.app.tourism.tags.Tags;
import com.app.tourism.uis.common_ui.activity_base.ActivityBase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends ActivityBase implements DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener {
    private ActivitySignUpBinding binding;
    private SignUpModel model;
    private FirebaseAuth mAuth;
    private DatabaseReference dRef;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private int time_type; //1 from 2 to
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        model = new SignUpModel();
        if (getUserModel() != null) {
            model.setFirstName(getUserModel().getFirst_name());
            model.setLastName(getUserModel().getLast_name());
            model.setPhoneCode(getUserModel().getPhone_code());
            model.setPhone(getUserModel().getPhone());
            model.setBirthDate(getUserModel().getDate_of_birth());
            model.setUserType(getUserModel().getUser_type());
            model.setEmail(getUserModel().getEmail());
            model.setPassword(getUserModel().getPassword());
            model.setCarNumber(getUserModel().getCar_number());

            if (getUserModel().getUser_type().equals(Tags.user_guide)){
                binding.llGuideData.setVisibility(View.VISIBLE);
            }
            binding.consUserType.setVisibility(View.GONE);
            binding.tv.setText(R.string.update_profile);
            binding.btnSignUp.setText(R.string.update_profile);
            binding.llLogin.setVisibility(View.GONE);
            if (getUserModel().getGender().equals(Tags.male)) {
                binding.rbMale.setChecked(true);
            } else {
                binding.rbFemale.setChecked(true);
            }

        }
        binding.setModel(model);

        binding.llLogin.setOnClickListener(view -> {
            navigateToLoginActivity();
        });

        binding.btnSignUp.setOnClickListener(view -> {
            if (model.isDataValid(this)) {
                if (getUserModel() == null) {
                    createAccount();

                } else {
                    reAuth();
                }
            }
        });

        binding.rbMale.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                model.setGender(Tags.male);
            }
        });
        binding.rbFemale.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                model.setGender(Tags.female);
            }
        });

        binding.cardTourist.setOnClickListener(view -> {
            binding.llGuideData.setVisibility(View.GONE);

            binding.cardTourist.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            binding.imageTourist.setColorFilter(ContextCompat.getColor(this, R.color.white));
            binding.tvTourist.setTextColor(ContextCompat.getColor(this, R.color.white));

            binding.cardTouristGuide.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
            binding.imageGuide.setColorFilter(ContextCompat.getColor(this, R.color.black));
            binding.tvGuide.setTextColor(ContextCompat.getColor(this, R.color.black));

            model.setUserType(Tags.user_normal);

        });

        binding.cardTouristGuide.setOnClickListener(view -> {
            binding.llGuideData.setVisibility(View.VISIBLE);
            binding.cardTouristGuide.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            binding.imageGuide.setColorFilter(ContextCompat.getColor(this, R.color.white));
            binding.tvGuide.setTextColor(ContextCompat.getColor(this, R.color.white));


            binding.cardTourist.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
            binding.imageTourist.setColorFilter(ContextCompat.getColor(this, R.color.black));
            binding.tvTourist.setTextColor(ContextCompat.getColor(this, R.color.black));


            model.setUserType(Tags.user_guide);

        });

        binding.llFromTime.setOnClickListener(view -> {
            time_type = 1;
            createTimeDialog();
        });

        binding.llToTime.setOnClickListener(view -> {
            time_type = 2;
            createTimeDialog();
        });

        binding.rbAr.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                model.setLanguage("ar");
            }
        });

        binding.rbEn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                model.setLanguage("en");
            }
        });
        binding.llDate.setOnClickListener(view -> {
            createDateDialog();
        });
    }

    private void createDateDialog() {
        Calendar now = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        datePickerDialog.setTitle(getString(R.string.date_of_birth));
        datePickerDialog.setMaxDate(now);
        datePickerDialog.setCancelColor(ContextCompat.getColor(this, R.color.gray4));
        datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setOkColor(ContextCompat.getColor(this, R.color.colorPrimary));
        datePickerDialog.setOkText(R.string.select);
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.show(getFragmentManager(), "");
    }

    private void createTimeDialog() {
        Calendar now = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND),false);
        timePickerDialog.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        timePickerDialog.setTitle("");
        timePickerDialog.setCancelColor(ContextCompat.getColor(this, R.color.gray4));
        timePickerDialog.setCancelText(getString(R.string.cancel));
        timePickerDialog.setOkColor(ContextCompat.getColor(this, R.color.colorPrimary));
        timePickerDialog.setOkText(R.string.select);
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        timePickerDialog.show(getFragmentManager(), "");
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

        UserModel userModel = new UserModel(user_id, model.getFirstName(), model.getLastName(), model.getPhoneCode(), model.getPhone(), model.getEmail(), model.getPassword(), model.getGender(), model.getBirthDate(), model.getCarNumber(), model.getUserType(),model.getLanguage(),model.getFrom_time(),model.getTo_time());
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.USERS_TABLE)
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

    private void reAuth() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(getUserModel().getEmail(), getUserModel().getPassword());
            currentUser.reauthenticate(credential).addOnSuccessListener(unused -> {


                currentUser.updateEmail(model.getEmail())
                        .addOnSuccessListener(unused1 -> {
                            currentUser.updatePassword(model.getPassword())
                                    .addOnSuccessListener(unused2 -> {
                                        updateProfile(dialog);
                                    }).addOnFailureListener(e -> {
                                dialog.dismiss();
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            });
                        }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                });

            }).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Session expired login again", Toast.LENGTH_SHORT).show();

        }

    }

    private void updateProfile(ProgressDialog dialog) {
        UserModel userModel = new UserModel(getUserModel().getUser_id(), model.getFirstName(), model.getLastName(), model.getPhoneCode(), model.getPhone(), model.getEmail(), model.getPassword(), model.getGender(), model.getBirthDate(), model.getCarNumber(), model.getUserType(),model.getLanguage(),model.getFrom_time(),model.getTo_time());
        userModel.setRate(getUserModel().getRate());
        userModel.setCanReceiveOrders(getUserModel().isCanReceiveOrders());
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.USERS_TABLE)
                .child(getUserModel().getUser_id())
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
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);
        String dateOfBirth = df.format(calendar.getTime());
        model.setBirthDate(dateOfBirth);
        int age = Math.abs(now.get(Calendar.YEAR) - calendar.get(Calendar.YEAR));
        binding.tvAge.setText(age + "");
        binding.setModel(model);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
        if (time_type==1){
            String time = dateFormat.format(calendar.getTime());
            model.setFrom_time(time);

        }else if (time_type ==2){
            String time = dateFormat.format(calendar.getTime());
            model.setTo_time(time);

        }

        binding.setModel(model);

    }
}