package com.app.tourism.models;

import android.content.Context;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.app.tourism.BR;
import com.app.tourism.R;

import java.io.Serializable;

public class SignUpModel extends BaseObservable implements Serializable {
    private String firstName;
    private String lastName;
    private String phoneCode;
    private String phone;
    private String email;
    private String password;
    private String birthDate;
    private String carNumber;
    private String gender;
    private String userType;
    public ObservableField<String> error_first_name = new ObservableField<>();
    public ObservableField<String> error_last_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();
    public ObservableField<String> error_birth_date = new ObservableField<>();
    public ObservableField<String> error_car_number = new ObservableField<>();

    public SignUpModel() {
        firstName = "";
        lastName = "";
        phoneCode = "+966";
        phone = "";
        email = "";
        password = "";
        birthDate ="";
        carNumber ="";
        gender = "male";
        userType ="normal";

    }

    public boolean isDataValid(Context context) {
        if (!firstName.isEmpty() &&
                !lastName.isEmpty() &&
                !phone.isEmpty() &&
                !email.isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length() >= 6&&
                !birthDate.isEmpty()

        ) {

            error_first_name.set(null);
            error_last_name.set(null);
            error_phone.set(null);
            error_email.set(null);
            error_password.set(null);
            error_birth_date.set(null);
            if (userType.equals("guide")){
                if (!carNumber.isEmpty()){
                    error_car_number.set(null);

                    return true;
                }else {
                    error_car_number.set(context.getString(R.string.field_required));
                    return false;
                }
            }else {
                return true;
            }
        } else {
            if (email.isEmpty()) {
                error_email.set(context.getString(R.string.field_required));
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                error_email.set(context.getString(R.string.inv_email));

            } else {
                error_email.set(null);
            }

            if (firstName.isEmpty()) {
                error_first_name.set(context.getString(R.string.field_required));
            }else {
                error_first_name.set(null);
            }

            if (lastName.isEmpty()) {
                error_last_name.set(context.getString(R.string.field_required));
            }else {
                error_last_name.set(null);
            }

            if (phone.isEmpty()) {
                error_phone.set(context.getString(R.string.field_required));
            }else {
                error_phone.set(null);
            }

            if (password.isEmpty()) {
                error_password.set(context.getString(R.string.field_required));
            } else if (password.length() < 6) {
                error_password.set(context.getString(R.string.pass_too_short));

            } else {
                error_password.set(null);
            }

            if (birthDate.isEmpty()) {
                error_birth_date.set(context.getString(R.string.field_required));
            }else {
                error_birth_date.set(null);
            }

            if (userType.equals("guide")){
                if (!carNumber.isEmpty()){
                    error_car_number.set(null);
                }else {
                    error_car_number.set(context.getString(R.string.field_required));
                }
            }
            return false;
        }

    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    @Bindable
    public String getLastName() {
        return lastName;

    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    @Bindable
    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
        notifyPropertyChanged(BR.phoneCode);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
        notifyPropertyChanged(BR.birthDate);
    }

    @Bindable
    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
        notifyPropertyChanged(BR.carNumber);
    }

   @Bindable
   public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        notifyPropertyChanged(BR.gender);
    }

    @Bindable
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
        notifyPropertyChanged(BR.userType);
    }
}
