package com.example.myapp_sherkat.classs;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp_sherkat.R;


public class CustomToast {


    public static void showToast(String textTost, Context context) {
        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View toastView = inflater.inflate(R.layout.toast_layout, null);
            Toast toast = new Toast(context);
            toast.setView(toastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                    0, 40);
            TextView textView = toastView.findViewById(R.id.tv_toast);
            textView.setText(textTost);
            toast.show();
        }


    }


}
