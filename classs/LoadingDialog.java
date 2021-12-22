package com.example.myapp_sherkat.classs;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.example.myapp_sherkat.R;

public class LoadingDialog {

    private  static AlertDialog alertDialog;


    public static void showloadingDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(LayoutInflater.from(context).inflate(R.layout.layout_progressbar, null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.background);
        alertDialog.show();

    }


    public static void dismissDialog(){
        alertDialog.dismiss();
    }

}
