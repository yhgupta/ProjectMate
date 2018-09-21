package com.projectmate.projectmate.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.projectmate.projectmate.Classes.AllUserItem;
import com.projectmate.projectmate.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<AllUserItem> mUsers;
    private Context mContext;
    private RecyclerViewClickListener mListener;

    public UserAdapter( ArrayList<AllUserItem> users, Context mContext, RecyclerViewClickListener mListener){
        this.mUsers = users;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_user, parent, false);

        return new UserAdapter.UserViewHolder(view,mListener );
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        AllUserItem currentUser = mUsers.get(position);
        holder.nameView.setText(currentUser.getUsername());
        SkillFlexAdapter adapter = new SkillFlexAdapter(currentUser.getSkills());

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

        holder.skillView.setLayoutManager(flexboxLayoutManager);
        holder.skillView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout rootView;
        TextView nameView;
        RecyclerView skillView;

        private RecyclerViewClickListener mListener;


        public UserViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            rootView = itemView.findViewById(R.id.project_item_layout);
            nameView = itemView.findViewById(R.id.user_item_name_view);
            skillView = itemView.findViewById(R.id.user_item_skills_recycler_view);

            mListener = listener;
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }

    }
}
