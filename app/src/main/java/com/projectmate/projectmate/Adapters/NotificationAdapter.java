package com.projectmate.projectmate.Adapters;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projectmate.projectmate.Classes.Notification;
import com.projectmate.projectmate.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> mProject;

    public NotificationAdapter(List<Notification> project) {
        this.mProject = project;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_notification, parent, false);
        return new NotificationAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        int notificationType = mProject.get(position).getType();
        if (notificationType == 1) {
            holder.main_layout1.setVisibility(View.VISIBLE);
            holder.main_layout2.setVisibility(View.GONE);
            holder.main_layout3.setVisibility(View.GONE);
            holder.main_layout4.setVisibility(View.GONE);
            holder.tvProjectName1.setText(mProject.get(position).getName());
            holder.tvProjectShortDesc1.setText(mProject.get(position).getShortDesc());
        } else if (notificationType == 2) {
            holder.main_layout2.setVisibility(View.VISIBLE);
            holder.main_layout1.setVisibility(View.GONE);
            holder.main_layout3.setVisibility(View.GONE);
            holder.main_layout4.setVisibility(View.GONE);
            holder.tvProjectName2.setText(mProject.get(position).getName());
            holder.tvProjectShortDesc2.setText(mProject.get(position).getShortDesc());
        } else if (notificationType == 3) {
            holder.main_layout3.setVisibility(View.VISIBLE);
            holder.main_layout2.setVisibility(View.GONE);
            holder.main_layout1.setVisibility(View.GONE);
            holder.main_layout4.setVisibility(View.GONE);
            holder.tvProjectName3.setText(mProject.get(position).getName());
            holder.tvProjectShortDesc3.setText(mProject.get(position).getShortDesc());
        } else if (notificationType == 4) {
            holder.main_layout4.setVisibility(View.VISIBLE);
            holder.main_layout2.setVisibility(View.GONE);
            holder.main_layout3.setVisibility(View.GONE);
            holder.main_layout1.setVisibility(View.GONE);
            holder.tvProjectName4.setText(mProject.get(position).getName());
            holder.tvProjectShortDesc4.setText(mProject.get(position).getShortDesc());
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectName1;
        TextView tvProjectShortDesc1;
        TextView tvProjectName2;
        TextView tvProjectShortDesc2;
        TextView tvProjectName3;
        TextView tvProjectShortDesc3;
        TextView tvProjectName4;
        TextView tvProjectShortDesc4;

        RelativeLayout main_layout1;
        RelativeLayout main_layout2;
        RelativeLayout main_layout3;
        RelativeLayout main_layout4;


        public NotificationViewHolder(View itemView) {
            super(itemView);

            main_layout1 = itemView.findViewById(R.id.notification_layout_1);
            main_layout2 = itemView.findViewById(R.id.notification_layout_2);
            main_layout3 = itemView.findViewById(R.id.notification_layout_3);
            main_layout4 = itemView.findViewById(R.id.notification_layout_4);


            tvProjectName1 = itemView.findViewById(R.id.name_of_project_1);
            tvProjectShortDesc1 = itemView.findViewById(R.id.short_desc_of_project_1);

            tvProjectName2 = itemView.findViewById(R.id.name_of_project_2);
            tvProjectShortDesc2 = itemView.findViewById(R.id.short_desc_of_project_2);

            tvProjectName3 = itemView.findViewById(R.id.name_of_project_3);
            tvProjectShortDesc3 = itemView.findViewById(R.id.short_desc_of_project_3);

            tvProjectName4 = itemView.findViewById(R.id.name_of_project_4);
            tvProjectShortDesc4 = itemView.findViewById(R.id.short_desc_of_project_4);
        }

    }
}


