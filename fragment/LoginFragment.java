package com.example.myapp_sherkat.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

public class LoginFragment extends Fragment {
    private View view;
    private TextInputEditText ed_phone;
    private TextInputEditText ed_password;
    private Button btn_login;
    private ScrollView scrollView;
    private ImageView imageView_login;
    private SimpleArcProgress loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {

            view = inflater.inflate(R.layout.fragment_login, container, false);

            intViews();

            set_img_page();

            set_icon_inputEditText();

            check_input();

            go_forget_pass();

            return view;

        } else {

            return view;
        }

    }

    private void set_img_page() {
        Glide.with(getContext()).load(R.drawable.ic_login_pic).into(imageView_login);
    }


    private void scroll_to_down() {

        new Handler().postDelayed(() -> {

            scrollView.scrollTo(0, scrollView.getBottom());

        }, 2000);

    }

    private void show_progressbar_btn() {

        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);


        ed_phone.setFocusable(false);
        ed_password.setFocusable(false);

    }

    private void hide_progressbar_btn() {

        loading.setVisibility(View.GONE);
        btn_login.setVisibility(View.VISIBLE);

        ed_phone.setFocusableInTouchMode(true);
        ed_password.setFocusableInTouchMode(true);

    }

    private void go_forget_pass() {
        TextView forget_pass_txt = view.findViewById(R.id.forget_pass_txt);
        forget_pass_txt.setOnClickListener(view -> {

            NavDirections navDirections = LoginFragmentDirections.actionLoginFragmentToForgetPassFragment();
            Navigation.findNavController(forget_pass_txt).navigate(navDirections);

        });
    }

    private void check_input() {

        btn_login.setOnClickListener(view -> {

            if (!(ed_phone.getText().toString().trim().equals("")) && !(ed_password.getText().toString().trim().equals(""))) {

                if (ed_phone.length() == 11) {

                    if (ed_password.length() >= 8) {

                        if (ed_phone.getText().toString().charAt(0) == '0' && ed_phone.getText().toString().charAt(1) == '9') {

                            loginUser();

                        } else {

                            CustomToast.showToast("شماره تلفن صحیح نیست", getContext());

                        }

                    } else {

                        CustomToast.showToast("رمز عبور حداقل باید شامل 8 کاراکتر باشد", getContext());

                    }
                } else {

                    CustomToast.showToast("شماره تلفن صحیح نیست", getContext());

                }

            } else {

                CustomToast.showToast("نام کاربری یا رمز عبور خالی هست", getContext());
            }

        });

    }

    private void loginUser() {

        ApiInterface apiinterface = ApiClient.getInstance().getApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("password", ed_password.getText().toString().trim());
        jsonObject.addProperty("mobile", ed_phone.getText().toString().trim());

        show_progressbar_btn();

        apiinterface.sendPost("login.php", jsonObject).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {


                if (response.isSuccessful()) {

                    switch (response.body().getIduser()) {
                        case 0:

                            CustomToast.showToast("رمز اشتباه هست", getContext());

                            hide_progressbar_btn();

                            break;

                        case -1:

                            CustomToast.showToast("شماره تلفن ثبت نشده است لطفا ثبت نام کنید", getContext());

                            hide_progressbar_btn();

                            break;

                        default:


                            new SharedPrefManager(getContext()).saveUserInfo(
                                    response.body().getIduser(),
                                    response.body().getUsername(),
                                    ed_phone.getText().toString().trim());

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

    private void set_icon_inputEditText() {

        ed_phone.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), R.drawable.ic_phone), null);
        ed_password.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), R.drawable.ic_key), null);

        ed_phone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {

                scroll_to_down();

                ed_phone.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), R.drawable.ic_phone_red), null);

            } else {

                ed_phone.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), R.drawable.ic_phone), null);

            }


        });

        ed_password.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {

                scroll_to_down();

                ed_password.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), R.drawable.ic_key_red), null);

            } else {

                ed_password.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), R.drawable.ic_key), null);
            }

        });


        ed_phone.setOnClickListener(view -> scroll_to_down());

        ed_password.setOnClickListener(view -> scroll_to_down());
    }

    private void intViews() {

        scrollView = view.findViewById(R.id.scroll);
        ed_phone = view.findViewById(R.id.phone_ed);
        ed_password = view.findViewById(R.id.password_ed);
        btn_login = view.findViewById(R.id.btn_login);
        imageView_login = view.findViewById(R.id.login_pic);
        loading = view.findViewById(R.id.loading);
    }
}