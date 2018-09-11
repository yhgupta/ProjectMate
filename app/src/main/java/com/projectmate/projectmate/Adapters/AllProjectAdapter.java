package com.projectmate.projectmate.Adapters;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.R;

import java.util.List;

public class AllProjectAdapter extends RecyclerView.Adapter<AllProjectAdapter.ProjectViewHolder>{

    private List<Project> mProject;
    private boolean moreAvailable=true;


    public AllProjectAdapter(List<Project> project) {
        this.mProject = project;
    }

    @NonNull
    @Override
    public AllProjectAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_project, parent, false);

        return new AllProjectAdapter.ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProjectAdapter.ProjectViewHolder holder, int position) {
        if(position==mProject.size()){
            if(!moreAvailable){
                holder.loadingBar.setVisibility(View.GONE);
                holder.mainLayout.setVisibility(View.GONE);
            }else{
                holder.mainLayout.setVisibility(View.INVISIBLE);
                holder.loadingBar.setVisibility(View.VISIBLE);
            }

        }
        else{
            holder.mainLayout.setVisibility(View.VISIBLE);
            holder.loadingBar.setVisibility(View.INVISIBLE);
            holder.tvProjectName.setText(mProject.get(position).getProjectName());
            holder.tvProjectShortDesc.setText(mProject.get(position).getProjectShortDesc() );
            // TODO HAVE AT SKILLS
            //holder.tvSkills.setText(mProject.get(position).getSkillIDs() );
        }


    }


    @Override
    public int getItemCount() {
        return mProject.size()+1;
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectName;
        TextView tvProjectShortDesc;

        LinearLayout mainLayout;
        ProgressBar loadingBar;

        ProjectViewHolder(View itemView) {
            super(itemView);

            tvProjectName = itemView.findViewById(R.id.tv_project_name);
            tvProjectShortDesc = itemView.findViewById(R.id.tv_short_desc);
            mainLayout = itemView.findViewById(R.id.project_item_layout);
            loadingBar = itemView.findViewById(R.id.project_item_pb);
        }

        private void viewClick(int project_id){
            //do whateevr with project_id
        }
    }

    public void setNoMorePresent(){
        moreAvailable=false;
    }


}
