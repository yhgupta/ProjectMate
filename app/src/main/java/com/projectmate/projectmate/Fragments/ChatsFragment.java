package com.projectmate.projectmate.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectmate.projectmate.Adapters.ChatAdapter;
import com.projectmate.projectmate.Classes.Chat;
import com.projectmate.projectmate.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);
        // Inflate the layout for this fragment

        ArrayList<Chat> chat = new ArrayList<>();
        chat.add(new Chat("Yash","Kill Him") );
        chat.add(new Chat("Fox","Kill Me") );
        chat.add(new Chat("Fox","Kill Me") );
        chat.add(new Chat("Fox","Kill Me") );
        chat.add(new Chat("Fox","Kill Me") );

        ChatAdapter adapter = new ChatAdapter(chat );
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = rootView.findViewById(R.id.chat_fragment_rv);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

}
