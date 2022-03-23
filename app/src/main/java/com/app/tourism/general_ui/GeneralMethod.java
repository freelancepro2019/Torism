package com.app.tourism.general_ui;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.app.tourism.R;
import com.app.tourism.tags.Tags;

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

    @BindingAdapter("image")
    public static void userImage2(TextView textView ,String image){
        if (image!=null&&!image.isEmpty()){
            String[] s = image.split(" ");
            String name = s[0].charAt(0)+""+s[1].charAt(0);
            textView.setText(name);
        }
    }

    @BindingAdapter("order_status")
    public static void orderStatus(TextView textView ,String order_status){
        if (order_status!=null){
            if (order_status.equals(Tags.status_accepted)){
                textView.setText(R.string.offer);
            }else if (order_status.equals(Tags.status_completed)){
                textView.setText(R.string.completed);

            }else if (order_status.equals(Tags.status_refused)){
                textView.setText(R.string.refused);

            }else if (order_status.equals(Tags.status_rated)){
                textView.setText(R.string.completed);

            }else {
                textView.setText(R.string.status_new);
            }
        }
    }

    @BindingAdapter("order_status_driver")
    public static void orderStatusDriver(TextView textView ,String order_status){
        if (order_status!=null){
            if (order_status.equals(Tags.status_accepted)){
                textView.setText(R.string.accepted);
            }else if (order_status.equals(Tags.status_completed)){
                textView.setText(R.string.completed);

            }else if (order_status.equals(Tags.status_refused)){
                textView.setText(R.string.refused);

            }else {
                textView.setText(R.string.status_new);
            }
        }
    }

    @BindingAdapter("order_status_color")
    public static void orderStatusBackground(CardView cardView , String order_status_color){
        if (order_status_color!=null){
            Context context = cardView.getContext();
            if (order_status_color.equals(Tags.status_accepted)){
                cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.gray4));
            }else if (order_status_color.equals(Tags.status_completed)){
                cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            }else if (order_status_color.equals(Tags.status_refused)){
                cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.red));

            }else if (order_status_color.equals(Tags.status_rated)){
                cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.rate));

            }else {
                cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.gray4));

            }
        }
    }

    @BindingAdapter("order_status_color_text")
    public static void orderStatusColor(TextView textView , String order_status_color_text){
        if (order_status_color_text!=null){
            Context context = textView.getContext();
            if (order_status_color_text.equals(Tags.status_accepted)){
                textView.setTextColor(ContextCompat.getColor(context,R.color.gray4));
            }else if (order_status_color_text.equals(Tags.status_completed)){
                textView.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));

            }else if (order_status_color_text.equals(Tags.status_refused)){
                textView.setTextColor(ContextCompat.getColor(context,R.color.red));

            }else if (order_status_color_text.equals(Tags.status_rated)){
                textView.setTextColor(ContextCompat.getColor(context,R.color.rate));

            }else {
                textView.setTextColor(ContextCompat.getColor(context,R.color.gray4));

            }
        }
    }


}










