package com.example.myapp_sherkat.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapp_sherkat.R;


public class FirstFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view==null) {

            view = inflater.inflate(R.layout.fragment_first, container, false);

            set_buttons();

            return view;

        }else {
            return view;
        }

    }


    private void set_buttons() {
        Button Buttonbtn_login_first = view.findViewById(R.id.login_btn_first);
        Button Buttonbtn_sign_first = view.findViewById(R.id.signup_btn_first);

        Buttonbtn_login_first.setOnClickListener(view -> {

            NavDirections navDirections = FirstFragmentDirections.actionFirstFragmentToLoginFragment();
            Navigation.findNavController(Buttonbtn_login_first).navigate(navDirections);

        });

        Buttonbtn_sign_first.setOnClickListener(view -> {

            NavDirections navDirections = FirstFragmentDirections.actionFirstFragmentToSignupFragment();
            Navigation.findNavController(Buttonbtn_sign_first).navigate(navDirections);

        });
    }
}