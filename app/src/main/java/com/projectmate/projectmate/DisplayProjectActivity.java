package com.projectmate.projectmate;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.projectmate.projectmate.Adapters.SkillFlexAdapter;
import com.projectmate.projectmate.Classes.Project;

import java.util.ArrayList;

public class DisplayProjectActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;

    private Project mProject = new Project(1,"Name","Yash","Gupta",new ArrayList<Integer>()) ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_project);
        constraintLayout = findViewById(R.id.activity_main_layout);
        progressBar=(ProgressBar)findViewById(R.id.progressBar1);
        displayProject();
    }

    public void displayProject(){
        progressBar.setVisibility(View.INVISIBLE);
        constraintLayout.setVisibility(View.VISIBLE);
        TextView displayPName = findViewById(R.id.dialog_addproject_et_name);
        TextView displayPShortDesc = findViewById(R.id.dialog_addproject_short_desc);
        TextView displayPDesc = findViewById(R.id.dialog_addproject_complete_desc);

        displayPName.setText(mProject.getProjectName());
        displayPShortDesc.setText(mProject.getProjectShortDesc());
        displayPDesc.setText(mProject.getProjectName());

        RecyclerView displayPSkill = findViewById(R.id.dialog_add_project_rv);

        final ArrayList<Integer> mySkills = new ArrayList<>(mProject.getSkills());

        final SkillFlexAdapter skillAdapter = new SkillFlexAdapter(mySkills);

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        // Set flex direction.
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

        displayPSkill.setLayoutManager(flexboxLayoutManager);

        displayPSkill.setAdapter(skillAdapter);


    }

}
