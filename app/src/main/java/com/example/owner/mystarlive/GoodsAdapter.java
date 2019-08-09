package com.example.owner.mystarlive;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.mystarlive.viewer.Goods;

import java.util.ArrayList;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsHolder>{
    private LayoutInflater inflater;
    private ArrayList<Goods> GoodsArrayList;
    Context context;

    public GoodsAdapter(Context context, ArrayList<Goods> GoodsArrayList){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.GoodsArrayList = GoodsArrayList;
    }

    @NonNull
    @Override
    public GoodsAdapter.GoodsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout. goods_item, parent, false);
        GoodsHolder holder = new GoodsHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsAdapter.GoodsHolder holder, int position) {
        Goods object = GoodsArrayList.get(position);
        Log.e("어댑터 goods : ", object.getImgURL());

        Glide.with(context).load(object.getImgURL()).into(holder.goodsiv);
        holder.goodstitle.setText(GoodsArrayList.get(position).getgoodstitle());
        holder.goodsprice.setText(GoodsArrayList.get(position).getprice());
    }

    @Override
    public int getItemCount() {
        return GoodsArrayList.size();
    }

    public class GoodsHolder extends RecyclerView.ViewHolder {
        TextView goodstitle, goodsprice;
        ImageView goodsiv;
        public GoodsHolder(View itemView) {
            super(itemView);
            goodstitle = (TextView) itemView.findViewById(R.id.goodstitle);
            goodsprice = (TextView) itemView.findViewById(R.id.goodsprice);
            goodsiv = (ImageView) itemView.findViewById(R.id.goodsiv);
        }
    }
}
