package com.projectmate.projectmate.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projectmate.projectmate.Classes.Skill;
import com.projectmate.projectmate.R;

import java.util.ArrayList;
import java.util.List;

public class SkillFlexAdapter extends RecyclerView.Adapter<SkillFlexAdapter.TestViewHolder> {

    private List<Skill> mSkills;

    public SkillFlexAdapter(ArrayList<Skill> skills) {
        this.mSkills=skills;
    }

    public void setSkills(ArrayList<Skill> skills){
        this.mSkills=skills;
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SkillFlexAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_flexbox_item , parent, false);
        return new SkillFlexAdapter.TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        holder.tvStringName.setText(mSkills.get(position).getSkillName());
    }

    @Override
    public int getItemCount() {
        return mSkills.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        TextView tvStringName;

        public TestViewHolder(View itemView) {
            super(itemView);
            tvStringName = itemView.findViewById(R.id.skill_added);
        }
    }
}
