package com.projectmate.projectmate;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.projectmate.projectmate.Adapters.SkillFlexAdapter;
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Database.StaticValues;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DisplayProjectActivity extends AppCompatActivity {

    //initialization of data types
    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;

    private RecyclerView displayPSkill;

    private Project mProject;

    private Callback mCallback;


    private CardView mSaveBtn;
    private TextView mSaveBtnText;
    private ProgressBar mSaveBtnProgress;
    private Boolean mSaveBtnClicked=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting of layout
        setContentView(R.layout.activity_display_project);

        //finding view for constraintlayout and progressBar
        constraintLayout = findViewById(R.id.activity_main_layout);
        progressBar = findViewById(R.id.progressBar1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Join Project");

        boolean showSaveBtn = getIntent().getBooleanExtra("SHOW_SAVE", true);

        //finding button fields
        mSaveBtn = findViewById(R.id.profile_btn_save);
        mSaveBtnText = findViewById(R.id.profile_btn_text);
        mSaveBtnProgress = findViewById(R.id.profile_btn_progress);

        //checks if it is pressed on not
        if(!showSaveBtn) mSaveBtn.setVisibility(View.GONE);

        displayPSkill = findViewById(R.id.dialog_add_skill_rv);

        //onClick of button
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSaveBtnClicked){
                    joinProject();
                    mSaveBtnClicked=true;
                }

            }
        });

        //checks for response and its data requesting by sending project id
        mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                Gson gson = new Gson();
                mProject = gson.fromJson(jsonData, Project.class);
                DisplayProjectActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayProject();
                    }
                });

            }
        };

        int proj_id = getIntent().getIntExtra("PROJECT_ID", 0);
        OkHttpRequests requests = new OkHttpRequests();
        String url = ProjectMateUris.getProject(proj_id);

        requests.performGetRequest(url, mCallback, StaticValues.getCodeChefAuthKey());
    }

    //dialog box to show the  project
    public void displayProject() {
        progressBar.setVisibility(View.INVISIBLE);
        constraintLayout.setVisibility(View.VISIBLE);
        TextView displayPName = findViewById(R.id.dialog_addproject_et_name);
        TextView displayPShortDesc = findViewById(R.id.dialog_addproject_short_desc);
        TextView displayPDesc = findViewById(R.id.dialog_addproject_complete_desc);

        displayPName.setText(mProject.getProjectName());
        displayPShortDesc.setText(mProject.getProjectShortDesc());
        displayPDesc.setText(mProject.getProjectCompleteDesc());

        final ArrayList<Integer> mySkills = new ArrayList<>(mProject.getSkills());

        final SkillFlexAdapter skillAdapter = new SkillFlexAdapter(mySkills);

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);

        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

        displayPSkill.setLayoutManager(flexboxLayoutManager);

        displayPSkill.setAdapter(skillAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        this.finish();
        return true;

    }

    //join request and its animation
    private void joinProject(){
        /*fadein and fadeout animation works and further
        data retrieving and notification sending and receiving works*/
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
                sendJoinRequest();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mSaveBtnText.startAnimation(fadeOut);
        mSaveBtnProgress.startAnimation(fadeIn);

    }

    private void sendJoinRequest(){
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    DisplayProjectActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DisplayProjectActivity.this, "Sent join request", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        };

        OkHttpRequests requests = new OkHttpRequests();
        String url = ProjectMateUris.JoinProject(mProject.getId());
        requests.performGetRequest(url, callback, StaticValues.getCodeChefAuthKey());
    }
}
