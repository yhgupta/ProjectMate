/*
package com.projectmate.projectmate.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Classes.ProjectSkills;
import com.projectmate.projectmate.Database.StaticValues;
import com.projectmate.projectmate.R;

import java.util.ArrayList;
import java.util.List;

public class MyProjectAdapter extends RecyclerView.Adapter<MyProjectAdapter.MyProjectViewHolder> {

    private List<ProjectSkills> mProjects;
    private Context mContext;

    public MyProjectAdapter(List<ProjectSkills> project, Context context) {
        this.mProjects = project; this.mContext = context;
    }


    @NonNull
    @Override
    public MyProjectAdapter.MyProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_my_project, parent, false);
        return new MyProjectAdapter.MyProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProjectAdapter.MyProjectViewHolder holder, int position) {
        ProjectSkills projectSkills = mProjects.get(position);
        holder.tvProjectName.setText(projectSkills.getProject().getProjectName());
        holder.tvProjectShortDesc.setText(projectSkills.getProject().getProjectShortDesc());

        SkillFlexAdapter adapter = new SkillFlexAdapter(projectSkills.getSkills());

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

        holder.rvSkills.setLayoutManager(flexboxLayoutManager);
        holder.rvSkills.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    static class MyProjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectName;
        TextView tvProjectShortDesc;
        RecyclerView rvSkills;

        public MyProjectViewHolder(View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.tv_project_name);
            tvProjectShortDesc = itemView.findViewById(R.id.tv_short_desc);
            rvSkills = itemView.findViewById(R.id.rv_my_skills);
        }
    }
}
*/
