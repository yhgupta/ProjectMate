/*
* This Activity will be shown to user if he is not logged in.
* This Activity is launched from MainActivity if no user is found
* It will open a API request to CodeChef for user Login
*/

package com.projectmate.projectmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    private CardView getStartedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getStartedView = findViewById(R.id.btn_get_started);

        getStartedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, BrowserActivity.class);
                startActivity(intent);
            }
        });
    }
}
