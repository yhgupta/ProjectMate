package com.projectmate.projectmate.Adapters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.projectmate.projectmate.Classes.AllUserItem;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.R;

import java.util.List;

public class AllUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<AllUserItem> mUsers;
    private Context mContext;

    private RecyclerViewClickListener mListener;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    private boolean firstTime = true;

    public AllUsersAdapter(List<AllUserItem> users, RecyclerView recyclerView, Context context, RecyclerViewClickListener listener, OnLoadMoreListener loadMoreListener) {
        this.mUsers = users;
        this.mContext = context;
        this.mListener = listener;
        this.onLoadMoreListener = loadMoreListener;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(firstTime){
                    firstTime = false;
                    return;
                }

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
            vh = new UserViewHolder(v, mListener);

        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            vh = new ProgressBarViewHolder(v);

        }

        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        return mUsers.get(position) == null ? VIEW_PROG : VIEW_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof UserViewHolder){
            AllUserItem currentUser = mUsers.get(position);
            ((UserViewHolder) holder).tvUserName.setText(currentUser.getUserName());


            SkillFlexAdapter adapter = new SkillFlexAdapter(currentUser.getListx());

            FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
            flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

            ((UserViewHolder) holder).rvSkillsView.setLayoutManager(flexboxLayoutManager);
            ((UserViewHolder) holder).rvSkillsView.setAdapter(adapter);
        }
        else {
            ((ProgressBarViewHolder) holder).progressBar.setIndeterminate(true);
        }


    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout rootView;
        TextView tvUserName;
        RecyclerView rvSkillsView;

        private RecyclerViewClickListener mListener;

        UserViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            rootView = itemView.findViewById(R.id.rv_item_root_view);
            tvUserName = itemView.findViewById(R.id.user_item_name_view);
            rvSkillsView = itemView.findViewById(R.id.user_item_skills_recycler_view);

            mListener = listener;
            rootView.setOnClickListener(this);

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
