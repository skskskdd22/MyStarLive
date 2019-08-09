package com.example.owner.mystarlive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/*Vod에 대한 리사이클러뷰 어댑터*/

public class VodAdapter extends RecyclerView.Adapter<VodAdapter.VodHolder> {
    private LayoutInflater inflater;
    private ArrayList<Vod> VodArrayList;
    Context context;
    String ImageURL = "http://49.247.206.36/img/";
    String VodURL = "http://49.247.206.36/HLS/";

    public VodAdapter(Context context, ArrayList<Vod> VodArrayList){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.VodArrayList = VodArrayList;
    }

    @NonNull
    @Override
    public VodAdapter.VodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout. vodlist, parent, false);
        VodHolder holder = new VodHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VodAdapter.VodHolder holder, int position) {
        ImageView iv = holder.iv;

        Vod object = VodArrayList.get(position);


       Glide.with(context).load(ImageURL + object.getImgURL() + ".png").into(holder.iv);
        holder.vodid.setText(VodArrayList.get(position).getvodid());
        holder.vodtitle.setText(VodArrayList.get(position).getvodtitle());

        holder.iv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

               String vodurl = VodURL + object.getImgURL() + ".m3u8";
                Intent intent = new Intent(context, VodPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Vodurl", vodurl);
                intent.putExtras(bundle);
                Log.e("보내기 전 Vodurl1 : ",vodurl);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return VodArrayList.size();
    }

    public class VodHolder extends RecyclerView.ViewHolder {
        TextView vodid, vodtitle;
        ImageView iv;
        public VodHolder(View itemView) {
            super(itemView);
            vodid = (TextView) itemView.findViewById(R.id.vodid);
            vodtitle = (TextView) itemView.findViewById(R.id.vodtitle);
            iv = (ImageView) itemView.findViewById(R.id.vodiv);
        }
    }
}
