package com.example.myapp_sherkat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp_sherkat.R;
import com.example.myapp_sherkat.adapter.PresentAdapter;
import com.example.myapp_sherkat.classs.ApiClient;
import com.example.myapp_sherkat.classs.Present;
import com.example.myapp_sherkat.classs.ResponcePresent;
import com.example.myapp_sherkat.classs.SharedPrefManager;
import com.example.myapp_sherkat.interfaces.ApiInterface;
import com.example.myapp_sherkat.interfaces.PaginationAdapterCallback;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPresentActivity extends AppCompatActivity implements PaginationAdapterCallback {

    private PresentAdapter adapter;
    private RecyclerView rv;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private Button btnRetry;
    private TextView txtError;

    private int PAGE_START = 0;
    private boolean isLoading = true;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    private ApiInterface anInterface;

    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        intViews();

        setRecyclerView();

        loadFirstPage();

        setRetry();

    }

    private void setRetry() {
        btnRetry.setOnClickListener(view -> {
            hideErrorView();
            loadFirstPage();
        });
    }

    private void setRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {

                        if (!isLastPage) {
                            if (isLoading) {

                                isLoading = false;
                                loadNextPage();

                            }

                        }
                    }
                }
            }
        });

    }

    private void intViews() {
        sharedPrefManager = new SharedPrefManager(this);

        ImageView img_back = findViewById(R.id.back_icon);
        img_back.setOnClickListener(view -> finish());
        TextView title_toolbar = findViewById(R.id.title_toolbar);
        title_toolbar.setText("حضوری های من");

        rv = findViewById(R.id.main_recycler);
        progressBar = findViewById(R.id.main_progress);
        errorLayout = findViewById(R.id.error_layout);
        btnRetry = findViewById(R.id.error_btn_retry);
        txtError = findViewById(R.id.error_txt_cause);

        adapter = new PresentAdapter(this);

        anInterface = ApiClient.getInstance().getApi();

    }


    private void loadFirstPage() {
        getData().enqueue(new Callback<ResponcePresent>() {
            @Override
            public void onResponse(Call<ResponcePresent> call, Response<ResponcePresent> response) {

                if (response.isSuccessful()) {

                    if (response.body().getPresentList().isEmpty()) {

                        ImageView img_empety = findViewById(R.id.img_empety);
                        TextView txt_empety = findViewById(R.id.txt_empety);
                        RelativeLayout ly = findViewById(R.id.ly);
                        img_empety.setVisibility(View.VISIBLE);
                        txt_empety.setVisibility(View.VISIBLE);
                        ly.setBackgroundColor(getResources().getColor(R.color.white3));

                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    currentPage += 1;
                    List<Present> results = response.body().getPresentList();
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);

                    TOTAL_PAGES = response.body().getPage();


                    if (currentPage <= TOTAL_PAGES) {
                        adapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponcePresent> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);

            }
        });

    }


    private void loadNextPage() {

        getData().enqueue(new Callback<ResponcePresent>() {
            @Override
            public void onResponse(Call<ResponcePresent> call, Response<ResponcePresent> response) {

                if (response.isSuccessful()) {

                    adapter.removeLoadingFooter();
                    currentPage += 1;
                    TOTAL_PAGES = response.body().getPage();
                    List<Present> results = response.body().getPresentList();
                    adapter.addAll(results);

                    isLoading = true;

                    if (currentPage <= TOTAL_PAGES) {
                        adapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponcePresent> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));

            }
        });
    }


    private Call<ResponcePresent> getData() {
        return anInterface.getPresent(
                sharedPrefManager.getUserId(),
                currentPage
        );
    }


    @Override
    public void retryPageLoad() {
        loadNextPage();
    }


    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }


    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg;

        if (throwable instanceof TimeoutException) {

            errorMsg = getResources().getString(R.string.error_msg_timeout);

        } else  {

            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        }

        return errorMsg;
    }


    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }



}