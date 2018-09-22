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

    //initializing variables
    private List<Project> mProject;
    private Context mContext;
    private RecyclerViewClickListener mListener;

    //constructors
    public ProjectAdapter(List<Project> project, Context context, RecyclerViewClickListener listener) {
        this.mProject = project;
        this.mContext = context;
        this.mListener = listener;
    }

    public void updateProjects(List<Project> projects){
        this.mProject = projects;
        notifyDataSetChanged();
    }

    //inflating the rv_item_my_project layout to display
    @NonNull
    @Override
    public ProjectAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_my_project, parent, false);

        return new ProjectAdapter.ProjectViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ProjectViewHolder holder, int position) {
        //getting position of user of click
        Project currentProject = mProject.get(position);

        //holds the value of projectName and shortDesc
        holder.tvProjectName.setText(currentProject.getProjectName());
        holder.tvProjectShortDesc.setText(currentProject.getProjectShortDesc());

        //skillFlexAdapter layout and its manages
        SkillFlexAdapter adapter = new SkillFlexAdapter(currentProject.getSkills());
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        holder.rvSkillsView.setLayoutManager(flexboxLayoutManager);
        holder.rvSkillsView.setAdapter(adapter);
    }


    //returns the project size
    @Override
    public int getItemCount() {
        if(mProject==null) return 0;
        return mProject.size();
    }

    //ViewHolder extending RecyclerView and implementing OnclickListener
    static class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //initialize the variables
        LinearLayout rootView;
        TextView tvProjectName;
        TextView tvProjectShortDesc;
        RecyclerView rvSkillsView;
        private RecyclerViewClickListener mListener;

        ProjectViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            //displays the values in their particular view
            rootView = itemView.findViewById(R.id.project_item_layout);
            tvProjectName = itemView.findViewById(R.id.tv_project_name);
            tvProjectShortDesc = itemView.findViewById(R.id.tv_short_desc);
            rvSkillsView = itemView.findViewById(R.id.rv_my_skills);
            mListener = listener;
            rootView.setOnClickListener(this);
        }

        //onClick
        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
