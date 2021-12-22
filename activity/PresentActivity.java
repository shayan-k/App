package com.example.myapp_sherkat.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.developers.coolprogressviews.SimpleArcProgress;
import com.example.myapp_sherkat.R;
import com.example.myapp_sherkat.classs.ApiClient;
import com.example.myapp_sherkat.classs.CustomToast;
import com.example.myapp_sherkat.classs.Present;
import com.example.myapp_sherkat.classs.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;


public class PresentActivity extends AppCompatActivity {

    private AppCompatButton btn_start;
    private AppCompatButton btn_end;
    private SharedPrefManager sharedPrefManager;
    private TextInputEditText ed_decc;
    private AlertDialog alertDialog;
    private PersianDate pdate;
    private ImageView img;
    private TextView txt;
    private SimpleArcProgress loading;
    private SimpleArcProgress loading_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_start_working);

        intViews();

        check_tme();

    }


    private void show_date() {

        LinearLayout ly_btns = findViewById(R.id.ly_btns);
        TextView txt_date = findViewById(R.id.date_txt);
        txt_date.setVisibility(View.VISIBLE);
        ly_btns.setVisibility(View.VISIBLE);
        txt_date.setText(pdate.dayName() + "، " + pdate.getShDay() + " " + pdate.monthName() + " " + pdate.getShYear());

        ImageView image = findViewById(R.id.img_present);
        Glide.with(this).load(R.drawable.ic_present2).into(image);
        image.setVisibility(View.VISIBLE);
    }

    private void check_tme() {


        int hour = pdate.getHour();

        if (hour >= 8 && hour <= 21) {

            if(!pdate.dayName().equals("جمعه")){

                check_disable_btn();
                btns();

            }else {

                Glide.with(this).load(R.drawable.ic_empty).into(img);
                txt.setText("امروز جمعه هست!");
            }


        } else {

            Glide.with(this).load(R.drawable.ic_empty).into(img);
            txt.setText("در این ساعت این امکان فعال نیست!");

        }


    }

    private void btns() {

        btn_start.setOnClickListener(view -> {
            set_start_time();
        });


        btn_end.setOnClickListener(view -> {

            dialog();


        });
    }

    private void set_start_time() {

        loading_btn.setVisibility(View.VISIBLE);
        btn_start.setVisibility(View.GONE);
        btn_end.setVisibility(View.GONE);

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("iduser", sharedPrefManager.getUserId());


        ApiClient.getInstance().getApi().sendPresent("set_start_time.php", jsonObject).enqueue(new Callback<Present>() {
            @Override
            public void onResponse(Call<Present> call, Response<Present> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatePresent().equals("no exist")) {

                        loading_btn.setVisibility(View.GONE);

                        showDialogExit();

                    } else if (response.body().getStatePresent().equals("ok")) {

                        CustomToast.showToast("زمان شروع ثبت شد", PresentActivity.this);

                        btn_start.setClickable(false);
                        btn_start.setAlpha(.4f);

                        btn_end.setClickable(true);
                        btn_end.setAlpha(1f);

                        loading_btn.setVisibility(View.GONE);
                        btn_start.setVisibility(View.VISIBLE);
                        btn_end.setVisibility(View.VISIBLE);

                        TextView txt_info=findViewById(R.id.info_txt);
                        txt_info.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Present> call, Throwable t) {


                loading_btn.setVisibility(View.GONE);
                btn_start.setVisibility(View.VISIBLE);
                btn_end.setVisibility(View.VISIBLE);

                CustomToast.showToast("اینترنت خود را بررسی کنید", PresentActivity.this);

            }
        });

    }

    private void dialog() {
        LayoutInflater Inflater = LayoutInflater.from(this);
        View view1 = Inflater.inflate(R.layout.layout_dialog_repoart, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(true);
        builder.setView(view1);
        alertDialog = builder.create();

        TextView btn_sumbit = view1.findViewById(R.id.sumbit);
        ed_decc = view1.findViewById(R.id.decc_ed);

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.background);
        alertDialog.show();

        btn_sumbit.setOnClickListener(view -> {

            if (ed_decc.getText().toString().trim().length() >= 20) {

                alertDialog.dismiss();

                set_end_time();

            } else {
                CustomToast.showToast("گزارش حداقل باید 20 کاراکتر باشد", PresentActivity.this);
            }
        });


    }


    private void set_end_time() {

        loading_btn.setVisibility(View.VISIBLE);
        btn_start.setVisibility(View.GONE);
        btn_end.setVisibility(View.GONE);


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("iduser", sharedPrefManager.getUserId());
        jsonObject.addProperty("decc", ed_decc.getText().toString().trim());

        ApiClient.getInstance().getApi().sendPresent("set_end_time.php", jsonObject).enqueue(new Callback<Present>() {
            @Override
            public void onResponse(Call<Present> call, Response<Present> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatePresent().equals("no exist")) {

                        loading_btn.setVisibility(View.GONE);
                        showDialogExit();

                    }else if (response.body().getStatePresent().equals("ok")) {

                        CustomToast.showToast("ثبت شد", PresentActivity.this);
                        alertDialog.dismiss();

                        btn_end.setAlpha(.4f);
                        btn_end.setClickable(false);

                        loading_btn.setVisibility(View.GONE);
                        btn_start.setVisibility(View.VISIBLE);
                        btn_end.setVisibility(View.VISIBLE);


                    }
                }
            }

            @Override
            public void onFailure(Call<Present> call, Throwable t) {

                loading_btn.setVisibility(View.GONE);
                btn_start.setVisibility(View.VISIBLE);
                btn_end.setVisibility(View.VISIBLE);

                CustomToast.showToast("اینترنت خود را بررسی کنید", PresentActivity.this);

            }
        });

    }

    private void check_disable_btn() {
        loading.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("iduser", sharedPrefManager.getUserId());

        ApiClient.getInstance().getApi().sendPresent("get_state_present.php", jsonObject).enqueue(new Callback<Present>() {
            @Override
            public void onResponse(Call<Present> call, Response<Present> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatePresent().equals("no end time")) {

                        btn_start.setClickable(false);
                        btn_start.setAlpha(.4f);


                    } else if (response.body().getStatePresent().equals("no added")) {

                        btn_end.setClickable(false);
                        btn_end.setAlpha(.4f);

                    } else {

                        btn_end.setClickable(false);
                        btn_end.setAlpha(.4f);

                        btn_start.setClickable(false);
                        btn_start.setAlpha(.4f);

                    }

                    show_date();
                    loading.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<Present> call, Throwable t) {

                Glide.with(PresentActivity.this).load(R.drawable.ic_no_net).into(img);
                txt.setText("اینترنت خود را بررسی کنید!");
                loading.setVisibility(View.GONE);
            }
        });

    }

    private void showDialogExit() {

        LayoutInflater Inflater = LayoutInflater.from(this);
        View view1 = Inflater.inflate(R.layout.layout_dialog_exist_app, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(false);
        builder.setView(view1);
        AlertDialog  alertDialog = builder.create();

        TextView btn_exist = view1.findViewById(R.id.exist);

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.background);
        alertDialog.show();

        btn_exist.setOnClickListener(view -> {

            finishAffinity();
            System.exit(0);

        });

    }
    private void intViews() {
        btn_end = findViewById(R.id.end_btn);
        btn_start = findViewById(R.id.start_btn);
        TextView title_toolbar = findViewById(R.id.title_toolbar);
        title_toolbar.setText("حضور و غیاب");
        ImageView back = findViewById(R.id.back_icon);
        back.setOnClickListener(view -> finish());
        sharedPrefManager = new SharedPrefManager(this);
        img = findViewById(R.id.img);
        txt = findViewById(R.id.txt);
        loading_btn = findViewById(R.id.loading_btn);
        loading = findViewById(R.id.loading);
        pdate = new PersianDate();//date library

    }
}
