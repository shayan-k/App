package com.example.myapp_sherkat.adapter;

import android.content.Context;
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
import com.example.myapp_sherkat.classs.Rest;
import com.example.myapp_sherkat.interfaces.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;


public class RestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Rest> dataList;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    public RestAdapter(Context context) {
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
                View v1 = inflater.inflate(R.layout.item_list_rest, parent, false);
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

        Rest result = dataList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:

                postVH postVH = (postVH) holder;
                postVH.date_rest.setText(result.getDate_rest().replace("-","/"));
                postVH.date_created.setText(result.getDate_created().replace("-", "/"));
                postVH.dec.setText(result.getDescription());

                if (result.getRest_status() == 0) {
                    postVH.rest_status.setText("در حال بررسی");
                } else if (result.getRest_status() == 1) {
                    postVH.rest_status.setText("تایید شده");
                }else{
                    postVH.rest_status.setText("رد شده");
                }


                if (result.isState()) {

                    postVH.dec.setVisibility(View.VISIBLE);
                    postVH.loadmore.setRotation(180);

                } else {
                    postVH.dec.setVisibility(View.GONE);
                    postVH. loadmore.setRotation(0);

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


    public void add(Rest r) {
        dataList.add(r);
        notifyItemInserted(dataList.size() - 1);
    }

    public void addAll(List<Rest> Results) {
        for (Rest result : Results) {
            add(result);
        }
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Rest());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = dataList.size() - 1;
        Rest result = getItem(position);

        if (result != null) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Rest getItem(int position) {
        return dataList.get(position);
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;

        notifyItemChanged(dataList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


    protected class postVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView date_rest, rest_status, date_created,dec;
        private ImageView loadmore;

        public postVH(View itemView) {
            super(itemView);

            date_rest = itemView.findViewById(R.id.date_rest);
            rest_status = itemView.findViewById(R.id.rest_status);
            date_created = itemView.findViewById(R.id.date_created);
            dec = itemView.findViewById(R.id.dec);
            loadmore = itemView.findViewById(R.id.loadmore);
            loadmore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (dec.getVisibility() == View.VISIBLE) {

               dec.setVisibility(View.GONE);
                loadmore.setRotation(0);

                dataList.get(getAdapterPosition()).setState(false);


            } else {

                dec.setVisibility(View.VISIBLE);
                loadmore.setRotation(180);
                dataList.get(getAdapterPosition()).setState(true);

            }
            dec.setAnimation(AnimationUtils.loadAnimation(context, R.anim.anim3));

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



