package com.projectmate.projectmate.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projectmate.projectmate.Classes.Message;
import com.projectmate.projectmate.Database.StaticValues;
import com.projectmate.projectmate.R;

import org.w3c.dom.Text;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    //list of message of type Message
    private List<Message> mMessages;

    //constructor of messageAdapter
    public MessageAdapter(List<Message> mMessages) {
        this.mMessages = mMessages;
    }

    //inflating the rv_item_message layout
    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageAdapter.MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_message, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {

        int my_id = StaticValues.getCurrentUser().getId();

        //getting the position of user
        Message currMessage = mMessages.get(position);

        /*checling if senders message or receiver message by getting its id
            and comparing with my_id
         */

        if(currMessage.getSender().getId()==my_id){
            holder.myMessage.setVisibility(View.VISIBLE);
            holder.theirMessage.setVisibility(View.GONE);

            holder.myMessage.setText(currMessage.getMessage());
        }else{
            holder.theirMessage.setVisibility(View.VISIBLE);
            holder.myMessage.setVisibility(View.GONE);

            holder.theirMessage.setText(currMessage.getMessage());
        }
    }

    //returns the size of message
    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    //Message view holder to display the message and its recyclerView
    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView theirMessage;
        TextView myMessage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            //sets senders and recivers message in their layout
            theirMessage = itemView.findViewById(R.id.their_message_body);
            myMessage = itemView.findViewById(R.id.my_message_body);
        }
    }
}
