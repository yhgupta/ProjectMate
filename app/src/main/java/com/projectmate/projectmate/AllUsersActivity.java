package com.projectmate.projectmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projectmate.projectmate.Adapters.AllUsersAdapter;
import com.projectmate.projectmate.Adapters.OnLoadMoreListener;
import com.projectmate.projectmate.Adapters.RecyclerViewClickListener;
import com.projectmate.projectmate.Adapters.UserAdapter;
import com.projectmate.projectmate.AlibabaCloud.OkHttpRequests;
import com.projectmate.projectmate.AlibabaCloud.ProjectMateUris;
import com.projectmate.projectmate.Classes.AllUserItem;
import com.projectmate.projectmate.Classes.Project;
import com.projectmate.projectmate.Database.StaticValues;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AllUsersActivity extends AppCompatActivity {

    private ArrayList<AllUserItem> mAllUsers;
    private RecyclerView mMainView;

    private Callback mCallback;
    private RecyclerViewClickListener mListener;
    private OnLoadMoreListener mLoadMoreListener;

    private AllUsersAdapter mAdapter;

    private int mProjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mProjectId = getIntent().getIntExtra("PROJECT_ID", 0);

        mAllUsers = new ArrayList<>();

        mMainView = findViewById(R.id.activity_all_users_rv);
        mMainView.setHasFixedSize(true);
        mMainView.setNestedScrollingEnabled(false);

        mMainView.setLayoutManager(new LinearLayoutManager(this));

        mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    mAllUsers.remove(mAllUsers.size()-1);
                    String jsonData = response.body().string();

                    Gson gson = new Gson();

                    TypeToken<ArrayList<AllUserItem>> token = new TypeToken<ArrayList<AllUserItem>>() {};
                    final ArrayList<AllUserItem> allUsers = gson.fromJson(jsonData, token.getType());

                    if(!allUsers.isEmpty()) mAllUsers.addAll(allUsers);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setLoaded();
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        };

        mListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                int userId = mAllUsers.get(position).getId();
                Intent intent = new Intent(AllUsersActivity.this, DisplayUserActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        };

        mLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        };

        mAdapter = new AllUsersAdapter(mAllUsers, mMainView, this, mListener, mLoadMoreListener);
        mMainView.setAdapter(mAdapter);

        loadMore();
    }


    private void loadMore(){
        OkHttpRequests requests = new OkHttpRequests();
        String url = ProjectMateUris.getUsersForProject(mProjectId, mAllUsers.size());

        mAllUsers.add(null);
        mAdapter.notifyDataSetChanged();
        requests.performGetRequest(url, mCallback, StaticValues.getCodeChefAuthKey());
    }
}
