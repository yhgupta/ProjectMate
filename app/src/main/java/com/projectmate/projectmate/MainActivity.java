package com.projectmate.projectmate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.autofill.AutofillValue;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.graphics.Color.*;

public class MainActivity extends AppCompatActivity {



    private RotateLoading mRotateLoading;

    private FragmentPagerAdapter mMainPagerAdapter;
    private ViewPager mViewPager;

    private BottomNavigationView mBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mRotateLoading = findViewById(R.id.rotateloading);

        mRotateLoading.start();

        //mViewPager = findViewById(R.id.main)
        mViewPager = findViewById(R.id.main_view_pager);

        mBottomNavigation = findViewById(R.id.bottom_navigation);

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




        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
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


        Log.v("USERCODE", userCode);

        StaticValues.setCodeChefAuthKey(userCode);

        boolean isFirstTime = prefs.getBoolean(DatabaseContract.FIRST_TIME_KEY, true);

        userFirstTime();




    }

    private int getItemID(int index){
        switch (index){
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

    private void userFirstTime() {
        /*final AlertDialog fetchingDetailsDialog = getFetchingDetailsDialog();
        fetchingDetailsDialog.show();*/
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    final String jsonResponse = response.body().string();

                    if (jsonResponse.equals(ProjectMateAPIContract.AUTHENTICATION_FAILED)){
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRotateLoading.stop();
                            }
                        });
                        Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Gson gson = new Gson();
                        User user = gson.fromJson(jsonResponse, User.class);

                        StaticValues.setCurrentUser(user);
                        if (user.getSkills().size() == 0) {
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }else{
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
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

    private void startAnimation(){
        final FrameLayout frameLayout = findViewById(R.id.main_frame_layout);
        //final RelativeLayout relativeLayout = findViewById(R.id.main_layout);
        final LinearLayout linearLayout = findViewById(R.id.main_layout);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(500);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mRotateLoading.stop();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                frameLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(500);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //relativeLayout.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        frameLayout.startAnimation(fadeOut);
        //relativeLayout.startAnimation(fadeIn);
        linearLayout.startAnimation(fadeIn);
    }

}

