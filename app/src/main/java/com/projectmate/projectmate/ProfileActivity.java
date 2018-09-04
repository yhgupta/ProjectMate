package com.projectmate.projectmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.projectmate.projectmate.Adapters.SkillAdapter;
import com.projectmate.projectmate.Classes.Skill;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mOrganizationEditText;
    private EditText mCityEditText;
    private EditText mCountryEditText;

    private TextView mAddSkill;
    private TextView mAddProject;

    private RecyclerView mSkillsRv;
    private RecyclerView mProjectsRv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mNameEditText = findViewById(R.id.profile_et_name);
        mOrganizationEditText = findViewById(R.id.profile_et_organization);
        mCityEditText = findViewById(R.id.profile_et_city);
        mCountryEditText = findViewById(R.id.profile_et_country);

        mAddSkill = findViewById(R.id.profile_tv_add_skill);
        mAddProject = findViewById(R.id.profile_tv_add_project);

        mSkillsRv = findViewById(R.id.profile_rv_skills);
        mProjectsRv = findViewById(R.id.profile_rv_projects);


        mAddSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddSkillDialog().show();
            }
        });

        List<Skill> array = new ArrayList<>();
        array.add(new Skill("Test", 5, "", ""));

        SkillAdapter skillAdapter = new SkillAdapter(array);

        mSkillsRv.setLayoutManager(new LinearLayoutManager(this));
        mSkillsRv.setAdapter(skillAdapter);

        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Your Profile");
        toolbar.setDisplayHomeAsUpEnabled(true);
    }

    private Dialog createAddSkillDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_add_skill, null));

        builder.setPositiveButton("Save and add another", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Save to array list and add another
            }

        }).setNeutralButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Save to array list
            }

        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        return builder.create();
    }
}
