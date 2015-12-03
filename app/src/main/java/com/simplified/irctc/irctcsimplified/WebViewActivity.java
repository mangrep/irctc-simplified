package com.simplified.irctc.irctcsimplified;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
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
    String otp = "";

    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).
                registerReceiver(new SMSBroadCastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        final Bundle bundle = intent.getExtras();
                        try {

                            if (bundle != null) {

                                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                                for (int i = 0; i < pdusObj.length; i++) {

                                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                                    String senderNum = phoneNumber;
                                    String message = currentMessage.getDisplayMessageBody();


                                    String[] tokens = message.split(":");

                                    try {
                                        Log.d(TAG, tokens.toString());
                                        otp = tokens[1].trim();
                                    } catch (IndexOutOfBoundsException e) {
                                        Log.e(TAG, "Unable to get otp " + e);
                                    } catch (NullPointerException e) {
                                        Log.e(TAG, "unable to split msg " + e);
                                    } catch (Exception e) {
                                        Log.e(TAG, e + "");
                                    }
                                    Log.i(TAG, "senderNum: " + senderNum + "; message: " + message + " otp is " + otp);
                                    // Show Alert
                                    Snackbar.make(coordinatorLayout, "OTP is " + otp, Snackbar.LENGTH_INDEFINITE).setAction("Confirm", null).show();

                                } // end for loop
                            } // bundle is null

                        } catch (Exception e) {
                            Log.e(TAG, "Exception smsReceiver" + e);

                        }
                    }
                }, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        Bundle userData = getIntent().getExtras();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.webview_activity);
//        otpwebviewBroadCastReceiver =
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
        mView.getSettings().setDomStorageEnabled(true);
        mView.getSettings().setSupportMultipleWindows(true);
        mView.addJavascriptInterface(WebViewActivity.this, "manish");
        mView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.d(TAG, "newProgress " + newProgress + " ");
//                if (Constants.login_url.equalsIgnoreCase(url)) {
//                mView.loadUrl("javascript: (function(username, password) {\n" +
//                        "    document.querySelector(\"input[name=j_username]\").value = username , document.querySelector(\"input[name=j_password]\").value = password\n" +
//                        "})('" + username + "', '" + passWord + "') ");
//                mView.loadUrl("javascript:document.querySelector('input[name=otp]').click()");
//                mView.loadUrl("javascript:document.querySelector('input[name=submit]').click()");
//                }
            }
        });
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
                mView.loadUrl("javascript:" + "/" + "www.irctc.co.in" + "/.test(location.host)?manish.init():manish.init()");
//                if (Constants.login_url.equalsIgnoreCase(url)) {
//                    mView.loadUrl("javascript: (function(username, password) {\n" +
//                            "    document.querySelector(\"input[name=j_username]\").value = username , document.querySelector(\"input[name=j_password]\").value = password\n" +
//                            "})('" + username + "', '" + passWord + "') ");
//                    mView.loadUrl("javascript:document.querySelector('input[name=otp]').click()");
//                    mView.loadUrl("javascript:document.querySelector('input[name=submit]').click()");
//                } else if (Constants.otp_page_url.equalsIgnoreCase(url)) {
//                    Log.d(TAG, "setting otp");
//                    mView.loadUrl("javascript:(function(a){\n" +
//                            "document.querySelector('input[name=loginotp]').value=a,\n" +
//                            "document.querySelector('input[type=submit]').click()})('" + otp + "');");
//                }

//                mView.loadUrl("javascript:$('input[name=\'j_username\']').val('arjun');");
//                mView.loadUrl("javascript:document.querySelector('input[name=j_password]').value ='" + passWord + "'");
//                mView.loadUrl("javascript:document.querySelector('input[name=otp]').click()");
            }
        });

        mView.loadUrl(Constants.login_url);

    }

    @JavascriptInterface
    public void init() {
        Log.d(TAG, " say hello");
    }

    @JavascriptInterface
    void nativeHelperForNB() {

    }
}
