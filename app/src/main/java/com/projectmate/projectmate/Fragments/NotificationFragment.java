package com.projectmate.projectmate.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projectmate.projectmate.Adapters.ActivitiesAdapter;
import com.projectmate.projectmate.Adapters.RecyclerViewClickListener;
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateAPIContract;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.Classes.Activity;
import com.projectmate.projectmate.Classes.Message;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Database.StaticValues;
import com.projectmate.projectmate.R;

import java.io.IOException;
import java.util.ArrayList;

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

                if(response.isSuccessful()){
                    String jsonData = response.body().string();
                    Gson gson = new Gson();

                    TypeToken<ArrayList<Activity>> token = new TypeToken<ArrayList<Activity>>() {
                    };
                    final ArrayList<Activity> activities = gson.fromJson(jsonData, token.getType());


                    if(!activities.isEmpty()) mActivities.addAll(activities);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivitiesAdapter.notifyDataSetChanged();
                        }
                    });


                }
            }
        };

        mItemClickListerner = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        };

        mActivitiesAdapter = new ActivitiesAdapter(mActivities, mItemClickListerner);

        recyclerView.setAdapter(mActivitiesAdapter);

        OkHttpRequests requests = new OkHttpRequests();
        String url = ProjectMateUris.GetActivities(0);
        requests.performGetRequest(url, mCallback, StaticValues.getCodeChefAuthKey());

        return recyclerView;
    }

}
