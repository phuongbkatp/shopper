package com.mcc.fshopper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.R;

/**
 * Created by Nasir on 8/29/2016.
 */
public class WebPageActivity extends BaseActivity {

    private Context mContext;
    private WebView webView;

    private String pageTitle = AppConstants.EMPTY_STRING, url = AppConstants.EMPTY_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariable();
        initView();
        initToolbar();
        initFunctionality();
    }

    private void initVariable() {
        mContext = getApplicationContext();

        Intent intent = getIntent();
        if(intent.hasExtra(AppConstants.BUNDLE_KEY_TITLE)) {
            pageTitle = intent.getStringExtra(AppConstants.BUNDLE_KEY_TITLE);
        }
        if(intent.hasExtra(AppConstants.BUNDLE_KEY_URL)) {
            url = intent.getStringExtra(AppConstants.BUNDLE_KEY_URL);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_webpage);
        webView = (WebView) findViewById(R.id.webView);

        initToolbar();
        enableBackButton();
        setToolbarTitle(pageTitle);
        initLoader();
    }


    private void initFunctionality() {

        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String webUrl) {

                webView.loadUrl(webUrl);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showLoader();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideLoader();
            }

        });

        // load web view
        webView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
