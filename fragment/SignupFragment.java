package com.example.myapp_sherkat.fragment;

import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.developers.coolprogressviews.SimpleArcProgress;
import com.example.myapp_sherkat.R;
import com.example.myapp_sherkat.classs.ApiClient;
import com.example.myapp_sherkat.classs.CustomToast;
import com.example.myapp_sherkat.classs.User;
import com.example.myapp_sherkat.interfaces.ApiInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SignupFragment extends Fragment {
    private View view;
    private TextInputEditText ed_phone;
    private TextInputEditText ed_password;
    private TextInputEditText ed_username;
    private Button btn_signup;
    private SimpleArcProgress loading;
    private ImageView imageView_sign;
    private ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {

            view = inflater.inflate(R.layout.fragment_signup, container, false);

            intViews();

            set_img_page();

            set_icon_inputEditText();

            check_input();

            return view;

        } else {
            return view;
        }

    }

    private void scroll_to_down() {

        new Handler().postDelayed(() -> {

            scrollView.scrollTo(0, scrollView.getBottom());

        }, 2000);

    }

    private void set_img_page() {

        Glide.with(getContext()).load(R.drawable.ic_signup_pic).into(imageView_sign);

    }

    private void check_input() {

        btn_signup.setOnClickListener((View.OnClickListener) v -> {

                    if (!(ed_username.getText().toString().trim().equals("")) && !(ed_password.getText().toString().trim().equals("")) && !(ed_phone.getText().toString().trim().equals(""))) {
                        if (ed_phone.length() == 11) {

                            if (ed_phone.getText().toString().charAt(0) == '0' && ed_phone.getText().toString().charAt(1) == '9') {

                                if (ed_username.length() >= 3) {

                                    if (ed_password.length() >= 8) {

                                        sginUp();

                                    } else {

                                        CustomToast.showToast("رمز عبور حداقل باید شامل 8 کاراکتر باشد", getContext());
                                    }
                                } else {
                                    CustomToast.showToast("نام کاربری حداقل باید شامل 3 کاراکتر باشد", getContext());

                                }

                            } else {
                                CustomToast.showToast("شماره تلفن صحیح نیست", getContext());

                            }


                        } else {
                            CustomToast.showToast("شماره تلفن صحیح نیست", getContext());

                        }


                    } else {

                        CustomToast.showToast("همه یا یکی از فیلدها خالی هست", getContext());

                    }


                }

        );

    }

    private void show_progressbar_btn() {
        loading.setVisibility(View.VISIBLE);
        btn_signup.setVisibility(View.GONE);

        ed_phone.setFocusable(false);
        ed_username.setFocusable(false);
        ed_password.setFocusable(false);


    }

    private void hide_progressbar_btn() {
        loading.setVisibility(View.GONE);
        btn_signup.setVisibility(View.VISIBLE);

        ed_phone.setFocusableInTouchMode(true);
        ed_password.setFocusableInTouchMode(true);
        ed_username.setFocusableInTouchMode(true);
    }

    private void sginUp() {

        show_progressbar_btn();

        ApiInterface apiinterface = ApiClient.getInstance().getApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("password", ed_password.getText().toString().trim());
        jsonObject.addProperty("mobile", ed_phone.getText().toString().trim());
        jsonObject.addProperty("username", ed_username.getText().toString().trim());

        apiinterface.sendPost("signup.php", jsonObject).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {


                if (response.isSuccessful()) {

                    switch (response.body().getIduser()) {

                        case -1:

                            CustomToast.showToast("شماره تلفن ثبت شده است لطفا وارد شوید", getContext());

                            hide_progressbar_btn();

                            break;

                        default:

                           hide_progressbar_btn();

                            Bundle bundle = new Bundle();
                            bundle.putString("username", ed_username.getText().toString().trim());
                            bundle.putInt("iduser", response.body().getIduser());
                            bundle.putString("phone", ed_phone.getText().toString().trim());
                            Navigation.findNavController(btn_signup).navigate(R.id.action_signupFragment_to_verfiFragment, bundle);


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

    private void intViews() {

        loading = view.findViewById(R.id.loading);
        ed_phone = view.findViewById(R.id.phone_ed);
        ed_username = view.findViewById(R.id.username_ed);
        ed_password = view.findViewById(R.id.password_ed);
        btn_signup = view.findViewById(R.id.btn_signup);
        imageView_sign = view.findViewById(R.id.signup_pic);
        scrollView = view.findViewById(R.id.scroll);

    }

    private void set_icon_inputEditText() {

        ed_phone.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), R.drawable.ic_phone), null);
        ed_password.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), R.drawable.ic_key), null);
        ed_username.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), R.drawable.ic_user), null);

        ed_username.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {

                scroll_to_down();

                ed_username.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), R.drawable.ic_user_red), null);

            } else {
                ed_username.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(getContext(), R.drawable.ic_user), null);

            }
        });

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

        ed_password.setOnClickListener(view -> {
            scroll_to_down();
        });

        ed_phone.setOnClickListener(view -> {
            scroll_to_down();
        });


        ed_username.setOnClickListener(view -> {
            scroll_to_down();
        });


    }
}