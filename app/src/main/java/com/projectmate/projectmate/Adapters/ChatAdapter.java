package com.projectmate.projectmate.Adapters;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.projectmate.projectmate.Classes.Message;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.R;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Message> mMessages;

    private int mUserId;

    private RecyclerViewClickListener mListener;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;



    public ChatAdapter(List<Message> messages, RecyclerView recyclerView, RecyclerViewClickListener listener, OnLoadMoreListener loadMoreListener, int userId) {
        this.mMessages = messages;
        this.mListener = listener;
        this.onLoadMoreListener = loadMoreListener;
        this.mUserId = userId;


        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }

            }
        });
    }


    public void setLoaded(){
        this.loading = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == VIEW_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_chat, parent, false);
            vh = new MessageViewHolder(v, mListener);

        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            vh = new ProgressBarViewHolder(v);

        }

        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position) == null ? VIEW_PROG : VIEW_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof MessageViewHolder){
            Message currentMessage = mMessages.get(position);

            String name = currentMessage.getSender().getId() == mUserId ?
                    currentMessage.getReceiver().getUsername() :
                    currentMessage.getSender().getUsername();

            ((MessageViewHolder) holder).senderName.setText(name);
            ((MessageViewHolder) holder).lastMessage.setText(currentMessage.getMessage());

        }
        else {
            ((ProgressBarViewHolder) holder).progressBar.setIndeterminate(true);
        }


    }


    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout rootView;
        TextView senderName;
        TextView lastMessage;

        private RecyclerViewClickListener mListener;

        MessageViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            rootView = itemView.findViewById(R.id.chat_item_layout);
            mListener = listener;
            rootView.setOnClickListener(this);

            senderName = itemView.findViewById(R.id.tv_name_of_person_to_chat);
            lastMessage = itemView.findViewById(R.id.tv_last_message_to_be_displayed);

        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }

    static class ProgressBarViewHolder extends RecyclerView.ViewHolder{

        ProgressBar progressBar;

        public ProgressBarViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }
}
