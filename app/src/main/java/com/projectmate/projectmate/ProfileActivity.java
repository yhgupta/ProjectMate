package com.projectmate.projectmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

import com.google.gson.Gson;
import com.projectmate.projectmate.Adapters.ProjectAdapter;
import com.projectmate.projectmate.Adapters.RecyclerViewClickListener;
import com.projectmate.projectmate.Adapters.SkillAdapter;
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Classes.Skill;
import com.projectmate.projectmate.Classes.User;
import com.projectmate.projectmate.Database.StaticValues;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This activity is shown to the user on start if he is a new user
 * It is used to add skills and projects done by the user
 */

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

    //List and Project Global adapters
    private SkillAdapter mSkillAdapter;

    private ProjectAdapter mProjectAdapter;


    private ArrayList<String> mAllSkills;
    private ArrayList<String> mAllGlobalSkill;

    //Global toast to prevent overlapping
    private Toast mToast;

    private CardView mSaveBtn;
    private TextView mSaveBtnText;
    private ProgressBar mSaveBtnProgress;
    private Boolean mSaveBtnClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //Setting toolbar title
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.profile_activity_toolbar);

        //Initializing all fields in the main View
        mNameEditText = findViewById(R.id.activity_profile_et_full_name);
        mOrganizationEditText = findViewById(R.id.activity_profile_et_organization);
        mCityEditText = findViewById(R.id.activity_profile_et_city);
        mCountryEditText = findViewById(R.id.activity_profile_et_country);

        mSaveBtn = findViewById(R.id.profile_btn_save);
        mSaveBtnText = findViewById(R.id.profile_btn_text);
        mSaveBtnProgress = findViewById(R.id.profile_btn_progress);

        //Disabling all edit text
        mNameEditText.setEnabled(false);
        mNameEditText.setInputType(InputType.TYPE_NULL);
        mOrganizationEditText.setEnabled(false);
        mOrganizationEditText.setInputType(InputType.TYPE_NULL);
        mCityEditText.setEnabled(false);
        mCityEditText.setInputType(InputType.TYPE_NULL);
        mCountryEditText.setEnabled(false);
        mCountryEditText.setInputType(InputType.TYPE_NULL);

        mAddSkill = findViewById(R.id.activity_profile_tv_add_skill);
        mAddProject = findViewById(R.id.activity_profile_tv_add_project);

        mSkillsRv = findViewById(R.id.activity_profile_rv_skills);
        mProjectsRv = findViewById(R.id.activity_profile_rv_projects);

        mSkillsRv.setNestedScrollingEnabled(false);
        mProjectsRv.setNestedScrollingEnabled(false);


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
        StaticValues.setAllSkills(new ArrayList<>(Arrays.asList(arraySkill)));
        mAllSkills = new ArrayList<>(Arrays.asList(arraySkill));
        mAllGlobalSkill = new ArrayList<>(mAllSkills);

        mProjectAdapter = new ProjectAdapter(mUser.getProjects(), this, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });

        //calls the dialog box to edit the project dialog
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                createEditProjectDialog(position).show();
            }
        };
        mProjectAdapter = new ProjectAdapter(mUser.getProjects(), this, listener);

        RecyclerViewClickListener skillListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                createEditSkillDialog(position).show();
            }
        };

        mSkillAdapter = new SkillAdapter(mUser.getSkills(), this, skillListener);

        //Set up layout managers and adapters
        mSkillsRv.setLayoutManager(new LinearLayoutManager(this));
        mSkillsRv.setAdapter(mSkillAdapter);
        mSkillsRv.setNestedScrollingEnabled(false);


        //Set up layout managers and adapters
        mProjectsRv.setLayoutManager(new LinearLayoutManager(this));
        mProjectsRv.setAdapter(mProjectAdapter);
        mProjectsRv.setNestedScrollingEnabled(false);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSaveBtnClicked) {
                    saveProfile();
                    mSaveBtnClicked = true;
                }

            }
        });


    }

    private User getUser() {
        return StaticValues.getCurrentUser();
    }


    //Create the dialog to add a project
    private Dialog createAddProjectDialog() {
        //Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Get Layout Inflater
        LayoutInflater inflater = this.getLayoutInflater();

        //Get the root view using Layout Inflater
        final View view = inflater.inflate(R.layout.dialog_add_project, null);


        //Set the root view as Dialogs Layout
        builder.setView(view);


        //Find add skill button
        TextView addSkill = view.findViewById(R.id.dialog_addproject_add_skill);

        //Find Recycler View of list of skills
        RecyclerView skillView = view.findViewById(R.id.dialog_add_project_rv);


        //Make list of skills and mySkills is the skills added by user
        final ArrayList<Skill> skills = new ArrayList<>(mUser.getSkills());
        final ArrayList<Skill> mySkills = new ArrayList<>();


        //Configuring the recycler view
        final SkillAdapter skillAdapter = new SkillAdapter(mySkills);
        skillView.setLayoutManager(new LinearLayoutManager(this));
        skillView.setAdapter(skillAdapter);


        addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAllSkillsProjectDialog(skillAdapter, skills, mySkills).show();
            }
        });


        //Adding positive and negative buttons
        builder.setPositiveButton(R.string.add_project_dialog_save_and_add_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (addProject(view, mySkills)) {
                    dialog.dismiss();
                    createAddProjectDialog().show();
                }
            }

        }).setNeutralButton(getString(R.string.add_project_dialog_save_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addProject(view, mySkills)) {
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
    private Dialog createAddSkillDialog() {
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
                createAllSkillsDialog(skillNameView, mAllSkills).show();
            }
        });

        //Adding positive and negative buttons
        builder.setPositiveButton(R.string.add_skill_dialog_save_and_add_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addSkill(view)) {
                    dialog.dismiss();
                    createAddSkillDialog().show();
                } else {
                    displayToast(getString(R.string.add_skill_dialog_invalid_skill_toast));

                }

            }

        }).setNeutralButton(getString(R.string.add_skill_dialog_save_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addSkill(view)) {
                    dialog.dismiss();
                } else {
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
    private boolean addProject(View rootView, ArrayList<Skill> skills) {
        //Get all of the views

        EditText tvName = rootView.findViewById(R.id.dialog_addproject_et_name);
        EditText tvShortDesc = rootView.findViewById(R.id.dialog_addproject_short_desc);
        EditText tvCompleteDesc = rootView.findViewById(R.id.dialog_addproject_complete_desc);


        //Get all fields from the views
        String name = tvName.getText().toString().trim();
        String shortDesc = tvShortDesc.getText().toString().trim();
        String completeDesc = tvCompleteDesc.getText().toString().trim();

        //Check condition
        if (name.isEmpty()) return false;
        if (skills.size() == 0) return false;
        //TODO: Check other conditions

        ArrayList<Integer> skillIds = new ArrayList<>();
        for (Skill skill : skills) {
            skillIds.add(skill.getSkillID());
        }
        //Create a new skill and add to user skills
        Project project = new Project(0, name, shortDesc, completeDesc, skillIds);
        mUser.getProjects().add(project);


        //Notify adapter
        mProjectAdapter.notifyDataSetChanged();

        return true;
    }


    //Add the skill to user skills and return true if successful
    private boolean addSkill(View rootView) {
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
        if (name.isEmpty()) return false;
        int position = mAllGlobalSkill.indexOf(name);

        if (position < 0) return false;

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
    private Dialog createAllSkillsDialog(final AutoCompleteTextView textView, ArrayList<String> allSkills) {

        //Create new dialog and get inflater
        AlertDialog.Builder listDialog = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        //inflates the layout
        View view = inflater.inflate(R.layout.dialog_list_skills, null);

        listDialog.setTitle("Select Skill");
        listDialog.setView(view);


        ListView listView = view.findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allSkills);
        listView.setAdapter(adapter);

        android.support.v7.widget.SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        final Dialog dialog = listDialog.create();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(((TextView) view).getText().toString());
                dialog.dismiss();
                textView.setSelection(textView.getText().toString().length());
                textView.dismissDropDown();
            }
        });

        return dialog;

    }

    //Displays all skills
    private Dialog createAllSkillsProjectDialog(final SkillAdapter skillAdapter, final ArrayList<Skill> allSkills, final ArrayList<Skill> mySkills) {

        //Create new dialog and get inflater
        AlertDialog.Builder listDialog = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_list_skills, null);

        listDialog.setTitle("Select Skill");
        listDialog.setView(view);

        ListView listView = view.findViewById(R.id.listView);

        final ArrayList<String> skillList = new ArrayList<>();
        for (Skill skill : allSkills) {
            skillList.add(skill.getSkillName());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, skillList);
        listView.setAdapter(adapter);

        android.support.v7.widget.SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });


        final Dialog dialog = listDialog.create();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String skill = ((TextView) view).getText().toString();
                int pos = skillList.indexOf(skill);

                mySkills.add(allSkills.get(pos));
                allSkills.remove(pos);
                skillList.remove(pos);

                skillAdapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });

        return dialog;

    }

    private void saveProfile() {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(400);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSaveBtnText.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(400);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSaveBtnProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mSaveBtnText.startAnimation(fadeOut);
        mSaveBtnProgress.startAnimation(fadeIn);

        saveToServer();

    }

    private void saveToServer() {
        Gson gson = new Gson();
        String jsonData = gson.toJson(mUser);
        Log.v("JSON", jsonData);
        String authToken = StaticValues.getCodeChefAuthKey();
        String url = ProjectMateUris.getAuthUrl();
        Log.v("URL", url);

        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {

                    //Start Main Activity
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        OkHttpRequests requests = new OkHttpRequests();
        requests.performPutRequest(url, jsonData, callback, authToken);
    }

    private void displayToast(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast.setText(message);
            mToast.show();
        }

    }

    private Dialog createEditSkillDialog(final int position) {
        //Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Get Layout Inflater
        LayoutInflater inflater = this.getLayoutInflater();

        //Get the root view using Layout Inflater
        final View view = inflater.inflate(R.layout.dialog_add_skill, null);


        //Set the root view as Dialogs Layout
        builder.setView(view);

        final EditText skillNameEditText = view.findViewById(R.id.dialog_addskill_et_name);
        final RatingBar ratingSkill = view.findViewById(R.id.dialog_addskill_rating);
        final EditText shortDescription = view.findViewById(R.id.dialog_addskill_short_desc);
        final EditText coursesTaken = view.findViewById(R.id.dialog_addskill_courses_taken);

        final Skill currSkill = mUser.getSkills().get(position);

        skillNameEditText.setText(currSkill.getSkillName());
        ratingSkill.setRating(currSkill.getSkillRating());
        shortDescription.setText(currSkill.getShortDescription());
        coursesTaken.setText(currSkill.getCoursesTaken());

        builder.setPositiveButton(getString(R.string.add_skill_dialog_save_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String skillName = skillNameEditText.getText().toString();

                if (mAllGlobalSkill.contains(skillName)) {
                    displayToast("Invalid Skill");
                    return;
                }
                String shortDesc = shortDescription.getText().toString();
                String desc = coursesTaken.getText().toString();
                int rating = (int) ratingSkill.getRating();

                currSkill.setSkillName(skillName);
                currSkill.setShortDescription(shortDesc);
                currSkill.setCoursesTaken(desc);
                currSkill.setSkillRating(rating);

                mSkillAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

        }).setNegativeButton(getString(R.string.add_skill_dialog_cancel_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mUser.getSkills().size() == 1) {
                    displayToast("Atleast one skill is necessary");
                    dialog.dismiss();
                }
                mUser.getSkills().remove(position);
                mAllSkills.add(currSkill.getSkillName());
                mSkillAdapter.notifyDataSetChanged();
                mSkillAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


        return builder.create();


    }

    private Dialog createEditProjectDialog(final int position) {
        //Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Get Layout Inflater
        LayoutInflater inflater = this.getLayoutInflater();

        //Get the root view using Layout Inflater
        final View view = inflater.inflate(R.layout.dialog_add_project, null);


        //Set the root view as Dialogs Layout
        builder.setView(view);

        //Find Recycler View of list of skills
        final RecyclerView skillView = view.findViewById(R.id.dialog_add_project_rv);

        //Find add skill button
        TextView addSkill = view.findViewById(R.id.dialog_addproject_add_skill);

        //Finding and filling all the views
        final EditText nameEditText = view.findViewById(R.id.dialog_addproject_et_name);
        final EditText shortDescription = view.findViewById(R.id.dialog_addproject_short_desc);
        final EditText description = view.findViewById(R.id.dialog_addproject_complete_desc);

        final Project currProject = mUser.getProjects().get(position);

        nameEditText.setText(currProject.getProjectName());
        shortDescription.setText(currProject.getProjectShortDesc());
        description.setText(currProject.getProjectCompleteDesc());

        //Make list of skills and mySkills is the skills added by user
        final ArrayList<Skill> skills = new ArrayList<>(mUser.getSkills());
        final ArrayList<Skill> mySkills = new ArrayList<>();

        ArrayList<Skill> toRemove = new ArrayList<>();

        for (Skill skill : skills) {
            if (currProject.getSkills().contains(skill.getSkillID())) {
                mySkills.add(skill);
                skills.remove(skill);
            }
        }

        final SkillAdapter skillAdapter = new SkillAdapter(mySkills);


        skillView.setLayoutManager(new LinearLayoutManager(this));

        skillView.setAdapter(skillAdapter);


        addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAllSkillsProjectDialog(skillAdapter, skills, mySkills).show();
            }
        });


        //Adding positive and negative buttons
        builder.setPositiveButton(getString(R.string.add_project_dialog_save_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String projectName = nameEditText.getText().toString();
                String shortDesc = shortDescription.getText().toString();
                String desc = description.getText().toString();

                currProject.setProjectName(projectName);
                currProject.setProjectShortDesc(shortDesc);
                currProject.setProjectCompleteDesc(desc);

                currProject.getSkills().clear();
                for (Skill skill : mySkills) {
                    currProject.getSkills().add(skill.getSkillID());
                }
                mProjectAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

        }).setNegativeButton(getString(R.string.add_project_dialog_cancel_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mUser.getProjects().remove(position);
                mProjectAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


        return builder.create();
    }

}
