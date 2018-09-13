package com.projectmate.projectmate.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.projectmate.projectmate.Adapters.SkillAdapter;
import com.projectmate.projectmate.Classes.Skill;
import com.projectmate.projectmate.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProjectFragment extends Fragment {

    private FloatingActionButton mFab;
    private TextView mAddProject;

    public MyProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_my_project, container, false);
        rootView.findViewById(R.id.fab);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Opening",Toast.LENGTH_LONG).show();
            }
        });
        
        return rootView;
    }




}
