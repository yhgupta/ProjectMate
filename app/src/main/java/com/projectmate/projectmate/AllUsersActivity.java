package com.projectmate.projectmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.projectmate.projectmate.Adapters.AllUsersAdapter;
import com.projectmate.projectmate.Adapters.OnLoadMoreListener;
import com.projectmate.projectmate.Adapters.RecyclerViewClickListener;
import com.projectmate.projectmate.Adapters.UserAdapter;
import com.projectmate.projectmate.Classes.AllUserItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

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
                    String jsonData = response.body().string();


                }
            }
        };

        mListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        };

        mLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        };

        mAdapter = new AllUsersAdapter(mAllUsers, mMainView, this, mListener, mLoadMoreListener);
    }
}
