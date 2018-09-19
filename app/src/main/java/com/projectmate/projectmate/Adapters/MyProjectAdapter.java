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
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.R;
import java.util.ArrayList;
import java.util.List;

public class MyProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Project> mProject;
    private Context mContext;

    private RecyclerViewClickListener mListener;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    private boolean mAllowEdit;

    public MyProjectAdapter(List<Project> project, RecyclerView recyclerView, Context context, RecyclerViewClickListener listener, OnLoadMoreListener loadMoreListener, boolean allowEdit) {
        this.mProject = project;
        this.mContext = context;
        this.mListener = listener;
        this.onLoadMoreListener = loadMoreListener;

        this.mAllowEdit = allowEdit;

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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_my_project, parent, false);
            vh = new ProjectViewHolder(v, mListener, mAllowEdit);

        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            vh = new ProgressBarViewHolder(v);

        }

        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        return mProject.get(position) == null ? VIEW_PROG : VIEW_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ProjectViewHolder){
            Project currentProject = mProject.get(position);
            ((ProjectViewHolder) holder).tvProjectName.setText(currentProject.getProjectName());
            ((ProjectViewHolder) holder).tvProjectShortDesc.setText(currentProject.getProjectShortDesc());

            ((ProjectViewHolder) holder).btnEditProject.setVisibility(View.VISIBLE);

            SkillFlexAdapter adapter = new SkillFlexAdapter(currentProject.getSkills());

            FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
            flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

            ((ProjectViewHolder) holder).rvSkillsView.setLayoutManager(flexboxLayoutManager);
            ((ProjectViewHolder) holder).rvSkillsView.setAdapter(adapter);
        }
        else {
            ((ProgressBarViewHolder) holder).progressBar.setIndeterminate(true);
        }


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
        ImageButton btnEditProject;

        private RecyclerViewClickListener mListener;

        ProjectViewHolder(View itemView, RecyclerViewClickListener listener, boolean allowEdit) {
            super(itemView);

            rootView = itemView.findViewById(R.id.project_item_layout);
            tvProjectName = itemView.findViewById(R.id.tv_project_name);
            tvProjectShortDesc = itemView.findViewById(R.id.tv_short_desc);
            rvSkillsView = itemView.findViewById(R.id.rv_my_skills);

            mListener = listener;
            rootView.setOnClickListener(this);

            if(allowEdit){
                btnEditProject = itemView.findViewById(R.id.project_btn_edit);
                btnEditProject.setOnClickListener(this);
            }

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
