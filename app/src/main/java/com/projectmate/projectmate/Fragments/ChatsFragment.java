package com.projectmate.projectmate.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projectmate.projectmate.Adapters.ChatAdapter;
import com.projectmate.projectmate.Adapters.OnLoadMoreListener;
import com.projectmate.projectmate.Adapters.RecyclerViewClickListener;
import com.projectmate.projectmate.Classes.Chat;
import com.projectmate.projectmate.Classes.Message;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.R;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;



public class ChatsFragment extends Fragment {

    private ArrayList<Message> mMessages;
    private ChatAdapter mAdapter;

    private Callback mCallback;

    private RecyclerViewClickListener mItemClickListener;
    private OnLoadMoreListener mLoadMoreListener;



    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.chat_fragment_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);


        mMessages = new ArrayList<>();

        mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonData = response.body().string();
                    Gson gson = new Gson();

                    TypeToken<ArrayList<Message>> token = new TypeToken<ArrayList<Message>>() {
                    };
                    final ArrayList<Message> messages = gson.fromJson(jsonData, token.getType());

                    mMessages.remove(mMessages.size() - 1);
                    mAdapter.setLoaded();

                    if(!messages.isEmpty()) mMessages.addAll(messages);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            }
        };


        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

}
