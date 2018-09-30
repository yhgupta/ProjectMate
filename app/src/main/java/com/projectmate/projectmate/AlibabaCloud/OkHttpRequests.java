package com.projectmate.projectmate.AlibabaCloud;

import android.util.Log;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * All the different request performed by the app
 */

public class OkHttpRequests {

    private final OkHttpClient client = new OkHttpClient();

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");


    //performs the get request
    public void performGetRequest(String url, Callback callback, String authToken) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authToken)
                .build();

        client.newCall(request).enqueue(callback);

    }

    //performs the put request
    public void performPutRequest(String url, String postdata, Callback callback, String authToken) {

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata);

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authToken)
                .build();

        client.newCall(request).enqueue(callback);
    }

    //performs the post request
    public void performPostRequestCodeChef(String url, String postdata, Callback callback) {

        Log.v("POST", postdata);
        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata);


        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(callback);
    }

}
