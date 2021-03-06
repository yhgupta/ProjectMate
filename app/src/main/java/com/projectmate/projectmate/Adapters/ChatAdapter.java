package com.projectmate.projectmate.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.projectmate.projectmate.Classes.Message;
import com.projectmate.projectmate.R;

import java.util.List;

/**
 * This class helps to fill in recycler view with chat items
 */


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    //list of messgae of type Message
    private List<Message> mMessages;
    private int mUserId;
    private RecyclerViewClickListener mListener;

    //Constructor ( list of message , recyclerView , userId )
    public ChatAdapter(List<Message> messages, RecyclerViewClickListener listener, int userId) {
        this.mMessages = messages;
        this.mListener = listener;
        this.mUserId = userId;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_chat, parent, false);
            vh = new MessageViewHolder(v, mListener);

        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            vh = new ProgressBarViewHolder(v);

        }

        return vh;
    }

    //Find the type according to which layout to be displayed
    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position) == null ? VIEW_PROG : VIEW_ITEM;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //takes the position of chat
        if (holder instanceof MessageViewHolder) {
            Message currentMessage = mMessages.get(position);

            String name = currentMessage.getSender().getId() == mUserId ?
                    currentMessage.getReceiver().getUsername() :
                    currentMessage.getSender().getUsername();

            //takes the name of sender and last message of chat
            ((MessageViewHolder) holder).senderName.setText(name);
            ((MessageViewHolder) holder).lastMessage.setText(currentMessage.getMessage());
        } else {
            ((ProgressBarViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    //returns the mMessage size
    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    //Message View holder
    static class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Initialization
        LinearLayout rootView;
        TextView senderName;
        TextView lastMessage;

        private RecyclerViewClickListener mListener;

        MessageViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            //assignment
            rootView = itemView.findViewById(R.id.chat_item_layout);
            mListener = listener;
            rootView.setOnClickListener(this);

            //displaying
            senderName = itemView.findViewById(R.id.tv_name_of_person_to_chat);
            lastMessage = itemView.findViewById(R.id.tv_last_message_to_be_displayed);

        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }

    static class ProgressBarViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public ProgressBarViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }
}
