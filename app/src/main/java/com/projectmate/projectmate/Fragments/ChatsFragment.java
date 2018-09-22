package com.projectmate.projectmate.Fragments;


import android.content.Intent;
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
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.ChatActivity;
import com.projectmate.projectmate.Classes.Chat;
import com.projectmate.projectmate.Classes.Message;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Database.StaticValues;
import com.projectmate.projectmate.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;



public class ChatsFragment extends Fragment {

    private ArrayList<Message> mMessages;
    private ChatAdapter mAdapter;

    private Callback mCallback;

    private RecyclerViewClickListener mItemClickListener;


    public ChatsFragment() {
        // Required empty public constructor
    }

    //inflating the fragments_chats layout to display the details
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);

        //recyclerview to display chat
        RecyclerView recyclerView = rootView.findViewById(R.id.chat_fragment_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);


        mMessages = new ArrayList<>();

        mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonData = response.body().string();
                    Gson gson = new Gson();

                    TypeToken<ArrayList<Message>> token = new TypeToken<ArrayList<Message>>() {
                    };
                    final ArrayList<Message> messages = gson.fromJson(jsonData, token.getType());

                    if(!messages.isEmpty()) mMessages.addAll(messages);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        };

        mItemClickListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                int myId = StaticValues.getCurrentUser().getId();
                int otherId = mMessages.get(position).getSender().getId();
                if(otherId == myId)
                    otherId = mMessages.get(position).getReceiver().getId();

                intent.putExtra("USER_ID", otherId);

                startActivity(intent);
            }
        };

        mAdapter = new ChatAdapter(mMessages, mItemClickListener, StaticValues.getCurrentUser().getId());

        recyclerView.setAdapter(mAdapter);
        

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                reload();
            }
        };

        timer.schedule(timerTask, 0, 10000);


        return rootView;
    }

    private void reload(){
        mMessages.clear();
        OkHttpRequests requests = new OkHttpRequests();
        String url = ProjectMateUris.GetUserChats();
        requests.performGetRequest(url, mCallback, StaticValues.getCodeChefAuthKey());
    }
}
