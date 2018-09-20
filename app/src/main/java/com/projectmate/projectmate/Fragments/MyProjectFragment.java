
package com.projectmate.projectmate.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projectmate.projectmate.Adapters.MyProjectAdapter;
import com.projectmate.projectmate.Adapters.OnLoadMoreListener;
import com.projectmate.projectmate.Adapters.RecyclerViewClickListener;
import com.projectmate.projectmate.Adapters.SkillFlexAdapter;
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Database.StaticValues;
import com.projectmate.projectmate.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProjectFragment extends Fragment {

    private FloatingActionButton mFab;
    private RecyclerView mProjectsRv;

    private CardView mSaveBtn;
    private TextView mSaveBtnText;
    private ProgressBar mSaveBtnProgress;

    private MyProjectAdapter mProjectAdapter;
    private ArrayList<Project> mProjects;

    private Toast mToast;
    private boolean mChangesMade = false;
    private boolean mSaveBtnClicked = false;

    Callback mCallback;

    private RecyclerViewClickListener mItemClickListener;
    private OnLoadMoreListener mLoadMoreListener;

    private boolean moreItemsPresent = true;
    private boolean loading;

    public MyProjectFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_project, container, false);

        mFab = rootView.findViewById(R.id.my_project_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddProjectDialog().show();
            }
        });

        loading = false;

        mSaveBtn = rootView.findViewById(R.id.my_project_btn_save);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("json", String.valueOf(mProjects.size()));
                if(!mChangesMade){
                    displayToast("No changes to save");
                }
                else if(!mSaveBtnClicked) saveProjects();
            }
        });

        mSaveBtnText = rootView.findViewById(R.id.my_project_btn_text);
        mSaveBtnProgress = rootView.findViewById(R.id.my_project_btn_progress);

        mProjects = new ArrayList<>();

        mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful()){
                    final String jsonData = response.body().string();
                    Gson gson = new Gson();

                    TypeToken<ArrayList<Project>> token = new TypeToken<ArrayList<Project>>() {};
                    final ArrayList<Project> projects = gson.fromJson(jsonData, token.getType());


                    mProjects.remove(mProjects.size() - 1);
                    mProjectAdapter.setLoaded();

                    if(projects.isEmpty()) moreItemsPresent = false;
                    else mProjects.addAll(projects);

                    loading = false;


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("JOSN", jsonData);
                            if(!projects.isEmpty()) mProjectAdapter
                                    .notifyItemRangeChanged(
                                            mProjects.size() - projects.size(),
                                            mProjects.size());

                            else mProjectAdapter.notifyItemRemoved(mProjects.size());


                        }
                    });


                }

            }
        };

        mItemClickListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(view instanceof ImageButton){
                    createEditProjectDialog(position).show();
                }
            }
        };

        mLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(!moreItemsPresent){
                    mProjectAdapter.setLoaded();
                    return;
                }
                if(loading) return;
                OkHttpRequests requests = new OkHttpRequests();
                String url = ProjectMateUris.getAllProjects(mProjects.size());

                mProjects.add(null);
                loading = true;
                mProjectsRv.post(new Runnable() {
                    @Override
                    public void run() {
                        mProjectAdapter.notifyItemChanged(mProjects.size()-1);
                    }
                });


                requests.performGetRequest(url, mCallback, StaticValues.getCodeChefAuthKey());
            }
        };


        mProjectsRv = rootView.findViewById(R.id.my_projects_recycler_view);
        mProjectsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mProjectsRv.setHasFixedSize(true);

        mProjectAdapter = new MyProjectAdapter(mProjects, mProjectsRv,
                getContext(), mItemClickListener, mLoadMoreListener, true);

        mProjectsRv.setAdapter(mProjectAdapter);

        mLoadMoreListener.onLoadMore();

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

        //Find Recycler View of list of skills
        final RecyclerView skillView = view.findViewById(R.id.dialog_add_project_rv);

        //Find add skill button
        TextView addSkill = view.findViewById(R.id.dialog_addproject_add_skill);

        //Make list of skills and mySkills is the skills added by user
        final ArrayList<String> skills = new ArrayList<>(StaticValues.getAllSkills());
        final ArrayList<Integer> mySkills = new ArrayList<>();

        final SkillFlexAdapter skillAdapter = new SkillFlexAdapter(mySkills);


        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        // Set flex direction.
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

        skillView.setLayoutManager(flexboxLayoutManager);

        skillView.setAdapter(skillAdapter);



        addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAllSkillsDialog(skillAdapter ,skills,mySkills).show();
            }
        });


        //Adding positive and negative buttons
        builder.setPositiveButton(getString(R.string.add_project_dialog_save_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(addProject(view, mySkills)){
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

    private Dialog createAllSkillsDialog(final SkillFlexAdapter skillAdapter, final ArrayList<String> allSkills, final ArrayList<Integer> mySkills){

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
                int skillId = StaticValues.getAllSkills().indexOf(skillName);

                mySkills.add(skillId);
                allSkills.remove(position);

                skillAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        return dialog;

    }

    private boolean addProject(View rootView, ArrayList<Integer>skills){

        //Get all of the views
        EditText tvName = rootView.findViewById(R.id.dialog_addproject_et_name);
        EditText tvShortDesc = rootView.findViewById(R.id.dialog_addproject_short_desc);
        EditText tvCompleteDesc = rootView.findViewById(R.id.dialog_addproject_complete_desc);



        //Get all fields from the views
        String name = tvName.getText().toString().trim();
        String shortDesc = tvShortDesc.getText().toString().trim();
        String completeDesc = tvCompleteDesc.getText().toString().trim();


        //Create a new skill and add to user skills
        Project project = new Project(0, name, shortDesc, completeDesc, skills);

        mProjects.add(0, project);

        mChangesMade = true;

        //Notify adapter
        mProjectAdapter.notifyDataSetChanged();

        return true;

    }

    private void saveProjects(){
        Log.v("json", String.valueOf(mProjects.size()));
        mSaveBtnClicked = true;
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

    private void saveToServer(){
        Gson gson = new Gson();
        Log.v("JSON", String.valueOf(mProjects.size()));
        String jsonData = gson.toJson(mProjects);
        Log.v("JSON", jsonData);
        String authToken = StaticValues.getCodeChefAuthKey();
        String url = ProjectMateUris.getAllProjects(0);
        Log.v("URL",url);

        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    saveSuccess();
                    reloadAll();
                }
            }
        };
        OkHttpRequests requests = new OkHttpRequests();
        requests.performPutRequest(url, jsonData, callback, authToken);
    }

    private void saveSuccess(){
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
        mSaveBtnClicked = false;
        mChangesMade = false;
    }

    private void reloadAll(){
        mProjects.clear();

        OkHttpRequests requests = new OkHttpRequests();
        String url = ProjectMateUris.getAllProjects(0);
        String authToken = StaticValues.getCodeChefAuthKey();

        moreItemsPresent = true;
        mProjects.add(null);
        loading = true;
        requests.performGetRequest(url, mCallback, authToken);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                saveSuccess();
            }
        });

    }

    private Dialog createEditProjectDialog(final int position){
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

        final Project currProject = mProjects.get(position);

        nameEditText.setText(currProject.getProjectName());
        shortDescription.setText(currProject.getProjectShortDesc());
        description.setText(currProject.getProjectCompleteDesc());

        //Make list of skills and mySkills is the skills added by user
        final ArrayList<String> skills = new ArrayList<>(StaticValues.getAllSkills());
        final ArrayList<Integer> mySkills = new ArrayList<>(currProject.getSkills());

        final SkillFlexAdapter skillAdapter = new SkillFlexAdapter(mySkills);

        Collections.sort(mySkills, Collections.<Integer>reverseOrder());

        for(int i : mySkills){
            skills.remove(i);
        }


        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        // Set flex direction.
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

        skillView.setLayoutManager(flexboxLayoutManager);

        skillView.setAdapter(skillAdapter);



        addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAllSkillsDialog(skillAdapter ,skills, mySkills).show();
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
               currProject.setSkills(mySkills);
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
                Log.v("Index", String.valueOf(position));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProjects.remove(position);
                    }
                });
                Log.v("json", String.valueOf(mProjects.size()));
                mProjectAdapter.notifyDataSetChanged();
                mChangesMade = true;
                dialog.dismiss();
            }
        });


        return builder.create();
    }

    private void displayToast(final String message){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mToast==null){
                    mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                    mToast.show();
                }else{
                    mToast.setText(message);
                    mToast.show();
                }
            }
        });


    }
}
