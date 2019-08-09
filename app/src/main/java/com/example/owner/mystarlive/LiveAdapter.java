package com.example.owner.mystarlive;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.LiveHolder>{
    private LayoutInflater inflater;
    private ArrayList<Live> dataModelArrayList;
    Context ctx;
    public LiveAdapter(Context ctx, ArrayList<Live> dataModelArrayList){
        inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.dataModelArrayList = dataModelArrayList;
    }

    @NonNull
    @Override
    public LiveAdapter.LiveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.live_horizon, parent, false);
        LiveHolder holder = new LiveHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LiveHolder holder, int position) {
     //   Picasso.get().load(dataModelArrayList.get(position).getImgURL()).into(holder.iv);
        holder.liveid.setText(dataModelArrayList.get(position).getliveid());
        holder.livetitle.setText(dataModelArrayList.get(position).getlivetitle());

    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public class LiveHolder extends RecyclerView.ViewHolder {
        TextView liveid, livetitle, livetime;
        ImageView iv;
        public LiveHolder(View itemView) {
            super(itemView);
            liveid = (TextView) itemView.findViewById(R.id.liveid);
            livetitle = (TextView) itemView.findViewById(R.id.livetitle);
          //  livetime = (TextView) itemView.findViewById(R.id.city);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
}