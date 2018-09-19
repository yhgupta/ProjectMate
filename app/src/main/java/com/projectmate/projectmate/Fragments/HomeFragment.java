package com.projectmate.projectmate.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projectmate.projectmate.Adapters.ProjectAdapter;
import com.projectmate.projectmate.Adapters.RecyclerViewClickListener;
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Database.StaticValues;
import com.projectmate.projectmate.R;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    private ProjectAdapter mAdapter;
    private ArrayList<Project> mProjects = new ArrayList<>();

    Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {

                final String jsonData = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("RESPONSE", jsonData);
                        Gson gson = new Gson();
                        TypeToken<ArrayList<Project>> token = new TypeToken<ArrayList<Project>>() {
                        };
                        ArrayList<Project> projects = gson.fromJson(jsonData, token.getType());

                        if(mProjects.isEmpty()&&moreItemsPresent) startAnimation();

                        if(projects.isEmpty()){
                            moreItemsPresent=false;
                            mAdapter.notifyDataSetChanged();
                            return;
                        }

                        mProjects.addAll(projects);
                        loadingFromServer = false;

                        if(projects.size()<15){
                            moreItemsPresent=false;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };

    private boolean loadingFromServer = true;
    private boolean moreItemsPresent = true;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setNestedScrollingEnabled(false);

        mAdapter = new ProjectAdapter(mProjects, getContext(), new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });

        recyclerView.setAdapter(mAdapter);

        if(mProjects.isEmpty()){
            OkHttpRequests requests = new OkHttpRequests();
            requests.performGetRequest(ProjectMateUris.getProjectsForMe(0), callback, StaticValues.getCodeChefAuthKey());
        }
        else if(mProjects.size()<15){
            mAdapter.notifyDataSetChanged();
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastCompletelyVisibleItemPosition = 0;

                lastCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                if(moreItemsPresent){
                    if(lastCompletelyVisibleItemPosition>=mProjects.size()-5){
                        if(!loadingFromServer){
                            OkHttpRequests requests = new OkHttpRequests();
                            requests.performGetRequest(ProjectMateUris.getAllProjects(mProjects.size()), callback, StaticValues.getCodeChefAuthKey());
                        }
                    }
                }

            }
        });


        return recyclerView;
    }

    private void startAnimation(){
        final FrameLayout frameLayout = getActivity().findViewById(R.id.activity_main_frame_layout);
        //final RelativeLayout relativeLayout = findViewById(R.id.activity_main_layout);
        final LinearLayout linearLayout = getActivity().findViewById(R.id.activity_main_layout);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(500);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                RotateLoading loading = getActivity().findViewById(R.id.rotateloading);
                loading.stop();
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
        linearLayout.startAnimation(fadeIn);
    }
}
