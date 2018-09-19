package com.projectmate.projectmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private int user_id = 1;

    Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if(response.isSuccessful()){

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


    }
}
