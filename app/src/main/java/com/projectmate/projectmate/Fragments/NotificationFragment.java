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
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projectmate.projectmate.Adapters.ActivitiesAdapter;
import com.projectmate.projectmate.Adapters.RecyclerViewClickListener;
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateAPIContract;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.ChatActivity;
import com.projectmate.projectmate.Classes.Activity;
import com.projectmate.projectmate.Database.StaticValues;
import com.projectmate.projectmate.DisplayProjectActivity;
import com.projectmate.projectmate.DisplayUserActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private ArrayList<Activity> mActivities;

    private Callback mCallback;

    private ActivitiesAdapter mActivitiesAdapter;

    private RecyclerViewClickListener mItemClickListerner;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);


        mActivities = new ArrayList<>();

        mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    final String jsonData = response.body().string();

                    Gson gson = new Gson();

                    TypeToken<ArrayList<Activity>> token = new TypeToken<ArrayList<Activity>>() {
                    };
                    final ArrayList<Activity> activities = gson.fromJson(jsonData, token.getType());


                    if (!activities.isEmpty()) mActivities.addAll(activities);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("activity", jsonData);
                            mActivitiesAdapter.notifyDataSetChanged();
                        }
                    });


                }
            }
        };

        mItemClickListerner = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (view instanceof Button) {
                    if (((Button) view).getText().toString().equals("ACCEPT")) {
                        replyToActivity(position, ProjectMateAPIContract.ACCEPT_CODE);
                    } else if (((Button) view).getText().toString().equals("REJECT")) {
                        replyToActivity(position, ProjectMateAPIContract.REJECT_CODE);
                    } else {
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        int my_id = StaticValues.getCurrentUser().getId();

                        Activity activity = mActivities.get(position);
                        int user_id = my_id == activity.getSender().getId()
                                ? activity.getReceiver().getId()
                                : activity.getSender().getId();
                        intent.putExtra("USER_ID", user_id);
                        startActivity(intent);
                    }
                } else {
                    if ((boolean) (view.getTag(100))) {
                        Intent intent = new Intent(getContext(), DisplayUserActivity.class);

                        int my_id = StaticValues.getCurrentUser().getId();

                        Activity activity = mActivities.get(position);

                        int user_id = my_id == activity.getSender().getId()
                                ? activity.getReceiver().getId()
                                : activity.getSender().getId();
                        int proj_id = activity.getProject().getId();

                        intent.putExtra("USER_ID", user_id);
                        intent.putExtra("PROJECT_ID", proj_id);
                        intent.putExtra("SHOW_SAVE", false);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), DisplayProjectActivity.class);
                        int proj_id = mActivities.get(position).getProject().getId();
                        intent.putExtra("PROJECT_ID", proj_id);
                        intent.putExtra("SHOW_SAVE", false);
                        startActivity(intent);
                    }

                }
            }
        };

        mActivitiesAdapter = new ActivitiesAdapter(mActivities, mItemClickListerner);

        recyclerView.setAdapter(mActivitiesAdapter);

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                reload();
            }
        };

        timer.schedule(timerTask, 0, 10000);

        return recyclerView;
    }


    private void reload() {
        mActivities.clear();
        OkHttpRequests requests = new OkHttpRequests();
        String url = ProjectMateUris.GetActivities(0);
        requests.performGetRequest(url, mCallback, StaticValues.getCodeChefAuthKey());
    }


    private void replyToActivity(int position, int reply) {
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    reload();
                }
            }
        };

        OkHttpRequests requests = new OkHttpRequests();
        String url = ProjectMateUris.ReplyToActivity(mActivities.get(position).getId(), reply);
        requests.performGetRequest(url, callback, StaticValues.getCodeChefAuthKey());

    }

}
