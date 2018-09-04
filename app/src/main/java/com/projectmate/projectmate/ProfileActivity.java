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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.projectmate.projectmate.Adapters.SkillAdapter;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Classes.Skill;
import com.projectmate.projectmate.Classes.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    //Global toast to prevent overlapping
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //Setting toolbar title and enabling back button
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.profile_activity_toolbar);
        toolbar.setDisplayHomeAsUpEnabled(true);


        //Initializing all fields in the main View
        mNameEditText = findViewById(R.id.profile_et_name);
        mOrganizationEditText = findViewById(R.id.profile_et_organization);
        mCityEditText = findViewById(R.id.profile_et_city);
        mCountryEditText = findViewById(R.id.profile_et_country);

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
                //TODO: Complete this
            }
        });


        //Get the current user either from the server if already present or
        //get him using the CodeChef API sending request to server
        mUser = getUser();


        //Initialize adapters for Skills and Projects Recycler Views
        mSkillAdapter = new SkillAdapter(mUser.getSkills());


        //Set up layout managers and adapters
        mSkillsRv.setLayoutManager(new LinearLayoutManager(this));
        mSkillsRv.setAdapter(mSkillAdapter);


        //Now load all skills in mAllSkills from XML arrays
        String[] array = getResources().getStringArray(R.array.skillsArray);
        mAllSkills = new ArrayList<>(Arrays.asList(array));

    }

    private User getUser(){
        String name = "Name";
        String organization = "Organization";
        String city = "City";
        String country = "Country";
        String username = "username";
        int ranking = 0;

        //If user is first time then
        User user = new User(name, organization, city, country, username, ranking);

        //else get array lists from net

        return user;
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
    private Dialog createAllSkillsDialog(final AutoCompleteTextView textView){
        //Create new dialog and get inflater
        AlertDialog.Builder listDialog = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_list_skills, null);

        listDialog.setTitle("Select Skill");
        listDialog.setView(view);

        ListView listView = view.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mAllSkills);
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
