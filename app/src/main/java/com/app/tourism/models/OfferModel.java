package com.app.tourism.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.app.tourism.BR;
import com.app.tourism.tags.Tags;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

import kotlin.jvm.Transient;

public class OfferModel extends BaseObservable implements Serializable {
    private String order_id;
    private String guide_id;
    private String guide_name;
    private String guide_phone;
    private String user_id;
    private String user_name;
    private String user_phone;
    private String address;
    private String details;
    private String booking_date;
    private String booking_time;
    private String date;
    private String order_status;
    private String order_cost;
    @Exclude
    private boolean isValid =false;
    @Exclude
    public ObservableField<String> error_address = new ObservableField<>();
    @Exclude
    public ObservableField<String> error_details = new ObservableField<>();
    @Exclude
    public ObservableField<String> error_date = new ObservableField<>();
    @Exclude
    public ObservableField<String> error_time = new ObservableField<>();

    public OfferModel() {
        guide_id = "";
        guide_name = "";
        guide_phone = "";
        user_id = "";
        user_name = "";
        user_phone = "";
        address = "";
        details = "";
        booking_date = "";
        booking_time = "";
        order_cost="0.0";
        order_status= Tags.status_new;
    }

    public void isDataValid() {
        if (!address.isEmpty() &&
                !details.isEmpty() &&
                !booking_date.isEmpty() &&
                !booking_time.isEmpty()
        ) {
            setValid(true);
        } else {
            setValid(false);
        }
    }

    public String getGuide_id() {
        return guide_id;
    }

    public void setGuide_id(String guide_id) {
        this.guide_id = guide_id;
    }

    public String getGuide_name() {
        return guide_name;
    }

    public void setGuide_name(String guide_name) {
        this.guide_name = guide_name;
    }

    public String getGuide_phone() {
        return guide_phone;
    }

    public void setGuide_phone(String guide_phone) {
        this.guide_phone = guide_phone;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
        isDataValid();
    }

    @Bindable
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
        notifyPropertyChanged(BR.details);
        isDataValid();
    }

    @Bindable
    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
        notifyPropertyChanged(BR.booking_date);
        isDataValid();
    }

    @Bindable
    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
        notifyPropertyChanged(BR.booking_time);
        isDataValid();
    }

    @Exclude
    @Bindable
    public boolean isValid() {
        return isValid;
    }

    @Exclude
    public void setValid(boolean valid) {
        isValid = valid;
        notifyPropertyChanged(BR.valid);
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_cost() {
        return order_cost;
    }

    public void setOrder_cost(String order_cost) {
        this.order_cost = order_cost;
    }
}
