/*
* This Activity will be shown to user if he is not logged in.
* This Activity is launched from MainActivity if no user is found
* It will open a API request to CodeChef for user Login
*/

package com.projectmate.projectmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }
}
