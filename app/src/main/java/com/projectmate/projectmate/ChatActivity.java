package com.projectmate.projectmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projectmate.projectmate.Classes.Message;
import com.projectmate.projectmate.Classes.Project;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private int user_id = 1;

    private ArrayList<Message> mMessages;

    //Callback of response from server
    Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if(response.isSuccessful()){
                String jsonData = response.body().string();

                Gson gson = new Gson();

                TypeToken<ArrayList<Message>> token = new TypeToken<ArrayList<Message>>() {};
                ArrayList<Message> messages = gson.fromJson(jsonData, token.getType());


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


    }
}
