package com.projectmate.projectmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.projectmate.projectmate.Adapters.ProjectAdapter;
import com.projectmate.projectmate.Adapters.RecyclerViewClickListener;
import com.projectmate.projectmate.Adapters.SkillAdapter;
import com.projectmate.projectmate.Adapters.SkillFlexAdapter;
import com.projectmate.projectmate.Classes.AllUserItem;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Classes.Skill;
import com.projectmate.projectmate.Classes.User;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private ArrayList<AllUserItem> allUserItems;
    private User mUser = new User(1,"yash","MANIT","Khargone","India"
            ,"yhgupta",1509, new ArrayList<Skill>(), new ArrayList<Project>()) ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser.getSkills().add(new Skill(1,"Fox"));
        mUser.getSkills().add(new Skill(2,"Kill the Fox"));

        setContentView(R.layout.activity_all_users);
        linearLayout = findViewById(R.id.all_user_display_activity);
        displayUser();
    }

    public void displayUser(){

        TextView displayName = findViewById(R.id.activity_all_users_et_full_name);
        TextView displayCity = findViewById(R.id.activity_all_users_et_city);
        TextView displayOrganisation = findViewById(R.id.activity_all_users_et_organization);
        TextView displayCountry = findViewById(R.id.activity_all_users_et_country);

        displayName.setText(mUser.getName());
        displayCity.setText(mUser.getCity());
        displayOrganisation.setText(mUser.getOrganization());
        displayCountry.setText(mUser.getLocation());

        RecyclerView displaySkills = findViewById(R.id.activity_all_users_rv_skills);
        RecyclerView displayProjects = findViewById(R.id.activity_all_users_rv_projects);

        final ArrayList<Skill> skills = new ArrayList<>(mUser.getSkills());
        final SkillAdapter skillAdapter = new SkillAdapter(skills, this, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                createDisplaySkillDialog(position).show();
            }
        });
        displaySkills.setLayoutManager(new LinearLayoutManager(this));
        displaySkills.setAdapter(skillAdapter);

        final ArrayList<Project> projects = new ArrayList<>(mUser.getProjects());
        final ProjectAdapter projectAdapter = new ProjectAdapter(projects, this, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                createDisplayProjectDialog(position).show();
            }
        });
        displayProjects.setLayoutManager(new LinearLayoutManager(this));
        displayProjects.setAdapter(projectAdapter);

    }

    private Dialog createDisplayProjectDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_add_project, null);

        builder.setView(view);

        TextView projectName = findViewById(R.id.dialog_addproject_et_name);
        TextView projectShortDesc = findViewById(R.id.dialog_addproject_short_desc);
        TextView projectDesc = findViewById(R.id.dialog_addproject_complete_desc);

        projectName.setText(mUser.getProjects().get(position).getProjectName());
        projectShortDesc.setText(mUser.getProjects().get(position).getProjectShortDesc());
        projectDesc.setText(mUser.getProjects().get(position).getProjectCompleteDesc());

        RecyclerView projectSkill = findViewById(R.id.dialog_add_project_rv);
        final ArrayList<Integer> mySkills = new ArrayList<>(mUser.getProjects().get(position).getSkills());
        final SkillFlexAdapter skillFlexAdapter = new SkillFlexAdapter(mySkills);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        projectSkill.setLayoutManager(flexboxLayoutManager);
        projectSkill.setAdapter(skillFlexAdapter);

        return builder.create();
    }

    private Dialog createDisplaySkillDialog( int position ){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        final View rootView = inflater.inflate(R.layout.dialog_add_skill_on_touch, null);

        builder.setView(rootView);

        TextView skillName = rootView.findViewById(R.id.dialog_addskill_et_name);
        RatingBar skillRating = rootView.findViewById(R.id.dialog_addskill_rating);
        skillRating.setEnabled(false);
        TextView skillShortDesc = rootView.findViewById(R.id.dialog_addskill_short_desc);
        TextView skillCoursesTaken = rootView.findViewById(R.id.dialog_addskill_courses_taken);

        skillName.setText(mUser.getSkills().get(position).getSkillName());
        skillRating.setRating(mUser.getSkills().get(position).getSkillRating());
        skillShortDesc.setText(mUser.getSkills().get(position).getShortDescription());
        skillCoursesTaken.setText(mUser.getSkills().get(position).getCoursesTaken());

        return builder.create();
    }

}
