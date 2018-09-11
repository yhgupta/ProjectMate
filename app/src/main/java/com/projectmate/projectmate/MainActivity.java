package com.projectmate.projectmate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateAPIContract;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.Classes.User;
import com.projectmate.projectmate.Database.DatabaseContract;
import com.projectmate.projectmate.Database.StaticValues;
import com.projectmate.projectmate.Fragments.MainFragmentsAdapter;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.graphics.Color.*;

public class MainActivity extends AppCompatActivity {

    private ImageButton mHomeButton;
    private ImageButton mMyProjectButton;
    private ImageButton mChatsButton;
    private ImageButton mNotificationButton;
    private ImageButton mProfileButton;

    private FragmentPagerAdapter mMainPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Finding all buttons
        mHomeButton = findViewById(R.id.home_button);
        mMyProjectButton = findViewById(R.id.project_button);
        mChatsButton = findViewById(R.id.chat_button);
        mNotificationButton = findViewById(R.id.notification_button);
        mProfileButton = findViewById(R.id.profile_button);


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

        Log.v("USERCODE", userCode);

        StaticValues.setCodeChefAuthKey(userCode);

        boolean isFirstTime = prefs.getBoolean(DatabaseContract.FIRST_TIME_KEY, true);

        if (isFirstTime) {
            userFirstTime();
        }

        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mMainPagerAdapter = new MainFragmentsAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMainPagerAdapter);

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        mMyProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });
        mChatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
            }
        });
        mNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(3);
            }
        });
        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(4);
            }
        });


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
                                if (user.getSkills().size() == 0) {
                                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                    startActivity(intent);
                                    fetchingDetailsDialog.dismiss();
                                    finish();
                                }
                                fetchingDetailsDialog.dismiss();
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

