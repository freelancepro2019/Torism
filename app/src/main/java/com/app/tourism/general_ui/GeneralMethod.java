package com.app.tourism.general_ui;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }

    @BindingAdapter({"f_name","l_name"})
    public static void userImage(TextView textView ,String f_name,String l_name){
        if (f_name!=null&&!f_name.isEmpty()&&l_name!=null&&!l_name.isEmpty()){
            String name = f_name.charAt(0)+""+l_name.charAt(0);
            textView.setText(name);
        }
    }


}










