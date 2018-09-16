
package com.projectmate.projectmate.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.projectmate.projectmate.Adapters.ProjectAdapter;
import com.projectmate.projectmate.Adapters.SkillAdapter;
import com.projectmate.projectmate.Adapters.SkillFlexAdapter;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Classes.Skill;
import com.projectmate.projectmate.Classes.User;
import com.projectmate.projectmate.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProjectFragment extends Fragment {

    private FloatingActionButton mFab;
    private User mUser;
    private TextView mAddProject;
    private ProjectAdapter mProjectAdapter;
    private ArrayList<String> mAllSkills;

    public MyProjectFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_project, container, false);

        String[] arraySkill = getResources().getStringArray(R.array.skillsArray);
        mAllSkills = new ArrayList<>(Arrays.asList(arraySkill));
        mFab = rootView.findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddProjectDialog().show();
            }
        });

        return rootView;
    }

    private Dialog createAddProjectDialog(){
        //Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //Get Layout Inflater
        LayoutInflater inflater = this.getLayoutInflater();

        //Get the root view using Layout Inflater
        final View view = inflater.inflate(R.layout.dialog_add_project, null);


        //Set the root view as Dialogs Layout
        builder.setView(view);

        final RecyclerView skillView = view.findViewById(R.id.dialog_add_project_rv);

        //Find add skill button
        TextView addSkill = view.findViewById(R.id.dialog_addproject_add_skill);
        final ArrayList<String> skills = new ArrayList<>(mAllSkills);
        final ArrayList<Skill> mySkills = new ArrayList<>();
        //Find Recycler View of list of skills

        //Make list of skills and mySkills is the skills added by user
        final SkillFlexAdapter skillAdapter = new SkillFlexAdapter(mySkills);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        // Set flex direction.
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

        skillView.setLayoutManager(flexboxLayoutManager);
        skillView.setAdapter(skillAdapter);

        addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAllSkillsDialog(skillAdapter,skills,mySkills).show();
            }
        });


        //Adding positive and negative buttons
        builder.setPositiveButton(getString(R.string.add_project_dialog_save_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(addProject(view, mySkills)){
                    dialog.dismiss();
                }else{
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



    private boolean addProject(View rootView, ArrayList<Skill>skills){

        //Get all of the views
        EditText tvName = rootView.findViewById(R.id.dialog_addproject_et_name);
        EditText tvShortDesc = rootView.findViewById(R.id.dialog_addproject_short_desc);
        EditText tvCompleteDesc = rootView.findViewById(R.id.dialog_addproject_complete_desc);



        //Get all fields from the views
        String name = tvName.getText().toString().trim();
        String shortDesc = tvShortDesc.getText().toString().trim();
        String completeDesc = tvCompleteDesc.getText().toString().trim();

        //Check condition
        if(name.isEmpty()) return false;
        if(skills.size()==0) return false;
        //TODO: Check other conditions

        ArrayList<Integer> skillIds = new ArrayList<>();
        for(Skill skill: skills){
            skillIds.add(skill.getSkillID());
        }
        //Create a new skill and add to user skills
        Project project = new Project(0, name, shortDesc, completeDesc, skillIds);
        mUser.getProjects().add(project);


        //Notify adapter
        mProjectAdapter.notifyDataSetChanged();


        return true;
    }

    private Dialog createAllSkillsDialog(final SkillFlexAdapter skillAdapter, final ArrayList<String> allSkills, final ArrayList<Skill> mySkills){

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
                String skillName = allSkills.get(position);
                allSkills.remove(skillName);
                Skill newSkill = new Skill(position, skillName);
                mySkills.add(newSkill);
                skillAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        return dialog;

    }

}
