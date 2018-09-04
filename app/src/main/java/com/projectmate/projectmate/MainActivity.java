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
import com.projectmate.projectmate.Database.StaticValues;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the shared preferences
        SharedPreferences prefs = getSharedPreferences(DatabaseContract.SHARED_PREFS, MODE_PRIVATE);
        String userCode = prefs.getString(DatabaseContract.AUTH_CODE_KEY, null);

        //userCode is null if code not initialized by Browser Activity
        if (userCode == null) {
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        StaticValues.setCodeChefAuthKey(userCode);

        boolean isFirstTime = prefs.getBoolean(DatabaseContract.AUTH_CODE_KEY, true);

        if(isFirstTime){
            Callback callback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if(response.isSuccessful()){

                        String jsonResponse = response.body().string();

                        Gson gson = new Gson();
                        User user = gson.fromJson(jsonResponse, User.class);

                        StaticValues.setCurrentUser(user);

                        if(user.getSkills().size()==0){
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                }
            };

        }
        else{

        }


    }

}

