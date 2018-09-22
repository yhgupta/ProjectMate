package com.projectmate.projectmate.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projectmate.projectmate.Database.StaticValues;
import com.projectmate.projectmate.R;

import java.util.ArrayList;
import java.util.List;

public class SkillFlexAdapter extends RecyclerView.Adapter<SkillFlexAdapter.TestViewHolder> {

    //initializing variables
    private List<Integer> mSkills;

    //constructor
    public SkillFlexAdapter(ArrayList<Integer> skills) {
        this.mSkills = skills;
    }

    //inflating the rv_item_flexbox_item layout
    @NonNull
    @Override
    public SkillFlexAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_flexbox_item, parent, false);
        return new SkillFlexAdapter.TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        //getting the position
        String skillName = StaticValues.getAllSkills().get(mSkills.get(position));
        holder.tvStringName.setText(skillName);
    }

    @Override
    public int getItemCount() {
        return mSkills.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        //initializing the variables
        TextView tvStringName;

        public TestViewHolder(View itemView) {
            super(itemView);
            //setting the fields in their particular view
            tvStringName = itemView.findViewById(R.id.skill_added);
        }
    }
}
