package com.projectmate.projectmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.projectmate.projectmate.Adapters.ChatAdapter;
import com.projectmate.projectmate.Classes.Chat;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    //TODO CHANGE THIS FROM TESTACTIVIY TO MAIN

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ArrayList<Chat> chat = new ArrayList<>();
        chat.add(new Chat("Yash","Kill Him") );
        chat.add(new Chat("Fox","Kill Me") );
        chat.add(new Chat("Fox","Kill Me") );
        chat.add(new Chat("Fox","Kill Me") );
        chat.add(new Chat("Fox","Kill Me") );

        ChatAdapter adapter = new ChatAdapter(chat);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.test_rv);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }
}
