package com.projectmate.projectmate.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.projectmate.projectmate.R;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

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

    private ProjectAdapter mProjectAdapter;


    //Global toast to prevent overlapping
    private Toast mToast;

    private CardView mSaveBtn;
    private TextView mSaveBtnText;
    private ProgressBar mSaveBtnProgress;
    private Boolean mSaveBtnClicked = false;


    private Boolean mChangesMade = false;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        //Initializing all fields in the main View
        mNameEditText = rootView.findViewById(R.id.activity_profile_et_full_name);
        mOrganizationEditText = rootView.findViewById(R.id.activity_profile_et_organization);
        mCityEditText = rootView.findViewById(R.id.activity_profile_et_city);
        mCountryEditText = rootView.findViewById(R.id.activity_profile_et_country);

        mSaveBtn = rootView.findViewById(R.id.profile_btn_save);
        mSaveBtnText = rootView.findViewById(R.id.profile_btn_text);
        mSaveBtnProgress = rootView.findViewById(R.id.profile_btn_progress);

        //Disabling all edit text
        mNameEditText.setEnabled(false);
        mNameEditText.setInputType(InputType.TYPE_NULL);
        mOrganizationEditText.setEnabled(false);
        mOrganizationEditText.setInputType(InputType.TYPE_NULL);
        mCityEditText.setEnabled(false);
        mCityEditText.setInputType(InputType.TYPE_NULL);
        mCountryEditText.setEnabled(false);
        mCountryEditText.setInputType(InputType.TYPE_NULL);

        mAddSkill = rootView.findViewById(R.id.activity_profile_tv_add_skill);
        mAddProject = rootView.findViewById(R.id.activity_profile_tv_add_project);

        mSkillsRv = rootView.findViewById(R.id.activity_profile_rv_skills);
        mProjectsRv = rootView.findViewById(R.id.activity_profile_rv_projects);


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
        mUser = StaticValues.getCurrentUser();

        //Setting all edit texts
        mNameEditText.setText(mUser.getName());
        mOrganizationEditText.setText(mUser.getOrganization());
        mCityEditText.setText(mUser.getCity());
        mCountryEditText.setText(mUser.getLocation());

        //Initialize adapters for Skills and Projects Recycler Views
        mSkillAdapter = new SkillAdapter(mUser.getSkills());

        //Set up layout managers and adapters

        //Now load all skills in mAllSkills from XML arrays

        mAllSkills = new ArrayList<>(StaticValues.getAllSkills());

        for (Skill skill : mUser.getSkills()) {
            mAllSkills.remove(skill.getSkillID());
        }

        //opening the project dialog box to edit the once saved projects
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                createEditProjectDialog(position).show();
            }
        };
        mProjectAdapter = new ProjectAdapter(mUser.getProjects(), getContext(), listener);

        //Opening skill dialog box to edit the once saved skill
        RecyclerViewClickListener skillListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                createEditSkillDialog(position).show();
            }
        };

        mSkillAdapter = new SkillAdapter(mUser.getSkills(), getContext(), skillListener);

        //Set up layout managers and adapters
        mProjectsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mProjectsRv.setAdapter(mProjectAdapter);
        mProjectsRv.setNestedScrollingEnabled(false);

        mSkillsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mSkillsRv.setAdapter(mSkillAdapter);
        mSkillsRv.setNestedScrollingEnabled(false);


        //saving the profile
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSaveBtnClicked) {
                    mSaveBtnClicked = true;
                    saveProfile();
                }

            }
        });

        return rootView;
    }

    //Create the dialog to add a project
    private Dialog createAddProjectDialog() {
        //Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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
        skillView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                if (addProject(view, mySkills)) {
                    dialog.dismiss();
                } else {
                    displayToast("Invalid");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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
        ArrayAdapter<String> autoCompleteTvAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mAllSkills);
        skillNameView.setAdapter(autoCompleteTvAdapter);


        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAllSkillsDialog(skillNameView, mAllSkills).show();
            }
        });

        //Adding positive and negative buttons
        builder.setPositiveButton(getString(R.string.add_skill_dialog_save_btn), new DialogInterface.OnClickListener() {
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

        displayToast("Project Added Successfully");
        mChangesMade = true;

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
        int position = StaticValues.getAllSkills().indexOf(name);

        if (position < 0) return false;

        //Create a new skill and add to user skills
        Skill skill = new Skill(position, name, rating, shortDesc, courses);
        mUser.getSkills().add(skill);

        //Finally remove the current skill as its added to user skills
        mAllSkills.remove(name);

        //Notify adapter
        mSkillAdapter.notifyDataSetChanged();

        mChangesMade = true;

        return true;
    }

    //Displays all skills
    private Dialog createAllSkillsDialog(final AutoCompleteTextView textView, ArrayList<String> allSkills) {

        //Create new dialog and get inflater
        AlertDialog.Builder listDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_list_skills, null);

        listDialog.setTitle("Select Skill");
        listDialog.setView(view);

        ListView listView = view.findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allSkills);
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
        AlertDialog.Builder listDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_list_skills, null);

        listDialog.setTitle("Select Skill");
        listDialog.setView(view);

        ListView listView = view.findViewById(R.id.listView);

        final ArrayList<String> skillList = new ArrayList<>();
        for (Skill skill : allSkills) {
            skillList.add(skill.getSkillName());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, skillList);
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

    //saving profile
    private void saveProfile() {
        if (!mChangesMade) {
            displayToast("No changes to save");
            mSaveBtnClicked = false;
            return;
        }
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
                saveToServer();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mSaveBtnText.startAnimation(fadeOut);
        mSaveBtnProgress.startAnimation(fadeIn);

    }

    //saving user details to server
    private void saveToServer() {
        Gson gson = new Gson();
        String jsonData = gson.toJson(mUser);
        Log.v("JSON", jsonData);
        String authToken = StaticValues.getCodeChefAuthKey();
        String url = ProjectMateUris.getAuthUrl();

        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Animation fadeOut = new AlphaAnimation(1, 0);
                            fadeOut.setInterpolator(new AccelerateInterpolator());
                            fadeOut.setDuration(400);

                            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    mSaveBtnProgress.setVisibility(View.INVISIBLE);
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
                                    mSaveBtnText.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            mSaveBtnText.startAnimation(fadeIn);
                            mSaveBtnProgress.startAnimation(fadeOut);
                            displayToast("Save Successful!");
                            mChangesMade = false;
                            mSaveBtnClicked = false;
                        }
                    });
                }
            }
        };
        OkHttpRequests requests = new OkHttpRequests();
        requests.performPutRequest(url, jsonData, callback, authToken);
    }

    //display toast message ( one function instead of writing whole function again and again
    private void displayToast(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast.setText(message);
            mToast.show();
        }

    }

    //dialog box to edit skill
    private Dialog createEditSkillDialog(final int position) {
        //Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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
                String shortDesc = shortDescription.getText().toString();
                String desc = coursesTaken.getText().toString();
                int rating = (int) ratingSkill.getRating();

                currSkill.setSkillName(skillName);
                currSkill.setShortDescription(shortDesc);
                currSkill.setCoursesTaken(desc);
                currSkill.setSkillRating(rating);

                mSkillAdapter.notifyDataSetChanged();

                mChangesMade = true;
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
                    Toast.makeText(getContext(), "Atleast One Skill is Needed", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    mUser.getSkills().remove(position);
                    mSkillAdapter.notifyDataSetChanged();
                    mChangesMade = true;
                    dialog.dismiss();
                }
            }
        });


        return builder.create();


    }

    //dialog box to edit project
    private Dialog createEditProjectDialog(final int position) {

        //Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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
                toRemove.add(skill);
            }
        }
        skills.removeAll(mySkills);

        //skills in skillAdapter
        final SkillAdapter skillAdapter = new SkillAdapter(mySkills);
        skillView.setLayoutManager(new LinearLayoutManager(getContext()));
        skillView.setAdapter(skillAdapter);


        addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAllSkillsProjectDialog(skillAdapter, skills, mySkills).show();
            }
        });


        //Adding positive and negative buttons
        //Also updates the field with new values
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

                mChangesMade = true;
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
                mChangesMade = true;
                dialog.dismiss();
            }
        });


        return builder.create();
    }


}
