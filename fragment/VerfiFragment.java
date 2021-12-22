package com.example.myapp_sherkat.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.developers.coolprogressviews.SimpleArcProgress;
import com.example.myapp_sherkat.R;
import com.example.myapp_sherkat.activity.MainActivity;
import com.example.myapp_sherkat.classs.ApiClient;
import com.example.myapp_sherkat.classs.CustomToast;
import com.example.myapp_sherkat.classs.SharedPrefManager;
import com.example.myapp_sherkat.classs.User;
import com.example.myapp_sherkat.interfaces.ApiInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerfiFragment extends Fragment {

    private View view;
    private Button btn_verfi;
    private TextInputEditText ed_code;
    private SimpleArcProgress loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view==null) {

            view = inflater.inflate(R.layout.fragment_verfi, container, false);

            init_views();

            go_to_next();

            return view;

        }else {
            return view;
        }


    }

    private void go_to_next() {

        btn_verfi.setOnClickListener(view -> {

            if(ed_code.length()==5){
                if(!ed_code.getText().toString().trim().equals("")){

                    varification();

                }else {
                    CustomToast.showToast("کد خالی هست" ,getContext());
                }

            }else {
                CustomToast.showToast("کد نامعتبر هست" ,getContext());
            }

        });

    }

    private void   varification() {

        ApiInterface apiinterface = ApiClient.getInstance().getApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", ed_code.getText().toString().trim());
        jsonObject.addProperty("mobile",getArguments().getString("phone"));

        show_progressbar_btn();

        apiinterface.sendPost("verify_code.php", jsonObject).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {


                if (response.isSuccessful()) {

                    switch (response.body().getResult_verify()) {
                        case "Expired code":

                            CustomToast.showToast("کد منقضی شده مجددا امتحان کنید", getContext());

                            hide_progressbar_btn();

                            break;

                        case "No match":

                            CustomToast.showToast("کد وارد شده اشتباه هست",getContext());

                            hide_progressbar_btn();

                            break;

                        default:

                            new SharedPrefManager(getContext()).saveUserInfo(
                                    getArguments().getInt("iduser"),
                                    getArguments().getString("username"),
                                    getArguments().getString("phone"));

                            CustomToast.showToast("خوش آمدید", getContext());

                            startActivity(new Intent(getContext(), MainActivity.class));

                            getActivity().finish();

                            break;


                    }


                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                hide_progressbar_btn();

                CustomToast.showToast("اینترنت خود را بررسی کنید!", getContext());
            }
        });



    }
    private void show_progressbar_btn() {

        loading.setVisibility(View.VISIBLE);
        btn_verfi.setVisibility(View.GONE);

        ed_code.setFocusable(false);

    }

    private void hide_progressbar_btn() {

        loading.setVisibility(View.GONE);
        btn_verfi.setVisibility(View.VISIBLE);

        ed_code.setFocusableInTouchMode(true);
    }

    private void init_views() {
        btn_verfi = view.findViewById(R.id.btn_verfi);
        ed_code = view.findViewById(R.id.code_ed);
        loading = view.findViewById(R.id.loading);

    }
}