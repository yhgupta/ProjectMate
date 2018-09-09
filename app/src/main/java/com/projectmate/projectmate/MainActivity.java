package com.projectmate.projectmate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateAPIContract;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Classes.Skill;
import com.projectmate.projectmate.Classes.User;
import com.projectmate.projectmate.Database.DatabaseContract;
import com.projectmate.projectmate.Database.StaticValues;

import java.io.IOException;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
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

        boolean isFirstTime = prefs.getBoolean(DatabaseContract.FIRST_TIME_KEY, true);

        if (isFirstTime) {
            userFirstTime();
        }


    }

    private void userFirstTime() {
        final AlertDialog fetchingDetailsDialog = getFetchingDetailsDialog();
        fetchingDetailsDialog.show();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    final String jsonResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("API KEY", StaticValues.getCodeChefAuthKey());
                            Log.v("RESPONSE", jsonResponse);

                            if (jsonResponse.equals(ProjectMateAPIContract.AUTHENTICATION_FAILED)) {

                                fetchingDetailsDialog.dismiss();
                                getReAuthenticationDialog().show();

                            } else {
                                Gson gson = new Gson();
                                User user = gson.fromJson(jsonResponse, User.class);

                                StaticValues.setCurrentUser(user);
                                if (user.getSkills().size() == 0 || true) {
                                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                    startActivity(intent);
                                    fetchingDetailsDialog.dismiss();
                                    finish();
                                }
                            }
                        }
                    });




                }
            }
        };

        String authUrl = ProjectMateUris.getAuthUrl();
        OkHttpRequests httpRequests = new OkHttpRequests();
        httpRequests.performGetRequest(authUrl, callback, StaticValues.getCodeChefAuthKey());

    }

    private android.app.AlertDialog getFetchingDetailsDialog() {
        SpotsDialog.Builder dialog = new SpotsDialog.Builder();
        dialog.setContext(this).
                setMessage("Fetching Details").
                setCancelable(false);

        return dialog.build();
    }

    private AlertDialog getReAuthenticationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Authentication Failed")
                //.setMessage("Re-Authentication required.")
                .setCancelable(false)
                .setPositiveButton("Re Authenticate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
                        dialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                });

        return builder.create();
    }
}

