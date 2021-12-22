package com.example.myapp_sherkat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapp_sherkat.R;
import com.example.myapp_sherkat.activity.MyPresentActivity;
import com.example.myapp_sherkat.activity.MyRestActivity;
import com.example.myapp_sherkat.activity.PresentActivity;
import com.example.myapp_sherkat.activity.RestActivity;
import com.example.myapp_sherkat.classs.DataParts;


import java.util.List;

public class MainPartsAdapter extends RecyclerView.Adapter<MainPartsAdapter.NewsViewHolder> {
    private Context context;
    private List<DataParts> parts;

    public MainPartsAdapter(Context context, List<DataParts> parts) {
        this.context = context;
        this.parts = parts;
    }


    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_parts, viewGroup, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        DataParts part = parts.get(position);
        Glide.with(context).load(part.getImgId()).into(holder.img);
        holder.txt.setText(part.getName());

        holder.itemView.setOnClickListener(view -> {

            switch (position){
                case 0:

                    context.startActivity( new Intent(context, PresentActivity.class));


                    break;

                case 1:
                    context.startActivity( new Intent(context, RestActivity.class));

                    break;
                case 2:

                    context.startActivity( new Intent(context, MyPresentActivity.class));

                    break;
                case 3:

                    context.startActivity( new Intent(context, MyRestActivity.class));


                    break;

               //case 4:

               //    context.startActivity( new Intent(context, AboutUsActivity.class));


               //    break;

            }

        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView txt;

        public NewsViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt_rc_parts);
            img = itemView.findViewById(R.id.img);


        }

    }


}

