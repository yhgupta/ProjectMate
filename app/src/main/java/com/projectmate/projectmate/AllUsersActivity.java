package com.projectmate.projectmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.projectmate.projectmate.Classes.AllUserItem;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private ArrayList<AllUserItem> allUserItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        linearLayout = findViewById(R.id.all_user_display_activity);
    }
    

}
