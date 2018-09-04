package com.projectmate.projectmate.AlibabaCloud;

import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpRequests {
    private String authToken;
    private final OkHttpClient client = new OkHttpClient();

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");


    public OkHttpRequests(String authToken) {
        this.authToken = authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    public void performGetRequest(String url, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", this.authToken)
                .build();

        client.newCall(request).enqueue(callback);

    }

    public void performPostRequest(String url, JSONObject postdata, Callback callback){

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", this.authToken)
                .build();

        client.newCall(request).enqueue(callback);
    }

}
