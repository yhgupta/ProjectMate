package com.projectmate.projectmate.Adapters;
        import android.content.Context;
        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.widget.CardView;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.flexbox.FlexDirection;
        import com.google.android.flexbox.FlexboxLayoutManager;
        import com.projectmate.projectmate.Classes.Project;
        import com.projectmate.projectmate.R;
        import java.util.ArrayList;
        import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>{

    private List<Project> mProject;
    private Context mContext;

    private RecyclerViewClickListener mListener;



    public ProjectAdapter(List<Project> project, Context context, RecyclerViewClickListener listener) {
        this.mProject = project;
        this.mContext = context;
        this.mListener = listener;
    }

    public void updateProjects(List<Project> projects){
        this.mProject = projects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProjectAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_my_project, parent, false);

        return new ProjectAdapter.ProjectViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ProjectViewHolder holder, int position) {
        Project currentProject = mProject.get(position);
        holder.tvProjectName.setText(currentProject.getProjectName());
        holder.tvProjectShortDesc.setText(currentProject.getProjectShortDesc());

        SkillFlexAdapter adapter = new SkillFlexAdapter(currentProject.getSkills());

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

        holder.rvSkillsView.setLayoutManager(flexboxLayoutManager);
        holder.rvSkillsView.setAdapter(adapter);

    }


    @Override
    public int getItemCount() {
        return mProject.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout rootView;
        TextView tvProjectName;
        TextView tvProjectShortDesc;
        RecyclerView rvSkillsView;

        private RecyclerViewClickListener mListener;

        ProjectViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            rootView = itemView.findViewById(R.id.project_item_layout);
            tvProjectName = itemView.findViewById(R.id.tv_project_name);
            tvProjectShortDesc = itemView.findViewById(R.id.tv_short_desc);
            rvSkillsView = itemView.findViewById(R.id.rv_my_skills);
            mListener = listener;

            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }


}
