package com.example.myapp_sherkat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developers.coolprogressviews.SimpleArcProgress;
import com.example.myapp_sherkat.R;
import com.example.myapp_sherkat.classs.ApiClient;
import com.example.myapp_sherkat.classs.CustomToast;
import com.example.myapp_sherkat.classs.SharedPrefManager;
import com.example.myapp_sherkat.classs.User;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class SplashActivity extends AppCompatActivity {
    private TextView txt_info;
    private SharedPrefManager sharedPrefManager;
    private JsonObject jsonObject;
    private AppCompatImageView refreshIcon;
    private SimpleArcProgress loading;
    private String date;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        set_animations();

        init();

        checkExistUser();

    }

    private void init() {

        loading = findViewById(R.id.loading);
        txt_info = findViewById(R.id.txt_info);
        sharedPrefManager = new SharedPrefManager(this);
        refreshIcon = findViewById(R.id.refreshIcon);
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH", Locale.US).format(Calendar.getInstance().getTime());
        date = timeStamp.substring(0, timeStamp.lastIndexOf("-"));
        time = timeStamp.substring(timeStamp.lastIndexOf("-") + 1);

        refreshIcon.setOnClickListener(view -> {
            refreshIcon.setVisibility(View.GONE);
            checkExistUser();
        });

    }

    private void checkExistUser() {
        loading.setVisibility(View.VISIBLE);

        if (jsonObject == null)
            jsonObject = new JsonObject();
        jsonObject.addProperty("iduser", sharedPrefManager.getUserId());

        ApiClient.getInstance().getApi().sendPost("checkUser.php", jsonObject).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    String dateResponse = response.body().getDate();

                    if (dateResponse.substring(0, dateResponse.lastIndexOf(" ")).trim().equals(date)
                            && dateResponse.substring(dateResponse.indexOf(" "), dateResponse.indexOf(":")).trim().equals(time)) {

                        if (sharedPrefManager.getUserPhone().isEmpty()) {

                            go_to_signPage();

                        } else {

                            if (response.body().getResult_verify().equals("exist")) {

                                go_to_mainPage();

                            } else {
                                txt_info.setVisibility(View.VISIBLE);
                                txt_info.setText("شما دیگه عضوی از این مجموعه نیستید!");
                                loading.setVisibility(View.GONE);
                            }
                        }

                    } else {

                        txt_info.setText("تاریخ و ساعت گوشی خود را تنظیم کنید و مجددا وارد برنامه شوید!");
                        loading.setVisibility(View.GONE);

                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loading.setVisibility(View.GONE);
                txt_info.setVisibility(View.VISIBLE);
                txt_info.setText("اینترنت خود را بررسی کنید!");
                refreshIcon.setVisibility(View.VISIBLE);
            }
        });
    }

    private void go_to_signPage() {

        new Handler().postDelayed(() -> {

            startActivity(new Intent(SplashActivity.this, SignLoginActivity.class));
            finish();

        }, 2600);

    }

    private void go_to_mainPage() {

        new Handler().postDelayed(() -> {

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();

        }, 2600);

    }

    private void set_animations() {

        ImageView img_splash = findViewById(R.id.img_splash);
        TextView txt_splash = findViewById(R.id.txt_splash);
        Glide.with(this).load(R.drawable.splash).into(img_splash);
        img_splash.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim2));
        txt_splash.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim));

    }


}