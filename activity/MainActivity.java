package com.example.myapp_sherkat.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.developers.coolprogressviews.SimpleArcProgress;
import com.example.myapp_sherkat.R;
import com.example.myapp_sherkat.adapter.MainPartsAdapter;
import com.example.myapp_sherkat.adapter.SliderAdapter;
import com.example.myapp_sherkat.classs.ApiClient;
import com.example.myapp_sherkat.classs.CustomToast;
import com.example.myapp_sherkat.classs.DataParts;
import com.example.myapp_sherkat.classs.SharedPrefManager;
import com.example.myapp_sherkat.classs.Slider;
import com.example.myapp_sherkat.fragment.BottomSheetFragment;
import com.example.myapp_sherkat.interfaces.ApiInterface;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private SimpleArcProgress loading;
    private AppCompatImageView refreshIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSlider();

        setSlider();

        setRecyclerView();

        go_to_user_page();

    }


    private void go_to_user_page() {

        ImageView userIcon = findViewById(R.id.user_icon);
        BottomSheetFragment bottomFragment = new BottomSheetFragment();
        userIcon.setOnClickListener(view -> {

            bottomFragment.show(getSupportFragmentManager(), null);

        });

    }


    private void setSlider() {
        loading.setVisibility(View.VISIBLE);
        ApiClient.getInstance().getApi().getSlider().enqueue(new Callback<List<Slider>>() {
            @Override
            public void onResponse(Call<List<Slider>> call, Response<List<Slider>> response) {
                if (response.isSuccessful()) {

                    SliderView sliderView = findViewById(R.id.slider);
                    SliderAdapter adapterslider = new SliderAdapter(MainActivity.this, response.body());
                    sliderView.setSliderAdapter(adapterslider);
                    sliderView.setIndicatorAnimation(IndicatorAnimations.SWAP);
                    sliderView.setSliderTransformAnimation(SliderAnimations.GATETRANSFORMATION);
                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                    sliderView.setScrollTimeInSec(4);
                    sliderView.startAutoCycle();

                    loading.setVisibility(View.GONE);
                    if (refreshIcon.getVisibility() == View.VISIBLE)
                        refreshIcon.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<List<Slider>> call, Throwable t) {

                loading.setVisibility(View.GONE);
                refreshIcon.setVisibility(View.VISIBLE);
                CustomToast.showToast("اینترنت خود را بررسی کنید!", MainActivity.this);
            }
        });


    }

    private void initSlider() {
        loading = findViewById(R.id.loading);
        refreshIcon = findViewById(R.id.refreshIcon);
        refreshIcon.setOnClickListener(view -> {
            refreshIcon.setVisibility(View.GONE);
            setSlider();
        });
    }


    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rc_parts);
        MainPartsAdapter adapter = new MainPartsAdapter(this, getData_parts());

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 4 ? 2 : 1;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public List<DataParts> getData_parts() {

        List<DataParts> data = new ArrayList<>();


        data.add(new DataParts("حضور و غیاب", R.drawable.ic_startworking));
        data.add(new DataParts("درخواست مرخصی", R.drawable.ic_rest));

        data.add(new DataParts("حضوری های من", R.drawable.ic_present));
        data.add(new DataParts("مرخصی های من", R.drawable.ic_my_rest));




        return data;
    }
}