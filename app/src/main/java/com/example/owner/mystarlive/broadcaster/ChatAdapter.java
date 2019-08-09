package com.example.owner.mystarlive.broadcaster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.mystarlive.ChatClient;
import com.example.owner.mystarlive.R;

import java.util.ArrayList;
/*
* 스트리밍 채팅에 대한 어댑터
* */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder>{

    private ArrayList <ChatClient> chatlist;


    public ChatAdapter(Context Context, ArrayList<ChatClient> chatlist) {
        super();

        this.chatlist = chatlist;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatitem, parent, false);
        ChatAdapter.ChatHolder commentHolder = new ChatAdapter.ChatHolder(view);
        return commentHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatHolder holder, int position) {
        ChatClient chatclient = chatlist.get(position);
        TextView Chatid = holder.Chatid;
        TextView Chatcontent = holder.Chatcontent;

    }

    @Override
    public int getItemCount() {
        return chatlist.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {

        TextView Chatid, Chatcontent;

        public ChatHolder(View itemView) {
            super(itemView);
            Chatid = (TextView)itemView.findViewById(R.id.name);
            Chatcontent = (TextView)itemView.findViewById(R.id.text);

        }
    }
}
