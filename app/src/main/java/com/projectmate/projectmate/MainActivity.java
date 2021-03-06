package com.projectmate.projectmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateAPIContract;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.Classes.User;
import com.projectmate.projectmate.Database.DatabaseContract;
import com.projectmate.projectmate.Database.StaticValues;
import com.projectmate.projectmate.Fragments.MainFragmentsAdapter;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This is the main activity which checks if user is authenticated and is home for all
 * the different fragments with a bottom navigation bar
 */

public class MainActivity extends AppCompatActivity {


    //Initialization of data types
    private RotateLoading mRotateLoading;

    private FragmentPagerAdapter mMainPagerAdapter;
    private ViewPager mViewPager;

    private BottomNavigationView mBottomNavigation;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //inflating layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding particular view
        mRotateLoading = findViewById(R.id.rotateloading);

        mRotateLoading.start();

        //mViewPager = findViewById(R.id.main)
        mViewPager = findViewById(R.id.activity_main_view_pager);

        mViewPager.setOffscreenPageLimit(4);

        //Set the title and its name and other properties
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mToolbar = findViewById(R.id.main_toolbar);
        mToolbar.setTitle("Project Mate");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorRed));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(mToolbar);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigation.setSelectedItemId(getItemID(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //sets the bottom navigation bar and changes on touch and slide
        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        mViewPager.setCurrentItem(0);
                        return true;
                    case R.id.action_project:
                        mViewPager.setCurrentItem(1);
                        return true;
                    case R.id.action_chat:
                        mViewPager.setCurrentItem(2);
                        return true;
                    case R.id.action_notification:
                        mViewPager.setCurrentItem(3);
                        return true;
                    case R.id.action_profile:
                        mViewPager.setCurrentItem(4);
                        return true;
                }
                return false;
            }
        });

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

        connectToServer();

    }

    //gets the itemsID nd changes the layout according to it
    private int getItemID(int index) {
        switch (index) {
            case 0:
                return R.id.action_home;
            case 1:
                return R.id.action_project;
            case 2:
                return R.id.action_chat;
            case 3:
                return R.id.action_notification;
            default:
                return R.id.action_profile;

        }
    }

    //First connection to server getting user details
    private void connectToServer() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //checks if authentication is successful or not
                if (response.isSuccessful()) {

                    final String jsonResponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("Response", jsonResponse);
                        }
                    });

                    if (jsonResponse.equals(ProjectMateAPIContract.AUTHENTICATION_FAILED)) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRotateLoading.stop();
                            }
                        });
                        Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Gson gson = new Gson();
                        User user = gson.fromJson(jsonResponse, User.class);

                        StaticValues.setCurrentUser(user);

                        if (user.getSkills() == null || user.getSkills().size() == 0) {
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        } else {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String[] arraySkill = getResources().getStringArray(R.array.skillsArray);
                                    StaticValues.setAllSkills(new ArrayList<>(Arrays.asList(arraySkill)));
                                    mMainPagerAdapter = new MainFragmentsAdapter(getSupportFragmentManager());
                                    mViewPager.setAdapter(mMainPagerAdapter);

                                }
                            });

                        }
                    }
                }
            }
        };

        String authUrl = ProjectMateUris.getAuthUrl();
        OkHttpRequests httpRequests = new OkHttpRequests();
        httpRequests.performGetRequest(authUrl, callback, StaticValues.getCodeChefAuthKey());
    }


}

