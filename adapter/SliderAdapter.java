package com.example.myapp_sherkat.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.Target;
import com.developers.coolprogressviews.SimpleArcProgress;
import com.example.myapp_sherkat.R;
import com.example.myapp_sherkat.classs.DataParts;
import com.example.myapp_sherkat.classs.Slider;
import com.makeramen.roundedimageview.RoundedImageView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<Slider> Items;


    public SliderAdapter(Context context, List<Slider> Items) {
        this.context = context;
        this.Items = Items;
    }


    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH holder, int position) {

            Glide.with(context).load(Items.get(position).getImage()).placeholder(R.color.placeholder_grey_20).into(holder.image);

    }

    @Override
    public int getCount() {
        return Items.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        private RoundedImageView image;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.slider_img);

        }
    }

}