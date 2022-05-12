package com.app.tourism.uis.activity_home.fragments.home_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.tourism.R;
import com.app.tourism.adapters.GuideAdapter;
import com.app.tourism.databinding.FilterDialogBinding;
import com.app.tourism.databinding.FragmentHomeBinding;
import com.app.tourism.models.UserModel;
import com.app.tourism.tags.Tags;
import com.app.tourism.uis.activity_send_order.SendOrderActivity;
import com.app.tourism.uis.common_ui.activity_base.FragmentBase;
import com.app.tourism.uis.activity_home.HomeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentHome extends FragmentBase implements  TimePickerDialog.OnTimeSetListener {
    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private GuideAdapter adapter;
    private DatabaseReference dRef;
    private String filterBy = "all";
    private String dialogFilterQuery = filterBy;
    private int time_type;
    private FilterDialogBinding filterDialogBinding;
    private TimePickerDialog timePickerDialog;
    private String fromTime = "";
    private String toTime = "";


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        dRef = FirebaseDatabase.getInstance().getReference();
        binding.setFilter(getString(R.string.All));
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new GuideAdapter(activity, getLang(), this);
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(() -> getGuides(filterBy));
        binding.recViewLayout.tvNoData.setText(R.string.no_guide);
        binding.cardViewSearch.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.searchFragment);
        });
        binding.filter.setOnClickListener(view -> showFilterDialog());
        binding.tvFilter.setOnClickListener(view -> showFilterDialog());

        getGuides(filterBy);
    }

    private void getGuides(String filterBy) {
        binding.recViewLayout.swipeRefresh.setRefreshing(true);
        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        Query query = dRef.child(Tags.USERS_TABLE);
        query.orderByChild("user_type")
                .equalTo(Tags.user_guide)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        binding.recViewLayout.swipeRefresh.setRefreshing(false);
                        List<UserModel> guidesList = new ArrayList<>();
                        if (snapshot.getValue() != null) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                UserModel userModel = ds.getValue(UserModel.class);
                                if (userModel != null) {
                                    if (filterBy.equals("all")) {
                                        if (!fromTime.isEmpty()&&!toTime.isEmpty()){
                                            if (isDateBetween(userModel.getFrom_time(),userModel.getTo_time())){
                                                guidesList.add(userModel);
                                            }
                                        }else {
                                            guidesList.add(userModel);
                                        }


                                    } else if (filterBy.equals("male_ar")) {
                                        if (userModel.getGender().equals(Tags.male) && userModel.getLanguage().equals("ar")) {
                                            if (!fromTime.isEmpty()&&!toTime.isEmpty()){
                                                if (isDateBetween(userModel.getFrom_time(),userModel.getTo_time())){
                                                    guidesList.add(userModel);
                                                }
                                            }else {
                                                guidesList.add(userModel);
                                            }


                                        }

                                    } else if (filterBy.equals("male_en")) {
                                        if (userModel.getGender().equals(Tags.male) && userModel.getLanguage().equals("en")) {
                                            if (!fromTime.isEmpty()&&!toTime.isEmpty()){
                                                if (isDateBetween(userModel.getFrom_time(),userModel.getTo_time())){
                                                    guidesList.add(userModel);
                                                }
                                            }else {
                                                guidesList.add(userModel);
                                            }

                                        }

                                    } else if (filterBy.equals("female_ar")) {
                                        if (userModel.getGender().equals(Tags.female) && userModel.getLanguage().equals("ar")) {
                                            if (!fromTime.isEmpty()&&!toTime.isEmpty()){
                                                if (isDateBetween(userModel.getFrom_time(),userModel.getTo_time())){
                                                    guidesList.add(userModel);
                                                }
                                            }else {
                                                guidesList.add(userModel);
                                            }

                                        }

                                    } else if (filterBy.equals("female_en")) {
                                        if (userModel.getGender().equals(Tags.female) && userModel.getLanguage().equals("en")) {
                                            if (!fromTime.isEmpty()&&!toTime.isEmpty()){
                                                if (isDateBetween(userModel.getFrom_time(),userModel.getTo_time())){
                                                    guidesList.add(userModel);
                                                }
                                            }else {
                                                guidesList.add(userModel);
                                            }


                                        }

                                    }
                                }
                            }

                            if (guidesList.size() > 0) {
                                adapter.updateList(sortedList(guidesList));
                                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
                            } else {
                                adapter.updateList(new ArrayList<>());

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
        Collections.sort(list, (m1, m2) -> Double.compare(m2.getRate(), m1.getRate()));
        return list;
    }

    private void createTimeDialog() {
        Calendar now = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), false);
        timePickerDialog.setAccentColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        timePickerDialog.setTitle("");
        timePickerDialog.setCancelColor(ContextCompat.getColor(activity, R.color.gray4));
        timePickerDialog.setCancelText(getString(R.string.cancel));
        timePickerDialog.setOkColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        timePickerDialog.setOkText(R.string.select);
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        timePickerDialog.show(activity.getFragmentManager(), "");
    }

    private void showFilterDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        filterDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.filter_dialog, null, false);
        filterDialogBinding.setFromTime(fromTime);
        filterDialogBinding.setToTime(toTime);

        if (filterBy.equals("male_ar")) {
            filterDialogBinding.rbMaleAr.setChecked(true);
        } else if (filterBy.equals("male_en")) {
            filterDialogBinding.rbMaleEn.setChecked(true);

        } else if (filterBy.equals("female_ar")) {
            filterDialogBinding.rbFemaleAr.setChecked(true);

        } else if (filterBy.equals("female_en")) {
            filterDialogBinding.rbFemaleEn.setChecked(true);

        } else {
            filterDialogBinding.rbAll.setChecked(true);

        }

        filterDialogBinding.rbAll.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                dialogFilterQuery = "all";
            }
        });
        filterDialogBinding.rbMaleAr.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                dialogFilterQuery = "male_ar";
            }
        });

        filterDialogBinding.rbMaleEn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                dialogFilterQuery = "male_en";
            }
        });


        filterDialogBinding.rbFemaleAr.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                dialogFilterQuery = "female_ar";
            }
        });

        filterDialogBinding.rbFemaleEn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                dialogFilterQuery = "female_en";
            }
        });

        filterDialogBinding.llFromTime.setOnClickListener(view -> {
            time_type = 1;
            createTimeDialog();
        });

        filterDialogBinding.llToTime.setOnClickListener(view -> {
            time_type = 2;
            createTimeDialog();
        });

        filterDialogBinding.clearTime.setOnClickListener(view -> {
            fromTime ="";
            toTime="";
            filterDialogBinding.setFromTime(fromTime);
            filterDialogBinding.setToTime(toTime);
        });


        filterDialogBinding.btnFilter.setOnClickListener(view -> {
            filterBy = dialogFilterQuery;

            if (dialogFilterQuery.equals("all")) {
                binding.setFilter(getString(R.string.All));
            } else if (dialogFilterQuery.equals("male_ar")) {
                binding.setFilter(getString(R.string.male_ar));
            } else if (dialogFilterQuery.equals("male_en")) {
                binding.setFilter(getString(R.string.male_en));

            } else if (dialogFilterQuery.equals("female_ar")) {
                binding.setFilter(getString(R.string.female_ar));

            } else if (dialogFilterQuery.equals("female_en")) {
                binding.setFilter(getString(R.string.female_en));

            }


            getGuides(filterBy);


            dialog.dismiss();
        });

        filterDialogBinding.imageClose.setOnClickListener(view -> dialog.dismiss());

        dialog.setContentView(filterDialogBinding.getRoot());
        dialog.show();
    }

    public void setItemData(UserModel guideModel) {
        Intent intent = new Intent(activity, SendOrderActivity.class);
        intent.putExtra("data", guideModel);
        startActivity(intent);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        if (time_type == 1) {
            fromTime = dateFormat.format(calendar.getTime());
            filterDialogBinding.setFromTime(fromTime);
        } else if (time_type == 2) {
            toTime = dateFormat.format(calendar.getTime());
            filterDialogBinding.setToTime(toTime);

        }


    }

    private boolean isDateBetween(String from, String to) {
        Log.e("from",from+"__"+to);
        if (!from.isEmpty()&&!to.isEmpty()){

            SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            try {
                Date fromDateDriver = format.parse(from);
                Calendar calendarFrom = Calendar.getInstance();
                calendarFrom.setTime(fromDateDriver);
                calendarFrom.add(Calendar.MINUTE,-1);
                fromDateDriver = calendarFrom.getTime();

                Date toDateDriver = format.parse(to);

                Date fromDate = format.parse(fromTime);
                Date toDate = format.parse(toTime);

                Log.e("date",fromDate+"_"+toDate+"_____"+fromDateDriver+"_"+toDateDriver);
                if (fromDate.after(fromDateDriver)&&fromDate.before(toDateDriver)){
                    return true;
                }

            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("sdasda",e.getMessage());
            }

        }
        Log.e("sda","tt");
        return false;

    }

}