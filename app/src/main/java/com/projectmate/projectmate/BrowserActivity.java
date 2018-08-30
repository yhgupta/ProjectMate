package com.projectmate.projectmate;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.projectmate.projectmate.CodeChefAPI.APIContract;
import com.projectmate.projectmate.Database.DatabaseContract;


public class BrowserActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("CodeChef Login");
        toolbar.setElevation(4);

        webView = findViewById(R.id.webview);
        mProgressBar = findViewById(R.id.progressBar);

        webView.setWebViewClient(new WebViewClientCodeChef());
        webView.setWebChromeClient(new WebChromeClientCodeChef());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Android");
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        mProgressBar.setMax(100 * 100);
        mProgressBar.setProgress(0);

        webView.loadUrl(APIContract.getCodeChefAuthUrl());

    }

    private class WebViewClientCodeChef extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //Check if CodeChef has redirected
            if (url.startsWith(APIContract.REDIRECT_URI)) {

                //Url start is now the start of code
                url = url.substring(APIContract.REDIRECT_URI.length() + "?code=".length());

                String code = url.substring(0, url.indexOf('&') - 1);

                //Save the obtained code in local database as SharedPreference
                SharedPreferences.Editor editor = getSharedPreferences(DatabaseContract.SHARED_PREFS, Context.MODE_PRIVATE).edit();
                editor.putString(DatabaseContract.AUTH_CODE_KEY, code);
                editor.apply();

                Intent intent = new Intent(BrowserActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            //Normally load the url if its other than the Auth Code
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", mProgressBar.getProgress(), 100 * 100);
            animation.setDuration(300);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
            AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
            anim.setDuration(300);
            mProgressBar.startAnimation(anim);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(0);
        }
    }

    private class WebChromeClientCodeChef extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {

            ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", mProgressBar.getProgress(), progress * 100);
            animation.setDuration(300);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();

        }
    }
}
