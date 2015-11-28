package com.simplified.irctc.irctcsimplified;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by turing on 28/11/15.
 */
public class WebViewActivity extends AppCompatActivity {
    private final String TAG = WebViewActivity.class.getName();
    WebView mView;
    String username = "";
    String passWord = "";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        Bundle userData = getIntent().getExtras();

        if (userData != null) {
            username = userData.getString(Constants.USERNAME);
            passWord = userData.getString(Constants.PASSWORD);
        }

        Log.d(TAG, "usernmae is  " + username + " password is " + passWord);
        mView = (WebView) findViewById(R.id.webView);
        mView.getSettings().setLoadWithOverviewMode(true);
        mView.getSettings().setJavaScriptEnabled(true);
        mView.getSettings().setUseWideViewPort(false);
        mView.getSettings().setBuiltInZoomControls(true);

        mView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e(TAG, "Url " + request.toString() + " response " + error.toString() + "");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "onPageStarted Loading " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "page finished loading " + url);
                mView.loadUrl("javascript: (function(username, password) {\n" +
                        "    document.querySelector(\"input[name=j_username]\").value = username , document.querySelector(\"input[name=j_password]\").value = password\n" +
                        "})('" + username + "', '" + passWord + "') ");
//                mView.loadUrl("javascript:$('input[name=\'j_username\']').val('arjun');");
//                mView.loadUrl("javascript:document.querySelector('input[name=j_password]').value ='" + passWord + "'");
//                mView.loadUrl("javascript:document.querySelector('input[name=otp]').click()");
            }

        });

        mView.loadUrl(Constants.login_url);

    }
}
