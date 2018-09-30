package com.projectmate.projectmate.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projectmate.projectmate.AlibabaCloud.ProjectMateAPIContract;
import com.projectmate.projectmate.Classes.Activity;
import com.projectmate.projectmate.Database.StaticValues;
import com.projectmate.projectmate.R;

import java.util.List;

/**
 * This class helps to fill in recycler view with activity items
 */

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivitiesViewHolder> {

    private List<Activity> mActivities;

    private RecyclerViewClickListener mListener;


    public ActivitiesAdapter(List<Activity> activity, RecyclerViewClickListener listener) {
        this.mActivities = activity;
        this.mListener = listener;
    }

    //Inflating view of rv_item_notification
    @NonNull
    @Override
    public ActivitiesAdapter.ActivitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_notification, parent, false);
        return new ActivitiesAdapter.ActivitiesViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesViewHolder holder, int position) {
        //getting position which is touched
        Activity currActivity = mActivities.get(position);

        int activityType = currActivity.getActivity_type();
        boolean isSenderMe = currActivity.getSender().getId() == StaticValues.getCurrentUser().getId();

        //setting visibility
        holder.rootViewAR.setVisibility(View.GONE);
        holder.rootViewAR.setTag(R.id.activity_main_layout, false);
        holder.rootViewSC.setVisibility(View.GONE);
        holder.rootViewSC.setTag(R.id.activity_main_layout, false);
        holder.rootViewN.setVisibility(View.GONE);
        holder.rootViewN.setTag(R.id.activity_main_layout, false);


        switch (activityType) {

            //activity  for request to join project
            case ProjectMateAPIContract.ACTIVITY_TYPE_REQUEST_JOIN:
                if (isSenderMe) {
                    holder.rootViewN.setVisibility(View.VISIBLE);

                    holder.permTextN.setText("You have sent join request for ");
                    holder.projNameN.setText(currActivity.getProject().getProject_name());
                } else {
                    holder.rootViewAR.setVisibility(View.VISIBLE);
                    holder.rootViewAR.setTag(R.id.activity_main_layout, true);

                    holder.permTextAR.setText("You have a join request from " + currActivity.getSender().getUsername() + " for ");
                    holder.projNameAR.setText(currActivity.getProject().getProject_name());
                }
                break;

            //activity to invite for project
            case ProjectMateAPIContract.ACTIVITY_TYPE_REQUEST_INVITE:
                if (isSenderMe) {
                    holder.rootViewN.setVisibility(View.VISIBLE);
                    holder.rootViewN.setTag(R.id.activity_main_layout, true);

                    holder.permTextN.setText("You have sent invitation to " + currActivity.getReceiver().getUsername() + " request for ");
                    holder.projNameN.setText(currActivity.getProject().getProject_name());
                } else {
                    holder.rootViewAR.setVisibility(View.VISIBLE);

                    holder.permTextAR.setText("You have a invitation from " + currActivity.getReceiver().getUsername() + " for ");
                    holder.projNameAR.setText(currActivity.getProject().getProject_name());
                }
                break;

            //activity to display that join request has accepted
            case ProjectMateAPIContract.ACTIVITY_TYPE_REQUEST_JOIN_ACCEPTED:
                holder.rootViewSC.setVisibility(View.VISIBLE);
                holder.projNameSC.setText(currActivity.getProject().getProject_name());
                if (isSenderMe) {
                    holder.permTextSC.setText("Your join request is accepted for ");
                } else {
                    holder.permTextSC.setText("You have accepted join request from " + currActivity.getSender().getUsername() + " for ");
                    holder.rootViewSC.setTag(R.id.activity_main_layout, true);
                }
                break;

            //activity to display that invite request has accepted
            case ProjectMateAPIContract.ACTIVITY_TYPE_REQUEST_INVITE_ACCEPTED:
                holder.rootViewSC.setVisibility(View.VISIBLE);
                holder.projNameSC.setText(currActivity.getProject().getProject_name());
                if (isSenderMe) {
                    holder.rootViewSC.setTag(R.id.activity_main_layout, true);
                    holder.permTextSC.setText(currActivity.getReceiver().getUsername() + " has accepted your invitation for ");
                } else {
                    holder.permTextSC.setText("You have accepted invitation from " + currActivity.getSender().getUsername() + " for ");
                }
                break;

            //activity to display that join request has been rejected
            case ProjectMateAPIContract.ACTIVITY_TYPE_REQUEST_JOIN_REJECTED:
                holder.rootViewN.setVisibility(View.VISIBLE);
                holder.projNameN.setText(currActivity.getProject().getProject_name());

                if (isSenderMe) {
                    holder.permTextN.setText("Your join request has been rejected for ");
                } else {
                    holder.permTextN.setText("Your have rejected join request from " + currActivity.getSender().getUsername() + " for ");
                    holder.rootViewN.setTag(R.id.activity_main_layout, true);
                }
                break;

            //activity to display that invite request has been rejected
            case ProjectMateAPIContract.ACTIVITY_TYPE_REQUEST_INVITE_REJECTED:
                holder.rootViewN.setVisibility(View.VISIBLE);
                holder.projNameN.setText(currActivity.getProject().getProject_name());

                if (isSenderMe) {
                    holder.rootViewSC.setTag(R.id.activity_main_layout, true);
                    holder.permTextN.setText("Your invite request has been rejected by " + currActivity.getReceiver().getUsername() + " for ");
                } else {
                    holder.permTextN.setText("Your have rejected invite request from" + currActivity.getSender().getUsername() + " for ");
                    holder.rootViewN.setTag(R.id.activity_main_layout, true);
                }
                break;
        }
    }


    @Override
    public int getItemCount() {
        return mActivities.size();
    }

    static class ActivitiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout rootViewAR;
        LinearLayout rootViewSC;
        LinearLayout rootViewN;

        TextView permTextAR;
        TextView permTextSC;
        TextView permTextN;

        TextView projNameAR;
        TextView projNameSC;
        TextView projNameN;

        Button accept;
        Button reject;
        Button startChat;

        private RecyclerViewClickListener mListener;

        ActivitiesViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            this.mListener = listener;

            //initalizing notification layouts
            rootViewAR = itemView.findViewById(R.id.notification_layout_1);
            rootViewSC = itemView.findViewById(R.id.notification_layout_2);
            rootViewN = itemView.findViewById(R.id.notification_layout_3);

            //displaying fixed message at the top
            permTextAR = itemView.findViewById(R.id.permanent_text_1);
            permTextSC = itemView.findViewById(R.id.permanent_text_2);
            permTextN = itemView.findViewById(R.id.permanent_text_3);

            //name of the project
            projNameAR = itemView.findViewById(R.id.name_of_project_1);
            projNameSC = itemView.findViewById(R.id.name_of_project_2);
            projNameN = itemView.findViewById(R.id.name_of_project_3);

            //accept_reject_startChat_buttons
            accept = itemView.findViewById(R.id.accept_view);
            reject = itemView.findViewById(R.id.reject_view);
            startChat = itemView.findViewById(R.id.start_chat_view);

            //onClick for layouts
            rootViewAR.setOnClickListener(this);
            rootViewSC.setOnClickListener(this);
            rootViewN.setOnClickListener(this);

            //onClick for accept_reject_startChat_buttons
            accept.setOnClickListener(this);
            reject.setOnClickListener(this);
            startChat.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }


    }
}
