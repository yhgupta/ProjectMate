package com.projectmate.projectmate.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projectmate.projectmate.Classes.Chat;
import com.projectmate.projectmate.R;
import com.projectmate.projectmate.TestActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Chat> mChat;


    public ChatAdapter(List<Chat> chat ) {
        this.mChat = chat;
    }


    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_chat, parent, false);
        return new ChatAdapter.ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        holder.tvName.setText(mChat.get(position).getName());
        holder.tvLastMessage.setText(mChat.get(position).getLastMessage());
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvLastMessage;

        public ChatViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name_view);
            tvLastMessage = itemView.findViewById(R.id.last_message_view);

        }
    }

}

