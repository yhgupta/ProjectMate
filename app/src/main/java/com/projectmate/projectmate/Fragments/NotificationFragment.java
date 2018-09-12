package com.projectmate.projectmate.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.projectmate.projectmate.Adapters.AllProjectAdapter;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private AllProjectAdapter mAdapter;
    private ArrayList<Project> mProjects = new ArrayList<>();

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new AllProjectAdapter(mProjects);

        recyclerView.setAdapter(mAdapter);


        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

}
