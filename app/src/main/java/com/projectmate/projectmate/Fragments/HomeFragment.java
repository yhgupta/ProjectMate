package com.projectmate.projectmate.Fragments;


import android.content.Intent;
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
import com.projectmate.projectmate.Adapters.MyProjectAdapter;
import com.projectmate.projectmate.Adapters.OnLoadMoreListener;
import com.projectmate.projectmate.Adapters.RecyclerViewClickListener;
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Database.StaticValues;
import com.projectmate.projectmate.DisplayProjectActivity;
import com.projectmate.projectmate.R;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    //initialization of variables
    private ArrayList<Project> mProjects;
    private MyProjectAdapter mProjectAdapter;

    private Callback mCallback;

    private RecyclerViewClickListener mItemClickListener;
    private OnLoadMoreListener mLoadMoreListener;

    private boolean animationDone = false;
    private boolean moreItemsPresent = true;

    private boolean loading = false;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setting of recyclerView
        final RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);


        mProjects = new ArrayList<>();

        mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String jsonData = response.body().string();
                    Gson gson = new Gson();

                    TypeToken<ArrayList<Project>> token = new TypeToken<ArrayList<Project>>() {
                    };
                    final ArrayList<Project> projects = gson.fromJson(jsonData, token.getType());

                    mProjects.remove(mProjects.size() - 1);
                    mProjectAdapter.setLoaded();

                    moreItemsPresent = true;
                    if (projects.isEmpty()) {
                        moreItemsPresent = false;
                    } else mProjects.addAll(projects);

                    loading = false;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("HOME", jsonData);
                            mProjectAdapter.notifyDataSetChanged();

                            if (!animationDone) {
                                startAnimation();
                                animationDone = true;
                            }
                        }
                    });

                }
            }
        };

        mItemClickListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), DisplayProjectActivity.class);
                intent.putExtra("PROJECT_ID", mProjects.get(position).getId());
                startActivity(intent);
            }
        };

        mLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!moreItemsPresent) {
                    mProjectAdapter.setLoaded();
                    return;
                }
                if (loading) return;
                OkHttpRequests requests = new OkHttpRequests();
                String url = ProjectMateUris.getProjectsForMe(mProjects.size());
                String authId = StaticValues.getCodeChefAuthKey();

                mProjects.add(null);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mProjectAdapter.notifyDataSetChanged();
                    }
                });

                requests.performGetRequest(url, mCallback, authId);
            }
        };

        mProjectAdapter = new MyProjectAdapter(mProjects, recyclerView, getContext(),
                mItemClickListener, mLoadMoreListener, false);

        recyclerView.setAdapter(mProjectAdapter);

        mLoadMoreListener.onLoadMore();

        return recyclerView;
    }

    private void startAnimation() {
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
