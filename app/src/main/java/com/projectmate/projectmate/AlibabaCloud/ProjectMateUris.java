package com.projectmate.projectmate.AlibabaCloud;

import android.net.Uri;

public class ProjectMateUris {

    public static String getAuthUrl(){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(ProjectMateAPIContract.SERVER_URL)
                .appendPath(ProjectMateAPIContract.API_PATH)
        .appendPath(ProjectMateAPIContract.USERS_PATH)
        .appendPath(ProjectMateAPIContract.CURRENT_USER_PATH);

        return builder.build().toString();
    }
}
