/*
package com.projectmate.projectmate.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.projectmate.projectmate.Classes.Skill;
import com.projectmate.projectmate.Classes.User;
import com.projectmate.projectmate.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> mUsers;


    public UserAdapter( ArrayList<User> users){
        this.mUsers = users;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_user, parent, false);

        return new UserAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User currentUser = mUsers.get(position);
        holder.skillFlexAdapter.setSkills(currentUser.getSkills());
        holder.nameView.setText(mUsers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        RecyclerView skillView;

        SkillFlexAdapter skillFlexAdapter;
        public UserViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.user_item_name_view);
            skillView = itemView.findViewById(R.id.user_item_skills_recycler_view);

            skillFlexAdapter = new SkillFlexAdapter(new ArrayList<Skill>());
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(itemView.getContext());
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);

            skillView.setLayoutManager(layoutManager);
            skillView.setAdapter(skillFlexAdapter);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext() ,"Opening",Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}
*/
