package com.example.owner.mystarlive;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder>{
    @NonNull
    @Override
    public ChatAdapter.ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        public ChatHolder(View itemView) {
            super(itemView);
        }
    }
}
