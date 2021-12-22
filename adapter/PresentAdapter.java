package com.example.myapp_sherkat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapp_sherkat.R;
import com.example.myapp_sherkat.classs.Present;
import com.example.myapp_sherkat.interfaces.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class PresentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Present> dataList;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    public PresentAdapter(Context context) {
        this.context = context;
        this.mCallback = (PaginationAdapterCallback) context;
        dataList = new ArrayList<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                View v1 = inflater.inflate(R.layout.item_list_present, parent, false);
                viewHolder = new postVH(v1);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Present result = dataList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:

                postVH postVH = (postVH) holder;
                postVH.start_at.setText(result.getStart_at().substring(0, 5));

                if(result.getEnd_at()==null){

                    postVH.end_at.setText("دکمه پایان زده نشده");

                }else {

                    postVH.end_at.setText(result.getEnd_at().substring(0, 5));

                }

                postVH.present_date.setText(result.getDate_present().replace("-", "/"));

                if(result.getRepoart()==null){

                    postVH.repoart.setText("گزارشی ثبت نشده");

                }else {

                    postVH.repoart.setText(result.getRepoart());

                }


                if (result.isState()) {

                    postVH.repoart.setVisibility(View.VISIBLE);
                    postVH.loadmore.setRotation(180);

                } else {
                    postVH.repoart.setVisibility(View.GONE);
                    postVH.loadmore.setRotation(0);

                }


                break;

            case LOADING:

                LoadingVH loadingVH = (LoadingVH) holder;
                if (retryPageLoad) {

                    loadingVH.mRetryBtn.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                } else {

                    loadingVH.mRetryBtn.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);

                }

                break;
        }

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == dataList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(Present r) {
        dataList.add(r);
        notifyItemInserted(dataList.size() - 1);
    }

    public void addAll(List<Present> Results) {
        for (Present result : Results) {
            add(result);
        }
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Present());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = dataList.size() - 1;
        Present result = getItem(position);

        if (result != null) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Present getItem(int position) {
        return dataList.get(position);
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;

        notifyItemChanged(dataList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


    protected class postVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView start_at, end_at, present_date, repoart;
        private ImageView loadmore;

        public postVH(View itemView) {
            super(itemView);

            start_at = itemView.findViewById(R.id.start_at);
            end_at = itemView.findViewById(R.id.end_at);
            present_date = itemView.findViewById(R.id.present_date);
            repoart = itemView.findViewById(R.id.repoart);
            loadmore = itemView.findViewById(R.id.loadmore);
            loadmore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (repoart.getVisibility() == View.VISIBLE) {

                repoart.setVisibility(View.GONE);
                loadmore.setRotation(0);

                dataList.get(getAdapterPosition()).setState(false);


            } else {

                repoart.setVisibility(View.VISIBLE);
                loadmore.setRotation(180);
                dataList.get(getAdapterPosition()).setState(true);

            }
            repoart.setAnimation(AnimationUtils.loadAnimation(context, R.anim.anim3));

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);

            mRetryBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            showRetry(false, null);
            mCallback.retryPageLoad();

        }
    }
}



