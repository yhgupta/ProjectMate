package com.projectmate.projectmate.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.projectmate.projectmate.Classes.Notification;
import com.projectmate.projectmate.Classes.Skill;
import com.projectmate.projectmate.R;

import java.util.ArrayList;
import java.util.List;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillViewHolder>{

    private List<Skill> mSkills;
    private Context mContext;
    private RecyclerViewClickListener mListener;

    public SkillAdapter(List<Skill> skill, Context context, RecyclerViewClickListener listener ){
        this.mSkills = skill;
        this.mContext = context;
        this.mListener = listener;
    }

    public SkillAdapter(List<Skill> skills) {
        this.mSkills = skills;
    }

    @NonNull
    @Override
    public SkillAdapter.SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_skill, parent, false);
        return new SkillViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder holder, int position) {
        holder.tvStringName.setText(mSkills.get(position).getSkillName());
        holder.ratingBar.setRating(mSkills.get(position).getSkillRating());
    }


    @Override
    public int getItemCount() {
        if(mSkills==null) return 0;
        return mSkills.size();
    }

    static class SkillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout rootView;
        private RecyclerViewClickListener mListener;
        TextView tvStringName;
        RatingBar ratingBar;

        SkillViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            rootView = itemView.findViewById(R.id.root_view_skill);

            tvStringName = itemView.findViewById(R.id.tv_skill_name);
            ratingBar = itemView.findViewById(R.id.skill_rating);

            mListener = listener;
            rootView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
