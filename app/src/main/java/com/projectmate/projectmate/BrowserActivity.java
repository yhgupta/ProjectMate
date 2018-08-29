package com.projectmate.projectmate;

import android.animation.ObjectAnimator;
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

        progressBar.setMax(100*100);
        progressBar.setProgress(0);

        webView.loadUrl("http://www.concretepage.com");

    }

    private class WebViewClientDemo extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
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
            animation.setDuration(500);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
        }
    }
}
