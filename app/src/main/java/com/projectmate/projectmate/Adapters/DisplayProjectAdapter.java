package com.projectmate.projectmate.Adapters;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.R;
import java.util.ArrayList;
import java.util.List;

public class DisplayProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>{

    private List<Project> mProject;


    public DisplayProjectAdapter(List<Project> project, Activity context) {
        this.mProject = project;
    }

    @NonNull
    @Override
    public ProjectAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_project, parent, false);
        return new ProjectAdapter.ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ProjectViewHolder holder, int position) {
        holder.tvProjectName.setText(mProject.get(position).getProjectName());
        holder.tvProjectShortDesc.setText(mProject.get(position).getProjectShortDesc() );


        // TODO HAVE AT SKILLS
        //holder.tvSkills.setText(mProject.get(position).getSkillIDs() );
    }


    @Override
    public int getItemCount() {
        return mProject.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectName;
        TextView tvProjectShortDesc;
        //TextView tvSkills;
        //RatingBar ratingBar;

        ProjectViewHolder(View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.tv_project_name);
            tvProjectShortDesc = itemView.findViewById(R.id.tv_short_desc);
            //tvSkills = itemView.findViewById(R.id.tv_skills);
            //ratingBar = itemView.findViewById(R.id.skill_rating);
        }
    }

}
