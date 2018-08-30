package com.projectmate.projectmate.CodeChefAPI;

/*
* This is a contract class for interacting with the CodeChef API
*/

import android.net.Uri;

public class APIContract {
    private static final String CLIENT_ID = "ce9c5200940495422025bf247128d9a5";
    public static final String REDIRECT_URI = "r://auth";

    public static String getCodeChefAuthUrl(){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.codechef.com")
                .appendPath("oauth")
                .appendPath("authorize")
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("state", "xyz")
                .appendQueryParameter("redirect_uri", REDIRECT_URI)
                .fragment("section-name");

        return builder.build().toString();
    }
}
