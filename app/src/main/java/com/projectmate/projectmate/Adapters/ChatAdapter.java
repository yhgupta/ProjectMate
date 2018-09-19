package com.projectmate.projectmate.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projectmate.projectmate.Classes.Chat;
import com.projectmate.projectmate.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Chat> mChat;
    private Context mContext;

    private RecyclerViewClickListener mListener;

    public ChatAdapter(List<Chat> chat, Context context, RecyclerViewClickListener listener) {
        this.mChat = chat;
        this.mContext = context;
        this.mListener = listener;
    }


    public ChatAdapter(ArrayList<Chat> chat) {
        this.mChat = chat;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_chat, parent, false);

        return new ChatAdapter.ChatViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        Chat currentChat = mChat.get(position);
        holder.tvName.setText(currentChat.getName());
        holder.tvLastMessage.setText(currentChat.getLastMessage());

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout rootView;
        TextView tvName;
        TextView tvLastMessage;
        private RecyclerViewClickListener mListener;

        public ChatViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            rootView = itemView.findViewById(R.id.chat_item_layout);
            tvName = itemView.findViewById(R.id.tv_name_of_person_to_chat);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message_to_be_displayed);
            mListener = listener;
            rootView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }

}

