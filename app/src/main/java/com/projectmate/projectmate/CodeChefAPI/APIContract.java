package com.projectmate.projectmate.CodeChefAPI;


import android.net.Uri;

/**
 * This is a contract class for interacting with the CodeChef API
 */


public class APIContract {
    public static final String CLIENT_ID = "ce9c5200940495422025bf247128d9a5";
    public static final String CLIENT_SECRET = "8609ebd672a09bccd571ab24c59e7291";
    public static final String REDIRECT_URI = "r://auth";

    public static final String CODECHEF_TOKEN_URL = "https://api.codechef.com/oauth/token/";

    public static String getCodeChefAuthUrl() {
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
