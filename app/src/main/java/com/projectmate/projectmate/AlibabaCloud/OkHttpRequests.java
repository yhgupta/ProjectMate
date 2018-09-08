package com.projectmate.projectmate.AlibabaCloud;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpRequests {
    private final OkHttpClient client = new OkHttpClient();

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");



    public void performGetRequest(String url, Callback callback, String authToken){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authToken)
                .build();

        client.newCall(request).enqueue(callback);

    }

    public void performPostRequest(String url, JSONObject postdata, Callback callback, String authToken){

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authToken)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void performPostRequestCodeChef(String url, String postdata, Callback callback){

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
