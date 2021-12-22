package com.example.myapp_sherkat.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.developers.coolprogressviews.SimpleArcProgress;
import com.example.myapp_sherkat.R;
import com.example.myapp_sherkat.classs.ApiClient;
import com.example.myapp_sherkat.classs.CustomToast;
import com.example.myapp_sherkat.classs.Rest;
import com.example.myapp_sherkat.classs.SharedPrefManager;
import com.example.myapp_sherkat.interfaces.ApiInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import java.util.Date;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestActivity extends AppCompatActivity {

    private Date date;
    private TextView id_date;
    private SharedPrefManager sharedPrefManager;
    private TextInputEditText ed_rest;
    private SimpleArcProgress loading;
    private AppCompatButton btn_rest;
    private TextView txt_date_picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_layout);

        set_views();

        set_back_btn();

    }

    private void set_views() {

        loading = findViewById(R.id.loading);
        sharedPrefManager = new SharedPrefManager(this);
        btn_rest = findViewById(R.id.btn_rest);
        ed_rest = findViewById(R.id.ed_rest);
        id_date = findViewById(R.id.id_date);
        TextView title_toolbar = findViewById(R.id.title_toolbar);
        title_toolbar.setText("درخواست مرخصی");

        txt_date_picker = findViewById(R.id.txt_date);
        txt_date_picker.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_date), null);
        txt_date_picker.setOnClickListener(view -> showCalendar());

        btn_rest.setOnClickListener(view -> {


            if (ed_rest.getText().toString().trim().length() >= 20) {


                if (!(id_date.getText().toString().trim().equals(""))) {

                    set_rest();

                } else {
                    CustomToast.showToast("زمان مرخصی را تعیین کنید", this);
                }

            } else {
                CustomToast.showToast("توضیحات حداقل 20 کاراکتر باشد", this);
            }


        });
    }


    private void set_rest() {

        ApiInterface apiinterface = ApiClient.getInstance().getApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("iduser", sharedPrefManager.getUserId());
        jsonObject.addProperty("dec", ed_rest.getText().toString().trim());
        jsonObject.addProperty("date_rest", date.toString());

        show_progressbar_btn();

        apiinterface.sendRest(jsonObject).enqueue(new Callback<Rest>() {
            @Override
            public void onResponse(Call<Rest> call, Response<Rest> response) {

                if (response.isSuccessful()) {
                    if (response.body().getResultRest().equals("no exist")) {

                        dialog();

                    }else if (response.body().getResultRest().equals("two request")) {

                        CustomToast.showToast("حداکثر دو بار می توان در ماه مرخصی گرفت", RestActivity.this);


                    } else if (response.body().getResultRest().equals("insert")) {

                        CustomToast.showToast("ثبت شد", RestActivity.this);
                    }
                    hide_progressbar_btn();
                }

            }

            @Override
            public void onFailure(Call<Rest> call, Throwable t) {
                hide_progressbar_btn();
                CustomToast.showToast("اینترنت خود را بررسی کنید", RestActivity.this);

            }
        });

    }

    private void show_progressbar_btn() {

        loading.setVisibility(View.VISIBLE);
        btn_rest.setVisibility(View.GONE);

        txt_date_picker.setClickable(false);
        ed_rest.setFocusable(false);

    }

    private void hide_progressbar_btn() {

        loading.setVisibility(View.GONE);
        btn_rest.setVisibility(View.VISIBLE);

        txt_date_picker.setClickable(true);
        ed_rest.setFocusableInTouchMode(true);

    }

    private void set_back_btn() {

        ImageView back_icon = findViewById(R.id.back_icon);
        back_icon.setOnClickListener(view -> {
            finish();
        });

    }

    public void showCalendar() {

        PersianDatePickerDialog picker = new PersianDatePickerDialog(this)
                .setPositiveButtonString("تایید")
                .setNegativeButton("انصراف")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(1399)
                .setMaxYear(1600)
                .setActionTextColor(Color.GRAY)
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setShowInBottomSheet(true)
                .setListener(new Listener() {
                    @Override
                    public void onDateSelected(PersianCalendar persianCalendar) {

                        id_date.setText(" زمان انتخاب شده: "+persianCalendar.getPersianLongDate());
                        date =persianCalendar.getTime();
                    }

                    @Override
                    public void onDismissed() {

                    }
                });

        picker.show();

    }

    private void dialog() {

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
}





/*
    private void includeLayout(int layout) {

        LinearLayout parentLayout = findViewById(R.id.parentLayout);
        View childLayout = ((LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(layout, null);
        parentLayout.addView(childLayout);

    }
 */