package com.projectmate.projectmate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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



    private TextView mHome;
    private TextView mProject;
    private TextView mNotification;
    private TextView mChat;
    private TextView mProfile;

    /*private ImageView mHome;
    private ImageView mProject;
    private ImageView mNotification;
    private ImageView mChat;
    private ImageView mProfile;*/


   /* private LinearLayout mHomeButton;
    private LinearLayout mMyProjectButton;
    private LinearLayout mChatsButton;
    private LinearLayout mNotificationButton;
    private LinearLayout mProfileButton;*/


    private RotateLoading mRotateLoading;

    private FragmentPagerAdapter mMainPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHome = findViewById(R.id.text_home);
        mChat = findViewById(R.id.text_chat);
        mProfile = findViewById(R.id.text_profile);
        mProject = findViewById(R.id.text_project);
        mNotification = findViewById(R.id.text_notification);


        //Finding all buttons
        /*mHomeButton = findViewById(R.id.home_button);
        mMyProjectButton = findViewById(R.id.project_button);
        mChatsButton = findViewById(R.id.chat_button);
        mNotificationButton = findViewById(R.id.notification_button);
        mProfileButton = findViewById(R.id.profile_button)*/;

        mRotateLoading = findViewById(R.id.rotateloading);

        mRotateLoading.start();

        //mViewPager = findViewById(R.id.main)
        mViewPager = findViewById(R.id.main_view_pager);

        /*mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });*/

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });


        /*mMyProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });*/

        mProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });

        /*mChatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
            }
        });*/

        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
            }
        });


        /*mNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(3);
            }
        });*/

        mNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(3);
            }
        });

        /*mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(4);
            }
        });*/

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(4);
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

