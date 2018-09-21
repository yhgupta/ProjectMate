package com.projectmate.projectmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.projectmate.projectmate.Adapters.ProjectAdapter;
import com.projectmate.projectmate.Adapters.RecyclerViewClickListener;
import com.projectmate.projectmate.Adapters.SkillAdapter;
import com.projectmate.projectmate.Adapters.SkillFlexAdapter;
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.Classes.AllUserItem;
import com.projectmate.projectmate.Classes.Skill;
import com.projectmate.projectmate.Classes.User;
import com.projectmate.projectmate.Database.StaticValues;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DisplayUserActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private ArrayList<AllUserItem> allUserItems;
    private User mUser;


    private int mUserId;
    private int projectId;

    private CardView mSaveBtn;
    private TextView mSaveBtnText;
    private ProgressBar mSaveBtnProgress;
    private Boolean mSaveBtnClicked=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_user);
        linearLayout = findViewById(R.id.all_user_display_activity);

        mUserId = getIntent().getIntExtra("USER_ID", 0);
        projectId = getIntent().getIntExtra("PROJECT_ID", 0);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Invite User");

        boolean showSaveBtn = getIntent().getBooleanExtra("SHOW_SAVE", true);

        mSaveBtn = findViewById(R.id.all_users_btn_save);
        mSaveBtnText = findViewById(R.id.profile_btn_text);
        mSaveBtnProgress = findViewById(R.id.profile_btn_progress);

        if(!showSaveBtn) mSaveBtn.setVisibility(View.GONE);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteUser();
            }
        });


        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonData = response.body().string();

                    Gson gson = new Gson();

                    mUser = gson.fromJson(jsonData, User.class);

                    DisplayUserActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayUser();
                        }
                    });
                }
            }
        };

        OkHttpRequests requests = new OkHttpRequests();
        String url = ProjectMateUris.getUser(mUserId);
        requests.performGetRequest(url, callback, StaticValues.getCodeChefAuthKey());
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

        final ProjectAdapter projectAdapter = new ProjectAdapter(mUser.getProjects(), this, new RecyclerViewClickListener() {
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

        final View view = inflater.inflate(R.layout.dialog_add_project_on_touch, null);

        builder.setView(view);

        TextView projectName = view.findViewById(R.id.dialog_addproject_et_name);
        TextView projectShortDesc = view.findViewById(R.id.dialog_addproject_short_desc);
        TextView projectDesc = view.findViewById(R.id.dialog_addproject_complete_desc);

        projectName.setText(mUser.getProjects().get(position).getProjectName());
        projectShortDesc.setText(mUser.getProjects().get(position).getProjectShortDesc());
        projectDesc.setText(mUser.getProjects().get(position).getProjectCompleteDesc());

        RecyclerView projectSkill = view.findViewById(R.id.dialog_add_project_rv);
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


    private void inviteUser(){
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
                sendInviteRequest();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mSaveBtnText.startAnimation(fadeOut);
        mSaveBtnProgress.startAnimation(fadeIn);

    }

    private void sendInviteRequest(){
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    DisplayUserActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DisplayUserActivity.this, "Sent invite request", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        };

        OkHttpRequests requests = new OkHttpRequests();
        String url = ProjectMateUris.InviteUserToProject(projectId,mUserId);
        requests.performGetRequest(url, callback, StaticValues.getCodeChefAuthKey());
    }

}
