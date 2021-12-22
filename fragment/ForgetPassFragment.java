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
import com.example.myapp_sherkat.classs.SharedPrefManager;
import com.example.myapp_sherkat.classs.User;
import com.example.myapp_sherkat.interfaces.ApiInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ForgetPassFragment extends Fragment {

    private View view;
    private Button next_level_btn;
    private TextInputEditText ed_phone;
    private SimpleArcProgress loading;
    private  ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null) {

            view = inflater.inflate(R.layout.fragment_forget_pass, container, false);

            init_views();

            go_next_level();

            scrollInput();

            set_img();

            return view;

        }else {
            return view;
        }

    }

    private void scrollInput() {

        ed_phone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                scroll_to_down();
            }

        });


        ed_phone.setOnClickListener(view -> scroll_to_down());
    }

    private void set_img() {
        ImageView img = view.findViewById(R.id.img_forget);
        Glide.with(getContext()).load(R.drawable.ic_forget).into(img);
    }

    private void scroll_to_down() {

        new Handler().postDelayed(() -> {

            scrollView.scrollTo(0, scrollView.getBottom());

        }, 2000);

    }


    private void show_progressbar_btn() {

        loading.setVisibility(View.VISIBLE);
        next_level_btn.setVisibility(View.GONE);

        ed_phone.setFocusable(false);

    }

    private void hide_progressbar_btn() {

        loading.setVisibility(View.GONE);
        next_level_btn.setVisibility(View.VISIBLE);

        ed_phone.setFocusableInTouchMode(true);

    }


    private void go_next_level() {
        next_level_btn.setOnClickListener(view -> {

            if (!(ed_phone.getText().toString().trim().equals(""))) {

                if (ed_phone.length() == 11) {


                        if (ed_phone.getText().toString().charAt(0) == '0' && ed_phone.getText().toString().charAt(1) == '9') {

                        forget_pass();

                        } else {

                            CustomToast.showToast("شماره تلفن صحیح نیست", getContext());

                        }

                } else {

                    CustomToast.showToast("شماره تلفن صحیح نیست", getContext());

                }

            } else {

                CustomToast.showToast("شماره تلفن خالی هست", getContext());
            }

    });
    }

    private void forget_pass() {
        ApiInterface apiinterface = ApiClient.getInstance().getApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile",ed_phone.getText().toString().trim());
        show_progressbar_btn();
        apiinterface.sendPost("forget_pass.php", jsonObject).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {


                if (response.isSuccessful()) {

                    switch (response.body().getIduser()) {
                        case -1:

                            CustomToast.showToast("شماره شما ثبت نشده لطفا ثبت نام کنید", getContext());

                            hide_progressbar_btn();

                            break;


                        default:

                            hide_progressbar_btn();

                            Bundle bundle = new Bundle();
                            bundle.putString("username",response.body().getUsername());
                            bundle.putInt("iduser",response.body().getIduser());
                            bundle.putString("phone",  ed_phone.getText().toString().trim());
                            Navigation.findNavController(next_level_btn).navigate(R.id.action_forgetPassFragment_to_verfiFragment,bundle);


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

    private void init_views() {

        next_level_btn = view.findViewById(R.id.btn_next_level);
        ed_phone = view.findViewById(R.id.phone_ed);
        loading = view.findViewById(R.id.loading);
        scrollView=view.findViewById(R.id.scrollView);

    }
}