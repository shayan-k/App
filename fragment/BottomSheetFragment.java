package com.example.myapp_sherkat.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapp_sherkat.R;
import com.example.myapp_sherkat.activity.SignLoginActivity;
import com.example.myapp_sherkat.classs.SharedPrefManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private View view;
    private SharedPrefManager sharedPrefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        set_info();

        set_exist_account();

        set_img();

        return view;

    }

    private void set_img() {
        ImageView img = view.findViewById(R.id.img_user_info);
        Glide.with(getContext()).load(R.drawable.ic_user_info_pic).into(img);
    }

    private void set_exist_account() {

        Button btn_exist_account = view.findViewById(R.id.exist_account);
        btn_exist_account.setOnClickListener(view -> {
            dialog();
        });
    }

    private void dialog() {

        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_exist_account, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCancelable(true);
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();

        TextView yes = view1.findViewById(R.id.yes);
        TextView cancel = view1.findViewById(R.id.cancel);

        yes.setOnClickListener(view -> {
            sharedPrefManager.remove();
            startActivity(new Intent(getContext(), SignLoginActivity.class));
            getActivity().finish();
        });
        cancel.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.background);
        alertDialog.show();


    }

    private void set_info() {
        sharedPrefManager = new SharedPrefManager(getContext());

        TextView user_txt = view.findViewById(R.id.username);

        TextView phone_txt = view.findViewById(R.id.phone);

        user_txt.append(sharedPrefManager.getUserUsername());
        phone_txt.append(sharedPrefManager.getUserPhone());

    }
}