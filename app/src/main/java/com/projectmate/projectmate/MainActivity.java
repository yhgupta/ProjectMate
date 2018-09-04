package com.projectmate.projectmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Classes.Skill;
import com.projectmate.projectmate.Classes.User;
import com.projectmate.projectmate.Database.DatabaseContract;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();

        //Get the shared preferences
        SharedPreferences prefs = getSharedPreferences(DatabaseContract.SHARED_PREFS, MODE_PRIVATE);
        String userCode = prefs.getString(DatabaseContract.AUTH_CODE_KEY, null);

        //userCode is null if code not initialized by Browser Activity
        if (userCode == null) {
            intent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }

        ((TextView) findViewById(R.id.tv_temp)).setText("The code is : " + userCode);

        /*Skill skill1 = new Skill("Android", 5, "", "");
        Skill skill2 = new Skill("Android2", 5, "", "");

        ArrayList<Skill> skills = new ArrayList<>();
        skills.add(skill1);
        skills.add(skill2);

        Project project1 = new Project("App", "Nothinsg", "tete", skills);

        ArrayList<Project> projects = new ArrayList<>();
        projects.add(project1);
        User user = new User("Parth", "Manit", "betul", "india", "parth115", 122, skills, projects);

        Gson gson = new Gson();
        String json = gson.toJson(user);

        ((TextView) findViewById(R.id.tv_temp)).setText(json);

        Log.v("JSON", json);*/
    }
}
