package com.projectmate.projectmate;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;

    private Project mProject;

    private Callback mCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_project);
        constraintLayout = findViewById(R.id.activity_main_layout);
        progressBar= findViewById(R.id.progressBar1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

        displayPSkill.setLayoutManager(flexboxLayoutManager);

        displayPSkill.setAdapter(skillAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.homeAsUp){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
