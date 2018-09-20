package com.projectmate.projectmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projectmate.projectmate.Adapters.MessageAdapter;
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.Classes.Message;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Database.StaticValues;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private int mUserId;

    private ArrayList<Message> mMessages;

    private MessageAdapter mMessageAdapter;

    private RecyclerView mRecyclerView;
    private EditText mMessageText;
    private ImageButton mSendBtn;

    //Callback of response from server
    private Callback mCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mUserId = getIntent().getIntExtra("USER_ID", 0);

        mRecyclerView = findViewById(R.id.activity_chats_rv);
        mMessageText = findViewById(R.id.activity_chats_et);
        mSendBtn = findViewById(R.id.activity_chats_btn_send);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);

        mRecyclerView.setLayoutManager(manager);
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

                    TypeToken<ArrayList<Message>> token = new TypeToken<ArrayList<Message>>() {};
                    ArrayList<Message> messages = gson.fromJson(jsonData, token.getType());

                    mMessages.addAll(messages);

                    ChatActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMessageAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        };

        mMessageAdapter = new MessageAdapter(mMessages);

        mRecyclerView.setAdapter(mMessageAdapter);

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                reload();
            }
        };

        timer.schedule(timerTask, 0, 5000);

    }

    private void reload(){
        mMessages.clear();
        OkHttpRequests requests = new OkHttpRequests();
        String url = ProjectMateUris.GetChat(mUserId, 0);
        requests.performGetRequest(url, mCallback, StaticValues.getCodeChefAuthKey());
    }

    private void sendMessage(){
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                reload();
            }
        };
        String message = mMessageText.getText().toString().trim();
        if(!message.isEmpty()){
            mMessageText.setText("");
            OkHttpRequests requests = new OkHttpRequests();
            String url = ProjectMateUris.GetChat(mUserId, 0);
            String data = "{\"message\":\""+message+"\"}";

            requests.performPutRequest(url, data, callback, StaticValues.getCodeChefAuthKey());
        }
    }
}
