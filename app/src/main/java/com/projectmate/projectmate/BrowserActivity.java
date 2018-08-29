package com.projectmate.projectmate;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
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
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("CodeChef Login");
        toolbar.setElevation(4);

        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);

        webView.setWebViewClient(new WebViewClientDemo());
        webView.setWebChromeClient(new WebChromeClientDemo());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Android");
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        progressBar.setMax(100*100);
        progressBar.setProgress(0);

        webView.loadUrl(APIContract.CODECHEF_AUTH_URL);

    }

    private class WebViewClientDemo extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith("r://auth?")){
                url = url.substring(14);
                int codeEndIndex = url.indexOf('&');
                String code = url.substring(0, codeEndIndex-1);
                Log.v("CODECHEF", code);
                SharedPreferences.Editor editor = getSharedPreferences(DatabaseContract.SHARED_PREFS, Context.MODE_PRIVATE).edit();
                editor.putString(DatabaseContract.AUTH_CODE_KEY, code);
                editor.apply();
                Intent intent = new Intent(BrowserActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                //TODO: Close all previously open activities
            }
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), 100 * 100);
            animation.setDuration(500);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
            AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
            anim.setDuration(300);
            progressBar.startAnimation(anim);
            progressBar.setVisibility(View.INVISIBLE);
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }
    }
    private class WebChromeClientDemo extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), progress * 100);
            animation.setDuration(300);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
        }
    }
}
