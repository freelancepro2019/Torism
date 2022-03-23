package com.app.tourism.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String user_id;
    private String first_name;
    private String last_name;
    private String full_name;
    private String phone_code;
    private String phone;
    private String email;
    private String password;
    private String gender;
    private String date_of_birth;
    private String car_number;
    private String user_type;
    //guide
    private boolean canReceiveOrders;
    private int rate;

    public UserModel() {
    }

    public UserModel(String user_id, String first_name, String last_name, String phone_code, String phone, String email, String password, String gender, String date_of_birth, String car_number, String user_type) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_code = phone_code;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.car_number = car_number;
        this.user_type = user_type;
        this.canReceiveOrders = true;
        this.rate = 0;
        this.full_name = first_name+" "+last_name;

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public boolean isCanReceiveOrders() {
        return canReceiveOrders;
    }

    public void setCanReceiveOrders(boolean canReceiveOrders) {
        this.canReceiveOrders = canReceiveOrders;
    }


    public float getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
