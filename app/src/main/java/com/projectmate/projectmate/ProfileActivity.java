package com.projectmate.projectmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.projectmate.projectmate.Adapters.ProjectAdapter;
import com.projectmate.projectmate.Adapters.SkillAdapter;
import com.projectmate.projectmate.Classes.Skill;
import com.projectmate.projectmate.Classes.User;
import com.projectmate.projectmate.Database.StaticValues;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity {


    //All the fields in the main view
    private EditText mNameEditText;
    private EditText mOrganizationEditText;
    private EditText mCityEditText;
    private EditText mCountryEditText;

    private TextView mAddSkill;
    private TextView mAddProject;

    private RecyclerView mSkillsRv;
    private RecyclerView mProjectsRv;

    //Initializing a global User object
    private User mUser;

    //String array containing all supported skills
    private ArrayList<String> mAllSkills;

    //List and Project Global adapters
    private SkillAdapter mSkillAdapter;

    private ArrayList<String> mAllProjects;
    private ProjectAdapter mProjectAdapter;



    //Global toast to prevent overlapping
    private Toast mToast;

    //Global progress dialog
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //Setting toolbar title
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.profile_activity_toolbar);

        //Initializing all fields in the main View
        mNameEditText = findViewById(R.id.profile_et_name);
        mOrganizationEditText = findViewById(R.id.profile_et_organization);
        mCityEditText = findViewById(R.id.profile_et_city);
        mCountryEditText = findViewById(R.id.profile_et_country);

        //Disabling all edit text
        mNameEditText.setEnabled(false);mNameEditText.setInputType(InputType.TYPE_NULL);
        mOrganizationEditText.setEnabled(false);mOrganizationEditText.setInputType(InputType.TYPE_NULL);
        mCityEditText.setEnabled(false);mCityEditText.setInputType(InputType.TYPE_NULL);
        mCountryEditText.setEnabled(false);mCountryEditText.setInputType(InputType.TYPE_NULL);

        mAddSkill = findViewById(R.id.profile_tv_add_skill);
        mAddProject = findViewById(R.id.profile_tv_add_project);

        mSkillsRv = findViewById(R.id.profile_rv_skills);
        mProjectsRv = findViewById(R.id.profile_rv_projects);


        //Setting up buttons click listeners
        mAddSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddSkillDialog().show();
            }
        });
        mAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddProjectDialog().show();
            }
        });


        //Get the current user either from the server if already present or
        //get him using the CodeChef API sending request to server
        mUser = getUser();

        //Setting all edit texts
        mNameEditText.setText(mUser.getName());
        mOrganizationEditText.setText(mUser.getOrganization());
        mCityEditText.setText(mUser.getCity());
        mCountryEditText.setText(mUser.getLocation());

        //Initialize adapters for Skills and Projects Recycler Views
        mSkillAdapter = new SkillAdapter(mUser.getSkills());

        //Set up layout managers and adapters
        mSkillsRv.setLayoutManager(new LinearLayoutManager(this));
        mSkillsRv.setAdapter(mSkillAdapter);


        //Now load all skills in mAllSkills from XML arrays
        String[] arraySkill = getResources().getStringArray(R.array.skillsArray);
        mAllSkills = new ArrayList<>(Arrays.asList(arraySkill));


        mProjectAdapter = new ProjectAdapter((mUser.getProjects()));

        //Set up layout managers and adapters
        mProjectsRv.setLayoutManager(new LinearLayoutManager(this));
        mProjectsRv.setAdapter(mProjectAdapter);


    }

    private User getUser(){
        return StaticValues.getCurrentUser();
    }

    //Create the dialog to add a project
    private Dialog createAddProjectDialog(){
        //Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Get Layout Inflater
        LayoutInflater inflater = this.getLayoutInflater();

        //Get the root view using Layout Inflater
        final View view = inflater.inflate(R.layout.dialog_add_project, null);


        //Set the root view as Dialogs Layout
        builder.setView(view);

        //Adding positive and negative buttons
        builder.setPositiveButton(R.string.add_project_dialog_save_and_add_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(true/*addProject(view)*/) {
                    dialog.dismiss();
                    createAddProjectDialog().show();
                }
            }

        }).setNeutralButton(getString(R.string.add_project_dialog_save_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(addSkill(view)){
                    dialog.dismiss();
                }
            }

        }).setNegativeButton(getString(R.string.add_project_dialog_cancel_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });


        return builder.create();

    }

    //Creates the dialog to add a skill
    private Dialog createAddSkillDialog(){
        //Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Get Layout Inflater
        LayoutInflater inflater = this.getLayoutInflater();

        //Get the root view using Layout Inflater
        final View view = inflater.inflate(R.layout.dialog_add_skill, null);


        //Set the root view as Dialogs Layout
        builder.setView(view);

        //Get the auto complete text view and the choose button
        final AutoCompleteTextView skillNameView = view.findViewById(R.id.dialog_addskill_et_name);
        Button chooseBtn = view.findViewById(R.id.dialog_addskill_btn_choose);

        //Populate the autocomplete textView and also view list of skills on
        //pressing choose button
        ArrayAdapter<String> autoCompleteTvAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, mAllSkills);
        skillNameView.setAdapter(autoCompleteTvAdapter);


        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAllSkillsDialog(skillNameView).show();
            }
        });

        //Adding positive and negative buttons
        builder.setPositiveButton(R.string.add_skill_dialog_save_and_add_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(addSkill(view)){
                    dialog.dismiss();
                    createAddSkillDialog().show();
                }else{
                    displayToast(getString(R.string.add_skill_dialog_invalid_skill_toast));
                    createAddSkillDialog().show();
                }

            }

        }).setNeutralButton(getString(R.string.add_skill_dialog_save_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(addSkill(view)){
                    dialog.dismiss();
                }else{
                    displayToast(getString(R.string.add_skill_dialog_invalid_skill_toast));
                    createAddSkillDialog().show();
                }
            }

        }).setNegativeButton(getString(R.string.add_skill_dialog_cancel_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });


        return builder.create();
    }


    //Add the project to user and return true if successful
   /* private boolean addProject(View rootView){
        //Get all of the views

        EditText tvName = rootView.findViewById(R.id.dialog_addproject_et_name);
        EditText tvShortDesc = rootView.findViewById(R.id.dialog_addproject_short_desc);
        EditText tvCompleteDesc = rootView.findViewById(R.id.dialog_addproject_complete_desc);
        //EditText tvskillsID = rootView.findViewById(R.id.dialog_addproject_skillsID);

        //Get all fields from the views
        String name = tvName.getText().toString().trim();
        String shortDesc = tvShortDesc.getText().toString().trim();
        String completeDesc = tvCompleteDesc.getText().toString().trim();
       // int skillsID = tvskillsID.getId();
        *//*
        TODO ARRAYLIST
        * *//*//ArrayList<Integer> skillsID = tvskillsID.getText().toString().trim();


        //Check condition
        if(name.isEmpty()) return false;
        int position = mAllProjects.indexOf(name);

        if(position<0) return false;

        //Create a new skill and add to user skills
        *//*Project project = new Project( name, shortDesc, completeDesc, skillsID);
        mUser.getSkills().add(project);

        //Finally remove the current skill as its added to user skills
        mAllProjects.remove(name);

        //Notify adapter
        mProjectAdapter.notifyDataSetChanged();*//*

        return true;
    }
*/

    //Add the skill to user skills and return true if successful
    private boolean addSkill(View rootView){
        //Get all of the views
        EditText tvName = rootView.findViewById(R.id.dialog_addskill_et_name);
        RatingBar ratingBar = rootView.findViewById(R.id.dialog_addskill_rating);
        EditText tvShortDesc = rootView.findViewById(R.id.dialog_addskill_short_desc);
        EditText tvCourses = rootView.findViewById(R.id.dialog_addskill_courses_taken);

        //Get all fields from the views
        String name = tvName.getText().toString().trim();
        float rating = ratingBar.getRating();
        String shortDesc = tvShortDesc.getText().toString().trim();
        String courses = tvCourses.getText().toString().trim();

        //Check condition
        if(name.isEmpty()) return false;
        int position = mAllSkills.indexOf(name);

        if(position<0) return false;

        //Create a new skill and add to user skills
        Skill skill = new Skill(position, name, rating, shortDesc, courses);
        mUser.getSkills().add(skill);

        //Finally remove the current skill as its added to user skills
        mAllSkills.remove(name);

        //Notify adapter
        mSkillAdapter.notifyDataSetChanged();

        return true;
    }

    //Displays all skills
    private Dialog createAddProjectsDialog(final AutoCompleteTextView textView){
        //Create new dialog and get inflater
        AlertDialog.Builder listDialog = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_list_projects, null);

        listDialog.setTitle("Add Project");
        listDialog.setView(view);

        ListView listView = view.findViewById(R.id.listView);

        /*
           TODO HAVE A LOOK AT SIMPLE_LSIT_ITEM_1
        * */

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mAllProjects);
        listView.setAdapter(adapter);


        final Dialog dialog = listDialog.create();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(mAllProjects.get(position));
                dialog.dismiss();
                textView.setSelection(mAllProjects.get(position).length());
            }
        });

        return dialog;

    }

    private Dialog createAllSkillsDialog(final AutoCompleteTextView textView){
        //Create new dialog and get inflater
        AlertDialog.Builder listDialog = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_list_skills, null);

        listDialog.setTitle("Select Skill");
        listDialog.setView(view);

        ListView listView = view.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mAllSkills);
        listView.setAdapter(adapter);


        final Dialog dialog = listDialog.create();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(mAllSkills.get(position));
                dialog.dismiss();
                textView.setSelection(mAllSkills.get(position).length());
            }
        });

        return dialog;

    }


    private void displayToast(String message){
        if(mToast==null){
            mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            mToast.show();
        }else{
            mToast.setText(message);
            mToast.show();
        }

    }
}
